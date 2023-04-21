package org.gamegineer.table.internal.core;

import net.jcip.annotations.ThreadSafe;
import org.gamegineer.table.core.CardEvent;
import org.gamegineer.table.core.ICard;
import org.gamegineer.table.core.ICardEvent;

/**
 * Implementation of {@link org.gamegineer.table.core.CardEvent}.
 */
@ThreadSafe
final class InternalCardEvent extends CardEvent {

    /** Serializable class version number. */
    private static final long serialVersionUID = -4136351175727140117L;

    /** The card event implementation to which all behavior is delegated. */
    private final ICardEvent delegate_;

    /**
     * Initializes a new instance of the {@code InternalCardEvent} class.
     * 
     * @param delegate
     *        The card event implementation to which all behavior is delegated;
     *        must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code delegate} is {@code null}.
     */
    private InternalCardEvent(final ICardEvent delegate) {
        super(delegate.getCard());
        delegate_ = delegate;
    }

    /**
     * Creates a new instance of the {@code InternalCardEvent} class.
     * 
     * @param card
     *        The card that fired the event; must not be {@code null}.
     * 
     * @return A new instance of the {@code InternalCardEvent} class; never
     *         {@code null}.
     */
    static InternalCardEvent createCardEvent(final ICard card) {
        assert card != null;
        return new InternalCardEvent(new CardEventDelegate(card));
    }

    @Override
    public ICard getCard() {
        return delegate_.getCard();
    }
}
