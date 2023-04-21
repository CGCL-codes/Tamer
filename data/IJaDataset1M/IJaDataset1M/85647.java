package backend.tools.algorithms;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractONDEXIterator;
import backend.core.AbstractRelation;
import backend.core.AttributeName;
import backend.core.ConceptGDS;
import backend.core.GDS;
import backend.core.ONDEXView;
import backend.core.RelationGDS;
import backend.core.security.Session;
import backend.filter.optimalpathway.PathwayDefinition;
import backend.filter.optimalpathway.RankRestriction;

/**
 * An Implentation of OptimalPathwaySearch to finding optimal routes from a source to a target based on a predefined pathway
 * @author hindlem
 *
 */
public class OptimalPathwaySearch {

    private static final boolean DEBUG = false;

    private AttributeName forceSingleAttributeValue = null;

    private PriorityBlockingQueue<Runnable> queue;

    private ThreadPoolExecutor tpe;

    private Session s;

    private AbstractONDEXGraph og;

    /**
	 * 
	 * @param s the current session
	 * @param og the ondex graph to preform the search on
	 */
    public OptimalPathwaySearch(Session s, AbstractONDEXGraph og) {
        this.s = s;
        this.og = og;
        queue = new PriorityBlockingQueue<Runnable>(1000);
        int processorsNum = Runtime.getRuntime().availableProcessors();
        tpe = new ThreadPoolExecutor(5 * processorsNum, 5 * processorsNum, Long.MAX_VALUE, TimeUnit.MILLISECONDS, queue);
    }

    private Int2ObjectOpenHashMap<HashMap<PathwayDefinition, Vector<int[]>>> concept2paths = new Int2ObjectOpenHashMap<HashMap<PathwayDefinition, Vector<int[]>>>();

    private IntOpenHashSet visibleRelations;

    private PathwayDefinition[] definitions;

    public void findPathways(PathwayDefinition[] defs) {
        this.definitions = defs;
        visibleRelations = new IntOpenHashSet();
        AbstractONDEXIterator<AbstractConcept> conceptIt = og.getConceptsOfConceptClass(s, definitions[0].getCcs()[0]);
        long concepts = conceptIt.size();
        if (concepts == 0) {
            return;
        }
        Int2ObjectOpenHashMap<ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>>> viewCache = setupViewPreCach();
        int foundConceptPaths = 0;
        System.out.println("Querying for seeds");
        while (conceptIt.hasNext()) {
            AbstractConcept concept = conceptIt.next();
            ONDEXView<AbstractRelation> relationsOnSeed = og.getRelationsOfConcept(s, concept);
            long reations = relationsOnSeed.size();
            relationsOnSeed.close();
            if (reations > 0) {
                HashMap<PathwayDefinition, Vector<int[]>> paths = new HashMap<PathwayDefinition, Vector<int[]>>();
                concept2paths.put(concept.getId(s), paths);
                int found = 0;
                for (int i = 0; i < definitions.length; i++) {
                    if (DEBUG) System.out.println("Checking " + definitions[i].getPathwayName());
                    assert viewCache.get(i) != null : "no precach View for pathway " + i + " name --> " + definitions[i].getPathwayName();
                    Vector<int[]> foundPaths = constructRankedPathwayList(definitions[i], concept, viewCache.get(i), true);
                    found = found + foundPaths.size();
                    paths.put(definitions[i], foundPaths);
                    if (i + 1 <= definitions.length - 1 && found > 0 && definitions[i].getPathwayRank() < definitions[i + 1].getPathwayRank()) {
                        break;
                    }
                }
                if (found > 0) {
                    foundConceptPaths++;
                }
                Iterator<PathwayDefinition> pathDefsIt = paths.keySet().iterator();
                while (pathDefsIt.hasNext()) {
                    PathwayDefinition pathwayDef = pathDefsIt.next();
                    Iterator<int[]> pathIt = paths.get(pathwayDef).iterator();
                    while (pathIt.hasNext()) {
                        int[] path = pathIt.next();
                        for (int rel : path) {
                            visibleRelations.add(rel);
                        }
                    }
                }
            }
        }
        conceptIt.close();
        ObjectIterator<ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>>> it = viewCache.values().iterator();
        while (it.hasNext()) {
            ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>> depthToRts = it.next();
            Iterator<ONDEXView<AbstractRelation>> views = depthToRts.values().iterator();
            while (views.hasNext()) {
                views.next().close();
            }
        }
    }

