package com.market.b2c.suport.util.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ���Բ�����
 * author: zhangde 
 * date:   Jul 5, 2009 
 */
public class PropertyFinder implements Finder {

    private Class cls;

    private Map<String, Object> parameters;

    private Map<String, Short> comparators;

    public PropertyFinder(Class cls) {
        this.cls = cls;
    }

    public void addParameter(String name, Object value) {
        if (parameters == null) parameters = new HashMap<String, Object>();
        parameters.put(name, value);
    }

    public void addParameter(String name, Object value, short comparator) {
        if (parameters == null) parameters = new HashMap<String, Object>();
        parameters.put(name, value);
        comparators.put(name, new Short(comparator));
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String getSql() {
        StringBuffer sql = new StringBuffer("select * from ").append(cls.getSimpleName()).append(" where 1=1");
        Iterator iterator = parameters.keySet().iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            Short comparator = null;
            if (comparators != null) comparator = comparators.get(propertyName);
            sql.append(" and ").append(propertyName).append(conversionComparator(comparator)).append(" ? ");
        }
        return sql.toString();
    }

    private String conversionComparator(Short comparator) {
        if (comparator == null || comparator == Finder.EQ) return " = ";
        if (comparator.shortValue() == Finder.GT) return " > ";
        if (comparator.shortValue() == Finder.LS) return " < ";
        return null;
    }

    @Override
    public Class getCls() {
        return cls;
    }
}
