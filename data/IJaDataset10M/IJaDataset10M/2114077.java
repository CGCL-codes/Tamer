package com.od.jtimeseries.util.identifiable;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 13-Dec-2010
 * Time: 20:38:44
 *
 * This listener will receive events when an identifiable/context tree changes
 * The important thing to note is that the context tree is a data structure which changes asynchronously, and these tree listener events are received asynchronously, after the tree structure has changed. The structure may have undergone subsequent changes by the time these events are received.
 *
 * Therefore, when you receive events notifying the addition or removal of nodes (possibly with their associated subtree/descendants), those nodes themselves may have changed (had more descendants added, for example) since the event was fired. When you interrogate the nodes referenced in the event you are looking at the current state of those nodes in the context tree, rather than the state when the event was fired. An alternative design would involve cloning the subtree structure for the changed nodes to use within the event - but that would have severe performance implications.  It is better to make listeners responsible for handling this.
 *
 * In general, any traversal of the context tree, or subtrees within it, should be done while holding the context tree lock, to ensure that the structure does not change while traversal is taking place.
 */
public interface IdentifiableTreeListener {

    /**
     * called when the node to which the listener was added changes
     */
    public void nodeChanged(Identifiable node, Object changeDescription);

    /**
     * descendant nodes changed in a way which did not affect tree structure (e.g. node description changed). The change is limited to the listed nodes in the IdentifiableTreeEvent, descendant nodes from the listed nodes are not assumed to be changed.
     */
    public void descendantChanged(IdentifiableTreeEvent contextTreeEvent);

    /**
     * descendant nodes were added.
     */
    public void descendantAdded(IdentifiableTreeEvent contextTreeEvent);

    /**
     * descendant nodes were removed
     */
    public void descendantRemoved(IdentifiableTreeEvent contextTreeEvent);
}
