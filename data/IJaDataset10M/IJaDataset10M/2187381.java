package main.naiveBayes;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.EdgeMap;
import y.base.Node;
import y.base.NodeCursor;
import main.graph.FormTree;
import main.graph.FormTreeWithMapping;
import main.graph.GetCMGraph;
import main.graph.GraphAlgo;
import main.graph.ValuedGraph;
import main.model.Attribute;
import main.model.EdgeGivenPath;
import main.model.EdgePriors;
import main.model.Entity;
import main.model.NodeAndLabel;
import main.model.OntoEle;
import main.model.OntoNodePair;
import main.model.OntoPath;
import main.model.SteinerTree;
import main.model.SteinerTreePathEle;

/**
 * 
 * compute a set of candidate trees in the ontology graph as candidate mappings
 * Each candidate tree consists of a set of paths corresponding to the set of form tree edges.
 * Each form tree edge is specified as a pair of ontology nodes (elements).
 * 
 * @author YuanAn
 *
 */
public class GetCandidateOntoTree {

    public static ArrayList<EdgeList> getOntoSteiners(FormTree tree, ValuedGraph cmg) {
        ArrayList<EdgeList> ans = new ArrayList<EdgeList>();
        try {
            EdgeMap weights = cmg.createEdgeMap();
            for (EdgeCursor ec = cmg.edges(); ec.ok(); ec.next()) {
                Edge edge = ec.edge();
                weights.setDouble(edge, 1.0);
            }
            ArrayList<String> labels = new ArrayList<String>();
            for (NodeCursor nc = tree.nodes(); nc.ok(); nc.next()) {
                Node n = nc.node();
                String v = (String) tree.getNodeValue(n);
                labels.add(v);
            }
            ArrayList<ArrayList<Node>> fixedSet = FindCorrespondence.getFixedNodes(cmg, labels);
            for (ArrayList<Node> fixed : fixedSet) {
                ArrayList<EdgeList> steiners = GraphAlgo.allSteinerTreesIntegrated(cmg, weights, fixed);
                ans.addAll(steiners);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static ArrayList<SteinerTree> getOntoSteinersWithMatchingCorrespondences(FormTree tree, ValuedGraph cmg) {
        ArrayList<SteinerTree> ans = new ArrayList<SteinerTree>();
        try {
            EdgeMap weights = cmg.createEdgeMap();
            for (EdgeCursor ec = cmg.edges(); ec.ok(); ec.next()) {
                Edge edge = ec.edge();
                weights.setDouble(edge, 1.0);
            }
            ArrayList<String> labels = new ArrayList<String>();
            for (NodeCursor nc = tree.nodes(); nc.ok(); nc.next()) {
                Node n = nc.node();
                String v = (String) tree.getNodeValue(n);
                labels.add(v);
            }
            ArrayList<ArrayList<NodeAndLabel>> fixedSet = FindCorrespondence.getFixedNoddAndLabels(cmg, labels);
            for (ArrayList<NodeAndLabel> fixedNodeAndLabel : fixedSet) {
                FormTree newTree = getFormTreeWithTwoEndLabels(tree, fixedNodeAndLabel, cmg);
                ArrayList<Node> fixed = getFixedOntoNodes(fixedNodeAndLabel);
                ArrayList<EdgeList> steiners = GraphAlgo.allSteinerTreesIntegrated(cmg, weights, fixed);
                for (EdgeList steiner : steiners) {
                    SteinerTree aSteinerTree = new SteinerTree();
                    aSteinerTree.setSteiner(steiner);
                    aSteinerTree.setFormTree(newTree);
                    ans.add(aSteinerTree);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static FormTree getFormTreeWithTwoEndLabels(FormTree tree, ArrayList<NodeAndLabel> fixedNodeAndLabel, ValuedGraph cmg) {
        FormTree ans = tree.duplicateFormTree();
        for (EdgeCursor ec = ans.edges(); ec.ok(); ec.next()) {
            Edge e = ec.edge();
            ArrayList<String> endLabels = new ArrayList<String>();
            Node firstNode = e.source();
            Node secondNode = e.target();
            String firstNodeString = (String) ans.getNodeValue(firstNode);
            String secondNodeString = (String) ans.getNodeValue(secondNode);
            String firstNodeOntoLabel = getNodeOntoLabel(fixedNodeAndLabel, firstNodeString, cmg);
            String secondNodeOntoLabel = getNodeOntoLabel(fixedNodeAndLabel, secondNodeString, cmg);
            endLabels.add(firstNodeOntoLabel);
            endLabels.add(secondNodeOntoLabel);
            ans.putEdgeToTwoNodeLabels(e, endLabels);
        }
        return ans;
    }

    public static String getNodeOntoLabel(ArrayList<NodeAndLabel> fixedNodeAndLabel, String nodeString, ValuedGraph cmg) {
        for (NodeAndLabel nodeLabel : fixedNodeAndLabel) {
            if (nodeLabel.getLabel().equalsIgnoreCase(nodeString)) {
                Node node = nodeLabel.getNode();
                OntoEle ontoEle = (OntoEle) cmg.getNodeValue(node);
                return ontoEle.getName();
            }
        }
        return null;
    }

    public static ArrayList<Node> getFixedOntoNodes(ArrayList<NodeAndLabel> fixedNodeAndLabel) {
        ArrayList<Node> ans = new ArrayList<Node>();
        for (NodeAndLabel nodeLabel : fixedNodeAndLabel) {
            ans.add(nodeLabel.getNode());
        }
        return ans;
    }

    /**
	 * return a set of paths in the Steiner tree corresponding to the edges in the form tree
	 * 
	 * @param tree
	 * @param steiner
	 * @param cmg
	 * @return
	 */
    public static HashMap<ArrayList<String>, OntoPath> getPathsFromSteiner(FormTree tree, EdgeList steiner, ValuedGraph cmg) {
        HashMap<ArrayList<String>, OntoPath> ans = new HashMap<ArrayList<String>, OntoPath>();
        for (EdgeCursor ec = tree.edges(); ec.ok(); ec.next()) {
            Edge treeEdge = ec.edge();
            ArrayList<String> nodePair = tree.getTwoEndLabels(treeEdge);
            String firstNodeLabel = nodePair.get(0);
            String secondNodeLabel = nodePair.get(1);
            EdgeList pathEdges = getCorrespondingPath(firstNodeLabel, secondNodeLabel, steiner, cmg);
            OntoPath path = new OntoPath();
            for (EdgeCursor pec = pathEdges.edges(); pec.ok(); pec.next()) {
                Edge pathEdge = pec.edge();
                path.addEdge(pathEdge);
            }
            ans.put(nodePair, path);
        }
        return ans;
    }

    /**
	 * find the path corresponding to the two given end nodes
	 * 
	 * @param firstNode
	 * @param secondNode
	 * @param steiner
	 * @return
	 */
    public static EdgeList getCorrespondingPath(String firstNodeLabel, String secondNodeLabel, EdgeList steiner, ValuedGraph cmg) {
        SteinerTreePathEle firstTreePathEle = new SteinerTreePathEle(firstNodeLabel, null, null);
        LinkedList<SteinerTreePathEle> queue = new LinkedList<SteinerTreePathEle>();
        queue.add(firstTreePathEle);
        ArrayList<Edge> visited = new ArrayList<Edge>();
        SteinerTreePathEle lastTreePathEle = null;
        while (!queue.isEmpty()) {
            SteinerTreePathEle head = queue.removeFirst();
            String headNodeLabel = head.getCurrentNode();
            ArrayList<Edge> incidentEdges = getIncidentEdges(headNodeLabel, steiner, cmg);
            for (Edge incidentEdge : incidentEdges) {
                if (!visited.contains(incidentEdge)) {
                    visited.add(incidentEdge);
                    Node headNode = getNodeCorrespondingLabel(incidentEdge, headNodeLabel, cmg);
                    Node incidentOtherNode = incidentEdge.opposite(headNode);
                    OntoEle incidentOtherNodeEle = (OntoEle) cmg.getNodeValue(incidentOtherNode);
                    String incidentOtherNodeLabel = incidentOtherNodeEle.getName();
                    if (incidentOtherNodeLabel.equalsIgnoreCase(secondNodeLabel)) {
                        lastTreePathEle = new SteinerTreePathEle(incidentOtherNodeLabel, incidentEdge, head);
                        break;
                    } else {
                        SteinerTreePathEle aEle = new SteinerTreePathEle(incidentOtherNodeLabel, incidentEdge, head);
                        queue.add(aEle);
                    }
                }
            }
        }
        EdgeList ans = new EdgeList();
        while (lastTreePathEle != null) {
            Edge leadingEdge = lastTreePathEle.getIncidentEdge();
            if (leadingEdge != null) ans.add(0, leadingEdge);
            lastTreePathEle = lastTreePathEle.getPreviousElement();
        }
        return ans;
    }

    public static ArrayList<Edge> getIncidentEdges(String headNodeLabel, EdgeList steiner, ValuedGraph cmg) {
        ArrayList<Edge> ans = new ArrayList<Edge>();
        for (EdgeCursor ec = steiner.edges(); ec.ok(); ec.next()) {
            Edge e = ec.edge();
            Node source = e.source();
            Node target = e.target();
            OntoEle sourceEle = (OntoEle) cmg.getNodeValue(source);
            OntoEle targetEle = (OntoEle) cmg.getNodeValue(target);
            if (sourceEle.getName().equalsIgnoreCase(headNodeLabel) || targetEle.getName().equalsIgnoreCase(headNodeLabel)) ans.add(e);
        }
        return ans;
    }

    public static Node getNodeCorrespondingLabel(Edge incidentEdge, String headNodeLabel, ValuedGraph cmg) {
        Node ans = incidentEdge.source();
        OntoEle ele = (OntoEle) cmg.getNodeValue(ans);
        if (ele.getName().equalsIgnoreCase(headNodeLabel)) return ans;
        return incidentEdge.target();
    }

    public static double getConditionalProbability(HashMap<ArrayList<String>, OntoPath> pairToPaths, EdgeList steiner, FormTree tree, ValuedGraph cmg, EdgePriors priors, ArrayList<ArrayList<EdgeGivenPath>> likelihoods) {
        System.out.println("Computing conditional probability for a candidate steiner tree.");
        Iterator<ArrayList<String>> outerIt = pairToPaths.keySet().iterator();
        double prob = 0.0;
        while (outerIt.hasNext()) {
            ArrayList<String> outerPair = outerIt.next();
            OntoPath aPath = pairToPaths.get(outerPair);
            ArrayList<EdgeGivenPath> likelihoodvalues = null;
            boolean found = false;
            for (ArrayList<EdgeGivenPath> values : likelihoods) {
                EdgeGivenPath aValue = values.get(0);
                OntoPath aValuePath = aValue.getPath();
                if (aValuePath == null) likelihoodvalues = values; else if (isSamePath(aPath, aValuePath)) {
                    likelihoodvalues = values;
                    found = true;
                    break;
                }
            }
            if (!found) {
                EdgeGivenPath notAppearingPath = likelihoodvalues.get(0);
                double conditionalProbForNotAppearingPath = notAppearingPath.getConditionalProb();
                prob += conditionalProbForNotAppearingPath * pairToPaths.size();
            } else {
                Iterator<ArrayList<String>> innerIt = pairToPaths.keySet().iterator();
                while (innerIt.hasNext()) {
                    ArrayList<String> innerPair = innerIt.next();
                    double aConditionalProb = getConditionalProbPairGivenPath(innerPair, likelihoodvalues, cmg);
                    prob += aConditionalProb;
                }
            }
            ArrayList<Edge> edges = aPath.getEdgePath();
            for (Edge edge : edges) {
                if (priors.get(edge) != null) prob += priors.get(edge).doubleValue(); else prob += priors.getDummyPrior();
            }
        }
        return prob;
    }

    public static boolean isSamePath(OntoPath aPath, OntoPath aValuePath) {
        boolean ans = true;
        ArrayList<Edge> aPathEdges = aPath.getEdgePath();
        ArrayList<Edge> aValuePathEdges = aValuePath.getEdgePath();
        for (Edge e : aPathEdges) if (!aValuePathEdges.contains(e)) return false;
        for (Edge e : aValuePathEdges) if (!aPathEdges.contains(e)) return false;
        return ans;
    }

    /**
	 * 
	 * return the conditional probability matching the pair of node labels.
	 * 
	 * @param innerPair
	 * @param likelihoodvalues
	 * @return
	 */
    public static double getConditionalProbPairGivenPath(ArrayList<String> innerPair, ArrayList<EdgeGivenPath> likelihoodvalues, ValuedGraph cmg) {
        double ans = 0.0;
        for (EdgeGivenPath edge : likelihoodvalues) {
            OntoNodePair pair = edge.getEdge();
            if (pair.getFirstNode() == null || pair.getSecondNode() == null) ans = edge.getConditionalProb(); else {
                if (isMatching(innerPair, pair, cmg)) {
                    ans = edge.getConditionalProb();
                    break;
                }
            }
        }
        return ans;
    }

    public static boolean isMatching(ArrayList<String> innerPair, OntoNodePair pair, ValuedGraph cmg) {
        boolean ans = false;
        Node firstNode = pair.getFirstNode();
        Node secondNode = pair.getSecondNode();
        String firstNodeLabel = ((OntoEle) cmg.getNodeValue(firstNode)).getName();
        String secondNodeLabel = ((OntoEle) cmg.getNodeValue(secondNode)).getName();
        if ((innerPair.get(0).equalsIgnoreCase(firstNodeLabel) && innerPair.get(1).equalsIgnoreCase(secondNodeLabel)) || (innerPair.get(0).equalsIgnoreCase(secondNodeLabel) && innerPair.get(1).equalsIgnoreCase(firstNodeLabel))) ans = true;
        return ans;
    }
}
