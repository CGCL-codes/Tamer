package de.gstpl.data;

import de.peathal.resource.L;

/**
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class MappedVariable {

    private Object defaultValue;

    private Class<?> clazz;

    private boolean userDefined;

    public MappedVariable(Class clazz, Object defaultValue, boolean userDefined) {
        setType(clazz);
        setDefaultValue(defaultValue);
        this.userDefined = userDefined;
    }

    public Class<?> getType() {
        return clazz;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isUserDefined() {
        return userDefined;
    }

    public String toString() {
        return "{" + L.tr("defaultValue:") + getDefaultValue() + ", type:" + getType() + ", userProperty:" + isUserDefined() + "}";
    }
}
