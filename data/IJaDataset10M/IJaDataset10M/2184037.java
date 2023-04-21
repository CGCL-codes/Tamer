package org.objectstyle.cayenne.property;

/**
 * An ArcProperty that points to a single graph node.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public interface SingleObjectArcProperty extends ArcProperty {

    public void setTarget(Object source, Object target, boolean setReverse);
}
