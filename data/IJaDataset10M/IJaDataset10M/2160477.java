package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Link;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A implementation of the link interface.
 * @author Christian "blair" Schwartz
 */
public class LinkImpl implements Link {

    /**
     * Property change support for all link properties.
     */
    protected PropertyChangeSupport support;

    /**
     * The links name.
     */
    protected String name;

    /**
     * The links source node.
     */
    protected Node source;

    /**
     * The links target node.
     */
    protected Node target;

    /**
     * Create a new Link instance.
     */
    LinkImpl() {
        support = new PropertyChangeSupport(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange(PROPERTY_NAME, oldName, name);
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        Node oldSource = this.source;
        this.source = source;
        support.firePropertyChange(PROPERTY_SOURCE, oldSource, source);
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        Node oldTarget = this.target;
        this.target = target;
        support.firePropertyChange(PROPERTY_TARGET, oldTarget, target);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return source.getName() + " -- " + name + "->" + target.getName();
    }
}
