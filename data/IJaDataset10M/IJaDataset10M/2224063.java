package com.db4o.internal;

public final class CallBackMode {

    public static final CallBackMode ALL = new CallBackMode("ALL");

    public static final CallBackMode DELETE_ONLY = new CallBackMode("DELETE_ONLY");

    public static final CallBackMode NONE = new CallBackMode("NONE");

    private String _desc;

    private CallBackMode(String desc) {
        _desc = desc;
    }

    @Override
    public String toString() {
        return _desc;
    }
}
