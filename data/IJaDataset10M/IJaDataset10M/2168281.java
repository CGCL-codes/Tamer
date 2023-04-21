package org.adapit.wctoolkit.events.actions.gen;

import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.events.actions.AbstractAction;
import org.adapit.wctoolkit.infrastructure.treecontrollers.DefaultElementTreeController;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.NamedElement;

public class RenameAction extends AbstractAction {

    public RenameAction(IElement element, DefaultElementTreeController controller) {
        super(element, controller);
    }

    public RenameAction() {
        super();
    }

    @Override
    protected IElement[] doAction(ActionEvent e) {
        try {
            if (DefaultApplicationFrame.getInstance().getDefaultContentPane().getSelectedElement() != null) {
                element = DefaultApplicationFrame.getInstance().getDefaultContentPane().getSelectedElement();
                if (element instanceof NamedElement) {
                    JTree jt = DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController().getTree();
                    TreePath tp = new TreePath(element.getNode().getPath());
                    selectParent(jt, tp, false);
                    jt.startEditingAtPath(tp);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private void selectParent(JTree tree, TreePath parent, boolean expand) {
        try {
            if (expand) {
                tree.makeVisible(parent);
                tree.setSelectionPath(parent);
            } else {
                tree.collapsePath(parent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
