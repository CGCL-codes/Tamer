package org.avaje.ebean.server.core;

import java.util.ArrayList;
import java.util.List;
import org.avaje.ebean.server.lib.ConfigProperties;
import org.avaje.ebean.server.plugin.Plugin;

/**
 * Helper used in producing debug output on lazy loading.
 */
public class DebugLazyLoad {

    private final String[] ignoreList;

    private final ConfigProperties properties;

    private final boolean debug;

    public DebugLazyLoad(Plugin plugin) {
        properties = plugin.getProperties().getConfigProperties();
        ignoreList = buildLazyLoadIgnoreList();
        debug = isLazyLoadDebug();
    }

    /**
	 * Return true if debugging is on.
	 */
    public boolean isDebug() {
        return debug;
    }

    /**
	 * Return the StackTraceElement that is believed to be the line of code that
	 * triggered the lazy loading.
	 * <p>
	 * This is determined by going up the stack trace ignoring all the sections
	 * of java and Ebean code etc until you find code not in the ignore list -
	 * this is assumed to be your application code that triggered the lazy
	 * loading (it could be a third party layer such as a web template).
	 * </p>
	 */
    public StackTraceElement getStackTraceElement(String beanType) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            String clsName = stackTrace[i].getClassName();
            if (isStackLine(clsName, beanType)) {
                return stackTrace[i];
            }
        }
        return null;
    }

    /**
	 * Return true if the code is expected to be application code.
	 */
    private boolean isStackLine(String stackClass, String beanType) {
        if (stackClass.startsWith(beanType)) {
            return false;
        }
        for (int i = 0; i < ignoreList.length; i++) {
            if (stackClass.startsWith(ignoreList[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean isLazyLoadDebug() {
        return properties.getBooleanProperty("debug.lazyload", false);
    }

    /**
	 * Build list of prefixes used to find the application code that triggered
	 * the lazy loading.
	 */
    private String[] buildLazyLoadIgnoreList() {
        List<String> ignore = new ArrayList<String>();
        ignore.add("org.avaje.ebean");
        ignore.add("java");
        ignore.add("sun.reflect");
        String extraIgnore = properties.getProperty("debug.lazyload.ignore", null);
        if (extraIgnore != null) {
            String[] split = extraIgnore.split(",");
            for (int i = 0; i < split.length; i++) {
                ignore.add(split[i].trim());
            }
        }
        return ignore.toArray(new String[ignore.size()]);
    }
}
