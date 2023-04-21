package de.gstpl.algo.localsearch;

import de.gstpl.algo.AutomaticTT;
import de.gstpl.algo.itc.ITCPerson;
import de.gstpl.algo.itc.ITCSubject;
import de.gstpl.data.ApplicationProperties;
import de.gstpl.data.IPerson;
import de.gstpl.data.IRoom;
import de.gstpl.data.ISubject;
import de.gstpl.data.ITimeInterval;
import de.gstpl.data.TimeFormat;
import de.gstpl.data.set.IRaster;
import de.gstpl.data.set.ITimeIntervalGroup;
import de.gstpl.data.set.PersonSet;
import de.gstpl.data.set.SetFactory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * This class holds all objects of one periode e.g. one week, 
 * which are necessary for optimization.
 * 
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class Periode implements Cloneable {

    private final double DEPTH_MULTIPLIER = ApplicationProperties.get().getDepthMultiplier();

    private final int MAX_NODES = ApplicationProperties.get().getMaxNodes();

    private final int FINAL_DEPTH = ApplicationProperties.get().getFinalDepth();

    private int processedNodes;

    private int maxProcessedNodes;

    private boolean initialCompress;

    private final int meanDuration;

    private final int noOfSlots;

    private final int noOfClusters;

    private final SetFactory setFactory;

    private final List<? extends IRoom> allRooms;

    private final List<? extends IPerson> allPersons;

    private final List<? extends ITimeInterval> allTIs;

    private Deque<TIMove> stack;

    private final Map<ITimeInterval, Integer> indexedTimeIntervals;

    private final List<ITimeIntervalGroup> clusterArray;

    private final List<ITimeInterval> unplacedTIs;

    private final Random random;

    private Map<ITimeInterval, Integer> complexityMap;

    public Periode(int noOfSlots_, int noOfClusters_, int meanDuration_, List<? extends IRoom> allRooms_, List<? extends IPerson> allPersons_, List<? extends ITimeInterval> allTIs_) {
        initialCompress = true;
        random = ApplicationProperties.get().getRandom();
        meanDuration = meanDuration_;
        noOfSlots = noOfSlots_;
        noOfClusters = noOfClusters_;
        setFactory = SetFactory.get();
        allTIs = allTIs_;
        allRooms = allRooms_;
        allPersons = allPersons_;
        indexedTimeIntervals = new HashMap<ITimeInterval, Integer>(noOfSlots);
        complexityMap = new HashMap<ITimeInterval, Integer>(noOfSlots);
        clusterArray = new ArrayList<ITimeIntervalGroup>(noOfClusters);
        unplacedTIs = new ArrayList<ITimeInterval>(noOfClusters * 2);
        for (int i = 0; i < noOfClusters; i++) {
            clusterArray.add(setFactory.createTimeIntervalGroup(i * meanDuration, allRooms));
        }
    }

    private Periode(int meanDur, int slots, int nGroups, List<? extends IRoom> rooms, List<? extends IPerson> persons, List<? extends ITimeInterval> timeintervals, Map<ITimeInterval, Integer> tis, List<ITimeIntervalGroup> clusterArray_, List<ITimeInterval> unplaced_, Map<ITimeInterval, Integer> complexity_) {
        initialCompress = false;
        random = ApplicationProperties.get().getRandom();
        meanDuration = meanDur;
        noOfSlots = slots;
        noOfClusters = nGroups;
        setFactory = SetFactory.get();
        allRooms = rooms;
        allPersons = persons;
        allTIs = timeintervals;
        indexedTimeIntervals = new HashMap<ITimeInterval, Integer>(tis);
        complexityMap = complexity_;
        unplacedTIs = new ArrayList<ITimeInterval>(unplaced_);
        clusterArray = new ArrayList<ITimeIntervalGroup>(clusterArray_.size());
        for (ITimeIntervalGroup tig : clusterArray_) {
            clusterArray.add((ITimeIntervalGroup) tig.clone());
        }
    }

    public Collection<ITimeInterval> getAll() {
        List<ITimeInterval> a = new ArrayList<ITimeInterval>(noOfSlots * 5);
        for (ITimeIntervalGroup tiGroup : clusterArray) {
            a.addAll(tiGroup.getAll());
        }
        return a;
    }

    /**    
     * @return the number of hard constraints violations.
     */
    public int getHardConstraintsViolations() {
        return unplacedTIs.size();
    }

    public void applyCurrentSettings(AutomaticTT automaticTT) {
        indexedTimeIntervals.clear();
        ITimeIntervalGroup tig;
        for (int i = 0; i < clusterArray.size(); i++) {
            tig = clusterArray.get(i);
            Map<ITimeInterval, IRoom> assignment = tig.getAssignment();
            for (ITimeInterval ti : tig.getAll()) {
                assert i < noOfSlots : i + " value:" + tig;
                assert i == tig.getStartTime() : "key:" + i + " startTime:" + tig.getStartTime();
                ITimeInterval tmpTI = automaticTT.cloneTIWithUpdatedRoomAndStartTime(ti, i, assignment.get(ti));
                indexedTimeIntervals.put(tmpTI, tmpTI.getStartTime());
            }
        }
        for (ITimeInterval ti : unplacedTIs) {
            ITimeInterval tmpTI = automaticTT.cloneTIWithUpdatedRoomAndStartTime(ti, -1, null);
            indexedTimeIntervals.put(tmpTI, -1);
        }
    }

    private List<ITimeInterval> tmpArrayForShuffle;

    /**
     * This method fixes some ITimeInterval's in groupsInWeeks. The rest and
     * overlapping ITimeInterval's will be added to the specified canister.         
     * After this method call the waste is empty.
     */
    @SuppressWarnings("unchecked")
    public void prepare4NextIteration(double percentageOfChanges, boolean shuffleBeforePuttingIntoUnplaced) {
        if (getUnplaced().size() < 1) {
            scPreparation(percentageOfChanges);
        } else {
            randomHCPreparation(percentageOfChanges, shuffleBeforePuttingIntoUnplaced);
        }
    }

    boolean tooManyNodes() {
        return maxProcessedNodes > MAX_NODES / 2;
    }

    private void scPreparation(double percentage) {
        final Map<ITimeInterval, Integer> map = getSoftConstraintMap();
        Comparator<ITimeInterval> comparator = new Comparator<ITimeInterval>() {

            public int compare(ITimeInterval o1, ITimeInterval o2) {
                int c1 = map.get(o1);
                int c2 = map.get(o2);
                if (c1 == c2) {
                    return 0;
                } else if (c1 < c2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        preparation(comparator, percentage);
    }

    private void preparation(Comparator<ITimeInterval> comp, double percentage) {
        Collections.sort(allTIs, comp);
        int TI_BORDER = (int) Math.round(allTIs.size() * percentage);
        ITimeInterval ti;
        for (int i = 0; i < TI_BORDER; i++) {
            ti = allTIs.get(i);
            int index = indexedTimeIntervals.get(ti);
            if (index >= 0) {
                clusterArray.get(index).remove(ti);
                addToBottomOfUnplaced(ti);
            }
        }
    }

    /**
     * This method removes n ITimeInterval's from all clusters; where n depends
     * on percentageOfChanges.
     * @param shuffleBeforeRemoving is false if you don't want that on 
     * every call of this method we remove nearly the same ITimeInterval's
     * where percentage increases the length of this 'same ITimeInterval' 
     * - list.
     */
    private void randomHCPreparation(double percentageOfChanges, boolean shuffleBeforeRemoving) {
        ITimeIntervalGroup tig;
        for (int i = clusterArray.size() - 1; i >= 0; i--) {
            tig = clusterArray.get(i);
            assert i < noOfSlots;
            int tiCounter = 0;
            int TI_BORDER = (int) Math.round(tig.getAll().size() * percentageOfChanges);
            Collection<? extends ITimeInterval> coll = tig.getAll();
            if (shuffleBeforeRemoving) {
                if (tmpArrayForShuffle == null) {
                    tmpArrayForShuffle = new ArrayList<ITimeInterval>(100);
                }
                tmpArrayForShuffle.clear();
                tmpArrayForShuffle.addAll(coll);
                Collections.shuffle(tmpArrayForShuffle, random);
                coll = tmpArrayForShuffle;
            }
            for (ITimeInterval ti : new ArrayList<ITimeInterval>(coll)) {
                if (tiCounter < TI_BORDER) {
                    tig.remove(ti);
                    addToBottomOfUnplaced(ti);
                }
                tiCounter++;
            }
        }
    }

    /**
     * The head should be the most difficult ITimeInterval.     
     */
    public void addToHeadOfUnplaced(ITimeInterval currentTI) {
        unplacedTIs.add(currentTI);
        indexedTimeIntervals.put(currentTI, -1);
    }

    /**
     * The bottom of the 'unplaced' should be the easiest of the 
     * difficult/unplaced ITimeInterval's.     
     */
    private void addToBottomOfUnplaced(ITimeInterval currentTI) {
        unplacedTIs.add(currentTI);
        indexedTimeIntervals.put(currentTI, -1);
    }

    public boolean check() {
        ITimeIntervalGroup tig;
        for (int i = 0; i < clusterArray.size(); i++) {
            tig = clusterArray.get(i);
            for (ITimeInterval ti : tig.getAll()) {
                if (indexedTimeIntervals.get(ti) != i) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method tries to inject overlapping (colliding) ITimeInterval's by 
     * swapping with minimal colliding groups and recursivly swaps those ITimeInterval's.
     */
    public void compress() {
        if (stack == null) {
            stack = new ArrayDeque<TIMove>(noOfSlots);
        } else {
            stack.clear();
        }
        int unplacedTIsSize = unplacedTIs.size();
        int depth;
        int maxCollisions;
        if (unplacedTIsSize < 1) {
            return;
        } else if (unplacedTIsSize < 6) {
            depth = FINAL_DEPTH;
            maxCollisions = (int) Math.round(depth * DEPTH_MULTIPLIER);
        } else if (unplacedTIsSize < 10) {
            depth = 3;
            maxCollisions = 3;
        } else if (unplacedTIsSize < 20) {
            depth = 2;
            maxCollisions = 2;
        } else if (initialCompress) {
            initialCompress = false;
            depth = 1;
            maxCollisions = 1;
        } else {
            depth = 1;
            maxCollisions = 2;
        }
        List<ITimeInterval> all = new ArrayList<ITimeInterval>(unplacedTIs);
        Collections.sort(all, complexityComparator);
        for (ITimeInterval ti : all) {
            processedNodes = 0;
            if (indexedTimeIntervals.get(ti) >= 0) {
                continue;
            }
            if (inject(-1, ti, depth, maxCollisions)) {
                assert indexedTimeIntervals.get(ti) >= 0 : processedNodes + " ti:" + ti;
            } else {
                increaseComplexity(ti);
            }
            stack.clear();
        }
    }

    /**
     * This method tries to insert the specified ITimeInterval into this
     * Periode
     * 
     * @return -1 if insertation was not possible, otherwise it returns the 
     * new startTime
     */
    private boolean inject(int oldStartTime, ITimeInterval currentTI, int depth, int maxCollidingTIs) {
        if (depth < 0) {
            return false;
        }
        if (processedNodes > MAX_NODES / 2) {
            if (processedNodes > MAX_NODES && maxCollidingTIs > 0) {
                return false;
            }
            if (maxCollidingTIs > 0) {
                maxCollidingTIs--;
            }
        }
        int lastStackSize = stack.size();
        ISubject subject = currentTI.getSubject();
        PersonSet cPersonSet = subject.getPersonSet();
        IRaster raster = subject.getWeekRaster(TimeFormat.DEFAULT_WEEK).getAllowed();
        int interv[] = getSearchInterval(currentTI);
        if (interv == null) {
            return false;
        }
        for (int startTime = raster.getNextAssignment(interv[0]); startTime >= 0 && startTime < interv[1]; startTime = raster.getNextAssignment(startTime + meanDuration)) {
            assert lastStackSize == stack.size() : " nodes:" + processedNodes + " pointer:" + lastStackSize + " stack.size:" + stack.size() + " " + stack;
            if (startTime == oldStartTime) {
                continue;
            }
            ITimeIntervalGroup currentGroup = clusterArray.get(startTime);
            if (doMove(currentTI, oldStartTime, startTime) >= 0) {
                stack.push(new TIMove(currentTI, oldStartTime, startTime));
                return true;
            } else if (depth < 1) {
                continue;
            }
            List<ITimeInterval> collidingTIs = new ArrayList<ITimeInterval>(maxCollidingTIs + 1);
            for (ITimeInterval tmpTI : currentGroup.getAll()) {
                if (cPersonSet.countCollisionsIfAdd(tmpTI.getSubject().getPersonSet()) > 0) {
                    collidingTIs.add(tmpTI);
                    if (collidingTIs.size() > maxCollidingTIs) {
                        break;
                    }
                }
            }
            if (collidingTIs.size() > maxCollidingTIs) {
                continue;
            } else if (collidingTIs.size() > 0) {
                boolean injected = false;
                int currentColliding = collidingTIs.size();
                for (ITimeInterval toMoveTI : collidingTIs) {
                    if (indexedTimeIntervals.get(toMoveTI) != startTime) {
                        injected = true;
                        continue;
                    }
                    processedNodes++;
                    injected = inject(startTime, toMoveTI, depth - 1, maxCollidingTIs - currentColliding);
                    if (!injected) {
                        break;
                    }
                }
                if (!injected) {
                    rollback(lastStackSize);
                } else {
                    if (indexedTimeIntervals.get(currentTI) == oldStartTime) {
                        int ret = doMove(currentTI, oldStartTime, startTime);
                        if (ret < 0) {
                            rollback(lastStackSize);
                        } else {
                            stack.push(new TIMove(currentTI, oldStartTime, startTime));
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private final ComplexityComp complexityComparator = new ComplexityComp();

    class ComplexityComp implements Comparator<ITimeInterval> {

        public int compare(ITimeInterval o1, ITimeInterval o2) {
            int c1 = getComplexity(o1);
            int c2 = getComplexity(o2);
            if (c1 == c2) {
                return 0;
            } else if (c1 < c2) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    class ComplexityEntry {

        int depth;

        int maxCollisions;

        boolean lastSuccess;

        long lastMilliSeconds;

        public ComplexityEntry() {
        }
    }

    private int getComplexity(ITimeInterval ti) {
        Integer i = complexityMap.get(ti);
        if (i == null) {
            return 0;
        } else {
            return i;
        }
    }

    private void increaseComplexity(ITimeInterval ti) {
        Integer i = complexityMap.get(ti);
        if (i == null) {
            complexityMap.put(ti, 1);
        } else {
            complexityMap.put(ti, i + 1);
        }
    }

    public void resetComplexity() {
        complexityMap.clear();
    }

    /**
     * This method returns the biggest possible interval where we can place the
     * specified ti in respect to all followers and beforers.
     * 
     * @return null if no search interval is possible, i.e. order collision.
     * Use the half open interval as follows: the 0-index as inclusive start 
     * and the 1-index as exclusive upper limit.
     */
    public int[] getSearchInterval(ITimeInterval ti) {
        Integer maxBefores = 0, minFollowers = noOfSlots;
        for (ISubject s : ((ITCSubject) ti.getSubject()).getFollows()) {
            Integer tmp = indexedTimeIntervals.get(((ITCSubject) s).getSingleTI());
            if (tmp != null && tmp < minFollowers) {
                minFollowers = tmp;
            }
        }
        for (ISubject s : ((ITCSubject) ti.getSubject()).getBefores()) {
            Integer tmp = indexedTimeIntervals.get(((ITCSubject) s).getSingleTI());
            if (tmp != null && tmp > maxBefores) {
                maxBefores = tmp;
            }
        }
        if (maxBefores < minFollowers) {
            return new int[] { maxBefores, minFollowers };
        } else {
            return null;
        }
    }

    /**
     * This method reverts the moves already done. Used from inject().
     */
    private final void rollback(int border) {
        TIMove move;
        while (stack.size() > border) {
            move = stack.removeFirst();
            int ret = doMove(move.ti, move.startNew, move.startOld);
            assert ret >= 0 : "startNew:" + move.startNew + " startOld:" + move.startOld + " ti:" + move.ti + ", new group:" + clusterArray.get(move.startNew) + ", old group:" + clusterArray.get(move.startOld);
        }
    }

    /**
     * Directly inject currentTI into group with specified startNew.
     * 
     * @return the new startTime of specified currentTI, -1 if injection was not
     * successful.
     */
    private final int doMove(ITimeInterval currentTI, int startOld, int startNew) {
        boolean addPossible;
        if (startNew >= 0) {
            assert clusterArray.get(startNew) != null : "nodes:" + processedNodes + " isUnplaced:" + unplacedTIs.contains(currentTI) + " ti:" + currentTI + " startNew:" + startNew + " startOld:" + startOld + " index:" + indexedTimeIntervals.get(currentTI) + " #unplaced:" + unplacedTIs.size() + " \tunplaced: " + unplacedTIs + " \tgroups: " + clusterArray;
            addPossible = clusterArray.get(startNew).addIfPossible(currentTI);
        } else {
            unplacedTIs.add(currentTI);
            addPossible = true;
        }
        if (addPossible) {
            indexedTimeIntervals.put(currentTI, startNew);
            boolean removePossible;
            if (startOld >= 0) {
                removePossible = clusterArray.get(startOld).remove(currentTI);
            } else {
                removePossible = unplacedTIs.remove(currentTI);
            }
            assert removePossible : "nodes:" + processedNodes + " startNew:" + startNew + " startOld:" + startOld + " ti:" + currentTI + ", new group:" + clusterArray.get(startNew) + ", old group:" + clusterArray.get(startOld) + " \tstack" + stack;
            return startNew;
        } else {
            return -1;
        }
    }

    public int getMaxProcessedNodes() {
        return maxProcessedNodes;
    }

    public int getStartTime(ITimeInterval ti) {
        return indexedTimeIntervals.get(ti);
    }

    public boolean addIfPossible(ITimeInterval currentTI, int startTime) {
        if (clusterArray.get(startTime).addIfPossible(currentTI)) {
            indexedTimeIntervals.put(currentTI, startTime);
            return true;
        }
        return false;
    }

    private static final StartTimeEntryComparator startTimeComparator = new StartTimeEntryComparator();

    /**
     * This method merges the specified Periode's into a new Periode.
     * It will produce newSize Periode's with this merge-procedure.
     */
    public List<Periode> merge(List<Periode> periodes, int newSize) {
        List<StartTimeEntry> unplacedEntries = new ArrayList<StartTimeEntry>(20);
        List<Periode> result = new ArrayList<Periode>(newSize);
        Periode mergedChild = new Periode(noOfSlots, noOfClusters, meanDuration, allRooms, allPersons, allTIs);
        List<Map<ITimeInterval, Integer>> memory = new ArrayList<Map<ITimeInterval, Integer>>(periodes.size());
        int startIndex = 0;
        int bestPeriode = Integer.MAX_VALUE;
        for (int pIndex = 0; pIndex < periodes.size(); pIndex++) {
            memory.add(periodes.get(pIndex).getSoftConstraintMap());
            int tmp = periodes.get(pIndex).getSoftConstraintsViolations();
            if (tmp < bestPeriode) {
                startIndex = pIndex;
            }
        }
        int differences = 0;
        for (ITimeInterval ti : allTIs) {
            int bestPeriodeIndex = startIndex;
            int minSCs = memory.get(bestPeriodeIndex).get(ti);
            int lastCounter = -1;
            for (int index = 0; index < periodes.size(); index++) {
                if (index == startIndex) {
                    continue;
                }
                Map<ITimeInterval, Integer> problemMap = memory.get(index);
                int tmp = problemMap.get(ti);
                if (minSCs > tmp) {
                    minSCs = tmp;
                    bestPeriodeIndex = index;
                }
                if (lastCounter != tmp) {
                    if (lastCounter >= 0) {
                        differences++;
                    }
                    lastCounter = tmp;
                }
            }
            int startTime = periodes.get(bestPeriodeIndex).getStartTime(ti);
            StartTimeEntry entry = new StartTimeEntry(startTime, ti);
            if (startTime < 0 || !mergedChild.addIfPossible(ti, startTime)) {
                unplacedEntries.add(entry);
            }
        }
        Collections.sort(unplacedEntries, startTimeComparator);
        for (StartTimeEntry entry : unplacedEntries) {
            mergedChild.addToHeadOfUnplaced(entry.ti);
        }
        result.add(mergedChild);
        return result;
    }

    public Collection<? extends ITimeInterval> getUnplaced() {
        return unplacedTIs;
    }

    /**     
     * @return a map which maps ITimeInterval's to its soft constraint violations.
     */
    private Map<ITimeInterval, Integer> getSoftConstraintMap() {
        Map<ITimeInterval, Integer> map = new HashMap<ITimeInterval, Integer>(allTIs.size());
        for (ITimeInterval ti : allTIs) {
            map.put(ti, 0);
        }
        for (IPerson p : allPersons) {
            TreeMap<Integer, ITimeInterval> indexRaster = new TreeMap<Integer, ITimeInterval>();
            for (ITimeInterval ti : p.getTimeIntervals()) {
                Integer time = indexedTimeIntervals.get(ti);
                assert time != null : "nodes:" + processedNodes + " ti" + ti + " " + indexedTimeIntervals;
                if (time >= 0) {
                    indexRaster.put(time, ti);
                }
            }
            ((ITCPerson) p).getSoftConstraintViolations(indexRaster, map);
        }
        return map;
    }

    public int getSoftConstraintsViolations() {
        int result = 0;
        for (IPerson p : allPersons) {
            TreeMap<Integer, ITimeInterval> raster = new TreeMap<Integer, ITimeInterval>();
            for (ITimeInterval ti : p.getTimeIntervals()) {
                assert indexedTimeIntervals.get(ti) != null : "nodes:" + processedNodes + " ti" + ti + " " + indexedTimeIntervals;
                int startTime = indexedTimeIntervals.get(ti);
                if (startTime >= 0) {
                    raster.put(startTime, ti);
                }
            }
            result += ((ITCPerson) p).getSoftConstraintViolations(raster, null);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Periode other = (Periode) obj;
        if (this.indexedTimeIntervals != other.indexedTimeIntervals && (this.indexedTimeIntervals == null || !this.indexedTimeIntervals.equals(other.indexedTimeIntervals))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (indexedTimeIntervals != null ? indexedTimeIntervals.hashCode() : 0);
    }

    @Override
    public Object clone() {
        Periode clone = new Periode(meanDuration, noOfSlots, noOfClusters, allRooms, allPersons, allTIs, indexedTimeIntervals, clusterArray, unplacedTIs, complexityMap);
        return clone;
    }
}

class StartTimeEntryComparator implements Comparator<StartTimeEntry> {

    public int compare(StartTimeEntry o1, StartTimeEntry o2) {
        if (o1.startTime < 0) {
            if (o2.startTime < 0) {
                return 0;
            }
            return 1;
        } else if (o2.startTime < 0) {
            return -1;
        } else if (o1.startTime == o2.startTime) {
            return 0;
        } else if (o1.startTime < o2.startTime) {
            return -1;
        } else {
            return 1;
        }
    }
}

/**
 * This class holds one ITimeInterval and its associated start time.
 */
class StartTimeEntry {

    ITimeInterval ti;

    int startTime;

    StartTimeEntry(int i_, ITimeInterval ti_) {
        startTime = i_;
        ti = ti_;
    }
}

/**
 * This class defines a move from a ITimeInterval from startNew_ to startOld_ 
 */
class TIMove {

    int startOld;

    int startNew;

    ITimeInterval ti;

    TIMove(ITimeInterval ti_, int startOld_, int startNew_) {
        ti = ti_;
        startNew = startNew_;
        startOld = startOld_;
    }

    @Override
    public String toString() {
        return "old:" + startOld + " new:" + startNew + "\nti:" + ti;
    }
}

/**
 * This class saves the colliding ITimeInterval's of one ITimeInterval with
 * specified startTime.
 */
class EvalPoint {

    int startTime;

    Collection<ITimeInterval> collidingTIs;

    EvalPoint(int startTime_, Collection<ITimeInterval> collidingTIs_) {
        startTime = startTime_;
        collidingTIs = collidingTIs_;
    }

    @Override
    public String toString() {
        return "startTime:" + startTime + " collidingTIs:" + collidingTIs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EvalPoint other = (EvalPoint) obj;
        return this.startTime == other.startTime && collidingTIs.size() == other.collidingTIs.size();
    }

    @Override
    public int hashCode() {
        return startTime * 13;
    }
}
