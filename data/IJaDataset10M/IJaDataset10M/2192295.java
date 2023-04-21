package com.sun.mmedia.rtsp.protocol;

public class DurationHeader {

    private long duration;

    public DurationHeader(String str) {
        duration = Long.parseLong(str);
    }

    public long getDuration() {
        return duration;
    }
}
