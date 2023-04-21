package org.mantikhor.llapi.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mantikhor.llapi.BaseProperty;
import org.mantikhor.llapi.BaseValNode;
import org.mantikhor.llapi.Burst;
import org.mantikhor.llapi.Container;
import org.mantikhor.llapi.Graph;
import org.mantikhor.llapi.Khoron;
import org.mantikhor.llapi.KhoronItem;
import org.mantikhor.llapi.Namenode;
import org.mantikhor.llapi.Path;
import org.mantikhor.llapi.PropertyDefinition;
import org.mantikhor.llapi.PropertyValNode;
import org.mantikhor.llapi.ResourceNode;
import org.mantikhor.llapi.Triptych;

/**
 * This particular IMMUTABLE implementation of Graph will handle Graphs that are Trees.
 *
 * Definition: A Tree is a graph such that given a terminating property there is
 * one and only one path from the terminating property back to the root node.
 *
 * A Tree - all terminating nodes (D through L) have a unique way back to the nameNode.
 *
 *                                  nameNode
 *                                /    |     \              Paths
 *       I                       A     B      C             [nn, A, D], [nn, A, E], [nn, A, F]
 *                              /|\   /|\    /|\            [nn, B, G], [nn, B, H], [nn, B, I]
 *                             D E F G H I  J K L           [nn, C, J], [nn, C, K], [nn, C, L]  # terminating nodes = # paths
 *
 * Not a Tree - the terminating node I can get to the nameNode in two ways:
 * {nameNode, C, I} OR {nameNode, B, I}
 *
 *                                  nameNode                Paths
 *       II                       /    |   \                [nn, A, D], [nn, A, E], [nn, A, F]
 *                               A     B   _C               [nn, B, G], [nn, B, H], [nn, B, I]
 *                              /|\   /|\ / | \ \           [nn, C, I], [nn, C, J], [nn, C, K], [nn, C, L] # terminating nodes < # paths
 *                             D E F G H I  J K  L
 *
 * Another tree -
 *
 *
 *                            ______nameNode______
 *                          / | | | | | | | | | | \
 *                         A  B C | | | | | | | | |          Paths
 *                         |  | | D E F | | | | | |
 *                         |__|_|_^ ^ ^ | | | | | |          same as I, but nn for III != nn for I
 *        III              |__|_|___| | G H I | | |          This is a tree also
 *                         |__|_|_____| ^ ^ ^ J K L          # terminating nodes = # paths
 *                            | |_______| | | ^ ^ ^
 *                            |_|_________| | | | |
 *                            |_|___________| | | |
 *                              |_____________| | |
 *                              |_______________| |
 *                              |_________________|
 *
 * Definition: A Terminating Property is: 1) A BaseProperty that is not a Burst.
 * Or ... 2) A BaseProperty that is a Burst whose getProperties() call returns a
 * collection of size 0.
 *
 * The private member variable 'graphInformation' of type GraphInformation is
 * initialized in the constructor of TreeImpl: public TreeImpl(ResourceNode
 * nameNode) { ... }
 *
 * This useful member variable (graphInformation) is used in all the other
 * methods in one way or another.
 *
 * The class GraphInformation is a utility class that helps analyze Graph(s).
 *
 * @author Dave Winslow
 */
public class TreeImpl implements Graph {

    /**
     * The Namenode of this GraphImpl
     */
    private Namenode namenode;

    /**
     * Basic graph information about this TreeImpl - this member variable is
     * initialized in TreeImpl's constructors.
     */
    private GraphInformation graphInformation;

    public GraphInformation getGraphInformation() {
        return graphInformation;
    }

    /**
     * Construct a TreeImpl via a ResourceNode
     *
     * @param namenode
     */
    private TreeImpl(ResourceNode namenode) {
        if (namenode == null) {
            throw new IllegalArgumentException("namenode cannot be null");
        }
        this.namenode = new NamenodeImpl(this, namenode.getResourceURI(), namenode.getProperties(), namenode.getReefNode().getProperties(), namenode.getKhorID());
    }

    public static TreeImpl valueByResourceNode(ResourceNode namenode) {
        TreeImpl retVal = new TreeImpl(namenode);
        GraphInformation gi = new GraphInformation(retVal);
        retVal.graphInformation = gi;
        if (retVal.graphInformation.getTerminatingBaseProperties().size() != retVal.getPathset().size()) {
            throw new IllegalArgumentException("Analysis shows that namenode will not produce a TreeImpl");
        }
        return retVal;
    }

    /**
     * NOT YET IMPLEMENTED -- needs discussion.
     *
     * @param namenode
     * @param properties
     */
    public TreeImpl(ResourceNode namenode, List<BaseProperty> properties) {
        throw new UnsupportedOperationException("TreeImpl(ResourceNode, List<BaseProperty>) not implemented yet. Use TreeImpl(ResourceNode).");
    }

    public Namenode getNamenode() {
        return this.namenode;
    }

