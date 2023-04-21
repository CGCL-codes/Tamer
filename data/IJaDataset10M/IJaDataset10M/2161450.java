package com.clican.pluto.cms.core.comparator;

import java.util.Comparator;
import org.apache.commons.beanutils.BeanUtils;

public class PropertyComparator<T> extends BaseComparator<T> implements Comparator<T> {

    private String propertyName;

    public PropertyComparator(String propertyName) {
        this.propertyName = propertyName;
    }

    public int compare(T o1, T o2) {
        try {
            return convertByOrder(BeanUtils.getProperty(o1, propertyName).compareTo(BeanUtils.getProperty(o2, propertyName)));
        } catch (Exception e) {
            log.error("", e);
        }
        return 0;
    }
}
