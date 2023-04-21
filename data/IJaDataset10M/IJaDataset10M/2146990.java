package com.google.template.soy.basetree;

/**
 * Abstract implementation of a Node.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 */
public abstract class AbstractNode implements Node {

    /** Just spaces. */
    protected static final String SPACES = "                                        ";

    /** The parent of this node. */
    private ParentNode<?> parent;

    protected AbstractNode() {
        parent = null;
    }

    /**
   * Copy constructor.
   * @param orig The node to copy.
   */
    protected AbstractNode(AbstractNode orig) {
        parent = null;
    }

    @Override
    public void setParent(ParentNode<?> parent) {
        this.parent = parent;
    }

    @Override
    public ParentNode<?> getParent() {
        return parent;
    }

    @Override
    public boolean hasAncestor(Class<? extends Node> ancestorClass) {
        for (Node node = this; node != null; node = node.getParent()) {
            if (ancestorClass.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <N extends Node> N getNearestAncestor(Class<N> ancestorClass) {
        for (Node node = this; node != null; node = node.getParent()) {
            if (ancestorClass.isInstance(node)) {
                return ancestorClass.cast(node);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toTreeString(int indent) {
        return SPACES.substring(0, indent) + "[" + toString() + "]\n";
    }

    @Override
    public abstract Node clone();
}
