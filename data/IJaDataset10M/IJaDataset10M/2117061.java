package com.android.browser;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class BrowserPreferencesPage extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private String LOGTAG = "BrowserPreferencesPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.browser_preferences);
        Preference e = findPreference(BrowserSettings.PREF_HOMEPAGE);
        e.setOnPreferenceChangeListener(this);
        e.setSummary(getPreferenceScreen().getSharedPreferences().getString(BrowserSettings.PREF_HOMEPAGE, null));
        e = findPreference(BrowserSettings.PREF_EXTRAS_RESET_DEFAULTS);
        e.setOnPreferenceChangeListener(this);
        e = findPreference(BrowserSettings.PREF_TEXT_SIZE);
        e.setOnPreferenceChangeListener(this);
        e.setSummary(getVisualTextSizeName(getPreferenceScreen().getSharedPreferences().getString(BrowserSettings.PREF_TEXT_SIZE, null)));
        e = findPreference(BrowserSettings.PREF_DEFAULT_ZOOM);
        e.setOnPreferenceChangeListener(this);
        e.setSummary(getVisualDefaultZoomName(getPreferenceScreen().getSharedPreferences().getString(BrowserSettings.PREF_DEFAULT_ZOOM, null)));
        e = findPreference(BrowserSettings.PREF_DEFAULT_TEXT_ENCODING);
        e.setOnPreferenceChangeListener(this);
        if (BrowserSettings.getInstance().showDebugSettings()) {
            addPreferencesFromResource(R.xml.debug_preferences);
        }
        PreferenceScreen websiteSettings = (PreferenceScreen) findPreference(BrowserSettings.PREF_WEBSITE_SETTINGS);
        Intent intent = new Intent(this, WebsiteSettingsActivity.class);
        websiteSettings.setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final PreferenceScreen websiteSettings = (PreferenceScreen) findPreference(BrowserSettings.PREF_WEBSITE_SETTINGS);
        websiteSettings.setEnabled(false);
        WebStorage.getInstance().getOrigins(new ValueCallback<Map>() {

            public void onReceiveValue(Map webStorageOrigins) {
                if ((webStorageOrigins != null) && !webStorageOrigins.isEmpty()) {
                    websiteSettings.setEnabled(true);
                }
            }
        });
        GeolocationPermissions.getInstance().getOrigins(new ValueCallback<Set<String>>() {

            public void onReceiveValue(Set<String> geolocationOrigins) {
                if ((geolocationOrigins != null) && !geolocationOrigins.isEmpty()) {
                    websiteSettings.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        BrowserSettings.getInstance().syncSharedPreferences(getPreferenceScreen().getSharedPreferences());
    }

    public boolean onPreferenceChange(Preference pref, Object objValue) {
        if (pref.getKey().equals(BrowserSettings.PREF_EXTRAS_RESET_DEFAULTS)) {
            Boolean value = (Boolean) objValue;
            if (value.booleanValue() == true) {
                finish();
            }
        } else if (pref.getKey().equals(BrowserSettings.PREF_HOMEPAGE)) {
            String value = (String) objValue;
            boolean needUpdate = value.indexOf(' ') != -1;
            if (needUpdate) {
                value = value.trim().replace(" ", "%20");
            }
            if (value.length() != 0 && Uri.parse(value).getScheme() == null) {
                value = "http://" + value;
                needUpdate = true;
            }
            pref.setSummary(value);
            if (needUpdate) {
                ((EditTextPreference) pref).setText(value);
                return false;
            } else {
                return true;
            }
        } else if (pref.getKey().equals(BrowserSettings.PREF_TEXT_SIZE)) {
            pref.setSummary(getVisualTextSizeName((String) objValue));
            return true;
        } else if (pref.getKey().equals(BrowserSettings.PREF_DEFAULT_ZOOM)) {
            pref.setSummary(getVisualDefaultZoomName((String) objValue));
            return true;
        } else if (pref.getKey().equals(BrowserSettings.PREF_DEFAULT_TEXT_ENCODING)) {
            pref.setSummary((String) objValue);
            return true;
        }
        return false;
    }

    private CharSequence getVisualTextSizeName(String enumName) {
        CharSequence[] visualNames = getResources().getTextArray(R.array.pref_text_size_choices);
        CharSequence[] enumNames = getResources().getTextArray(R.array.pref_text_size_values);
        if (visualNames.length != enumNames.length) {
            return "";
        }
        for (int i = 0; i < enumNames.length; i++) {
            if (enumNames[i].equals(enumName)) {
                return visualNames[i];
            }
        }
        return "";
    }

    private CharSequence getVisualDefaultZoomName(String enumName) {
        CharSequence[] visualNames = getResources().getTextArray(R.array.pref_default_zoom_choices);
        CharSequence[] enumNames = getResources().getTextArray(R.array.pref_default_zoom_values);
        if (visualNames.length != enumNames.length) {
            return "";
        }
        for (int i = 0; i < enumNames.length; i++) {
            if (enumNames[i].equals(enumName)) {
                return visualNames[i];
            }
        }
        return "";
    }
}
