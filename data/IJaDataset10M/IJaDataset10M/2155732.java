package com.the_eventhorizon.todo.utils;

import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.ArrayList;

/**
 * @author pkrupets
 */
public class ArrayUtilsTest {

    @Test
    public void testShiftItems_CannotBeShifted() {
        List<String> items = new ArrayList<String>();
        items.add("Item 1");
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] {}));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] {}));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] {}));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] {}));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] {}));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] {}));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] {}));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] {}));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 0 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 0 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 0 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 0 }));
        items.clear();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        items.add("Item 5");
        items.add("Item 6");
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 0 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 0 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 5 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 5 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 5 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 5 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 0, 1 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 1, 0 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 4, 5 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 5, 4 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 0, 1 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 1, 0 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 4, 5 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 5, 4 }));
        Assert.assertFalse(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 0, 1, 2, 3, 4, 5 }));
        Assert.assertNull(ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 0, 1, 2, 3, 4, 5 }));
    }

    @Test
    public void testShiftItems_ContinuousSet() {
        List<String> items = new ArrayList<String>();
        resetArray(items);
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 3 }));
        checkResult(items, ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 3 }), new int[] { 0 }, 0, 3, new String[] { "Item 4", "Item 1", "Item 2", "Item 3", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 3 }), new int[] { 2 }, 2, 3, new String[] { "Item 1", "Item 2", "Item 4", "Item 3", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 3 }), new int[] { 4 }, 3, 4, new String[] { "Item 1", "Item 2", "Item 3", "Item 5", "Item 4", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 3 }), new int[] { 5 }, 3, 5, new String[] { "Item 1", "Item 2", "Item 3", "Item 5", "Item 6", "Item 4" });
        resetArray(items);
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 2, 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 2, 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 2, 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 2, 3 }));
        checkResult(items, ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 2, 3 }), new int[] { 0, 1 }, 0, 3, new String[] { "Item 3", "Item 4", "Item 1", "Item 2", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 2, 3 }), new int[] { 1, 2 }, 1, 3, new String[] { "Item 1", "Item 3", "Item 4", "Item 2", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 2, 3 }), new int[] { 3, 4 }, 2, 4, new String[] { "Item 1", "Item 2", "Item 5", "Item 3", "Item 4", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 2, 3 }), new int[] { 4, 5 }, 2, 5, new String[] { "Item 1", "Item 2", "Item 5", "Item 6", "Item 3", "Item 4" });
    }

    @Test
    public void testShiftItems_NonContinuousSet() {
        List<String> items = new ArrayList<String>();
        resetArray(items);
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 1, 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 3, 1 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 1, 3 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 3, 1 }));
        checkResult(items, ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 1, 3 }), new int[] { 0, 1 }, 0, 3, new String[] { "Item 2", "Item 4", "Item 1", "Item 3", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 3, 1 }), new int[] { 0, 2 }, 0, 3, new String[] { "Item 2", "Item 1", "Item 4", "Item 3", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 1, 3 }), new int[] { 2, 4 }, 1, 4, new String[] { "Item 1", "Item 3", "Item 2", "Item 5", "Item 4", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 3, 1 }), new int[] { 4, 5 }, 1, 5, new String[] { "Item 1", "Item 3", "Item 5", "Item 6", "Item 2", "Item 4" });
        resetArray(items);
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UPPER_MOST, items.size(), new int[] { 0, 5 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.UP, items.size(), new int[] { 5, 0 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN, items.size(), new int[] { 0, 5 }));
        Assert.assertTrue(ArrayUtils.canShift(eShiftDirection.DOWN_MOST, items.size(), new int[] { 5, 0 }));
        checkResult(items, ArrayUtils.shift(eShiftDirection.UPPER_MOST, items, new Integer[] { 0, 5 }), new int[] { 0, 1 }, 0, 5, new String[] { "Item 1", "Item 6", "Item 2", "Item 3", "Item 4", "Item 5" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.UP, items, new Integer[] { 5, 0 }), new int[] { 0, 4 }, 0, 5, new String[] { "Item 1", "Item 2", "Item 3", "Item 4", "Item 6", "Item 5" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN, items, new Integer[] { 0, 5 }), new int[] { 1, 5 }, 0, 5, new String[] { "Item 2", "Item 1", "Item 3", "Item 4", "Item 5", "Item 6" });
        resetArray(items);
        checkResult(items, ArrayUtils.shift(eShiftDirection.DOWN_MOST, items, new Integer[] { 5, 0 }), new int[] { 4, 5 }, 0, 5, new String[] { "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 6" });
    }

    private void resetArray(List<String> items) {
        items.clear();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        items.add("Item 5");
        items.add("Item 6");
    }

    private void checkResult(List<String> items, ShiftResult result, int[] newIndices, int changedFrom, int changedTo, String[] newItems) {
        Assert.assertEquals(changedFrom, result.firstAffectedIndex);
        Assert.assertEquals(changedTo, result.lastAffectedIndex);
        Assert.assertArrayEquals(newIndices, result.newSelectionIndices);
        Assert.assertArrayEquals(newItems, items.toArray(new String[items.size()]));
    }
}
