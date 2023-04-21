package org.gamegineer.table.core;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the basic aspects of classes that implement the
 * {@link org.gamegineer.table.core.ICardListener} interface.
 */
public abstract class AbstractCardListenerTestCase {

    /** The card listener under test in the fixture. */
    private ICardListener listener_;

    /**
     * Initializes a new instance of the {@code AbstractCardListenerTestCase}
     * class.
     */
    protected AbstractCardListenerTestCase() {
        super();
    }

    protected abstract ICardListener createCardListener() throws Exception;

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        listener_ = createCardListener();
        assertNotNull(listener_);
    }

    /**
     * Ensures the {@code cardLocationChanged} method throws an exception when
     * passed a {@code null} event.
     */
    @Test(expected = NullPointerException.class)
    public void testCardLocationChanged_Event_Null() {
        listener_.cardLocationChanged(null);
    }

    /**
     * Ensures the {@code cardOrientationChanged} method throws an exception
     * when passed a {@code null} event.
     */
    @Test(expected = NullPointerException.class)
    public void testCardOrientationChanged_Event_Null() {
        listener_.cardOrientationChanged(null);
    }

    /**
     * Ensures the {@code cardSurfaceDesignsChanged} method throws an exception
     * when passed a {@code null} event.
     */
    @Test(expected = NullPointerException.class)
    public void testCardSurfaceDesignsChanged_Event_Null() {
        listener_.cardSurfaceDesignsChanged(null);
    }
}
