package net.liveseeds.statistics;

import junit.framework.TestCase;
import net.liveseeds.base.world.DefaultWorld;
import net.liveseeds.base.world.World;
import net.liveseeds.base.DefaultDriver;
import net.liveseeds.base.Driver;
import net.liveseeds.base.liveseed.LiveSeed;
import net.liveseeds.base.cell.Cell;
import com.sosnoski.util.array.IntArray;
import java.util.Arrays;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class MutationStatisticsTest extends TestCase {

    private static final int ITERATIONS = 100;

    private Statistics statistics;

    private World world;

    private Driver driver;

    public MutationStatisticsTest() {
    }

    public MutationStatisticsTest(final String string) {
        super(string);
    }

    protected void setUp() throws Exception {
        super.setUp();
        statistics = new MutationStatistics();
        world = new DefaultWorld();
        driver = new DefaultDriver();
    }

    public void testGetWorld() throws Exception {
        statistics.init(driver, world);
        assertSame(world, statistics.getWorld());
    }

    public void testGetWorldException() throws Exception {
        try {
            statistics.getWorld();
            fail(StatisticsException.class.getName() + " was expected");
        } catch (StatisticsException e) {
        }
    }

    public void testGetDriver() throws Exception {
        statistics.init(driver, world);
        assertSame(driver, statistics.getDriver());
    }

    public void testGetDriverException() throws Exception {
        try {
            statistics.getWorld();
            fail(StatisticsException.class.getName() + " was expected");
        } catch (StatisticsException e) {
        }
    }

    public void testStatistics() throws Exception {
        statistics.init(driver, world);
        final IntArray array = new IntArray();
        for (int i = 0; i < ITERATIONS; i++) {
            array.add(getMutatedAmount());
            driver.moveTime();
        }
        assertTrue(Arrays.equals(array.toArray(), statistics.getStatistics()));
    }

    public void testMin() throws Exception {
        statistics.init(driver, world);
        int min = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            final int amount = getMutatedAmount();
            if (amount < min) {
                min = amount;
            }
            driver.moveTime();
        }
        assertEquals(min, statistics.getMin());
    }

    public void testGetMax() throws Exception {
        statistics.init(driver, world);
        int max = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            final int amount = getMutatedAmount();
            if (amount > max) {
                max = amount;
            }
            driver.moveTime();
        }
        assertEquals(max, statistics.getMax());
    }

    public void testGetMaxException() throws Exception {
        try {
            statistics.getMax();
            fail(StatisticsException.class.getName() + " was expected");
        } catch (StatisticsException e) {
        }
    }

    public void testGetMinException() throws Exception {
        try {
            statistics.getMin();
            fail(StatisticsException.class.getName() + " was expected");
        } catch (StatisticsException e) {
        }
    }

    public void testCleanup() throws Exception {
        statistics.init(driver, world);
        final IntArray array = new IntArray();
        for (int i = 0; i < ITERATIONS; i++) {
            array.add(getMutatedAmount());
            driver.moveTime();
        }
        statistics.cleanup();
        for (int i = 0; i < ITERATIONS; i++) {
            driver.moveTime();
        }
        assertTrue(Arrays.equals(array.toArray(), statistics.getStatistics()));
    }

    private int getMutatedAmount() {
        int result = 0;
        for (int i = 0; i < world.getCells().length; i++) {
            final Cell[] row = world.getCells()[i];
            for (int j = 0; j < row.length; j++) {
                final Cell cell = row[j];
                for (int k = 0; k < cell.getLiveSeeds().length; k++) {
                    final LiveSeed liveSeed = cell.getLiveSeeds()[k];
                    if (liveSeed.isMutant()) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public void testGetLength() throws Exception {
        assertEquals(0, statistics.getLength());
        statistics.init(driver, world);
        assertEquals(0, statistics.getLength());
        for (int i = 0; i < ITERATIONS; i++) {
            driver.moveTime();
        }
        assertEquals(ITERATIONS, statistics.getLength());
    }
}
