package com.quickorm.logging;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * @author aaa
 */
public class LogFactory {

    protected static final HashMap<String, Log> instances = new HashMap<String, Log>();

    private static final String COMMONS_LOGGING_LOGFACTORY_CLASSNAME = "org.apache.commons.logging.LogFactory";

    private static Method commons_Logging_LogFactory_getLog_Method;

    public static Log getLog(Class type) {
        return getLog(type.getName());
    }

    public static Log getLog(String string) {
        synchronized (instances) {
            if (instances.containsKey(string)) {
                return instances.get(string);
            } else {
                Log log = null;
                try {
                    if (commons_Logging_LogFactory_getLog_Method == null) {
                        Class commons_Logging_LogFactory_Class = Class.forName(COMMONS_LOGGING_LOGFACTORY_CLASSNAME);
                        commons_Logging_LogFactory_getLog_Method = commons_Logging_LogFactory_Class.getMethod("getLog", String.class);
                    }
                    Object logObj = commons_Logging_LogFactory_getLog_Method.invoke(null, string);
                    log = new Log(logObj, string);
                } catch (Exception ex) {
                    log = new Log(null, string);
                }
                instances.put(string, log);
                return log;
            }
        }
    }
}
