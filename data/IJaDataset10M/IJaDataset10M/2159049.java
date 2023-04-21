package JaCoP.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import JaCoP.core.IntDomain;
import JaCoP.core.IntVar;
import JaCoP.core.IntervalDomain;
import JaCoP.core.Store;
import JaCoP.core.TimeStamp;
import JaCoP.core.ValueEnumeration;
import JaCoP.core.Var;
import JaCoP.util.SimpleArrayList;
import JaCoP.util.SimpleHashSet;

/**
 * Alldistinct constraint assures that all FDVs have different values.
 * 
 * This implementation is based on Regin paper. It uses slightly modified
 * Hopcroft-Karp algorithm to compute maximum matching. The value graph is
 * analysed and Tarjan algorithm for finding strongly connected components is
 * used. Maximum matching and Value Graph is stored as TimeStamp Mutable
 * variables to minimize recomputation. Value graph is expensive in terms of
 * memory usage. Use this constraint with care. One variable with domain
 * 0..1000000 will make it use few MB already and kill the efficiency.
 * 
 * @author Radoslaw Szymanek and Krzysztof Kuchcinski
 * @version 3.0
 */
public class Alldistinct extends Constraint {

    static final boolean debugAll = false;

    static final boolean debugPruning = false;

    static int idNumber = 1;

    boolean backtrackOccured = true;

    /**
	 * It counts the number of executions of the consistency function. 
	 */
    public int consistencyChecks = 0;

    /**
	 * It computes how many times did consistency execution has been 
	 * re-executed due to narrowing event at the end of the consistency 
	 * function.
	 */
    public int fullConsistencyPassesWithNarrowingEvent = 0;

    LinkedHashSet<IntVar> freeVariables = new LinkedHashSet<IntVar>();

    protected ArrayList<IntVar> freeVariablesAtFailure = new ArrayList<IntVar>();

    boolean impositionFailure = false;

    IdentityHashMap<IntVar, TimeStamp<Integer>> matching;

    boolean maximumMatchingNotRecomputed = true;

    int n;

    TimeStamp<Integer> nStamp;

    boolean permutationConsistency = true;

    Integer potentialFreeValues[];

    IdentityHashMap<IntVar, Integer> scc;

    IdentityHashMap<IntVar, TimeStamp<Integer>> sccStamp;

    TimeStamp<Integer> stampNotGroundedVariables;

    TimeStamp<Integer> stampReachability;

    HashMap<Integer, TimeStamp<Integer>> stamps;

    TimeStamp<Integer> stampValues;

    HashMap<Integer, Integer> valueIndex;

    HashMap<Integer, SimpleArrayList<IntVar>> valueMapVariable;

    LinkedHashSet<IntVar> variableQueue = new LinkedHashSet<IntVar>();

    int vn;

    /**
	 * It specifies all variables which have to have different values.
	 */
    public IntVar[] list;

    /**
	 * It specifies the arguments required to be saved by an XML format as well as 
	 * the constructor being called to recreate an object from an XML format.
	 */
    public static String[] xmlAttributes = { "list" };

    /**
	 * It constructs an alldistinct constraint. 
	 * @param list an array of variables.
	 */
    public Alldistinct(IntVar[] list) {
        queueIndex = 0;
        numberId = idNumber++;
        this.list = new IntVar[list.length];
        for (int i = 0; i < list.length; i++) this.list[i] = list[i];
        valueMapVariable = new HashMap<Integer, SimpleArrayList<IntVar>>();
        stamps = new HashMap<Integer, TimeStamp<Integer>>();
        matching = new IdentityHashMap<IntVar, TimeStamp<Integer>>();
        sccStamp = new IdentityHashMap<IntVar, TimeStamp<Integer>>();
        IntDomain sum = new IntervalDomain(5);
        for (int i = 0; i < this.list.length; i++) sum.addDom(this.list[i].dom());
        int value = 0;
        SimpleArrayList<IntVar> currentSimpleArrayList = null;
        potentialFreeValues = new Integer[sum.getSize()];
        valueIndex = new HashMap<Integer, Integer>(sum.getSize(), 0.5f);
        int m = 0;
        for (ValueEnumeration enumer = sum.valueEnumeration(); enumer.hasMoreElements(); ) {
            value = enumer.nextElement();
            Integer valueInteger = value;
            potentialFreeValues[m] = valueInteger;
            valueIndex.put(valueInteger, m);
            m++;
            currentSimpleArrayList = new SimpleArrayList<IntVar>();
            for (int i = 0; i < this.list.length; i++) if (this.list[i].domain.contains(value)) currentSimpleArrayList.add(this.list[i]);
            valueMapVariable.put(valueInteger, currentSimpleArrayList);
        }
    }

    /**
	 * It constructs an alldistinct constraint.
	 * @param list arraylist of variables.
	 */
    public Alldistinct(ArrayList<? extends IntVar> list) {
        this(list.toArray(new IntVar[list.size()]));
    }

    @Override
    public ArrayList<Var> arguments() {
        ArrayList<Var> variables = new ArrayList<Var>(list.length);
        for (Var var : list) variables.add(var);
        return variables;
    }