    public Khoron getKhoron() {
        return KhoronImpl.getInstance();
    }

    public KhoronItem getKhoronItem(int khorID) {
        if (khorID == 0) {
            throw new IllegalArgumentException("khorID cannot be zero");
        }
        if (this.namenode.getKhorID() == khorID) {
            return this.namenode;
        }
        Map<Integer, KhoronItem> basePropertiesThatAreKhoronItems = this.graphInformation.getPropertiesThatAreKhoronItems();
        KhoronItem retValue = basePropertiesThatAreKhoronItems.get(Integer.valueOf(khorID));
        if (retValue != null) {
            return retValue;
        }
        Map<Integer, KhoronItem> baseValNodesThatAreKhoronItems = this.graphInformation.getValNodesThatAreKhoronItems();
        retValue = baseValNodesThatAreKhoronItems.get(Integer.valueOf(khorID));
        if (retValue != null) {
            return retValue;
        }
        return null;
    }

    public boolean containsKhoronItem(int khorID) {
        if (khorID == 0) {
            throw new IllegalArgumentException("khorID cannot be zero");
        }
        if (this.namenode.getKhorID() == khorID) {
            return true;
        }
        Integer khorIDAsInteger = Integer.valueOf(khorID);
        return this.graphInformation.getPropertiesThatAreKhoronItems().keySet().contains(khorIDAsInteger) || this.graphInformation.getValNodesThatAreKhoronItems().keySet().contains(khorIDAsInteger);
    }

    public int[] getAllPropertyKhorIDs() {
        Map<Integer, KhoronItem> basePropertiesThatAreKhoronItems = this.graphInformation.getPropertiesThatAreKhoronItems();
        int sizeOfBasePropertiesThatAreKhoronItems = basePropertiesThatAreKhoronItems.values().size();
        int[] retVal = new int[sizeOfBasePropertiesThatAreKhoronItems];
        int ndx = 0;
        for (KhoronItem khoronItem : basePropertiesThatAreKhoronItems.values()) {
            retVal[ndx++] = khoronItem.getKhorID();
        }
        Arrays.sort(retVal);
        return retVal;
    }

    public int[] getAllPropertyValKhorIDs() {
        Map<Integer, KhoronItem> baseValNodesThatAreKhoronItems = this.graphInformation.getValNodesThatAreKhoronItems();
        int[] retVal = new int[baseValNodesThatAreKhoronItems.values().size()];
        int ndx = 0;
        for (KhoronItem khoronItem : baseValNodesThatAreKhoronItems.values()) {
            retVal[ndx++] = khoronItem.getKhorID();
        }
        Arrays.sort(retVal);
        return retVal;
    }

    public List<BaseProperty> getParents(BaseProperty someProperty) {
        List<BaseProperty> retValList = new ArrayList<BaseProperty>();
        for (BaseProperty baseProperty : this.graphInformation.getBaseProperties()) {
            BaseValNode baseValNode = baseProperty.getPropertyValueNode();
            if (baseValNode instanceof Burst) {
                Burst burst = (Burst) baseValNode;
                if (burst.containsProperty(someProperty)) {
                    retValList.add(baseProperty);
                }
            }
        }
        return retValList;
    }

    @SuppressWarnings("unchecked")
    public List<BaseProperty> getAllProperties() {
        return new ArrayList<BaseProperty>(this.graphInformation.getBaseProperties());
    }

    @SuppressWarnings("unchecked")
    public List<BaseProperty> getAllProperties(PropertyDefinition propertyDef) throws IllegalArgumentException {
        List<BaseProperty> retVal = new ArrayList<BaseProperty>();
        for (BaseProperty baseProperty : this.graphInformation.getBaseProperties()) {
            if (baseProperty.getPropertyDefinition().equals(propertyDef)) {
                retVal.add(baseProperty);
            }
        }
        return retVal;
    }

    public List<BaseProperty> getAllPropertiesWithValue(PropertyValNode valNode) throws IllegalArgumentException {
        List<BaseProperty> retVal = new ArrayList<BaseProperty>();
        for (BaseProperty baseProperty : this.graphInformation.getBaseProperties()) {
            if (baseProperty.getPropertyValueNode().equals(valNode)) {
                retVal.add(baseProperty);
            }
        }
        return retVal;
    }

    public List<PropertyDefinition> getAllPropertyDefinitions() {
        return new ArrayList<PropertyDefinition>(this.graphInformation.getPropertyDefinitions());
    }

    public List<BaseValNode> getAllPropertyValues() {
        return new ArrayList<BaseValNode>(this.graphInformation.getBaseValNodes());
    }

    public Set<Path> getPathset() {
        List<BaseProperty> terminatingProperties = this.graphInformation.getTerminatingBaseProperties();
        int numberOfPaths = terminatingProperties.size();
        Set<Path> returnValue = new HashSet<Path>(numberOfPaths);
        for (int ndx = 0; ndx < numberOfPaths; ndx++) {
            returnValue.add(PathImpl.newInstance((ResourceNode) this.namenode, this.calculatePath(this, terminatingProperties.get(ndx))));
        }
        return returnValue;
    }

