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
import net.walend.grid.ObjectNotOnGridException;

/**
 @author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
*/
public abstract class AbstractArrayGrid2DTest extends TestCase {

    public AbstractArrayGrid2DTest(String testName) {
        super(testName);
    }

    public void testSizeI(Grid2D grid, int expected) {
        assertTrue("Expected grid.sizeI() to be " + expected + " but got " + grid.sizeI(), grid.sizeI() == expected);
    }

    public void testSizeJ(Grid2D grid, int expected) {
        assertTrue("Expected grid.sizeJ() to be " + expected + " but got " + grid.sizeJ(), grid.sizeJ() == expected);
    }

    public void testGet(Grid2D grid, int i, int j, Object expected, boolean offGrid) {
        try {
            assertTrue(grid.toString() + ".get(" + i + "," + j + ") is " + grid.get(i, j) + " not " + expected, EqualsTest.equals(grid.get(i, j), expected));
            if (offGrid) {
                fail("Should have thrown an OffGridException");
            }
        } catch (OffGridException oge) {
            if (!offGrid) {
                fail(oge);
            }
        }
    }

    public void testSameGrid2DAs(Grid2D grid1, Grid2D grid2, boolean expected) {
        assertTrue("grid1.sameGrid2DAs(grid2) is " + grid1.sameGrid2DAs(grid2) + " but should be " + expected, grid1.sameGrid2DAs(grid2) == expected);
    }

    public void testSameStateAs(Grid2D grid1, HasState ob, boolean expected) {
        assertTrue("grid1.sameStateAs(ob) is " + grid1.sameStateAs(ob) + " but should be " + expected, grid1.sameStateAs(ob) == expected);
    }

    public void testMaxAddress(Grid grid, Address expected) {
        assertTrue("grid.maxAddress() is " + grid.maxAddress() + " not " + expected, grid.maxAddress().equals(expected));
    }

    public void testContainsAddress(Grid grid, Address address, boolean expected) {
        assertTrue("grid.containsAddress(" + address + ") is " + grid.containsAddress(address) + " not " + expected, grid.containsAddress(address) == expected);
    }

    public void testGet(Grid grid, Address address, Object expected, boolean offGrid) {
        try {
            assertTrue(grid.toString() + ".get(" + address + ") is " + grid.get(address) + " not " + expected, EqualsTest.equals(grid.get(address), expected));
            if (offGrid) {
                fail("Should have thrown an OffGridException");
            }
        } catch (OffGridException oge) {
            if (!offGrid) {
                fail(oge);
            }
        }
    }

    public void testAddressOf(Grid grid, Object object, Address expected, boolean offGrid) {
        try {
            assertTrue(grid.toString() + ".addressOf(" + object + ") is " + grid.addressOf(object) + " not " + expected, EqualsTest.equals(grid.addressOf(object), expected));
            if (offGrid) {
                fail("Should have thrown an ObjectNotOnGridException");
            }
        } catch (ObjectNotOnGridException oge) {
            if (!offGrid) {
                fail(oge);
            }
        }
    }

    protected abstract Grid2D createGrid(Grid2D grid);

