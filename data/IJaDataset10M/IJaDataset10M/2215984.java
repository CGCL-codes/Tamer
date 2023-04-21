package astcentric.structure.bl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Repository of {@link DataFactory}s.
 */
public class DataFactoryRepository implements DataFactory<Object> {

    private final Map<Class<?>, DataFactory<Object>> _factories = new HashMap<Class<?>, DataFactory<Object>>();

    /**
   * Registers the specified factory for the specified class or interface.
   * The same factory will be used also for subclasses or classes implementing
   * the specified interface.
   */
    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> dataClass, DataFactory<T> dataFactory) {
        _factories.put(dataClass, (DataFactory<Object>) dataFactory);
    }

    public Data create(Object data) {
        if (data == null) {
            throw new IllegalArgumentException("Unspecified data.");
        }
        Class<? extends Object> dataClass = data.getClass();
        HashSet<Class<Object>> classes = new HashSet<Class<Object>>();
        DataFactory<Object> dataFactory = findFactory(dataClass, classes);
        if (dataFactory == null) {
            throw new IllegalArgumentException("Can not translate objects of class " + dataClass.getName());
        }
        for (Class<Object> clazz : classes) {
            register(clazz, dataFactory);
        }
        DataFactory<Object> datafactory = dataFactory;
        return datafactory.create(data);
    }

    @SuppressWarnings("unchecked")
    private DataFactory<Object> findFactory(Class<?> dataClass, Set<Class<Object>> classes) {
        if (dataClass == null) {
            return null;
        }
        DataFactory<Object> dataFactory = _factories.get(dataClass);
        if (dataFactory != null) {
            return dataFactory;
        }
        classes.add((Class<Object>) dataClass);
        Class<?>[] interfaces = dataClass.getInterfaces();
        for (Class<?> interfaze : interfaces) {
            dataFactory = findFactory(interfaze, classes);
            if (dataFactory != null) {
                return dataFactory;
            }
        }
        return findFactory(dataClass.getSuperclass(), classes);
    }
}
