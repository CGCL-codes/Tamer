package org.springframework.context.access;

import junit.framework.TestCase;
import org.springframework.beans.factory.access.BeanFactoryLocator;

/**
 * @author Colin Sampaleanu
 */
public class DefaultLocatorFactoryTests extends TestCase {

    public void testGetInstance() {
        BeanFactoryLocator bf = DefaultLocatorFactory.getInstance();
        BeanFactoryLocator bf2 = DefaultLocatorFactory.getInstance();
        assertTrue(bf.equals(bf2));
    }

    public void testGetInstanceString() {
        BeanFactoryLocator bf = DefaultLocatorFactory.getInstance("my-bean-refs.xml");
        BeanFactoryLocator bf2 = DefaultLocatorFactory.getInstance("my-bean-refs.xml");
        assertTrue(bf.equals(bf2));
    }
}
