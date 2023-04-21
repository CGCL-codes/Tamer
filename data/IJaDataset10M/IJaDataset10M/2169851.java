package com.volantis.mcs.clientapp;

/**
 * Providers o timer start and stop time 
 */
class TimerFeed implements TimerDataFeed {

    public int getStartTime() {
        return 1800000;
    }

    public int getStopTime() {
        return 0;
    }
}
