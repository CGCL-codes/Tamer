package de.nava.informa.impl.basic;

import de.nava.informa.core.ItemGuidIF;
import de.nava.informa.core.ItemIF;

/**
 * In-Memory implementation of the ItemGuidIF interface.
 *
 * @author Michael Harhen
 */
public class ItemGuid implements ItemGuidIF {

    private static final long serialVersionUID = -2439211599593399143L;

    private long id;

    private ItemIF item;

    private String location;

    private boolean permaLink;

    /**
   * Default constructor.
   * @param item the item
   */
    public ItemGuid(ItemIF item) {
        this(item, null, true);
    }

    public ItemGuid(ItemIF item, String location, boolean permaLink) {
        this.item = item;
        this.location = location;
        this.permaLink = permaLink;
    }

    public ItemIF getItem() {
        return item;
    }

    public void setItem(ItemIF item) {
        this.item = item;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPermaLink() {
        return permaLink;
    }

    public void setPermaLink(boolean permaLink) {
        this.permaLink = permaLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
