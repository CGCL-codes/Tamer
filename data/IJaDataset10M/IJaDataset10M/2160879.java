package org.eyrene.javaj.pattern;

/**
 * <p>Title: CompositeLeaf.java</p>
 * <p>Description: Pattern Composite - Leaf (extends Model)</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class CompositeLeaf extends MVCModel implements CompositeComponent {

    protected CompositeComponent parent;

    private final int id;

    private String name;

    public CompositeLeaf(int id) {
        this(id, "Composite Leaf - " + id);
    }

    public CompositeLeaf(int id, String name) {
        if (name == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        this.name = name;
    }

    public CompositeComponent getParent() {
        return parent;
    }

    public void setParent(CompositeComponent parent) {
        this.parent = parent;
    }

    public int getChildrenCount() {
        return 0;
    }

    public CompositeComponent getChild(int id) {
        throw new UnsupportedOperationException("Operation not implemented for leaf node!");
    }

    public void addChild(CompositeComponent child) {
        throw new UnsupportedOperationException("Operation not implemented for leaf node!");
    }

    public CompositeComponent removeChild(int id) {
        throw new UnsupportedOperationException("Operation not implemented for leaf node!");
    }

    public String toString() {
        return getName();
    }

    public int hashCode() {
        return getID();
    }
}
