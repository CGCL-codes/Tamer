package net.sf.csutils.core.query.tree;

/**
 * This class represents a logical negation.
 */
public class UnaryMinus {

    private final Object item;

    UnaryMinus(Object pItem) {
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
