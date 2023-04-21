package com.ixora.common.ui.preferences;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import com.ixora.common.ComponentConfigurationDefault;
import com.ixora.common.ConfigurationMgr;
import com.ixora.common.ComponentConfiguration;
import com.ixora.common.exception.FailedToLoadConfiguration;
import com.ixora.common.xml.XMLExternalizable;

/**
  * Use this class only if you invoke {@link ConfigurationMgr#registerConfiguration(String, ComponentConfigurationDefault)}
  * during application initialization with an instance of this class as the second parameter.
  * @author Daniel Moraru
  */
public class PreferencesConfiguration extends ComponentConfigurationDefault {

    private static final long serialVersionUID = -98567176101722690L;

    public PreferencesConfiguration() throws FailedToLoadConfiguration {
        super("config/" + PreferencesComponent.NAME + "/config.properties");
    }

    /**
	 * Overridden to create the property if not already there.
	 * @see com.ixora.common.typedproperties.TypedProperties#setObject(java.lang.String, java.lang.Object)
	 */
    public <T> void setObject(String key, T value) {
        if (!hasProperty(key)) {
            Integer type = null;
            if (value instanceof Integer) {
                type = TYPE_INTEGER;
            } else if (value instanceof Boolean) {
                type = TYPE_BOOLEAN;
            } else if (value instanceof Color) {
                type = TYPE_COLOR;
            } else if (value instanceof Date) {
                type = TYPE_DATE;
            } else if (value instanceof File) {
                type = TYPE_FILE;
            } else if (value instanceof Float) {
                type = TYPE_FLOAT;
            } else if (value instanceof String) {
                type = TYPE_STRING;
            } else if (value instanceof Serializable) {
                type = TYPE_SERIALIZABLE;
            } else if (value instanceof XMLExternalizable) {
                type = TYPE_XML_EXTERNALIZABLE;
            } else {
                throw new IllegalArgumentException("Object is of unsupported type.");
            }
            setProperty(key, type, false, false);
        }
        super.setObject(key, value);
    }

    /**
	 * Shorthand method. It is the same as invoking <code>ConfigurationMgr.get(PreferencesComponent.NAME)</code>.<p>
	 * NOTE: This method returns null if {@link ConfigurationMgr#registerConfiguration(String, ComponentConfigurationDefault)}
	 * was not invoked during application initialization with an instance of this class as the second parameter.
	 * @return
	 */
    public static PreferencesConfiguration get() {
        ComponentConfiguration ret = ConfigurationMgr.get(PreferencesComponent.NAME);
        if (ret instanceof PreferencesConfiguration) {
            return (PreferencesConfiguration) ret;
        }
        return null;
    }
}
