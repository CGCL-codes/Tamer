package org.eclipse.plugin.worldwind.views.tree;

import gov.nasa.worldwind.layers.Layer;
import java.util.ArrayList;
import org.eclipse.swt.graphics.Image;

public class TreeParent extends TreeObject {

    private ArrayList<TreeObject> children;

    public TreeParent(Layer layer) {
        super(layer);
        children = new ArrayList<TreeObject>();
    }

    public TreeParent(Layer layer, Image image) {
        super(layer, image);
        children = new ArrayList<TreeObject>();
    }

    public void addChild(TreeObject child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(TreeObject child) {
        children.remove(child);
        child.setParent(null);
    }

    public void clearChildren() {
        children.clear();
    }

    public TreeObject[] getChildren() {
        return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void setRemovable(boolean removable) {
        super.setRemovable(removable);
        for (TreeObject to : children) {
            to.setRemovable(removable);
        }
    }
}
