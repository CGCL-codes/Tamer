package acide.gui.statusBarPanel.popup.listeners;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import acide.gui.mainWindow.AcideMainWindow;

/**
 * ACIDE - A Configurable IDE status bar popup menu console copy menu
 * item action listener.
 * 
 * @version 0.8
 * @see ActionListener
 */
public class AcideCopyMenuItemAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        StringSelection data = new StringSelection(AcideMainWindow.getInstance().getStatusBar().getStatusMessage().getText());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(data, data);
    }
}
