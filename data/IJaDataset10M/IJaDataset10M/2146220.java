package jopt.js.api.util;

import java.util.ArrayList;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointMultiIntArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.csp.util.SortableIntList;
import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * Represents a collection of IntIntervalSets, while being, itself, an IntValIntervalSet.  The values of the 
 * members of the collection sum to the overall values in the set.
 * @author jboerkoel
 *
 */
public class IntIntervalCollection extends IntValIntervalSet implements ChoicePointDataSource, ChoicePointEntryListener {

    SortableIntList timelineIDs;

    ArrayList timelines;

    private boolean isBuilt = false;

    protected ChoicePointNumArraySet cpdata;

    ChoicePointMultiIntArray timelineChanges;

    /**
	 * Constructs a new IntIntervalCollection
	 *
	 */
    public IntIntervalCollection() {
        super();
        timelineIDs = new SortableIntList();
        timelines = new ArrayList();
    }

    /**
	 * Registers the timeline addition in the timelineChanges field, so
	 * that this can be back trackable.
	 * @param timelineID - id of the timeline being added
	 * @param timeline - the delta of the timeline that was added
	 */
    private void registerTimelineAddition(int timelineID, IntValIntervalSet timeline) {
        if ((timelineChanges == null) || (!isBuilt)) return;
        int index = timeline.getFirstIntervalIndex();
        while (index >= 0) {
            int start = timeline.getMin(index);
            int end = timeline.getMax(index);
            int worth = timeline.getWorth(index);
            int tcIndex = timelineChanges.add(0, timelineID);
            timelineChanges.set(1, tcIndex, start);
            timelineChanges.set(2, tcIndex, end);
            timelineChanges.set(3, tcIndex, worth);
            index = timeline.getNextIntervalIndex(index);
        }
    }

    /**
	 * Returns the value of the isBuilt flag, specifying whether or not this collection is 
	 * built and should start recording choice point changes
	 * @return true if this collection is built
	 */
    public boolean isBuilt() {
        return isBuilt;
    }

    /**
	 * Sets the is built field, if this class is not set as built, it will not record changes
	 * making pushing and popping irrelevant.
	 * @param built new value for the isBuilt flag
	 */
    public void setBuilt(boolean built) {
        this.isBuilt = built;
    }

    /**
	 * Returns the current timeline for the id that is given by timelineID
	 * @param timelineID - id of the timeline to be gotten
	 * @return IntValIntervalSet registered for the given id
	 */
    public IntValIntervalSet getTimelineForID(int timelineID) {
        int index = timelineIDs.binarySearch(timelineID);
        if (index < 0) {
            return new IntValIntervalSet();
        } else {
            return (IntValIntervalSet) timelines.get(index);
        }
    }

    /**
	 * Returns a list of all the timeline Id's that have been specified so far
	 * @return array of ints representing the ids of all timelines specified
	 */
    public int[] getTimelineIDs() {
        int[] ids = new int[timelineIDs.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = timelineIDs.get(i);
        }
        return ids;
    }

    /**
	 * Returns an array of ints representing the ids of the timelines that actually contribute
	 * to the overall IntValIntervalSet
	 * @return - array of the ids of timelines that are actually utilized
	 */
    public int[] getUtilizedTimelineIDs() {
        ArrayIntList ids = new ArrayIntList();
        for (int i = 0; i < timelineIDs.size(); i++) {
            if (!((IntIntervalSet) timelines.get(i)).isEmpty()) {
                ids.add(timelineIDs.get(i));
            }
        }
        return (int[]) ids.toArray(new int[0]);
    }

    /**
	 * Replaces the current timeline indexed by timelineID to this new timeline, and
	 * adds the difference to the overall IntValIntervalSet
	 * @param timelineID - id of the timeline that is being replaced
	 * @param timeline - the new timeline
	 */
    public IntValIntervalSet set(int timelineID, IntIntervalSet timeline) {
        IntValIntervalSet valTimeline;
        if (timeline instanceof IntValIntervalSet) {
            valTimeline = (IntValIntervalSet) timeline.clone();
        } else {
            valTimeline = new IntValIntervalSet();
            valTimeline.add(timeline, 1);
        }
        int index = timelineIDs.binarySearch(timelineID);
        if (index < 0) {
            index = -index - 1;
            timelineIDs.add(index, timelineID);
            timelines.add(index, valTimeline);
            this.add(valTimeline);
            registerTimelineAddition(timelineID, valTimeline);
            return valTimeline;
        } else {
            IntValIntervalSet currentTimeline = (IntValIntervalSet) timelines.get(index);
            timelines.set(index, valTimeline.clone());
            valTimeline.remove(currentTimeline);
            this.add(valTimeline);
            registerTimelineAddition(timelineID, valTimeline);
            return valTimeline;
        }
    }

    /**
	 * Returns a count of timelines that actually have something in them
	 * @return number of timelines that are specified on this collection
	 */
    public int count() {
        int count = 0;
        for (int i = 0; i < timelines.size(); i++) {
            if (((IntIntervalSet) timelines.get(i)).size() > 0) {
                count++;
            }
        }
        return count;
    }

    public boolean choicePointStackSet() {
        if (cpdata != null) return true;
        return false;
    }

    public void setChoicePointStack(ChoicePointStack cps) {
        if (cps == null) return;
        if (cps != null) {
            this.cpdata = cps.newNumStackSet(this);
            this.timelineChanges = cpdata.newMultiIntList(4);
        }
    }

    public void beforeChoicePointPopEvent() {
        for (int i = 0; i < timelineChanges.size(); i++) {
            int timelineID = timelineChanges.get(0, i);
            int index = timelineIDs.binarySearch(timelineID);
            IntValIntervalSet currentTimeline = (IntValIntervalSet) timelines.get(index);
            int start = timelineChanges.get(1, i);
            int end = timelineChanges.get(2, i);
            int worth = timelineChanges.get(3, i);
            currentTimeline.remove(start, end, worth);
            this.remove(start, end, worth);
        }
    }

    public void afterChoicePointPopEvent() {
    }

    public void beforeChoicePointPushEvent() {
    }

    public void afterChoicePointPushEvent() {
        for (int i = 0; i < timelineChanges.size(); i++) {
            int timelineID = timelineChanges.get(0, i);
            int index = timelineIDs.binarySearch(timelineID);
            IntValIntervalSet currentTimeline = (IntValIntervalSet) timelines.get(index);
            int start = timelineChanges.get(1, i);
            int end = timelineChanges.get(2, i);
            int worth = timelineChanges.get(3, i);
            currentTimeline.add(start, end, worth);
            this.add(start, end, worth);
        }
    }
}
