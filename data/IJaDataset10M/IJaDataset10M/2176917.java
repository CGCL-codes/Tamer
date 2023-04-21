package ibcontroller;

import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JDialog;

class NotCurrentlyAvailableDialogHandler implements WindowHandler {

    public void handleWindow(Window window, int eventID) {
        if (!Utils.clickButton(window, "OK")) {
            System.err.println("IBController: The system is not currently available.");
            return;
        }
        if (TwsListener.getLoginFrame() != null) {
            JButton button2 = Utils.findButton(TwsListener.getLoginFrame(), "Login");
            button2.requestFocus();
            KeyEvent ke = new KeyEvent(button2, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_F4, KeyEvent.CHAR_UNDEFINED);
            button2.dispatchEvent(ke);
        }
    }

    public boolean recogniseWindow(Window window) {
        if (!(window instanceof JDialog)) return false;
        return (Utils.titleContains(window, "Login") && Utils.findLabel(window, "not currently available") != null);
    }
}
