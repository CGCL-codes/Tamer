package org.openaion.gameserver.configs.main;

import org.openaion.commons.configuration.Property;

/**
 * @author lord_rex
 * 
 */
public class ShutdownConfig {

    @Property(key = "gameserver.shutdown.mode", defaultValue = "1")
    public static int HOOK_MODE;

    @Property(key = "gameserver.shutdown.delay", defaultValue = "60")
    public static int HOOK_DELAY;

    @Property(key = "gameserver.shutdown.interval", defaultValue = "1")
    public static int ANNOUNCE_INTERVAL;

    @Property(key = "gameserver.shutdown.safereboot", defaultValue = "true")
    public static boolean SAFE_REBOOT;
}
