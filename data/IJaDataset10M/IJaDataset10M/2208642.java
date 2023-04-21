package com.loribel.tools.xa.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import com.loribel.commons.abstraction.GB_NodesOwner;
import com.loribel.commons.abstraction.GB_ObjectAction;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.gui.GB_TreeVMDefault;
import com.loribel.tools.xa.vm.GB_XAToolsModel;
import com.loribel.tools.xa.vm.GB_XAToolsMultiVM;

/**
 * View manager to use XA Tools.
 */
public class GB_AllXATreeNodeMultiVM extends GB_XAToolsMultiVM implements GB_XAToolsModel {

    /**
     * Inner class to use the selected node as Xml Action.
     */
    private class MyObjectAction implements GB_ObjectAction {

        public void doAction(Object a_node) throws Exception {
            TreePath l_treePath = treeVM.getMainTree().getSelectionPath();
            if (l_treePath != null) {
                Object l_node = l_treePath.getLastPathComponent();
                if (l_node instanceof GB_XmlActionTreeNode) {
                    GB_ObjectAction l_xa = ((GB_XmlActionTreeNode) l_node).getXmlAction();
                    l_xa.doAction(a_node);
                }
            }
        }
    }

    private GB_TreeVMDefault treeVM;

    private GB_ObjectAction xa;

    public GB_AllXATreeNodeMultiVM(DefaultMutableTreeNode a_root, GB_NodesOwner a_nodesOwner) {
        super();
        xa = new MyObjectAction();
        treeVM = new GB_TreeVMDefault();
        treeVM.setRoot(a_root);
        setModel(this);
        setNodesOwner(a_nodesOwner);
    }

    public GB_ViewManager getViewManager() {
        return treeVM;
    }

    public GB_ObjectAction getXmlAction() {
        return xa;
    }

    public void loadDataToView() {
        treeVM.setWidthForTree(200);
    }
}
