package org.efficientia.cimap.lcdui;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * The CIMAP class provides the application's MIDlet. 
 * 
 * @author Ram�n Jim�nez (rjimenezz AT users DOT sourceforge DOT net)
 *
 */
public class CIMAP extends MIDlet {

    protected AppMediator mediator;

    public CIMAP() {
        mediator = new AppMediator(Display.getDisplay(this));
    }

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        mediator.disconnect();
    }

    public void pauseApp() {
    }

    public void startApp() throws MIDletStateChangeException {
        mediator.showConnectForm();
    }
}
