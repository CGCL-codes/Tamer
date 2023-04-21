package org.gamegineer.table.core;

import java.util.EventObject;
import net.jcip.annotations.ThreadSafe;

/**
 * An event fired by a card.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
@ThreadSafe
public class CardEvent extends EventObject {

    /** Serializable class version number. */
    private static final long serialVersionUID = 2812928653096081948L;

    /**
     * Initializes a new instance of the {@code CardEvent} class.
     * 
     * @param source
     *        The card that fired the event; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code source} is {@code null}.
     */
    public CardEvent(@SuppressWarnings("hiding") final ICard source) {
        super(source);
    }

    public final ICard getCard() {
        return (ICard) getSource();
    }
}
