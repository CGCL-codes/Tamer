package skycastle.util;

import junit.framework.TestCase;

/**
 * Test class for GridSnapper.
 *
 * @author Hans H�ggstr�m
 */
public class GridSnapperTest extends TestCase {

    private GridSnapper myGridSnapper = null;

    /**
     * Constructs an instance of ClassTest using default parameters.
     */
    public GridSnapperTest(String name) {
        super(name);
    }

    public void testGridSnapper() {
        myGridSnapper = new GridSnapper(4, 4, 0, 0, 0.5f);
        assertPositionUpdateWorks(5, 3, true, 1, 1, 4, 4);
        assertPositionUpdateWorks(1, 1, true, 0, 0, 0, 0);
        assertPositionUpdateWorks(0, 0, false, 0, 0, 0, 0);
        assertPositionUpdateWorks(2, 0, false, 0, 0, 0, 0);
        assertPositionUpdateWorks(2.001f, 0, true, 1, 0, 4, 0);
        myGridSnapper = new GridSnapper(3.15f, 3.15f, 0.2f, 0.2f);
        assertPositionUpdateWorks(0, 0, false, 0, 0, 0.2f, 0.2f);
        assertPositionUpdateWorks(3.2f, 2.9f, true, 1, 1, 3.15f + 0.2f, 3.15f + 0.2f);
        assertPositionUpdateWorks(7.1f, 0, true, 2, 0, 6.3f + 0.2f, 0.2f);
    }

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myGridSnapper = new GridSnapper(4);
    }

    /**
     * Tears down the fixture, for example, close a network connection.
     * This method is called after a test is executed.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void assertPositionUpdateWorks(final float x, final float z, final boolean positionShouldChange, final int expectedGridX, final int expectedGridZ, final float expectedWorldX, final float expectedWorldZ) {
        final boolean changed = myGridSnapper.updatePosition(x, z);
        if (positionShouldChange) {
            assertTrue("vector should change", changed);
        } else {
            assertFalse("vector should not change", changed);
        }
        assertGridLocation(expectedGridX, expectedGridZ);
        assertWorldLocation(expectedWorldX, expectedWorldZ);
    }

    private void assertGridLocation(final int expectedX, final int expectedZ) {
        final int gridX = myGridSnapper.getGridX();
        final int gridZ = myGridSnapper.getGridZ();
        assertEquals("The grid x coordinate should match.  ", expectedX, gridX);
        assertEquals("The grid z coordinate should match.  ", expectedZ, gridZ);
    }

    private void assertWorldLocation(final float expectedX, final float expectedZ) {
        final float gridX = myGridSnapper.getWorldX();
        final float gridZ = myGridSnapper.getWorldZ();
        assertEquals("The world x coordinate should match.  ", expectedX, gridX, 0.001);
        assertEquals("The world z coordinate should match.  ", expectedZ, gridZ, 0.001);
    }
}
