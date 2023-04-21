package net.sf.jmms;

import java.util.EventObject;

public class PlayerEvent extends EventObject {

    public static final int LOADING = 0;

    public static final int LOADED = 1;

    public static final int STARTED = 2;

    public static final int STOPPED = 3;

    public static final int UNLOADED = 4;

    public static final int ENDED = 5;

    public static final int POSITION_CHANGED = 6;

    public static final int SETTINGS_CHANGED = 7;

    public static final int INFO_CHANGED = 8;

    public static final int ERROR = 9;

    private static final String[] TYPE_NAMES = { "LOADING", "LOADED", "STARTED", "STOPPED", "UNLOADED", "ENDED", "POSITION_CHANGED", "SETTINGS_CHANGED", "INFO_CHANGED", "ERROR" };

    private int type;

    public PlayerEvent(MediaPlayer source, int type) {
        super(source);
        this.type = type;
    }

    public MediaPlayer getPlayer() {
        return (MediaPlayer) source;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return TYPE_NAMES[type];
    }

    public String toString() {
        return "PlayerEvent [source=" + source + ", type=" + getTypeName() + "]";
    }
}
