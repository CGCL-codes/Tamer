package pcgen.rules.context;

import java.util.Map;

/**
 * The Class <code>MapChanges</code> is responsible for tracking changes to 
 * a map so that the changes can be committed or rolled back at a later 
 * stage. Items can be added to the map, removed from the map or the map 
 * can be cleared.
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class MapChanges<K, V> {

    private final Map<K, V> positive;

    private final Map<K, V> negative;

    private final boolean clear;

    /**
	 * Instantiates a new map changes object.
	 * 
	 * @param added the map of items added
	 * @param removed the map of items removed
	 * @param globallyCleared has the map been cleared
	 */
    public MapChanges(Map<K, V> added, Map<K, V> removed, boolean globallyCleared) {
        positive = added;
        negative = removed;
        clear = globallyCleared;
    }

    /**
	 * Includes global clear.
	 * 
	 * @return true, if the map was cleared
	 */
    public boolean includesGlobalClear() {
        return clear;
    }

    /**
	 * Checks if the change set is empty.
	 * 
	 * @return true, if is empty
	 */
    public boolean isEmpty() {
        return !clear && !hasAddedItems() && !hasRemovedItems();
    }

    /**
	 * Gets the map of added items.
	 * 
	 * @return the added items
	 */
    public Map<K, V> getAdded() {
        return positive;
    }

    /**
	 * Checks for added items.
	 * 
	 * @return true, if there are items to add
	 */
    public boolean hasAddedItems() {
        return positive != null && !positive.isEmpty();
    }

    /**
	 * Gets the map of removed items.
	 * 
	 * @return the removed items
	 */
    public Map<K, V> getRemoved() {
        return negative;
    }

    /**
	 * Checks for removed items.
	 * 
	 * @return true, if there are items to remove
	 */
    public boolean hasRemovedItems() {
        return negative != null && !negative.isEmpty();
    }
}
