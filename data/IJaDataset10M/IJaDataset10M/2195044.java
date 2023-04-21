package com.sh.architecture.controller;

import com.sh.architecture.model.AbstractTreeModel;
import com.sh.architecture.model.NodeModel;

/**
 * Result Framework Controller 
 * 
 * @author sursini
 *
 * @param <TreeModelClass>
 */
public class RFController<TreeModelClass extends AbstractTreeModel<?>> extends AbstractTreeController<TreeModelClass> {

    private NodeModel<?> current = null;

    public NodeModel<?> getCurrent() {
        return current;
    }

    public void setCurrent(NodeModel<?> current) {
        this.current = current;
    }

    public void delete() {
        getModel().removeNodeFromParent(current);
    }

    public boolean hasCurrent() {
        return current != null;
    }
}
