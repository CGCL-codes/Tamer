package jp.android_group.payforward.monac;

import jp.android_group.payforward.monac.R;
import org.piax.trans.common.PeerId;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;

public class Setting extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String PEER_ID_KEY = "peer_id_key";

    public static final String USER_ID_KEY = "user_id_key";

    public static final String USER_NAME_KEY = "user_name_key";

    public static final String ASK_DATA_EX_KEY = "ask_data_ex_key";

    public static final String USE_BLUETOOTH_KEY = "use_bluetooth_key";

    public static final String PERIODIC_BLUETOOTH_KEY = "periodic_bluetooth_key";

    public static final String USE_WIFI_KEY = "use_wifi_key";

    public static final String USE_NFC_KEY = "use_nfc_key";

    public static final String USER_PRIVATE_KEY = "user_private_key";

    public static final String USER_PUBLIC_KEY = "user_public_key";

    public static final String AUTHENTICATE_KEY = "authenticate_key";

    public static final String PREF_VON_KEY = "pref_von_key";

    public static final String VON_ENTRIES_KEY = "von_entries_key";

    public static final String ABOUT_KEY = "about_key";

    public static final String SYNCHRONIZER_ENABLE_KEY = "synchronizer_enable_key";

    public static final int MENU_REISSUE = (Menu.FIRST + 1);

    private Activity activity;

    private Preference authPref;

    String vonId;

    String peerId;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 10) {
            addPreferencesFromResource(R.xml.pref);
        } else {
            addPreferencesFromResource(R.xml.pref_nonfc);
        }
        activity = this;
        authPref = getPreferenceScreen().findPreference(AUTHENTICATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userName = PreferenceManager.getDefaultSharedPreferences(this).getString(USER_NAME_KEY, "");
        if (userName.equals("")) {
            authPref.setSummary(getString(R.string.authenticate_desc));
        } else {
            authPref.setSummary(getString(R.string.authenticated_as) + userName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_REISSUE, Menu.NONE, getString(R.string.reissue_peerid)).setIcon(android.R.drawable.ic_menu_revert);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference != null && preference.getKey() != null) {
            String prefKey = preference.getKey();
            if (prefKey.equals(AUTHENTICATE_KEY)) {
                String pid = PreferenceManager.getDefaultSharedPreferences(this).getString(Setting.PEER_ID_KEY, "");
                String url = Monac.GATEWAY_URL + Monac.PATH_AUTHENTICATE + "?pid=" + pid + "&callback=monac://main";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                activity.finish();
            } else if (prefKey.equals(ABOUT_KEY)) {
                String packegeName = getPackageName();
                PackageInfo packageInfo = null;
                try {
                    packageInfo = getPackageManager().getPackageInfo(packegeName, PackageManager.GET_META_DATA);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle(R.string.license_title_txt);
                ad.setMessage(getString(R.string.license_msg_txt) + "\n\n" + "version : " + ((packageInfo != null) ? packageInfo.versionName : ""));
                ad.setNeutralButton(getString(R.string.view_more), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://code.google.com/p/payforwarding/"));
                        startActivity(intent);
                    }
                });
                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        }
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int fid, MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case MENU_REISSUE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.reissue_peerid_desc)).setTitle(R.string.app_name).setPositiveButton(getString(R.string.reissue), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        PeerId peerId = PeerId.newId();
                        String peerIdString = peerId.toString();
                        SharedPreferences.Editor prefEdit = PreferenceManager.getDefaultSharedPreferences(activity).edit();
                        prefEdit.putString(Setting.PEER_ID_KEY, peerIdString);
                        prefEdit.commit();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).setCancelable(true);
                builder.show();
                return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }
}
