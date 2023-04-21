package com.uusee.framework.util.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:du_yi@bbn.cn">duyi</a>
 * 
 * Create Time: 2008-3-27 下午03:26:31
 */
public class CriteriaInfo {

    private List<Filter> filters = new ArrayList<Filter>();

    private List<Sort> sorts = new ArrayList<Sort>();

    private int firstResult = -1;

    private int maxResult = -1;

    private int pageNo = 1;

    private boolean offset = false;

    public void allEq(Map propertyNameValues) {
        filters.add(new Filter(propertyNameValues));
    }

    public void and(Filter logicCondition, Filter otherLogicConfig) {
        filters.add(new Filter(logicCondition, otherLogicConfig, Filter.AND));
    }

    public void between(String propertyName, Object lowValue, Object highValue) {
        filters.add(new Filter(propertyName, lowValue, highValue, Filter.BETWEEN));
    }

    public void eq(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.EQ));
    }

    public void eqProperty(String propertyName, String otherPropertyName) {
        filters.add(new Filter(propertyName, otherPropertyName, Filter.EQ_PROPERTY));
    }

    public void ge(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.GE));
    }

    public void geProperty(String propertyName, String otherPropertyName) {
        filters.add(new Filter(propertyName, otherPropertyName, Filter.GE_PROPERTY));
    }

    public void gt(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.GT));
    }

    public void gtProperty(String propertyName, String otherPropertyName) {
        filters.add(new Filter(propertyName, otherPropertyName, Filter.GT_PROPERTY));
    }

    public void idEq(Object value) {
        filters.add(new Filter(value));
    }

    public void in(String propertyName, Collection values) {
        filters.add(new Filter(propertyName, values, Filter.IN));
    }

    public void in(String propertyName, Object[] values) {
        filters.add(new Filter(propertyName, values, Filter.IN));
    }

    public void isEmpty(String propertyName) {
        filters.add(new Filter(propertyName, Filter.IS_EMPTY));
    }

    public void isNotEmpty(String propertyName) {
        filters.add(new Filter(propertyName, Filter.IS_NOT_EMPTY));
    }

    public void isNotNull(String propertyName) {
        filters.add(new Filter(propertyName, Filter.IS_NOT_NULL));
    }

    public void isNull(String propertyName) {
        filters.add(new Filter(propertyName, Filter.IS_NULL));
    }

    public void le(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.LE));
    }

    public void leProperty(String propertyName, String otherPropertyName) {
        filters.add(new Filter(propertyName, otherPropertyName, Filter.LE_PROPERTY));
    }

    public void like(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.LIKE));
    }

    public void rightlike(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.RIGHT_LIKE));
    }

    public void leftlike(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.LEFT_LIKE));
    }

    public void lt(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.LT));
    }

    public void ltProperty(String propertyName, String otherPropertyName) {
        filters.add(new Filter(propertyName, otherPropertyName, Filter.LT_PROPERTY));
    }

    public void ne(String propertyName, Object value) {
        filters.add(new Filter(propertyName, value, Filter.NE));
    }

    public void neProperty(String propertyName, String otherPropertyName) {
        filters.add(new Filter(propertyName, otherPropertyName, Filter.NE_PROPERTY));
    }

    public void not(Filter logicCondition) {
        filters.add(new Filter(logicCondition, null, Filter.NOT));
    }

    public void or(Filter logicCondition, Filter otherLogicConfig) {
        filters.add(new Filter(logicCondition, otherLogicConfig, Filter.OR));
    }

    public void addSort(String property, String order) {
        sorts.add(new Sort(property, order));
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public boolean isOffset() {
        return offset;
    }

    public void setOffset(boolean offset) {
        this.offset = offset;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
