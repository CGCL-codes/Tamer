package com.android.im.app;

public class ProviderDef {

    public long mId;

    public String mName;

    public String mFullName;

    public String mSignUpUrl;

    public ProviderDef(long id, String name, String fullName, String signUpUrl) {
        mId = id;
        mName = name;
        if (fullName != null) {
            mFullName = fullName;
        } else {
            mFullName = name;
        }
        mSignUpUrl = signUpUrl;
    }
}
