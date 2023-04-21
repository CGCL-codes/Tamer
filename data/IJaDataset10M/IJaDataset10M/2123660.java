package name.schaefer.heiko.twijjer;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import name.schaefer.heiko.twijjer.util.Logger;

/**
 * MIDlet main - starts up the Controller
 *
 * @author Heiko Schaefer
 */
public class Twijjer extends MIDlet {

    Controller controller = null;

    public void startApp() {
        Logger.log("startApp()");
        if (controller == null) {
            controller = Controller.createInstance(this);
        } else {
            controller.displayReset();
        }
    }

    public void pauseApp() {
        Logger.log("pauseApp()");
    }

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        if (!unconditional) {
            Logger.log("saying no to destroyApp()");
            throw new MIDletStateChangeException();
        }
    }
}
