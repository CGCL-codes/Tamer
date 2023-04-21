package org.brainypdm.modules.cleaner.constants;

public class CleanerConstants {

    public static final String CONF_PREFIX = "cleaner.";

    public static final String CLEANER_THREAD_NAME = CONF_PREFIX + "cleaner.thread.name";

    public static final String CLEANER_THREAD_SLEEP_PERIOD = CONF_PREFIX + "cleaner.thread.sleep";

    public static final String CLEANER_THREAD_N_MAX_PD = CONF_PREFIX + "passivate.max.performance_data";

    public static final String CLEANER_THREAD_OLD_MINUTES = CONF_PREFIX + "passivate.oldminute.performance_data";
}
