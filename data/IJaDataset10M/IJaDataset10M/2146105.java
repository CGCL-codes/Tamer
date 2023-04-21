package com.bbn.vessel.author.models;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jdom.Element;
import com.bbn.vessel.author.graphEditor.editor.GroupingGraphEditor;
import com.bbn.vessel.author.graphEditor.views.BasicBoxView;
import com.bbn.vessel.author.graphEditor.views.GraphViews;
import com.bbn.vessel.author.imspec.IMConstants;
import com.bbn.vessel.author.undo.SimpleEdit;
import com.bbn.vessel.author.undo.UndoableEdit;
import com.bbn.vessel.author.util.wizard.WizardState;
import com.bbn.vessel.core.arguments.ArgSpec;
import com.bbn.vessel.core.util.XMLHelper;

/**
 * this class describes a node that contains other nodes.
 *
 *
 * throughout the documentation and method naming of this class you will see
 * three terms that should be defined
 *
 * external terminal: the terminal on the outside of the group node, that is the
 * terminal of the node itself that allows it to connect to other nodes that it
 * does not contain
 *
 * outside terminal: the terminal inside the group that relays to an outside
 * terminal. currently they reside on a dummy node inside the group graph
 *
 * inside terminal: any terminal of a node which is contained in the group graph
 * which is connected to an outside terminal
 *
 *
 * @author RTomlinson
 */
public class GroupNode extends VesselNode {

    class mapOutsideAndExternalRunnable implements Runnable {

        private final Terminal outsideTerminal;

        private final Terminal externalTerminal;

        mapOutsideAndExternalRunnable(Terminal outsideTerminal, Terminal externalTerminal) {
            this.outsideTerminal = outsideTerminal;
            this.externalTerminal = externalTerminal;
        }

        @Override
        public void run() {
            external2Outside.put(externalTerminal, outsideTerminal);
            outside2External.put(outsideTerminal, externalTerminal);
        }
    }

    class unmapOutsideAndExternalRunnable implements Runnable {

        private final Terminal outsideTerminal;

        private final Terminal externalTerminal;

        unmapOutsideAndExternalRunnable(Terminal outsideTerminal, Terminal externalTerminal) {
            this.outsideTerminal = outsideTerminal;
            this.externalTerminal = externalTerminal;
        }

        @Override
        public void run() {
            external2Outside.remove(externalTerminal);
            outside2External.remove(outsideTerminal);
        }
    }

    /**
   *
   */
    private static final String TAG_EXTERNAL_TERMINAL = "external-terminal";

    private static final String TAG_OUTSIDE_TERMINAL = "outside-terminal";

    static final String TAG_GROUP = "group";

    protected static final String TAG_TEMPLATE = "template";

    private static final String TAG_AUTHORING_PROPS = "authoring-props";

    private static final String TAG_PROPERTY = "property";

    private static final String TAG_NAME = "name";

    private static final String TAG_VALUE = "value";

    /**
     * The type name of the GroupNode nodes
     */
    public static final String DEFAULT_GROUP_NODE_TYPE_PREFIX = "GroupNode_";

    private final Graph groupGraph;

    private final Map<Terminal, Terminal> external2Outside = new HashMap<Terminal, Terminal>();

    private final Map<Terminal, Terminal> outside2External = new HashMap<Terminal, Terminal>();

    private final Logger logger = Logger.getLogger(GroupNode.class);

    private String nodeTypePrefix = null;

    private boolean isTemplate = false;

    /**
     * The type name of the TerminalNodes
     */
    public static final String GROUP_TERMINAL_NODE_TYPE = "GroupTerminalNode";

    private List<SubGraphPropertyAlias> subGraphProperties = new Vector<SubGraphPropertyAlias>();

    private final Map<String, String> authoringProperties = new LinkedHashMap<String, String>();

    private boolean suppressAddSubgraphProperties;

    private WizardState wizardState;

    private Element wizardStateElement;

    /**
     * @param graph
     *            the graph the group node will belong to
     */
    public GroupNode(Graph graph) {
        super((NodeSpec) null, graph);
        setTitle(Side.TOP, getUniqueName());
        getNodeSpec().setDescription("Group Node");
        getNodeSpec().setColor(new Color(153, 153, 0));
        groupGraph = new Graph(getNodeSpec().getType(), this, getGraph().getEditorFrame(), getGraph().getWorkspace());
        groupGraph.setContainingGroupNode(this);
    }

