package org.jbeanmapper.configurator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.commons.digester.Digester;
import java.util.List;

/**
 * @author Brian Pugh
 */
public class TestBeanMappingsRule extends TestCase {

    public void testBegin() throws Exception {
        Digester digester = new Digester();
        BeanMappingsRule beanMappingsRule = new BeanMappingsRule();
        beanMappingsRule.setDigester(digester);
        beanMappingsRule.begin("", "", null);
        Object obj = digester.pop();
        assertTrue(obj instanceof List);
    }

    public static Test suite() {
        return new TestSuite(TestBeanMappingsRule.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
