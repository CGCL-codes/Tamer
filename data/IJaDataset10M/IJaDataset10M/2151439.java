package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;

public class TestNoRequestPathInfo extends CactusStrutsTestCase {

    public void testNoPathSet() {
        try {
            actionPerform();
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException ise) {
            return;
        }
    }
}
