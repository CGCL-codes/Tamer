package java.awt;

import java.awt.Menu.AccessibleAWTMenu;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import junit.framework.TestCase;

public class AccessibleAWTMenuTest extends TestCase {

    Menu menu;

    AccessibleContext ac;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        menu = new Menu();
        ac = menu.getAccessibleContext();
    }

    public void testGetAccessibleRole() {
        assertSame(AccessibleRole.MENU, ac.getAccessibleRole());
    }

    public void testAccessibleAWTMenu() {
        assertTrue(ac instanceof AccessibleAWTMenu);
    }
}
