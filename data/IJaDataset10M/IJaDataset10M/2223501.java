package com.thyante.thelibrarian.components;

import com.thyante.thelibrarian.model.specification.MediumType;

/**
 * 
 * @author Matthias-M. Christen
 */
public interface IMediumGallery {

    /**
	 * Removes the items <code>rgItems</code> from the medium gallery.
	 * @param rgItems The items to remove
	 */
    public void removeItems(MediaGalleryItem[] rgItems);

    /**
	 * Removes all the items from the gallery.
	 */
    public void removeAllItems();

    /**
	 * Returns the group holding the items for the medium type <code>type</code>.
	 * @param type The medium type
	 * @return The composite containing all the items of <code>type</code>
	 */
    public MediaGalleryGroup getGroupForType(MediumType type);

    /**
	 * Returns all the groups of the gallery.
	 * @return The gallery groups
	 */
    public MediaGalleryGroup[] getGroups();
}
