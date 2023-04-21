package prisms.ui.list;

/** Represents an action that can be performed on a {@link DataListNode} by the user */
public class NodeAction {

    private String theText;

    private boolean theMultiple;

    /**
	 * Creates a NodeAction
	 * 
	 * @param text The text to display for the action
	 * @param multiple Whether this action should be displayed for multiply selected nodes with this
	 *        action in common
	 */
    public NodeAction(String text, boolean multiple) {
        theText = text;
        theMultiple = multiple;
    }

    /** @return The text to use to represent this action */
    public String getText() {
        return theText;
    }

    /**
	 * @return Whether this action will be displayed for multiply selected nodes with this action in
	 *         common
	 */
    public boolean getMultiple() {
        return theMultiple;
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodeAction)) return false;
        NodeAction na = (NodeAction) o;
        return na.theText.equals(theText) && na.theMultiple == theMultiple;
    }
}
