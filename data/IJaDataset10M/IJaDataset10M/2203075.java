package tr.model.Item;

/**
 * Item selector.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public interface ItemSelector {

    /**
     * Determines whether or not a given item should be selected.
     * @param item The item
     * @return true iff the item should be selected.
     */
    public boolean isSelected(Item item);
}
