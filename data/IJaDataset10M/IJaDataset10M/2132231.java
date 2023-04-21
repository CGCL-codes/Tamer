package com.thyante.thelibrarian.model;

import com.thyante.thelibrarian.model.specification.IItem;
import com.thyante.thelibrarian.model.specification.ITemplate;

/**
 * @author Matthias-M. Christen
 */
public interface ICollection {

    /**
	 * Creates a new <code>IItem</code>.
	 * @return
	 */
    public IItem createItem();

    /**
	 * Returns the underlying template of the collection.
	 * @return The template defining the fields for collection items
	 */
    public ITemplate getTemplate();
}
