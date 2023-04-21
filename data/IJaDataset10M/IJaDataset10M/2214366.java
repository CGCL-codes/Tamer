package com.ontotext.ordi.sar;

import org.slf4j.Logger;

public class OrdiUtil {

    public static void closeQuietly(com.ontotext.platform.iteration.CloseableIterator<?> it, Logger log) {
        if (it != null) {
            try {
                it.close();
            } catch (Exception e) {
                if (log != null) {
                    log.warn("Ignoring error while closing iterator.", e);
                } else {
                    System.out.println("Ignoring error while closing iterator.");
                    e.printStackTrace();
                }
            }
        }
    }
}
