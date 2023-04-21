package com.velocityme.valueobjects;

import java.util.Comparator;

/**
 *
 * @author  Robert
 */
public class GroupValueToString extends GroupValue implements Comparable {

    /** Creates a new instance of GroupValueToString */
    public GroupValueToString(GroupValue p_value) {
        super(p_value);
    }

    public String toString() {
        return getNodeValue().getName();
    }

    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }
}
