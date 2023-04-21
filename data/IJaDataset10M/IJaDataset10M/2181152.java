package jvc.util.cache;

import java.util.Date;

public class CacheObject {

    private int intValue;

    private long longValue;

    private boolean booleanValue;

    private String StringValue;

    private Date DateValue;

    private long TimeOut = 1000 * 60 * 5;

    private long ActiveTime;

    private Object ObjectValue;

    public Object getObjectValue() {
        return ObjectValue;
    }

    public void setObjectValue(Object objectValue) {
        ObjectValue = objectValue;
    }

    public CacheObject(int value) {
        this.intValue = value;
    }

    public CacheObject(long value) {
        this.longValue = value;
    }

    public CacheObject(String value) {
        this.StringValue = value;
    }

    public CacheObject(Object value) {
        this.ObjectValue = value;
    }

    public CacheObject(int value, long TimeOut) {
        this.intValue = value;
        this.TimeOut = TimeOut;
    }

    public CacheObject(long value, long TimeOut) {
        this.longValue = value;
        this.TimeOut = TimeOut;
    }

    public CacheObject(String value, long TimeOut) {
        this.StringValue = value;
        this.TimeOut = TimeOut;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Date getDateValue() {
        return DateValue;
    }

    public void setDateValue(Date dateValue) {
        DateValue = dateValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public String getStringValue() {
        return StringValue;
    }

    public void setStringValue(String stringValue) {
        StringValue = stringValue;
    }

    public long getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(long timeOut) {
        TimeOut = timeOut;
    }

    public long getActiveTime() {
        return ActiveTime;
    }

    public void setActiveTime(long activeTime) {
        ActiveTime = activeTime;
    }
}
