package ca.sqlpower.architect.swingui.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import ca.sqlpower.architect.swingui.ArchitectFrame;
import ca.sqlpower.architect.swingui.DBTree;

public class CutSelectedAction extends AbstractArchitectAction {

    public CutSelectedAction(ArchitectFrame frame) {
        super(frame, Messages.getString("CutSelectedAction.name"), Messages.getString("CutSelectedAction.description"));
        putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(ActionEvent e) {
        final DBTree dbTree = getSession().getDBTree();
        final Component focusOwner = getSession().getArchitectFrame().getFocusOwner();
        if (getSession().getArchitectFrame().isAncestorOf(focusOwner)) {
            dbTree.cutSelection();
        }
    }
}
