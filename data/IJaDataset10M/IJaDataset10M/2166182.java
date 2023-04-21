package adt.spatial.test;

import com.infomatiq.jsi.Rectangle;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 * @param <T>
 *            element type
 * 
 */
public interface Traversal<T> {

    /**
	 * Method gets called for each element contained in the tree.
	 * 
	 * @param rectangle
	 *            the minimal bounding box of the element.
	 * @param element
	 *            the element itself.
	 */
    public void element(Rectangle rectangle, T element);

    /**
	 * Method gets called for each inner node in the tree.
	 * 
	 * @param rectangle
	 *            the minimal bounding box of the node.
	 */
    public void node(Rectangle rectangle);
}
