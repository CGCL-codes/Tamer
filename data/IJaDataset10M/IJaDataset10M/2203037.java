package de.tudresden.inf.rn.mobilis.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesClient extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