    /**
     * Override to avoid redundant id that is already in the nodespec name
     *
     * @see com.bbn.vessel.author.models.VesselNode#getUniqueName()
     */
    @Override
    public String getUniqueName() {
        return getNodeSpec().getType();
    }

    /**
     * @param element
     *            xml representation of the node
     * @param graph
     *            graph the node will belong to
     * @throws IOException
     *             for parsing problems
     */
    public GroupNode(Element element, Graph graph) throws IOException {
        super(element, graph);
        if (element.getName().equals(TAG_TEMPLATE)) {
            isTemplate = true;
        }
        Element groupElement = element.getChild(TAG_GROUP);
        Element subGraphElement = groupElement.getChild(Graph.TAG_GRAPH);
        suppressAddSubgraphProperties = true;
        groupGraph = new Graph(subGraphElement, this, getGraph().getEditorFrame(), getGraph().getWorkspace());
        List<Element> connectionElements = XMLHelper.getChildren(groupElement, Connection.TAG_CONNECTION);
        for (Element connectionElement : connectionElements) {
            Element outsideTerminalElement = connectionElement.getChild(TAG_OUTSIDE_TERMINAL);
            Terminal outsideTerminal = Terminal.parseTerminalElement(groupGraph, outsideTerminalElement);
            Element externalTerminalElement = connectionElement.getChild(TAG_EXTERNAL_TERMINAL);
            Terminal externalTerminal = Terminal.parseTerminalElement(this, externalTerminalElement);
            mapOutsideAndExternalTerminals(outsideTerminal, externalTerminal);
        }
        Element authoringPropsElt = element.getChild(TAG_AUTHORING_PROPS);
        if (authoringPropsElt != null) {
            for (Element propElt : XMLHelper.getChildren(authoringPropsElt, TAG_PROPERTY)) {
                String name = propElt.getChildText(TAG_NAME);
                String value = propElt.getChildText(TAG_VALUE);
                authoringProperties.put(name, value);
            }
        }
        Map<String, GraphNode> nodeMap = new TreeMap<String, GraphNode>();
        Collection<GraphNode> nodes = groupGraph.getNodes();
        for (GraphNode node : nodes) {
            nodeMap.put(node.getUniqueName(), node);
        }
        List<Element> subgraphPropertyAliasElements = XMLHelper.getChildren(element, SubGraphPropertyAlias.TAG_SUBGRAPH_PROPERTIES_ALIAS);
        for (Element subgraphPropertyAliasElement : subgraphPropertyAliasElements) {
            SubGraphPropertyAlias subgraphPropertyAlias = new SubGraphPropertyAlias(subgraphPropertyAliasElement, nodeMap);
            subGraphProperties.add(subgraphPropertyAlias);
        }
        wizardStateElement = element.getChild(WizardState.TAG_WIZARD_STATE);
        suppressAddSubgraphProperties = false;
    }

    private void mapOutsideAndExternalTerminals(Terminal outsideTerminal, Terminal externalTerminal) {
        UndoableEdit edit = new SimpleEdit("Map Terminals", new mapOutsideAndExternalRunnable(outsideTerminal, externalTerminal), new unmapOutsideAndExternalRunnable(outsideTerminal, externalTerminal));
        edit.run();
        getGraph().getUndoManager().recordEdit(edit);
    }

    private void unmapOutsideAndExternalTerminals(final Terminal outsideTerminal, final Terminal externalTerminal) {
        UndoableEdit edit = new SimpleEdit("Map Terminals", new unmapOutsideAndExternalRunnable(outsideTerminal, externalTerminal), new mapOutsideAndExternalRunnable(outsideTerminal, externalTerminal));
        edit.run();
        getGraph().getUndoManager().recordEdit(edit);
    }

