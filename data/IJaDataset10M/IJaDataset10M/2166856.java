package com.technoetic.xplanner.charts;

import java.io.Serializable;
import java.util.Date;

public class DataSample implements Serializable {

    private Date sampleTime;

    private int referenceId;

    private String aspect;

    private double value;

    DataSample() {
    }

    public DataSample(Date sampleTime, int referenceId, String aspect, double value) {
        this.sampleTime = sampleTime;
        this.referenceId = referenceId;
        this.aspect = aspect;
        this.value = value;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        return aspect + " of oid " + referenceId + " on " + sampleTime + " was " + value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSample)) return false;
        final DataSample dataSample = (DataSample) o;
        if (referenceId != dataSample.referenceId) return false;
        if (value != dataSample.value) return false;
        if (aspect != null ? !aspect.equals(dataSample.aspect) : dataSample.aspect != null) return false;
        if (sampleTime != null ? !sampleTime.equals(dataSample.sampleTime) : dataSample.sampleTime != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        long temp;
        result = (sampleTime != null ? sampleTime.hashCode() : 0);
        result = 29 * result + referenceId;
        result = 29 * result + (aspect != null ? aspect.hashCode() : 0);
        temp = Double.doubleToLongBits(value);
        result = 29 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
