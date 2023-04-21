package cunei.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ValueOfParameter<T> extends Parameter<T> {

    public static synchronized <T> ValueOfParameter<T> get(final Configuration config, final String name, final Class<?> clazz, final T def) {
        @SuppressWarnings("unchecked") ValueOfParameter<T> result = (ValueOfParameter<T>) config.get(name);
        if (result == null) {
            Method valueOfMethod;
            try {
                valueOfMethod = clazz.getMethod("valueOf", new Class[] { String.class });
            } catch (Exception e) {
                valueOfMethod = null;
            }
            result = new ValueOfParameter<T>(config, name, valueOfMethod, def);
            config.register(result);
        }
        return result;
    }

    private Method valueOfMethod;

    private ValueOfParameter(Configuration config, String name, Method valueOfMethod, T def) {
        super(config, name, def);
        this.valueOfMethod = valueOfMethod;
    }

    public synchronized String getValueString() {
        return super.getValueString().toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public boolean setValueFromString(String value) {
        if (value == null || valueOfMethod == null) return false;
        try {
            setValue((T) valueOfMethod.invoke(null, value.toUpperCase()));
        } catch (IllegalAccessException e) {
            return false;
        } catch (InvocationTargetException e) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "{ENUMPARAM " + getName() + "=" + getValue() + "}";
    }
}
