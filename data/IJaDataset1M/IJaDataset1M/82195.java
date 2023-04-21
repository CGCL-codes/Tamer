package edu.byu.cs.guided.search.heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.Comparator;
import edu.byu.cs.analysis.StaticAnalyzer;
import edu.byu.cs.trace.sets.TraceSet;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.Config.Exception;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.VMState;
import gov.nasa.jpf.search.Search;

public class GuidedDFSearch extends Search {

    protected int numberNewChildren = 0;

    protected int initHeuristicValue;

    protected int queueLimit;

    int maxHeuristic = Integer.MIN_VALUE;

    int minHeuristic = Integer.MAX_VALUE;

    int heuristicTotal = 0;

    int heuristicCount = 0;

    protected TreeSet<MetaHeuristicState> localQueue;

    protected Map<Integer, TreeSet<MetaHeuristicState>> multiLevelQueue;

    protected TreeSet<MetaHeuristicState> otherQueue;

    protected VMState init_state;

    protected MetaHeuristicState currentState;

    protected MetaHeuristicState newState;

    protected Random rand;

    protected MetaHeuristic metaHeuristic;

    protected PFSMHeuristic pfsmHeuristic;

    protected int initMetaHeuristicValue, parentMetaValue;

    protected int lowestMetaValue;

    protected String[] inputTraceFile;

    protected int seqNumber = -1;

    protected int[] refinementIndices;

    protected String sourceLoc;

    protected String sourceFolderLoc;

    protected String classFilesLoc;

    protected Config config;

    protected boolean printToXML = false;

    public GuidedDFSearch(Config config, JVM vm) throws Exception {
        super(config, vm);
        this.config = config;
        this.initializeConfigParameterValue();
        otherQueue = new TreeSet<MetaHeuristicState>(getMetaComparator(config));
        localQueue = new TreeSet<MetaHeuristicState>(getMetaComparator(config));
        multiLevelQueue = new HashMap<Integer, TreeSet<MetaHeuristicState>>();
        rand = new Random();
        String[] args = new String[2];
        args[0] = classFilesLoc;
        StaticAnalyzer.intializeClasses(args);
        StaticAnalyzer.initializeSuperAndSubClasses();
        if (inputTraceFile == null) {
            metaHeuristic = new MetaHeuristic(vm.getMainClassName(), sourceLoc, sourceFolderLoc);
        } else {
            metaHeuristic = new MetaHeuristic(inputTraceFile);
        }
        metaHeuristic.initializeMetaHeuristicAnalysis(config, refinementIndices);
        metaHeuristic.pickTraceSet(seqNumber);
        initMetaHeuristicValue = metaHeuristic.traceSet.numOftotalLocations();
        lowestMetaValue = initMetaHeuristicValue;
        parentMetaValue = initMetaHeuristicValue;
        for (int i = 0; i <= initMetaHeuristicValue; i++) {
            try {
                multiLevelQueue.put(i, new TreeSet<MetaHeuristicState>(getMetaComparator(config)));
            } catch (Exception e) {
                System.err.println("Error initializing the comparator class");
                e.printStackTrace();
            }
        }
        DebugGuided.printIntegerVal(initMetaHeuristicValue, "initial meta heuristic value ");
    }

