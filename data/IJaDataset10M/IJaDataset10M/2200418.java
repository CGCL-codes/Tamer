package org.suse.swamp.designer.model;

import java.util.*;

/**
 * ShapesDiagram is the container for the whole diagram. 
 * It maintains a list of all included Shapes and can notify about changes to them.
 */
public class ShapesDiagram extends ModelElement {

    /** Property ID to use when a child is added to this diagram. */
    public static final String CHILD_ADDED_PROP = "ShapesDiagram.ChildAdded";

    /** Property ID to use when a child is removed from this diagram. */
    public static final String CHILD_REMOVED_PROP = "ShapesDiagram.ChildRemoved";

    private static final long serialVersionUID = 1;

    private Collection shapes = new Vector();

    /** 
		 * Add a shape to this diagram.
		 * @param s a non-null shape instance
		 * @return true, if the shape was added, false otherwise
		 */
    public boolean addChild(Shape s) {
        if (s != null && shapes.add(s)) {
            firePropertyChange(CHILD_ADDED_PROP, null, s);
            return true;
        }
        return false;
    }

    /** Return a List of Shapes in this diagram. */
    public List getChildren() {
        return new Vector(shapes);
    }

    /** Return a List of Nodes in this diagram. */
    public List getNodes() {
        Vector nodes = new Vector();
        for (Iterator it = shapes.iterator(); it.hasNext(); ) {
            Shape shape = (Shape) it.next();
            if (shape instanceof Node) {
                nodes.add(shape);
            }
        }
        return nodes;
    }

    /**
		 * Remove a shape from this diagram.
		 * @param s a non-null shape instance;
		 * @return true, if the shape was removed, false otherwise
		 */
    public boolean removeChild(Shape s) {
        if (s != null && shapes.remove(s)) {
            firePropertyChange(CHILD_REMOVED_PROP, null, s);
            return true;
        }
        return false;
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }
}
