package com.totsp.mavenplugin.gwt.support;

import junit.framework.TestCase;

/**
 *
 * @author cooper
 */
public class MakeCatalinaBaseTest extends TestCase {

    public MakeCatalinaBaseTest(String testName) {
        super(testName);
    }

    public void testMain() throws Exception {
        System.out.println("main");
        String source = GwtWebInfProcessorTest.getTestFile("src/test/testWeb2.xml");
        String tomcat = GwtWebInfProcessorTest.getTestFile("target/tomcat");
        String[] args = { tomcat, source };
        MakeCatalinaBase.main(args);
    }
}
