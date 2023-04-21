package com.ontotext.ordi.sar.utils;

import org.apache.log4j.Logger;
import com.ontotext.ordi.iterator.CloseableIterator;

public class GenericUtils {

    /**
	 * Expands a path string.
	 * 
	 * @param path The path string to be expanded
	 * @return
	 */
    public static String expand(String path) {
        String userDir = System.getProperty("user.dir");
        path = path.replace("$user.dir", userDir);
        return path;
    }

    public static void closeQuietly(CloseableIterator<?> it, Logger log) {
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
