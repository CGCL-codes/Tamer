package gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class TaskManagerConfig {

    /**
     * Interval for deadlock detector run schedule
     */
    @Property(key = "gameserver.deadlock.interval", defaultValue = "300")
    public static int DEADLOCK_DETECTOR_INTERVAL;

    /**
     * Enable/disable deadlock detector
     */
    @Property(key = "gameserver.deadlock.enable", defaultValue = "true")
    public static boolean DEADLOCK_DETECTOR_ENABLED;
}