    public void testQueries() {
        MutableGrid2D setupGrid = new MutableArrayGrid2D(3, 5);
        Grid2D grid = createGrid(setupGrid);
        testSizeI(grid, 3);
        testSizeJ(grid, 5);
        setupGrid.put(1, 3, "A");
        grid = createGrid(setupGrid);
        grid.toString();
        testGet(grid, 1, 3, "A", false);
        testGet(grid, 1, 4, null, false);
        testGet(grid, 1, 6, null, true);
        testGet(grid, 6, 1, null, true);
        testGet(grid, 6, 6, null, true);
        testGet(grid, -1, 1, null, true);
        testGet(grid, 1, -1, null, true);
        setupGrid = new MutableArrayGrid2D(3, 5);
        Grid2D grid2 = createGrid(setupGrid);
        testSameGrid2DAs(grid, grid2, false);
        testSameStateAs(grid, grid2, false);
        setupGrid.put(1, 3, "A");
        grid2 = createGrid(setupGrid);
        testSameGrid2DAs(grid, grid2, true);
        testSameStateAs(grid, grid2, true);
        setupGrid = new MutableArrayGrid2D(6, 2, "C");
        Grid2D grid3 = createGrid(setupGrid);
        assertTrue("grid3.getFillObject() is " + grid3.getFillObject() + " but should be C", "C".equals(grid3.getFillObject()));
        assertTrue("grid3 should contain Grid2D.I, but doesn't", grid3.containsDimension(Grid2D.I));
        assertTrue("grid3 should contain Grid2D.J, but doesn't", grid3.containsDimension(Grid2D.J));
        assertTrue("grid3.getDimensions() is " + grid3.getDimensions() + " not " + Grid2D.Dimensions2D.IT, grid3.getDimensions().equals(Grid2D.Dimensions2D.IT));
        testMaxAddress(grid3, new Address2D(5, 1));
        testContainsAddress(grid3, new Address2D(0, 0), true);
        testContainsAddress(grid3, new Address2D(5, 1), true);
        testContainsAddress(grid3, new Address2D(4, 0), true);
        testContainsAddress(grid3, new Address2D(-1, 0), false);
        testContainsAddress(grid3, new Address2D(0, -1), false);
        testContainsAddress(grid3, new Address2D(5, 3), false);
        testContainsAddress(grid3, new Address2D(2, 7), false);
        testContainsAddress(grid3, new Address2D(7, 7), false);
        setupGrid.put(4, 0, "B");
        grid3 = createGrid(setupGrid);
        testGet(grid3, new Address2D(0, 0), "C", false);
        testGet(grid3, new Address2D(5, 1), "C", false);
        testGet(grid3, new Address2D(4, 0), "B", false);
        testGet(grid3, new Address2D(-1, 0), "C", true);
        testGet(grid3, new Address2D(0, -1), "C", true);
        testGet(grid3, new Address2D(5, 3), "C", true);
        testGet(grid3, new Address2D(2, 7), "C", true);
        testGet(grid3, new Address2D(7, 7), "C", true);
        testAddressOf(grid3, "B", new Address2D(4, 0), false);
        testAddressOf(grid3, "D", new Address2D(4, 0), true);
        assertTrue("grid3.getPrincipleInterface() is " + grid3.getPrincipleInterface() + " not " + net.walend.grid.Grid2D.class, grid3.getPrincipleInterface().equals(net.walend.grid.Grid2D.class));
    }

    public void testIterator(Iterator it, List expected) {
        int spot = 0;
        while (it.hasNext()) {
            Object next = it.next();
            assertTrue("it.next() returned " + next + " not " + expected.get(spot), EqualsTest.equals(next, expected.get(spot)));
            spot++;
        }
        assertTrue("Expected " + expected.size() + " in the iteration. Only got " + (spot), spot == expected.size());
    }

    public void testStripeIterators() {
        MutableGrid2D setupGrid = new MutableArrayGrid2D(4, 4);
        setupGrid.put(0, 0, "a");
        setupGrid.put(0, 1, "b");
        setupGrid.put(0, 2, "c");
        setupGrid.put(0, 3, "d");
        setupGrid.put(1, 0, "e");
        setupGrid.put(1, 1, "f");
        setupGrid.put(1, 2, "g");
        setupGrid.put(1, 3, "h");
        setupGrid.put(2, 0, "i");
        setupGrid.put(2, 1, "j");
        setupGrid.put(2, 2, "k");
        setupGrid.put(2, 3, "l");
        setupGrid.put(3, 0, "m");
        setupGrid.put(3, 1, "n");
        setupGrid.put(3, 2, "o");
        setupGrid.put(3, 3, "p");
        Grid2D grid = createGrid(setupGrid);
        List i1list = new ArrayList();
        i1list.add("e");
        i1list.add("f");
        i1list.add("g");
        i1list.add("h");
        Iterator iit = grid.iStripeIterator(1);
        testIterator(iit, i1list);
        List j1list = new ArrayList();
        j1list.add("b");
        j1list.add("f");
        j1list.add("j");
        j1list.add("n");
        Iterator jit = grid.jStripeIterator(1);
        testIterator(jit, j1list);
    }

    public void testStripeIteratorByAddress() {
        MutableGrid2D setupGrid = new MutableArrayGrid2D(4, 4);
        setupGrid.put(0, 0, "a");
        setupGrid.put(0, 1, "b");
        setupGrid.put(0, 2, "c");
        setupGrid.put(0, 3, "d");
        setupGrid.put(1, 0, "e");
        setupGrid.put(1, 1, "f");
        setupGrid.put(1, 2, "g");
        setupGrid.put(1, 3, "h");
        setupGrid.put(2, 0, "i");
        setupGrid.put(2, 1, "j");
        setupGrid.put(2, 2, "k");
        setupGrid.put(2, 3, "l");
        setupGrid.put(3, 0, "m");
        setupGrid.put(3, 1, "n");
        setupGrid.put(3, 2, "o");
        setupGrid.put(3, 3, "p");
        Grid2D grid = createGrid(setupGrid);
        List i1list = new ArrayList();
        i1list.add("e");
        i1list.add("f");
        i1list.add("g");
        i1list.add("h");
        Iterator iit = grid.stripeIterator(new Address2D(1, 3), Grid2D.I);
        testIterator(iit, i1list);
        List j1list = new ArrayList();
        j1list.add("b");
        j1list.add("f");
        j1list.add("j");
        j1list.add("n");
        Iterator jit = grid.stripeIterator(new Address2D(3, 1), Grid2D.J);
        testIterator(jit, j1list);
    }

