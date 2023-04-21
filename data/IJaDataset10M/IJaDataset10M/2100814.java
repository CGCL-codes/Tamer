package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElementNode;
import com.tensegrity.palowebviewer.modules.paloclient.client.XHierarchy;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public class HirarchyElementFolder extends FolderNode {

    /**
	 * Constructs the node.
	 * @param model tree model
	 * @param object parent subset object.
	 */
    public HirarchyElementFolder(PaloTreeModel model, XHierarchy hierarchy) {
        super(model, hierarchy);
    }

    /**
     * {@inheritDoc}
     */
    public String getFolderName() {
        return "Elements";
    }

    /**
	 * Returns parent subset object.
	 * @return parent subset.
	 */
    public XHierarchy getHierarchy() {
        return (XHierarchy) getXObject();
    }

    /**
     * {@inheritDoc}
     */
    protected PaloTreeNode createNode(XObject obj) {
        return new ElementNodeNode(getPaloTreeModel(), (XElementNode) obj);
    }

    /**
     * {@inheritDoc}
     */
    protected int getChildType() {
        return IXConsts.TYPE_ELEMENT_NODE;
    }

    /**
     * {@inheritDoc}
     */
    protected XObject[] getXObjectChildren() {
        return getHierarchy().getNodes();
    }
}