    /**
	 * Creates the precach object required to constructRankedPathwayList
	 * @param definitions
	 * @return
	 */
    private Int2ObjectOpenHashMap<ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>>> setupViewPreCach() {
        Int2ObjectOpenHashMap<ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>>> pathwayto_depthToRts = new Int2ObjectOpenHashMap<ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>>>();
        System.out.println("Constructing pre Cache");
        for (int i = 0; i < definitions.length; i++) {
            ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>> depthToRts = new ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>>();
            PathwayDefinition definition = definitions[i];
            boolean hasEmptyPoint = false;
            for (int j = 0; j < definition.getDepth() - 1; j++) {
                ONDEXView<AbstractRelation> rtRelationsofRT = og.getRelationsOfRelationType(s, definition.getRts()[j]);
                ONDEXView<AbstractRelation> rtsOfccFrom = og.getRelationsOfConceptClass(s, definition.getCcs()[j]);
                ONDEXView<AbstractRelation> rtsOfccTo = og.getRelationsOfConceptClass(s, definition.getCcs()[j + 1]);
                ONDEXView<AbstractRelation> rtsOfcc = ONDEXView.and(rtsOfccFrom, rtsOfccTo);
                rtsOfccFrom.close();
                rtsOfccTo.close();
                ONDEXView<AbstractRelation> rtsOfRTandCC = ONDEXView.and(rtsOfcc, rtRelationsofRT);
                rtsOfcc.close();
                rtRelationsofRT.close();
                if (rtsOfRTandCC.size() == 0) {
                    hasEmptyPoint = true;
                    Iterator<ONDEXView<AbstractRelation>> views = depthToRts.values().iterator();
                    while (views.hasNext()) {
                        views.next().close();
                    }
                    break;
                }
                depthToRts.put(j + 1, rtsOfRTandCC);
            }
            if (hasEmptyPoint) {
                if (DEBUG) System.out.println(i + ": It has been determined that there are no pathways for " + definitions[i].getPathwayName() + ": it will therefore be excluded from this search");
                definitions = removeItemFromArray(definitions, i);
                i--;
            } else {
                assert pathwayto_depthToRts.get(i) == null : "overwite error";
                pathwayto_depthToRts.put(i, depthToRts);
            }
        }
        assert pathwayto_depthToRts.size() == definitions.length : " Some Views where not initialized views: " + pathwayto_depthToRts.size() + " defs:" + definitions.length;
        System.out.println("Done !! Constructing pre Cache");
        return pathwayto_depthToRts;
    }

