package petrieditor.modules.treemodule;

import petrieditor.model.Arc;
import petrieditor.model.InhibitorArc;
import petrieditor.model.PetriNet;
import petrieditor.model.Transition;
import petrieditor.modules.Module;
import petrieditor.modules.ResultPane;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Module for constructing execution tree and graph and doing related analyses.
 * 
 * Fully stateless.
 * 
 * @author pawel
 */
public class TreeModule implements Module {

    private static final String MODULE_NAME = "Tree and graph";

    class SSComponent {

        HashSet<Transition> innerTransitions = new HashSet<Transition>();

        HashSet<SSComponent> exits = new HashSet<SSComponent>();

        HashSet<Transition> reachableTransitions = new HashSet<Transition>();
    }

    /**
     * Returns the module name.
     */
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * Builds the tree and graph and does related analyses.
     * 
     * @warning Temporarily tampers with the net, but restores its state afterwards.
     */
    public ResultPane run(PetriNet petriNet) {
        System.out.println("Tree module");
        TreeModuleResultSet resultSet = new TreeModuleResultSet(petriNet);
        for (Transition t : petriNet.getTransitions()) {
            for (Arc a : t.getInputArcs()) {
                if (a instanceof InhibitorArc) {
                    JOptionPane.showMessageDialog(null, "Inhibitor arc found, analysis not possible", "Tree module", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
        }
        dfsTreeConstruction(resultSet, resultSet.rootVertex);
        if (resultSet.reachableTransitions.size() == resultSet.petriNet.getTransitions().size()) {
            resultSet.allTransitionsReachable = true;
        }
        if (resultSet.isBounded) {
            stronglyConnectedComponents(resultSet);
        }
        return new TreeModuleResultPane(resultSet);
    }

    /**
     * Internal use only.
     * 
     * DFS method for constructing the tree.
     * 
     * @warning Changes state of the network and DOES NOT restore it.
     * 
     * @param resultSet Result set which is being constructed.
     * @param vertex Vertex to be analysed.
     * @param dfsPathMarkings List of markings encountered during the search - from the root (inclusive) to supplied vertex (not inclusive)
     */
    private void dfsTreeConstruction(TreeModuleResultSet resultSet, GraphVertex vertex, LinkedList<ArrayList<Integer>> dfsPathMarkings) {
        if (vertex.colour != GraphVertex.Colour.WHITE) {
            return;
        }
        if (vertex.isCopy()) {
            return;
        }
        vertex.colour = GraphVertex.Colour.GRAY;
        dfsPathMarkings.add(vertex.getMarking());
        for (Transition transition : resultSet.petriNet.getTransitions()) {
            resultSet.petriNet.setNetworkMarking(vertex.getMarking());
            if (transition.isEnabled()) {
                resultSet.reachableTransitions.add(transition);
                transition.fire();
                GraphVertex newVertex = new GraphVertex(resultSet.petriNet.getNetworkMarking());
                for (ArrayList<Integer> previous : dfsPathMarkings) {
                    if (newVertex.testAndMarkOmega(previous)) {
                        resultSet.isBounded = false;
                        break;
                    }
                }
                vertex.addExit(transition, newVertex);
                GraphVertex oldVertex = resultSet.vertices.get(newVertex.getMarking());
                if (oldVertex != null) {
                    newVertex.markAsCopyOf(oldVertex);
                } else {
                    resultSet.vertices.put(newVertex.getMarking(), newVertex);
                    dfsTreeConstruction(resultSet, newVertex, dfsPathMarkings);
                }
            }
        }
        if (vertex.getExitTransitions().isEmpty()) {
            resultSet.deadlockFound = true;
        }
        dfsPathMarkings.removeLast();
        vertex.colour = GraphVertex.Colour.BLACK;
    }

    /**
     * Convenience method for the above one.
     */
    private void dfsTreeConstruction(TreeModuleResultSet resultSet, GraphVertex vertex) {
        dfsTreeConstruction(resultSet, vertex, new LinkedList<ArrayList<Integer>>());
    }

    /**
     * Finds the strongly connected components of the execution graph.
     * And tests for L3 and L4 liveness classes in bounded nets.
     */
    private void stronglyConnectedComponents(TreeModuleResultSet resultSet) {
        HashMap<ArrayList<Integer>, GraphVertex> transposed = new HashMap<ArrayList<Integer>, GraphVertex>();
        for (ArrayList<Integer> arrlist : resultSet.vertices.keySet()) {
            transposed.put(arrlist, new GraphVertex(arrlist));
            resultSet.vertices.get(arrlist).colour = GraphVertex.Colour.WHITE;
        }
        for (ArrayList<Integer> arrlist : resultSet.vertices.keySet()) {
            GraphVertex from = transposed.get(arrlist);
            GraphVertex originalFrom = resultSet.vertices.get(arrlist);
            for (Transition t : originalFrom.getExitTransitions()) {
                GraphVertex to = transposed.get(originalFrom.getExit(t).getPrimaryVertex().getMarking());
                to.addExit(t, from);
            }
        }
        LinkedList<ArrayList<Integer>> visitTimes = new LinkedList<ArrayList<Integer>>();
        for (ArrayList<Integer> arrlist : resultSet.vertices.keySet()) {
            dfsTimestamping(resultSet.vertices.get(arrlist), visitTimes);
        }
        HashMap<ArrayList<Integer>, Integer> sssnumbers = new HashMap<ArrayList<Integer>, Integer>();
        int sssId = 0;
        for (ArrayList<Integer> list : visitTimes) {
            if (dfsDoSSC(transposed.get(list), sssnumbers, sssId)) {
                sssId++;
            }
        }
        for (ArrayList<Integer> arrlist : sssnumbers.keySet()) {
            System.out.println(transposed.get(arrlist) + " " + sssnumbers.get(arrlist));
        }
        transposed = null;
        visitTimes = null;
        resultSet.stronglyConnectedComponentsCount = sssId;
        if (resultSet.isBounded && resultSet.allTransitionsReachable) {
            if (resultSet.stronglyConnectedComponentsCount == 1) {
                resultSet.allTransitionsReachableFromEverySCC = resultSet.everyTransitionsInSomeSCC = true;
            } else {
                SSComponent components[] = new SSComponent[sssId];
                for (int i = 0; i < sssId; i++) {
                    components[i] = new SSComponent();
                }
                for (ArrayList<Integer> marking : sssnumbers.keySet()) {
                    int compNumber = sssnumbers.get(marking);
                    GraphVertex vertex = resultSet.vertices.get(marking);
                    for (Transition t : vertex.getExitTransitions()) {
                        GraphVertex targetVertex = vertex.getExit(t).getPrimaryVertex();
                        int targetCompNumber = sssnumbers.get(targetVertex.getMarking());
                        if (compNumber == targetCompNumber) {
                            components[compNumber].innerTransitions.add(t);
                        } else {
                            components[compNumber].exits.add(components[targetCompNumber]);
                        }
                    }
                }
                HashSet<Transition> allInnerTransitions = sscTransitionReachanilityDFS(components[sssnumbers.get(resultSet.rootVertex.getMarking())]);
                if (allInnerTransitions.size() == resultSet.petriNet.getTransitions().size()) {
                    resultSet.everyTransitionsInSomeSCC = true;
                    if (!resultSet.deadlockFound) {
                        resultSet.allTransitionsReachableFromEverySCC = true;
                        for (int i = 0; i < sssId; i++) {
                            if (components[i].reachableTransitions.size() != resultSet.petriNet.getTransitions().size()) {
                                resultSet.allTransitionsReachableFromEverySCC = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * DFS used by ssc algorithm in first (timestamping) phase
     * @param gv
     * @param visitTimes
     */
    private void dfsTimestamping(GraphVertex gv, LinkedList<ArrayList<Integer>> visitTimes) {
        if (gv.colour != GraphVertex.Colour.WHITE) {
            return;
        }
        gv.colour = GraphVertex.Colour.GRAY;
        for (Transition t : gv.getExitTransitions()) {
            GraphVertex newVertex = gv.getExit(t).getPrimaryVertex();
            dfsTimestamping(newVertex, visitTimes);
        }
        visitTimes.addFirst(gv.getMarking());
        gv.colour = GraphVertex.Colour.BLACK;
    }

    /**
     * DFS used by ssc algorithm in second (enumeration) phase
     * 
     * @param vertex
     * @param sssnumbers
     * @param sssid
     * @return
     */
    private boolean dfsDoSSC(GraphVertex vertex, HashMap<ArrayList<Integer>, Integer> sssnumbers, int sssid) {
        if (vertex.colour != GraphVertex.Colour.WHITE) {
            return false;
        }
        vertex.colour = GraphVertex.Colour.GRAY;
        sssnumbers.put(vertex.getMarking(), sssid);
        for (Transition t : vertex.getExitTransitions()) {
            GraphVertex newVertex = vertex.getExit(t).getPrimaryVertex();
            dfsDoSSC(newVertex, sssnumbers, sssid);
        }
        vertex.colour = GraphVertex.Colour.BLACK;
        return true;
    }

    private HashSet<Transition> sscTransitionReachanilityDFS(SSComponent myComponent) {
        HashSet<Transition> rt = new HashSet<Transition>(myComponent.innerTransitions);
        for (SSComponent tComponent : myComponent.exits) {
            rt.addAll(sscTransitionReachanilityDFS(tComponent));
        }
        myComponent.reachableTransitions = rt;
        return rt;
    }
}
