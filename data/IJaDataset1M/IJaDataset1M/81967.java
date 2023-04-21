package org.happy.commons.patterns.decorator;

import static org.junit.Assert.*;
import org.happy.commons.patterns.decorator.beans.MyBean;
import org.happy.commons.patterns.decorator.beans.MyBeanDecorator;
import org.happy.commons.patterns.decorator.beans.MyBeanImpl;
import org.junit.Test;

/**
 * tests the decorator
 * @author Andreas Hollmann
 *
 */
public class Decorator_1x0ImplTest {

    public Decorator_1x0<Integer> createDecorator(Integer decorated) {
        return new Decorator_1x0Impl<Integer>(decorated);
    }

    @Test
    public void testDecorator_1x0Impl() {
        Decorator_1x0<Integer> d = createDecorator(7);
        assertEquals(new Integer(7), d.getDecorated());
    }

    @Test
    public void testGetSetDecorated() {
        Decorator_1x0<Integer> d = createDecorator(7);
        d.setDecorated(8);
        assertEquals(new Integer(8), d.getDecorated());
    }

    @Test
    public void testGetVersion() {
        Decorator_1x0<Integer> d = createDecorator(7);
        assertEquals(new Float(1), d.getVersion());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getLastDecoratedObject() {
        MyBean bean = new MyBeanImpl(new Integer(7));
        MyBean beanCopy = bean;
        int numberOfInterators = 100;
        for (int i = 0; i < numberOfInterators; i++) {
            beanCopy = new MyBeanDecorator(beanCopy);
        }
        MyBean fetchedBean = Decorator_1x0Impl.getLastDecoratedObject((Decorator_1x0<MyBean>) beanCopy);
        assertNotNull(fetchedBean);
        assertEquals(bean, fetchedBean);
        assertTrue(bean == fetchedBean);
    }
}
