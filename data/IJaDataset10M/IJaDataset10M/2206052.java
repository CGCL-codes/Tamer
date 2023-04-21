package com.mscg.settings;

import com.mscg.settings.listener.SettingsChangeListener;

public interface SettingsService {

    public void setValue(String key, String value);

    public String getValue(String key);

    public String getValue(String key, String defaultValue);

    public void addOnSettingsChangeListener(SettingsChangeListener listener);

    public void removeOnSettingsChangeListener(SettingsChangeListener listener);
}
