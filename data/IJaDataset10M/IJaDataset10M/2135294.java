package org.gamegineer.game.core.config;

import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the basic aspects of classes that implement the
 * {@link org.gamegineer.game.core.config.IPlayerConfiguration} interface.
 */
public abstract class AbstractPlayerConfigurationTestCase {

    /** The player configuration under test in the fixture. */
    private IPlayerConfiguration m_config;

    /**
     * Initializes a new instance of the
     * {@code AbstractPlayerConfigurationTestCase} class.
     */
    protected AbstractPlayerConfigurationTestCase() {
        super();
    }

    protected abstract IPlayerConfiguration createPlayerConfiguration() throws Exception;

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        m_config = createPlayerConfiguration();
        assertNotNull(m_config);
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_config = null;
    }

    /**
     * Ensures the {@code getRoleId} method does not return {@code null}.
     */
    @Test
    public void testGetRole_ReturnValue_NonNull() {
        assertNotNull(m_config.getRoleId());
    }

    /**
     * Ensures the {@code getUserId} method does not return {@code null}.
     */
    @Test
    public void testGetUser_ReturnValue_NonNull() {
        assertNotNull(m_config.getUserId());
    }
}
