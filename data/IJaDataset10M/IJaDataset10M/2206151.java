package com.hadeslee.audiotag.tag.datatype;

import java.util.*;

/**
 * A two way mapping between an id and a value
 */
public abstract class AbstractValuePair {

    protected final Map idToValue = new LinkedHashMap();

    protected final Map valueToId = new LinkedHashMap();

    protected final List valueList = new ArrayList();

    protected Iterator iterator = idToValue.keySet().iterator();

    protected String value;

    /**
     * Get list in alphabetical order
     */
    public List getAlphabeticalValueList() {
        return valueList;
    }

    public Map getIdToValueMap() {
        return idToValue;
    }

    public Map getValueToIdMap() {
        return valueToId;
    }

    /**
     *
     * @return the number of elements in the mapping
     */
    public int getSize() {
        return valueList.size();
    }
}
