package org.gamegineer.table.internal.core;

import net.jcip.annotations.ThreadSafe;
import org.gamegineer.table.core.ICardPile;
import org.gamegineer.table.core.ITable;
import org.gamegineer.table.core.ITableContentChangedEvent;
import org.gamegineer.table.core.TableContentChangedEvent;

/**
 * Implementation of {@link org.gamegineer.table.core.TableContentChangedEvent}.
 */
@ThreadSafe
final class InternalTableContentChangedEvent extends TableContentChangedEvent {

    /** Serializable class version number. */
    private static final long serialVersionUID = 685439950163321404L;

    /**
     * The table content changed event implementation to which all behavior is
     * delegated.
     */
    private final ITableContentChangedEvent delegate_;

    /**
     * Initializes a new instance of the {@code
     * InternalTableContentChangedEvent} class.
     * 
     * @param delegate
     *        The table content changed event implementation to which all
     *        behavior is delegated; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code delegate} is {@code null}.
     */
    private InternalTableContentChangedEvent(final ITableContentChangedEvent delegate) {
        super(delegate.getTable());
        delegate_ = delegate;
    }

    /**
     * Creates a new instance of the {@code InternalTableContentChangedEvent}
     * class.
     * 
     * @param table
     *        The table that fired the event; must not be {@code null}.
     * @param cardPile
     *        The card pile associated with the event; must not be {@code null}.
     * 
     * @return A new instance of the {@code InternalTableContentChangedEvent}
     *         class; never {@code null}.
     */
    static InternalTableContentChangedEvent createTableContentChangedEvent(final ITable table, final ICardPile cardPile) {
        assert table != null;
        assert cardPile != null;
        return new InternalTableContentChangedEvent(new TableContentChangedEventDelegate(table, cardPile));
    }

    @Override
    public ICardPile getCardPile() {
        return delegate_.getCardPile();
    }

    @Override
    public ITable getTable() {
        return delegate_.getTable();
    }
}
