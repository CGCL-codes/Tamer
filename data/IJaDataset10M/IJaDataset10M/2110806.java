package acide.gui.consolePanel.popup.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acide.gui.mainWindow.AcideMainWindow;

/**
 * ACIDE - A Configurable IDE console panel search menu item action.
 * 
 * @version 0.8
 * @see ActionListener
 */
public class AcideConsolePanelSearchMenuItemAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        AcideMainWindow.getInstance().getMenu().getConfigurationMenu().getConsoleMenu().getSearchConsoleMenuItem().doClick();
    }
}
