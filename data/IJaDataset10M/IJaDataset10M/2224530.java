package org.jcvi.common.core.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jcvi.common.core.util.iter.ArrayIterable;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayIterable {

    String[] stooges = new String[] { "Larry", "Curly", "Moe", "Shemp", "Joe Besser", "Curly-Joe" };

    @Test
    public void iterator() {
        ArrayIterable<String> sut = new ArrayIterable<String>(stooges);
        Iterator<String> iter = sut.iterator();
        int counter = 0;
        while (iter.hasNext()) {
            String actual = iter.next();
            assertEquals(stooges[counter++], actual);
        }
    }

    @Test
    public void throwsNoSuchElementExceptionWhenHasNextIsFalse() {
        ArrayIterable<String> sut = new ArrayIterable<String>(stooges);
        Iterator<String> iter = sut.iterator();
        while (iter.hasNext()) {
            iter.next();
        }
        try {
            iter.next();
            fail("should throw NoSuchElementException when has next =false");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void emptyArray() {
        Iterator iter = new ArrayIterable(new Object[] {}).iterator();
        assertFalse(iter.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void nullArrayShouldThrowNullPointerException() {
        new ArrayIterable(null);
    }
}
