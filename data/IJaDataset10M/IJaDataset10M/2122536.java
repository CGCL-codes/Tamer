package prisms.ui.list;

/** An event that may occur on a list */
public class DataListEvent extends prisms.ui.DataEvent {

    private final DataListNode theNode;

    private int theIndex;

    /**
	 * Creates a DataListEvent
	 * 
	 * @param type The type of the event
	 * @param node The node that the event occurred on
	 */
    public DataListEvent(Type type, DataListNode node) {
        super(type);
        theNode = node;
    }

    /**
	 * Creates a DataListEvent
	 * 
	 * @param type The type of the event
	 * @param node The node that the event occurred on
	 * @param index The index that the node was added to or removed from
	 */
    public DataListEvent(Type type, DataListNode node, int index) {
        this(type, node);
        theIndex = index;
    }

    /** @return The node that this event occurred on */
    public DataListNode getNode() {
        return theNode;
    }

    /** @return The index that this event's node was added to or removed from */
    public int getIndex() {
        return theIndex;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(getType()).append(": ").append(theNode.getText());
        if (theIndex >= 0) ret.append(" at ").append(theIndex);
        return ret.toString();
    }
}
