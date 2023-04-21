package ch.fusun.baron.core.rmi;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import ch.fusun.baron.core.command.CommandUtil;
import ch.fusun.baron.core.injection.Inject;
import ch.fusun.baron.core.injection.ReInjector;
import ch.fusun.baron.data.DataRegistry;

/**
 * Utility to set up the network
 */
public class KryoUtil {

    private static KryoUtil instance;

    @Inject
    DataRegistry dataRegistry;

    private final List<Class<?>> classes;

    /**
	 * Finds all classes that have to be sent over the net
	 */
    public KryoUtil() {
        ReInjector.getInstance().reInject(this);
        classes = new LinkedList<Class<?>>();
        addBasicClasses(classes);
        for (Class<?> commandClass : CommandUtil.getInstance().getCommandClasses()) {
            classes.add(commandClass);
            completeUsingClassesRec(commandClass, classes);
        }
        for (Class<?> dataUpdate : dataRegistry.getUpdateClasses()) {
            classes.add(dataUpdate);
            completeUsingClassesRec(dataUpdate, classes);
        }
        removeBasicClasses(classes);
        sortClasses(classes);
        System.out.println(classes);
    }

    private static void sortClasses(List<Class<?>> classes) {
        Collections.sort(classes, new Comparator<Class<?>>() {

            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    private static void removeBasicClasses(List<Class<?>> classes) {
        classes.remove(Integer.class);
        classes.remove(String.class);
        classes.remove(Boolean.class);
    }

    private static void addBasicClasses(List<Class<?>> classes) {
        classes.add(LinkedList.class);
        classes.add(ArrayList.class);
        classes.add(HashMap.class);
    }

    private void completeUsingClassesRec(Class<?> clazz, List<Class<?>> classes) {
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> c = field.getType();
            if (!c.isPrimitive() && !classes.contains(c)) {
                classes.add(c);
                completeUsingClassesRec(c, classes);
            }
            Type genericFieldType = field.getGenericType();
            checkGenericTypes(classes, genericFieldType);
        }
    }

    private void checkGenericTypes(List<Class<?>> classes, Type genericFieldType) {
        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            for (Type fieldArgType : fieldArgTypes) {
                if (fieldArgType instanceof Class) {
                    Class<?> fieldArgClass = (Class<?>) fieldArgType;
                    if (!classes.contains(fieldArgClass)) {
                        classes.add(fieldArgClass);
                        completeUsingClassesRec(fieldArgClass, classes);
                    }
                } else {
                    checkGenericTypes(classes, fieldArgType);
                }
            }
        }
    }

    /**
	 * @return The singleton instance
	 */
    public static KryoUtil getInstance() {
        if (instance == null) {
            instance = new KryoUtil();
        }
        return instance;
    }

    /**
	 * @return All the classes that are sent over the net
	 */
    public List<Class<?>> getClassesForRegistration() {
        return classes;
    }

    /**
	 * @param classes
	 *            The basis classes
	 * @return All the associated classes
	 */
    public List<Class<?>> retrieveAllClasses(List<Class<?>> classes) {
        List<Class<?>> result = new ArrayList<Class<?>>();
        addBasicClasses(result);
        for (Class<?> clazz : classes) {
            result.add(clazz);
            completeUsingClassesRec(clazz, result);
        }
        removeBasicClasses(result);
        sortClasses(result);
        return result;
    }
}
