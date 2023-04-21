package net.sf.traser.client;

import net.sf.traser.common.Identifier;

/**
 * The item count record.
 * Is immutable therefore thread safe.
 * @author karnokd, 2007.12.12.
 * @version $Revision 1.0$
 */
public class ItemCount {

    /**
	 * The item identifier in string format.
	 */
    private final Identifier item;

    /**
	 * The count.
	 */
    private final Integer count;

    /**
	 * Constructor. Initializes the fields.
	 * @param item the item identifier in string format. cannot be null
	 * @param count the item count, can be null
	 */
    public ItemCount(Identifier item, Integer count) {
        this.item = item;
        this.count = count;
    }

    /**
	 * @return the item identifier in string representation
	 */
    public Identifier getItemID() {
        return item;
    }

    /**
	 * @return the count of items
	 */
    public Integer getCount() {
        return count;
    }
}
