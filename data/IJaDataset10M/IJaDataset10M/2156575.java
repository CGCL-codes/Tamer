package org.bdgp.cv.datamodel;

import javax.swing.tree.*;

public interface DAGModel extends TreeModel {

    public Object getParent(Object child, int index);

    public int getParentCount(Object child);

    public int getIndexOfParent(Object child, Object parent);
}
