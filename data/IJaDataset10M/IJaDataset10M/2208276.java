package imei;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class IMEIMIDlet extends MIDlet {

    protected void startApp() {
        Alert alt = new Alert("IMEI", IMEI.get(), null, AlertType.INFO);
        alt.setCommandListener(new CommandListener() {

            public void commandAction(Command command, Displayable displayable) {
                destroyApp(false);
                notifyDestroyed();
            }
        });
        Display.getDisplay(this).setCurrent(alt);
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean unc) {
    }
}
