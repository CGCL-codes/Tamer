package org.simcom.pageengine;

import org.simcom.demo.HelloWorld;
import org.simcom.demo.navomatic.Page1;

public class TestSimplePageInjector extends SimComPageTestCase {

    public void testInjectLable() throws Exception {
        tester.startPage(HelloWorld.class);
        tester.assertRenderedPage(HelloWorld.class);
    }

    public void testNavomaticPages() throws Exception {
        tester.startPage(Page1.class);
        tester.assertRenderedPage(Page1.class);
    }
}