    /**
	 * It is assumed that the Final ConceptClass of the Array is also the targetConceptClass
	 * @param definition
	 * @param concept
	 * @param depthToRts 
	 */
    private Vector<int[]> constructRankedPathwayList(PathwayDefinition definition, AbstractConcept concept, ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>> depthToRts, boolean justToVisible) {
        System.out.println("Finding pathways for " + concept.getPID(s));
        Vector<int[]> paths = new Vector<int[]>();
        todoCount = 1;
        threadsFinished = false;
        tpe.execute(new FindNextStep(concept, new int[definition.getDepth() - 1], 1, justToVisible, paths, depthToRts));
        while (true) {
            countLock.lock();
            if (threadsFinished) break;
            try {
                try {
                    if (DEBUG) System.out.println("sleep");
                    finishedSteps.await();
                    if (DEBUG) System.out.println("wake");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                countLock.unlock();
            }
        }
        if (DEBUG) System.out.println("threads running => " + tpe.getActiveCount());
        if (DEBUG) System.out.println("queue size => " + queue.size());
        if (paths.size() == 0) {
            return paths;
        }
        if (DEBUG) System.out.println("\tFound Pathway with " + paths.size() + " routes from source " + concept.getId(s) + " to target...");
        Iterator<int[]> pathItt = paths.iterator();
        while (pathItt.hasNext()) {
            int[] pathway = pathItt.next();
            if (DEBUG) System.out.print("\t");
            for (int rel : pathway) {
                if (rel < 1) {
                    if (DEBUG) System.out.print("null-" + rel);
                } else {
                    int from = og.getRelation(s, rel).getKey(s).getFromID();
                    int to = og.getRelation(s, rel).getKey(s).getToID();
                    if (DEBUG) System.out.print(" " + rel + "(" + from + "-" + to + ")");
                }
            }
            if (DEBUG) System.out.println("\n");
        }
        if (paths.size() < 2) {
            return paths;
        } else {
            if (DEBUG) System.out.println("Filtering for optimal route based on restrictions");
        }
        HashMap<Integer, ArrayList<RankRestriction>> orderRestrictionsByImportance = new HashMap<Integer, ArrayList<RankRestriction>>();
        Iterator<HashSet<RankRestriction>> restrictions = definition.getRestrictionslist().iterator();
        while (restrictions.hasNext()) {
            Iterator<RankRestriction> restrictionIt = restrictions.next().iterator();
            while (restrictionIt.hasNext()) {
                RankRestriction restPart = restrictionIt.next();
                Integer importance = restPart.getRelitiveRankWithinRanks();
                ArrayList<RankRestriction> level = orderRestrictionsByImportance.get(importance);
                if (level == null) {
                    level = new ArrayList<RankRestriction>();
                    orderRestrictionsByImportance.put(importance, level);
                }
                level.add(restPart);
            }
        }
        Integer[] importanceKeys = orderRestrictionsByImportance.keySet().toArray(new Integer[orderRestrictionsByImportance.size()]);
        Arrays.sort(importanceKeys);
        for (Integer importanceLevel : importanceKeys) {
            ArrayList<RankRestriction> resrictionsAtImportanceLevel = orderRestrictionsByImportance.get(importanceLevel);
            Vector<int[]> bestPathsForLevel = new Vector<int[]>();
            Iterator<RankRestriction> restsIt = resrictionsAtImportanceLevel.iterator();
            while (restsIt.hasNext()) {
                RankRestriction restriction = restsIt.next();
                Object bestGdsValue = null;
                Vector<int[]> bestPathsForRestriction = new Vector<int[]>(2);
                int count = 0;
                Iterator<int[]> pathIt = paths.iterator();
                while (pathIt.hasNext()) {
                    count++;
                    int[] pathway = pathIt.next();
                    int relationId = pathway[restriction.getDepth() - 1];
                    AbstractRelation relation = og.getRelation(s, relationId);
                    if (DEBUG) System.out.println(restriction.getFavoredRelationAttributeValue());
                    if (restriction.getFavoredRelationAttributeValue().equals(RankRestriction.EQUIVILENCE)) {
                        AbstractConcept from = relation.getFromConcept(s);
                        GDS gdsFrom = from.getConceptGDS(s, restriction.getAttName());
                        if (gdsFrom == null) {
                            continue;
                        }
                        Object fromValue = gdsFrom.getValue(s);
                        AbstractConcept to = relation.getToConcept(s);
                        GDS gdsTo = to.getConceptGDS(s, restriction.getAttName());
                        if (gdsTo == null) {
                            continue;
                        }
                        Object toValue = gdsTo.getValue(s);
                        if (fromValue.equals(toValue)) {
                            if (DEBUG) System.out.println("Equality present " + importanceLevel + " is equal " + toValue);
                            if (DEBUG) System.out.println(from.getPID(s) + " to " + to.getPID(s));
                            bestPathsForRestriction.add(pathway);
                            continue;
                        } else {
                            if (DEBUG) System.out.println("Equality not present " + importanceLevel + " is not equal " + toValue);
                            continue;
                        }
                    }
                    RelationGDS gds = relation.getRelationGDS(s, restriction.getAttName());
                    if (gds == null) {
                        if (DEBUG) System.out.println(restriction.getAttName() + " gds is null for " + relation.getOfTypeSet(s).getId(s));
                        continue;
                    }
                    Object value = gds.getValue(s);
                    if (value == null) {
                        if (DEBUG) System.out.println(restriction.getAttName() + " is null for " + relation.getOfTypeSet(s).getId(s));
                        continue;
                    } else if (bestGdsValue == null) {
                        bestGdsValue = value;
                        bestPathsForRestriction.add(pathway);
                        continue;
                    }
                    if (DEBUG) System.out.print("\nValue " + value + " No. " + count + " to rt ");
                    if (DEBUG) printIntArray(pathway);
                    if (DEBUG) System.out.print("\n");
                    if (value instanceof Number) {
                        if (DEBUG) System.out.println("Value is number");
                        int difference = getNumberDifference(value, bestGdsValue);
                        if (restriction.getFavoredRelationAttributeValue().equals(RankRestriction.HIGEST_VALUE)) {
                            if (difference > 0) {
                                if (DEBUG) System.out.println("Found Higher Value " + value + " > " + bestGdsValue + " for " + restriction.getAttName());
                                bestGdsValue = value;
                                bestPathsForRestriction.clear();
                                bestPathsForRestriction.add(pathway);
                            } else if (difference == 0) {
                                if (DEBUG) System.out.println("Found Equal Value 4H " + value + " == " + bestGdsValue + " for " + restriction.getAttName());
                                bestPathsForRestriction.add(pathway);
                            } else {
                                if (DEBUG) System.out.println("Ignore Lower Value " + value + " > " + bestGdsValue + " for " + restriction.getAttName());
                            }
                        } else if (restriction.getFavoredRelationAttributeValue() == RankRestriction.LOWEST_VALUE) {
                            if (difference < 0) {
                                if (DEBUG) System.out.println("Found Lower Value " + value + " < " + bestGdsValue + " for " + restriction.getAttName());
                                bestGdsValue = value;
                                bestPathsForRestriction.clear();
                                bestPathsForRestriction.add(pathway);
                            } else if (difference == 0) {
                                if (DEBUG) System.out.println("Found Equal Value 4L " + value + " == " + bestGdsValue + " for " + restriction.getAttName());
                                bestPathsForRestriction.add(pathway);
                            } else {
                                if (DEBUG) System.out.println("Ignore Higher Value " + value + " > " + bestGdsValue + " for " + restriction.getAttName());
                            }
                        }
                        continue;
                    } else {
                        System.err.println("Value is not number" + value.getClass());
                    }
                }
                bestPathsForLevel.addAll(bestPathsForRestriction);
            }
            if (bestPathsForLevel.size() > 0) {
                if (DEBUG) System.out.println("Met Requirements for " + bestPathsForLevel.size() + " at level " + importanceLevel);
                paths = bestPathsForLevel;
                Iterator<int[]> pathIt = paths.iterator();
                while (pathIt.hasNext()) {
                    int[] pathway = pathIt.next();
                    if (DEBUG) System.out.print("\nto rt ");
                    if (DEBUG) printIntArray(pathway);
                    if (DEBUG) System.out.print("\n");
                }
                break;
            }
        }
        System.out.println("Paths found " + paths.size());
        return paths;
    }

    private ReentrantLock ondexGet = new ReentrantLock();

    private ReentrantLock rlForDepthToRts = new ReentrantLock();

    private ReentrantLock gdsIdLock = new ReentrantLock();

    private ReentrantLock countLock = new ReentrantLock();

    private static int todoCount = 1;

    private Condition finishedSteps = countLock.newCondition();

    private boolean threadsFinished = false;

    private ReentrantLock addJobLock = new ReentrantLock();

    /**
	 * Finds the next step in the graph
	 * @author hindlem
	 *
	 */
    private class FindNextStep implements Runnable, Comparable<FindNextStep> {

        private AbstractConcept conceptAtHead;

        private int[] relationsRead;

        private int currentPosition;

        private boolean justToVisible;

        private Vector<int[]> paths;

        private ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>> depthToRts;

        private Object fixedGDSValue = null;

        public FindNextStep(AbstractConcept conceptAtHead, int[] relationsRead, int currentPosition, boolean justToVisible, Vector<int[]> paths, ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>> depthToRts, Object fixedGDSValue) {
            this.conceptAtHead = conceptAtHead;
            this.relationsRead = relationsRead;
            this.currentPosition = currentPosition;
            this.justToVisible = justToVisible;
            this.paths = paths;
            this.depthToRts = depthToRts;
            this.fixedGDSValue = fixedGDSValue;
        }

        public FindNextStep(AbstractConcept conceptAtHead, int[] relationsRead, int currentPosition, boolean justToVisible, Vector<int[]> paths, ConcurrentHashMap<Integer, ONDEXView<AbstractRelation>> depthToRts) {
            this(conceptAtHead, relationsRead, currentPosition, justToVisible, paths, depthToRts, null);
        }

        public void run() {
            ONDEXView<AbstractRelation> relations;
            ondexGet.lock();
            try {
                relations = og.getRelationsOfConcept(s, conceptAtHead);
            } finally {
                ondexGet.unlock();
            }
            if (relations.size() == 0) {
                relations.close();
                decrementCount();
                testFinished();
                return;
            }
            ONDEXView<AbstractRelation> connectedRelationsOfCCOfRT;
            rlForDepthToRts.lock();
            try {
                assert depthToRts.get(currentPosition) != null : currentPosition + " does not have precach View";
                connectedRelationsOfCCOfRT = ONDEXView.and(relations, depthToRts.get(currentPosition));
            } finally {
                rlForDepthToRts.unlock();
                relations.close();
            }
            if (connectedRelationsOfCCOfRT.size() == 0) {
                connectedRelationsOfCCOfRT.close();
                decrementCount();
                testFinished();
                return;
            }
            countLock.lock();
            try {
                todoCount = (int) (todoCount + (connectedRelationsOfCCOfRT.size() - 1));
            } finally {
                countLock.unlock();
            }
            boolean isFirst = true;
            while (connectedRelationsOfCCOfRT.hasNext()) {
                AbstractRelation relation = connectedRelationsOfCCOfRT.next();
                int[] copy;
                if (!isFirst) {
                    copy = new int[relationsRead.length];
                    if (currentPosition - 1 > 0) {
                        System.arraycopy(relationsRead, 0, copy, 0, currentPosition);
                    }
                } else {
                    copy = relationsRead;
                }
                copy[currentPosition - 1] = relation.getId(s).intValue();
                AbstractConcept newHeadConcept;
                AbstractConcept toConcept = relation.getToConcept(s);
                if (!toConcept.equals(conceptAtHead)) {
                    newHeadConcept = toConcept;
                } else {
                    newHeadConcept = relation.getFromConcept(s);
                }
                if (forceSingleAttributeValue != null) {
                    ConceptGDS gds = newHeadConcept.getConceptGDS(s, forceSingleAttributeValue);
                    if (gds != null) {
                        gdsIdLock.lock();
                        try {
                            Object gdsValue = (Object) gds.getValue(s);
                            if (currentPosition - 1 > 0 && fixedGDSValue == null) {
                                fixedGDSValue = gdsValue;
                            } else {
                                if (fixedGDSValue == null && gdsValue != null) {
                                    fixedGDSValue = gdsValue;
                                } else if (gdsValue != null && fixedGDSValue != null && !fixedGDSValue.equals(gdsValue)) {
                                    if (DEBUG) System.out.println("pathway crosses taxID..excluding :" + fixedGDSValue + " to " + gdsValue);
                                    decrementCount();
                                    continue;
                                }
                            }
                        } finally {
                            gdsIdLock.unlock();
                        }
                    }
                }
                if (currentPosition + 1 <= copy.length) {
                    addJobLock.lock();
                    try {
                        tpe.execute(new FindNextStep(newHeadConcept, copy, currentPosition + 1, justToVisible, paths, depthToRts, fixedGDSValue));
                    } finally {
                        addJobLock.unlock();
                    }
                } else {
                    paths.add(copy);
                    decrementCount();
                }
                isFirst = false;
            }
            connectedRelationsOfCCOfRT.close();
            testFinished();
        }

        private void testFinished() {
            countLock.lock();
            try {
                if (todoCount == 0) {
                    threadsFinished = true;
                    finishedSteps.signal();
                }
            } finally {
                countLock.unlock();
            }
        }

        private void decrementCount() {
            countLock.lock();
            try {
                todoCount--;
                if (DEBUG) System.out.println(todoCount);
            } finally {
                countLock.unlock();
            }
        }

        public int compareTo(FindNextStep o) {
            return (currentPosition < o.currentPosition ? -1 : (currentPosition == o.currentPosition ? 0 : 1));
        }
    }

    public void printIntArray(int[] array) {
        for (int num : array) {
            System.out.print(num + ";");
        }
    }

    /**
	 * Makes a .compareTo comparison for all common Number implementions
	 * -1
	 * 1
	 * 0
	 * @param value
	 * @param bestGdsValue
	 * @return
	 */
    private static int getNumberDifference(Object value, Object bestGdsValue) {
        if (value instanceof Byte) {
            return ((Byte) value).compareTo((Byte) bestGdsValue);
        } else if (value instanceof Double) {
            return ((Double) value).compareTo((Double) bestGdsValue);
        } else if (value instanceof Float) {
            return ((Float) value).compareTo((Float) bestGdsValue);
        } else if (value instanceof Integer) {
            return ((Integer) value).compareTo((Integer) bestGdsValue);
        } else if (value instanceof Long) {
            return ((Long) value).compareTo((Long) bestGdsValue);
        } else if (value instanceof Short) {
            return ((Short) value).compareTo((Short) bestGdsValue);
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).compareTo((BigInteger) bestGdsValue);
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo((BigDecimal) bestGdsValue);
        }
        throw new RuntimeException("Incompatible Number for .compareTo " + value.getClass());
    }