    public void testSize(Collection co, int expected) {
        assertTrue("co.size() is " + co.size() + " not " + expected, co.size() == expected);
    }

    public void testIsEmpty(Collection co, boolean expected) {
        assertTrue("co.isEmpty() is " + co.isEmpty() + " not " + expected, co.isEmpty() == expected);
    }

    public void testContains(Collection co, Object it, boolean expected) {
        assertTrue("co.contains(" + it + ") is " + co.contains(it) + " not " + expected, co.contains(it) == expected);
    }

    public void testContainsAll(Collection co, Set it, boolean expected) {
        assertTrue("co.containsAll(" + it + ") is " + co.containsAll(it) + " not " + expected, co.containsAll(it) == expected);
    }

    public void testCollectionQueries() {
        MutableGrid2D setupGrid = new MutableArrayGrid2D(3, 5, "D");
        Grid2D grid = createGrid(setupGrid);
        testSize(grid, 15);
        testIsEmpty(grid, false);
        setupGrid.put(2, 1, "A");
        grid = createGrid(setupGrid);
        testContains(grid, "A", true);
        testContains(grid, "B", false);
        Set contents = new HashSet();
        testContainsAll(grid, contents, true);
        contents.add("A");
        testContainsAll(grid, contents, true);
        contents.add("D");
        testContainsAll(grid, contents, true);
        contents.add("B");
        testContainsAll(grid, contents, false);
    }

    public void testIterator() {
        MutableGrid2D setupGrid = new MutableArrayGrid2D(4, 4);
        setupGrid.put(0, 0, "a");
        setupGrid.put(0, 1, "b");
        setupGrid.put(0, 2, "c");
        setupGrid.put(0, 3, "d");
        setupGrid.put(1, 0, "e");
        setupGrid.put(1, 1, "f");
        setupGrid.put(1, 2, "g");
        setupGrid.put(1, 3, "h");
        setupGrid.put(2, 0, "i");
        setupGrid.put(2, 1, "j");
        setupGrid.put(2, 2, "k");
        setupGrid.put(2, 3, "l");
        setupGrid.put(3, 0, "m");
        setupGrid.put(3, 1, "n");
        setupGrid.put(3, 2, "o");
        setupGrid.put(3, 3, "p");
        Grid2D grid = createGrid(setupGrid);
        List expected = new ArrayList(grid);
        testIterator(grid.iterator(), expected);
    }

    public void testAddressIterator() {
        MutableGrid2D setupGrid = new MutableArrayGrid2D(4, 4);
        setupGrid.put(0, 0, "a");
        setupGrid.put(0, 1, "b");
        setupGrid.put(0, 2, "c");
        setupGrid.put(0, 3, "d");
        setupGrid.put(1, 0, "e");
        setupGrid.put(1, 1, "f");
        setupGrid.put(1, 2, "g");
        setupGrid.put(1, 3, "h");
        setupGrid.put(2, 0, "i");
        setupGrid.put(2, 1, "j");
        setupGrid.put(2, 2, "k");
        setupGrid.put(2, 3, "l");
        setupGrid.put(3, 0, "m");
        setupGrid.put(3, 1, "n");
        setupGrid.put(3, 2, "o");
        setupGrid.put(3, 3, "p");
        Grid2D grid = createGrid(setupGrid);
        List expected = new ArrayList(16);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expected.add(new Address2D(i, j));
            }
        }
        testIterator(grid.addressIterator(), expected);
    }

    public void testAdd() {
        try {
            Grid2D setupGrid = new MutableArrayGrid2D(4, 4);
            Grid2D grid = createGrid(setupGrid);
            grid.add("A");
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testAddAll() {
        try {
            Grid2D setupGrid = new MutableArrayGrid2D(4, 4);
            Grid2D grid = createGrid(setupGrid);
            Set set = new HashSet();
            set.add("A");
            grid.addAll(set);
            fail("Should have thrown an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public abstract void testRemove();

    public abstract void testRemoveAll();

    public abstract void testRetainAll();

    public abstract void testClear();

    public abstract void testConstructors();
}
