package org.adapit.wctoolkit.infrastructure.treecontrollers;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.adapit.wctoolkit.models.util.ObserverMutableTreeNode;

public class WorkCASETreeSelectionListener implements TreeSelectionListener {

    private DefaultElementTreeController ctrl;

    public WorkCASETreeSelectionListener(DefaultElementTreeController ctrl) {
        super();
        this.ctrl = ctrl;
    }

    public void valueChanged(TreeSelectionEvent e) {
        try {
            ObserverMutableTreeNode node = (ObserverMutableTreeNode) (e.getPath().getLastPathComponent());
            if (node == null) return;
            Object o = (Object) node.getElement();
            ctrl.setSelected(o);
            ctrl.getDefaultContentPane().setElement(node.getElement());
            ctrl.setSelectedNode(node);
            for (int i = 0; i < e.getPaths().length; i++) {
                if (e.isAddedPath(i)) ctrl.getSelectedElements().add(e.getPaths()[i]); else ctrl.getSelectedElements().remove(e.getPaths()[i]);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
