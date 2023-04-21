package org.t2framework.commons.bootstrap;

import java.util.Iterator;
import org.t2framework.commons.bootstrap.CommonsInitializer;
import junit.framework.TestCase;

public class CommonsInitializerTest extends TestCase {

    public void testGetService_exist() throws Exception {
        Hoge hoge = CommonsInitializer.get(Hoge.class);
        assertNotNull(hoge);
        assertEquals("hello", hoge.hello());
    }

    public void testGetService_notExist() throws Exception {
        FooFoo f = CommonsInitializer.get(FooFoo.class);
        assertNull(f);
    }

    public void testGetService_multiple() throws Exception {
        Bar bar = CommonsInitializer.get(Bar.class);
        assertNotNull(bar);
        Iterator<Bar> iterator = CommonsInitializer.iterator(CommonsInitializer.load(Bar.class));
        assertNotNull(iterator);
        assertNotNull(iterator.next());
        assertNotNull(iterator.next());
    }

    public void testGetService_multiple2() throws Exception {
        Iterator<Bar> iterator = CommonsInitializer.iterator(Bar.class);
        assertNotNull(iterator);
        assertNotNull(iterator.next());
        assertNotNull(iterator.next());
    }
}
