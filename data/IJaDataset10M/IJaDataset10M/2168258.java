package org.cloner.deep;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.cloner.CloneException;

public abstract class DeepCloner {

    protected static DeepCloner beanCloner;

    protected static DeepCloner mapCloner = new MapCloner();

    protected static DeepCloner arrayCloner = new ArrayCloner();

    protected static DeepCloner listCloner = new ListCloner();

    protected static DeepCloner setCloner = new SetCloner();

    static {
        try {
            Class.forName("org.hibernate.SessionFactory");
            beanCloner = new HibernateBeanCloner();
        } catch (Throwable t) {
            beanCloner = new DefaultCloner();
        }
    }

    public static Object deepClone(Object bean, Class<?> clazz) {
        Map<Object, Object> cloned = new HashMap<Object, Object>();
        try {
            return beanCloner.doclone(bean, clazz, cloned);
        } catch (Exception e) {
            throw new CloneException("Cannot clone " + bean, e);
        }
    }

    protected abstract Object doclone(Object obj, Class<?> clazz, Map<Object, Object> cloned) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SecurityException, InvocationTargetException, NoSuchMethodException;
}