    @Override
    public void removeLevel(int level) {
        variableQueue = new LinkedHashSet<IntVar>();
        backtrackOccured = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void consistency(Store store) {
        if (impositionFailure) throw Store.failException;
        if (store.currentQueue == queueIndex) {
            LinkedHashSet<IntVar> copy = (LinkedHashSet<IntVar>) variableQueue.clone();
            for (IntVar Q : copy) {
                if (Q.singleton()) {
                    int qValue = Q.min();
                    int lastNotGround = stampNotGroundedVariables.value();
                    for (int i = 0; i <= lastNotGround; i++) if (list[i] != Q) list[i].domain.inComplement(store.level, list[i], qValue);
                }
            }
            if (queueIndex + 2 < store.queueNo) {
                store.changed[queueIndex + 2].add(this);
                return;
            }
        }
        consistencyChecks++;
        maximumMatchingNotRecomputed = true;
        if (stampValues.value() - 1 == stampNotGroundedVariables.value()) permutationConsistency = true; else permutationConsistency = false;
        LinkedHashSet<IntVar> fdvs = variableQueue;
        if (debugAll) {
            System.out.println("Changed Variables " + variableQueue);
        }
        IntDomain Qdom = null;
        Integer zero = 0;
        SimpleArrayList<IntVar> currentSimpleArrayList = null;
        TimeStamp<Integer> stamp = null;
        SimpleHashSet<IntVar> singletons = new SimpleHashSet<IntVar>();
        while (!variableQueue.isEmpty()) {
            variableQueue = new LinkedHashSet<IntVar>();
            for (IntVar Q : fdvs) {
                Qdom = Q.dom();
                if (Qdom.singleton()) {
                    int qValue = Q.value();
                    singletons.add(Q);
                    int lastNotGroundedVariable = stampNotGroundedVariables.value();
                    for (int i = 0; i <= lastNotGroundedVariable; i++) if (list[i] == Q) {
                        list[i] = list[lastNotGroundedVariable];
                        list[lastNotGroundedVariable] = Q;
                        stampNotGroundedVariables.update(lastNotGroundedVariable - 1);
                        break;
                    }
                    currentSimpleArrayList = valueMapVariable.get(qValue);
                    stamp = stamps.get(qValue);
                    int lastPosition = stamp.value();
                    int positionV = currentSimpleArrayList.indexOf(Q);
                    stamp.update(zero);
                    if (positionV > 0) {
                        currentSimpleArrayList.setElementAt(currentSimpleArrayList.get(0), positionV);
                        currentSimpleArrayList.setElementAt(Q, 0);
                    }
                    for (int c = 1; c <= lastPosition; c++) currentSimpleArrayList.get(c).domain.inComplement(store.level, currentSimpleArrayList.get(c), qValue);
                    for (int c = 1; c <= lastPosition; c++) variableQueue.add(currentSimpleArrayList.get(c));
                }
            }
            fdvs.addAll(variableQueue);
        }
        variableQueue.clear();
        boolean narrowingEvent = false;
        Iterator<IntVar> iter = fdvs.iterator();
        if (debugAll) {
            System.out.println("Before");
            System.out.println("Mapping Value->Variable" + valueMapVariable);
            System.out.println("Stamps for size of Mapping Value->Variable" + stamps);
            System.out.println("Maximum Matching " + matching);
        }
        for (; iter.hasNext(); ) {
            IntVar V = iter.next();
            IntDomain vPrunedDomain = V.recentDomainPruning();
            if (debugAll) {
                System.out.println("Variable changed " + V);
                System.out.println("Pruned Domain " + vPrunedDomain);
            }
            if (!vPrunedDomain.isEmpty()) {
                Integer matchedValue = matching.get(V).value();
                if (vPrunedDomain.contains(matchedValue)) freeVariables.add(V);
                if (debugAll) {
                    System.out.println(" V " + V + " matchedValue " + matchedValue + " prunedDom " + vPrunedDomain + "contains? " + vPrunedDomain.contains(matchedValue));
                }
                for (ValueEnumeration enumer = vPrunedDomain.valueEnumeration(); enumer.hasMoreElements(); ) {
                    int value = enumer.nextElement();
                    Integer integerValue = value;
                    currentSimpleArrayList = valueMapVariable.get(integerValue);
                    stamp = stamps.get(integerValue);
                    int lastPosition = stamp.value();
                    int positionV = currentSimpleArrayList.indexOf(V, lastPosition);
                    if (positionV == -1) continue;
                    if (lastPosition > positionV) {
                        stamp.update(lastPosition - 1);
                        currentSimpleArrayList.setElementAt(currentSimpleArrayList.get(lastPosition), positionV);
                        currentSimpleArrayList.setElementAt(V, lastPosition);
                        continue;
                    }
                    if (lastPosition == positionV) {
                        stamp.update(lastPosition - 1);
                        if (lastPosition == 0) {
                            int stampValue = stampValues.value() - 1;
                            int indexDeletedValue = valueIndex.get(integerValue);
                            if (indexDeletedValue < stampValue) {
                                valueIndex.put(potentialFreeValues[indexDeletedValue], stampValue);
                                valueIndex.put(potentialFreeValues[stampValue], indexDeletedValue);
                                integerValue = potentialFreeValues[indexDeletedValue];
                                potentialFreeValues[indexDeletedValue] = potentialFreeValues[stampValue];
                                potentialFreeValues[stampValue] = integerValue;
                            }
                            stampValues.update(stampValues.value() - 1);
                        }
                    }
                }
            } else if (debugAll) {
                System.out.println("There was an Variable which was marked as changed" + " but there is no difference in domain" + V);
                System.out.println("Most probably the result of current " + " implementation of variableQueue signals");
            }
        }
        if (debugAll) {
            System.out.println("After");
            System.out.println("Mapping Value->Variable" + valueMapVariable);
            System.out.println("Stamps for size of Mapping Value->Variable" + stamps);
        }
        if (debugAll) {
            System.out.println("Looking Maximum Matching ");
        }
        while (!singletons.isEmpty()) {
            IntVar singleton = singletons.removeFirst();
            fdvs.remove(singleton);
            freeVariables.remove(singleton);
            Integer integerValue = singleton.value();
            matching.get(singleton).update(integerValue);
            int stampValue = stampValues.value() - 1;
            int indexDeletedValue = valueIndex.get(integerValue);
            if (indexDeletedValue < stampValue) {
                valueIndex.put(potentialFreeValues[indexDeletedValue], stampValue);
                valueIndex.put(potentialFreeValues[stampValue], indexDeletedValue);
                integerValue = potentialFreeValues[indexDeletedValue];
                potentialFreeValues[indexDeletedValue] = potentialFreeValues[stampValue];
                potentialFreeValues[stampValue] = integerValue;
            }
            stampValues.update(stampValues.value() - 1);
        }
        if (!freeVariables.isEmpty()) {
            if (!hopcroftKarpMaximumMatching()) {
                freeVariablesAtFailure = new ArrayList<IntVar>(freeVariables);
                freeVariables.clear();
                variableQueue.clear();
                throw Store.failException;
            }
            freeVariables.clear();
        } else {
            int lastNotGroundedVariable = stampNotGroundedVariables.value();
            IntVar variable = null;
            Integer matchedValue;
            int positionMatched;
            for (int i = 0; i <= lastNotGroundedVariable; i++) {
                variable = list[i];
                matchedValue = matching.get(variable).value();
                currentSimpleArrayList = valueMapVariable.get(matchedValue);
                positionMatched = currentSimpleArrayList.indexOf(variable);
                if (positionMatched != 0) {
                    currentSimpleArrayList.setElementAt(currentSimpleArrayList.get(0), positionMatched);
                    currentSimpleArrayList.setElementAt(variable, 0);
                }
            }
        }
        if (debugAll) {
            System.out.println("Maximum Matching " + matching);
        }
        ArrayList<IntVar> l = new ArrayList<IntVar>();
        HashMap<IntVar, Integer> dfsnum = new HashMap<IntVar, Integer>();
        HashMap<IntVar, Integer> low = new HashMap<IntVar, Integer>();
        n = nStamp.value();
        int lastNotGroundedVariable = stampNotGroundedVariables.value();
        if (maximumMatchingNotRecomputed || permutationConsistency) {
            while (!fdvs.isEmpty()) {
                IntVar changedVariable = fdvs.iterator().next();
                fdvs.remove(changedVariable);
                if (debugAll) {
                    System.out.println("Tarjan start, changed variabled " + changedVariable);
                }
                revisitTarjan(changedVariable, l, dfsnum, low, fdvs);
                if (debugAll) {
                    System.out.println("Tarjan end");
                }
            }
            nStamp.update(n + 1);
        } else {
            scc = new IdentityHashMap<IntVar, Integer>();
            vn = nStamp.value();
            for (int i = 0; i <= lastNotGroundedVariable; i++) {
                if (debugAll) {
                    System.out.println("Tarjan start, changed variabled " + list[i]);
                    System.out.println("Tarjan start, value mapping " + valueMapVariable);
                }
                if (scc.get(list[i]) == null) visitTarjan(list[i], l, dfsnum, low);
                if (debugAll) {
                    System.out.println("Tarjan end");
                }
            }
            if (debugAll) {
                System.out.println("Tarjan end state " + scc);
            }
            IntVar next;
            for (Iterator<IntVar> e = scc.keySet().iterator(); e.hasNext(); ) {
                next = (IntVar) e.next();
                sccStamp.get(next).update(scc.get(next));
            }
            nStamp.update(vn + 1);
        }
        LinkedHashSet<IntVar> variablesReachableFromFreeValues = new LinkedHashSet<IntVar>(list.length, 0.50f);
        int stampValue = stampValues.value();
        int lastNotGroundedVariablePlusOne = lastNotGroundedVariable + 1;
        if (stampValue - lastNotGroundedVariablePlusOne > 0) {
            HashSet<Integer> matchedValues = new HashSet<Integer>(list.length, 0.50f);
            int noOfReachedVariablesLastTime = stampReachability.value();
            for (int i = 0; i <= lastNotGroundedVariable; i++) matchedValues.add(matching.get(list[i]).value());
            for (int i = 0; i < stampValue && variablesReachableFromFreeValues.size() < noOfReachedVariablesLastTime && variablesReachableFromFreeValues.size() != lastNotGroundedVariablePlusOne; i++) if (!matchedValues.contains(potentialFreeValues[i])) markReachableVariables(variablesReachableFromFreeValues, potentialFreeValues[i]);
            stampReachability.update(variablesReachableFromFreeValues.size());
        }
        if (debugAll) {
            System.out.println("All reached variables " + variablesReachableFromFreeValues);
            System.out.println("Check for All NOT reached variables if there is an " + " edge from matched variable to a different");
        }
        IntVar variable = null;
        Integer matched;
        int variableComponentId;
        int lastPosition;
        IntVar possibleDifferentComponentVariable;
        for (int j = 0; j <= lastNotGroundedVariable; j++) {
            variable = list[j];
            if (debugAll) {
                System.out.println("Variable " + variable + " is considered ");
            }
            if (!variablesReachableFromFreeValues.contains(variable)) {
                if (debugPruning) {
                    System.out.println("Variable " + variable + " is not reached by free values ");
                }
                variableComponentId = sccStamp.get(variable).value();
                matched = matching.get(variable).value();
                currentSimpleArrayList = valueMapVariable.get(matched);
                stamp = stamps.get(matched);
                lastPosition = stamp.value();
                if (debugAll) System.out.println("currentSimpleArrayList " + currentSimpleArrayList + " stamp " + lastPosition);
                if (lastPosition == 0 && permutationConsistency) {
                    if (debugPruning) System.out.println("Value " + matched + " has only this variable possible " + variable);
                    variable.domain.in(store.level, variable, matched, matched);
                    variableQueue.remove(variable);
                }
                for (int i = 0; i <= lastPosition; i++) {
                    possibleDifferentComponentVariable = currentSimpleArrayList.get(i);
                    if (variableComponentId != sccStamp.get(possibleDifferentComponentVariable).value()) {
                        if (debugPruning) {
                            System.out.println("\n\n\n\n\nVariable " + possibleDifferentComponentVariable + "can not take value " + matched + "\n\n\n\n");
                        }
                        possibleDifferentComponentVariable.domain.inComplement(store.level, possibleDifferentComponentVariable, matched);
                        narrowingEvent = true;
                        variableQueue.add(possibleDifferentComponentVariable);
                        currentSimpleArrayList.set(i, currentSimpleArrayList.get(lastPosition));
                        currentSimpleArrayList.set(lastPosition, possibleDifferentComponentVariable);
                        lastPosition = lastPosition - 1;
                        stamp.update(lastPosition);
                    }
                }
            }
        }
        if (!narrowingEvent && stampValues.value() - 1 == stampNotGroundedVariables.value()) {
            int sizePotentialFreeValues = stampValues.value();
            int currentlyUsedPotentialFreeValue = 0;
            Integer value;
            while (currentlyUsedPotentialFreeValue < sizePotentialFreeValues) {
                value = potentialFreeValues[currentlyUsedPotentialFreeValue];
                currentlyUsedPotentialFreeValue++;
                stamp = stamps.get(value);
                stampValue = stamp.value();
                if (stampValue == 0) {
                    if (valueMapVariable.get(value).get(0).dom().getSize() > 1) {
                        System.out.println("Transformation Alldistinct-Permutation and " + "missing propagation ");
                        valueMapVariable.get(value).get(0).domain.in(store.level, valueMapVariable.get(value).get(0), value, value);
                        variableQueue.add(valueMapVariable.get(value).get(0));
                        narrowingEvent = true;
                    }
                }
            }
        }
        backtrackOccured = false;
        if (narrowingEvent) {
            consistencyChecks--;
            fullConsistencyPassesWithNarrowingEvent++;
            consistency(store);
        }
        if (debugAll) {
            System.out.println("Consistency technique has finished execution ");
        }
    }

    @Override
    public int getConsistencyPruningEvent(Var var) {
        if (consistencyPruningEvents != null) {
            Integer possibleEvent = consistencyPruningEvents.get(var);
            if (possibleEvent != null) return possibleEvent;
        }
        return IntDomain.ANY;
    }

    private boolean hopcroftKarpMaximumMatching() {
        maximumMatchingNotRecomputed = false;
        boolean maximumMatchingFound = false;
        HashSet<Integer> nonFreeValues = new HashSet<Integer>();
        Integer matched;
        IntVar variable = null;
        int lastNotGroundedVariable = stampNotGroundedVariables.value();
        for (int i = 0; i <= lastNotGroundedVariable; i++) {
            variable = list[i];
            if (!freeVariables.contains(variable)) {
                matched = matching.get(variable).value();
                nonFreeValues.add(matched);
                if (backtrackOccured) {
                    SimpleArrayList<IntVar> currentSimpleArrayList = valueMapVariable.get(matched);
                    int positionMatched = currentSimpleArrayList.indexOf(variable);
                    if (positionMatched != 0) {
                        currentSimpleArrayList.setElementAt(currentSimpleArrayList.get(0), positionMatched);
                        currentSimpleArrayList.setElementAt(variable, 0);
                    }
                }
            }
        }
        HashMap<Integer, Integer> notYetUsedVariablePointer = new HashMap<Integer, Integer>();
        int sizePotentialFreeValues = stampValues.value();
        int currentlyUsedPotentialFreeValue = 0;
        Integer value;
        TimeStamp<Integer> stamp;
        int stampValue;
        while (currentlyUsedPotentialFreeValue < sizePotentialFreeValues) {
            value = potentialFreeValues[currentlyUsedPotentialFreeValue];
            currentlyUsedPotentialFreeValue++;
            stamp = stamps.get(value);
            stampValue = stamp.value();
            notYetUsedVariablePointer.put(value, stampValue);
        }
        while (!maximumMatchingFound) {
            ArrayList<LinkedList<Object>> allpaths = new ArrayList<LinkedList<Object>>();
            LinkedList<Object> path = new LinkedList<Object>();
            if (debugAll) {
                System.out.println("Non Free Values" + nonFreeValues);
            }
            HashSet<Integer> visitedValues = new HashSet<Integer>(valueMapVariable.size());
            HashSet<IntVar> visitedVariables = new HashSet<IntVar>(matching.size());
            currentlyUsedPotentialFreeValue = 0;
            while (currentlyUsedPotentialFreeValue < sizePotentialFreeValues) {
                if (path.size() == 0) {
                    while (currentlyUsedPotentialFreeValue < sizePotentialFreeValues) {
                        Integer potentialTop = potentialFreeValues[currentlyUsedPotentialFreeValue];
                        currentlyUsedPotentialFreeValue++;
                        if (!nonFreeValues.contains(potentialTop)) {
                            visitedValues.add(potentialTop);
                            path.addLast(potentialTop);
                            break;
                        }
                    }
                }
                if (debugAll) System.out.println("First element of the path " + path);
                if (path.size() == 0) if (allpaths.size() == 0) return false; else break;
                Integer top = (Integer) path.getLast();
                IntVar first;
                while (true) {
                    if (debugAll) System.out.println("Visited variables " + visitedVariables);
                    if (debugAll) System.out.println("Free variables " + freeVariables);
                    if (debugAll) System.out.println("Values for last path element " + valueMapVariable.get(top));
                    int notYetUsedVariable = notYetUsedVariablePointer.get(top);
                    if (debugAll) System.out.println("notYetUsedVariable " + notYetUsedVariable);
                    if (notYetUsedVariable == -1) if (path.size() == 1) break; else {
                        if (debugAll) System.out.println("Path to shorten " + path);
                        path.removeLast();
                        path.removeLast();
                        if (debugAll) System.out.println("Shorten path" + path);
                        top = (Integer) path.getLast();
                        continue;
                    }
                    first = valueMapVariable.get(top).get(notYetUsedVariable);
                    notYetUsedVariablePointer.put(top, notYetUsedVariable - 1);
                    if (!visitedVariables.contains(first)) {
                        path.addLast(first);
                        visitedVariables.add(first);
                        if (debugAll) System.out.println("Current path " + path);
                        if (freeVariables.contains(first)) break;
                        top = matching.get(first).value();
                        path.addLast(top);
                        visitedValues.add(top);
                    }
                    if (debugAll) System.out.println("Current path " + path);
                }
                if (path.size() % 2 == 0) {
                    allpaths.add(path);
                    path = new LinkedList<Object>();
                } else if (path.size() > 2) {
                    path.removeLast();
                    path.removeLast();
                } else {
                    path.removeLast();
                }
                if (debugAll) System.out.println("Free variables " + freeVariables);
                if (debugAll) System.out.println("Allpaths " + allpaths);
                if (freeVariables.size() == allpaths.size()) {
                    maximumMatchingFound = true;
                    break;
                }
            }
            if (debugAll) System.out.println("Allpaths " + allpaths);
            if (allpaths.size() == 0) return false;
            int allPathsSize = allpaths.size();
            for (int p = 0; p < allPathsSize; p++) {
                LinkedList<Object> freepath = allpaths.get(p);
                int freePathSize = freepath.size();
                for (int pos = 0; pos < freePathSize; pos = pos + 2) {
                    Integer matchedValue = (Integer) freepath.get(pos);
                    IntVar matchedVariable = (IntVar) freepath.get(pos + 1);
                    if (!freeVariables.remove(matchedVariable)) nonFreeValues.remove(matching.get(matchedVariable).value());
                    matching.get(matchedVariable).update(matchedValue);
                    SimpleArrayList<IntVar> currentSimpleArrayList = valueMapVariable.get(matchedValue);
                    int positionMatched = currentSimpleArrayList.indexOf(matchedVariable);
                    if (positionMatched != 0) {
                        currentSimpleArrayList.setElementAt(currentSimpleArrayList.get(0), positionMatched);
                        currentSimpleArrayList.setElementAt(matchedVariable, 0);
                    }
                    nonFreeValues.add(matchedValue);
                }
            }
            if (!maximumMatchingFound) {
                sizePotentialFreeValues = stampValues.value();
                currentlyUsedPotentialFreeValue = 0;
                notYetUsedVariablePointer = new HashMap<Integer, Integer>(sizePotentialFreeValues);
                while (currentlyUsedPotentialFreeValue < sizePotentialFreeValues) {
                    value = potentialFreeValues[currentlyUsedPotentialFreeValue];
                    currentlyUsedPotentialFreeValue++;
                    stamp = stamps.get(value);
                    stampValue = stamp.value();
                    notYetUsedVariablePointer.put(value, stampValue);
                }
            }
        }
        return true;
    }

    @Override
    public String id() {
        if (id != null) return id; else return this.getClass().getSimpleName() + numberId;
    }

    @Override
    public void impose(Store store) {
        store.registerRemoveLevelListener(this);
        stampValues = new TimeStamp<Integer>(store, valueMapVariable.size());
        stampReachability = new TimeStamp<Integer>(store, list.length);
        nStamp = new TimeStamp<Integer>(store, 0);
        stampNotGroundedVariables = new TimeStamp<Integer>(store, list.length - 1);
        Integer zero = 0;
        for (IntVar var : list) {
            var.putModelConstraint(this, getConsistencyPruningEvent(var));
            queueVariable(store.level, var);
            matching.put(var, new TimeStamp<Integer>(store, zero));
            sccStamp.put(var, new TimeStamp<Integer>(store, zero));
        }
        store.addChanged(this);
        store.countConstraint();
        Integer value = null;
        for (Iterator<Integer> e = valueMapVariable.keySet().iterator(); e.hasNext(); ) {
            value = e.next();
            stamps.put(value, new TimeStamp<Integer>(store, valueMapVariable.get(value).size() - 1));
        }
        for (IntVar var : list) freeVariables.add(var);
        LinkedHashSet<IntVar> fdvs = new LinkedHashSet<IntVar>(freeVariables);
        if (!hopcroftKarpMaximumMatching()) {
            impositionFailure = true;
            return;
        }
        n = nStamp.value();
        ArrayList<IntVar> l = new ArrayList<IntVar>();
        HashMap<IntVar, Integer> dfsnum = new HashMap<IntVar, Integer>();
        HashMap<IntVar, Integer> low = new HashMap<IntVar, Integer>();
        while (!fdvs.isEmpty()) {
            IntVar changedVariable = fdvs.iterator().next();
            fdvs.remove(changedVariable);
            revisitTarjan(changedVariable, l, dfsnum, low, fdvs);
        }
        nStamp.update(n + 1);
        if (debugAll) {
            System.out.println("Mapping Value->Variable" + valueMapVariable);
            System.out.println("Maximum Matching " + matching);
        }
        store.raiseLevelBeforeConsistency = true;
    }

    private void markReachableVariables(LinkedHashSet<IntVar> variablesReachableFromFreeValues, Integer value) {
        if (debugAll) {
            System.out.println("Start mark reachable variables " + value);
        }
        SimpleArrayList<IntVar> currentSimpleArrayList = valueMapVariable.get(value);
        TimeStamp<Integer> stamp = stamps.get(value);
        int lastPosition = stamp.value();
        Integer matched;
        for (int i = 0; i <= lastPosition; i++) {
            IntVar reachableVariable = currentSimpleArrayList.get(i);
            if (variablesReachableFromFreeValues.contains(reachableVariable)) continue;
            if (debugAll) {
                System.out.println("Variable " + reachableVariable + " has been reached from value " + value);
            }
            matched = matching.get(reachableVariable).value();
            variablesReachableFromFreeValues.add(reachableVariable);
            markReachableVariables(variablesReachableFromFreeValues, matched);
        }
    }

    @Override
    public void queueVariable(int level, Var var) {
        if (debugAll) System.out.println("Var " + var + ((IntVar) var).recentDomainPruning());
        variableQueue.add((IntVar) var);
    }

    @Override
    public void removeConstraint() {
        for (Var var : list) var.removeConstraint(this);
    }

    private void revisitTarjan(IntVar x, ArrayList<IntVar> l, HashMap<IntVar, Integer> dfsnum, HashMap<IntVar, Integer> low, LinkedHashSet<IntVar> fdvs) {
        Integer nInteger = n;
        dfsnum.put(x, nInteger);
        low.put(x, nInteger);
        n++;
        if (debugAll) System.out.println("Tarjan invocation : \nx " + x + "\nn " + n + "\nl " + l + "\ndfsnum " + dfsnum + "\nlow " + low + "\n");
        l.add(x);
        Integer matchedValue = matching.get(x).value();
        if (debugAll) System.out.println("Matched value " + matchedValue + " for " + x);
        SimpleArrayList<IntVar> currentSimpleArrayList = valueMapVariable.get(matchedValue);
        if (debugAll) System.out.println("Mapped variables to Matched value " + currentSimpleArrayList);
        TimeStamp<Integer> stamp = stamps.get(matchedValue);
        int lastPosition = stamp.value();
        if (debugAll) System.out.println("Last valid position for variables " + lastPosition);
        int sccStampX = sccStamp.get(x).value();
        for (int i = 0; i <= lastPosition; i++) {
            IntVar v = currentSimpleArrayList.get(i);
            if (sccStampX == sccStamp.get(v).value()) if (dfsnum.get(v) == null) {
                revisitTarjan(v, l, dfsnum, low, fdvs);
                int lowv = low.get(v);
                if (low.get(x) > lowv) low.put(x, lowv);
            } else {
                if (debugAll) System.out.println("Part 2 : low " + x + "=" + low.get(x) + " dfsnum " + v + "=" + dfsnum.get(v));
                int dfsnumv = dfsnum.get(v);
                if (dfsnumv < dfsnum.get(x)) if (l.contains(v)) if (low.get(x) > dfsnumv) low.put(x, dfsnumv);
            }
        }
        if (debugAll) {
            System.out.println("Invocation " + x + " Low values for it " + low);
            System.out.println("Invocation " + x + " Dfsnum values for it " + dfsnum);
        }
        int lowx = low.get(x);
        if (lowx == dfsnum.get(x)) {
            if (debugAll) System.out.println("Component found  ");
            Var component;
            while (true) {
                component = l.remove(l.size() - 1);
                if (debugAll) System.out.println("Component part  " + component + "id " + lowx);
                sccStamp.get(component).update(lowx);
                fdvs.remove(component);
                if (component == x) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean satisfied() {
        boolean sat = true;
        int i = 0;
        while (sat && i < list.length) {
            IntDomain vDom = list[i].dom();
            int vMin = vDom.min(), vMax = vDom.max();
            int j = 0;
            while (sat && j < list.length) {
                if (i != j) {
                    IntDomain ljDom = list[j].dom();
                    sat = (vMin > ljDom.max() || vMax < ljDom.min());
                }
                j++;
            }
            i++;
        }
        return sat;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(id());
        buf.append(" : alldistinct([");
        for (int i = 0; i < list.length; i++) {
            buf.append(list[i]);
            if (i < list.length - 1) buf.append(", ");
        }
        buf.append("]");
        return buf.toString();
    }

    private void visitTarjan(IntVar x, ArrayList<IntVar> l, HashMap<IntVar, Integer> dfsnum, HashMap<IntVar, Integer> low) {
        Integer vnInteger = vn;
        dfsnum.put(x, vnInteger);
        low.put(x, vnInteger);
        vn++;
        if (debugAll) System.out.println("Tarjan invocation : \nx " + x + "\nn " + vn + "\nl " + l + "\ndfsnum " + dfsnum + "\nlow " + low + "\n");
        l.add(x);
        Integer matchedValue = matching.get(x).value();
        if (debugAll) System.out.println("Matched value " + matchedValue + " for " + x);
        SimpleArrayList<IntVar> currentSimpleArrayList = valueMapVariable.get(matchedValue);
        if (debugAll) System.out.println("Mapped variables to Matched value " + currentSimpleArrayList);
        TimeStamp<Integer> stamp = stamps.get(matchedValue);
        int lastPosition = stamp.value();
        if (debugAll) System.out.println("Last valid position for variables " + lastPosition);
        IntVar v;
        for (int i = 1; i <= lastPosition; i++) {
            v = currentSimpleArrayList.get(i);
            if (dfsnum.get(v) == null) {
                visitTarjan(v, l, dfsnum, low);
                int lowv = low.get(v);
                if (low.get(x) > lowv) {
                    low.put(x, lowv);
                }
            } else {
                if (debugAll) System.out.println("Part 2 : low " + x + "=" + low.get(x) + " dfsnum " + v + "=" + dfsnum.get(v));
                int dfsnumv = dfsnum.get(v);
                if (dfsnumv < dfsnum.get(x)) if (l.contains(v)) if (low.get(x) > dfsnumv) {
                    low.put(x, dfsnumv);
                }
            }
        }
        if (debugAll) {
            System.out.println("Invocation " + x + " Low values for it " + low);
            System.out.println("Invocation " + x + " Dfsnum values for it " + dfsnum);
        }
        int lowx = low.get(x);
        if (lowx == dfsnum.get(x)) {
            if (debugAll) System.out.println("Component found  ");
            while (true) {
                IntVar component = l.remove(l.size() - 1);
                if (debugAll) System.out.println("Component part  " + component);
                scc.put(component, lowx);
                if (component == x) {
                    break;
                }
            }
        }
    }

    @Override
    public Constraint getGuideConstraint() {
        return new XeqC(guideVariable, guideValue);
    }

    @Override
    public int getGuideValue() {
        return guideValue;
    }

    IntVar guideVariable = null;

    int guideValue;

    boolean greedy = true;

    @Override
    public Var getGuideVariable() {
        int minCurrentPruning = 1;
        int maxCurrentPruning = 100000;
        guideVariable = null;
        int lastNotGroundedVariable = stampNotGroundedVariables.value();
        for (int i = 0; i <= lastNotGroundedVariable; i++) {
            if (list[i].getSize() == 2) {
                Integer firstValue = list[i].min();
                Integer secondValue = list[i].max();
                int pruningFirstValue = estimatePruning(list[i], firstValue);
                if (pruningFirstValue >= minCurrentPruning) {
                    int pruningSecondValue = estimatePruning(list[i], secondValue);
                    if (pruningFirstValue < pruningSecondValue) {
                        if (pruningFirstValue > minCurrentPruning) {
                            if (stamps.get(firstValue).value() < stamps.get(secondValue).value() || (stamps.get(firstValue).value() == stamps.get(secondValue).value() && !greedy)) {
                                guideVariable = list[i];
                                guideValue = firstValue;
                            } else {
                                guideVariable = list[i];
                                guideValue = secondValue;
                            }
                            minCurrentPruning = pruningFirstValue;
                            maxCurrentPruning = pruningSecondValue;
                        } else if (pruningFirstValue == minCurrentPruning && pruningSecondValue > maxCurrentPruning) {
                            if (stamps.get(firstValue).value() < stamps.get(secondValue).value() || (stamps.get(firstValue).value() == stamps.get(secondValue).value() && !greedy)) {
                                guideVariable = list[i];
                                guideValue = firstValue;
                            } else {
                                guideVariable = list[i];
                                guideValue = secondValue;
                            }
                            maxCurrentPruning = pruningSecondValue;
                        }
                    } else {
                        if (pruningSecondValue > minCurrentPruning) {
                            if (stamps.get(firstValue).value() <= stamps.get(secondValue).value() || (stamps.get(firstValue).value() == stamps.get(secondValue).value() && greedy)) {
                                guideVariable = list[i];
                                guideValue = firstValue;
                            } else {
                                guideVariable = list[i];
                                guideValue = secondValue;
                            }
                            minCurrentPruning = pruningSecondValue;
                            maxCurrentPruning = pruningFirstValue;
                        } else if (pruningSecondValue == minCurrentPruning && pruningFirstValue > maxCurrentPruning) {
                            if (stamps.get(firstValue).value() <= stamps.get(secondValue).value() || (stamps.get(firstValue).value() == stamps.get(secondValue).value() && greedy)) {
                                guideVariable = list[i];
                                guideValue = firstValue;
                            } else {
                                guideVariable = list[i];
                                guideValue = secondValue;
                            }
                            maxCurrentPruning = pruningFirstValue;
                        }
                    }
                }
                firstValue = null;
                secondValue = null;
            }
        }
        if (stampValues.value() - stampNotGroundedVariables.value() == 1) {
            int sizePotentialFreeValues = stampValues.value();
            int currentlyUsedPotentialFreeValue = 0;
            Integer value;
            TimeStamp<Integer> stamp;
            int stampValue;
            SimpleArrayList<IntVar> currentSimpleArrayList = null;
            while (currentlyUsedPotentialFreeValue < sizePotentialFreeValues) {
                value = potentialFreeValues[currentlyUsedPotentialFreeValue];
                currentlyUsedPotentialFreeValue++;
                stamp = stamps.get(value);
                stampValue = stamp.value();
                if (stampValue == 1) {
                    currentSimpleArrayList = valueMapVariable.get(value);
                    int pruningFirstVariable = estimatePruning(currentSimpleArrayList.get(0), value);
                    if (pruningFirstVariable < minCurrentPruning) continue;
                    int pruningSecondVariable = estimatePruning(currentSimpleArrayList.get(1), value);
                    if (pruningSecondVariable < minCurrentPruning) continue;
                    if (pruningFirstVariable < pruningSecondVariable) {
                        if (pruningFirstVariable > minCurrentPruning) {
                            if (currentSimpleArrayList.get(0).getSize() < currentSimpleArrayList.get(1).getSize() || (currentSimpleArrayList.get(0).getSize() == currentSimpleArrayList.get(1).getSize() && !greedy)) {
                                guideVariable = currentSimpleArrayList.get(0);
                                guideValue = value;
                            } else {
                                guideVariable = currentSimpleArrayList.get(1);
                                guideValue = value;
                            }
                            minCurrentPruning = pruningFirstVariable;
                            maxCurrentPruning = pruningSecondVariable;
                        } else if (pruningFirstVariable == minCurrentPruning && pruningSecondVariable > maxCurrentPruning) {
                            if (currentSimpleArrayList.get(0).getSize() < currentSimpleArrayList.get(1).getSize() || (currentSimpleArrayList.get(0).getSize() == currentSimpleArrayList.get(1).getSize() && !greedy)) {
                                guideVariable = currentSimpleArrayList.get(0);
                                guideValue = value;
                            } else {
                                guideVariable = currentSimpleArrayList.get(1);
                                guideValue = value;
                            }
                            maxCurrentPruning = pruningSecondVariable;
                        }
                    } else {
                        if (pruningSecondVariable > minCurrentPruning) {
                            if (currentSimpleArrayList.get(0).getSize() <= currentSimpleArrayList.get(1).getSize() || (currentSimpleArrayList.get(0).getSize() == currentSimpleArrayList.get(1).getSize() && greedy)) {
                                guideVariable = currentSimpleArrayList.get(0);
                                guideValue = value;
                            } else {
                                guideVariable = currentSimpleArrayList.get(1);
                                guideValue = value;
                            }
                            minCurrentPruning = pruningSecondVariable;
                            maxCurrentPruning = pruningFirstVariable;
                        } else if (pruningSecondVariable == minCurrentPruning && pruningFirstVariable > maxCurrentPruning) {
                            if (currentSimpleArrayList.get(0).getSize() <= currentSimpleArrayList.get(1).getSize() || (currentSimpleArrayList.get(0).getSize() == currentSimpleArrayList.get(1).getSize() && !greedy)) {
                                guideVariable = currentSimpleArrayList.get(0);
                                guideValue = value;
                            } else {
                                guideVariable = currentSimpleArrayList.get(1);
                                guideValue = value;
                            }
                            maxCurrentPruning = pruningFirstVariable;
                        }
                    }
                }
            }
            value = null;
            stamp = null;
            currentSimpleArrayList = null;
        }
        return guideVariable;
    }

    int estimatePruning(IntVar x, Integer v) {
        ArrayList<IntVar> exploredX = new ArrayList<IntVar>();
        ArrayList<Integer> exploredV = new ArrayList<Integer>();
        int pruning = estimatePruningRecursive(x, v, exploredX, exploredV);
        SimpleArrayList<IntVar> currentSimpleArrayList = null;
        Integer value = null;
        for (int i = 0; i < exploredV.size(); i++) {
            value = exploredV.get(i);
            currentSimpleArrayList = valueMapVariable.get(value);
            TimeStamp<Integer> stamp = stamps.get(value);
            int lastPosition = stamp.value();
            for (int j = 0; j <= lastPosition; j++) if (!exploredX.contains(currentSimpleArrayList.get(j))) pruning++;
            stamp = null;
        }
        currentSimpleArrayList = null;
        value = null;
        exploredX = null;
        exploredV = null;
        return pruning;
    }

    int estimatePruningRecursive(IntVar xVar, Integer v, ArrayList<IntVar> exploredX, ArrayList<Integer> exploredV) {
        if (exploredX.contains(xVar)) return 0;
        exploredX.add(xVar);
        exploredV.add(v);
        int pruning = 0;
        IntDomain xDom = xVar.dom();
        pruning = xDom.getSize() - 1;
        TimeStamp<Integer> stamp = null;
        SimpleArrayList<IntVar> currentSimpleArrayList = null;
        ValueEnumeration enumer = xDom.valueEnumeration();
        if (stampValues.value() - stampNotGroundedVariables.value() == 1) for (int i = enumer.nextElement(); enumer.hasMoreElements(); i = enumer.nextElement()) {
            if (!exploredV.contains(i)) {
                Integer iInteger = i;
                stamp = stamps.get(iInteger);
                int lastPosition = stamp.value();
                if (lastPosition < exploredX.size() + 1) {
                    currentSimpleArrayList = valueMapVariable.get(iInteger);
                    IntVar singleVar = null;
                    boolean single = true;
                    for (int m = 0; m <= lastPosition; m++) if (!exploredX.contains(currentSimpleArrayList.get(m))) if (singleVar != null) singleVar = currentSimpleArrayList.get(m); else single = false;
                    if (single && singleVar == null) {
                        System.out.println(this);
                        System.out.println("StampValues - 1 " + (stampValues.value() - 1));
                        System.out.println("Not grounded Var " + stampNotGroundedVariables.value());
                        int lastNotGroundedVariable = stampNotGroundedVariables.value();
                        Var variable = null;
                        for (int l = 0; l <= lastNotGroundedVariable; l++) {
                            variable = list[l];
                            System.out.println("Stamp for " + variable + " " + sccStamp.get(variable).value());
                            System.out.println("Matching " + matching.get(variable).value());
                        }
                    }
                    if (single && singleVar != null) pruning += estimatePruningRecursive(singleVar, iInteger, exploredX, exploredV);
                    singleVar = null;
                }
                iInteger = null;
            }
        }
        enumer = null;
        stamp = stamps.get(v);
        currentSimpleArrayList = valueMapVariable.get(v);
        int lastPosition = stamp.value();
        for (int i = 0; i <= lastPosition; i++) {
            IntVar variable = currentSimpleArrayList.get(i);
            if (!exploredX.contains(variable) && variable.dom().getSize() < exploredV.size() + 2) {
                boolean single = true;
                Integer singleVal = null;
                for (ValueEnumeration enumerX = variable.dom().valueEnumeration(); enumerX.hasMoreElements(); ) {
                    Integer next = enumerX.nextElement();
                    if (!exploredV.contains(next)) if (singleVal == null) singleVal = next; else single = false;
                }
                if (single) pruning += estimatePruningRecursive(variable, singleVal, exploredX, exploredV);
            }
        }
        stamp = null;
        return pruning;
    }

    @Override
    public void increaseWeight() {
        if (increaseWeight) {
            for (Var v : list) v.weight++;
        }
    }
}
