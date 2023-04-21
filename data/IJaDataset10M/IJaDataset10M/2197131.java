package com.android.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TestingSettings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.testing_settings);
    }
}
