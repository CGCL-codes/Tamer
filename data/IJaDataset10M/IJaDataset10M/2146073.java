package org.xmlsh.sh.shell;

import java.util.Properties;

@SuppressWarnings("serial")
public class SystemEnvironment extends Properties {

    private static ThreadLocal<SystemEnvironment> sInstance;

    private static synchronized ThreadLocal<SystemEnvironment> _this() {
        if (sInstance == null) sInstance = new ThreadLocal<SystemEnvironment>() {

            protected synchronized SystemEnvironment initialValue() {
                return new SystemEnvironment();
            }
        };
        return sInstance;
    }

    /**
	 * Get an instance of the SystemEnvironment
	 * This is a thread local copy
	 * 
	 */
    private SystemEnvironment() {
    }

    static SystemEnvironment getInstance() {
        return _this().get();
    }

    public static void uninitialize() {
        _this().get().clear();
    }
}
