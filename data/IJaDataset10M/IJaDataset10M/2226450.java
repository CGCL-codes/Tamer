package org.jbeanmapper.creator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author Brian Pugh
 */
public class TestBasicBeanCreator extends TestCase {

    public void testCreateBean() throws Exception {
        BeanCreator creator = new DefaultBeanCreator();
        creator.createBean(TestBean.class, null);
    }

    public void testCreateBeanNull() throws Exception {
        try {
            BeanCreator creator = new DefaultBeanCreator();
            creator.createBean(null, null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public static class TestBean {
    }

    public static Test suite() {
        return new TestSuite(TestBasicBeanCreator.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
