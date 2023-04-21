package com.yahoo.zookeeper.server.util;

import org.apache.log4j.Logger;

public class Profiler {

    private static final Logger LOG = Logger.getLogger(Profiler.class);

    public interface Operation<T> {

        public T execute() throws Exception;
    }

    public static <T> T profile(Operation<T> op, long timeout, String message) throws Exception {
        long start = System.currentTimeMillis();
        T res = op.execute();
        long end = System.currentTimeMillis();
        if (end - start > timeout) {
            LOG.warn("Elapsed " + (end - start) + " ms: " + message);
        }
        return res;
    }
}
