package jacky.lanlan.song.collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Enumeration;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

public class IterableAdapterTest {

    private IterableAdapter<Integer> adapter;

    @Before
    public void init() {
        Enumeration<Integer> enu = mock(Enumeration.class);
        when(enu.hasMoreElements()).thenReturn(true, true, true, false);
        when(enu.nextElement()).thenReturn(1, 2, 3);
        adapter = new IterableAdapter<Integer>(enu);
    }

    @Test
    public void testIterator() throws Exception {
        int count = 0;
        for (int i : adapter) assertEquals(++count, i);
    }

    @Test
    public void testRemove() throws Exception {
        Iterator<Integer> iter = adapter.iterator();
        iter.next();
        iter.remove();
        assertTrue(iter.hasNext());
        iter.next();
        assertTrue(iter.hasNext());
        iter.next();
        assertFalse(iter.hasNext());
    }
}
