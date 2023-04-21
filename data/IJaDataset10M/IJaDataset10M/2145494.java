package com.aelitis.azureus.core.dht.control;

public interface DHTControlListener {

    public static final int CT_ADDED = 1;

    public static final int CT_CHANGED = 2;

    public static final int CT_REMOVED = 3;

    public void activityChanged(DHTControlActivity activity, int type);
}
