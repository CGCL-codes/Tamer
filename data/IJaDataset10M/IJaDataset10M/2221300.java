package com.android.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentQueryMap;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.security.Credentials;
import android.security.KeyStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.widget.LockPatternUtils;
import android.telephony.TelephonyManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import android.util.Log;

/**
 * Gesture lock pattern settings.
 */
public class SecuritySettings extends PreferenceActivity {

    private static final String KEY_LOCK_ENABLED = "lockenabled";

    private static final String KEY_VISIBLE_PATTERN = "visiblepattern";

    private static final String KEY_INCORRECT_DELAY = "incorrectdelay";

    private static final String KEY_TACTILE_FEEDBACK_ENABLED = "tactilefeedback";

    private static final String KEY_PIN_BASED_LOCKING_ENABLED = "pinbasedlocking";

    private static final String KEY_PIN_CHECK_TIMEOUT = "pinlockchecktimeout";

    private static final String KEY_VISIBLE_DOTS = "visibledots";

    private static final String KEY_SHOW_ERROR_PATH = "showerrorpath";

    private static final String KEY_SHOW_CUSTOM_MSG = "showcustommsg";

    private static final String KEY_SHOW_SLIDERS = "showsliders";

    private static final int CONFIRM_PATTERN_THEN_DISABLE_AND_CLEAR_REQUEST_CODE = 55;

    private static final String PREFS_NAME = "location_prefs";

    private static final String PREFS_USE_LOCATION = "use_location";

    private LockPatternUtils mLockPatternUtils;

    private CheckBoxPreference mLockEnabled;

    private CheckBoxPreference mVisiblePattern;

    private CheckBoxPreference mVisibleDots;

    private CheckBoxPreference mShowErrorPath;

    private CheckBoxPreference mShowCustomMsg;

    private EditTextPreference mCustomMsg;

    private ListPreference mIncorrectDelay;

    private CheckBoxPreference mTactileFeedback;

    private CheckBoxPreference mPinBasedLocking;

    private ListPreference mPinCheckTimeout;

    private Preference mChoosePattern;

    private CheckBoxPreference mShowPassword;

    private static final String LOCATION_CATEGORY = "location_category";

    private static final String LOCATION_NETWORK = "location_network";

    private static final String LOCATION_GPS = "location_gps";

    private static final String ASSISTED_GPS = "assisted_gps";

    private static final int CSTOR_MIN_PASSWORD_LENGTH = 8;

    private static final int CSTOR_INIT_DIALOG = 1;

    private static final int CSTOR_CHANGE_PASSWORD_DIALOG = 2;

    private static final int CSTOR_UNLOCK_DIALOG = 3;

    private static final int CSTOR_RESET_DIALOG = 4;

    private static final int PIN_CHECK_TIMEOUT_INTERVAL = 500;

    private CredentialStorage mCredentialStorage = new CredentialStorage();

    private CheckBoxPreference mNetwork;

    private CheckBoxPreference mGps;

    private CheckBoxPreference mAssistedGps;

    private ContentQueryMap mContentQueryMap;

    private final class SettingsObserver implements Observer {

