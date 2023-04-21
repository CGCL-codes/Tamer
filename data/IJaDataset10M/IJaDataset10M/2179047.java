package org.nvframe.util.settings;

import java.util.HashMap;
import java.util.Map;

public class SettingMap implements SettingItem {

    private Map<String, Setting> settingMap = new HashMap<String, Setting>();

    public SettingMap(Map<String, String> map) {
    }

    public Setting getSetting(String key) {
        return settingMap.get(key);
    }
}
