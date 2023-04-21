package edu.byu.ece.edif.util.clockdomain;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import edu.byu.ece.edif.arch.ClockingArchitecture;
import edu.byu.ece.edif.arch.xilinx.XilinxClockingArchitecture;
import edu.byu.ece.edif.arch.xilinx.XilinxTools;
import edu.byu.ece.edif.core.EdifCell;
import edu.byu.ece.edif.core.EdifCellInstance;
import edu.byu.ece.edif.core.EdifDesign;
import edu.byu.ece.edif.core.EdifEnvironment;
import edu.byu.ece.edif.core.EdifException;
import edu.byu.ece.edif.core.EdifLibrary;
import edu.byu.ece.edif.core.EdifNet;
import edu.byu.ece.edif.core.EdifPortRef;
import edu.byu.ece.edif.tools.flatten.FlattenedEdifCell;
import edu.byu.ece.edif.tools.flatten.FlattenedEdifCellInstance;
import edu.byu.ece.edif.tools.replicate.nmr.xilinx.XilinxResourceMapper;
import edu.byu.ece.edif.util.graph.EdifCellInstanceEdge;
import edu.byu.ece.edif.util.graph.EdifCellInstanceGraph;
import edu.byu.ece.edif.util.iob.IOBAnalyzer;
import edu.byu.ece.edif.util.iob.XilinxVirtexIOBAnalyzer;
import edu.byu.ece.edif.util.jsap.ClockDomainCommandParser;
import edu.byu.ece.edif.util.parse.ParseException;
import edu.byu.ece.graph.AbstractGraphToDotty;
import edu.byu.ece.graph.Edge;
import edu.byu.ece.graph.dfs.DepthFirstTree;
import edu.byu.ece.graph.dfs.SCCDepthFirstSearch;

/**
 * This class is used to identify clock domains in an EDIF design and
 * then classify nets into one of those domains. Once nets are classified,
 * EdifCellInstances can also be classified into those domains.
 * 
 * The only state contained in this class is the clocking
 * architecture - one instance of this class can classify nets and 
 * instances into clock domains for many EdifCells using the same 
 * architecture.
 * 
 * TODO: Evaluate whether or not it is truly necessary to use 
 * EdifCellInstanceGraph in this class as extensively as is 
 * presently.
 * 
 */
public class ClockDomainParser {

    /**
     * Constructor. 
     * 
     * @param clockingArch
     */
    public ClockDomainParser(ClockingArchitecture clockingArch) {
        _clockingArch = clockingArch;
    }

