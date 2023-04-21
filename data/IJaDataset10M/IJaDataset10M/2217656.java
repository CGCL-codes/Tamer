package org.gamegineer.table.internal.ui.model;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.concurrent.atomic.AtomicInteger;
import net.jcip.annotations.ThreadSafe;

/**
 * Mock implementation of
 * {@link org.gamegineer.table.internal.ui.model.ICardPileModelListener}.
 */
@ThreadSafe
public class MockCardPileModelListener implements ICardPileModelListener {

    /** The count of card pile focus gained events received. */
    private final AtomicInteger cardPileFocusGainedEventCount_;

    /** The count of card pile focus lost events received. */
    private final AtomicInteger cardPileFocusLostEventCount_;

    /** The count of card pile model state changed events received. */
    private final AtomicInteger cardPileModelStateChangedEventCount_;

    /**
     * Initializes a new instance of the {@code MockCardPileModelListener}
     * class.
     */
    public MockCardPileModelListener() {
        cardPileFocusGainedEventCount_ = new AtomicInteger(0);
        cardPileFocusLostEventCount_ = new AtomicInteger(0);
        cardPileModelStateChangedEventCount_ = new AtomicInteger(0);
    }

    @Override
    public void cardPileFocusGained(final CardPileModelEvent event) {
        assertArgumentNotNull(event, "event");
        cardPileFocusGainedEventCount_.incrementAndGet();
    }

    @Override
    public void cardPileFocusLost(final CardPileModelEvent event) {
        assertArgumentNotNull(event, "event");
        cardPileFocusLostEventCount_.incrementAndGet();
    }

    @Override
    public void cardPileModelStateChanged(final CardPileModelEvent event) {
        assertArgumentNotNull(event, "event");
        cardPileModelStateChangedEventCount_.incrementAndGet();
    }

    /**
     * Gets the count of card pile focus gained events received.
     * 
     * @return The count of card pile focus gained events received.
     */
    public final int getCardPileFocusGainedEventCount() {
        return cardPileFocusGainedEventCount_.get();
    }

    /**
     * Gets the count of card pile focus lost events received.
     * 
     * @return The count of card pile focus lost events received.
     */
    public final int getCardPileFocusLostEventCount() {
        return cardPileFocusLostEventCount_.get();
    }

    /**
     * Gets the count of card pile model state changed events received.
     * 
     * @return The count of card pile model state changed events received.
     */
    public final int getCardPileModelStateChangedEventCount() {
        return cardPileModelStateChangedEventCount_.get();
    }
}
