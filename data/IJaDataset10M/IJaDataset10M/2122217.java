package oss.jthinker.util;

/**
 * Associated trigger. Gets some value as initial and then is updated
 * accordingly with peer's changes.
 * 
 * @author iappel
 * @param T trigger state type
 */
public class AssociatedTrigger<T> extends Trigger<T> implements TriggerListener<T> {

    private final Trigger<T> _peer;

    /**
     * Creates a new AssociatedTrigger instance.
     * 
     * @param initialState initial state
     * @param peer peer to get subsequent values
     */
    public AssociatedTrigger(T initialState, Trigger<T> peer) {
        super(initialState);
        peer.addStateConsumer(this);
        _peer = peer;
    }

    /**
     * Sets trigger's value to peer's value.
     * @inheritDoc
     */
    public void stateChanged(TriggerEvent<? extends T> event) {
        if (event.getSource() == _peer) {
            setState(event.getState());
        }
    }
}
