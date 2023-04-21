package pcgen.base.graph.core;

import java.util.EventListener;

/**
 * @author Thomas Parker (thpr [at] yahoo.com)
 * 
 * The listener interface for receiving EdgeChangeEvents and NodeChangeEvents.
 * When an Edge or Node has been added to or removed from a Graph, the
 * respective method in the listener object is invoked, and the EdgeChangeEvent
 * or NodeChangeEvent is passed to it.
 */
public interface GraphChangeListener<N, ET extends Edge<N>> extends EventListener {

    /**
	 * Method called when a Node has been added to a Graph and this
	 * GraphChangeListener has been added as a GraphChangeListener to the source
	 * Graph.
	 * 
	 * @param gce
	 *            The NodeChangeEvent that occurred.
	 */
    public void nodeAdded(NodeChangeEvent<N> gce);

    /**
	 * Method called when a Node has been removed from a Graph and this
	 * GraphChangeListener has been added as a GraphChangeListener to the source
	 * Graph.
	 * 
	 * @param gce
	 *            The NodeChangeEvent that occurred.
	 */
    public void nodeRemoved(NodeChangeEvent<N> gce);

    /**
	 * Method called when an Edge has been added to a Graph and this
	 * GraphChangeListener has been added as a GraphChangeListener to the source
	 * Graph.
	 * 
	 * @param gce
	 *            The EdgeChangeEvent that occurred.
	 */
    public void edgeAdded(EdgeChangeEvent<N, ET> gce);

    /**
	 * Method called when an Edge has been removed from a Graph and this
	 * GraphChangeListener has been added as a GraphChangeListener to the source
	 * Graph.
	 * 
	 * @param gce
	 *            The EdgeChangeEvent that occurred.
	 */
    public void edgeRemoved(EdgeChangeEvent<N, ET> gce);
}
