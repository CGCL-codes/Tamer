package mrusanov.fantasyruler.player.technology;

import junit.framework.TestCase;

public class LevelTechCostDecoratorTest extends TestCase {

    public TechLevelCostsDecorator decorator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        decorator = new TechLevelCostsDecorator(new LinearTechCost());
    }

    public void testGetPointsToLevel() {
        assertEquals(19, decorator.getTechPointsToLevel(10));
    }

    public void testGetLevel() {
        assertEquals(2, decorator.getLevel(4));
    }

    public void testTechPointToNextLevel() {
        assertEquals(2, decorator.getTechPointsToNextLevel(5));
    }

    public void testPercentToNextLevel() {
        assertEquals(0.5, decorator.getPercentToNextLevel(6), 0.01);
        assertEquals(0, decorator.getPercentToNextLevel(7), 0.01);
    }

    private static class LinearTechCost implements TechLevelCosts {

        @Override
        public int getTechPointsToLevel(int level) {
            return 2 * level - 1;
        }
    }
}
