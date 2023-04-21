package net.walend.digraph;

import java.util.Set;

/**
MutableUEDigraph adds mutators to the UEDigraph interface so that a developer can add and remove edges and nodes.
<p>
Implementations should provide three constructors: a constructor with no arguments that creates a reasonably small UEDigraph, a constructor that supplies arguments for the initial number of nodes
and edges as appropriate, and a copy constructor that has a single UEDigraph as a parameter. This last constructor should copy the digraph into itself.

@author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
@since 20010612
*/
public interface MutableUEDigraph extends UEDigraph {

    /**
Return true if the node is added successfully, false if the digraph does not change.
*/
    public boolean addNode(Object node);

    /**
Return null if no existing edge is displaced by edge. Otherwise, return the edge that is displaced.

@throws NodeMissingException if either node is not in the digraph.
@throws EdgeNotUniqueException if the edge is already in the digraph.
*/
    public Object addEdge(Object fromNode, Object toNode, Object edge) throws NodeMissingException, EdgeNotUniqueException;

    /**
Return the Set of orphaned edges that are removed with node

@throws NodeMissingException if the node is not in the digraph
*/
    public Set removeNode(Object node) throws NodeMissingException;

    /**
Return true if this edge existed in the digraph, false if this operation doesn't change the digraph at all.
*/
    public boolean removeEdge(Object edge);

    /**
Return the edge that connected fromNode to toNode, or null if no edge existed.

@throws NodeMissingException if either node is not in the digraph
*/
    public Object removeEdge(Object fromNode, Object toNode) throws NodeMissingException;

    /**
Return true if adding the nodes changes the digraph.
*/
    public boolean addNodes(Set nodes);

    /**
Return the Set of edges orphaned edges when these nodes are removed.
*/
    public Set removeNodes(Set nodes);

    /**
Return true if any edges were removed from the digraph. False if not.
*/
    public boolean removeEdges(Set edges);

    /**
Return a Set of edges orphaned when digraph is removed
*/
    public Set removeUEDigraph(UEDigraph digraph);

    /**
Return a Set of orphaned edges when the nodes are removed.
*/
    public Set retainNodes(Set nodes);

    /**
Return true if this UEDigraph chages at all, false if not.
*/
    public boolean retainEdges(Set edges);

    /**
Remove all nodes and edges from the UEDigraph
*/
    public void clear();

    /**
Remove all the edges from the UEDigraph.
*/
    public void clearEdges();
}
