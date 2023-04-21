package edu.byu.ece.edif.util.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import edu.byu.ece.edif.core.EdifCell;
import edu.byu.ece.edif.core.EdifPort;
import edu.byu.ece.edif.core.EdifSingleBitPort;
import edu.byu.ece.graph.BasicGraph;
import edu.byu.ece.graph.Edge;
import edu.byu.ece.graph.HierarchicalGraph;
import edu.byu.ece.graph.NodeEdgeMap;

/**
 * A graph view of an EdifCell object in which the graph nodes are collections
 * of instances (EdifCellInstanceCollection objects). This class requires an
 * instance of the EdifCellInstanceGraph during construction. This connectivity
 * information is used to determine the edge relationships between grouped
 * nodes.
 * <p>
 * TODO: I think this class should be named "EdifCellGroupConnectivity". The use
 * of "instance" is confusing later on as I can't distinguish it from the
 * EdifCellInstanceGraph.
 */
public class EdifCellInstanceCollectionGraph extends AbstractEdifGraph implements HierarchicalGraph {

    public EdifCellInstanceCollectionGraph(EdifCell cell, EdifCellInstanceGroupings groupings, boolean includeTopLevelPorts) {
        this(new EdifCellInstanceGraph(cell), groupings, includeTopLevelPorts);
    }

    /**
     * Default to include Top Level Ports in the graph.
     */
    public EdifCellInstanceCollectionGraph(EdifCellInstanceGraph ecic, EdifCellInstanceGroupings groupings) {
        this(ecic, groupings, true);
    }

    public EdifCellInstanceCollectionGraph(EdifCellInstanceGraph ecic, EdifCellInstanceGroupings groupings, boolean includeTopLevelPorts) {
        super(groupings.getNumberGroups());
        _cell = ecic.getCell();
        _ecic = ecic;
        _groupings = groupings;
        _topLevelPortNodes = new LinkedHashSet();
        _init(ecic, includeTopLevelPorts);
    }

    protected EdifCellInstanceCollectionGraph(EdifCellInstanceCollectionGraph groupConn) {
        super(groupConn);
        _cell = groupConn._cell;
        _topLevelPortNodes = new LinkedHashSet(groupConn._topLevelPortNodes);
        _groupings = new EdifCellInstanceGroupings(groupConn._groupings);
    }

    public Object clone() {
        return new EdifCellInstanceCollectionGraph(this);
    }

    /**
     * @param node A node to look for in the inner graph
     * @return true if the given node is found in the inner graph
     */
    public boolean containsInnerNode(Object node) {
        return _ecic.containsNode(node);
    }

    public EdifCell getCell() {
        return _cell;
    }

    /**
     * Removes the given node Object from the graph
     * 
     * @param edge The node Object to remove
     * @param removeAllEdges If true, removes ALL Edges that refer to this node,
     * not just the ones mapped from this node (the easy-to-get-to Edges). In
     * other words, true removes ALL references to this node, while false may
     * leave some Edges that refer to this node.
     */
    public void removeNode(Object node, boolean removeAllEdges) {
        super.removeNode(node, removeAllEdges);
        _topLevelPortNodes.remove(node);
    }

    public Collection getTopLevelPortNodes() {
        return new ArrayList(_topLevelPortNodes);
    }

    public boolean isNodeTopLevelPort(Object node) {
        if (node instanceof EdifSingleBitPort) return true; else return false;
    }

    public EdifCellInstanceGroupings getGroupings() {
        return _groupings;
    }

    /**
     * @param edge An Edge object in thie HierarchicalGraph
     * @return A Collection of Edge objects corresponding to the given edge's
     * inner edges (those that make up this edge)
     */
    public Collection<EdifCellInstanceEdge> getInnerEdges(Edge edge) {
        if (edge instanceof EdifCellInstanceCollectionLink) return ((EdifCellInstanceCollectionLink) edge).getLinks(); else return null;
    }

    /**
     * @return The inside graph that this graph is built upon
     */
    public EdifCellInstanceGraph getInnerGraph() {
        return _ecic;
    }

    /**
     * @param node A node in this HierarchicalGraph
     * @return A Collection of nodes from the inner graph that correspond to the
     * given node in this graph
     */
    public Collection getInnerNodes(Object node) {
        if (node instanceof EdifCellInstanceCollection) return (EdifCellInstanceCollection) node; else return null;
    }

