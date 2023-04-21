package net.jgf.settings;

import net.jgf.config.ConfigException;
import net.jgf.system.Jgf;

/**
 * SettingHandler provides access to Settings.
 * 
 * @see Settings
 */
public class SettingHandler<T> {

    private T value;

    private Setting<T> setting;

    private Setting<T> internalSetting;

    private String settingReferenceName;

    private boolean isSettingReference;

    public SettingHandler(Class<? extends Setting<T>> settingClass) {
        try {
            this.internalSetting = settingClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new ConfigException("Cannot create instance of setting class '" + settingClass.getName() + "'", e);
        } catch (InstantiationException e) {
            throw new ConfigException("Cannot create instance of setting class '" + settingClass.getName() + "'", e);
        }
    }

    public SettingHandler(Class<? extends Setting<T>> settingClass, String readValue) {
        this(settingClass);
        this.readValue(readValue);
    }

    /**
     * This supports resolution of setting references with format:
     * #{setting/name}
     * @param value
     */
    public void readValue(String value) {
        String pvalue = value.trim();
        if (("#{".equals(pvalue.substring(0, 2))) && ("}".equals(pvalue.substring(pvalue.length() - 1)))) {
            String settingName = pvalue.substring(2, pvalue.length() - 1).trim();
            Jgf.getDirectory().unregister(this, "setting");
            this.setSetting(null);
            this.settingReferenceName = settingName;
            Jgf.getDirectory().register(this, "setting", settingName);
        } else {
            this.isSettingReference = false;
            this.setValue(internalSetting.parseValue(pvalue));
        }
    }

    public void setValue(T value) {
        if (isSettingReference) {
            if (setting == null) {
                throw new ConfigException("SettingHandler references a setting with id '" + settingReferenceName + "' but no setting with that id was found.");
            }
            setting.setValue(value);
        } else {
            this.value = value;
        }
    }

    public T getValue() {
        if (isSettingReference) {
            if (setting == null) {
                throw new ConfigException("SettingHandler references a setting with id '" + settingReferenceName + "' but no setting with that id was found.");
            }
            return setting.getValue();
        } else {
            return value;
        }
    }

    public T getDefaultValue() {
        if (isSettingReference) {
            if (setting == null) {
                throw new ConfigException("SettingHandler references a setting with id '" + settingReferenceName + "' but no setting with that id was found.");
            }
            return setting.getDefaultValue();
        } else {
            return null;
        }
    }

    public String getLabel() {
        if (isSettingReference) {
            if (setting == null) {
                throw new ConfigException("SettingHandler references a setting with id '" + settingReferenceName + "' but no setting with that id was found.");
            }
            return setting.getLabel();
        } else {
            return null;
        }
    }

    public void setSetting(Setting<T> setting) {
        this.isSettingReference = true;
        this.setting = setting;
    }
}
