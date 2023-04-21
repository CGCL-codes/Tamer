package net.sourceforge.beanprocessor.objectcloner;

import net.sourceforge.beanprocessor.AbstractConfigBase;
import net.sourceforge.beanprocessor.Condition;

/**
 * @author Martin Marinschek (latest modification by $Author: tober $)
 * @version $Revision: 1.2 $ $Date: 2006/09/11 23:12:23 $
 */
public class CloneConfig extends AbstractConfigBase implements RTCloneConfig {

    public CloneConfig(RTCloneSettings defaultSettings) {
        super(defaultSettings);
    }

    protected CloneSettings getDefaultSettingsForModification() {
        return (CloneSettings) super.getDefaultSettingsForModification();
    }

    public CloneConfig put(Class type, CloneSettings settings) {
        super.put(type, settings);
        return this;
    }

    public CloneConfig put(Class type, CloneSettings settings, Condition condition) {
        super.put(type, settings, condition);
        return this;
    }

    public CloneConfig include(Class type, String... propertyNames) {
        super.include(type, propertyNames);
        return this;
    }

    public CloneConfig initializer(Class type, Initializer initializer) {
        if (!isSettingsListEmpty()) {
            throw new IllegalStateException("Initializers must be added prior to includes or excludes");
        }
        getDefaultSettingsForModification().initializer(type, initializer);
        return this;
    }

    public RTCloneSettings getSettings(Object object) {
        return (RTCloneSettings) super.getSettings(object);
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
