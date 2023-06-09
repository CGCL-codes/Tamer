package com.ibm.celldt.simulator.conf;

import org.eclipse.osgi.util.NLS;

/**
 * Groups several properties that define the behaviour of the simulator control.
 * 
 * @author Daniel Felix Ferber
 * 
 */
public class Parameters extends NLS {

    private static final String BUNDLE_NAME = "com.ibm.celldt.simulator.conf.parameters";

    public static String COMMAND_SHUTDOWN;

    /**
	 * Minimum amount of memory that is required for a simulator configuration.
	 * This property might be moved to the architecture plugin that describes
	 * the cpu.
	 */
    public static String MIN_MEMORY_SIZE;

    /**
	 * Flag to enable Java API.
	 * This library is not yet fully functional.
	 */
    public static String USE_JAVA_API;

    public static String HANDLE_JAVA_API_GUI_ISSUE;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Parameters.class);
    }

    /** Facility method to get property as string. */
    public static int getMinMemorySize() {
        return Integer.parseInt(MIN_MEMORY_SIZE);
    }

    /** Facility method to get property as boolean. */
    public static boolean doUseJavaAPI() {
        return Boolean.valueOf(USE_JAVA_API).booleanValue();
    }

    public static boolean doHandleJavaApiGuiIssue() {
        return Boolean.valueOf(HANDLE_JAVA_API_GUI_ISSUE).booleanValue();
    }

    private Parameters() {
    }
}
