package net.sourceforge.xmlfacade.util;

import net.sourceforge.xmlfacade.util.IteratorState;
import net.sourceforge.xmlfacade.util.AbstractIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIteratorTest {

    @Test(expected = NoSuchElementException.class)
    public void testEmpty() {
        Iterator<String> iterator = new AbstractIterator<String>() {

            @Override
            protected IteratorState<String> prepareElement() {
                return IteratorState.end();
            }
        };
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void test() {
        Iterator<String> iterator = new AbstractIterator<String>() {

            private boolean done = false;

            @Override
            protected IteratorState<String> prepareElement() {
                if (!done) {
                    done = true;
                    return IteratorState.preparedNext("aaa");
                } else {
                    return IteratorState.end();
                }
            }
        };
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals("aaa", iterator.next());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testWrongImpl() {
        Iterable<String> iterable = new Iterable<String>() {

            public Iterator<String> iterator() {
                return new AbstractIterator<String>() {

                    @Override
                    protected IteratorState<String> prepareElement() {
                        return IteratorState.notPrepared();
                    }
                };
            }
        };
        try {
            iterable.iterator().hasNext();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            iterable.iterator().next();
            fail();
        } catch (IllegalStateException e) {
        }
    }
}
