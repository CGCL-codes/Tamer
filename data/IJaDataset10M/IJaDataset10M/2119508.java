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
import net.walend.grid.MutableArrayGrid2D;
import net.walend.grid.MutableGrid2D;
import net.walend.grid.Address2D;
import net.walend.grid.OffGridException;
import net.walend.grid.Address;
import net.walend.grid.Grid2D;
import net.walend.grid.Grid;

/**
 @author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
*/
public class MutableArrayGrid2DTest extends AbstractArrayGrid2DTest {

    public MutableArrayGrid2DTest(String testName) {
        super(testName);
    }

    protected Grid2D createGrid(Grid2D grid) {
        return new MutableArrayGrid2D(grid);
    }

    public void testPut() {
        MutableGrid2D grid = new MutableArrayGrid2D(3, 5);
        testGet(grid, 1, 3, null, false);
        Object result = grid.put(1, 3, "A");
        assertTrue("grid.put(1,3,'A') should return null, not " + result, result == null);
        testGet(grid, 1, 3, "A", false);
        result = grid.put(1, 3, "B");
        assertTrue("grid.put(1,3,'B') should return 'A', not " + result, EqualsTest.equals("A", result));
        testGet(grid, 1, 3, "B", false);
    }

    public void testTake() {
        MutableGrid2D grid = new MutableArrayGrid2D(3, 5);
        testGet(grid, 1, 3, null, false);
        Object result = grid.put(1, 3, "A");
        result = grid.take(1, 3);
        assertTrue("grid.take(1,3) should return 'A', not " + result, "A".equals(result));
        testGet(grid, 1, 3, null, false);
        result = grid.put(1, 3, "B");
        testGet(grid, 1, 3, "B", false);
        result = grid.take(1, 3);
        assertTrue("grid.take(1,3,'B') should return 'B', not " + result, EqualsTest.equals("B", result));
        testGet(grid, 1, 3, null, false);
    }

    public void testPutByAddress() {
        MutableGrid2D grid = new MutableArrayGrid2D(3, 5);
        testGet(grid, new Address2D(1, 3), null, false);
        Object result = grid.put(new Address2D(1, 3), "A");
        assertTrue("grid.put(new Address2D(1,3),'A') should return null, not " + result, result == null);
        testGet(grid, new Address2D(1, 3), "A", false);
        result = grid.put(new Address2D(1, 3), "B");
        assertTrue("grid.put(new Address2D(1,3),'B') should return 'A', not " + result, EqualsTest.equals("A", result));
        testGet(grid, new Address2D(1, 3), "B", false);
    }

    public void testTakeByAddress() {
        MutableGrid2D grid = new MutableArrayGrid2D(3, 5);
        testGet(grid, new Address2D(1, 3), null, false);
        Object result = grid.put(new Address2D(1, 3), "A");
        result = grid.take(new Address2D(1, 3));
        assertTrue("grid.take(new Address2D(1,3)) should return 'A', not " + result, "A".equals(result));
        testGet(grid, new Address2D(1, 3), null, false);
        result = grid.put(new Address2D(1, 3), "B");
        testGet(grid, new Address2D(1, 3), "B", false);
        result = grid.take(new Address2D(1, 3));
        assertTrue("grid.take(new Address2D(1,3),'B') should return 'B', not " + result, EqualsTest.equals("B", result));
        testGet(grid, new Address2D(1, 3), null, false);
    }

