package cunei.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import cunei.config.Configuration;
import cunei.config.Parameter;
import cunei.config.StringParameter;
import cunei.config.SystemConfig;

public abstract class TypeOfTypes {

    private static final Configuration CFG_TYPE = SystemConfig.getInstance("Type");

    protected static class TypeOfTypesParameter<T extends TypeOfTypes> extends Parameter<T> {

        public static synchronized <T extends TypeOfTypes> TypeOfTypesParameter<T> get(final Configuration config, final String name, final T def) {
            @SuppressWarnings("unchecked") TypeOfTypesParameter<T> result = (TypeOfTypesParameter<T>) config.get(name);
            if (result == null) {
                result = new TypeOfTypesParameter<T>(config, name, def);
                config.register(result);
            }
            return result;
        }

        private TypeOfTypesParameter(final Configuration config, final String name, final T def) {
            super(config, name, def);
        }

        public boolean setValueFromString(String value) {
            setValue(TypesOfTypes.<T>valueOf(value));
            return true;
        }
    }

    private final int id;

    protected int indexId;

    private final String name;

    private Method get;

    protected TypeOfTypes(final int id, final String name) {
        this.id = id;
        this.indexId = id;
        this.name = name;
        String className = StringParameter.get(CFG_TYPE, name + ".Class", CasedString.class.getName()).getValue();
        try {
            get = Class.forName(className).getDeclaredMethod("get", new Class[] { String.class });
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load class " + className + ": " + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to instantiate get(String) method in " + className + ": " + e.getMessage());
        }
    }

    public final Type get(String name) {
        Type result = null;
        try {
            result = (Type) get.invoke(null, name);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke get(String) method: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access get(String) method: " + e.getMessage());
        }
        return result;
    }

    public final int getId() {
        return id;
    }

    public int getIndexId() {
        return indexId;
    }

    public String toString() {
        return name;
    }
}
