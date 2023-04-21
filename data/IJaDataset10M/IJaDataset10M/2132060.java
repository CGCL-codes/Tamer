package acide.gui.consolePanel.listeners;

import acide.gui.consolePanel.AcideConsolePanel;
import acide.gui.mainWindow.AcideMainWindow;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ACIDE - A Configurable IDE console panel popup menu listener.										
 *					
 * @version 0.8	
 * @see MouseAdapter																													
 */
public class AcideConsolePanelPopupMenuListener extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        maybeShowPopup(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        maybeShowPopup(mouseEvent);
    }

    /**
	 * Shows the popup menu.
	 * 
	 * @param mouseEvent
	 *            mouse event.
	 */
    private void maybeShowPopup(MouseEvent mouseEvent) {
        AcideConsolePanel consolePanel = AcideMainWindow.getInstance().getConsolePanel();
        if (mouseEvent.isPopupTrigger()) {
            if (consolePanel.getTextPane().getSelectedText() == null) {
                consolePanel.getPopupMenu().getCopyMenuItem().setEnabled(false);
                consolePanel.getPopupMenu().getCutMenuItem().setEnabled(false);
            } else {
                consolePanel.getPopupMenu().getCopyMenuItem().setEnabled(true);
                if (consolePanel.getTextPane().getSelectionStart() < consolePanel.getPromptCaretPosition()) consolePanel.getPopupMenu().getCutMenuItem().setEnabled(false); else consolePanel.getPopupMenu().getCutMenuItem().setEnabled(true);
            }
            if (consolePanel.getTextPane().getSelectionStart() < consolePanel.getPromptCaretPosition()) consolePanel.getPopupMenu().getPasteMenuItem().setEnabled(false); else consolePanel.getPopupMenu().getPasteMenuItem().setEnabled(true);
            if (consolePanel.getTextPane().isEditable()) consolePanel.getPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        }
    }
}
