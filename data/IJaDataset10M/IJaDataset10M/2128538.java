package com.commandus.meetup;

import com.commandus.meetup.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.CheckBoxPreference;
import android.widget.Toast;

/**
 * Shows finding a preference from the hierarchy and a custom preference type.
 */
public class ViewSettings extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String KEY_ADVANCED_CHECKBOX_PREFERENCE = "advanced_checkbox_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }
}