    public void testAdd() {
        try {
            Grid2D grid = new MutableArrayGrid2D(4, 4);
            grid.add("A");
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testAddAll() {
        try {
            Grid2D grid = new MutableArrayGrid2D(4, 4);
            Set set = new HashSet();
            set.add("A");
            grid.addAll(set);
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testRemove() {
        MutableGrid2D grid = new MutableArrayGrid2D(4, 4, "A");
        grid.put(2, 2, "B");
        grid.put(1, 1, "B");
        boolean result1 = grid.remove("A");
        assertTrue("Removing the fill object should never change the grid", !result1);
        boolean result2 = grid.remove("B");
        assertTrue("Removing B should return true", result2);
        testGet(grid, 2, 2, "B", false);
        boolean result3 = grid.remove("C");
        assertTrue("Removing C should return false", !result3);
    }

    public void testRemoveAll() {
        MutableGrid2D grid = new MutableArrayGrid2D(4, 4, "A");
        grid.put(3, 3, "B");
        grid.put(1, 1, "B");
        grid.put(1, 2, "C");
        Set set = new HashSet();
        set.add("A");
        boolean result1 = grid.removeAll(set);
        assertTrue("Removing the fill object should never change the grid", !result1);
        testContains(grid, "A", true);
        set.add("B");
        boolean result2 = grid.removeAll(set);
        assertTrue("Removing B should return true", result2);
        testContains(grid, "B", false);
        testContains(grid, "A", true);
        testContains(grid, "C", true);
    }

    public void testRetainAll() {
        MutableGrid2D grid = new MutableArrayGrid2D(4, 4, "A");
        grid.put(3, 3, "B");
        grid.put(1, 1, "B");
        grid.put(1, 2, "C");
        Set set = new HashSet();
        set.add("C");
        boolean result2 = grid.retainAll(set);
        assertTrue("Removing B should return true", result2);
        testContains(grid, "B", false);
        testContains(grid, "A", true);
        testContains(grid, "C", true);
    }

    public void testClear() {
        MutableGrid2D grid = new MutableArrayGrid2D(4, 4, "A");
        grid.put(3, 3, "B");
        grid.put(1, 1, "B");
        grid.put(1, 2, "C");
        testContains(grid, "B", true);
        testContains(grid, "A", true);
        testContains(grid, "C", true);
        grid.clear();
        testContains(grid, "B", false);
        testContains(grid, "A", true);
        testContains(grid, "C", false);
    }

    public void testConstructors() {
        Grid2D test1 = new MutableArrayGrid2D(3, 3, "A");
        Grid2D test2 = new MutableArrayGrid2D(3, 3);
        Grid2D test3 = new MutableArrayGrid2D(new Address2D(5, 5));
        Grid2D test4 = new MutableArrayGrid2D(new Address2D(4, 4), "B");
        Grid2D test5 = new MutableArrayGrid2D(test1);
        Grid2D test6 = new MutableArrayGrid2D(new Address2D(4, 4), test1);
        Grid2D test7 = new MutableArrayGrid2D(6, 6, test1);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new MutableArrayGrid2DTest("testConstructors"));
        suite.addTest(new MutableArrayGrid2DTest("testQueries"));
        suite.addTest(new MutableArrayGrid2DTest("testPut"));
        suite.addTest(new MutableArrayGrid2DTest("testTake"));
        suite.addTest(new MutableArrayGrid2DTest("testStripeIterators"));
        suite.addTest(new MutableArrayGrid2DTest("testPutByAddress"));
        suite.addTest(new MutableArrayGrid2DTest("testTakeByAddress"));
        suite.addTest(new MutableArrayGrid2DTest("testStripeIteratorByAddress"));
        suite.addTest(new MutableArrayGrid2DTest("testCollectionQueries"));
        suite.addTest(new MutableArrayGrid2DTest("testIterator"));
        suite.addTest(new MutableArrayGrid2DTest("testAddressIterator"));
        suite.addTest(new MutableArrayGrid2DTest("testAdd"));
        suite.addTest(new MutableArrayGrid2DTest("testRemove"));
        suite.addTest(new MutableArrayGrid2DTest("testAddAll"));
        suite.addTest(new MutableArrayGrid2DTest("testRemoveAll"));
        suite.addTest(new MutableArrayGrid2DTest("testRetainAll"));
        suite.addTest(new MutableArrayGrid2DTest("testClear"));
        return suite;
    }
}
