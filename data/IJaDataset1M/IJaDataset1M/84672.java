package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel.PaloTreeNode;

public abstract class FolderNode extends PaloTreeNode {

    public abstract String getFolderName();

    public FolderNode(PaloTreeModel model, XObject object) {
        super(model, object);
    }

    public String toString() {
        return "FolderNode[" + getXObject().getName() + "/" + getFolderName() + "]";
    }
}
