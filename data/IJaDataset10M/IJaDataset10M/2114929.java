package org.gamegineer.game.internal.ui;

import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the {@link org.gamegineer.game.internal.ui.Services}
 * class.
 */
public final class ServicesTest {

    /** The services under test in the fixture. */
    private Services m_services;

    /**
     * Initializes a new instance of the {@code ServicesTest} class.
     */
    public ServicesTest() {
        super();
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        m_services = Services.getDefault();
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_services = null;
    }

    /**
     * Ensures the {@code getGameSystemUiRegistry} method does not return
     * {@code null}, which validates the game system user interface registry
     * service was registered with OSGi correctly.
     */
    @Test
    public void testGetGameSystemUiRegistry_ReturnValue_NonNull() {
        assertNotNull(m_services.getGameSystemUiRegistry());
    }

    /**
     * Ensures the {@code open} method throws an exception when passed a
     * {@code null} bundle context.
     */
    @Test(expected = NullPointerException.class)
    public void testOpen_Context_Null() {
        m_services.open(null);
    }
}
