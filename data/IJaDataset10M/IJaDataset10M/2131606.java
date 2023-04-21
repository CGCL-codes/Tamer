package org.yawlfoundation.yawl.elements;

/**
 * 
 * Abstract class. A super class of YExternalNetElement.   Has little relation to the
 * YAWL paper.
 * @author Lachlan Aldred
 * 
 */
public abstract class YNetElement implements Cloneable, Comparable {

    private String _id;

    /**
     * Constructor
     * @param id
     */
    protected YNetElement(String id) {
        _id = id;
    }

    /**
     * @return the id of this YNetElement
     */
    public String getID() {
        return _id;
    }

    public String toString() {
        String fullClassName = getClass().getName();
        String shortClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 2);
        return shortClassName + ":" + getID();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int compareTo(Object o) {
        YNetElement ne = (YNetElement) o;
        return getID().compareTo(ne.getID());
    }
}
