package com.aol.map;

import com.aol.services.LogLevel;
import com.aol.services.licensing.LicensingLogLevel;
import org.apache.log4j.Level;

public class MQLogLevel extends LogLevel {

    /** 
     * The logging level for all log records. It should be greater
     * than INFO_INT so that it's caught by loggers that are only interested
     * in entries more important than INFO (WARN, ERROR, etc)
     */
    public static final LogLevel TILE = new MQLogLevel(LOGLEVEL_INT + 100 + 1, "Tile", 6);

    public static final LogLevel TILE_RESULT = new MQLogLevel(LOGLEVEL_INT + 100 + 2, "Tile-Result", 6);

    public static final LogLevel LOCATE = new MQLogLevel(LOGLEVEL_INT + 100 + 3, "Locate", 6);

    public static final LogLevel LOCATE_RESULT = new MQLogLevel(LOGLEVEL_INT + 100 + 4, "Locate-Result", 6);

    public MQLogLevel() {
        super(LOGLEVEL_INT + 1, "Unknown", 6);
    }

    protected MQLogLevel(int level, String name, int sysLogEquiv) {
        super(level, name, sysLogEquiv);
    }

    public Level getLevel(String sArg, LogLevel defaultLevel) {
        return MQLogLevel.toLevel(sArg, defaultLevel);
    }

    /**
     * Convert the string passed as argument to a level. If the
     * conversion fails, then this method returns {@link Level}'s value. 
     */
    public static final Level toLevel(String sArg, LogLevel defaultLevel) {
        Level l = LogLevel.toLevel(sArg, defaultLevel);
        if (l != defaultLevel) return l;
        l = LicensingLogLevel.toLevel(sArg, defaultLevel);
        if (l != defaultLevel) return l;
        String s = sArg.toUpperCase();
        if (s.equals("TILE")) return MQLogLevel.TILE;
        if (s.equals("TILE-RESULT")) return MQLogLevel.TILE_RESULT;
        if (s.equals("LOCATE")) return MQLogLevel.LOCATE;
        if (s.equals("LOCATE-RESULT")) return MQLogLevel.LOCATE_RESULT;
        return Level.toLevel(sArg, defaultLevel);
    }
}
