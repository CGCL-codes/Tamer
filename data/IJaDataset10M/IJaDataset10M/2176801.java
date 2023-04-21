package javax.swing.plaf.basic;

import javax.swing.plaf.ComponentUI;

public class BasicCheckBoxMenuItemUITest extends BasicMenuItemUITest {

    protected BasicCheckBoxMenuItemUI checkBoxUI;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        checkBoxUI = new BasicCheckBoxMenuItemUI();
        menuItemUI = checkBoxUI;
        prefix = "CheckBoxMenuItem.";
    }

    @Override
    protected void tearDown() throws Exception {
        checkBoxUI = null;
        menuItemUI = null;
        super.tearDown();
    }

    @Override
    public void testCreateUI() {
        ComponentUI ui1 = BasicCheckBoxMenuItemUI.createUI(null);
        ComponentUI ui2 = BasicCheckBoxMenuItemUI.createUI(null);
        assertTrue(ui1 instanceof BasicCheckBoxMenuItemUI);
        assertNotSame(ui1, ui2);
    }

    @Override
    public void testGetPropertyPrefix() {
        assertEquals("CheckBoxMenuItem", menuItemUI.getPropertyPrefix());
    }

    public void testProcessMouseEvent() {
    }

    public void testGetSizes() {
        try {
            checkBoxUI.getMinimumSize(null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
        }
        try {
            checkBoxUI.getMaximumSize(null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
        }
    }
}
