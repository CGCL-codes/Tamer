package org.jcvi.util;

import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestEmptyIterator {

    EmptyIterator<String> sut = EmptyIterator.createEmptyIterator();

    @Test
    public void removeDoesNothing() {
        sut.remove();
    }

    @Test
    public void hasNextReturnsFalse() {
        assertFalse(sut.hasNext());
    }

    @Test
    public void nextThrowsNoSuchElementException() {
        try {
            sut.next();
            fail("should throw no such element exception");
        } catch (NoSuchElementException e) {
            assertEquals("no elements in empty iterator", e.getMessage());
        }
    }
}
