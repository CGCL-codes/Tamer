package org.gamegineer.table.internal.core;

import org.gamegineer.table.core.AbstractCardTestCase;
import org.gamegineer.table.core.ICard;
import org.gamegineer.table.core.ITable;

/**
 * A fixture for testing the {@link org.gamegineer.table.internal.core.Card}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.table.core.ICard} interface.
 */
public final class CardAsCardTest extends AbstractCardTestCase {

    /**
     * Initializes a new instance of the {@code CardAsCardTest} class.
     */
    public CardAsCardTest() {
        super();
    }

    @Override
    protected ICard createCard(final ITable table) {
        return new Card(((Table) table).getTableContext());
    }

    @Override
    protected ITable createTable() {
        return new Table();
    }
}
