package tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WorldofZuulTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests");
        suite.addTestSuite(ArmorTest.class);
        suite.addTestSuite(PixelToolTest.class);
        suite.addTestSuite(WeaponTest.class);
        suite.addTestSuite(HeroTest.class);
        suite.addTestSuite(RoomTest.class);
        suite.addTestSuite(TexturesTest.class);
        suite.addTestSuite(ScreenManagerTest.class);
        suite.addTestSuite(QuestToolTest.class);
        suite.addTestSuite(MonsterTest.class);
        suite.addTestSuite(PotionTest.class);
        suite.addTestSuite(PowerTest.class);
        return suite;
    }
}
