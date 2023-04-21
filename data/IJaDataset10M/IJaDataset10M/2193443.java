package com.g2d.studio.cpj;

import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import com.g2d.studio.cpj.entity.CPJObject;
import com.g2d.studio.swing.G2DList;
import com.g2d.studio.swing.G2DTree;

public class CPJResourceList<T extends CPJObject<?>> extends G2DList<T> {

    private static final long serialVersionUID = 1L;

    final Class<T> type;

    CPJResourceList(Class<T> type, DefaultMutableTreeNode root) {
        this.type = type;
        Vector<T> list = G2DTree.getNodesSubClass(root, type);
        super.setListData(list);
    }
}
