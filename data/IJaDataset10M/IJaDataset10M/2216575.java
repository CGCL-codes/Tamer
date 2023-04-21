package com.pobox.tupletest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestTest extends junit.framework.TestCase {

    Test test;

    Tuple tuple;

    protected void setUp() {
        test = new Test() {

            {
                add(new Item(1, 'a'));
                add(new Item(2, 'd'));
                add(new Item(3, 'd'));
                add(new Item(4, 'c'));
                add(new Item(5, 'g'));
            }
        };
        tuple = new Tuple() {

            {
                add(new Item(1, 'a'));
                add(new Item(4, 'c'));
                add(new Item(5, 'g'));
            }
        };
    }

    public void testConstructor() {
        assertTrue((new Test()) instanceof ArrayList);
    }

    public void testConstructorWithArg() {
        assertNotSame(test, new Test(test));
        assertEquals(test, new Test(test));
    }

    public void testIsCovered() {
        assertTrue(test.covers(tuple));
    }

    public void testIsCoveredAllElements() {
        Tuple tuple = new Tuple();
        for (Item item : test) {
            tuple.add(item);
        }
        assertTrue(test.covers(tuple));
    }

    public void testIsCoveredFalse() {
        tuple.add(new Item(9, 'z'));
        assertFalse(test.covers(tuple));
    }

    public void testReport() {
        assertEquals(" 1a 2d 3d 4c 5g", test.report());
    }

    public void testCountTuples() {
        final Item item1 = new Item(1, 'a');
        final Item item2 = new Item(2, 'b');
        final Item item3 = new Item(3, 'c');
        test = new Test() {

            {
                add(item1);
                add(item2);
                add(item3);
            }
        };
        Table testTable = TableTest.setupSmallFeatureTable();
        List<Tuple> wantedTuples = testTable.getAllWantedTuples(2);
        Tuple changeTuple = new Tuple();
        changeTuple.add(item2);
        changeTuple.add(item3);
        wantedTuples.remove(changeTuple);
        List<Tuple> coveredTuples = testTable.getCoveredTuples();
        coveredTuples.add(changeTuple);
        assertEquals(3, test.countTuples(wantedTuples, coveredTuples));
    }

    public void testGrowPermissionArrayIfRequired() {
        assertEquals(0, test.dimensionChangePermitted.size());
        test.growPermissionArrayIfRequired();
        assertEquals(5, test.dimensionChangePermitted.size());
        for (Boolean permission : test.dimensionChangePermitted) {
            assertTrue(permission);
        }
    }

    public void testIsDimensionChangePermitted() {
        for (int i = 0; i < test.size(); i++) {
            assertTrue(test.isDimensionChangePermitted(i));
        }
        test.setDimensionChangePermission(3, false);
        assertFalse(test.isDimensionChangePermitted(3));
    }

    public void testpluginTupleToTestCase() {
        Test input = new Test() {

            {
                add(new Item(1, 'A'));
                add(new Item(2, 'A'));
                add(new Item(3, 'A'));
                add(new Item(4, 'A'));
                add(new Item(5, 'A'));
            }
        };
        Test expected = new Test() {

            {
                add(new Item(1, 'C'));
                add(new Item(2, 'C'));
                add(new Item(3, 'A'));
                add(new Item(4, 'C'));
                add(new Item(5, 'A'));
            }
        };
        Tuple tuple = new Tuple() {

            {
                add(new Item(1, 'C'));
                add(new Item(2, 'C'));
                add(new Item(4, 'C'));
            }
        };
        Test result = input.pluginTupleToTestCase(tuple);
        assertEquals(expected, result);
    }

    public void testGetListOfMutableDimensions() {
        test.setDimensionChangePermission(1, false);
        test.setDimensionChangePermission(3, false);
        List<Integer> list = test.getListOfMutableDimensions();
        assertEquals(3, list.size());
        assertTrue(list.contains(Integer.valueOf(0)));
        assertTrue(list.contains(Integer.valueOf(2)));
        assertTrue(list.contains(Integer.valueOf(4)));
    }

    public void testSwapToBestItemSeenBestEmpty() {
        try {
            test.swapToBestItemSeen(2, new Tuple());
            fail("expected an assertionError");
        } catch (java.lang.AssertionError ae) {
            assertEquals("best Tuple cannot be empty", ae.getMessage());
        }
    }

    public void testSwapToBestItemSeenBestNull() {
        try {
            test.swapToBestItemSeen(2, null);
            fail("expected an assertionError");
        } catch (java.lang.AssertionError ae) {
            assertEquals("best Tuple cannot be null", ae.getMessage());
        }
    }

    public void testSwapToBestItemSeenSingleItem() {
        Tuple best = new Tuple();
        Item item = new Item(3, 'c');
        best.add(item);
        test.swapToBestItemSeen(2, best);
        assertSame("item swap in this test failed", item, test.get(2));
    }

    public void testSwapToBestItemSeenMultiItem() {
        Tuple best = new Tuple();
        Item item1 = new Item(2, 'y');
        Item item2 = new Item(3, 'z');
        best.add(item1);
        best.add(item2);
        test.randomizer = new Random(1L);
        test.swapToBestItemSeen(2, best);
        assertSame("item swap in this test failed", item1, test.get(2));
    }
}
