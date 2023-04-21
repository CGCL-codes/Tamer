package net.sf.opengroove.mobile;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OpenGrooveMobile extends MIDlet {

    protected void destroyApp(boolean unconditional) {
    }

    protected void pauseApp() {
        destroyApp(true);
    }

    protected void startApp() throws MIDletStateChangeException {
        Form form = new Form("OpenGroove Mobile");
        form.append(new StringItem(" ", " "));
        form.append(new StringItem("Welcome to OpenGroove Mobile. ", "Hold on a sec..."));
        Display.getDisplay(this).setCurrent(form);
    }
}
