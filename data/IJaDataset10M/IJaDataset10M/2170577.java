package net.sourceforge.processdash.ui.help;

import java.awt.Component;

public interface DashHelpProvider {

    public void enableHelpKey(Component comp, String id);

    public void enableHelp(Component comp, String helpID);

    public void enableHelpOnButton(Component comp, String helpID);

    public void displayHelpTopic(String helpID);

    public void displaySearchTab();

    public String getHelpIDString(Component comp);
}
