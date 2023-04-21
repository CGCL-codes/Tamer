package org.tuba.spatschorke.diploma.repository.mock.views.data;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;

public class TreeObject implements IAdaptable {

    private String name;

    private TreeObject parent = null;

    private List<TreeObject> children = new ArrayList<TreeObject>();

    public TreeObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setParent(TreeObject parent) {
        this.parent = parent;
    }

    public TreeObject getParent() {
        return parent;
    }

    public List<TreeObject> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    @Override
    public String toString() {
        return getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class key) {
        return null;
    }
}
