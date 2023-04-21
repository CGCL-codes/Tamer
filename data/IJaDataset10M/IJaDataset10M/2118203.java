package org.rococoa.cocoa;

import org.junit.Test;
import org.rococoa.cocoa.foundation.NSMutableIndexSet;
import org.rococoa.cocoa.foundation.NSUInteger;
import org.rococoa.test.RococoaTestCase;
import static org.junit.Assert.*;

/**
 * @author David Kocher
 * @version $Id:$
 */
public class NSIndexSetTest extends RococoaTestCase {

    @Test
    public void testIndexWithRange() {
        NSMutableIndexSet index = NSMutableIndexSet.new_();
        index.addIndex(new NSUInteger(1));
        index.addIndex(new NSUInteger(2));
        assertEquals(new NSUInteger(2), index.count());
        assertFalse(index.containsIndex(new NSUInteger(0)));
        assertTrue(index.containsIndex(new NSUInteger(1)));
        assertTrue(index.containsIndex(new NSUInteger(2)));
        assertFalse(index.containsIndex(new NSUInteger(3)));
    }

    @Test
    public void testIndexWithDoubleRange() {
        NSMutableIndexSet index = NSMutableIndexSet.new_();
        index.addIndex(new NSUInteger(1));
        index.addIndex(new NSUInteger(2));
        index.addIndex(new NSUInteger(4));
        index.addIndex(new NSUInteger(5));
        assertEquals(new NSUInteger(4), index.count());
        assertFalse(index.containsIndex(new NSUInteger(0)));
        assertTrue(index.containsIndex(new NSUInteger(1)));
        assertTrue(index.containsIndex(new NSUInteger(2)));
        assertFalse(index.containsIndex(new NSUInteger(3)));
        assertTrue(index.containsIndex(new NSUInteger(4)));
        assertTrue(index.containsIndex(new NSUInteger(5)));
    }
}
