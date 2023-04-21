package se.agileworkshop.client.test;

import se.agileworkshop.client.GuiActions;
import se.agileworkshop.client.app.MyGuiActions;
import junit.framework.TestCase;

public class MyGuiActionsTest extends TestCase {

    public void testPnrLength() {
        GuiActions gui = new MyGuiActions();
        assertFalse("NOK pnr", gui.prnEntered("XXXXXXXX"));
    }

    public void testPnr() {
        GuiActions gui = new MyGuiActions();
        assertFalse("NOK pnrA", gui.prnEntered("7803285554"));
        assertFalse("NOK pnrB", gui.prnEntered("7803285555"));
    }
}
