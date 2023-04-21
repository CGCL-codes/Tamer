package org.achup.generador.model;

import java.util.ArrayList;
import java.util.List;
import org.achup.generador.project.ProjectNode;

/**
 * The root object of the model hierarchy.
 * 
 * @author Marco Bassaletti Olivos.
 * 
 */
public class ModelNode extends ProjectNode {

    protected List<ModelNode> children;

    /**
     * Default constructor.
     */
    public ModelNode() {
        children = new ArrayList<ModelNode>();
    }

    /**
     * Copy constructor.
     * @param modelNode A model node to copy from.
     */
    public ModelNode(ModelNode modelNode) {
        super(modelNode);
        this.children = new ArrayList<ModelNode>(modelNode.children);
    }

    /**
     * Returns the index of the specified child in the internal child list.
     *
     * @param child
     *            The child to search for.
     * @return The index of this child.
     */
    public int indexOfChild(ModelNode child) {
        return children.indexOf(child);
    }

    /**
     * Adds a new child to the end of the list of children of this node.
     *
     * @param child
     *            The new child.
     */
    public void appendChild(ModelNode child) {
        children.add(child);
    }

    /**
     * Removes a child from the child list.
     *
     * @param child
     *            The child node to remove.
     * @return <code>true</code> if the child list contained the specified
     *         element.
     */
    public boolean removeChild(ModelNode child) {
        return children.remove(child);
    }

    /**
     * Returns the internal list of children of this node.
     *
     * @return The list of children.
     */
    public List<ModelNode> getChildren() {
        return children;
    }

    /**
     * Sets the internal list of children of this node.
     *
     * @param children
     *            The new list of children.
     */
    public void setChilds(List<ModelNode> childs) {
        this.children = childs;
    }

    /**
     * Removes all children from this node.
     */
    public void clearChildren() {
        if (this.children != null) {
            children.clear();
        }
    }
}
