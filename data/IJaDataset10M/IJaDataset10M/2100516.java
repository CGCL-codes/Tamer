package nokia;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import ui.MenuEngine;
import common.Util;

public class N_MainMidlet extends MIDlet {

    MenuEngine m_menuEngine;

    public N_MainMidlet() {
        Util.UI_DISPLAY = Display.getDisplay(this);
        Util.UI_MIDLET = this;
    }

    public void startApp() {
        Util.WEIQI_CANVAS = new N_WeiqiCanvas();
        Util.init();
        m_menuEngine = new MenuEngine();
        m_menuEngine.start(Util.UI_DISPLAY);
    }

    public void pauseApp() {
        m_menuEngine = null;
        Util.WEIQI_CANVAS = null;
        Util.release();
    }

    public void destroyApp(boolean unconditional) {
    }

    public void exit() {
        destroyApp(false);
        notifyDestroyed();
    }
}