    /**
     * @deprecated This contains architecture-specific code. 
     * classifyNets should provide the same functionality as is provided here.
     * 
     * Iterate through the design and classify nets into a clock domain. First,
     * clock domains are identified as those nets that drive clock ports of
     * sequential cells. Once clock domains have been identified, sequential
     * cells and their children (up to another sequential cell) are added to
     * that domain.
     * 
     * @return A map whose key is the EdifNet of the clock domain and whose
     * value is a set of EdifNets that belong to that domain. The sets of
     * EdifNets are not necessarily mutually exclusive.
     */
    public static Map<EdifNet, Set<EdifNet>> oldClassifyNets(EdifCell cell, Set<EdifNet> clockNets) {
        LinkedHashMap<EdifNet, Set<EdifNet>> clkToNetMap = new LinkedHashMap<EdifNet, Set<EdifNet>>();
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(cell);
        for (EdifNet clockNet : clockNets) {
            clkToNetMap.put(clockNet, new LinkedHashSet<EdifNet>());
            for (EdifPortRef clockInputEPR : clockNet.getInputPortRefs()) {
                if (XilinxTools.isClockPort(clockInputEPR.getSingleBitPort())) {
                    EdifCellInstance clockedECI = clockInputEPR.getCellInstance();
                    Queue<EdifNet> q = new ConcurrentLinkedQueue<EdifNet>();
                    Set<EdifNet> classifiedNets = new LinkedHashSet<EdifNet>();
                    if (clockedECI != null && clockedECI.getType().toLowerCase().contains(RAM_PREFIX)) {
                        for (EdifPortRef myEpr : graph.getEPRsWhichReferenceInputPortsOfECI(clockedECI)) {
                            if (myEpr.getPort().getName().toLowerCase().contains("clk")) {
                                if (myEpr.getNet().equals(clockNet)) {
                                    String portName = "a";
                                    if (myEpr.getPort().getName().toLowerCase().contains("b")) portName = "b";
                                    for (EdifPortRef outRefs : graph.getEPRsWhichReferenceOutputPortsOfECI(clockedECI)) {
                                        if (outRefs.getPort().getName().toLowerCase().contains(portName) && !classifiedNets.contains(outRefs.getNet())) {
                                            q.add(outRefs.getNet());
                                            classifiedNets.add(outRefs.getNet());
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        for (EdifPortRef myEpr : graph.getEPRsWhichReferenceOutputPortsOfECI(clockedECI)) {
                            if (!classifiedNets.contains(myEpr.getNet())) {
                                q.add(myEpr.getNet());
                                classifiedNets.add(myEpr.getNet());
                            }
                        }
                    }
                    while (!q.isEmpty()) {
                        EdifNet myNet = q.poll();
                        classifiedNets.add(myNet);
                        for (EdifPortRef sinkEPRofClassifiedNet : myNet.getInputPortRefs()) {
                            EdifCellInstance sinkECIofClassifiedNet = sinkEPRofClassifiedNet.getCellInstance();
                            for (EdifPortRef sourceEPRofSinkECI : graph.getEPRsWhichReferenceOutputPortsOfECI(sinkECIofClassifiedNet)) {
                                if (!classifiedNets.contains(sourceEPRofSinkECI.getNet()) && !isSequential(sourceEPRofSinkECI.getCellInstance().getCellType())) {
                                    q.add(sourceEPRofSinkECI.getNet());
                                    classifiedNets.add(sourceEPRofSinkECI.getNet());
                                }
                            }
                        }
                    }
                    clkToNetMap.get(clockNet).addAll(classifiedNets);
                }
            }
        }
        return clkToNetMap;
    }

    /**
     * @deprecated This is architecture-specific code. The same code is located
     * in arch.xilinx.XilinxTools; the method is still here for
     * oldClassifyNets to use.
     * 
     * Return true if the EdifCell ec is sequential (has a clock port).
     * 
     * @param cell The name of the cell to check
     * @return True if this cell is sequential
     */
    public static boolean isSequential(EdifCell cell) {
        String str = cell.getName().toLowerCase();
        if (str.startsWith("fd")) return true;
        if (str.startsWith("ram")) return true;
        if (str.startsWith("clkdll")) return true;
        if (str.startsWith("dcm")) return true;
        if (str.startsWith("sr")) return true;
        return false;
    }

    /**
     * Iterate through the design and classify nets into a clock domain. First,
     * clock domains are identified as those nets that drive clock ports of
     * sequential cells. Once clock domains have been identified, sequential
     * cells and their children (up to another sequential cell) are added to
     * that domain.
     * 
     * @return A map whose key is the EdifNet of the clock domain and whose
     * value is a set of EdifNets that belong to that domain. The sets of
     * EdifNets are not necessarily mutually exclusive.
     */
    public ClockDomainEdifNetClassification classifyNets(EdifCell cell, EdifCellInstanceGraph graph) {
        Set<EdifNet> clockNets = _clockingArch.getClockNets(cell);
        LinkedHashMap<EdifNet, Set<EdifNet>> clkToNetMap = new LinkedHashMap<EdifNet, Set<EdifNet>>();
        for (EdifNet clockNet : clockNets) {
            clkToNetMap.put(clockNet, new LinkedHashSet<EdifNet>());
            for (EdifPortRef clockInputEPR : clockNet.getInputPortRefs()) {
                if (_clockingArch.isClockPort(clockInputEPR.getSingleBitPort())) {
                    EdifCellInstance clockedECI = clockInputEPR.getCellInstance();
                    Queue<EdifNet> q = new ConcurrentLinkedQueue<EdifNet>();
                    Set<EdifNet> classifiedNets = new LinkedHashSet<EdifNet>();
                    if (clockedECI != null && clockedECI.getType().toLowerCase().contains(RAM_PREFIX)) {
                        for (EdifPortRef myEpr : graph.getEPRsWhichReferenceInputPortsOfECI(clockedECI)) {
                            if (myEpr.getPort().getName().toLowerCase().contains("clk")) {
                                if (myEpr.getNet().equals(clockNet)) {
                                    String portName = "a";
                                    if (myEpr.getPort().getName().toLowerCase().contains("b")) portName = "b";
                                    for (EdifPortRef outRefs : graph.getEPRsWhichReferenceOutputPortsOfECI(clockedECI)) {
                                        if (outRefs.getPort().getName().toLowerCase().contains(portName) && !classifiedNets.contains(outRefs.getNet())) {
                                            q.add(outRefs.getNet());
                                            classifiedNets.add(outRefs.getNet());
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        for (EdifPortRef myEpr : graph.getEPRsWhichReferenceOutputPortsOfECI(clockedECI)) {
                            if (!classifiedNets.contains(myEpr.getNet())) {
                                q.add(myEpr.getNet());
                                classifiedNets.add(myEpr.getNet());
                            }
                        }
                    }
                    while (!q.isEmpty()) {
                        EdifNet myNet = q.poll();
                        classifiedNets.add(myNet);
                        for (EdifPortRef sinkEPRofClassifiedNet : myNet.getInputPortRefs()) {
                            EdifCellInstance sinkECIofClassifiedNet = sinkEPRofClassifiedNet.getCellInstance();
                            for (EdifPortRef sourceEPRofSinkECI : graph.getEPRsWhichReferenceOutputPortsOfECI(sinkECIofClassifiedNet)) {
                                if (!classifiedNets.contains(sourceEPRofSinkECI.getNet()) && !_clockingArch.isSequential(sourceEPRofSinkECI.getCellInstance().getCellType())) {
                                    q.add(sourceEPRofSinkECI.getNet());
                                    classifiedNets.add(sourceEPRofSinkECI.getNet());
                                }
                            }
                        }
                    }
                    clkToNetMap.get(clockNet).addAll(classifiedNets);
                }
            }
        }
        LinkedHashSet<EdifNet> noClockNets = new LinkedHashSet<EdifNet>(cell.getNetList());
        for (EdifNet clkNet : clkToNetMap.keySet()) {
            Set<EdifNet> clkNetClassifiedNets = clkToNetMap.get(clkNet);
            noClockNets.removeAll(clkNetClassifiedNets);
        }
        return new ClockDomainEdifNetClassification(noClockNets, clockNets, clkToNetMap);
    }

    public ClockDomainEdifNetClassification classifyNets(EdifCell cell) {
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(cell, true);
        return classifyNets(cell, graph);
    }

    /**
     * Iterate through the design and classify EdifCellInstances into a clock
     * domain. Based on the net domains which should have been previously found,
     * non-sequential cells, like LUTs, belong to the domains of their input
     * nets, and sequential cells belong to the domain of the net driving their
     * clock port(s).
     * 
     * @return A map whose key is the EdifNet of the clock domain and whose
     * value is a set of EdifCellInstances that belong to that domain. The sets
     * of EdifCellInstances are not necessarily mutually exclusive.
     */
    public ClockDomainECIClassification classifyECIs(EdifCell cell, EdifCellInstanceGraph graph, Map<EdifNet, Set<EdifNet>> clkToNetMap) {
        Set<EdifCellInstance> noClockECIs = new LinkedHashSet<EdifCellInstance>(cell.getSubCellList());
        Map<EdifNet, Set<EdifCellInstance>> clkToECIMap = new LinkedHashMap<EdifNet, Set<EdifCellInstance>>();
        for (EdifNet n : clkToNetMap.keySet()) clkToECIMap.put(n, new LinkedHashSet<EdifCellInstance>());
        for (EdifCellInstance eci : cell.getSubCellList()) {
            for (EdifPortRef epr : graph.getEPRsWhichReferenceInputPortsOfECI(eci)) {
                if (_clockingArch.isSequential(eci.getCellType())) {
                    if (_clockingArch.isClockPort(epr.getSingleBitPort())) {
                        if (clkToECIMap.get(epr.getNet()) != null) {
                            clkToECIMap.get(epr.getNet()).add(eci);
                            noClockECIs.remove(eci);
                        }
                    }
                } else {
                    for (EdifNet net : clkToNetMap.keySet()) {
                        if (clkToNetMap.get(net).contains(epr.getNet())) {
                            clkToECIMap.get(net).add(eci);
                            noClockECIs.remove(eci);
                        }
                    }
                }
            }
        }
        return new ClockDomainECIClassification(noClockECIs, clkToECIMap);
    }

    public ClockDomainECIClassification classifyECIs(EdifCell cell, EdifCellInstanceGraph graph) {
        ClockDomainEdifNetClassification characterization = classifyNets(cell, graph);
        Map<EdifNet, Set<EdifNet>> nets = characterization.getClkToNetMap();
        return classifyECIs(cell, graph, nets);
    }

    public ClockDomainECIClassification classifyECIs(EdifCell cell) {
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(cell, true);
        return classifyECIs(cell, graph);
    }

    /**
     * Perform SCC decomposition on the EdifCellInstanceGraph graph.
     * 
     * @param noIOBFB Boolean value indicating whether or not to exclude IOBs as
     * feedback paths.
     * @return SCCDepthFirstSearch data structure
     */
    protected SCCDepthFirstSearch doSCCAnalysis(PrintStream output, EdifCell cell, EdifCellInstanceGraph graph, boolean noIOBFB) {
        SCCDepthFirstSearch scc = new SCCDepthFirstSearch(graph);
        if (noIOBFB) {
            IOBAnalyzer iobAnalyzer = new XilinxVirtexIOBAnalyzer((FlattenedEdifCell) cell, graph);
            Collection<EdifCellInstanceEdge> possibleIOBFeedbackEdges = iobAnalyzer.getIOBFeedbackEdges();
            Collection<Edge> iobFeedbackEdges = new ArrayList<Edge>();
            Collection<DepthFirstTree> sccs = scc.getTrees();
            Collection<Edge> edgesInFeedback = new LinkedHashSet<Edge>();
            for (DepthFirstTree scc1 : sccs) {
                edgesInFeedback.addAll(scc1.getEdges());
            }
            for (EdifCellInstanceEdge possibleIOBFeedbackEdge : possibleIOBFeedbackEdges) {
                if (edgesInFeedback.contains(possibleIOBFeedbackEdge)) iobFeedbackEdges.add(possibleIOBFeedbackEdge);
            }
            if (iobFeedbackEdges.size() > 0) {
                output.println("");
                output.println("The following IOBs were found in feedback structures: " + iobAnalyzer.getFeedbackIOBs());
                output.println("\tThese IOBs will be excluded from feedback analysis.");
                graph.removeEdges(iobFeedbackEdges);
                scc = new SCCDepthFirstSearch(graph);
            }
        }
        return scc;
    }

    /**
     * Find all clock crossing in the design. This is found by iterating over
     * all EdifCellInstances in the design. Clock crossings occur when a
     * sequential cell's inputs are in a different domain than its own.
     */
    public Map<EdifNet, Map<EdifNet, Set<EdifNet>>> getClockCrossings(EdifCell cell, Map<EdifNet, Set<EdifNet>> clkToNetMap) {
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(cell);
        Map<EdifNet, Map<EdifNet, Set<EdifNet>>> clockDomainCrossings = new HashMap<EdifNet, Map<EdifNet, Set<EdifNet>>>();
        for (EdifCellInstance sinkECI : cell.getSubCellList()) {
            EdifNet sinkECIclock = null;
            EdifNet sinkECIclockA = null;
            EdifNet sinkECIclockB = null;
            String resourceType = XilinxResourceMapper.getInstance().getResourceType(sinkECI);
            if (_clockingArch.isSequential(sinkECI.getCellType())) {
                for (EdifPortRef epr : graph.getEPRsWhichReferenceInputPortsOfECI(sinkECI)) {
                    if (_clockingArch.isClockPort(epr.getSingleBitPort())) {
                        if (resourceType != null && resourceType.equals("BRAM")) {
                            if (epr.getPort().getName().toLowerCase().contains("clka")) sinkECIclockA = epr.getNet(); else if (epr.getPort().getName().toLowerCase().contains("clkb")) sinkECIclockB = epr.getNet(); else sinkECIclock = epr.getNet();
                        } else sinkECIclock = epr.getNet();
                    }
                }
                for (EdifPortRef sinkEPR : graph.getEPRsWhichReferenceInputPortsOfECI(sinkECI)) {
                    if (!_clockingArch.isClockPort(sinkEPR.getSingleBitPort())) {
                        String portName = sinkEPR.getPort().getName().toLowerCase();
                        int last = portName.indexOf("_");
                        if (last > 0) portName = portName.substring(0, last);
                        if (resourceType != null && resourceType.equals("BRAM")) {
                            EdifNet clk = null;
                            if (portName.endsWith("a")) clk = sinkECIclockA; else if (portName.endsWith("b")) clk = sinkECIclockB; else clk = sinkECIclock;
                            for (EdifNet clockNet : clkToNetMap.keySet()) {
                                if (clkToNetMap.get(clockNet).contains(sinkEPR.getNet()) && clockNet != clk) {
                                    addClockDomainCrossing(clockDomainCrossings, clockNet, sinkECIclock, sinkEPR.getNet());
                                }
                            }
                        } else {
                            for (EdifNet clockNet : clkToNetMap.keySet()) {
                                if (clkToNetMap.get(clockNet).contains(sinkEPR.getNet()) && clockNet != sinkECIclock) {
                                    addClockDomainCrossing(clockDomainCrossings, clockNet, sinkECIclock, sinkEPR.getNet());
                                }
                            }
                        }
                    }
                }
            }
        }
        return clockDomainCrossings;
    }

    protected static void addClockDomainCrossing(Map<EdifNet, Map<EdifNet, Set<EdifNet>>> crossings, EdifNet sourceClock, EdifNet sinkClock, EdifNet crossingNet) {
        Map<EdifNet, Set<EdifNet>> sourceClockCrossings = crossings.get(sourceClock);
        if (sourceClockCrossings == null) {
            sourceClockCrossings = new HashMap<EdifNet, Set<EdifNet>>();
            crossings.put(sourceClock, sourceClockCrossings);
        }
        Set<EdifNet> sourceSinkCrossingNets = sourceClockCrossings.get(sinkClock);
        if (sourceSinkCrossingNets == null) {
            sourceSinkCrossingNets = new HashSet<EdifNet>();
            sourceClockCrossings.put(sinkClock, sourceSinkCrossingNets);
        }
        sourceSinkCrossingNets.add(crossingNet);
    }

    /**
     * Find all asynchronous resets/presets in the design.
     * 
     * @return A map whose keys are nets that drive asynchronous reset ports and
     * whose values are Sets of EdifCellInstances that have asynchronous
     * resets/presets.
     */
    protected Map<EdifNet, Set<EdifCellInstance>> getAsynchronousResets(EdifCell cell, EdifCellInstanceGraph graph) {
        Map<EdifNet, Set<EdifCellInstance>> retVal = new LinkedHashMap<EdifNet, Set<EdifCellInstance>>();
        for (EdifCellInstance eci : cell.getSubCellList()) {
            if (_clockingArch.hasAsynchronousReset(eci.getCellType())) {
                for (EdifPortRef epr : graph.getEPRsWhichReferenceInputPortsOfECI(eci)) {
                    String name = epr.getPort().getName().toLowerCase();
                    if (_resetPortNames.contains(name)) {
                        EdifNet net = epr.getNet();
                        if (!retVal.containsKey(net)) retVal.put(net, new LinkedHashSet<EdifCellInstance>());
                        retVal.get(net).add(eci);
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * Output summary of SCC to a printstream
     * 
     * @param scc The SCC to analyze
     * @param output The PrintStream to write the summary to
     */
    protected void analyzeSCCs(PrintStream output, SCCDepthFirstSearch scc, Map<EdifNet, Set<EdifCellInstance>> clkToECIMap) {
        output.println("Total sccs in design: " + scc.getTopologicallySortedTreeList().size());
        List<DepthFirstTree> list = scc.getTopologicallySortedTreeList();
        for (DepthFirstTree tree : list) {
            HashSet<EdifNet> clkSet = new LinkedHashSet<EdifNet>();
            for (Object o : tree.getNodes()) {
                EdifCellInstance eci = null;
                try {
                    eci = (EdifCellInstance) o;
                } catch (ClassCastException e) {
                    continue;
                }
                for (EdifNet n : clkToECIMap.keySet()) {
                    if (clkToECIMap.get(n).contains(eci)) clkSet.add(n);
                }
            }
            output.println("  SCC(" + tree.getNodes().size() + ") " + clkSet);
        }
    }

    /**
     * @return a String of the version of this class
     */
    public static String getVersionInfo() {
        return "ClockDomainParser version 0.1";
    }

    /**
     * Void method used in executable to display information about
     * asynchronous cells in the design.
     */
    private void showAsync(PrintStream output, Set<EdifNet> clocks, ClockDomainECIClassification eciClassification, EdifCell cell, EdifCellInstanceGraph graph, boolean async, boolean async_reset, boolean async_reset_cells, boolean show_no_domain) {
        Map<EdifNet, Set<EdifCellInstance>> clkToECIMap = eciClassification.getClkToECIMap();
        if (async) {
            output.println(BAR + "\nClassified Asynchronous Edif Cells\n" + BAR);
            for (EdifNet net1 : clocks) {
                output.println("NET: " + net1);
                Set<String> strSet = new TreeSet<String>();
                for (EdifCellInstance eci : clkToECIMap.get(net1)) {
                    if (!_clockingArch.isSequential(eci.getCellType())) strSet.add(net1 + ": " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
                }
                for (String s : strSet) {
                    output.println("  " + s);
                }
                output.println();
            }
            if (show_no_domain) {
                Set<EdifCellInstance> noClockECIs = eciClassification.getNoClockECIs();
                output.println("No Domain");
                Set<String> strSet = new TreeSet<String>();
                for (EdifCellInstance eci : noClockECIs) {
                    if (!_clockingArch.isSequential(eci.getCellType())) strSet.add("No domain: " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
                }
                for (String s : strSet) {
                    output.println("  " + s);
                }
                output.println();
            }
        }
        if (async_reset) {
            output.println(BAR + "\nAsynchronous Resets\n" + BAR);
            Map<EdifNet, Set<EdifCellInstance>> asynchResetMap = getAsynchronousResets(cell, graph);
            if (asynchResetMap.size() > 0) {
                for (EdifNet net : asynchResetMap.keySet()) {
                    output.println("  " + net);
                }
            } else output.println("None");
            output.println();
        }
        if (async_reset_cells) {
            output.println(BAR + "\nAsynchronous Reset Cells\n" + BAR);
            Map<EdifNet, Set<EdifCellInstance>> asynchResetMap = getAsynchronousResets(cell, graph);
            if (asynchResetMap.size() > 0) {
                for (EdifNet net : asynchResetMap.keySet()) {
                    Set<String> strSet = new TreeSet<String>();
                    for (EdifCellInstance eci : asynchResetMap.get(net)) {
                        strSet.add(net + ": " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
                    }
                    output.println("NET: " + net + " (" + strSet.size() + " cells)");
                    for (String s : strSet) {
                        output.println("  " + s);
                    }
                }
            } else output.println("None");
            output.println();
        }
    }

    /**
     * Void method used in executable to create a dotty graph
     */
    private void showDottyGraph(PrintStream output, Map<EdifNet, Set<EdifCellInstance>> clkToECIMap, EdifCell cell, EdifCellInstanceGraph graph, JSAPResult result) {
        String[] params = result.getStringArray(ClockDomainCommandParser.CREATE_DOTTY_GRAPH);
        int i = 0;
        while (i < params.length) {
            boolean found = false;
            for (EdifCellInstance eci : cell.getSubCellList()) {
                if (((FlattenedEdifCellInstance) eci).getHierarchicalEdifName().toLowerCase().equals(params[i].toLowerCase())) {
                    Stack<EdifCellInstance> myStack = new Stack<EdifCellInstance>();
                    Set<EdifCellInstance> eciSet = new LinkedHashSet<EdifCellInstance>();
                    eciSet.add(eci);
                    for (EdifPortRef epr : eci.getInputEPRs()) {
                        if (epr.getPort().getName().toLowerCase().equals(params[i + 1].toLowerCase())) {
                            myStack.addAll(graph.getPredecessors(eci, epr));
                        }
                    }
                    while (!myStack.isEmpty()) {
                        try {
                            EdifCellInstance e = myStack.pop();
                            if (!eciSet.contains(e) && !_clockingArch.isSequential(e.getCellType())) {
                                myStack.addAll(graph.getPredecessors(e));
                            }
                            eciSet.add(e);
                        } catch (ClassCastException e) {
                        }
                    }
                    String name = eci.getName() + "_" + eciSet.size() + ".dot";
                    AbstractGraphToDotty agtd = new AbstractGraphToDotty();
                    String data = agtd.createDottyBody(graph.getSubGraph(eciSet), clkToECIMap);
                    try {
                        FileWriter fw = new FileWriter(name);
                        fw.write(data);
                        fw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                output.println(params[i] + " not found");
            }
            i += 2;
        }
    }

    /**
     * Void method used in executable to show clock crossings in the 
     * design.
     */
    private void showClockCrossings(PrintStream output, EdifCell cell, Map<EdifNet, Set<EdifNet>> clkToNetMap) {
        output.println(BAR + "\nClock Domain Crossings\n" + BAR);
        Map<EdifNet, Map<EdifNet, Set<EdifNet>>> clockCrossings = getClockCrossings(cell, clkToNetMap);
        for (EdifNet sourceClock : clockCrossings.keySet()) {
            for (EdifNet sinkClock : clockCrossings.get(sourceClock).keySet()) {
                output.println("  Crossings from " + sourceClock + " to " + sinkClock);
                for (EdifNet crossingClock : clockCrossings.get(sourceClock).get(sinkClock)) {
                    output.println("    " + crossingClock);
                }
            }
        }
    }

    /**
     * Void method used in executable to show synchronous cells in the
     * design.
     */
    private void showSynchronous(PrintStream output, Set<EdifNet> clocks, ClockDomainECIClassification eciClassification, boolean show_no_domain) {
        output.println(BAR + "\nClassified Synchronous Edif Cells\n" + BAR);
        Map<EdifNet, Set<EdifCellInstance>> clkToECIMap = eciClassification.getClkToECIMap();
        Set<EdifCellInstance> noClockECIs = eciClassification.getNoClockECIs();
        for (EdifNet net : clocks) {
            output.println("NET: " + net);
            Set<String> strSet = new TreeSet<String>();
            for (EdifCellInstance eci : clkToECIMap.get(net)) {
                if (_clockingArch.isSequential(eci.getCellType())) strSet.add(net + ": " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
            }
            for (String s : strSet) {
                output.println("  " + s);
            }
            output.println();
        }
        if (show_no_domain) {
            output.println("No Domain");
            Set<String> strSet = new TreeSet<String>();
            for (EdifCellInstance eci : noClockECIs) {
                if (_clockingArch.isSequential(eci.getCellType())) strSet.add("No domain: " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
            }
            for (String s : strSet) {
                output.println("  " + s);
            }
            output.println();
        }
    }

    /**
     * Void method used in executables to show EdifCells classified into
     * clock domains.
     */
    private void showCells(PrintStream output, Set<EdifNet> clocks, ClockDomainECIClassification eciClassification, boolean show_no_domain) {
        output.println(BAR + "\nClassified Edif Cells\n" + BAR);
        Map<EdifNet, Set<EdifCellInstance>> clkToECIMap = eciClassification.getClkToECIMap();
        Set<EdifCellInstance> noClockECIs = eciClassification.getNoClockECIs();
        for (EdifNet net : clocks) {
            output.println("NET: " + net + " (" + clkToECIMap.get(net).size() + " cells in domain)");
            Set<String> strSet = new TreeSet<String>();
            for (EdifCellInstance eci : clkToECIMap.get(net)) {
                strSet.add(net + ": " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
            }
            for (String s : strSet) {
                output.println("  " + s);
            }
            output.println();
        }
        if (show_no_domain) {
            output.println("No Domain (" + noClockECIs.size() + " cells)");
            Set<String> strSet = new TreeSet<String>();
            for (EdifCellInstance eci : noClockECIs) {
                strSet.add("No domain: " + ((FlattenedEdifCellInstance) eci).getHierarchicalEdifName() + " (" + eci.getCellType().getName() + ")");
            }
            for (String s : strSet) {
                output.println("  " + s);
            }
            output.println();
        }
    }

    /**
     * Void method used in executables to show EdifNets classified into
     * clock domains.
     */
    private void showNets(PrintStream output, Set<EdifNet> clocks, ClockDomainEdifNetClassification netClassification, boolean show_no_domain) {
        Map<EdifNet, Set<EdifNet>> clkToNetMap = netClassification.getClkToNetMap();
        Set<EdifNet> noClockNets = netClassification.getNoClockNets();
        output.println(BAR + "\nClassified Nets\n" + BAR);
        for (EdifNet net : clocks) {
            output.println("NET: " + net + " (" + clkToNetMap.get(net).size() + " nets in domain)");
            Set<String> strSet = new TreeSet<String>();
            for (EdifNet n : clkToNetMap.get(net)) {
                Set<String> driverSet = new TreeSet<String>();
                for (EdifPortRef epr : n.getOutputPortRefs()) {
                    if (epr.getCellInstance() != null) driverSet.add(((FlattenedEdifCellInstance) epr.getCellInstance()).getHierarchicalEdifName() + " (" + epr.getCellInstance().getType() + ")");
                }
                strSet.add(net + ": " + n.getName() + " driven by " + driverSet);
            }
            for (String s : strSet) {
                output.println("  " + s);
            }
            output.println();
        }
        if (show_no_domain) {
            output.println("No Domain (" + noClockNets.size() + " nets)");
            Set<String> strSet = new TreeSet<String>();
            for (EdifNet n : noClockNets) {
                Set<String> driverSet = new TreeSet<String>();
                for (EdifPortRef epr : n.getOutputPortRefs()) {
                    if (epr.getCellInstance() != null) driverSet.add(((FlattenedEdifCellInstance) epr.getCellInstance()).getHierarchicalEdifName() + " (" + epr.getCellInstance().getType() + ")");
                }
                strSet.add("No domain: " + n.getName() + " driven by " + driverSet);
            }
            for (String s : strSet) {
                output.println("  " + s);
            }
            output.println();
        }
    }

    /**
     * Void method used in executables to show gated clocks in the design.
     */
    private void show_gated_clocks(PrintStream output, Map<EdifNet, Set<EdifNet>> clkToNetMap) {
        boolean found = false;
        output.println(BAR + "\nGated clocks\n" + BAR);
        for (EdifNet net : clkToNetMap.keySet()) {
            String str = "";
            for (EdifPortRef epr : net.getOutputPortRefs()) {
                if (!_clockingArch.isClockDriver(epr.getCellInstance().getCellType())) {
                    str += ((FlattenedEdifCellInstance) epr.getCellInstance()).getHierarchicalEdifName() + " (" + epr.getCellInstance().getCellType() + ")";
                    found = true;
                }
            }
            if (!str.equals("")) output.println("  " + net + ": driven by " + str);
        }
        if (!found) output.println("  No gated clocks");
        output.println();
    }

    /**
     * Method used as the heart of executables using ClockDomainParser code
     * (JEdifClockDomain and main() of this class).
     */
    public void exec(PrintStream output, JSAPResult result, EdifCell cell) {
        String[] clocksToShow = result.getStringArray(ClockDomainCommandParser.DOMAIN);
        Set<EdifNet> clocks = null;
        output.print("Generating EdifCellInstanceGraph Graph...");
        EdifCellInstanceGraph graph = new EdifCellInstanceGraph(cell, true);
        output.println("done");
        ClockDomainEdifNetClassification edifNetClassification = classifyNets(cell, graph);
        Map<EdifNet, Set<EdifNet>> clkToNetMap = edifNetClassification.getClkToNetMap();
        if (clocksToShow[0].toLowerCase().equals(ALL)) clocks = new LinkedHashSet<EdifNet>(clkToNetMap.keySet()); else {
            clocks = new LinkedHashSet<EdifNet>();
            for (int i = 0; i < clocksToShow.length; i++) {
                boolean match = false;
                for (EdifNet net : clkToNetMap.keySet()) {
                    if (net.getName().toLowerCase().equals(clocksToShow[i].toLowerCase())) {
                        match = true;
                        clocks.add(net);
                        break;
                    }
                }
                if (!match) output.println("\n*** No match for " + clocksToShow[i]);
            }
        }
        output.println(BAR + "\nDesign clocks\n" + BAR);
        for (EdifNet net : clkToNetMap.keySet()) output.println("  " + net);
        output.println();
        boolean show_no_domain = result.getBoolean(ClockDomainCommandParser.SHOW_NO_DOMAIN);
        if (result.getBoolean(ClockDomainCommandParser.SHOW_GATED_CLOCKS)) {
            show_gated_clocks(output, edifNetClassification.getClkToNetMap());
        }
        if (result.getBoolean(ClockDomainCommandParser.SHOW_NETS)) {
            showNets(output, clocks, edifNetClassification, show_no_domain);
        }
        ClockDomainECIClassification eciClassification = classifyECIs(cell, graph, clkToNetMap);
        Map<EdifNet, Set<EdifCellInstance>> clkToECIMap = eciClassification.getClkToECIMap();
        if (result.getBoolean(ClockDomainCommandParser.SHOW_CELLS)) {
            showCells(output, clocks, eciClassification, show_no_domain);
        }
        if (result.getBoolean(ClockDomainCommandParser.SHOW_SYNCHRONOUS)) {
            showSynchronous(output, clocks, eciClassification, show_no_domain);
        }
        boolean async, async_reset, async_reset_cells;
        async = result.getBoolean(ClockDomainCommandParser.SHOW_ASYNCHRONOUS);
        async_reset = result.getBoolean(ClockDomainCommandParser.SHOW_ASYNCHRONOUS_RESETS);
        async_reset_cells = result.getBoolean(ClockDomainCommandParser.SHOW_ASYNCHRONOUS_RESET_CELLS);
        showAsync(output, clocks, eciClassification, cell, graph, async, async_reset, async_reset_cells, show_no_domain);
        if (result.contains(ClockDomainCommandParser.SHOW_CLOCK_CROSSINGS)) {
            output.println("Showing crossings");
            showClockCrossings(output, cell, clkToNetMap);
        } else output.println("Not Showing crossings");
        if (result.getBoolean(ClockDomainCommandParser.DO_SCC_ANALYSIS)) {
            SCCDepthFirstSearch scc = doSCCAnalysis(output, cell, graph, result.getBoolean(ClockDomainCommandParser.NO_IOB_FEEDBACK));
            output.println(BAR + "\nSCC Analysis\n" + BAR);
            analyzeSCCs(output, scc, clkToECIMap);
            output.println();
        }
        if (result.contains(ClockDomainCommandParser.CREATE_DOTTY_GRAPH)) {
            showDottyGraph(output, clkToECIMap, cell, graph, result);
        }
    }

    public static void main(String[] args) throws ParseException, FileNotFoundException, EdifException {
        PrintStream output = System.out;
        JSAP parser = new ClockDomainCommandParser();
        JSAPResult result = parser.parse(args);
        EdifLibrary xilinxLib = edu.byu.ece.edif.arch.xilinx.XilinxLibrary.library;
        ClockingArchitecture clockingArch = new XilinxClockingArchitecture();
        ClockDomainParser cda = new ClockDomainParser(clockingArch);
        String dirs[] = result.getStringArray(ClockDomainCommandParser.DIR);
        if (dirs == null) dirs = new String[0];
        String files[] = result.getStringArray(ClockDomainCommandParser.FILE);
        if (files == null) files = new String[0];
        EdifEnvironment edif_file = edu.byu.ece.edif.util.merge.EdifMergeParser.parseAndMergeEdif(result.getString(ClockDomainCommandParser.INPUT_FILE), Arrays.asList(dirs), Arrays.asList(files), xilinxLib);
        FlattenedEdifCell flatCell = new FlattenedEdifCell(edif_file.getTopCell());
        EdifCell cell = flatCell.getOriginalCell();
        EdifEnvironment top = flatCell.getLibrary().getLibraryManager().getEdifEnvironment();
        EdifDesign new_design = new EdifDesign("ROOT");
        new_design.setTopCellInstance(new EdifCellInstance("flat_" + cell.getName(), null, flatCell));
        top.setTopDesign(new_design);
        cell.getLibrary().deleteCell(cell, true);
        top.getLibraryManager().pruneNonReferencedCells();
        if (result.contains(ClockDomainCommandParser.OUTPUT_FILE)) output = new PrintStream(result.getString(ClockDomainCommandParser.OUTPUT_FILE));
        cda.exec(output, result, flatCell);
    }

    /**
     * Object used to define architecture-specific clocking structure, etc.
     */
    protected ClockingArchitecture _clockingArch;

    protected static final String BAR = "-----------------------------------";

    protected static final String ALL = "all";

    protected static final String RAM_PREFIX = "ramb4";

    private static final Set<String> _resetPortNames;

    static {
        _resetPortNames = new TreeSet<String>();
        _resetPortNames.add("clr");
        _resetPortNames.add("pre");
    }
}
