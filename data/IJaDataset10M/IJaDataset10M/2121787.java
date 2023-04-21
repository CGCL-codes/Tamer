package org.dubh.problems;

import org.junit.Test;
import static org.junit.Assert.*;

public class RotatedListSearchTests {

    @Test
    public void shouldFindInRotatedList() {
        int[] a = { 3, 4, 5, 6, 1, 2 };
        int result = RotatedListSearch.searchRotatedList(a, 2);
        assertEquals(5, result);
    }

    @Test
    public void shouldFindInNonRotatedList() {
        int[] a = { 1, 3, 6, 7, 10 };
        int result = RotatedListSearch.searchRotatedList(a, 6);
        assertEquals(2, result);
    }

    @Test
    public void shouldFindInExactlyEvenlySplitRotatedList() {
        int[] a = { 4, 5, 6, 1, 2, 3 };
        int result = RotatedListSearch.searchRotatedList(a, 5);
        assertEquals(1, result);
    }
}
