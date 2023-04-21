package org.databene.commons;

import java.util.concurrent.Callable;

/**
 * Provides system related utility methods.<br/><br/>
 * Created: 21.10.2009 19:26:24
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class SysUtil {

    private SysUtil() {
    }

    public static void runWithSystemProperty(String name, String value, Runnable runner) {
        String oldValue = System.getProperty(name);
        try {
            System.setProperty(name, value);
            runner.run();
        } finally {
            System.setProperty(name, (oldValue != null ? oldValue : ""));
        }
    }

    public static <T> T callWithSystemProperty(String name, String value, Callable<T> callee) throws Exception {
        String oldValue = System.getProperty(name);
        try {
            System.setProperty(name, value);
            return callee.call();
        } finally {
            System.setProperty(name, oldValue);
        }
    }
}
