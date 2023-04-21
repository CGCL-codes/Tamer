package com.daipresents.logging.log4j12;

import org.apache.log4j.Logger;

public class Log4j12PerformanceSample {

    static final Logger logger = Logger.getLogger(Log4j12PerformanceSample.class);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int cnt = 1000;
        for (int i = 0; i < cnt; i++) {
            for (int j = 0; j < cnt; j++) {
                logger.debug("fujihara.");
            }
        }
        long end = System.currentTimeMillis();
        logger.debug(String.valueOf((end - start) / 1000));
    }
}
