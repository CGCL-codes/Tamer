package de.gstpl.algo.itc;

import de.gstpl.data.ISubject;
import de.gstpl.data.ITimeInterval;
import de.gstpl.data.db4o.SubjectDb4oImpl;
import de.gstpl.data.set.IRaster;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class ITCSubject extends SubjectDb4oImpl {

    private static final long serialVersionUID = 98242412109898977L;

    private Set<ITCSubject> befores;

    private Set<ITCSubject> follows;

    public ITCSubject() {
        this("defaultSubjectName");
    }

    public ITCSubject(String name) {
        setName(name);
    }

    /**
     * This method specifies a ISubject, which has to take place
     * BEFORE this ISubject.
     */
    public void addBefore(ITCSubject s) {
        getBefores().add(s);
    }

    public void removeBefore(ITCSubject s) {
        getBefores().remove(s);
    }

    public Collection<ITCSubject> getBefores() {
        if (befores == null) {
            befores = new HashSet<ITCSubject>(5);
        }
        return befores;
    }

    /**
     * This method specifies a ISubject, which has to take place
     * AFTER this ISubject.
     */
    public void addFollow(ITCSubject ti) {
        getFollows().add(ti);
    }

    public void removeFollow(ITCSubject ti) {
        getFollows().add(ti);
    }

    public Collection<ITCSubject> getFollows() {
        if (follows == null) {
            follows = new HashSet<ITCSubject>(5);
        }
        return follows;
    }

    public ITCTimeInterval getSingleTI() {
        assert !getTimeIntervals().isEmpty();
        assert ((List) getTimeIntervals()).get(0) != null;
        return (ITCTimeInterval) ((List) getTimeIntervals()).get(0);
    }

    /**
     * This method counts the number of required but not satisfied IFeature's
     * for all ISubject's (of this ISubject's) which take place in
     * specified week.
     */
    public int getFeatureViolations(int week) {
        int counter = 0;
        for (ITimeInterval ti : getTimeIntervals()) {
            if (!ti.isUnplaced() && ti.getYearSet().isAssigned(week) && !ti.getRoom().getFeatures().containsAll(getFeatures())) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * This method counts the number of collisions against the previous
     * defined raster. But only for ISubject's which happen in specified
     * week.
     */
    public int getRasterViolations(int week) {
        IRaster refRaster = getWeekRaster(week).getAllowed();
        int counter = 0;
        for (ITimeInterval ti : getTimeIntervals()) {
            if (ti.getYearSet().isAssigned(week) && !ti.isUnplaced()) {
                int end = ti.getEndTime();
                for (int i = ti.getStartTime(); i < end; i++) {
                    if (!refRaster.isAssigned(i)) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    /**
     * This method counts the number of violations against the order.
     * I.e. if a ISubject should happen before another TI the counter
     * will increase if it is not the case.
     */
    public int getOrderViolations() {
        int counter = 0;
        int thisStartTime = getSingleTI().getStartTime();
        if (thisStartTime < 0) {
            return 0;
        }
        for (ISubject s : getBefores()) {
            for (ITimeInterval ti : s.getTimeIntervals()) {
                if (ti.getEndTime() > thisStartTime && !ti.isUnplaced()) {
                    counter++;
                }
            }
        }
        int endTime = getSingleTI().getEndTime();
        for (ISubject s : getFollows()) {
            for (ITimeInterval ti : s.getTimeIntervals()) {
                if (endTime > ti.getStartTime() && !ti.isUnplaced()) {
                    counter++;
                }
            }
        }
        return counter;
    }
}
