package com.cooldroidapps.savemyapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);
        Preference proUpdatePref = findPreference(getString(R.string.pro_update_pref_button_key));
        proUpdatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                installProVersion();
                return true;
            }
        });
    }

    public void installProVersion() {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setData(Uri.parse(getString(R.string.pro_version_url)));
        startActivity(marketIntent);
    }
}
