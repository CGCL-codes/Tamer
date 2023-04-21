package org.mediavirus.graphl.graph;

import java.util.Collection;
import java.util.EventListener;
import javax.swing.event.EventListenerList;

/**
 * An abstract base class for all graphs.
 */
public abstract class AbstractGraph implements Graph {

    /** The list of listeners. */
    protected EventListenerList m_listeners;

    /**
     * Creates a new graph.
     */
    public AbstractGraph() {
        m_listeners = new EventListenerList();
    }

    /**
     * Adds a listener to the graph.
     *
     * @param listener              the listener added to the graph
     */
    public void addGraphListener(GraphListener listener) {
        m_listeners.add(GraphListener.class, listener);
    }

    /**
     * Removes a listener from the graph.
     *
     * @param listener              the listener removed from the graph
     */
    public void removeGraphListener(GraphListener listener) {
        m_listeners.remove(GraphListener.class, listener);
    }

    /**
     * Notifies the graph that its elements have been updated.
     */
    public synchronized void notifyLayoutUpdated() {
        EventListener[] listeners = m_listeners.getListeners(GraphListener.class);
        for (int i = 0; i < listeners.length; i++) ((GraphListener) listeners[i]).graphLayoutUpdated(this);
    }

    /**
     * Notifies the graph that its nodes have been updated, but that no node has changed position.
     */
    public synchronized void notifyUpdated() {
        EventListener[] listeners = m_listeners.getListeners(GraphListener.class);
        for (int i = 0; i < listeners.length; i++) ((GraphListener) listeners[i]).graphUpdated(this);
    }

    public synchronized void notifyPropertyChanged() {
        fireGraphContentsChanged();
    }

    /**
     * Fires the graph contents changed event.
     */
    public void fireGraphContentsChanged() {
        EventListener[] listeners = m_listeners.getListeners(GraphListener.class);
        for (int i = 0; i < listeners.length; i++) ((GraphListener) listeners[i]).graphContentsChanged(this);
    }

    /**
     * Fires the elements added event.
     *
     * @param nodes                 the collection of nodes
     * @param edges                 the collection of edges
     */
    protected void fireElementsAdded(Collection<Node> nodes, Collection<Edge> edges) {
        EventListener[] listeners = m_listeners.getListeners(GraphListener.class);
        for (int i = 0; i < listeners.length; i++) ((GraphListener) listeners[i]).elementsAdded(this, nodes, edges);
    }

    /**
     * Fires the elements removed event.
     *
     * @param nodes                 the collection of nodes
     * @param edges                 the collection of edges
     */
    protected void fireElementsRemoved(Collection<Node> nodes, Collection<Edge> edges) {
        EventListener[] listeners = m_listeners.getListeners(GraphListener.class);
        for (int i = 0; i < listeners.length; i++) ((GraphListener) listeners[i]).elementsRemoved(this, nodes, edges);
    }
}
