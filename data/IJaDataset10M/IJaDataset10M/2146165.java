package org.mentalray.run;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class EditPreferences extends PreferenceActivity {

    private ListPreference _backgroundColorPreference;

    private ListPreference _fontColorPreference;

    private ListPreference _fontStylePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
