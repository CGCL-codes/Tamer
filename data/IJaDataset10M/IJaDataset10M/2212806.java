package org.lnicholls.galleon.apps.weather;

import org.lnicholls.galleon.app.*;
import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class WeatherConfiguration implements AppConfiguration {

    public String getId() {
        return mId;
    }

    public void setId(String value) {
        if (mId != null && !mId.equals(value)) mModified = true;
        mId = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String value) {
        if (mName != null && !mName.equals(value)) mModified = true;
        mName = value;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String value) {
        if (mCity != null && !mCity.equals(value)) mModified = true;
        mCity = value;
    }

    public String getState() {
        return mState;
    }

    public void setState(String value) {
        if (mState != null && !mState.equals(value)) mModified = true;
        mState = value;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String value) {
        if (mZip != null && !mZip.equals(value)) mModified = true;
        mZip = value;
    }

    public void setModified(boolean value) {
        mModified = value;
    }

    public boolean isModified() {
        return mModified;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean isShared() {
        return mShared;
    }

    public void setShared(boolean value) {
        mShared = value;
    }

    private boolean mShared;

    private String mId;

    private String mName;

    private String mCity;

    private String mState;

    private String mZip;

    private boolean mModified;

    public String getRange() {
        return mRange;
    }

    public void setRange(String value) {
        if (mRange != null && !mRange.equals(value)) mModified = true;
        mRange = value;
    }

    private String mRange;

    public WeatherConfiguration() {
        super();
        mRange = "600";
    }
}
