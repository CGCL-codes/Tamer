package com.san.utils;

import org.apache.commons.lang.time.StopWatch;

public class StopWatchUtil {

    public static StopWatch getStopWatch() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        return watch;
    }
}
