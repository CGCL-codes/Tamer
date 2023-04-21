package com.gaoke.simpleerp.orm;

import org.apache.commons.lang.StringUtils;

/**
 * 与具体ORM实现无关的属性过滤条件封装类.
 * 
 * PropertyFilter主要记录页面中简单的搜索过滤条件,比Hibernate的Criterion要简单.
 * 
 * TODO:扩展其他对比方式如大于、小于及其他数据类型如数字和日期.
 * 
 * @author calvin
 */
public class PropertyFilter {

    /**
	 * 多个属性间OR关系的分隔符.
	 */
    private static final String OR_SEPARATOR = "_OR_";

    /**
	 * 属性比较类型.
	 */
    public enum MatchType {

        EQ, LIKE, LT, GT, LE, GE
    }

    private String[] propertyNames = null;

    private Object value;

    private MatchType matchType = MatchType.EQ;

    public PropertyFilter() {
    }

    /**
	 * 
	 * @param filterName EQ_S_NAME
	 * @param value
	 */
    public PropertyFilter(final String filterName, final Object value) {
        String matchTypeCode = StringUtils.substringBefore(filterName, "_");
        try {
            matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称没有按规则编写,无法得到属性比较类型.", e);
        }
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        propertyNames = StringUtils.split(propertyNameStr, PropertyFilter.OR_SEPARATOR);
        this.value = value;
    }

    /**
	 * 是否有多个属性
	 */
    public boolean isMultiProperty() {
        return (propertyNames.length > 1);
    }

    /**
	 * 获取唯一的属性名称.
	 */
    public String getPropertyName() {
        if (propertyNames.length > 1) throw new IllegalArgumentException("There are not only one property");
        return propertyNames[0];
    }

    /**
	 * 获取属性名称列表.
	 */
    public String[] getPropertyNames() {
        return propertyNames;
    }

    public Object getValue() {
        return value;
    }

    public MatchType getMatchType() {
        return matchType;
    }
}
