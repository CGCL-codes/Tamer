package net.liveseeds.listeners;

import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * <a href="mailto:misha@ispras.ru">Mikhail Ksenzov</a>
 */
public class StrongListenerSetTest extends TestCase {

    private ListenerSet listenerSet;

    private Object listener1;

    private Object listener2;

    public StrongListenerSetTest() {
    }

    public StrongListenerSetTest(final String string) {
        super(string);
    }

    protected void setUp() throws Exception {
        super.setUp();
        listenerSet = new StrongListenerSet();
        listener1 = new Object();
        listener2 = new Object();
    }

    public void testAddListener() throws Exception {
        listenerSet.addListener(listener1);
        listenerSet.addListener(listener2);
        final Iterator iterator = listenerSet.notifyListeners();
        final List list = new ArrayList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(2, list.size());
        assertTrue(list.contains(listener1));
        assertTrue(list.contains(listener2));
    }

    public void testRemoveListener() throws Exception {
        listenerSet.addListener(listener1);
        listenerSet.addListener(listener2);
        listenerSet.removeListener(listener2);
        final Iterator iterator = listenerSet.notifyListeners();
        final List list = new ArrayList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(1, list.size());
        assertTrue(list.contains(listener1));
    }

    public void testClear() throws Exception {
        listenerSet.addListener(listener1);
        listenerSet.addListener(listener2);
        listenerSet.clear();
        final Iterator iterator = listenerSet.notifyListeners();
        final List list = new ArrayList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(0, list.size());
    }

    public void testNotifyListener() throws Exception {
        listenerSet.addListener(listener1);
        listenerSet.notifyListeners();
        listenerSet.addListener(listener2);
        final Iterator iterator = listenerSet.notifyListeners();
        final List list = new ArrayList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(1, list.size());
        assertTrue(list.contains(listener1));
    }
}
