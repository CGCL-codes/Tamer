package com.pobox.tupletest;

import junit.framework.TestCase;
import junitx.extensions.ComparabilityTestCase;
import junitx.extensions.EqualsHashCodeTestCase;

public class ItemTest extends TestCase {

    Item item;

    protected void setUp() throws Exception {
        super.setUp();
        item = new Item();
    }

    public void testGetDimension() {
        item.setDimension(12);
        assertEquals(12, item.getDimension());
    }

    public void testGetFeature() {
        item.setFeature('a');
        assertEquals('a', item.getFeature());
    }

    public void testToString() {
        item.setDimension(8);
        item.setFeature('b');
        assertEquals("Item[dimension=8,feature=b]", item.toString());
    }

    public void testConstructor() {
        item = new Item(9, 'a');
        assertEquals(9, item.getDimension());
        assertEquals('a', item.getFeature());
    }

    public void testNoArgConstructor() {
        assertEquals(0, item.getDimension());
        assertEquals('?', item.getFeature());
    }

    public void testCompareToDimensionDifference() {
        Item itemBefore = new Item(9, 'a');
        Item itemAfter = new Item(9, 'b');
        assertEquals(-1, itemBefore.compareTo(itemAfter));
        assertEquals(1, itemAfter.compareTo(itemBefore));
    }

    public void testEqualsWithBadObjectType() {
        assertFalse(item.equals(new Object()));
    }

    public static class EqualsHashCodeTest extends EqualsHashCodeTestCase {

        public EqualsHashCodeTest(String name) {
            super(name);
        }

        protected Object createInstance() {
            return new Item(5, 'd');
        }

        protected Object createNotEqualInstance() {
            return new Item(2, 'q');
        }
    }

    public static class ComparabilityTest extends ComparabilityTestCase {

        public ComparabilityTest(String name) {
            super(name);
        }

        @Override
        protected Comparable createEqualInstance() throws Exception {
            return new Item(5, 'd');
        }

        @Override
        protected Comparable createGreaterInstance() throws Exception {
            return new Item(6, 'e');
        }

        @Override
        protected Comparable createLessInstance() throws Exception {
            return new Item(1, 'a');
        }
    }
}
