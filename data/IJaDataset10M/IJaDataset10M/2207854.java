package mdes.slick.sui.event;

/**
 * Indicates that a component/object state has changed.
 *
 * @author davedes
 */
public class ChangeEvent extends EventObject {

    /**
     * Creates a new instance of ChangeEvent
     */
    public ChangeEvent(Object source) {
        super(source);
    }
}
