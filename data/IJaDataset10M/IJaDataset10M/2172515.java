package sk.tuke.ess.depend;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ObjectDependencies {

    private final Set<Object> analyzedObjects;

    private ObjectDependencies() {
        this.analyzedObjects = new HashSet<Object>();
    }

    /**
     * Zistí objekty (a ich typy), na ktorých je daná skupinu objektov závislá (aj tranzitívne závislosti). 
     * @param objects objekty, pre ktoré zistí závislosti.
     * @return 
     * @throws DependenciesException 
     */
    public static ObjectDependencies dependenciesFor(Object... objects) throws DependenciesException {
        ObjectDependencies deps = new ObjectDependencies();
        deps.analyze(objects);
        return deps;
    }

    public Object[] getDependantObjects() {
        return analyzedObjects.toArray();
    }

    public Class<?>[] getDependantTypes() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (Object object : analyzedObjects) {
            classes.add(object.getClass());
        }
        return classes.toArray(new Class<?>[] {});
    }

    private void analyze(Object... objects) throws DependenciesException {
        for (Object object : objects) {
            if (isAcceptable(object)) {
                if (!object.getClass().isArray()) {
                    analyzedObjects.add(object);
                    analyzeObject(object);
                } else {
                    analyzeArray(object);
                }
            }
        }
    }

    private boolean isAcceptable(Object object) {
        return object != null && !isAnalyzed(object);
    }

    private boolean isAnalyzed(Object obj1) {
        return analyzedObjects.contains(obj1);
    }

    private void analyzeArray(Object object) throws DependenciesException {
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            analyze(Array.get(object, i));
        }
    }

    private void analyzeObject(Object object) throws DependenciesException {
        analyzeObject(object, object.getClass());
    }

    private void analyzeObject(Object object, Class<?>[] types) throws DependenciesException {
        for (Class<?> type : types) {
            analyzeObject(object, type);
        }
    }

    private void analyzeObject(Object object, Class<?> type) throws DependenciesException {
        if (type != null) {
            analyzeObject(object, type.getDeclaredFields());
            analyzeObject(object, type.getSuperclass());
            analyzeObject(object, type.getInterfaces());
        }
    }

    private void analyzeObject(Object object, Field[] fields) throws DependenciesException {
        for (Field field : fields) {
            analyze(getFieldValue(object, field));
        }
    }

    private Object getFieldValue(Object object, Field field) throws DependenciesException {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException ex) {
            throw new DependenciesException(ex);
        }
    }
}
