package org.jefb.web.model.filetree;

import java.util.List;
import org.zkoss.zul.SimpleTreeNode;

public interface IFileSystemNode {

    boolean isLeaf();

    List<SimpleTreeNode> getChildren();

    public SimpleTreeNode getChildAt(int childIndex);

    void loadChildren();

    String getPath();
}
