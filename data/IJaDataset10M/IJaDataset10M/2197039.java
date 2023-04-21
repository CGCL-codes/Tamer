package alice.tuplecentre;

/**
 * Represents a reaction which has been triggered
 * inside a tuple centre by a specific event
 *
 * @see Reaction
 * @see Event
 * @see TupleCentreVM
 *
 *
 * @version 1.0
 */
public class TriggeredReaction implements java.io.Serializable {

    /** the event triggering the reaction */
    public Event event;

    /** the reaction triggered */
    public Reaction reaction;

    public TriggeredReaction(Event ev, Reaction re) {
        event = ev;
        reaction = re;
    }
}
