package com.textflex.onthemark;

/**
 *
 * @author david
 */
public class ConfigData {

    private String event = "";

    private String shortcut = "";

    public ConfigData(String aEvent, String aShortcut) {
        event = aEvent;
        shortcut = aShortcut;
    }

    public String getEvent() {
        return event;
    }

    public String getShortcut() {
        return shortcut;
    }
}
