package mirrormonkey.util.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * Contains test cases for the package <tt>mirrormonkey.util.listeners</tt>,
 * which in turn contains an abstraction layer for the listener design pattern.
 * 
 * @author Philipp Christian Loewner
 * 
 */
public class ListenerConfigurationTest {

    /**
	 * Checks whether listeners added to a <tt>ListenerConfiguration</tt> are
	 * available by the interface class that they should be mapped to.
	 */
    @Test
    public void testAddListener() {
        ListenerConfiguration lc = new ListenerConfiguration();
        ManagedListener l = new ImplementingListeners();
        lc.addListener(l);
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        lc.getListeners(MappedListenerClass.class, c);
        assertEquals(1, c.size());
        assertSame(c.get(0), l);
    }

    /**
	 * Checks whether inheriting from listeners does not mess up the mapping
	 */
    @Test
    public void testInheritedListener() {
        ListenerConfiguration lc = new ListenerConfiguration();
        ManagedListener l = new InheritingListeners();
        lc.addListener(l);
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        lc.getListeners(MappedListenerClass.class, c);
        assertEquals(1, c.size());
        assertSame(c.get(0), l);
    }

    /**
	 * Checks whether removing a single listener will actually remove the
	 * mapping
	 */
    @Test
    public void testRemoveListener() {
        ListenerConfiguration lc = new ListenerConfiguration();
        ManagedListener l = new ImplementingListeners();
        lc.addListener(l);
        lc.removeListener(l);
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        lc.getListeners(MappedListenerClass.class, c);
        assertTrue(c.isEmpty());
    }

    /**
	 * Checks whether listeners are not mapped to interface classes that aren't
	 * annotated with <tt>MapListener</tt>
	 */
    @Test
    public void testNoMapping() {
        ListenerConfiguration lc = new ListenerConfiguration();
        ManagedListener l = new ImplementingListeners();
        lc.addListener(l);
        lc.removeListener(l);
        List<UnmappedListenerClass> c = new LinkedList<UnmappedListenerClass>();
        lc.getListeners(UnmappedListenerClass.class, c);
        assertTrue(c.isEmpty());
    }

    /**
	 * Checks whether adding and removing works with multiple listeners
	 */
    @Test
    public void testMultiListenersSameClass() {
        ListenerConfiguration lc = new ListenerConfiguration();
        List<MappedListenerClass> ls = new LinkedList<MappedListenerClass>();
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        for (int i = 0; i < 5; i++) {
            MappedListenerClass mlc = new ImplementingListeners();
            ls.add(mlc);
            lc.addListener(mlc);
            lc.getListeners(MappedListenerClass.class, c);
            assertEquals(i + 1, c.size());
            assertTrue(c.containsAll(ls));
            c.clear();
        }
        for (int i = 4; i >= 0; i--) {
            lc.removeListener(ls.get(0));
            ls.remove(0);
            lc.getListeners(MappedListenerClass.class, c);
            assertEquals(i, c.size());
            assertTrue(c.containsAll(ls));
            c.clear();
        }
    }

    /**
	 * Checks whether adding listeners with multiple different mapped listener
	 * classes works correctly
	 */
    @Test
    public void testMultiListenersMultiClass() {
        ListenerConfiguration lc = new ListenerConfiguration();
        MappedListenerClass mappedListener = new ImplementingListeners();
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        lc.addListener(mappedListener);
        lc.getListeners(MappedListenerClass.class, c);
        assertEquals(1, c.size());
        assertTrue(c.contains(mappedListener));
        c.clear();
        AnotherMappedListenerClass anotherMappedListener = new ImplementingAnotherMappedListenerClass();
        List<AnotherMappedListenerClass> c2 = new LinkedList<AnotherMappedListenerClass>();
        lc.addListener(anotherMappedListener);
        lc.getListeners(AnotherMappedListenerClass.class, c2);
        assertEquals(1, c2.size());
        assertTrue(c2.contains(anotherMappedListener));
        c2.clear();
        lc.getListeners(MappedListenerClass.class, c);
        assertEquals(1, c.size());
        assertTrue(c.contains(mappedListener));
        c.clear();
        lc.removeListener(anotherMappedListener);
        lc.getListeners(AnotherMappedListenerClass.class, c2);
        assertEquals(0, c2.size());
        c2.clear();
        lc.getListeners(MappedListenerClass.class, c);
        assertEquals(1, c.size());
        assertTrue(c.contains(mappedListener));
        c.clear();
    }

    /**
	 * Checks whether a listener that implements multiple mapped interfaces will
	 * be available by all those interfaces
	 */
    @Test
    public void checkAddImplementMultipleInterfaces() {
        ListenerConfiguration lc = new ListenerConfiguration();
        ImplementMultiple im = new ImplementMultiple();
        lc.addListener(im);
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        List<AnotherMappedListenerClass> ac = new LinkedList<AnotherMappedListenerClass>();
        lc.getListeners(MappedListenerClass.class, c);
        lc.getListeners(AnotherMappedListenerClass.class, ac);
        assertEquals(1, c.size());
        assertTrue(c.contains(im));
        assertEquals(1, ac.size());
        assertTrue(ac.contains(im));
    }

    /**
	 * Checks whether a listener that implements multiple mapped interfaces will
	 * be removed for all those interfaces when it is removed from the
	 * <tt>ListenerConfiguration</tt>
	 */
    @Test
    public void testRemoveImplementMultipleInterfaces() {
        ListenerConfiguration lc = new ListenerConfiguration();
        ImplementMultiple im = new ImplementMultiple();
        lc.addListener(im);
        lc.removeListener(im);
        List<MappedListenerClass> c = new LinkedList<MappedListenerClass>();
        List<AnotherMappedListenerClass> ac = new LinkedList<AnotherMappedListenerClass>();
        lc.getListeners(MappedListenerClass.class, c);
        lc.getListeners(AnotherMappedListenerClass.class, ac);
        assertTrue(c.isEmpty());
        assertTrue(ac.isEmpty());
    }
}
