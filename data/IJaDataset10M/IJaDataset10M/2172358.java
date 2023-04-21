package com.aloaproject.ciquta;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

/**
 * @author Daniele Demichelis
 */
public final class OrderImpl implements Order {

    private final String property;

    private boolean asc;

    public static final Order getOrder(String property, boolean asc) {
        return new OrderImpl(property, asc);
    }

    private OrderImpl(String property, boolean asc) {
        this.property = property;
        this.asc = asc;
    }

    public Comparator getComparator() {
        return new Comparator() {

            public int compare(Object obj1, Object obj2) {
                Object value1 = valueOf(obj1, property);
                Object value2 = valueOf(obj2, property);
                int c = ((Comparable) value1).compareTo((Comparable) value2);
                return (asc ? c : -c);
            }
        };
    }

    private final Object valueOf(Object obj, String property) {
        Object value;
        try {
            value = org.apache.commons.beanutils.PropertyUtils.getProperty(obj, property);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
        return value;
    }
}
