package com.ridanlabs.onelist.db;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public final class RegistrationModel {

    public RegistrationModel(String deviceID) {
        this.deviceID = deviceID;
    }

    public RegistrationModel(String deviceID, String registrationToken) {
        this.deviceID = deviceID;
        this.registrationToken = registrationToken;
    }

    public Key getKey() {
        return this.key;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public String getRegistrationToken() {
        return this.registrationToken;
    }

    @Override
    public boolean equals(Object registrationModel) {
        if (((RegistrationModel) registrationModel).deviceID == this.deviceID) return true;
        return false;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String deviceID;

    @Persistent
    private String registrationToken;
}
