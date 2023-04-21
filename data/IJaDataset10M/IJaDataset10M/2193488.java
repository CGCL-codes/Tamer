package org.gamegineer.common.internal.persistence.memento;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.internal.persistence.memento.Adapters} class.
 */
public final class AdaptersTest {

    /** The adapters under test in the fixture. */
    private Adapters m_adapters;

    /**
     * Initializes a new instance of the {@code AdaptersTest} class.
     */
    public AdaptersTest() {
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
        m_adapters = Adapters.getDefault();
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_adapters = null;
    }

    /**
     * Ensures the {@code register} method throws an exception when passed a
     * {@code null} adapter manager.
     */
    @Test(expected = NullPointerException.class)
    public void testRegister_Manager_Null() {
        m_adapters.register(null);
    }

    /**
     * Ensures the {@code unregister} method throws an exception when passed a
     * {@code null} adapter manager.
     */
    @Test(expected = NullPointerException.class)
    public void testUnregister_Manager_Null() {
        m_adapters.unregister(null);
    }
}
