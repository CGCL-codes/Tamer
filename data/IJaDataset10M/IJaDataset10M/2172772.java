package net.sf.doolin.gui.field.tree;

import java.util.Collection;

/**
 * Children definition for a node.
 * 
 * @author Damien Coraboeuf
 */
public interface Children {

    /**
	 * Computes the children objects from a parent
	 * 
	 * @param root
	 *            Parent object
	 * @return List of children objects
	 */
    Collection<?> getChildBeans(Object root);

    /**
	 * @return Node id for the children
	 */
    String getId();
}
