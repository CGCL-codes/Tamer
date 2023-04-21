package org.jaqlib.core.bean;

import junit.framework.TestCase;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.DefaultBeanFactory;

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

        @SuppressWarnings("unused")
        public NoDefaultConstructorClass(String arg) {
        }
    }
}
