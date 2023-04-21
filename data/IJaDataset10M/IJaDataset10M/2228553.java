package de.pepping.android.ringtone.handler;

import static de.pepping.android.ringtone.Constants.TAG;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import de.pepping.android.ringtone.Constants;
import de.pepping.android.ringtone.R;
import de.pepping.android.ringtone.activity.MainSettingsActivity;
import de.pepping.android.ringtone.fwk.Setting;
import de.pepping.android.ringtone.fwk.SettingHandler;

public class GpsSettingHandler extends SettingHandler {

    private static final String TAG2 = "com.android.settings";

    public GpsSettingHandler(Setting setting) {
        super(setting);
    }

    private void updateSetting(boolean gpsEnabled) {
        Setting setting = mSetting;
        setting.descr = mActivity.getString(gpsEnabled ? R.string.txt_status_turned_on : R.string.txt_status_turned_off);
        setting.checked = gpsEnabled;
        setting.updateView();
    }

    @Override
    public void activate(MainSettingsActivity activity) {
        mActivity = activity;
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        updateSetting(manager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void onSelected(int buttonIndex) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mActivity.startActivity(intent);
    }

    @Override
    public void onSwitched(boolean isSwitched) {
        try {
            if (Constants.SDK_VERSION >= 9) {
                onSelected(0);
            } else {
                toggleGpsState();
                updateSetting(isSwitched);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
            onSelected(0);
        }
    }

    private void toggleGpsState() {
        Intent intent = new Intent();
        intent.setClassName(TAG2, TAG2 + ".widget.SettingsAppWidgetProvider");
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setData(Uri.parse(String.valueOf(3)));
        mActivity.sendBroadcast(intent);
    }

    @Override
    public void onValueChanged(int value) {
    }
}
