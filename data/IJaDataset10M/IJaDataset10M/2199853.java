package org.gjt.universe.tests;

import org.gjt.universe.*;
import junit.framework.*;

public class GameOptionsTest extends TestCase {

    public GameOptionsTest(String name) {
        super(name);
    }

    /** This method performs a test of setting the game scale object
     *  and ensuring that it is adopted both in the current instance
     *  of the game options and, after saving to the configuration file,
     *  any future instances.
     */
    public void testGameScales() {
        GameScale[] allScales = GameScale.ALL_SCALES;
        GameEngine.Initialize();
        GameOptions testGO = new GameOptions();
        GameEngine.Instance().registerGameOptions(testGO);
        for (int i = 0; i < allScales.length; i++) {
            GameScale gs = allScales[i];
            testGO.setGameScale(gs);
            assertEquals("Returned game scale should match one just set.", (Object) gs.getKey(), (Object) testGO.getGameScale().getKey());
            testGO.saveToConfig();
            testGO = new GameOptions();
            GameEngine.Instance().registerGameOptions(testGO);
            assertEquals("Restored game scale should match one just set.", (Object) gs.getKey(), (Object) testGO.getGameScale().getKey());
        }
    }
}
