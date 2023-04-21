package acide.gui.consolePanel.popup.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acide.gui.mainWindow.AcideMainWindow;

/**
 * <p>
 * ACIDE - A Configurable IDE console panel popup menu console close console
 * menu item action listener.
 * </p>
 * <p>
 * Closes the current ACIDE - A Configurable IDE console panel.
 * </p>
 * 
 * @version 0.8
 * @see ActionListener
 */
public class AcideCloseConsoleMenuItemAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        AcideMainWindow.getInstance().getMenu().getConfigurationMenu().getConsoleMenu().getCloseConsoleMenuItem().doClick();
    }
}