    private void initializeConfigParameterValue() {
        classFilesLoc = config.getString("search.heuristic.example", null);
        if (classFilesLoc == null) {
            System.err.println("The location for class files is empty");
            System.exit(1);
        }
        String xmlPrint = config.getString("print_to_xml", null);
        if (xmlPrint != null) {
            printToXML = true;
        }
        queueLimit = config.getInt("search.heuristic.queue_limit", -1);
        initHeuristicValue = config.getInt("search.heuristic.initial_value", 0);
        inputTraceFile = config.getStringArray("search.heuristic.meta.inputTraceFile");
        if (inputTraceFile == null) {
            sourceLoc = config.getString("search.heuristic.meta.srcloc");
            sourceFolderLoc = config.getString("search.heuristic.meta.srcFolderLoc");
        }
        seqNumber = config.getInt("search.heuristic.seqNum", -1);
        try {
            refinementIndices = config.getIntArray("search.heuristic.refinement");
        } catch (Exception e) {
            System.err.println("the refinement index specified is not a int");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void resetSearch() {
        MetaHeuristic.refinement = false;
        multiLevelQueue.clear();
        DebugGuided.printString("Cleared the multi level queue");
        localQueue.clear();
        otherQueue.clear();
        initMetaHeuristicValue = metaHeuristic.traceSet.numOftotalLocations();
        lowestMetaValue = initMetaHeuristicValue;
        parentMetaValue = initMetaHeuristicValue;
        for (int i = 0; i <= initMetaHeuristicValue; i++) {
            try {
                multiLevelQueue.put(i, new TreeSet<MetaHeuristicState>(getMetaComparator(config)));
            } catch (Exception e) {
                System.err.println("Could not put in the queue");
                e.printStackTrace();
                System.exit(1);
            }
        }
        DebugGuided.printIntegerVal(initMetaHeuristicValue, "meta heuristic after refinement ");
        vm.restoreState(init_state);
        vm.resetNextCG();
        startSearch();
    }

    @SuppressWarnings("unchecked")
    protected static Comparator<MetaHeuristicState> getMetaComparator(Config config) throws Config.Exception {
        return config.getEssentialInstance("search.heuristic.comparator.class", (Class<Comparator<MetaHeuristicState>>) (Class<?>) Comparator.class);
    }

    public MetaHeuristicState getNew() {
        return newState;
    }

    public MetaHeuristicState getOld() {
        return currentState;
    }

    public MetaHeuristic getMetaHeuristicInstance() {
        return metaHeuristic;
    }

    protected void generateChildren(int maxDepth) {
        numberNewChildren = 0;
        int primaryVal;
        while (!done) {
            if (!forward() || hasPropertyTermination()) {
                notifyStateProcessed();
                return;
            }
            depth++;
            TraceSet nextTraceSet = metaHeuristic.computeMetaHeuristicValue(currentState.getTraceSet());
            if (printToXML && MetaHeuristic.refinement) {
                StaticClass.setMetaValue(metaHeuristic.getMetaHeuristicValue(nextTraceSet));
            }
            if (MetaHeuristic.refinement || MetaHeuristic.moveToNextTrace) {
                DebugGuided.printString("The refinement variable has been set" + "or need to move to the next trace");
                notifyStateProcessed();
                return;
            }
            primaryVal = metaHeuristic.getMetaHeuristicValue(nextTraceSet);
            notifyStateAdvanced();
            if (!isEndState && !isIgnoredState) {
                if (depth >= maxDepth) {
                    notifySearchConstraintHit(DEPTH_CONSTRAINT);
                } else {
                    ArrayList<Integer> otherHeuristics = new ArrayList<Integer>();
                    otherHeuristics.add(metaHeuristic.computeSecondaryDistanceHeuristicValue(pfsmHeuristic, nextTraceSet));
                    int randVal = rand.nextInt((int) Math.pow(2.0, 32.0));
                    newState = new MetaHeuristicState(vm, primaryVal, randVal, otherHeuristics, nextTraceSet);
                    notifyStateStored();
                    localQueue.add(newState);
                    numberNewChildren++;
                }
            }
            backtrack();
            depth--;
            notifyStateBacktracked();
        }
    }

    public int getQueueSize() {
        return otherQueue.size();
    }

    private void expandState() {
        currentState = otherQueue.first();
        boolean removed = otherQueue.remove(currentState);
        assert removed : "Inconsistency in heuristic queue.";
        vm.restoreState(currentState.getVMState());
        parentMetaValue = currentState.getPrimaryHeuristicVal();
        if (parentMetaValue < lowestMetaValue) {
            lowestMetaValue = parentMetaValue;
        }
        depth = vm.getPathLength();
        notifyStateRestored();
        DebugGuided.printString(" ********the first level value is " + currentState.getPrimaryHeuristicVal() + "  \t The second level is " + currentState.getOtherHeuristicVals().get(0));
    }

    public void search() {
        init_state = vm.getState();
        pfsmHeuristic = new PFSMHeuristic(vm);
        notifySearchStarted();
        startSearch();
        notifySearchFinished();
    }

    public void startSearch() {
        currentState = getInitialMetaHeuristicState();
        notifyStateStored();
        int maxDepth = getMaxSearchDepth();
        done = false;
        generateChildren(maxDepth);
        updateQueue();
        while ((otherQueue.size() != 0) && !done) {
            expandState();
            generateChildren(maxDepth);
            if (MetaHeuristic.refinement) {
                resetSearch();
            } else if (MetaHeuristic.moveToNextTrace) {
                return;
            }
            updateQueue();
            if (otherQueue.size() <= 0) {
                pickBacktrackState();
            }
        }
    }

    private MetaHeuristicState getInitialMetaHeuristicState() {
        int randVal = rand.nextInt((int) Math.pow(2.0, 32.0));
        ArrayList<Integer> otherHeuristics = new ArrayList<Integer>();
        otherHeuristics.add(initHeuristicValue);
        TraceSet traceSet = null;
        try {
            traceSet = (TraceSet) metaHeuristic.traceSet.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Could not clone the initial trace set");
            e.printStackTrace();
            System.exit(1);
        }
        MetaHeuristicState hState = new MetaHeuristicState(vm, metaHeuristic.getMetaHeuristicValue(traceSet), randVal, otherHeuristics, traceSet);
        return hState;
    }

    private void updateQueue() {
        if (localQueue.size() > 0) {
            if (localQueue.first().getPrimaryHeuristicVal() > 0) {
                if (localQueue.first().getPrimaryHeuristicVal() < parentMetaValue) {
                    otherQueue.add(localQueue.first());
                    localQueue.remove(localQueue.first());
                } else if (localQueue.first().getPrimaryHeuristicVal() == parentMetaValue) {
                    if (localQueue.first().getPriority() < Integer.MAX_VALUE) {
                        otherQueue.add(localQueue.first());
                        localQueue.remove(localQueue.first());
                    }
                }
            }
            Iterator<MetaHeuristicState> it = localQueue.iterator();
            while (it.hasNext()) {
                MetaHeuristicState tmpState = (MetaHeuristicState) it.next();
                TreeSet<MetaHeuristicState> tmpQueue = multiLevelQueue.get(tmpState.getPrimaryHeuristicVal());
                tmpQueue.add(tmpState);
                multiLevelQueue.put(tmpState.getPrimaryHeuristicVal(), tmpQueue);
            }
            localQueue.clear();
            if (queueLimit > 0) {
                for (int i = 0; i <= initMetaHeuristicValue; i++) {
                    if (multiLevelQueue.containsKey(i)) {
                        TreeSet<MetaHeuristicState> tmp = multiLevelQueue.get(i);
                        while (tmp.size() > queueLimit) {
                            boolean removed = tmp.remove(tmp.first());
                            assert removed : "Inconsistency in heruistic queue";
                            notifySearchConstraintHit(QUEUE_CONSTRAINT);
                        }
                    }
                }
            }
        }
    }

    private void pickBacktrackState() {
        int targetMetaValue;
        int randV = rand.nextInt(10);
        if (parentMetaValue == lowestMetaValue) {
            if (randV < 8) {
                targetMetaValue = lowestMetaValue + 1;
            } else {
                targetMetaValue = lowestMetaValue;
            }
        } else {
            if (randV >= 9 && parentMetaValue < initMetaHeuristicValue) {
                targetMetaValue = parentMetaValue + 1;
            } else {
                targetMetaValue = parentMetaValue;
            }
        }
        if (multiLevelQueue.containsKey(targetMetaValue) && multiLevelQueue.get(targetMetaValue).size() > 0) {
            TreeSet<MetaHeuristicState> tmpQueue = multiLevelQueue.get(targetMetaValue);
            otherQueue.add(tmpQueue.first());
            tmpQueue.remove(tmpQueue.first());
            multiLevelQueue.put(targetMetaValue, tmpQueue);
        } else {
            for (int i = multiLevelQueue.size(); i >= 0; i--) {
                if (multiLevelQueue.containsKey(i) && multiLevelQueue.get(i).size() > 0) {
                    TreeSet<MetaHeuristicState> tmpQueue = multiLevelQueue.get(i);
                    otherQueue.add(tmpQueue.first());
                    tmpQueue.remove(tmpQueue.first());
                    multiLevelQueue.put(i, tmpQueue);
                    break;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void printState() {
        ThreadInfo[] threadInfo = vm.getLiveThreads();
        for (int threadInfoIndex = 0; threadInfoIndex < threadInfo.length; threadInfoIndex++) {
            if (threadInfo[threadInfoIndex].getPC() != null && threadInfo[threadInfoIndex].isRunnable()) {
                if (threadInfo[threadInfoIndex] == vm.getCurrentThread()) {
                    System.out.print("current:");
                }
                System.out.print("\t\tIndex: " + threadInfo[threadInfoIndex].getIndex() + "\t");
                System.out.print("CName: " + threadInfo[threadInfoIndex].getPC().getMethodInfo().getClassName() + "\t");
                System.out.print("MName: " + threadInfo[threadInfoIndex].getPC().getMethodInfo().getName() + "\t");
                System.out.println("L: " + threadInfo[threadInfoIndex].getPC().getPosition());
            } else {
                System.out.println("Index: " + threadInfo[threadInfoIndex].getIndex());
            }
        }
    }

    @SuppressWarnings("unused")
    private void printSuccessors() {
        System.out.println("\t\tSuccessors ***********************************");
        printState();
    }
}
