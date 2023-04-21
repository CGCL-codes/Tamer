package org.gamegineer.tictactoe.core;

import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the basic aspects of classes that implement the
 * {@link org.gamegineer.tictactoe.core.ISquare} interface.
 */
public abstract class AbstractSquareTestCase {

    /** The square under test in the fixture. */
    private ISquare m_square;

    /**
     * Initializes a new instance of the {@code AbstractSquareTestCase} class.
     */
    protected AbstractSquareTestCase() {
        super();
    }

    protected abstract ISquare createSquare() throws Exception;

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        m_square = createSquare();
        assertNotNull(m_square);
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_square = null;
    }

    /**
     * Ensures the {@code getId} method does not return {@code null}.
     */
    @Test
    public void testGetId_ReturnValue_NonNull() {
        assertNotNull(m_square.getId());
    }
}
