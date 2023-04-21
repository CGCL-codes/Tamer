package org.achup.generador.project;

/**
 * Root of project hierarchy, most of @see Project members extend this class.
 * All nodes have a name and a parent node, both can be <code>null</code>.
 * 
 * @author Marco Bassaletti Olivos.
 */
public abstract class ProjectNode {

    private String name;

    private ProjectNode parent;

    /**
     * Default constructor.
     */
    protected ProjectNode() {
        name = toString();
        parent = null;
    }

    /**
     * Copy constructor.
     * @param projectNode A project node to copy from.
     */
    protected ProjectNode(ProjectNode projectNode) {
        this.name = projectNode.name;
        this.parent = projectNode.parent;
    }

    /**
     * Gets this node name.
     *
     * @return The name of this node.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this node name.
     *
     * @param name
     *            The new name of this node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the parent of this node.
     *
     * @return The parent node or <code>null</code> if there is no parent.
     */
    public ProjectNode getParent() {
        return parent;
    }

    /**
     * Sets the parent of this node.
     *
     * @param parent
     *            The new parent node.
     */
    public void setParent(ProjectNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
