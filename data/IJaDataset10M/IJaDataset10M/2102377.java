package com.grt192.event.component;

import com.grt192.sensor.canjaguar.GRTJagSwitch;

public class JagSwitchEvent {

    public static final int LEFT_PRESSED = 0;

    public static final int LEFT_RELEASED = 1;

    public static final int RIGHT_PRESSED = 2;

    public static final int RIGHT_RELEASED = 3;

    private GRTJagSwitch source;

    private int id;

    private String key;

    public JagSwitchEvent(GRTJagSwitch source, int id, String key) {
        this.source = source;
        this.id = id;
        this.key = key;
    }

    public GRTJagSwitch getSource() {
        return source;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
