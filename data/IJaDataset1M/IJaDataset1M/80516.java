package net.sourceforge.jnipp.pman;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;

class RemoveAllSectionsAction extends AbstractAction {

    private static RemoveAllSectionsAction removeAllSectionsAction = new RemoveAllSectionsAction();

    protected static RemoveAllSectionsAction getRemoveAllSectionsAction() {
        return removeAllSectionsAction;
    }

    public RemoveAllSectionsAction() {
        super("Remove All Sections", new ImageIcon(RemoveAllSectionsAction.class.getResource("/net/sourceforge/jnipp/pman/images/Delete.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode currentlySelectedNode = MainFrame.getMainFrame().getCurrentlySelectedNode();
        if (currentlySelectedNode == null) return;
        if (currentlySelectedNode instanceof ProjectNode) {
            MainFrame.getMainFrame().getProject().removeAllPeerGenSettings();
            MainFrame.getMainFrame().getProject().removeAllProxyGenSettings();
        } else if (currentlySelectedNode instanceof AllPeerGenSettingsNode) {
            MainFrame.getMainFrame().getProject().removeAllPeerGenSettings();
        } else {
            MainFrame.getMainFrame().getProject().removeAllProxyGenSettings();
        }
        ((DefaultTreeModel) MainFrame.getMainFrame().getTree().getModel()).nodeStructureChanged(currentlySelectedNode);
    }
}
