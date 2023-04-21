package org.jrazdacha.model;

import static org.junit.Assert.*;
import org.jrazdacha.model.Image.ImageComparator;
import org.junit.Test;

/**
 * Test case for image comparator object
 */
public class ImageComparatorTest {

    @Test
    public void test_Image_Compare() {
        ImageComparator comparator = new ImageComparator();
        Image a = new Image();
        a.setOrderNumber(0);
        Image b = new Image();
        b.setOrderNumber(1);
        assertEquals(-1, comparator.compare(a, b));
        assertEquals(1, comparator.compare(b, a));
    }

    @Test
    public void test_Image_Compare_Equals() {
        ImageComparator comparator = new ImageComparator();
        Image a = new Image();
        a.setOrderNumber(0);
        Image b = new Image();
        b.setOrderNumber(0);
        assertEquals(0, comparator.compare(a, b));
    }
}
