package org.isistan.flabot.util.edition;

import org.eclipse.swt.widgets.Composite;

/**
 * A container for edition tabs
 * @author $Author: dacostae $
 *
 */
public interface CompositeEditionItem<T> extends EditionItem<T> {

    /** 
	 * Adds a new edition item
	 * @param editionItem
	 */
    boolean addEditionItem(EditionItem<T> editionItem);

    /**
	 * Removes an existing edition item
	 * @param editionItem
	 */
    boolean removeEditionItem(EditionItem<T> editionItem);

    /**
	 * The container this composite provides for children EditionItems
	 */
    Composite getChildContainer();
}
