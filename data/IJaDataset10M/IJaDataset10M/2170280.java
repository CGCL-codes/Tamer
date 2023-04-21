package hk.com.twonil.android;

import java.io.Serializable;
import java.util.UUID;
import android.content.Context;
import android.content.SharedPreferences;

public class UserProfile implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String USER_PROFILE_UUIDS = "userProfileUuids";

    private transient Preferences mPreferences;

    String mUuid;

    String mName;

    String mEmail;

    public UserProfile(Context context) {
        mUuid = UUID.randomUUID().toString();
    }

    UserProfile(Preferences preferences, String uuid) {
        this.mUuid = uuid;
        refresh(preferences);
    }

    public String getUuid() {
        return mUuid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public boolean equals(Object o) {
        if (o instanceof UserProfile) {
            return ((UserProfile) o).mUuid.equals(mUuid);
        }
        return super.equals(o);
    }

    /**
     * Refresh the account from the stored settings.
     */
    public void refresh(Preferences preferences) {
        mPreferences = preferences;
        mName = preferences.mSharedPreferences.getString(mUuid + ".name", mName);
        mEmail = preferences.mSharedPreferences.getString(mUuid + ".email", mEmail);
    }

    public void delete(Preferences preferences) {
        String[] uuids = preferences.mSharedPreferences.getString(USER_PROFILE_UUIDS, "").split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = uuids.length; i < length; i++) {
            if (!uuids[i].equals(mUuid)) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(uuids[i]);
            }
        }
        String profileUuids = sb.toString();
        SharedPreferences.Editor editor = preferences.mSharedPreferences.edit();
        editor.putString(USER_PROFILE_UUIDS, profileUuids);
        editor.remove(mUuid + ".name");
        editor.remove(mUuid + ".email");
        editor.commit();
    }

    public void save(Preferences preferences) {
        mPreferences = preferences;
        String uuids = preferences.mSharedPreferences.getString(USER_PROFILE_UUIDS, "");
        if (!uuids.contains(mUuid)) {
            uuids += (uuids.length() != 0 ? "," : "") + mUuid;
            SharedPreferences.Editor editor = preferences.mSharedPreferences.edit();
            editor.putString(USER_PROFILE_UUIDS, uuids);
            editor.commit();
        }
        SharedPreferences.Editor editor = preferences.mSharedPreferences.edit();
        editor.putString(mUuid + ".name", mName);
        editor.putString(mUuid + ".email", mEmail);
        editor.commit();
    }
}
