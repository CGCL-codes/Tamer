package edu.byu.ece.edif.tools.replicate.nmr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import edu.byu.ece.edif.arch.xilinx.XilinxMergeParser;
import edu.byu.ece.edif.arch.xilinx.XilinxTools;
import edu.byu.ece.edif.core.EdifCell;
import edu.byu.ece.edif.core.EdifCellInstance;
import edu.byu.ece.edif.core.EdifNameConflictException;
import edu.byu.ece.edif.core.EdifNet;
import edu.byu.ece.edif.core.EdifPortRef;
import edu.byu.ece.edif.core.EdifRuntimeException;
import edu.byu.ece.edif.core.InvalidEdifNameException;
import edu.byu.ece.edif.tools.flatten.FlattenedEdifCell;
import edu.byu.ece.edif.tools.replicate.nmr.tmr.TMRReplicationType;
import edu.byu.ece.edif.tools.replicate.nmr.xilinx.XilinxNMRArchitecture;
import edu.byu.ece.edif.tools.replicate.nmr.xilinx.XilinxVirtexDeviceUtilizationTracker;
import edu.byu.ece.edif.util.graph.AbstractEdifGraph;
import edu.byu.ece.edif.util.graph.EdifCellBadCutGroupings;
import edu.byu.ece.edif.util.graph.EdifCellInstanceCollection;
import edu.byu.ece.edif.util.graph.EdifCellInstanceCollectionGraph;
import edu.byu.ece.edif.util.graph.EdifCellInstanceEdge;
import edu.byu.ece.edif.util.graph.EdifCellInstanceGraph;
import edu.byu.ece.edif.util.graph.EdifCellInstanceGroupings;
import edu.byu.ece.edif.util.graph.EdifPortRefEdge;
import edu.byu.ece.graph.AbstractGraphToDotty;
import edu.byu.ece.graph.BasicGraph;
import edu.byu.ece.graph.DirectedGraph;
import edu.byu.ece.graph.Edge;
import edu.byu.ece.graph.dfs.BasicDepthFirstSearchTree;
import edu.byu.ece.graph.dfs.DepthFirstSearchForest;
import edu.byu.ece.graph.dfs.DepthFirstTree;
import edu.byu.ece.graph.dfs.SCCDepthFirstSearch;

/**
 * A set of utilities to work with strongly connected components (SCCs),
 * including methods to break apart large SCCs into smaller, more manageable
 * units. Triplication of SCCs for a given list of EdifEdges is also performed
 * by this class.
 * 
 * @author Michael Wirthlin, Brian Pratt, Keith Morgan
 * @see SCCDepthFirstSearch
 */
public class NMRGraphUtilities {

    public static void main(String args[]) {
        System.out.println("Parsing . . .");
        EdifCell cell = XilinxMergeParser.parseAndMergeXilinx(args);
        System.out.println("Starting Flattening . . .");
        EdifCell flat_cell = null;
        try {
            flat_cell = new FlattenedEdifCell(cell);
        } catch (EdifNameConflictException e1) {
            e1.toRuntime();
        } catch (InvalidEdifNameException e1) {
            e1.toRuntime();
        }
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(flat_cell);
        final String part = "XCV1000FG680";
        NMRArchitecture nmrArch = new XilinxNMRArchitecture();
        System.out.println("Calculating normal resource utilization of cell " + flat_cell);
        DeviceUtilizationTracker du_tracker = null;
        try {
            du_tracker = new XilinxVirtexDeviceUtilizationTracker(flat_cell, part);
        } catch (OverutilizationException e) {
            throw new EdifRuntimeException("ERROR: Initial contents of cell " + flat_cell + " do not fit into part " + part);
        }
        ReplicationUtilizationTracker rTracker = new ReplicationUtilizationTracker(du_tracker);
        System.out.println("Normal utilization for cell " + flat_cell);
        System.out.println(du_tracker);
        EdifCellInstanceGroupings groupings = new EdifCellBadCutGroupings(flat_cell, nmrArch, graph);
        EdifCellInstanceCollectionGraph badCutGroupConn = new EdifCellInstanceCollectionGraph(graph, groupings, true);
        SCCDepthFirstSearch sccDFS = new SCCDepthFirstSearch(badCutGroupConn);
        Collection sccs = new ArrayList(sccDFS.getTrees().size());
        for (DepthFirstTree tree : sccDFS.getTrees()) {
            sccs.add(tree.getNodes());
        }
        String data = new AbstractGraphToDotty().createColoredDottyBody(badCutGroupConn, sccs);
        AbstractGraphToDotty.printFile("sccGraph.dot", data);
        ReplicationType replicationType = TMRReplicationType.getInstance(nmrArch);
        boolean override = false;
        nmrSCCsUsingSCCDecomposition(sccDFS, nmrArch, rTracker, true, DEFAULT_SCC_SORT_TYPE, replicationType, override);
        System.out.println("Post TMR estimate:\n" + du_tracker);
    }

    public static final int DEFAULT_SCC_SORT_TYPE = 3;

