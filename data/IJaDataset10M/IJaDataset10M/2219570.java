package org.docflower.model;

public class EntityAttributesTreeItem {

    private String id;

    private String label;

    private String description;

    private EntityAttributesTreeItem parent;

    private EntityAttributesTreeItem[] children;

    public boolean hasChildren() {
        return (children != null && children.length > 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EntityAttributesTreeItem getParent() {
        return parent;
    }

    public void setParent(EntityAttributesTreeItem parent) {
        this.parent = parent;
    }

    public void setChildren(EntityAttributesTreeItem[] children) {
        this.children = children;
    }

    public EntityAttributesTreeItem[] getChildren() {
        return children;
    }
}
