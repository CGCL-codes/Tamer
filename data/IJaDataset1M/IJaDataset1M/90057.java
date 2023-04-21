package org.karatasi.android.playground.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;

public class KaratasiWithFragments extends FragmentActivity {

    TabHost mTabHost;

    TabManager mTabManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    public static class TabManager implements TabHost.OnTabChangeListener {

        @Override
        public void onTabChanged(String arg0) {
        }
    }
}
