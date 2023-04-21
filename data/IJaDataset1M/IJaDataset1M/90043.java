package maggames.games.seega;

import java.util.Properties;
import maggames.core.base.BaseGameEngine;
import junit.framework.TestCase;

/**
 * @author BenjaminPLee
 *
 */
public class SeegaGameEngineTest extends TestCase {

    private static SeegaGameEngine engine;

    protected void setUp() throws Exception {
        super.setUp();
        engine = new SeegaGameEngine();
        engine.initialize(new Properties());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        engine = null;
    }

    /**
	 * Test method for {@link maggames.games.seega.SeegaGameEngine#initialize(java.util.Properties)}.
	 */
    public final void testInitialize() {
        BaseGameEngine engine2 = new SeegaGameEngine();
        Properties props = new Properties();
        props.setProperty(engine.heightProp, "21");
        props.setProperty(engine.widthProp, "13");
        engine2.initialize(props);
        assertEquals(engine2.getGameBoard().getHeight(), 21);
        assertEquals(engine2.getGameBoard().getWidth(), 13);
    }

    /**
	 * Test method for {@link maggames.games.seega.SeegaGameEngine#checkForWin()}.
	 */
    public final void testCheckForWin() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link maggames.games.seega.SeegaGameEngine#checkTurn(maggames.core.GameTurn)}.
	 */
    public final void testCheckTurn() {
        fail("Not yet implemented");
    }

    /**
	 * Test method for {@link maggames.games.seega.SeegaGameEngine#takeTurn(maggames.core.GameTurn)}.
	 */
    public final void testTakeTurn() {
        fail("Not yet implemented");
    }
}
