package net.sf.csutils.core.query.tree;

/**
 * This class represents a logical negation.
 */
public class Negation {

    private final Object item;

    Negation(Object pItem) {
        item = pItem;
    }

    /**
	 * Returns the negated item.
	 * @return The negated item.
	 */
    public Object getItem() {
        return item;
    }
}