        public void update(Observable o, Object arg) {
            updateToggles();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.security_settings);
        mLockPatternUtils = new LockPatternUtils(getContentResolver());
        createPreferenceHierarchy();
        mNetwork = (CheckBoxPreference) getPreferenceScreen().findPreference(LOCATION_NETWORK);
        mGps = (CheckBoxPreference) getPreferenceScreen().findPreference(LOCATION_GPS);
        mAssistedGps = (CheckBoxPreference) getPreferenceScreen().findPreference(ASSISTED_GPS);
        updateToggles();
        Cursor settingsCursor = getContentResolver().query(Settings.Secure.CONTENT_URI, null, "(" + Settings.System.NAME + "=?)", new String[] { Settings.Secure.LOCATION_PROVIDERS_ALLOWED }, null);
        mContentQueryMap = new ContentQueryMap(settingsCursor, Settings.System.NAME, true, null);
        mContentQueryMap.addObserver(new SettingsObserver());
        mCredentialStorage.handleIntent(getIntent());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        PreferenceScreen root = this.getPreferenceScreen();
        PreferenceCategory inlinePrefCat = new PreferenceCategory(this);
        inlinePrefCat.setTitle(R.string.lock_settings_title);
        root.addPreference(inlinePrefCat);
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.ChooseLockPatternTutorial");
        mChoosePattern = getPreferenceManager().createPreferenceScreen(this);
        mChoosePattern.setIntent(intent);
        inlinePrefCat.addPreference(mChoosePattern);
        mLockEnabled = new LockEnabledPref(this);
        mLockEnabled.setTitle(R.string.lockpattern_settings_enable_title);
        mLockEnabled.setSummary(R.string.lockpattern_settings_enable_summary);
        mLockEnabled.setKey(KEY_LOCK_ENABLED);
        inlinePrefCat.addPreference(mLockEnabled);
        mVisiblePattern = new CheckBoxPreference(this);
        mVisiblePattern.setKey(KEY_VISIBLE_PATTERN);
        mVisiblePattern.setTitle(R.string.lockpattern_settings_enable_visible_pattern_title);
        inlinePrefCat.addPreference(mVisiblePattern);
        mVisibleDots = new CheckBoxPreference(this);
        mVisibleDots.setKey(KEY_VISIBLE_DOTS);
        mVisibleDots.setTitle(R.string.lockpattern_settings_enable_visible_dots_title);
        inlinePrefCat.addPreference(mVisibleDots);
        mShowErrorPath = new CheckBoxPreference(this);
        mShowErrorPath.setKey(KEY_SHOW_ERROR_PATH);
        mShowErrorPath.setTitle(R.string.lockpattern_settings_show_error_path);
        inlinePrefCat.addPreference(mShowErrorPath);
        mShowCustomMsg = new CheckBoxPreference(this);
        mShowCustomMsg.setKey(KEY_SHOW_CUSTOM_MSG);
        mShowCustomMsg.setTitle(R.string.lockpattern_settings_show_custom_msg);
        inlinePrefCat.addPreference(mShowCustomMsg);
        mCustomMsg = new EditTextPreference(this);
        mCustomMsg.setTitle(R.string.lockpattern_settings_custom_msg);
        mCustomMsg.setDialogTitle(R.string.lockpattern_settings_custom_msg_dialog);
        mCustomMsg.setOnPreferenceChangeListener(mOnCustomMsgChangeListener);
        inlinePrefCat.addPreference(mCustomMsg);
        mIncorrectDelay = new ListPreference(this);
        mIncorrectDelay.setKey(KEY_INCORRECT_DELAY);
        mIncorrectDelay.setTitle(R.string.lockpattern_settings_incorrect_delay_title);
        mIncorrectDelay.setEntries(R.array.incorrect_delay_entries);
        mIncorrectDelay.setEntryValues(R.array.incorrect_delay_values);
        mIncorrectDelay.setOnPreferenceChangeListener(mIncorrectDelayChangeListener);
        inlinePrefCat.addPreference(mIncorrectDelay);
        mTactileFeedback = new CheckBoxPreference(this);
        mTactileFeedback.setKey(KEY_TACTILE_FEEDBACK_ENABLED);
        mTactileFeedback.setTitle(R.string.lockpattern_settings_enable_tactile_feedback_title);
        inlinePrefCat.addPreference(mTactileFeedback);
        mPinBasedLocking = new CheckBoxPreference(this);
        mPinBasedLocking.setKey(KEY_PIN_BASED_LOCKING_ENABLED);
        mPinBasedLocking.setTitle(R.string.lockpattern_settings_enable_pin_based_locking);
        inlinePrefCat.addPreference(mPinBasedLocking);
        mPinCheckTimeout = new ListPreference(this);
        mPinCheckTimeout.setKey(KEY_PIN_CHECK_TIMEOUT);
        mPinCheckTimeout.setTitle(R.string.lockpattern_settings_set_pin_check_timeout);
        mPinCheckTimeout.setEntries(R.array.lockpattern_pin_check_timeout_entries);
        mPinCheckTimeout.setEntryValues(R.array.lockpattern_pin_check_timeout_values);
        mPinCheckTimeout.setOnPreferenceChangeListener(mOnPinCheckTimeoutChangeListener);
        inlinePrefCat.addPreference(mPinCheckTimeout);
        int activePhoneType = TelephonyManager.getDefault().getPhoneType();
        if (TelephonyManager.PHONE_TYPE_CDMA != activePhoneType) {
            PreferenceScreen simLockPreferences = getPreferenceManager().createPreferenceScreen(this);
            simLockPreferences.setTitle(R.string.sim_lock_settings_category);
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.IccLockSettings");
            simLockPreferences.setIntent(intent);
            PreferenceCategory simLockCat = new PreferenceCategory(this);
            simLockCat.setTitle(R.string.sim_lock_settings_title);
            root.addPreference(simLockCat);
            simLockCat.addPreference(simLockPreferences);
        }
        PreferenceCategory passwordsCat = new PreferenceCategory(this);
        passwordsCat.setTitle(R.string.security_passwords_title);
        root.addPreference(passwordsCat);
        CheckBoxPreference showPassword = mShowPassword = new CheckBoxPreference(this);
        showPassword.setKey("show_password");
        showPassword.setTitle(R.string.show_password);
        showPassword.setSummary(R.string.show_password_summary);
        showPassword.setPersistent(false);
        passwordsCat.addPreference(showPassword);
        PreferenceCategory credentialsCat = new PreferenceCategory(this);
        credentialsCat.setTitle(R.string.credentials_category);
        root.addPreference(credentialsCat);
        mCredentialStorage.createPreferences(credentialsCat);
        return root;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean patternExists = mLockPatternUtils.savedPatternExists();
        mLockEnabled.setEnabled(patternExists);
        mVisiblePattern.setEnabled(patternExists);
        mVisibleDots.setEnabled(patternExists);
        mShowErrorPath.setEnabled(patternExists);
        mIncorrectDelay.setEnabled(patternExists);
        mTactileFeedback.setEnabled(patternExists);
        mPinBasedLocking.setEnabled(!patternExists);
        mPinCheckTimeout.setEnabled(mLockPatternUtils.isPinLockingEnabled());
        mLockEnabled.setChecked(mLockPatternUtils.isLockPatternEnabled());
        mVisiblePattern.setChecked(mLockPatternUtils.isVisiblePatternEnabled());
        mVisibleDots.setChecked(mLockPatternUtils.isVisibleDotsEnabled());
        mShowErrorPath.setChecked(mLockPatternUtils.isShowErrorPath());
        mShowCustomMsg.setChecked(mLockPatternUtils.isShowCustomMsg());
        mCustomMsg.setText(mLockPatternUtils.getCustomMsg());
        mTactileFeedback.setChecked(mLockPatternUtils.isTactileFeedbackEnabled());
        mPinBasedLocking.setChecked(mLockPatternUtils.isPinLockingEnabled());
        int chooseStringRes = mLockPatternUtils.savedPatternExists() ? R.string.lockpattern_settings_change_lock_pattern : R.string.lockpattern_settings_choose_lock_pattern;
        mChoosePattern.setTitle(chooseStringRes);
        mShowPassword.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 1) != 0);
        mCredentialStorage.resume();
    }

    private Preference.OnPreferenceChangeListener mIncorrectDelayChangeListener = new Preference.OnPreferenceChangeListener() {

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mIncorrectDelay) {
                int value = Integer.parseInt(newValue.toString());
                mLockPatternUtils.setIncorrectDelay(value);
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener mOnPinCheckTimeoutChangeListener = new Preference.OnPreferenceChangeListener() {

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mPinCheckTimeout) {
                int index = mPinCheckTimeout.findIndexOfValue((String) newValue) + 1;
                mLockPatternUtils.setPinCheckTimeout(PIN_CHECK_TIMEOUT_INTERVAL * index);
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener mOnCustomMsgChangeListener = new Preference.OnPreferenceChangeListener() {

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference == mCustomMsg) {
                mLockPatternUtils.setCustomMsg(newValue.toString());
            }
            return true;
        }
    };

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        final String key = preference.getKey();
        if (KEY_LOCK_ENABLED.equals(key)) {
            boolean patternEnabled = isToggled(preference);
            mLockPatternUtils.setLockPatternEnabled(patternEnabled);
        } else if (KEY_VISIBLE_PATTERN.equals(key)) {
            mLockPatternUtils.setVisiblePatternEnabled(isToggled(preference));
        } else if (KEY_VISIBLE_DOTS.equals(key)) {
            mLockPatternUtils.setVisibleDotsEnabled(isToggled(preference));
        } else if (KEY_SHOW_ERROR_PATH.equals(key)) {
            mLockPatternUtils.setShowErrorPath(isToggled(preference));
        } else if (KEY_SHOW_CUSTOM_MSG.equals(key)) {
            mLockPatternUtils.setShowCustomMsg(isToggled(preference));
        } else if (KEY_TACTILE_FEEDBACK_ENABLED.equals(key)) {
            mLockPatternUtils.setTactileFeedbackEnabled(isToggled(preference));
        } else if (KEY_PIN_BASED_LOCKING_ENABLED.equals(key)) {
            mLockPatternUtils.setPinLockingEnabled(isToggled(preference));
            mPinCheckTimeout.setEnabled(mLockPatternUtils.isPinLockingEnabled());
        } else if (KEY_PIN_CHECK_TIMEOUT.equals(key)) {
            int valueIndex = (mLockPatternUtils.getPinCheckTimeout() / PIN_CHECK_TIMEOUT_INTERVAL) - 1;
            mPinCheckTimeout.setValueIndex(Math.max(0, Math.min(valueIndex, 3)));
        } else if (preference == mShowPassword) {
            Settings.System.putInt(getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, mShowPassword.isChecked() ? 1 : 0);
        } else if (preference == mNetwork) {
            Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.NETWORK_PROVIDER, mNetwork.isChecked());
        } else if (preference == mGps) {
            boolean enabled = mGps.isChecked();
            Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, enabled);
            if (mAssistedGps != null) {
                mAssistedGps.setEnabled(enabled);
            }
        } else if (preference == mAssistedGps) {
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.ASSISTED_GPS_ENABLED, mAssistedGps.isChecked() ? 1 : 0);
        }
        return false;
    }

    private void showPrivacyPolicy() {
        Intent intent = new Intent("android.settings.TERMS");
        startActivity(intent);
    }

    private void updateToggles() {
        ContentResolver res = getContentResolver();
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);
        mNetwork.setChecked(Settings.Secure.isLocationProviderEnabled(res, LocationManager.NETWORK_PROVIDER));
        mGps.setChecked(gpsEnabled);
        if (mAssistedGps != null) {
            mAssistedGps.setChecked(Settings.Secure.getInt(res, Settings.Secure.ASSISTED_GPS_ENABLED, 2) == 1);
            mAssistedGps.setEnabled(gpsEnabled);
        }
    }

    private boolean isToggled(Preference pref) {
        return ((CheckBoxPreference) pref).isChecked();
    }

    /**
     * For the user to disable keyguard, we first make them verify their
     * existing pattern.
     */
    private class LockEnabledPref extends CheckBoxPreference {

        public LockEnabledPref(Context context) {
            super(context);
        }

        @Override
        protected void onClick() {
            if (mLockPatternUtils.savedPatternExists() && isChecked()) {
                confirmPatternThenDisableAndClear();
            } else {
                super.onClick();
            }
        }
    }

    /**
     * Launch screen to confirm the existing lock pattern.
     * @see #onActivityResult(int, int, android.content.Intent)
     */
    private void confirmPatternThenDisableAndClear() {
        final Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.ConfirmLockPattern");
        startActivityForResult(intent, CONFIRM_PATTERN_THEN_DISABLE_AND_CLEAR_REQUEST_CODE);
    }

    /**
     * @see #confirmPatternThenDisableAndClear
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final boolean resultOk = resultCode == Activity.RESULT_OK;
        if ((requestCode == CONFIRM_PATTERN_THEN_DISABLE_AND_CLEAR_REQUEST_CODE) && resultOk) {
            mLockPatternUtils.setLockPatternEnabled(false);
            mLockPatternUtils.saveLockPattern(null);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return mCredentialStorage.createDialog(id);
    }

    private class CredentialStorage implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        private static final int MINIMUM_PASSWORD_LENGTH = 8;

        private static final int UNLOCK_DIALOG = 1;

        private static final int PASSWORD_DIALOG = 2;

        private static final int RESET_DIALOG = 3;

        private KeyStore mKeyStore = KeyStore.getInstance();

        private int mState = mKeyStore.test();

        private int mDialogId;

        private boolean mSubmit;

        private CheckBoxPreference mAccessCheckBox;

        private Preference mInstallButton;

        private Preference mPasswordButton;

        private Preference mResetButton;

        private Intent mExternalIntent;

        void handleIntent(Intent intent) {
            if (intent != null) {
                if (Credentials.UNLOCK_ACTION.equals(intent.getAction())) {
                    mExternalIntent = intent;
                    showDialog((mState == KeyStore.UNINITIALIZED) ? PASSWORD_DIALOG : UNLOCK_DIALOG);
                }
            }
        }

        void resume() {
            mState = mKeyStore.test();
            updatePreferences(mState);
        }

        Dialog createDialog(int id) {
            mDialogId = id;
            switch(id) {
                case UNLOCK_DIALOG:
                    return createUnlockDialog();
                case PASSWORD_DIALOG:
                    return createPasswordDialog();
                case RESET_DIALOG:
                    return createResetDialog();
            }
            return null;
        }

        private void initialize(String password) {
            mKeyStore.password(password);
            updatePreferences(KeyStore.NO_ERROR);
        }

        private void reset() {
            mKeyStore.reset();
            updatePreferences(KeyStore.UNINITIALIZED);
        }

        private void lock() {
            mKeyStore.lock();
            updatePreferences(KeyStore.LOCKED);
        }

        private int unlock(String password) {
            mKeyStore.unlock(password);
            return mKeyStore.getLastError();
        }

        private int changePassword(String oldPassword, String newPassword) {
            mKeyStore.password(oldPassword, newPassword);
            return mKeyStore.getLastError();
        }

        public boolean onPreferenceChange(Preference preference, Object value) {
            if (preference == mAccessCheckBox) {
                if ((Boolean) value) {
                    showDialog((mState == KeyStore.UNINITIALIZED) ? PASSWORD_DIALOG : UNLOCK_DIALOG);
                } else {
                    lock();
                }
                return true;
            }
            return false;
        }

        public boolean onPreferenceClick(Preference preference) {
            if (preference == mInstallButton) {
                Credentials.getInstance().installFromSdCard(SecuritySettings.this);
            } else if (preference == mPasswordButton) {
                showDialog(PASSWORD_DIALOG);
            } else if (preference == mResetButton) {
                showDialog(RESET_DIALOG);
            } else {
                return false;
            }
            return true;
        }

        public void onClick(DialogInterface dialog, int button) {
            mSubmit = (button == DialogInterface.BUTTON_POSITIVE);
            if (button == DialogInterface.BUTTON_NEUTRAL) {
                reset();
            }
        }

        public void onDismiss(DialogInterface dialog) {
            if (mSubmit && !isFinishing()) {
                mSubmit = false;
                if (!checkPassword((Dialog) dialog)) {
                    showDialog(mDialogId);
                    return;
                }
            }
            removeDialog(mDialogId);
            updatePreferences(mState);
            if (mExternalIntent != null) {
                mExternalIntent = null;
                finish();
            }
        }

        private boolean checkPassword(Dialog dialog) {
            String oldPassword = getText(dialog, R.id.old_password);
            String newPassword = getText(dialog, R.id.new_password);
            String confirmPassword = getText(dialog, R.id.confirm_password);
            if (oldPassword != null && oldPassword.length() == 0) {
                showError(dialog, R.string.credentials_password_empty);
                return false;
            } else if (newPassword == null) {
                return !checkError(dialog, unlock(oldPassword));
            } else if (newPassword.length() == 0 || confirmPassword.length() == 0) {
                showError(dialog, R.string.credentials_passwords_empty);
            } else if (newPassword.length() < MINIMUM_PASSWORD_LENGTH) {
                showError(dialog, R.string.credentials_password_too_short);
            } else if (!newPassword.equals(confirmPassword)) {
                showError(dialog, R.string.credentials_passwords_mismatch);
            } else if (oldPassword == null) {
                initialize(newPassword);
                return true;
            } else {
                return !checkError(dialog, changePassword(oldPassword, newPassword));
            }
            return false;
        }

        private boolean checkError(Dialog dialog, int error) {
            if (error == KeyStore.NO_ERROR) {
                updatePreferences(KeyStore.NO_ERROR);
                return false;
            }
            if (error == KeyStore.UNINITIALIZED) {
                updatePreferences(KeyStore.UNINITIALIZED);
                return false;
            }
            if (error < KeyStore.WRONG_PASSWORD) {
                return false;
            }
            int count = error - KeyStore.WRONG_PASSWORD + 1;
            if (count > 3) {
                showError(dialog, R.string.credentials_wrong_password);
            } else if (count == 1) {
                showError(dialog, R.string.credentials_reset_warning);
            } else {
                showError(dialog, R.string.credentials_reset_warning_plural, count);
            }
            return true;
        }

        private String getText(Dialog dialog, int viewId) {
            TextView view = (TextView) dialog.findViewById(viewId);
            return (view == null || view.getVisibility() == View.GONE) ? null : view.getText().toString();
        }

        private void showError(Dialog dialog, int stringId, Object... formatArgs) {
            TextView view = (TextView) dialog.findViewById(R.id.error);
            if (view != null) {
                if (formatArgs == null || formatArgs.length == 0) {
                    view.setText(stringId);
                } else {
                    view.setText(dialog.getContext().getString(stringId, formatArgs));
                }
                view.setVisibility(View.VISIBLE);
            }
        }

        private void createPreferences(PreferenceCategory category) {
            mAccessCheckBox = new CheckBoxPreference(SecuritySettings.this);
            mAccessCheckBox.setTitle(R.string.credentials_access);
            mAccessCheckBox.setSummary(R.string.credentials_access_summary);
            mAccessCheckBox.setOnPreferenceChangeListener(this);
            category.addPreference(mAccessCheckBox);
            mInstallButton = new Preference(SecuritySettings.this);
            mInstallButton.setTitle(R.string.credentials_install_certificates);
            mInstallButton.setSummary(R.string.credentials_install_certificates_summary);
            mInstallButton.setOnPreferenceClickListener(this);
            category.addPreference(mInstallButton);
            mPasswordButton = new Preference(SecuritySettings.this);
            mPasswordButton.setTitle(R.string.credentials_set_password);
            mPasswordButton.setSummary(R.string.credentials_set_password_summary);
            mPasswordButton.setOnPreferenceClickListener(this);
            category.addPreference(mPasswordButton);
            mResetButton = new Preference(SecuritySettings.this);
            mResetButton.setTitle(R.string.credentials_reset);
            mResetButton.setSummary(R.string.credentials_reset_summary);
            mResetButton.setOnPreferenceClickListener(this);
            category.addPreference(mResetButton);
        }

        private void updatePreferences(int state) {
            mAccessCheckBox.setEnabled(state != KeyStore.UNINITIALIZED);
            mAccessCheckBox.setChecked(state == KeyStore.NO_ERROR);
            mResetButton.setEnabled(state != KeyStore.UNINITIALIZED);
            if (mState == state) {
                return;
            } else if (state == KeyStore.NO_ERROR) {
                Toast.makeText(SecuritySettings.this, R.string.credentials_enabled, Toast.LENGTH_SHORT).show();
            } else if (state == KeyStore.UNINITIALIZED) {
                Toast.makeText(SecuritySettings.this, R.string.credentials_erased, Toast.LENGTH_SHORT).show();
            } else if (state == KeyStore.LOCKED) {
                Toast.makeText(SecuritySettings.this, R.string.credentials_disabled, Toast.LENGTH_SHORT).show();
            }
            mState = state;
        }

        private Dialog createUnlockDialog() {
            View view = View.inflate(SecuritySettings.this, R.layout.credentials_unlock_dialog, null);
            if (mExternalIntent != null) {
                view.findViewById(R.id.hint).setVisibility(View.VISIBLE);
            }
            Dialog dialog = new AlertDialog.Builder(SecuritySettings.this).setView(view).setTitle(R.string.credentials_unlock).setPositiveButton(android.R.string.ok, this).setNegativeButton(android.R.string.cancel, this).create();
            dialog.setOnDismissListener(this);
            return dialog;
        }

        private Dialog createPasswordDialog() {
            View view = View.inflate(SecuritySettings.this, R.layout.credentials_password_dialog, null);
            if (mState == KeyStore.UNINITIALIZED) {
                view.findViewById(R.id.hint).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.old_password_prompt).setVisibility(View.VISIBLE);
                view.findViewById(R.id.old_password).setVisibility(View.VISIBLE);
            }
            Dialog dialog = new AlertDialog.Builder(SecuritySettings.this).setView(view).setTitle(R.string.credentials_set_password).setPositiveButton(android.R.string.ok, this).setNegativeButton(android.R.string.cancel, this).create();
            dialog.setOnDismissListener(this);
            return dialog;
        }

        private Dialog createResetDialog() {
            return new AlertDialog.Builder(SecuritySettings.this).setTitle(android.R.string.dialog_alert_title).setIcon(android.R.drawable.ic_dialog_alert).setMessage(R.string.credentials_reset_hint).setNeutralButton(getString(android.R.string.ok), this).setNegativeButton(getString(android.R.string.cancel), this).create();
        }
    }
}
