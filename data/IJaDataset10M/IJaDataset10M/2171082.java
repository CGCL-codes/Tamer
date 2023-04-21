package prisms.ui;

/** An event that may occur on a data structure */
public abstract class DataEvent {

    /** The type of an event */
    public static enum Type {

        /** An event for which a data item was added */
        ADD, /** An event for which a data item was removed */
        REMOVE, /** An event for which a data item was moved */
        MOVE, /** An event for which a data item was modified */
        CHANGE, /** An event for which the data structure should be completely refreshed */
        REFRESH
    }

    private final Type theType;

    /**
	 * Creates a DataEvent
	 * 
	 * @param type The type of the event
	 */
    public DataEvent(Type type) {
        theType = type;
    }

    /** @return This event's type */
    public Type getType() {
        return theType;
    }
}
