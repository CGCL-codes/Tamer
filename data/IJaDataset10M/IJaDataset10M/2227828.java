package com.android.WiFiQualityMeasurement;

public class WiFiQualityMeasurementPingAsyncTaskPingInfo {

    private String mPingResult;

    private int mPingTimeOut;

    public void setText(String str) {
        mPingResult = str.toString();
        return;
    }

    public void setTimeout(int timout) {
        mPingTimeOut = timout;
        return;
    }

    public int getTimeout() {
        return mPingTimeOut;
    }

    public String getResult() {
        return mPingResult;
    }
}
