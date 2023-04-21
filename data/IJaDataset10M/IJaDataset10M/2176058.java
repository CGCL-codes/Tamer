package org.furthurnet.datastructures.supporting;

public interface BandwidthUser {

    public static final int UPSTREAM = 0;

    public static final int DOWNSTREAM = 1;

    public double getActualSpeed();

    public int getBandwidthType();
}
