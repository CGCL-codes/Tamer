package net.sf.hipster.model;

import java.util.List;

/**
 * Represents a tag in the {@link Library}.
 */
public class Tag extends net.sf.hipster.model.List {

    /** The tag name. */
    public String name;

    /**
     * Instantiates a new tag.
     *
     * @param name
     *            the tag name
     */
    public Tag(String name) {
        super(name);
        this.name = name;
    }

    /**
     * Gets the tag name.
     *
     * @return the tag name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name name.
     *
     * @param name
     *            the new tag name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIconName() {
        return "tag";
    }

    public boolean isLeaf() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HipsterTreeNode> getChildren() {
        return Library.getInstance().getSession().createQuery("from Task as task where task.text like :tag").setParameter("tag", "%" + getName() + "%").setCacheable(true).list();
    }

    public String toString() {
        return getName();
    }
}
