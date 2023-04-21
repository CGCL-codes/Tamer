package com.crypticbit.ipa.entity.concept.wrapper.impl;

import java.text.DecimalFormat;
import com.crypticbit.ipa.entity.concept.GeoLocation;
import com.crypticbit.ipa.entity.concept.wrapper.WhereTag;

public class WhereImpl implements GeoLocation, WhereTag.GeoSetter {

    private Double longitude;

    private Double latitude;

    private Integer accuracy;

    private Integer altitude;

    DecimalFormat twoDecimalPlacesFormat = new DecimalFormat("#.##");

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Integer getAccuracy() {
        return accuracy;
    }

    @Override
    public Integer getAltitude() {
        return altitude;
    }

    @Override
    public void setLongitude(Double value) {
        this.longitude = value;
    }

    @Override
    public void setLatitude(Double value) {
        this.latitude = value;
    }

    @Override
    public void setAccuracy(Double value) {
        this.accuracy = value.intValue();
    }

    @Override
    public void setAltitude(Integer value) {
        this.altitude = value;
    }

    public String toString() {
        if (longitude == null || latitude == null || (longitude == 0.0 && latitude == 0.0)) {
            return "Unknown";
        } else {
            return Double.valueOf(twoDecimalPlacesFormat.format(longitude)) + "," + Double.valueOf(twoDecimalPlacesFormat.format(latitude)) + (altitude == null ? "" : " @" + altitude + "m high") + (accuracy == null ? "" : " (+/-" + accuracy + "m)");
        }
    }
}
