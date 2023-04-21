package ca.sandstorm.luminance.test.gamelogic;

import ca.sandstorm.luminance.gamelogic.MenuState;
import android.test.AndroidTestCase;

/**
 * Testing of the MenuState Class
 * Can't be testing because it uses Engine
 * And its also a State.
 * @author lianghuang
 *
 */
public class MenuStateTest extends AndroidTestCase {

    MenuState _ms;

    protected void setUp() throws Exception {
        _ms = new MenuState();
        super.setUp();
    }

    public void testMenuState() throws Exception {
        assertNotNull(_ms);
    }

    public void testGetGUIManager() throws Exception {
    }

    public void testAddWidgets() throws Exception {
    }

    public void testDeviceChanged() throws Exception {
    }

    public void testResume() throws Exception {
    }

    public void testInit() throws Exception {
    }

    public void testIsActive() throws Exception {
    }

    public void testIsVisible() throws Exception {
    }

    public void testMessageReceived() throws Exception {
    }

    public void testUpdate() throws Exception {
    }

    public void testDraw() throws Exception {
    }
}
