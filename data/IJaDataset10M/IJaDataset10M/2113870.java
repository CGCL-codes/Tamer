package com.gr.staffpm.tasks.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.gr.staffpm.widget.jqgrid.model.SortInfo;
import com.gr.staffpm.widget.jqgrid.model.SortOrder;
import com.gr.staffpm.widget.jqgrid.util.ReflectionUtils;

/**
 * @author Graham Rhodes 9 May 2011 17:09:48
 *
 */
public class QueryUtils {

    private static class IOrderComparator<T extends Serializable> implements Comparator<T> {

        private final SortInfo order;

        public IOrderComparator(SortInfo order) {
            this.order = order;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compare(T o1, T o2) {
            try {
                Object ov1 = ReflectionUtils.getPropertyValue(o1, order.getProperty());
                Object ov2 = ReflectionUtils.getPropertyValue(o2, order.getProperty());
                if (ov1 instanceof Comparable && ov2 instanceof Comparable) {
                    Comparable<Object> c1 = (Comparable<Object>) ov1;
                    Comparable<Object> c2 = (Comparable<Object>) ov2;
                    if (order.getSortOrder().equals(SortOrder.asc)) return c1.compareTo(c2); else if (order.getSortOrder().equals(SortOrder.desc)) return c2.compareTo(c1);
                }
            } catch (Exception e) {
            }
            return 0;
        }

        ;
    }

    public static <T extends Serializable> T matchQuery(T bean, T searchBean, String... searchFields) {
        if (bean == null) return null;
        if (searchFields == null) return bean;
        for (String filter : searchFields) {
            String propertyName = filter;
            try {
                Object rValue = ReflectionUtils.getPropertyValue(searchBean, propertyName);
                ;
                if (rValue != null) {
                    Object value = getPropertyValue(bean, propertyName);
                    String strVale = value.toString();
                    if (!strVale.startsWith(rValue.toString())) {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return bean;
    }

    public static Object getPropertyValue(Object bean, String propertyPath) throws NoSuchFieldException {
        if (bean == null) throw new IllegalArgumentException("bean cannot be null");
        Field field = ReflectionUtils.getField(bean.getClass(), propertyPath);
        field.setAccessible(true);
        try {
            return (field.get(bean));
        } catch (IllegalAccessException e) {
            return (null);
        }
    }

    public static <T extends Serializable> List<T> sortList(List<T> list, SortInfo info) {
        IOrderComparator<T> comparator = null;
        if (info != null) comparator = new IOrderComparator<T>(info);
        if (comparator != null) {
            Collections.sort(list, comparator);
        }
        return list;
    }

    public static <T extends Serializable> List<T> findSubList(List<T> list, T searchBean, SortInfo info, String... searchFields) {
        if (list == null) return null;
        List<T> nlist = new ArrayList<T>();
        for (T bean : list) {
            T result = matchQuery(bean, searchBean, searchFields);
            if (result != null) {
                nlist.add(bean);
            }
        }
        return sortList(nlist, info);
    }
}
