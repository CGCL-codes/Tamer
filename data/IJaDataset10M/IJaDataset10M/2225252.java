package com.chungco.rest.boxnet;

import com.chungco.rest.ILoginProvider;

public class BoxLoginProvider implements ILoginProvider {

    private String mUsername;

    private String mPassword;

    private final String mSID;

    public BoxLoginProvider(final String pUsername, final String pPassword, final String pSID) {
        mUsername = pUsername;
        mPassword = pPassword;
        mSID = pSID;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getSID() {
        return mSID;
    }
}
