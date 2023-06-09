package com.open_squad.openplan.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.geometry.Rectangle;

public class Node {

    private Rectangle layout;

    private List<Node> children;

    private Node parent;

    private PropertyChangeSupport listeners;

    public static final String PROPERTY_LAYOUT = "NodeLayout";

    public Node() {
        this.layout = new Rectangle();
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.listeners = new PropertyChangeSupport(this);
    }

    public void setLayout(Rectangle newLayout) {
        Rectangle oldLayout = this.layout;
        this.layout = newLayout;
        getListeners().firePropertyChange(PROPERTY_LAYOUT, oldLayout, newLayout);
    }

    public Rectangle getLayout() {
        return this.layout;
    }

    public boolean addChild(Node child) {
        child.setParent(this);
        return this.children.add(child);
    }

    public boolean removeChild(Node child) {
        return this.children.remove(child);
    }

    public List<Node> getChildrenArray() {
        return this.children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return this.parent;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public PropertyChangeSupport getListeners() {
        return listeners;
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }
}