    /**
     * @param node A node in this HierarchicalGraph
     * @return A DirectedGraph object which is the subgraph corresponding to the
     * given node's inner nodes or null if the node passed in isn't of the
     * correct type
     */
    public EdifCellInstanceGraph getNodeGraph(Object node) {
        if (node instanceof EdifCellInstanceCollection) return _ecic.getSubGraph((EdifCellInstanceCollection) node); else return null;
    }

    /**
     * @param node A node in the inner graph
     * @return The node in this HierarchicalGraph that corresponds to the given
     * inner node or null if not found.
     */
    public Object getParentNode(Object node) {
        for (EdifCellInstanceCollection group : _groupings.getInstanceGroups()) {
            if (group.contains(node)) return group;
        }
        return null;
    }

    /**
     * Creates an EdifCellInstanceCollectionGraph object that contains only the
     * information linking the given Collection of EdifCellInstanceCollection
     * objects.
     * 
     * @param ecics The Collection of EdifCellInstanceCollection objects of
     * interest
     * @return A new EdifCellInstanceCollectionGraph object containing only the
     * given ecics
     */
    public BasicGraph getSubGraph(Collection eciColls) {
        EdifCellInstanceCollectionGraph ecigc = (EdifCellInstanceCollectionGraph) super.getSubGraph(eciColls);
        ecigc._groupings.retainGroups(eciColls);
        return ecigc;
    }

    /**
     * Merges the two passed-in groups by adding the contents of group1 into
     * group2. group1 is deleted and group2 remains.
     * 
     * @param group1 The group to remove
     * @param group2 The group to add to
     * @return A reference to the expanded group (group2)
     */
    public EdifCellInstanceCollection mergeGroupIntoGroup(EdifCellInstanceCollection group1, EdifCellInstanceCollection group2) {
        _groupings.mergeGroupIntoGroup(group1, group2);
        Collection newSourceNodes = new LinkedHashSet();
        Collection newSinkNodes = new LinkedHashSet();
        Collection inputEdges = getInputEdges(group1);
        for (Iterator i = inputEdges.iterator(); i.hasNext(); ) {
            EdifCellInstanceCollectionLink inputEdge = (EdifCellInstanceCollectionLink) i.next();
            removeEdge(inputEdge);
            inputEdge.setSink(group2);
            Object sourceNode = inputEdge.getSource();
            if (sourceNode != group2) {
                addEdge(inputEdge);
                newSourceNodes.add(sourceNode);
            }
        }
        Collection outputEdges = getOutputEdges(group1);
        for (Iterator i = outputEdges.iterator(); i.hasNext(); ) {
            EdifCellInstanceCollectionLink outputEdge = (EdifCellInstanceCollectionLink) i.next();
            removeEdge(outputEdge);
            outputEdge.setSource(group2);
            Object sinkNode = outputEdge.getSink();
            if (sinkNode != group2) {
                addEdge(outputEdge);
                newSinkNodes.add(sinkNode);
            }
        }
        removeNode(group1, false);
        _mergeEdges(group2, newSourceNodes, true);
        _mergeEdges(group2, newSinkNodes, false);
        return group2;
    }

    /**
     * Initializes the connectivity maps for the given cell. Note that this
     * happens only once and that this does not track changes in the EdifCell.
     * If the cell changes, this connectivity will be stale.
     */
    protected void _init(EdifCellInstanceGraph ecic, boolean includeTopLevelPorts) {
        this.addNodes(_groupings.getInstanceGroups());
        if (includeTopLevelPorts) {
            for (Iterator i = _cell.getPortList().iterator(); i.hasNext(); ) {
                EdifPort p = (EdifPort) i.next();
                this.addNodes(p.getSingleBitPortList());
            }
        }
        Map nodesToCollectionLink = new LinkedHashMap();
        for (Iterator i = ecic.getEdges().iterator(); i.hasNext(); ) {
            EdifCellInstanceEdge edge = (EdifCellInstanceEdge) i.next();
            if (!includeTopLevelPorts && (edge.isSourceTopLevel() || edge.isSinkTopLevel())) continue;
            Object source = _groupings.getGroup(edge.getSource());
            if (source == null) source = edge.getSourceEPR().getSingleBitPort();
            Object sink = _groupings.getGroup(edge.getSink());
            if (sink == null) sink = edge.getSinkEPR().getSingleBitPort();
            if (source == sink) continue;
            EdifCellInstanceCollectionLink collEdge = _addEdgeToCollectionLinksMap(edge, source, sink, nodesToCollectionLink, _groupings);
            if (collEdge == null) continue;
            if (edge.isSourceTopLevel()) {
                _topLevelPortNodes.add(source);
            }
            if (edge.isSinkTopLevel()) {
                _topLevelPortNodes.add(sink);
            }
            addEdge(collEdge);
        }
    }

