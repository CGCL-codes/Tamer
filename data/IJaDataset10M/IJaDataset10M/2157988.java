package org.skunk.dav.client.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.skunk.dav.client.gui.DAVTreeNode;
import org.skunk.dav.client.gui.Explorer;
import org.skunk.dav.client.gui.ResourceManager;

public class NewFileAction extends AbstractAction {

    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Explorer ex;

    public NewFileAction(Explorer ex) {
        this.ex = ex;
    }

    public void actionPerformed(ActionEvent ae) {
        DAVTreeNode node = ex.getSelectedNode();
        if (node != null) {
            String message = ResourceManager.getMessage(ResourceManager.NEW_FILE_PROMPT);
            String title = ResourceManager.getMessage(ResourceManager.NEW_FILE_DIALOG_TITLE);
            String fileName = JOptionPane.showInputDialog(ex, message, title, JOptionPane.QUESTION_MESSAGE);
            if (fileName == null || fileName.trim().length() == 0) {
                log.trace("user has cancelled or submitted whitespace");
                return;
            }
            ex.createFile(node, fileName);
        } else {
            log.trace("error -- new file action requested outside of DAV connection");
        }
    }
}
