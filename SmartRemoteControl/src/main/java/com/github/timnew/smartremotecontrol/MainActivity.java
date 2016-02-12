package com.github.timnew.smartremotecontrol;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity
        extends SherlockFragmentActivity {

    @ViewById
    protected ViewPager pager;
    private PagerActionBarAdapter pagerActionBarAdapter;

    @Bean
    protected InfraredEmitter emitter;

    @AfterViews
    void afterViews() {
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);

        pagerActionBarAdapter = new PagerActionBarAdapter(this, pager);
        pagerActionBarAdapter.notifyDataSetChanged();

        checkIrSupport();
    }

    private void checkIrSupport() {
        //No hardware IR support
        if (emitter == null || !emitter.hasIrEmitter()) {
            finish();
            Intent intent = new Intent(this, DialogHostActivity.class);
            startActivity(intent);
            return;
        }

        /*
        unsigned long SANAKY_FAN_ON_OFF  = 0x1FE807F;     //BT Speaker Down = 0x1FE807F (not affected)     //TV 1 = 0x2FD807F
        unsigned long SANAKY_FAN_SPEED   = 0x1FE50AF;     //BT Speaker Power = 0x1FE50AF
        unsigned long SANAKY_FAN_OSC     = 0x4106897;     //BT Speaker 3 = 0x1FE6897                       //TV Info = 0x2FD6897
        unsigned long SANAKY_FAN_MIST    = 0x2FD906F;     //BT Speaker BT = 0x1FE906F (not working)        //TV 9 = 0x2FD906F
        unsigned long SANAKY_TIMER       = 0x1FE00FF;     //BT Speaker Up = 0x1FE00FF
        unsigned long SANAKY_ION         = 0x1FEA857;     //BT Speaker 2 = 0x1FEA857
        unsigned long SANAKY_MODE        = 0x1FE30CF;     //BT Speaker Mute = 0x1FE30CF

        unsigned long BT_SPEAKER_POWER     = 0x1FE50AF;
        unsigned long BT_SPEAKER_BT        = 0x1FE906F;     //not working
        unsigned long BT_SPEAKER_AUX       = 0x1FE8877;
        unsigned long BT_SPEAKER_VOL_UP    = 0x1FE40BF;
        unsigned long BT_SPEAKER_VOL_DOWN  = 0x1FEC03F;
        unsigned long BT_SPEAKER_LIGHT     = 0x1FE38C7;
         */

        //Test send code
        emitter.NEC(32, 0x2FD58A7);
        emitter.NEC(32, 0x1FE50AF);
        emitter.NEC(32, 0x1FE40BF);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater supportMenuInflater = getSupportMenuInflater();

        supportMenuInflater.inflate(R.menu.main_activity, menu);

        return true;
    }

    @OptionsItem(R.id.menu_add_remote_panel)
    protected void showAddRemotePanel() {
        pagerActionBarAdapter.registerRemotePanel("debug", "http://192.168.1.6:4000/panels/tv%20box/index.html#box");
        pagerActionBarAdapter.notifyDataSetChanged();
    }
}
