package net.liveseeds.base.world;

import junit.framework.TestCase;
import net.liveseeds.base.cell.Cell;
import net.liveseeds.base.BasePropertiesLoader;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class DispatchingResourceBalanceTest extends TestCase {

    private World world;

    private ResourceBalance resourceBalance;

    public DispatchingResourceBalanceTest() {
    }

    public DispatchingResourceBalanceTest(final String string) {
        super(string);
    }

    protected void setUp() throws Exception {
        super.setUp();
        world = new DefaultWorld();
        resourceBalance = new DispatchingResourceBalance();
        resourceBalance.start(world);
    }

    public void testBalance() throws Exception {
        final Cell cell = world.getCell(0, 0);
        final int amount = resourceBalance.getResourceBalanceTreshold() - cell.getResourceAmount();
        cell.putResource(1000 * amount);
        for (int i = 0; i < world.getCells().length; i++) {
            final Cell[] row = world.getCells()[i];
            for (int j = 0; j < row.length; j++) {
                final Cell currentCell = row[j];
                assertTrue(currentCell.getResourceAmount() <= resourceBalance.getResourceBalanceTreshold());
            }
        }
    }

    public void testGetResourceBalanceTreshold() {
        assertEquals(BasePropertiesLoader.resourceBalanceTreshold, resourceBalance.getResourceBalanceTreshold());
    }

    public void testGetResourceBalanceNormalized() {
        assertEquals(BasePropertiesLoader.resourceBalanceNormalized, resourceBalance.getResourceBalanceNormalized());
    }
}
