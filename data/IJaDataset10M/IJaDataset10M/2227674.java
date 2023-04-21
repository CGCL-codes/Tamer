package com.ibatis.sqlmap.engine.mapping.sql.dynamic.elements;

import com.ibatis.sqlmap.client.SqlMapException;
import java.lang.reflect.Array;
import java.util.*;

public class IterateContext implements Iterator {

    private static final String PROCESS_INDEX = "ProcessIndex";

    private static final String PROCESS_STRING = "ProcessString";

    private Iterator iterator;

    private int index = -1;

    private String property;

    private boolean allowNext = true;

    private boolean isFinal = false;

    private SqlTag tag;

    private IterateContext parent;

    private boolean someSubElementsHaveContent;

    private boolean isPrependEnabled;

    public IterateContext(Object collection, SqlTag tag, IterateContext parent) {
        this.parent = parent;
        this.tag = tag;
        if (collection instanceof Collection) {
            this.iterator = ((Collection) collection).iterator();
        } else if (collection instanceof Iterator) {
            this.iterator = ((Iterator) collection);
        } else if (collection.getClass().isArray()) {
            List list = arrayToList(collection);
            this.iterator = list.iterator();
        } else {
            throw new SqlMapException("ParameterObject or property was not a Collection, Array or Iterator.");
        }
    }

    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    public Object next() {
        index++;
        return iterator.next();
    }

    public void remove() {
        iterator.remove();
    }

    public int getIndex() {
        return index;
    }

    public boolean isFirst() {
        return index == 0;
    }

    public boolean isLast() {
        return iterator != null && !iterator.hasNext();
    }

    private List arrayToList(Object array) {
        List list = null;
        if (array instanceof Object[]) {
            list = Arrays.asList((Object[]) array);
        } else {
            list = new ArrayList();
            for (int i = 0, n = Array.getLength(array); i < n; i++) {
                list.add(Array.get(array, i));
            }
        }
        return list;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean isAllowNext() {
        return allowNext;
    }

    public void setAllowNext(boolean performIterate) {
        this.allowNext = performIterate;
    }

    public SqlTag getTag() {
        return tag;
    }

    public void setTag(SqlTag tag) {
        this.tag = tag;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public String getEndProperty() {
        if (parent != null) {
            int parentPropertyIndex = property.indexOf(parent.getProperty());
            if (parentPropertyIndex > -1) {
                int endPropertyIndex1 = property.indexOf(']', parentPropertyIndex);
                int endPropertyIndex2 = property.indexOf('.', parentPropertyIndex);
                return property.substring(parentPropertyIndex + Math.max(endPropertyIndex1, endPropertyIndex2) + 1, property.length());
            } else {
                return property;
            }
        } else {
            return property;
        }
    }

    protected Map processTagProperty(String tagProperty) {
        if (parent != null) {
            Map parentResult = parent.processTagProperty(tagProperty);
            return this.addIndex((String) parentResult.get(PROCESS_STRING), (Integer) parentResult.get(PROCESS_INDEX));
        } else {
            return this.addIndex(tagProperty, 0);
        }
    }

    public String addIndexToTagProperty(String tagProperty) {
        Map map = this.processTagProperty(tagProperty);
        return (String) map.get(PROCESS_STRING);
    }

    protected Map addIndex(String input, int startIndex) {
        if (input != null && input.startsWith("[")) {
            input = "_collection" + input;
        }
        String endProperty = getEndProperty() + "[";
        int propertyIndex = input.indexOf(endProperty, startIndex);
        int modificationIndex = 0;
        if (propertyIndex > -1) {
            if (input.charAt(propertyIndex + endProperty.length()) == ']') {
                input = input.substring(0, propertyIndex + endProperty.length()) + this.getIndex() + input.substring(propertyIndex + endProperty.length());
                modificationIndex = propertyIndex + endProperty.length();
            }
        }
        Map ret = new HashMap();
        ret.put(PROCESS_INDEX, new Integer(modificationIndex));
        ret.put(PROCESS_STRING, input);
        return ret;
    }

    public IterateContext getParent() {
        return parent;
    }

    public void setParent(IterateContext parent) {
        this.parent = parent;
    }

    public boolean someSubElementsHaveContent() {
        return someSubElementsHaveContent;
    }

    public void setSomeSubElementsHaveContent(boolean someSubElementsHaveContent) {
        this.someSubElementsHaveContent = someSubElementsHaveContent;
    }

    public boolean isPrependEnabled() {
        return isPrependEnabled;
    }

    public void setPrependEnabled(boolean isPrependEnabled) {
        this.isPrependEnabled = isPrependEnabled;
    }
}
