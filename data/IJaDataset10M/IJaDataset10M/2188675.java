package org.gamegineer.table.internal.ui.model;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.ui.model.MockTableModelListener} class
 * to ensure it does not violate the contract of the
 * {@link org.gamegineer.table.internal.ui.model.ITableModelListener} interface.
 */
public final class MockTableModelListenerAsTableModelListenerTest extends AbstractTableModelListenerTestCase {

    /**
     * Initializes a new instance of the {@code
     * MockTableModelListenerAsTableModelListenerTest} class.
     */
    public MockTableModelListenerAsTableModelListenerTest() {
        super();
    }

    @Override
    protected ITableModelListener createTableModelListener() {
        return new MockTableModelListener();
    }
}
