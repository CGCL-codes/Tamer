package net.sourceforge.mpango;

import net.sourceforge.mpango.battle.BattleEngineTest;
import net.sourceforge.mpango.entity.FleetTest;
import net.sourceforge.mpango.entity.ShieldTest;
import net.sourceforge.mpango.entity.UnitTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sourceforge.romulan.battle");
        suite.addTestSuite(ShieldTest.class);
        suite.addTestSuite(UnitTest.class);
        suite.addTestSuite(FleetTest.class);
        suite.addTestSuite(BattleEngineTest.class);
        return suite;
    }
}
