package nl.utwente.ewi.tpl.runtime.nodeselection;

/**
 * A {@code Selector} represents a single selection step in a node selection.
 * The {@code Selector} can select a child, the current node, the direct
 * ancestor, the value, or all children. These types are indicated by
 * {@link SelectorType}. {@code Selector}s can be created via TPL with the
 * {@link SelectorConverter}.
 *
 * @author Emond Papegaaij
 */
public class Selector {

    /**
	 * The enumeration {@code SelectorType} indicates the type of a
	 * {@link Selector}.
	 *
	 * @author Emond Papegaaij
	 */
    public static enum SelectorType {

        /**
		 * The {@link Selector} selects a single child.
		 */
        IDENTIFIER, /**
		 * The {@link Selector} selects the current node.
		 */
        THIS, /**
		 * The {@link Selector} selects the direct ancestor.
		 */
        PARENT, /**
		 * The {@link Selector} selects the value of the current node.
		 */
        VALUE, /**
		 * The {@link Selector} selects all children.
		 */
        ALL
    }

    /**
	 * Contains the type of the {@code Selector}.
	 */
    private SelectorType type;

    /**
	 * Contains the text of the {@code Selector}. This is used to store the name
	 * of the child to select, when the type is {@link SelectorType#IDENTIFIER}.
	 */
    private String text;

    /**
	 * Creates a new {@code Selector}.
	 * @param type The type of the {@code Selector}.
	 * @param text The text of the {@code Selector}; tne name of the child to
	 * select.
	 */
    public Selector(SelectorType type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
	 * Returns the type of the {@code Selector}.
	 * @return The type of the {@code Selector}.
	 */
    public SelectorType getType() {
        return type;
    }

    /**
	 * Returns the text of the {@code Selector}. This is the name of the child to
	 * select, when the type is {@link SelectorType#IDENTIFIER}.
	 * @return The text of the {@code Selector}.
	 */
    public String getText() {
        return text;
    }

    /**
	 * Returns a string representation of the {@code Selector}.
	 * @return A string representation of the {@code Selector}.
	 */
    public String toString() {
        return text;
    }
}