    public static List<Edge> createBasicDecompositionCutset(AbstractEdifGraph graph, SCCDepthFirstSearch sccs, NMRArchitecture arch) {
        List<Edge> cuts = new ArrayList<Edge>();
        Stack<DepthFirstTree> s = new Stack<DepthFirstTree>();
        for (DepthFirstTree scc : sccs.getTrees()) {
            s.push(scc);
        }
        SCCDepthFirstSearch lastSearch = sccs;
        while (!s.empty()) {
            DepthFirstTree subSCCDFSTree = s.pop();
            Collection backEdges = subSCCDFSTree.getBackEdges();
            Collection badEdges = findBadEdges(backEdges, arch);
            backEdges.removeAll(badEdges);
            if (badEdges.size() == 0) {
                graph.removeEdges(backEdges);
                cuts.addAll(backEdges);
            } else {
                if (backEdges.size() != 0) {
                    graph.removeEdges(backEdges);
                    cuts.addAll(backEdges);
                    BasicGraph sccSubGraph = graph.getSubGraph(subSCCDFSTree.getNodes());
                    SCCDepthFirstSearch sccDFS = new SCCDepthFirstSearch(sccSubGraph);
                    for (DepthFirstTree scc : sccDFS.getTopologicallySortedTreeList()) {
                        s.push(scc);
                    }
                } else {
                    LinkedList visitOrder = new LinkedList(subSCCDFSTree.getNodes());
                    visitOrder.addFirst(visitOrder.removeLast());
                    BasicGraph sccSubGraph = graph.getSubGraph(visitOrder);
                    SCCDepthFirstSearch sccDFS = new SCCDepthFirstSearch(sccSubGraph, visitOrder);
                    for (DepthFirstTree scc : sccDFS.getTopologicallySortedTreeList()) {
                        s.push(scc);
                    }
                }
            }
        }
        return cuts;
    }

    public static Collection<EdifPortRef> createAfterFFsCutset(AbstractEdifGraph graph, NMRArchitecture nmrArch) {
        Collection<EdifPortRef> cuts = new LinkedHashSet<EdifPortRef>();
        for (Object node : graph.getNodes()) {
            if (node instanceof EdifCellInstance) {
                EdifCellInstance eci = (EdifCellInstance) node;
                if (XilinxTools.isRegisterCell(eci.getCellType())) {
                    EdifPortRef eprQ = null;
                    EdifCellInstanceEdge edgeQ = null;
                    for (Object edge : graph.getOutputEdges(node)) {
                        if (edge instanceof EdifCellInstanceEdge) {
                            EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                            EdifPortRef epr = eciEdge.getSourceEPR();
                            eprQ = epr;
                            edgeQ = eciEdge;
                            if (!nmrArch.isBadCutConnection(eciEdge.getSourceEPR(), eciEdge.getSinkEPR())) {
                                graph.removeEdge(edgeQ);
                                cuts.add(eciEdge.getSinkEPR());
                            }
                        }
                    }
                }
            }
        }
        return cuts;
    }

