package edu.cuny.egt.rps;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

/**
 * <p>
 * </p>
 * 
 * @author Jinzhong Niu
 */
public class RpsStrategyTest extends TestCase {

    static Logger logger = Logger.getLogger(RpsStrategyTest.class);

    public RpsStrategyTest(String name) {
        super(name);
    }

    public void testFight() {
        double rewards[] = null;
        rewards = RpsAction.fight(RpsAction.R, RpsAction.P);
        assertTrue(rewards[0] == -1 && rewards[1] == 1);
        rewards = RpsAction.fight(RpsAction.R, RpsAction.S);
        assertTrue(rewards[0] == 1 && rewards[1] == -1);
        rewards = RpsAction.fight(RpsAction.P, RpsAction.S);
        assertTrue(rewards[0] == -1 && rewards[1] == 1);
        rewards = RpsAction.fight(RpsAction.P, RpsAction.R);
        assertTrue(rewards[0] == 1 && rewards[1] == -1);
        rewards = RpsAction.fight(RpsAction.S, RpsAction.R);
        assertTrue(rewards[0] == -1 && rewards[1] == 1);
        rewards = RpsAction.fight(RpsAction.S, RpsAction.P);
        assertTrue(rewards[0] == 1 && rewards[1] == -1);
        rewards = RpsAction.fight(RpsAction.R, RpsAction.R);
        assertTrue(rewards[0] == 0 && rewards[1] == 0);
        rewards = RpsAction.fight(RpsAction.P, RpsAction.P);
        assertTrue(rewards[0] == 0 && rewards[1] == 0);
        rewards = RpsAction.fight(RpsAction.S, RpsAction.S);
        assertTrue(rewards[0] == 0 && rewards[1] == 0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(RpsStrategyTest.class);
    }
}