    private PathwayDefinition[] removeItemFromArray(PathwayDefinition[] definitions, int i) {
        PathwayDefinition[] newDefinitions = new PathwayDefinition[definitions.length - 1];
        System.out.println("remove " + i);
        for (int j = 0; j < newDefinitions.length; j++) {
            int n;
            if (j >= i) n = j + 1; else n = j;
            newDefinitions[j] = definitions[n];
        }
        return newDefinitions;
    }

    public void finalize() {
        tpe.shutdownNow();
        tpe = null;
        queue.clear();
        queue = null;
    }

    /**
	 * 
	 * @return the attribute for the gds the value of which is guarenteed to be constsiant accross all found pathways
	 */
    public AttributeName getForceSingleAttributeValue() {
        return forceSingleAttributeValue;
    }

    /**
	 * 
	 * @param forceSingleAttributeValue the attribute for the gds the value of which is guarenteed to be constsiant accross all found pathways
	 */
    public void setForceSingleAttributeValue(AttributeName forceSingleAttributeValue) {
        this.forceSingleAttributeValue = forceSingleAttributeValue;
    }

    public IntOpenHashSet getVisibleRelations() {
        return visibleRelations;
    }

    public Int2ObjectOpenHashMap<HashMap<PathwayDefinition, Vector<int[]>>> getConcept2paths() {
        return concept2paths;
    }
}
