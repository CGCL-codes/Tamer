package org.t2framework.commons.meta.impl;

import junit.framework.TestCase;
import org.t2framework.commons.Constants;
import org.t2framework.commons.exception.ConstructorNotFoundRuntimeException;
import org.t2framework.commons.meta.BeanDescFactory;

public class InstanceFactoryImplTest extends TestCase {

    public void testNewInstance1_simple() throws Exception {
        try {
            InstanceFactoryImpl<Hoge> factory = new InstanceFactoryImpl<Hoge>();
            Hoge o = factory.newInstance(BeanDescFactory.getBeanDesc(Hoge.class), Constants.EMPTY_ARRAY);
            assertNotNull(o);
        } finally {
            BeanDescFactory.clear();
        }
    }

    public void testNewInstance2_foundAppropriateOne() throws Exception {
        try {
            InstanceFactoryImpl<Hoge> factory = new InstanceFactoryImpl<Hoge>();
            Hoge o = factory.newInstance(BeanDescFactory.getBeanDesc(Hoge.class), new Object[] { "aaaa", 123 });
            assertNotNull(o);
            assertEquals("aaaa", o.s);
            assertEquals(123, o.i);
        } finally {
            BeanDescFactory.clear();
        }
    }

    public void testNewInstance1_notFound() throws Exception {
        try {
            InstanceFactoryImpl<Hoge> factory = new InstanceFactoryImpl<Hoge>();
            try {
                @SuppressWarnings("unused") Hoge o = factory.newInstance(BeanDescFactory.getBeanDesc(Hoge.class), new Object[] { "a" });
                fail();
            } catch (ConstructorNotFoundRuntimeException expected) {
                assertTrue(true);
            }
        } finally {
            BeanDescFactory.clear();
        }
    }

    public static class Hoge {

        public String s;

        public int i;

        public Hoge() {
        }

        public Hoge(String s, int i) {
            this.s = s;
            this.i = i;
        }
    }
}
