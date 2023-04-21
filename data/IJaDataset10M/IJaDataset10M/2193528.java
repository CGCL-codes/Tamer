package org.jaqlib.db;

import junit.framework.TestCase;
import org.jaqlib.AccountImpl;

public class DefaultBeanFactoryTest extends TestCase {

    private BeanFactory beanFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        beanFactory = new DefaultBeanFactory();
    }

    public void testNewInstance() {
        assertEquals(AccountImpl.class, beanFactory.newInstance(AccountImpl.class).getClass());
    }

    public void testNewInstance_NoDefaultConstructor() {
        try {
            beanFactory.newInstance(NoDefaultConstructorClass.class);
            fail("Did not throw RuntimeException");
        } catch (RuntimeException e) {
            assertEquals(InstantiationException.class, e.getCause().getClass());
        }
    }

    private static class NoDefaultConstructorClass {

        public NoDefaultConstructorClass(String arg) {
        }
    }
}
