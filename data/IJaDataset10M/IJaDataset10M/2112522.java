package pspdash;

import java.awt.Component;
import java.net.URLEncoder;

class SimpleHelpProvider implements DashHelpProvider {

    public void enableHelpKey(Component comp, String id) {
    }

    public void enableHelp(Component comp, String helpID) {
    }

    public void enableHelpOnButton(Component comp, String helpID) {
    }

    public void displayHelpTopic(String helpID) {
        Browser.launch("help/frame.html?" + URLEncoder.encode(helpID));
    }

    public void displaySearchTab() {
        Browser.launch("help/frame.html");
    }

    public String getHelpIDString(Component comp) {
        return null;
    }
}
