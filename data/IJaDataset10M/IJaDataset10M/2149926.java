package org.gamegineer.table.core;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.concurrent.atomic.AtomicInteger;
import net.jcip.annotations.ThreadSafe;

/**
 * Mock implementation of {@link org.gamegineer.table.core.ICardPileListener}.
 */
@ThreadSafe
public class MockCardPileListener implements ICardPileListener {

    /** The count of card added events received. */
    private final AtomicInteger cardAddedEventCount_;

    /** The count of card pile bounds changed events received. */
    private final AtomicInteger cardPileBoundsChangedEventCount_;

    /** The count of card removed events received. */
    private final AtomicInteger cardRemovedEventCount_;

    /**
     * Initializes a new instance of the {@code MockCardPileListener} class.
     */
    public MockCardPileListener() {
        cardAddedEventCount_ = new AtomicInteger(0);
        cardPileBoundsChangedEventCount_ = new AtomicInteger(0);
        cardRemovedEventCount_ = new AtomicInteger(0);
    }

    @Override
    public void cardAdded(final CardPileContentChangedEvent event) {
        assertArgumentNotNull(event, "event");
        cardAddedEventCount_.incrementAndGet();
    }

    @Override
    public void cardPileBoundsChanged(final CardPileEvent event) {
        assertArgumentNotNull(event, "event");
        cardPileBoundsChangedEventCount_.incrementAndGet();
    }

    @Override
    public void cardRemoved(final CardPileContentChangedEvent event) {
        assertArgumentNotNull(event, "event");
        cardRemovedEventCount_.incrementAndGet();
    }

    /**
     * Gets the count of card added events received.
     * 
     * @return The count of card added events received.
     */
    public final int getCardAddedEventCount() {
        return cardAddedEventCount_.get();
    }

    /**
     * Gets the count of card pile bounds changed events received.
     * 
     * @return The count of card pile bounds changed events received.
     */
    public final int getCardPileBoundsChangedEventCount() {
        return cardPileBoundsChangedEventCount_.get();
    }

    /**
     * Gets the count of card removed events received.
     * 
     * @return The count of card removed events received.
     */
    public final int getCardRemovedEventCount() {
        return cardRemovedEventCount_.get();
    }
}