    /**
     *
     * @param treeImpl
     * @return
     */
    private Set<Path> getPathset(TreeImpl treeImpl) {
        List<BaseProperty> terminatingProperties = treeImpl.getGraphInformation().getTerminatingBaseProperties();
        int numberOfPaths = terminatingProperties.size();
        Set<Path> returnValue = new HashSet<Path>(numberOfPaths);
        for (int ndx = 0; ndx < numberOfPaths; ndx++) {
            returnValue.add(PathImpl.newInstance((ResourceNode) treeImpl.getNamenode(), treeImpl.calculatePath(treeImpl, terminatingProperties.get(ndx))));
        }
        return returnValue;
    }

    public Set<Path> getPathset(BaseProperty property) throws IllegalArgumentException {
        if (property == null) {
            throw new IllegalArgumentException("property cannot be null.");
        }
        if (!this.graphInformation.getValNodesThatAreResourceNodes().values().contains(property.getPropertyValueNode())) {
            throw new IllegalArgumentException("property's value node is not a ResourceNode in this Graph instance.");
        }
        TreeImpl treeImpl = TreeImpl.valueByResourceNode((ResourceNode) property.getPropertyValueNode());
        return treeImpl.getPathset(treeImpl);
    }

    public Set<Path> getSubgraphPathset(ResourceNode newStartNode) throws IllegalArgumentException {
        if (newStartNode == null) {
            throw new IllegalArgumentException("newStartNode cannot be null.");
        }
        if (!this.graphInformation.getValNodesThatAreResourceNodes().values().contains(newStartNode)) {
            throw new IllegalArgumentException("cannot find newStartNode in this TreeImpl");
        }
        Graph g = this.getSubgraph(newStartNode);
        return g.getPathset();
    }

    public Graph getSubgraph(ResourceNode newStartNode) throws IllegalArgumentException {
        if (newStartNode == null) {
            throw new IllegalArgumentException("newStartNode cannot be null.");
        }
        if (!this.graphInformation.getValNodesThatAreResourceNodes().values().contains(newStartNode)) {
            throw new IllegalArgumentException("newStartNode is not a ResourceNode in this Graph instance.");
        }
        return TreeImpl.valueByResourceNode(newStartNode);
    }

    public boolean couldContain(Object other) throws IllegalArgumentException {
        return false;
    }

    public boolean contains(Object other) {
        return false;
    }

    public Set<Container> subcontains(Object other) {
        return null;
    }

    public boolean equals(Container other) {
        return false;
    }

    public Triptych diff(Container other) throws IllegalArgumentException {
        return null;
    }

    public Container immutableForm() {
        return this;
    }

    public boolean isEmpty() {
        return this.namenode.isEmpty();
    }

    public boolean hasSameContents(Container other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Graph)) {
            return false;
        }
        return this.isContentContainedBy(other) && other.isContentContainedBy(this);
    }

    public boolean isContentContainedBy(Container other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Graph)) {
            return false;
        }
        Graph otherAsGraph = (Graph) other;
        Namenode othersNameNode = otherAsGraph.getNamenode();
        if (!this.namenode.isContentContainedBy(othersNameNode)) {
            return false;
        }
        List<BaseProperty> othersProperties = otherAsGraph.getAllProperties();
        List<BaseProperty> thisProperties = this.graphInformation.getBaseProperties();
        if (!(othersProperties.containsAll(thisProperties))) {
            return false;
        }
        return true;
    }

    /**
     * Given a Graph (g) and a BaseProperty (terminatingProperty) calculate the one and only one
     * path from terminatingProperty back to the base node.
     *
     * This only works for graphs that are trees.
     *
     * @param g
     * @param terminatingProperty
     * @param list
     */
    private List<BaseProperty> calculatePath(Graph g, BaseProperty terminatingProperty) {
        if (g == null) {
            throw new IllegalArgumentException("g cannot be null");
        }
        if (terminatingProperty == null) {
            throw new IllegalArgumentException("terminatingProperty cannot be null");
        }
        List<BaseProperty> returnValue = new ArrayList<BaseProperty>();
        BaseProperty underExamination = terminatingProperty;
        returnValue.add(0, terminatingProperty);
        while (true) {
            List<BaseProperty> parents = g.getParents(underExamination);
            if (parents == null || parents.size() == 0) {
                break;
            }
            assert parents.size() == 1;
            returnValue.add(0, parents.get(0));
            underExamination = parents.get(0);
        }
        return returnValue;
    }

    public static boolean isTree(Graph g) {
        Map map = GraphInformation.recursivelyAnalyzeGraph(g.getNamenode());
        List<BaseProperty> properties = GraphInformation.getBaseProperties(map);
        for (BaseProperty prop : properties) {
            if (g.getParents(prop).size() > 1) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.getGraphInformation().toString();
    }
}
