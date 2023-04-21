package com.googlecode.tcime.unofficial;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Manages IME preferences. 
 */
public class ImePreferenceActivity extends PreferenceActivity {

    private final String licenseUrl = "file:///android_asset/licensing.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        Preference license = findPreference(getString(R.string.prefs_licensing));
        license.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                showLicenseDialog();
                return true;
            }
        });
    }

    private void showLicenseDialog() {
        View licenseView = View.inflate(this, R.layout.licensing, null);
        WebView webView = (WebView) licenseView.findViewById(R.id.license_view);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        webView.loadUrl(licenseUrl);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ime_name);
        builder.setView(licenseView);
        builder.show();
    }
}
