package net.walend.grid.test;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Modifier;
import junit.framework.TestSuite;
import junit.framework.Test;
import net.walend.toolkit.junit.TestCase;
import net.walend.collection.EqualsTest;
import net.walend.collection.HasState;
import net.walend.grid.MutableMapGrid2D;
import net.walend.grid.MutableGrid2D;
import net.walend.grid.Address2D;
import net.walend.grid.OffGridException;
import net.walend.grid.Address;
import net.walend.grid.MapGrid2D;
import net.walend.grid.Grid2D;
import net.walend.grid.Grid;

/**
 @author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
 @since 2001928
*/
public class MapGrid2DTest extends AbstractMapGrid2DTest {

    public MapGrid2DTest(String testName) {
        super(testName);
    }

    protected Grid2D createGrid(Grid2D grid) {
        return new MapGrid2D(grid);
    }

    public void testRemove() {
        try {
            MutableGrid2D setupGrid = new MutableMapGrid2D(4, 4, "A");
            setupGrid.put(2, 2, "B");
            setupGrid.put(1, 1, "B");
            Grid2D grid = createGrid(setupGrid);
            boolean result1 = grid.remove("A");
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testRemoveAll() {
        try {
            MutableGrid2D setupGrid = new MutableMapGrid2D(4, 4, "A");
            setupGrid.put(2, 2, "B");
            setupGrid.put(1, 1, "B");
            Grid2D grid = createGrid(setupGrid);
            Set goners = new HashSet();
            goners.add("A");
            goners.add("C");
            boolean result1 = grid.removeAll(goners);
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testRetainAll() {
        try {
            MutableGrid2D setupGrid = new MutableMapGrid2D(4, 4, "A");
            setupGrid.put(2, 2, "B");
            setupGrid.put(1, 1, "B");
            Grid2D grid = createGrid(setupGrid);
            Set goners = new HashSet();
            goners.add("A");
            goners.add("C");
            boolean result1 = grid.retainAll(goners);
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testClear() {
        try {
            MutableGrid2D setupGrid = new MutableMapGrid2D(4, 4, "A");
            setupGrid.put(2, 2, "B");
            setupGrid.put(1, 1, "B");
            Grid2D grid = createGrid(setupGrid);
            grid.clear();
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testConstructors() {
        MutableGrid2D setupGrid = new MutableMapGrid2D(4, 4, "A");
        MapGrid2D grid1 = new MapGrid2D(setupGrid);
        MapGrid2D grid2 = new MapGrid2D(new Address2D(6, 8), setupGrid);
        MapGrid2D grid3 = new MapGrid2D(8, 6, setupGrid);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new MapGrid2DTest("testConstructors"));
        suite.addTest(new MapGrid2DTest("testQueries"));
        suite.addTest(new MapGrid2DTest("testStripeIterators"));
        suite.addTest(new MapGrid2DTest("testStripeIteratorByAddress"));
        suite.addTest(new MapGrid2DTest("testCollectionQueries"));
        suite.addTest(new MapGrid2DTest("testIterator"));
        suite.addTest(new MapGrid2DTest("testAddressIterator"));
        suite.addTest(new MapGrid2DTest("testAdd"));
        suite.addTest(new MapGrid2DTest("testRemove"));
        suite.addTest(new MapGrid2DTest("testAddAll"));
        suite.addTest(new MapGrid2DTest("testRemoveAll"));
        suite.addTest(new MapGrid2DTest("testRetainAll"));
        suite.addTest(new MapGrid2DTest("testClear"));
        return suite;
    }
}
