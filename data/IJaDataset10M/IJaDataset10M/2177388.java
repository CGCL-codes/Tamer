package com.tetrasix.majix.uis;

/**
 *
 * @version 1.1
 */
public class MajixEvent {

    public static final int EVENT_UPDATE_STYLE_MAP = 1;

    public static final int EVENT_UNLOCK_STYLE_MAP = 1;

    private int _event_type;

    private Object _source;

    public MajixEvent(int event_type, Object source) {
        _event_type = event_type;
        _source = source;
    }

    public int getEventType() {
        return _event_type;
    }

    public Object getSource() {
        return _source;
    }
}
