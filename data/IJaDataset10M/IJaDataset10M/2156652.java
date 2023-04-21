package org.jampa.model.disk;

public interface IDiskItem {

    public boolean hasChildren();

    public DirectoryItem getParent();

    public String getLabel();
}
