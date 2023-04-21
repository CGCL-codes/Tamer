package org.gamegineer.table.core;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.core.MockCardPileListener} class to ensure it
 * does not violate the contract of the
 * {@link org.gamegineer.table.core.ICardPileListener} interface.
 */
public final class MockCardPileListenerAsCardPileListenerTest extends AbstractCardPileListenerTestCase {

    /**
     * Initializes a new instance of the {@code
     * MockCardPileListenerAsCardPileListenerTest} class.
     */
    public MockCardPileListenerAsCardPileListenerTest() {
        super();
    }

    @Override
    protected ICardPileListener createCardPileListener() {
        return new MockCardPileListener();
    }
}
