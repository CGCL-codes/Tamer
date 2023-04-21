package org.xnap.commons.settings;

/**
 * @author Steffen Pingel
 */
public class BooleanSetting extends AbstractSetting<Boolean> {

    public BooleanSetting(SettingResource backend, String key, Boolean defaultValue, Validator validator) {
        super(backend, key, defaultValue, validator);
    }

    public BooleanSetting(SettingResource backend, String key, Boolean defaultValue) {
        super(backend, key, defaultValue, null);
    }

    protected Boolean fromString(String value) {
        return Boolean.valueOf(value);
    }

    protected String toString(Boolean value) {
        return value.toString();
    }
}
