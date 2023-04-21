package acide.gui.explorerPanel.popup.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acide.gui.mainWindow.AcideMainWindow;

/**
 * ACIDE - A Configurable IDE explorer panel popup menu remove file menu
 * item action listener.
 * 
 * @version 0.8
 * @see ActionListener
 */
public class AcideRemoveFileMenuItemAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        AcideMainWindow.getInstance().getMenu().getProjectMenu().getRemoveFileMenuItem().setEnabled(true);
        AcideMainWindow.getInstance().getMenu().getProjectMenu().getRemoveFileMenuItem().doClick();
    }
}