    public static Collection<EdifPortRef> createBeforeFFsCutset(AbstractEdifGraph graph, NMRArchitecture nmrArch) {
        Collection<EdifPortRef> cuts = new LinkedHashSet<EdifPortRef>();
        for (Object node : graph.getNodes()) {
            if (node instanceof EdifCellInstance) {
                EdifCellInstance eci = (EdifCellInstance) node;
                if (XilinxTools.isRegisterCell(eci.getCellType())) {
                    EdifPortRef eprD = null;
                    EdifCellInstanceEdge edgeD = null;
                    for (Object edge : graph.getInputEdges(node)) {
                        if (edge instanceof EdifCellInstanceEdge) {
                            EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                            EdifPortRef epr = eciEdge.getSinkEPR();
                            if (epr.getPort().getName().equalsIgnoreCase("D")) {
                                eprD = epr;
                                edgeD = eciEdge;
                                if (!nmrArch.isBadCutConnection(eciEdge.getSourceEPR(), eciEdge.getSinkEPR())) {
                                    graph.removeEdge(edgeD);
                                    cuts.add(eprD);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        return cuts;
    }

    /**
     * Create a valid set of cuts which break all feedback in the given SCC.
     * This method breaks up the SCC little by little instead of generating the
     * entire cutset at once. Thus, cuts are determined in sets, breaking the
     * problem up when a full cutset is not immediately found.
     * 
     * @param graph The top-level graph
     * @param scc A BasicDepthFirstSearchTree object representing an SCC in the
     * top-level graph
     * @param arch The TMRArchitecture, needed to determine good/bad cuts
     * @return A Collection of EdifEdges making up a valid set of cuts to break
     * all feedback in the given SCC
     */
    public static Collection<Edge> createDecomposeValidCutSet(AbstractEdifGraph graph, BasicDepthFirstSearchTree scc, NMRArchitecture arch) {
        AbstractEdifGraph sccGraph = (AbstractEdifGraph) graph.getSubGraph(scc.getNodes());
        if (DEBUG) System.out.println("createDecomposeValidCutSet() called on SCC: " + sccGraph);
        Collection<Edge> cuts = new LinkedHashSet<Edge>();
        Stack<BasicDepthFirstSearchTree> s = new Stack<BasicDepthFirstSearchTree>();
        s.push(scc);
        while (!s.empty()) {
            BasicDepthFirstSearchTree subSCCDFSTree = s.pop();
            if (DEBUG) System.out.println("POP: stack SCC: " + subSCCDFSTree);
            Collection<Edge> goodBackEdges = subSCCDFSTree.getBackEdges();
            Collection<Edge> badBackEdges = null;
            badBackEdges = findBadEdges(goodBackEdges, arch);
            goodBackEdges.removeAll(badBackEdges);
            cuts.addAll(goodBackEdges);
            if (badBackEdges.size() > 0) {
                if (DEBUG) System.out.println("Bad back edges found.");
                if (DEBUG) System.out.println("Bad edges: " + badBackEdges);
                if (goodBackEdges.size() == 0) {
                    if (DEBUG) System.out.print("Zero good back edges found. Shifting DFS to another node: ");
                    List visitList = (List) subSCCDFSTree.getNodes();
                    Object formerRootNode = visitList.remove(0);
                    visitList.add(formerRootNode);
                    if (DEBUG) System.out.println(visitList.get(0));
                    AbstractEdifGraph subSCCGraph = (AbstractEdifGraph) sccGraph.getSubGraph(subSCCDFSTree.getNodes());
                    DepthFirstSearchForest newSearch = new DepthFirstSearchForest(subSCCGraph, visitList);
                    if (newSearch.getTrees().size() != 1) {
                        System.out.println("Warning (createDecomposeValidCutSet): new SCC forest is not size 1. (size=" + newSearch.getTrees().size() + ")");
                    }
                    BasicDepthFirstSearchTree new_tree = (BasicDepthFirstSearchTree) newSearch.getTrees().iterator().next();
                    s.push(new_tree);
                    if (DEBUG) System.out.println("\tPushing subtree with " + new_tree.getNodes().size() + " nodes and " + new_tree.getEdges().size() + " edges.");
                } else {
                    if (DEBUG) System.out.println("Removing " + goodBackEdges.size() + " edges from graph with " + sccGraph.getEdges().size() + " edges...");
                    sccGraph.removeEdges(goodBackEdges);
                    SCCDepthFirstSearch newSearch = new SCCDepthFirstSearch(sccGraph);
                    if (DEBUG) System.out.println("Clearing stack");
                    s.clear();
                    for (Iterator i = newSearch.getTrees().iterator(); i.hasNext(); ) {
                        BasicDepthFirstSearchTree smallScc = (BasicDepthFirstSearchTree) i.next();
                        s.push(smallScc);
                        if (DEBUG) System.out.println("\tPushing subtree with " + smallScc.getNodes().size() + " nodes and " + smallScc.getEdges().size() + " edges.");
                    }
                    if (DEBUG) System.out.println("Found " + newSearch.getSingleNodes().size() + " SingleNodeSCCs: " + newSearch.getSingleNodes());
                }
            } else {
                if (DEBUG) System.out.println("No bad back edges found!");
            }
        }
        return cuts;
    }

    /**
     * The highest fanin flip-flop is the flip-flop with the most nets leading into the D
     * input going five levels of instances backwards.
     * 
     * @param graph
     * @param sccs
     * @param arch
     * @return
     */
    public static Collection<EdifPortRef> createHighestFFFaninCutset(AbstractEdifGraph graph, SCCDepthFirstSearch sccs, NMRArchitecture arch) {
        Collection<EdifPortRef> cuts = new LinkedHashSet<EdifPortRef>();
        Stack<DepthFirstTree> s = new Stack<DepthFirstTree>();
        for (DepthFirstTree scc : sccs.getTrees()) {
            s.push(scc);
        }
        while (!s.empty()) {
            DepthFirstTree subSCCDFSTree = s.pop();
            EdifCellInstance highFaninNode = null;
            EdifPortRef highFaninDInput = null;
            EdifCellInstanceEdge highFaninDEdge = null;
            int highFaninCount = 0;
            for (Iterator i = subSCCDFSTree.getNodes().iterator(); i.hasNext(); ) {
                Object node = i.next();
                if (node instanceof EdifCellInstance) {
                    EdifCellInstance eci = (EdifCellInstance) node;
                    if (!XilinxTools.isRegisterCell(eci.getCellType())) continue;
                    EdifPortRef eprD = null;
                    EdifCellInstanceEdge edgeD = null;
                    for (Object edge : graph.getInputEdges(node)) {
                        if (edge instanceof EdifCellInstanceEdge) {
                            EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                            EdifPortRef epr = eciEdge.getSinkEPR();
                            if (eciEdge.getSinkEPR().getPort().getName().equalsIgnoreCase("D")) {
                                eprD = epr;
                                edgeD = eciEdge;
                            }
                        }
                    }
                    if (eprD == null) continue;
                    int numPredecessors = computeFanin(node, graph, 5);
                    if (numPredecessors > highFaninCount) {
                        Collection inputEdges = new ArrayList();
                        inputEdges.add(edgeD);
                        Collection<Edge> badEdges = findBadEdges(inputEdges, arch);
                        if (badEdges.size() == 0) {
                            highFaninCount = numPredecessors;
                            highFaninDInput = eprD;
                            highFaninDEdge = edgeD;
                            highFaninNode = eci;
                        }
                    }
                }
            }
            if (highFaninNode != null) {
                graph.removeEdge(highFaninDEdge);
                BasicGraph sccSubGraph = graph.getSubGraph(subSCCDFSTree.getNodes());
                cuts.add(highFaninDInput);
                SCCDepthFirstSearch newSearch = new SCCDepthFirstSearch(sccSubGraph);
                for (DepthFirstTree scc : newSearch.getTrees()) {
                    s.push(scc);
                }
            }
        }
        return cuts;
    }

    /**
     * The highest fanin flip-flop is the flip-flop with the most nets leading into the D
     * input going five levels of instances backwards.
     * 
     * @param graph
     * @param sccs
     * @param arch
     * @return
     */
    public static Collection<EdifPortRef> createHighestFFFaninOutputCutset(AbstractEdifGraph graph, SCCDepthFirstSearch sccs, NMRArchitecture arch) {
        Collection<EdifPortRef> cuts = new LinkedHashSet<EdifPortRef>();
        Stack<DepthFirstTree> s = new Stack<DepthFirstTree>();
        for (DepthFirstTree scc : sccs.getTrees()) {
            s.push(scc);
        }
        while (!s.empty()) {
            DepthFirstTree subSCCDFSTree = s.pop();
            EdifCellInstance highFaninNode = null;
            List<Edge> highFaninEdgesQ = null;
            Set<EdifPortRef> highFaninQEPRs = null;
            int highFaninCount = 0;
            for (Iterator i = subSCCDFSTree.getNodes().iterator(); i.hasNext(); ) {
                Object node = i.next();
                if (node instanceof EdifCellInstance) {
                    EdifCellInstance eci = (EdifCellInstance) node;
                    if (!XilinxTools.isRegisterCell(eci.getCellType())) continue;
                    Set<EdifPortRef> eprsQ = new LinkedHashSet<EdifPortRef>();
                    List<Edge> edgesQ = new ArrayList<Edge>();
                    for (Object edge : graph.getOutputEdges(node)) {
                        if (edge instanceof EdifCellInstanceEdge) {
                            EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                            EdifPortRef epr = eciEdge.getSourceEPR();
                            eprsQ.add(epr);
                            edgesQ.add(eciEdge);
                        }
                    }
                    if (eprsQ.size() != 0) {
                        int numPredecessors = computeFanin(node, graph, 5);
                        if (numPredecessors > highFaninCount) {
                            Collection<Edge> badEdgesQ = findBadEdges(edgesQ, arch);
                            if (badEdgesQ.size() == 0) {
                                highFaninCount = numPredecessors;
                                highFaninNode = eci;
                                highFaninEdgesQ = edgesQ;
                                highFaninQEPRs = eprsQ;
                            }
                        }
                    }
                }
            }
            if (highFaninNode == null) {
                List<Edge> highFaninEdgesQ2 = null;
                Set<EdifPortRef> highFaninQEPRs2 = null;
                int highFaninCount2 = 0;
                for (Iterator i = subSCCDFSTree.getNodes().iterator(); i.hasNext(); ) {
                    Object node = i.next();
                    if (node instanceof EdifCellInstance) {
                        EdifCellInstance eci = (EdifCellInstance) node;
                        if (!XilinxTools.isRegisterCell(eci.getCellType())) continue;
                        List<Edge> edgesQ = new ArrayList<Edge>();
                        for (Object edge : graph.getOutputEdges(node)) {
                            if (edge instanceof EdifCellInstanceEdge) {
                                EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                                EdifPortRef epr = eciEdge.getSourceEPR();
                                edgesQ.add(eciEdge);
                            }
                        }
                        if (edgesQ.size() != 0) {
                            int numPredecessors = computeFanin(node, graph, 5);
                            if (numPredecessors > highFaninCount2) {
                                Collection<Edge> badEdgesQ = findBadEdges(edgesQ, arch);
                                edgesQ.removeAll(badEdgesQ);
                                if (edgesQ.size() > 0) {
                                    highFaninCount2 = numPredecessors;
                                    highFaninEdgesQ2 = edgesQ;
                                    highFaninQEPRs2 = new LinkedHashSet<EdifPortRef>();
                                    for (Edge edge : edgesQ) {
                                        EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                                        highFaninQEPRs2.add(eciEdge.getSinkEPR());
                                    }
                                }
                            }
                        }
                    }
                }
                if (highFaninQEPRs != null) {
                    graph.removeEdges(highFaninEdgesQ2);
                    cuts.addAll(highFaninQEPRs2);
                } else {
                    throw new EdifRuntimeException("Error: no flip-flop D input with good cuts on the Q output found in the SCC. This cutset algorithm only works with feedback created with registers using a D input and Q output.");
                }
            } else {
                graph.removeEdges(highFaninEdgesQ);
                cuts.addAll(highFaninQEPRs);
            }
            BasicGraph sccSubGraph = graph.getSubGraph(subSCCDFSTree.getNodes());
            SCCDepthFirstSearch newSearch = new SCCDepthFirstSearch(sccSubGraph);
            for (DepthFirstTree scc : newSearch.getTrees()) {
                s.push(scc);
            }
        }
        return cuts;
    }

    public static int computeFanin(Object node, AbstractEdifGraph graph, int levels) {
        Stack nodes = new Stack();
        Set visited = new HashSet();
        Set<EdifNet> foundNets = new HashSet<EdifNet>();
        nodes.add(node);
        while (!nodes.isEmpty()) {
            Object currentNode = nodes.pop();
            visited.add(currentNode);
            if (currentNode instanceof EdifCellInstance) {
                EdifCellInstance currentEci = (EdifCellInstance) currentNode;
                for (Object edge : graph.getInputEdges(currentNode)) {
                    if (edge instanceof EdifCellInstanceEdge) {
                        EdifCellInstanceEdge eciEdge = (EdifCellInstanceEdge) edge;
                        foundNets.add(eciEdge.getNet());
                    }
                }
                if (nodes.size() < levels) {
                    for (Object predecessor : graph.getPredecessors(currentEci)) {
                        if (!visited.contains(predecessor)) nodes.push(predecessor);
                    }
                }
            }
        }
        return foundNets.size();
    }

    /**
     * Create a valid set of cuts which break all feedback in the given SCC.
     * This method breaks up the SCC little by little instead of generating the
     * entire cutset at once. Thus, cuts are determined in sets, breaking the
     * problem up when a full cutset is not immediately found. This algorithm
     * will cut the edges with the highest fanout first.
     * 
     * @param graph The top-level graph
     * @param scc A BasicDepthFirstSearchTree object representing an SCC in the
     * top-level graph
     * @param arch The TMRArchitecture, needed to determine good/bad cuts
     * @return A Collection of EdifEdges making up a valid set of cuts to break
     * all feedback in the given SCC
     */
    public static Collection<EdifPortRef> createDecomposeValidCutSetFanout(AbstractEdifGraph graph, SCCDepthFirstSearch sccs, NMRArchitecture arch) {
        Collection<EdifPortRef> globalCutsToMake = new LinkedHashSet<EdifPortRef>();
        Stack<DepthFirstTree> s = new Stack<DepthFirstTree>();
        for (DepthFirstTree scc : sccs.getTrees()) {
            s.push(scc);
        }
        while (!s.empty()) {
            DepthFirstTree subSCCDFSTree = s.pop();
            if (DEBUG) System.out.println("Popping off a graph with " + subSCCDFSTree.getNodes().size() + " nodes from a stack with " + (s.size() + 1) + " graphs");
            BasicGraph sccSubGraph = graph.getSubGraph(subSCCDFSTree.getNodes());
            Object highFanoutNode = null;
            int highFanoutCount = 0;
            boolean hasBadCut = false;
            Collection outputEdges = null;
            for (Iterator i = sccSubGraph.getNodes().iterator(); i.hasNext(); ) {
                Object node = i.next();
                Collection nodeSuccessors = sccSubGraph.getSuccessors(node);
                int numSuccessors = nodeSuccessors.size();
                if (numSuccessors > highFanoutCount) {
                    outputEdges = sccSubGraph.getOutputEdges(node);
                    Collection<EdifPortRefEdge> badEdges = getBadEdges(outputEdges, arch);
                    if (numSuccessors - badEdges.size() > highFanoutCount) {
                        if (badEdges.size() > 0) {
                            outputEdges.removeAll(badEdges);
                            hasBadCut = true;
                        } else {
                            hasBadCut = false;
                        }
                        highFanoutCount = outputEdges.size();
                        highFanoutNode = node;
                    }
                }
            }
            if (DEBUG) System.out.println("\tNode " + highFanoutNode + " has fanout of " + highFanoutCount + " chosen for cutting");
            outputEdges = sccSubGraph.getOutputEdges(highFanoutNode);
            sccSubGraph.removeEdges(outputEdges);
            if (hasBadCut) {
                if (DEBUG) System.out.println("This OUTPUT has a bad cut");
                for (Iterator j = outputEdges.iterator(); j.hasNext(); ) {
                    EdifPortRefEdge edge = (EdifPortRefEdge) j.next();
                    globalCutsToMake.add(edge.getSinkEPR());
                }
            } else {
                globalCutsToMake.add((EdifPortRef) highFanoutNode);
            }
            SCCDepthFirstSearch newSearch = new SCCDepthFirstSearch(sccSubGraph);
            if (newSearch.getTrees().size() == 0) {
                if (DEBUG) System.out.println("\tCutting output removes all feedback in SCC. No graph pushed on stack.");
            } else {
                if (DEBUG) System.out.println("\tCutting output breaks SCC into " + newSearch.getTrees().size() + " sccs");
                for (DepthFirstTree scc : newSearch.getTrees()) {
                    s.push(scc);
                    if (DEBUG) System.out.println("\tPushing subtree with " + scc.getNodes().size() + " nodes and " + scc.getEdges().size() + " edges.");
                }
            }
        }
        return globalCutsToMake;
    }

    /**
     * Performs a cut by choosing outputs of Flip-flops first.
     */
    public static Collection<EdifPortRef> createDecomposeValidCutSetFFFanout(AbstractEdifGraph graph, SCCDepthFirstSearch sccs, NMRArchitecture arch) {
        Collection<EdifPortRef> globalCutsToMake = new LinkedHashSet<EdifPortRef>();
        Stack<DepthFirstTree> s = new Stack<DepthFirstTree>();
        for (DepthFirstTree scc : sccs.getTrees()) {
            s.push(scc);
        }
        while (!s.empty()) {
            DepthFirstTree subSCCDFSTree = s.pop();
            if (DEBUG) System.out.println("Popping off a graph with " + subSCCDFSTree.getNodes().size() + " nodes from a stack with " + (s.size() + 1) + " graphs");
            BasicGraph sccSubGraph = graph.getSmallSubGraph(subSCCDFSTree.getNodes());
            Object highFanoutNode = null;
            int highFanoutCount = 0;
            boolean hasBadCut = false;
            Collection outputEdges = null;
            for (Iterator i = sccSubGraph.getNodes().iterator(); i.hasNext(); ) {
                EdifPortRef epr = (EdifPortRef) i.next();
                EdifCellInstance driving_cell = epr.getCellInstance();
                if (driving_cell == null || !edu.byu.ece.edif.arch.xilinx.XilinxTools.isRegisterCell(driving_cell.getCellType())) continue;
                Collection nodeSuccessors = sccSubGraph.getSuccessors(epr);
                int numSuccessors = nodeSuccessors.size();
                if (numSuccessors > highFanoutCount) {
                    outputEdges = sccSubGraph.getOutputEdges(epr);
                    Collection<EdifPortRefEdge> badEdges = getBadEdges(outputEdges, arch);
                    if (numSuccessors - badEdges.size() > highFanoutCount) {
                        if (badEdges.size() > 0) {
                            outputEdges.removeAll(badEdges);
                            hasBadCut = true;
                        } else {
                            hasBadCut = false;
                        }
                        highFanoutCount = outputEdges.size();
                        highFanoutNode = epr;
                    }
                }
            }
            if (highFanoutNode == null) {
                for (Iterator i = sccSubGraph.getNodes().iterator(); i.hasNext(); ) {
                    Object node = i.next();
                    Collection nodeSuccessors = sccSubGraph.getSuccessors(node);
                    int numSuccessors = nodeSuccessors.size();
                    if (numSuccessors > highFanoutCount) {
                        outputEdges = sccSubGraph.getOutputEdges(node);
                        Collection<EdifPortRefEdge> badEdges = getBadEdges(outputEdges, arch);
                        if (numSuccessors - badEdges.size() > highFanoutCount) {
                            if (badEdges.size() > 0) {
                                outputEdges.removeAll(badEdges);
                                hasBadCut = true;
                            } else {
                                hasBadCut = false;
                            }
                            highFanoutCount = outputEdges.size();
                            highFanoutNode = node;
                        }
                    }
                }
            }
            if (DEBUG) System.out.println("\tNode " + highFanoutNode + " has fanout of " + highFanoutCount + " chosen for cutting");
            outputEdges = sccSubGraph.getOutputEdges(highFanoutNode);
            sccSubGraph.removeEdges(outputEdges);
            if (hasBadCut) {
                if (DEBUG) System.out.println("This OUTPUT has a bad cut");
                for (Iterator j = outputEdges.iterator(); j.hasNext(); ) {
                    EdifPortRefEdge edge = (EdifPortRefEdge) j.next();
                    globalCutsToMake.add(edge.getSinkEPR());
                }
            } else {
                globalCutsToMake.add((EdifPortRef) highFanoutNode);
            }
            SCCDepthFirstSearch newSearch = new SCCDepthFirstSearch(sccSubGraph);
            if (newSearch.getTrees().size() == 0) {
                if (DEBUG) System.out.println("\tCutting output removes all feedback in SCC. No graph pushed on stack.");
            } else {
                if (DEBUG) System.out.println("\tCutting output breaks SCC into " + newSearch.getTrees().size() + " sccs");
                for (DepthFirstTree scc : newSearch.getTrees()) {
                    s.push(scc);
                    if (DEBUG) System.out.println("\tPushing subtree with " + scc.getNodes().size() + " nodes and " + scc.getEdges().size() + " edges.");
                }
            }
        }
        return globalCutsToMake;
    }

    protected static Collection<EdifPortRefEdge> getBadEdges(Collection<EdifPortRefEdge> edges, NMRArchitecture arch) {
        Collection<EdifPortRefEdge> badEdges = new ArrayList<EdifPortRefEdge>();
        for (Iterator j = edges.iterator(); j.hasNext(); ) {
            EdifPortRefEdge edge = (EdifPortRefEdge) j.next();
            EdifNet net = edge.getNet();
            if (arch.isBadCutConnection(edge.getSourceEPR(), edge.getSinkEPR()) || EdifReplicationPropertyReader.isDoNotRestoreOrDoNotDetectLocation(net)) {
                badEdges.add(edge);
            }
        }
        return badEdges;
    }

    /**
     * Create a valid cut set from scratch. Assuming that there is a valid cut
     * set then there exists a depth-first search tree in which all back edges
     * are not tagged as "bad". There are N different depth search trees for an
     * SCC where N is the number of nodes in the tree. Each different tree is
     * created by using a different node as the "start" node.
     * 
     * @deprecated This method does NOT examine all cutset possibilities; use
     * {@link #createDecomposeValidCutSet(AbstractEdifGraph, BasicDepthFirstSearchTree, NMRArchitecture)}
     * instead.
     */
    @Deprecated
    public static Collection createValidCutSet(DirectedGraph graph, BasicDepthFirstSearchTree tree, NMRArchitecture arch) {
        DirectedGraph newGraph = graph.getSubGraph(tree.getNodes());
        for (Iterator i = tree.getNodes().iterator(); i.hasNext(); ) {
            Object root = i.next();
            if (DEBUG) System.out.print("\tFind new cutset with root " + root);
            ArrayList visitOrder = new ArrayList(tree.getNodes());
            visitOrder.remove(root);
            visitOrder.add(0, root);
            DepthFirstSearchForest dfs = new DepthFirstSearchForest(newGraph, visitOrder);
            if (dfs.getTrees().size() != 1) System.out.println("Warning (createValidCutSet): new SCC forest is not size 1: " + dfs.getTrees());
            BasicDepthFirstSearchTree new_tree = (BasicDepthFirstSearchTree) dfs.getTrees().iterator().next();
            Collection backEdges = new_tree.getBackEdges();
            if (!hasBadEdge(backEdges, arch)) {
                if (DEBUG) System.out.println(" - found");
                return backEdges;
            }
            if (DEBUG) {
                System.out.println(" - failed: Back edges:");
                for (Iterator j = backEdges.iterator(); j.hasNext(); ) {
                    Edge edge = (Edge) j.next();
                    System.out.print("\t" + edge);
                    if (EdifCellBadCutGroupings.isBadCutEdge(edge, arch)) System.out.println(" - Bad"); else System.out.println();
                }
            }
        }
        System.out.println("No set of back edges that provides a valid cut");
        System.out.println(tree);
        return new ArrayList(1);
    }

    /**
     * Given a Strongly-Connected Component (SCC), identify a set of edges in
     * the graph that, when cut, will "break" or decompose the SCC into one or
     * more trees, each of which is <i>not</i> an SCC.
     * 
     * @param graph The given SCC
     * @param sccTree A valid topological order of the SCC
     * @return An SCCDepthFirstSearch of the resulting {@link AbstractEdifGraph}.
     * Why does this method require the entire graph? Why isn't the sccTree
     * enough? -James C.
     */
    public static SCCDepthFirstSearch decomposeSCC(AbstractEdifGraph graph, BasicDepthFirstSearchTree sccTree) {
        AbstractEdifGraph smallGraph = (AbstractEdifGraph) graph.getSubGraph(sccTree.getNodes());
        if (DEBUG) System.out.println("Performing SCC decomposition for SCC");
        DepthFirstSearchForest smallSCC = new SCCDepthFirstSearch(smallGraph);
        if (smallSCC.getTrees().size() != 1) throw new RuntimeException("SCC sub graph produces more than one SCC");
        BasicDepthFirstSearchTree smallTree = (BasicDepthFirstSearchTree) smallSCC.getTrees().iterator().next();
        AbstractEdifGraph newGraph = (AbstractEdifGraph) smallSCC.getGraph();
        Edge cutLink = smallTree.getLongestBackEdge();
        System.out.println("Cut edge = " + cutLink);
        if (DEBUG) System.out.println("Edges before cut = " + newGraph.getEdges().size());
        boolean removed = newGraph.removeEdge(cutLink);
        if (DEBUG) {
            System.out.println(" Edges after cut = " + newGraph.getEdges().size());
            System.out.println("Cut graph\r\n" + newGraph);
        }
        if (!removed) throw new RuntimeException("Edge NOT removed");
        SCCDepthFirstSearch cutdfs = new SCCDepthFirstSearch(newGraph);
        return cutdfs;
    }

    /**
     * Finds and returns any "bad" edges in the collection of edges. Uses the
     * TMRArchitecture for determining a bad cut edge.
     * 
     * @param edges
     * @param arch
     * @return A Collection of "bad" Edge objects in the given Collection Edge
     * objects.
     */
    public static Collection<Edge> findBadEdges(Collection<Edge> edges, NMRArchitecture arch) {
        Collection<Edge> badCutEdges = new ArrayList<Edge>();
        for (Edge edge : edges) {
            EdifNet net = null;
            if (edge instanceof EdifCellInstanceEdge) {
                net = ((EdifCellInstanceEdge) edge).getNet();
            }
            if (EdifCellBadCutGroupings.isBadCutEdge(edge, arch) || (net != null && EdifReplicationPropertyReader.isDoNotRestoreOrDoNotDetectLocation(net))) badCutEdges.add(edge);
        }
        return badCutEdges;
    }

    /**
     * Determines whether there are any "bad" edges in the collection of edges.
     * Uses the TMRArchitecture for determining a bad cut.
     * 
     * @param edges
     * @param arch
     * @return
     */
    public static boolean hasBadEdge(Collection edges, NMRArchitecture arch) {
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge edge = (Edge) i.next();
            if (EdifCellBadCutGroupings.isBadCutEdge(edge, arch)) return true;
        }
        return false;
    }

    /**
     * Checks whether the given graph is a strongly-connected component or not.
     * Warning: This method is terribly slow and inefficient. The object of
     * creating the method was to have a sure way of judging a graph as an SCC
     * or not. Thus it was designed to be accurate rather than efficient.
     * 
     * @param graph The potential SCC (a DirectedGraph)
     * @return true if the graph is an SCC, false otherwise
     */
    public static boolean isSCC(DirectedGraph graph) {
        for (Object node : graph.getNodes()) {
            BasicDepthFirstSearchTree dfstree = new BasicDepthFirstSearchTree(null, graph, node);
            System.out.println("DFS for node " + node + ": " + dfstree);
            for (Object checkNode : graph.getNodes()) if (!dfstree.containsNode(checkNode)) {
                if (DEBUG) System.out.println("Warning: Graph is not an SCC. Could not reach node " + checkNode + " from node " + node);
                return false;
            }
        }
        return true;
    }

    public static Collection smallestCutSet(DirectedGraph graph, BasicDepthFirstSearchTree tree, NMRArchitecture arch) {
        DirectedGraph newGraph = graph.getSubGraph(tree.getNodes());
        for (Iterator i = tree.getNodes().iterator(); i.hasNext(); ) {
            Object root = i.next();
            if (DEBUG) System.out.print("\tFind new cutset with root " + root);
            ArrayList visitOrder = new ArrayList(tree.getNodes());
            visitOrder.remove(root);
            visitOrder.add(0, root);
            DepthFirstSearchForest dfs = new DepthFirstSearchForest(newGraph, visitOrder);
            if (dfs.getTrees().size() != 1) System.out.println("Warning (smallestCutSet): new SCC forest is not size 1: " + dfs.getTrees());
            BasicDepthFirstSearchTree new_tree = (BasicDepthFirstSearchTree) dfs.getTrees().iterator().next();
            Collection backEdges = new_tree.getBackEdges();
            if (hasBadEdge(backEdges, arch)) continue;
            if (!hasBadEdge(backEdges, arch)) {
                if (DEBUG) System.out.println(" - found");
                return backEdges;
            }
            if (DEBUG) {
                System.out.println(" - failed: Back edges:");
                for (Iterator j = backEdges.iterator(); j.hasNext(); ) {
                    Edge edge = (Edge) j.next();
                    System.out.print("\t" + edge);
                    if (EdifCellBadCutGroupings.isBadCutEdge(edge, arch)) System.out.println(" - Bad"); else System.out.println();
                }
            }
        }
        System.out.println("No set of back edges that provides a valid cut");
        System.out.println(tree);
        return new ArrayList(1);
    }

    /**
     * Determines how many SCCs can be triplicated using the given capacity
     * object. This method will explore SCC decomposition to include as many
     * SCCs as possible.
     * 
     * @param sccDFS The SCCDepthFirstSearch structure of the edif circuit
     * @param arch The NMRArchitecture
     * @param capacity The capacity object to use for testing SCC inclusion.
     * @param allowSCCDecomposition This flag indicates that SCCs will be
     * decomposed if they don't fit. If false, any SCCs that don't fit are
     * skipped.
     * @param sortType The method of SCC sorting used (3 = topological sorting)
     * @param edgeCutSet An empty List for returning Edge objects that must be
     * cut.
     * @param replicationFacotor The replication factor to use (3=tmr, 2=dwc,
     * etc.)
     * @return true if ALL instances were triplicated, false otherwise
     */
    public static boolean nmrSCCsUsingSCCDecomposition(SCCDepthFirstSearch sccDFS, NMRArchitecture arch, ReplicationUtilizationTracker capacity, boolean allowSCCDecomposition, int sortType, ReplicationType replicationType, boolean override) {
        AbstractEdifGraph graph = (AbstractEdifGraph) sccDFS.getGraph();
        boolean allSCCInstancesTriplicated = true;
        List sccList = new ArrayList();
        List singleNodeSCCs = new ArrayList();
        List sortedSCCList;
        if (sortType == 1) sortedSCCList = sccDFS.getDescendingTreeList(); else if (sortType == 2) sortedSCCList = sccDFS.getAscendingTreeList(); else sortedSCCList = sccDFS.getTopologicallySortedTreeList();
        sccList.addAll(sortedSCCList);
        while (sccList.size() > 0) {
            if (DEBUG) System.out.print("(" + sccList.size() + ") ");
            BasicDepthFirstSearchTree scc = (BasicDepthFirstSearchTree) sccList.get(0);
            sccList.remove(scc);
            Collection sccNodes = scc.getNodes();
            if (DEBUG) {
                System.out.print("Evaluating SCC of size:" + sccNodes.size() + " . . .");
                System.out.println();
                for (Iterator i = sccNodes.iterator(); i.hasNext(); ) {
                    System.out.println("\t" + i.next());
                }
            }
            Collection sccInstances = new ArrayList();
            for (Object node : sccNodes) {
                if (node instanceof EdifCellInstance) {
                    sccInstances.add(node);
                } else if (node instanceof EdifCellInstanceCollection) {
                    sccInstances.addAll((EdifCellInstanceCollection) node);
                } else {
                    if (DEBUG) System.out.println("WARNING: " + "tmrSCCsUsingSCCDecomposition NOT triplicating" + node + "of type " + node.getClass());
                }
            }
            try {
                capacity.addToTrackerAtomic(sccInstances, replicationType, override);
                if (DEBUG) {
                    System.out.println(" SCC fits");
                    System.out.println(capacity.toString());
                }
            } catch (DuplicateNMRRequestException e1) {
                continue;
            } catch (OverutilizationEstimatedStopException e2) {
                allSCCInstancesTriplicated = false;
                if (DEBUG) System.out.println("SCC does not fit");
                if (allowSCCDecomposition) {
                    if (DEBUG) System.out.println("\tDecomposing SCC...");
                    _decomposeSCC(graph, sccList, singleNodeSCCs, scc);
                }
            } catch (OverutilizationHardStopException e3) {
                allSCCInstancesTriplicated = false;
                if (DEBUG) System.out.println("Received HardStopException. Skipping problem instances.");
                try {
                    capacity.addToTrackerAsManyAsPossible(sccInstances, replicationType, override);
                } catch (DuplicateNMRRequestException e4) {
                    continue;
                } catch (OverutilizationEstimatedStopException e5) {
                    allSCCInstancesTriplicated = false;
                    if (DEBUG) System.out.println("SCC does not fit");
                    if (allowSCCDecomposition) {
                        if (DEBUG) System.out.println("\tDecomposing SCC...");
                        _decomposeSCC(graph, sccList, singleNodeSCCs, scc);
                    }
                } catch (OverutilizationHardStopException e6) {
                    throw new EdifRuntimeException("ERROR: Shouldn't get here! " + e6);
                }
            }
        }
        for (Iterator i = singleNodeSCCs.iterator(); i.hasNext(); ) {
            EdifCellInstance instance = (EdifCellInstance) i.next();
            try {
                if (DEBUG) System.out.println("Adding instance: " + instance);
                capacity.addToTracker(instance, replicationType, override);
            } catch (DuplicateNMRRequestException e4) {
                continue;
            } catch (OverutilizationEstimatedStopException e5) {
                allSCCInstancesTriplicated = false;
                if (DEBUG) System.out.println("Received EstimatedStopException " + "in tmrSCCsUsingSCCDecomposition. Halting triplication.");
                break;
            } catch (OverutilizationHardStopException e6) {
                allSCCInstancesTriplicated = false;
                if (DEBUG) System.out.println("\tCan't add instance. No " + instance.getType() + "'s left.");
                continue;
            }
        }
        return allSCCInstancesTriplicated;
    }

    /**
     * Identify a valid set of edges that completely removes feedback in the
     * given DFSTree. Assume that the tree is a SCC.
     * 
     * @param graph
     * @param tree
     * @param arch
     * @return A Collection of
     */
    public static Collection validCutSet(DirectedGraph graph, BasicDepthFirstSearchTree tree, NMRArchitecture arch) {
        Collection backEdges = tree.getBackEdges();
        if (!hasBadEdge(backEdges, arch)) return backEdges;
        return createValidCutSet(graph, tree, arch);
    }

    public static Collection validCutSet(SCCDepthFirstSearch sccs, NMRArchitecture arch) {
        ArrayList cuts = new ArrayList();
        for (Iterator i = sccs.getTrees().iterator(); i.hasNext(); ) {
            BasicDepthFirstSearchTree scc = (BasicDepthFirstSearchTree) i.next();
            cuts.addAll(validCutSet(sccs.getGraph(), scc, arch));
        }
        return cuts;
    }

    protected static boolean DEBUG = false;

    /**
     * Internal helper method for tmrSCCsUsingSCCDecomposition method. This
     * method breaks up an SCC into smaller SCCs and single nodes. These are
     * added to the internal structures used by the parent method.
     * 
     * @param graph The top-level graph which contains this SCC
     * @param sccList The List of SCCs to triplicate
     * @param singleNodeSCCs The List of single nodes to triplicate later
     * @param scc The SCC to decompose
     */
    private static void _decomposeSCC(AbstractEdifGraph graph, List sccList, List singleNodeSCCs, BasicDepthFirstSearchTree scc) {
        List descendingSCCList;
        SCCDepthFirstSearch smallSCCs = decomposeSCC(graph, scc);
        descendingSCCList = smallSCCs.getDescendingTreeList();
        if (DEBUG) {
            for (Iterator j = descendingSCCList.iterator(); j.hasNext(); ) {
                BasicDepthFirstSearchTree smallScc = (BasicDepthFirstSearchTree) j.next();
                System.out.println("\tAdding subtree with " + smallScc.getNodes().size() + " nodes and " + smallScc.getEdges().size() + " edges.");
            }
        }
        sccList.addAll(descendingSCCList);
        if (DEBUG) {
            for (Iterator j = smallSCCs.getSingleNodes().iterator(); j.hasNext(); ) {
                Object node = j.next();
                System.out.println("\tFound single-node subtree: " + node);
            }
        }
        singleNodeSCCs.addAll(smallSCCs.getSingleNodes());
    }
}