    /**
     * for duplicating groupnodes
     *
     * @param groupNode
     *            node to copy
     * @param graph
     *            graph taht will contain the new node
     * @param groupGraph
     *            graph that the new node contains
     * @param name
     *            name of the new node
     * @param aliases
     *            the subgraph property aliases for this node
     * @throws IOException
     *             if there is a mismatch between the terminals and the spec
     */
    public GroupNode(GroupNode groupNode, Graph graph, Graph groupGraph, String name, Vector<SubGraphPropertyAlias> aliases) throws IOException {
        super(groupNode, graph, name);
        nodeTypePrefix = name;
        this.groupGraph = groupGraph;
        this.subGraphProperties = aliases;
        groupGraph.setContainingGroupNode(this);
        List<Terminal> outsideTerminalsByName = new ArrayList<Terminal>();
        Iterator<GraphNode> modelIt = groupGraph.iterateNodes();
        while (modelIt.hasNext()) {
            GraphNode node = modelIt.next();
            if (node.getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE)) {
                final Terminal outsideTerm = node.getTerminal(0);
                outsideTerminalsByName.add(outsideTerm);
            }
        }
        for (int i = 0; i < getTerminalCount(); i++) {
            final Terminal extTerm = getTerminal(i);
            for (Terminal outsideTerm : outsideTerminalsByName) {
                if ((extTerm.getName().equals(outsideTerm.getName())) && (outsideTerm.getTerminalType().isOpposite(extTerm.getTerminalType()))) {
                    mapOutsideAndExternalTerminals(outsideTerm, extTerm);
                    break;
                }
            }
        }
    }

    /**
     * add a node from to group, updating links appropriately. this method is
     * the inverse of removeGraphNode
     *
     * @param n
     *            a node that lives in the parent graph
     * @return a pointer to the copy of the graph node that has been added to
     *         the group graph, in case you want it
     */
    public GraphNode addGraphNode(GraphNode n) {
        return addGraphNode(n, n.getView().getBounds());
    }

    /**
     * add a node from to group, updating links appropriately. this method is
     * the inverse of removeGraphNode
     *
     * @param graphNode
     *            a node that lives in the parent graph
     * @param boundingBox
     *            the bounding box of all the nodes that are selected along with
     *            this one, or null if this is the only node selected
     * @return a pointer to the copy of the graph node that has been added to
     *         the group graph, in case you want it
     */
    public GraphNode addGraphNode(GraphNode graphNode, Rectangle boundingBox) {
        GraphNode duplicate = graphNode.duplicate(groupGraph);
        groupGraph.addGraphElement(duplicate);
        Point reasonableTargetPoint = getReasonableTargetPoint();
        Point sourceReferencePoint = graphNode.getView().getReferencePoint();
        int xOffset = 0;
        int yOffset = 0;
        if (boundingBox != null) {
            xOffset = sourceReferencePoint.x - boundingBox.x;
            yOffset = sourceReferencePoint.y - boundingBox.y;
        }
        if (graphNode.getView() != null) {
            duplicate.getView().setReferencePoint(new Point(reasonableTargetPoint.x + xOffset, reasonableTargetPoint.y + yOffset));
        }
        int terminalCount = graphNode.getTerminalCount();
        List<Terminal> terminals = new ArrayList<Terminal>(terminalCount);
        for (int i = 0; i < terminalCount; i++) {
            Terminal terminal = graphNode.getTerminal(i);
            terminals.add(terminal);
        }
        for (int i = 0; i < terminalCount; i++) {
            Terminal terminal = terminals.get(i);
            String signalName = terminal.getSignalName();
            Terminal duplicateTerminal = duplicate.getTerminal(i);
            Collection<Connection> connections = new ArrayList<Connection>(terminal.getConnections());
            if (connections.isEmpty()) {
            } else {
                Terminal newExternalTerminal = null;
                Terminal newOutsideTerminal = null;
                for (Connection connection : connections) {
                    Terminal otherTerminal = connection.getOtherTerminal(terminal);
                    GraphNode otherGraphNode = otherTerminal.getGraphNode();
                    if (otherGraphNode == this) {
                        Set<Terminal> insideTerminals = getInsideTerminals(otherTerminal);
                        getGraph().removeGraphElement(connection);
                        if (otherTerminal.getConnections().isEmpty()) {
                            removeExternalTerminal(otherTerminal);
                        }
                        for (Terminal insideTerminal : insideTerminals) {
                            Connection newConnection = new Connection(insideTerminal, duplicateTerminal, connection.getType(), groupGraph);
                            groupGraph.addGraphElement(newConnection);
                        }
                    } else {
                        getGraph().removeGraphElement(connection);
                        if (newExternalTerminal == null) {
                            String fullName = duplicate.getUniqueName() + "." + terminal.getName();
                            TerminalSpec terminalSpec = new TerminalSpec(fullName, terminal.getTerminalType(), terminal.isMultiple(), terminal.getConstraint());
                            newExternalTerminal = addGroupNodeTerminal(terminalSpec, signalName);
                            newOutsideTerminal = addOutsideTerminal(newExternalTerminal, terminal.getView().getReferencePoint(), signalName);
                        }
                        addInsideTerminal(newExternalTerminal, duplicateTerminal, newOutsideTerminal);
                        Connection newConnection = new Connection(otherTerminal, newExternalTerminal, connection.getType(), getGraph());
                        getGraph().addGraphElement(newConnection);
                    }
                }
            }
        }
        ((BasicBoxView) view).layoutTerminals();
        if (getGraph().containsGraphElement(graphNode)) {
            getGraph().removeGraphElement(graphNode);
        }
        return duplicate;
    }

    private Point getReasonableTargetPoint() {
        GraphViews graphViews = groupGraph.getGraphViews();
        if (graphViews != null) {
            return graphViews.getViewPortCorner();
        } else {
            return new Point(0, 0);
        }
    }

    /**
     * remove a node from the group, adding it to the parent graph and updating
     * links appropriately. this method is the inverse of addGraphNode
     *
     * @param graphNode
     *            a node that lives in the group graph
     * @return a pointer to the copy of the graph node that has been added to
     *         the group graph, in case you want it
     */
    public GraphNode removeGraphNode(GraphNode graphNode) {
        Graph parentGraph = getGraph();
        GraphNode duplicate = graphNode.duplicate(parentGraph);
        parentGraph.addGraphElement(duplicate);
        if (graphNode.getView() != null) {
            duplicate.getView().setReferencePoint(graphNode.getView().getReferencePoint());
        }
        int terminalCount = graphNode.getTerminalCount();
        List<Terminal> terminals = new ArrayList<Terminal>(terminalCount);
        for (int i = 0; i < terminalCount; i++) {
            Terminal terminal = graphNode.getTerminal(i);
            terminals.add(terminal);
        }
        for (int i = 0; i < terminalCount; i++) {
            Terminal originalTerminal = terminals.get(i);
            Terminal duplicateTerminal = duplicate.getTerminal(i);
            Collection<Connection> originalConnections = new ArrayList<Connection>(originalTerminal.getConnections());
            for (Connection connection : originalConnections) {
                Terminal originalOtherTerminal = connection.getOtherTerminal(originalTerminal);
                if (originalOtherTerminal.getGraphNode().getNodeSpec().getType().equals(GroupNode.GROUP_TERMINAL_NODE_TYPE)) {
                    Terminal extTerminal = outside2External.get(originalOtherTerminal);
                    for (Connection extConnection : extTerminal.getConnections()) {
                        Terminal extOtherTerminal = extConnection.getOtherTerminal(extTerminal);
                        parentGraph.addGraphElement(new Connection(duplicateTerminal, extOtherTerminal, connection.getType(), parentGraph));
                    }
                    if (originalOtherTerminal.numConnections() == 1) {
                        removeExternalTerminal(extTerminal);
                    }
                } else {
                    Terminal newExtTerminal = addExternalTerminalForInsideTerminal(originalOtherTerminal);
                    parentGraph.addGraphElement(new Connection(duplicateTerminal, newExtTerminal, connection.getType(), parentGraph));
                }
            }
        }
        groupGraph.removeGraphElement(graphNode);
        ((BasicBoxView) view).layoutTerminals();
        if (getGraph().containsGraphElement(graphNode)) {
            getGraph().removeGraphElement(graphNode);
        }
        return duplicate;
    }

    private Terminal addGroupNodeTerminal(final TerminalSpec terminalSpec, String signalName) {
        if (findTerminal(terminalSpec.getTerminalType(), terminalSpec.getName()) != null) {
            logger.error("duplicate terminal!", new Exception());
            return null;
        }
        Terminal newTerminal = Terminal.constructTerminal(this, terminalSpec, getGraph());
        if (!newTerminal.isInputType()) {
            newTerminal.setSignalName(signalName);
        }
        UndoableEdit edit = new SimpleEdit("Add GroupNode Terminal", RUN_addGroupNodeTerminal(terminalSpec, newTerminal), RUN_removeGroupNodeTerminal(terminalSpec, newTerminal));
        getGraph().getUndoManager().recordEdit(edit);
        edit.run();
        return newTerminal;
    }

    private void addGroupNodeTerminal(TerminalSpec terminalSpec, Terminal terminal) {
        getNodeSpec().addTerminalSpec(terminalSpec);
        addTerminal(terminal);
        if (getView() != null) getView().setReferencePoint(getView().getReferencePoint());
    }

    private Runnable RUN_addGroupNodeTerminal(final TerminalSpec terminalSpec, final Terminal terminal) {
        return new Runnable() {

            @Override
            public void run() {
                addGroupNodeTerminal(terminalSpec, terminal);
            }
        };
    }

    private Runnable RUN_removeGroupNodeTerminal(final TerminalSpec terminalSpec, final Terminal terminal) {
        return new Runnable() {

            @Override
            public void run() {
                getNodeSpec().removeTerminalSpec(terminalSpec);
                removeTerminal(terminal);
                if (getView() != null) getView().setReferencePoint(getView().getReferencePoint());
            }
        };
    }

    /**
     * remove a terminal from the group node
     *
     * @param terminal
     *            an external terminal
     */
    public void removeExternalTerminal(final Terminal terminal) {
        for (Connection conn : new ArrayList<Connection>(terminal.getConnections())) {
            getGraph().removeGraphElement(conn);
        }
        final TerminalSpec terminalSpec = terminal.getTerminalSpec();
        final Terminal outsideTerminal = external2Outside.get(terminal);
        unmapOutsideAndExternalTerminals(outsideTerminal, terminal);
        final GraphNode terminalNode = outsideTerminal.getGraphNode();
        UndoableEdit edit = new SimpleEdit("Remove GroupNode Terminal", RUN_removeGroupNodeTerminal(terminalSpec, terminal), RUN_addGroupNodeTerminal(terminalSpec, terminal));
        getGraph().getUndoManager().recordEdit(edit);
        edit.run();
        getGroupGraph().removeGraphElement(terminalNode);
    }

    private Terminal addOutsideTerminal(Terminal externalTerminal, Point insidePoint, String signalName) {
        TerminalType oppositeType;
        Side oppositeConstraint;
        if (externalTerminal.isInputType()) {
            oppositeType = externalTerminal.getSignalType().getOutputTerminalType();
            oppositeConstraint = Side.RIGHT;
            if (insidePoint != null) insidePoint = new Point(insidePoint.x - 150, insidePoint.y);
        } else {
            oppositeType = externalTerminal.getSignalType().getInputTerminalType();
            oppositeConstraint = Side.LEFT;
            if (insidePoint != null) insidePoint = new Point(insidePoint.x + 150, insidePoint.y);
        }
        TerminalSpec oppositeSpec = new TerminalSpec(externalTerminal.getName(), oppositeType, externalTerminal.isMultiple(), oppositeConstraint);
        NodeSpec nodeSpec = new NodeSpec(GroupNode.GROUP_TERMINAL_NODE_TYPE, getGraph().getNodeSpecTable().getCategory(NodeSpecTable.UNCATEGORIZED), Collections.singletonList(oppositeSpec));
        nodeSpec.setColor(Color.BLUE);
        GraphNode box = new VesselNode(nodeSpec, groupGraph);
        Terminal outsideTerminal = box.getTerminal(0);
        if (!outsideTerminal.isInputType()) {
            outsideTerminal.setSignalName(signalName);
        }
        groupGraph.addGraphElement(box);
        if (insidePoint != null) box.getView().setReferencePoint(insidePoint);
        mapOutsideAndExternalTerminals(outsideTerminal, externalTerminal);
        return box.getTerminal(0);
    }

    /**
     * @param externalTerminal
     *            the terminal of this GroupNode
     * @param insideTerminal
     *            the terminal of the GraphNode inside this GroupNode
     * @param outsideTerminal
     *            the terminal of the terminal node
     */
    private void addInsideTerminal(Terminal externalTerminal, Terminal insideTerminal, Terminal outsideTerminal) {
        Connection outsideTerminalConnection = new Connection(outsideTerminal, insideTerminal, Connection.Type.SPLINE, groupGraph);
        groupGraph.addGraphElement(outsideTerminalConnection);
    }

    /**
     * gets all the inside terminals for an externalt terminal
     *
     * @param externalTerminal
     *            an external terminal belonging to this group node
     * @return all the inside terminals connected to the outside terminal that
     *         maps to the external terminal
     */
    public Set<Terminal> getInsideTerminals(Terminal externalTerminal) {
        Terminal outsideTerminal = external2Outside.get(externalTerminal);
        Set<Terminal> insideTerminals = new HashSet<Terminal>();
        for (Connection connection : outsideTerminal.getConnections()) {
            Terminal insideTerminal = connection.getOtherTerminal(outsideTerminal);
            insideTerminals.add(insideTerminal);
        }
        return insideTerminals;
    }

    /**
     * @return The sub-Graph of this group
     */
    public Graph getGroupGraph() {
        return groupGraph;
    }

    /**
     * Override GraphNode toXML to add in a node-spec subnode and the subgraph property aliases
     *
     * @see com.bbn.vessel.author.models.VesselNode#toXML()
     */
    @Override
    public Element toXML() {
        Element element = super.toXML();
        Element groupElement = new Element(TAG_GROUP);
        element.addContent(groupElement);
        Element subGraphElement = groupGraph.toXML();
        groupElement.addContent(subGraphElement);
        List<Terminal> externalTerminals = new ArrayList<Terminal>(external2Outside.keySet());
        Collections.sort(externalTerminals, new Comparator<Terminal>() {

            @Override
            public int compare(Terminal e1, Terminal e2) {
                int ret = e1.compareTo(e2);
                if (ret != 0) {
                    return ret;
                }
                Terminal o1 = external2Outside.get(e1);
                Terminal o2 = external2Outside.get(e2);
                return o1.compareTo(o2);
            }
        });
        for (Terminal externalTerminal : externalTerminals) {
            Terminal outsideTerminal = external2Outside.get(externalTerminal);
            Element connectionElement = new Element(Connection.TAG_CONNECTION);
            groupElement.addContent(connectionElement);
            externalTerminal.addTerminalId(connectionElement, TAG_EXTERNAL_TERMINAL);
            outsideTerminal.addTerminalId(connectionElement, TAG_OUTSIDE_TERMINAL);
        }
        Element authoringPropsElement = new Element(TAG_AUTHORING_PROPS);
        for (String key : authoringProperties.keySet()) {
            Element propElt = new Element(TAG_PROPERTY);
            XMLHelper.addStringElement(propElt, TAG_NAME, key);
            XMLHelper.addStringElement(propElt, TAG_VALUE, authoringProperties.get(key));
            authoringPropsElement.addContent(propElt);
        }
        element.addContent(authoringPropsElement);
        for (SubGraphPropertyAlias alias : subGraphProperties) {
            Element subgraphPropertiesElement = alias.toXML();
            element.addContent(subgraphPropertiesElement);
        }
        if (getWizardState() != null) {
            Element wizardStateElement = getWizardState().toXML(this);
            element.addContent(wizardStateElement);
        }
        return element;
    }

    @Override
    public GraphNode duplicate(Graph graph) {
        return duplicate(graph, null);
    }

    /**
     *
     * @param graph graph the duplicate will reside in
     * @param duplicateMap an optional map, into which every graph element which is duplicated by
     * the recursion will be mapped to its duplicate
     * @return the duplicate of this node, in case you want it.
     */
    public GraphNode duplicate(Graph graph, Map<GraphElement, GraphElement> duplicateMap) {
        GroupNode toReturn = null;
        try {
            Vector<SubGraphPropertyAlias> newSubGraphProperties = new Vector<SubGraphPropertyAlias>();
            final Graph newGraph = groupGraph.duplicate(Collections.unmodifiableCollection(subGraphProperties), newSubGraphProperties, duplicateMap);
            toReturn = new GroupNode(this, graph, newGraph, getNodeTypePrefix(), newSubGraphProperties);
            if (duplicateMap != null) {
                duplicateMap.put(this, toReturn);
                for (Terminal term : getTerminals()) duplicateMap.put(term, toReturn.findTerminal(term.getTerminalType(), term.getName()));
            }
            newGraph.setContainingGroupNode(toReturn);
        } catch (IOException e) {
            logger.error(e, e);
        }
        for (String key : authoringProperties.keySet()) {
            toReturn.setAuthoringProperty(key, authoringProperties.get(key));
        }
        return toReturn;
    }

    private String getNodeTypePrefix() {
        return nodeTypePrefix != null ? nodeTypePrefix : DEFAULT_GROUP_NODE_TYPE_PREFIX;
    }

    /**
     * maps an external terminal (one on the outside of the group node that
     * interacts with the larger graph) to an outside terminal (the terminals in
     * the subgraph which correspond to the external terminals)
     *
     * @param externalTerminal
     *            an external terminal of this group node
     * @return the corresponding outside terminal
     */
    public Terminal getOutsideForExternal(Terminal externalTerminal) {
        return external2Outside.get(externalTerminal);
    }

    /**
     * maps an outside terminal (one on the external of the group node that
     * interacts with the larger graph) to an external terminal (the terminals
     * in the subgraph which correspond to the outside terminals)
     *
     * @param outsideTerminal
     *            an outside terminal of this group node
     * @return the corresponding external terminal
     */
    public Terminal getExternalForOutside(Terminal outsideTerminal) {
        return outside2External.get(outsideTerminal);
    }

    public void setTypePrefix(String nodeTypePrefix) {
        this.nodeTypePrefix = nodeTypePrefix;
    }

    @Override
    protected String getRootTag() {
        if (isTemplate) {
            return TAG_TEMPLATE;
        } else {
            return super.getRootTag();
        }
    }

    public void setIsTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public int getSubgraphPropertyCount() {
        return subGraphProperties.size();
    }

    public SubGraphPropertyAlias getSubgraphPropertyAlias(int index) {
        return subGraphProperties.get(index);
    }

    public void removeSubgraphPropertiesForNode(GraphNode node) {
        for (SubGraphPropertyAlias alias : new ArrayList<SubGraphPropertyAlias>(subGraphProperties)) {
            if (alias.getSubGraphNode() == node) {
                subGraphProperties.remove(alias);
            }
        }
    }

    public void addSubgraphPropertiesForNode(GraphNode node) {
        if (suppressAddSubgraphProperties) {
            return;
        }
        for (List<String> path : ArgSpec.getArgSpecLeafPaths(node.getArgSpecs().values())) {
            subGraphProperties.add(new SubGraphPropertyAlias(node, path, true));
        }
    }

    /**
     * adds a terminal to the outside of the group node, corresponding to a
     * terminal of a node in the group
     *
     * @param terminal
     *            the terminal of the node in the group
     * @return the external terminal that has been added, in case you want it
     */
    public Terminal addExternalTerminalForInsideTerminal(Terminal terminal) {
        return addExternalTerminalForInsideTerminal(terminal, getUniqueExternalNameFor(terminal));
    }

    /**
     * adds a terminal with a specified name to the outside of the group node, corresponding to a
     * terminal of a node in the group
     * @param terminal
     *            the terminal of the node in the group
     * @param fullName the full name of the terminal
     * @return the external terminal that has been added, in case you want it
     */
    public Terminal addExternalTerminalForInsideTerminal(Terminal terminal, String fullName) {
        final TerminalSpec terminalSpec = new TerminalSpec(fullName, terminal.getTerminalType(), terminal.isMultiple(), terminal.getConstraint());
        final String signalName = terminal.getSignalName();
        Terminal[] outsideAndExternalTerminals = addOutsideAndExternalTerminals(terminalSpec, signalName, terminal.getView() == null ? null : terminal.getView().getReferencePoint());
        addInsideTerminal(outsideAndExternalTerminals[1], terminal, outsideAndExternalTerminals[0]);
        return outsideAndExternalTerminals[1];
    }

    /**
     * @param terminal
     * @return
     */
    public String getUniqueExternalNameFor(Terminal terminal) {
        String nameBeforeSuffix = terminal.getGraphNode().getUniqueName() + "." + terminal.getName();
        String fullName = nameBeforeSuffix;
        int counter = 1;
        while (findTerminal(terminal.getTerminalType(), fullName) != null) fullName = nameBeforeSuffix + counter++;
        return fullName;
    }

    /**
     * add an outside terminal and corresponding external terminal
     *
     * @param externalSpec
     *            the terminal spec of the external terminal to add
     * @param signalName
     *            the signal name for both terminals
     * @param referencePoint
     *            location where you want the outside terminal
     * @return outside, external, in case you want them
     */
    public Terminal[] addOutsideAndExternalTerminals(TerminalSpec externalSpec, String signalName, Point referencePoint) {
        Terminal externalTerminal = addGroupNodeTerminal(externalSpec, signalName);
        Terminal outsideTerminal = addOutsideTerminal(externalTerminal, referencePoint, signalName);
        return new Terminal[] { outsideTerminal, externalTerminal };
    }

    /**
     * if node is an outside terminal node, remove it's terminals, otherwise
     * find any outside terminal nodes that are left unconnected by the removal
     * of this node and remove them and their terminals
     */
    public void removeTerminalsForNode(GraphNode node) {
        if (node.getNodeSpec().getType().equals(GROUP_TERMINAL_NODE_TYPE)) {
            if (node.getTerminalCount() > 0) {
                final Terminal externalTerminal = outside2External.get(node.getTerminal(0));
                if (externalTerminal != null) {
                    removeExternalTerminal(externalTerminal);
                }
            }
        } else {
            for (Terminal insideTerminal : new ArrayList<Terminal>(node.getTerminals())) {
                for (Connection conn : new ArrayList<Connection>(insideTerminal.getConnections())) {
                    Terminal outsideTerminal = conn.getOtherTerminal(insideTerminal);
                    if ((outsideTerminal.numConnections() == 1) && (outsideTerminal.getGraphNode().getNodeSpec().getType().equals(GROUP_TERMINAL_NODE_TYPE))) {
                        removeExternalTerminal(outside2External.get(outsideTerminal));
                    }
                }
            }
        }
    }

    public void renameExternalTerminal(Terminal externalTerminal, String newTerminalName) {
        if (findTerminalSpec(externalTerminal.getTerminalType(), newTerminalName) != null) {
            throw new IllegalArgumentException("There is already a " + externalTerminal.getTerminalType().getDisplayName() + " terminal called " + newTerminalName);
        }
        TerminalSpec terminalSpec = findTerminalSpec(externalTerminal.getTerminalType(), externalTerminal.getName());
        terminalSpec.setName(newTerminalName);
        final Terminal outsideTerminal = this.getOutsideForExternal(externalTerminal);
        GraphNode outsideTerminalNode = outsideTerminal.getGraphNode();
        outsideTerminalNode.findTerminalSpec(outsideTerminal.getTerminalType(), outsideTerminal.getName()).setName(newTerminalName);
        if (outsideTerminalNode.getView() != null) outsideTerminalNode.getView().updateLayout();
    }

    /**
     * Finds an external terminal of named externalName if one exists, otherwise
     * exposes term under that name
     *
     * @param term
     *            an inside terminal
     * @param externalName
     *            the name you want it exposed to the parent graph as
     * @return
     */
    public Terminal findOrExposeTerminal(Terminal term, String externalName) {
        Terminal externalTerminal = findTerminal(term.getTerminalType(), externalName);
        if (externalTerminal == null) {
            externalTerminal = addExternalTerminalForInsideTerminal(term);
            findTerminalSpec(externalTerminal.getTerminalType(), externalTerminal.getName()).setName(externalName);
            Terminal outsideTerminal = getOutsideForExternal(externalTerminal);
            outsideTerminal.getGraphNode().findTerminalSpec(outsideTerminal.getTerminalType(), outsideTerminal.getName()).setName(externalName);
        }
        return externalTerminal;
    }

    /**
     * show open the editor for the group graph and bring it to front.
     */
    public void viewGroupGraph() {
        ((GroupingGraphEditor) getGraph().getGraphEditor()).viewGroupGraph(this);
    }

    /**
     * get the authoring property for a specific name
     * @param propName name of the desired authoring property
     * @return the authoring property with the desired name
     */
    public String getAuthoringProperty(String propName) {
        return authoringProperties.get(propName);
    }

    /**
     * set the authoring property for a specific name
     * @param propName name of the authoring property
     * @param propValue the authoring property with the provided name
     */
    public void setAuthoringProperty(String propName, String propValue) {
        authoringProperties.put(propName, propValue);
    }

    /**
     * remove the authoring property for a specific name
     * @param propName name of the authoring property
     */
    public void removeAuthoringProperty(String propName) {
        authoringProperties.remove(propName);
    }

    /**
     * @return the WizardState if any
     */
    public WizardState getWizardState() {
        if (wizardStateElement != null) {
            setWizardState(new WizardState(wizardStateElement, this));
        }
        return wizardState;
    }

    /**
     * @param wizardState the WizardState to set
     */
    public void setWizardState(WizardState wizardState) {
        this.wizardState = wizardState;
        wizardStateElement = null;
    }

    /**
     * @return true if this is a read-only group
     */
    public boolean isReadOnly() {
        return isInstruction() && !isUserEdited();
    }

    /**
     * @return true if the GroupNode implements instruction
     */
    private boolean isInstruction() {
        return getAuthoringProperty(IMConstants.AUTHORING_PROP_MECHANIC_NAME) != null;
    }

    /**
     * @return true if this isInstruction that has been hand-edited by the user
     */
    public boolean isUserEdited() {
        final String editedByUserString = getAuthoringProperty(IMConstants.AUTHORING_PROP_EDITED_BY_USER);
        return editedByUserString != null && editedByUserString.equals("true");
    }

    /**
     * Mark as edited
     */
    public void setUserEdited() {
        setAuthoringProperty(IMConstants.AUTHORING_PROP_EDITED_BY_USER, "true");
    }

    /**
     * loop through and remove any unconnected external terminals of specified type
     * @param termType type of external terminal you want to remove, or null for all types
     */
    public void removeUnconnectedExternalTerminals(TerminalType termType) {
        for (Terminal externalTerm : new ArrayList<Terminal>(getTerminals())) {
            if (externalTerm.getConnections().size() == 0) if (termType == null || termType.equals(externalTerm.getTerminalType())) removeExternalTerminal(externalTerm);
        }
    }

    /**
     * @return all the authoringProperties
     *
     */
    public Map<String, String> getAuthoringProperties() {
        return Collections.unmodifiableMap(authoringProperties);
    }
}
