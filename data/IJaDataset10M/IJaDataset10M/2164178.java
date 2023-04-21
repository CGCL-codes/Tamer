package org.gudy.azureus2.pluginsimpl.local.ui.config;

import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.plugins.ui.config.IntParameter;
import org.gudy.azureus2.pluginsimpl.local.PluginConfigImpl;

public class IntParameterImpl extends ParameterImpl implements IntParameter {

    private int defaultValue;

    private boolean limited;

    private int min_value;

    private int max_value;

    public IntParameterImpl(PluginConfigImpl config, String key, String label, int defaultValue) {
        super(config, key, label);
        config.notifyParamExists(getKey());
        COConfigurationManager.setIntDefault(getKey(), defaultValue);
        this.defaultValue = defaultValue;
        this.limited = false;
    }

    public IntParameterImpl(PluginConfigImpl config, String key, String label, int defaultValue, int min_value, int max_value) {
        this(config, key, label, defaultValue);
        this.min_value = min_value;
        this.max_value = max_value;
        this.limited = true;
    }

    /**
	 * @return Returns the defaultValue.
	 */
    public int getDefaultValue() {
        return defaultValue;
    }

    public int getValue() {
        return (config.getUnsafeIntParameter(getKey(), getDefaultValue()));
    }

    public void setValue(int b) {
        config.setUnsafeIntParameter(getKey(), b);
    }

    public boolean isLimited() {
        return limited;
    }

    public int getMinValue() {
        return this.min_value;
    }

    public int getMaxValue() {
        return this.max_value;
    }
}
