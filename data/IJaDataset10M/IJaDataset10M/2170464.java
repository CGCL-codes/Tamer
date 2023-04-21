package edu.sdsc.rtdsm.framework.data;

import java.util.*;

public class SourceData {

    private String sourceName;

    Hashtable<Long, TimeStampedData> tsData = new Hashtable<Long, TimeStampedData>();

    public SourceData(String sourceName) {
        this.sourceName = sourceName;
    }

    public TimeStampedData getTimeStampedData(long tsVal) {
        Long key = new Long(tsVal);
        return getTimeStampedData(key);
    }

    public TimeStampedData getTimeStampedData(Long key) {
        if (!tsData.containsKey(key)) {
            TimeStampedData value = new TimeStampedData(key.longValue());
            tsData.put(key, value);
        }
        return tsData.get(key);
    }

    public Enumeration<Long> getTimeStamps() {
        return tsData.keys();
    }
}