    /**
     * A double-mapping to keep track of CollectionLink objects based on their
     * source and sink groups. The mapping is: source group -> map2, map2: sink
     * group -> CollectionLink
     * 
     * @param edge The EdifCellInstanceEdge to add
     * @param nodesToCollectionLink The double Map
     * @param groupings The associated EdifCellInstanceGroupings object
     * @return The EdifCellInstanceCollectionLink object that this edge was
     * added to.
     */
    protected static EdifCellInstanceCollectionLink _addEdgeToCollectionLinksMap(EdifCellInstanceEdge edge, Object sourceGroup, Object sinkGroup, Map nodesToCollectionLink, EdifCellInstanceGroupings groupings) {
        Map sinkToCollectionLinkMap = (Map) nodesToCollectionLink.get(sourceGroup);
        if (sinkToCollectionLinkMap == null) {
            sinkToCollectionLinkMap = new LinkedHashMap();
            nodesToCollectionLink.put(sourceGroup, sinkToCollectionLinkMap);
        }
        EdifCellInstanceCollectionLink collLink = (EdifCellInstanceCollectionLink) sinkToCollectionLinkMap.get(sinkGroup);
        if (collLink == null) {
            collLink = new EdifCellInstanceCollectionLink(sourceGroup, sinkGroup, groupings);
            sinkToCollectionLinkMap.put(sinkGroup, collLink);
        }
        collLink.addLinkNoCheck(edge);
        return collLink;
    }

    /**
     * Examines the input or output Edges (depending on checkSourceEdges) for
     * CollectionLink Edges that should be merged. Only checks for duplicate
     * Edges with the given new nodes as predecessor/successor nodes.
     */
    protected void _mergeEdges(EdifCellInstanceCollection group, Collection newConnectedNodes, boolean checkSourceEdges) {
        NodeEdgeMap nodeMap = new NodeEdgeMap(newConnectedNodes.size(), checkSourceEdges);
        Collection edges;
        if (checkSourceEdges) edges = getInputEdges(group); else edges = getOutputEdges(group);
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge edge = (Edge) i.next();
            if ((checkSourceEdges && newConnectedNodes.contains(edge.getSource())) || (!checkSourceEdges && newConnectedNodes.contains(edge.getSink()))) nodeMap.addEdge(edge);
        }
        for (Iterator i = newConnectedNodes.iterator(); i.hasNext(); ) {
            Object node = i.next();
            Collection edgeColl = (Collection) nodeMap.get(node);
            if (edgeColl.size() > 1) {
                if (checkSourceEdges) _mergeEdgesNoCheck(edgeColl, node, group); else _mergeEdgesNoCheck(edgeColl, group, node);
            }
        }
    }

    /**
     * Merges the given Collection of EdifCellInstanceCollectionLink objects
     * into one. WARNING: This method assumes that the Edges have the same
     * source and sink. It will not check this assumption.
     */
    protected void _mergeEdgesNoCheck(Collection edges, Object source, Object sink) {
        EdifCellInstanceCollectionLink newEdge = new EdifCellInstanceCollectionLink(source, sink, _groupings);
        addEdge(newEdge);
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            EdifCellInstanceCollectionLink oldEdge = (EdifCellInstanceCollectionLink) i.next();
            newEdge.addLinks(oldEdge.getLinks());
            removeEdge(oldEdge);
        }
    }

    protected EdifCell _cell;

    protected EdifCellInstanceGraph _ecic;

    protected EdifCellInstanceGroupings _groupings;

    /**
     * A Collection to keep track of the Top Level Port Nodes in this graph
     */
    protected Collection _topLevelPortNodes;

    public static boolean DEBUG = false;
}
