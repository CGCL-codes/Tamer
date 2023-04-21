package net.jgf.settings;

import net.jgf.config.Config;
import net.jgf.config.Configurable;

/**
 * Map of stored settings
 */
@Configurable
public class BooleanSetting extends Setting<Boolean> {

    public Boolean parseValue(String value) {
        return Boolean.parseBoolean(value);
    }

    public String toString() {
        return Boolean.toString(this.getValue());
    }

    @Override
    public void readConfig(Config config, String configPath) {
        super.readConfig(config, configPath);
    }
}
