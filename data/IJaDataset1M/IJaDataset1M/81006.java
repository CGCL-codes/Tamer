package com.volantis.mcs.papi;

import com.volantis.mcs.context.TestMarinerPageContext;

/**
 * Tests the Menu PAPI element.
 */
public class MenuTestCase extends MenuItemCollectorTestCaseAbstract {

    public void testAddMenuItem() {
    }

    /**
     * This test needs to override the MarinerPageContext.pushElement method to
     * do nothing. If put in the main TestMarinerPageContext class this might
     * break another test elsewhere
     */
    class MenuTestCaseMarinerPageContext extends TestMarinerPageContext {

        public void pushElement(PAPIElement element) {
        }
    }

    ;

    protected TestMarinerPageContext getPageContext() {
        return new MenuTestCaseMarinerPageContext();
    }

    protected PAPIElement createTestablePAPIElement() {
        return new MenuElement();
    }
}
