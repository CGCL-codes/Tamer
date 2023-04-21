package net.sf.joafip.store.service.objectfortest;

import java.util.Iterator;
import net.sf.joafip.AbstractJoafipTestCase;
import net.sf.joafip.TestException;

public class TestLucList extends AbstractJoafipTestCase {

    private LucList list;

    public TestLucList() throws TestException {
        super();
    }

    public TestLucList(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        list = new LucList();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        list = null;
        super.tearDown();
    }

    public void testEmpty() {
        final Iterator<Object> iterator = list.iterator();
        assertFalse("must not have next", iterator.hasNext());
    }

    public void testAdd() {
        final String[] strings = new String[] { "elt1", "elt2", "elt3" };
        list.add(strings[0]);
        assertContains(strings, 1);
        list.add(strings[1]);
        assertContains(strings, 2);
        list.add(strings[2]);
        assertContains(strings, 3);
    }

    public void testRemove() {
        final String[] strings1 = new String[] { "elt1", "elt2", "elt3" };
        final String[] strings2 = new String[] { "elt1", "elt3" };
        list.add(strings1[0]);
        list.add(strings1[1]);
        list.add(strings1[2]);
        list.remove(strings1[1]);
        assertContains(strings2, 2);
    }

    private void assertContains(final String[] strings, final int size) {
        final Iterator<Object> iterator = list.iterator();
        for (int index = 0; index < size; index++) {
            assertTrue("must have next", iterator.hasNext());
            assertEquals("bad value", strings[index], iterator.next());
        }
    }
}
