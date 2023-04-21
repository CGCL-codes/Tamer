package org.vizzini.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Provides unit tests for the <code>DefaultToken</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class DefaultTokenTest {

    /** First token. */
    protected IToken _token0;

    /** Second token. */
    protected IToken _token1;

    /** Third token. */
    protected IToken _token2;

    /**
     * Sets up the test fixture. (Called before every test case method.)
     *
     * @since  v0.4
     */
    @Before
    public void setUp() {
        _token0 = create(TestData.POSITION0, TestData.TOKEN_NAME0, TestData.VALUE0);
        _token0.setTeam(TestData.TEAM0);
        _token0.setAgent(TestData.AGENT0);
        _token1 = create(TestData.POSITION1, TestData.TOKEN_NAME1, TestData.VALUE1);
        _token1.setTeam(TestData.TEAM1);
        _token1.setAgent(TestData.AGENT1);
        _token2 = create(TestData.POSITION2, TestData.TOKEN_NAME2, TestData.VALUE2);
        _token2.setTeam(TestData.TEAM2);
        _token2.setAgent(TestData.AGENT0);
    }

    /**
     * Tears down the test fixture. (Called after every test case method.)
     *
     * @since  v0.4
     */
    @After
    public void tearDown() {
        _token0 = null;
        _token1 = null;
    }

    /**
     * Test the <code>clone()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testClone() {
        IToken clone = (IToken) _token0.clone();
        assertEquals(_token0.getName(), clone.getName());
        assertEquals(_token0.getPosition(), clone.getPosition());
        assertEquals(_token0.getValue(), clone.getValue());
        assertEquals(_token0.getAgent(), clone.getAgent());
        assertNotSame(_token0, clone);
    }

    /**
     * Test the <code>equals()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testEquals() {
        assertEquals(_token0, _token0);
        assertTrue(_token0.equals(_token0));
        assertFalse(_token0.equals(_token1));
        assertEquals(_token0, _token2);
        assertFalse(_token0.equals(null));
    }

    /**
     * Test the <code>getTeam()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testGetTeam() {
        assertEquals(TestData.TEAM0, _token0.getTeam());
    }

    /**
     * Test the <code>hashCode()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testHashCode() {
        assertEquals(_token0.hashCode(), _token0.hashCode());
        assertFalse(_token0.hashCode() == _token1.hashCode());
        assertEquals(_token0.hashCode(), _token2.hashCode());
    }

    /**
     * Test the <code>setAgent()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testSetAgent() {
        assertEquals(TestData.AGENT0, _token0.getAgent());
    }

    /**
     * Test the <code>setName()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testSetName() {
        assertEquals(TestData.TOKEN_NAME0, _token0.getName());
        Exception exception = null;
        try {
            _token0.setName(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>setPosition()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testSetPosition() {
        assertEquals(TestData.POSITION0, _token0.getPosition());
        _token0.setAgent(TestData.AGENT0);
        _token0.setPosition(TestData.POSITION1);
        Exception exception = null;
        try {
            _token0.setPosition(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>setTeam()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testSetTeam() {
        assertEquals(TestData.TEAM0, _token0.getTeam());
    }

    /**
     * Test the <code>setValue()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testSetValue() {
        assertEquals(TestData.VALUE0, _token0.getValue());
    }

    /**
     * Test the <code>toString()</code> method.
     *
     * @since  v0.1
     */
    @Test
    public void testToString() {
        String expected = _token0.getClass().getName() + " [_name=X,_value=42,_position=org.vizzini.game.IntegerPosition (1,2,3)," + "_team=org.vizzini.game.DefaultTeam [_name=team0]," + "_agent=org.vizzini.game.DefaultAgent [_name=fred,_score=12,_team=org.vizzini.game.DefaultTeam [_name=team0]]]";
        assertEquals(expected, _token0.toString());
    }

    /**
     * @param   position  Initial position.
     * @param   name      Name.
     * @param   value     Value.
     *
     * @return  a token of the appropriate type.
     *
     * @since   v0.1
     */
    protected IToken create(IPosition position, String name, int value) {
        return new DefaultToken(position, name, value);
    }
}
