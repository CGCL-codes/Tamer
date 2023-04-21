package openrpg2.common.core.gui;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * GUI implementations must implement this interface in order to be used with OpenRPG2
 * @author Snowdog
 */
public interface ManageableGUI {

    public void showGUI();

    public void setGUIManagerCallback(GUIManagerCallback guiManagerCallbackRef);

    public void setMenuBar(JMenuBar menu);

    public void addModuleGUI(String modulename, JPanel modulepanel);

    public void removeModuleGUI(String modulename);
}
