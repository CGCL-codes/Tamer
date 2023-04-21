package com.rbnb.api;

interface DataSizeMetricsInterface {

    public abstract void calculateDataSizes(long[] cacheDSIO, long[] archiveDSIO);
}
