package org.jhotdraw.test.contrib;

import org.jhotdraw.application.DrawApplication;
import org.jhotdraw.contrib.CTXWindowMenu;
import org.jhotdraw.contrib.MDIDesktopPane;
import org.jhotdraw.test.JHDTestCase;

public class CTXWindowMenuTest extends JHDTestCase {

    private CTXWindowMenu ctxwindowmenu;

    /**
	 * Constructor CTXWindowMenuTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public CTXWindowMenuTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public CTXWindowMenu createInstance() throws Exception {
        MDIDesktopPane desktop = (MDIDesktopPane) (((DrawApplication) getDrawingEditor()).getDesktop());
        return new CTXWindowMenu("TestCTXWindowMenu", desktop, getDrawingEditor());
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        ctxwindowmenu = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        ctxwindowmenu = null;
        super.tearDown();
    }

    public void testVault() throws Exception {
    }
}
