package com.akop.spark;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import com.akop.spark.activity.playstation.AccountSetupLogin;
import com.akop.spark.activity.playstation.AccountSummary;
import com.akop.spark.parser.Parser;
import com.akop.spark.parser.PsnParser;

public class PsnAccount extends Account {

    private static final long serialVersionUID = 4804600471625415833L;

    private String mOnlineId;

    private String mEmailAddress;

    private String mPassword;

    private long mLastSummarySync;

    public PsnAccount(Context context) {
        super(context, ACCOUNT_PLAYSTATION_NETWORK);
        mOnlineId = null;
        mEmailAddress = null;
        mPassword = null;
        mLastSummarySync = 0;
    }

    protected PsnAccount(Preferences preferences, String uuid) {
        super(preferences, uuid);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    public boolean isValid() {
        return getEmailAddress() != null && getPassword() != null;
    }

    public String getOnlineId() {
        return mOnlineId;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public String getPassword() {
        return mPassword;
    }

    public long getLastSummaryUpdate() {
        return mLastSummarySync;
    }

    public void setOnlineId(String gamertag) {
        mOnlineId = gamertag;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setLastSummaryUpdate(long ms) {
        mLastSummarySync = ms;
    }

    public long getSummaryRefreshInterval() {
        return 1 * DateUtils.HOUR_IN_MILLIS;
    }

    @Override
    protected void onSave(SharedPreferences.Editor editor) {
        super.onSave(editor);
        editor.putString(mUuid + ".onlineId", mOnlineId);
        editor.putString(mUuid + ".emailAddress", mEmailAddress);
        editor.putString(mUuid + ".password", mPassword);
        editor.putLong(mUuid + ".lastSummarySync", mLastSummarySync);
    }

    @Override
    protected void onLoad(SharedPreferences preferences) {
        super.onLoad(preferences);
        mOnlineId = preferences.getString(mUuid + ".onlineId", null);
        mEmailAddress = preferences.getString(mUuid + ".emailAddress", null);
        mPassword = preferences.getString(mUuid + ".password", null);
        mLastSummarySync = preferences.getLong(mUuid + ".lastSummarySync", 0);
    }

    @Override
    public String getDescription() {
        return "PlayStation Network";
    }

    @Override
    public String getScreenName() {
        return mOnlineId;
    }

    @Override
    public String getLogonId() {
        return mEmailAddress;
    }

    @Override
    public void open(Context context) {
        AccountSummary.actionShowAccountSummary(context, this);
    }

    @Override
    public void editLogin(Activity context) {
        AccountSetupLogin.actionEditLoginData(context, this, false);
    }

    @Override
    public void runSetupWizard(Activity context) {
        AccountSetupLogin.actionEditLoginData(context, this, true);
    }

    @Override
    public int getUnreadMessageCount(Context context) {
        return 0;
    }

    @Override
    public void actionComposeMessage(Context context, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void actionOpenMessage(Context context, long messageUid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser getParser(Context context) {
        return new PsnParser(context);
    }
}
