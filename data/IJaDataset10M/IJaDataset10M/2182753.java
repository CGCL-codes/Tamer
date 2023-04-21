package de.gstpl.data.set;

import de.gstpl.data.DBFactory;
import de.gstpl.data.IDBProperties;
import de.gstpl.data.IRoom;
import de.gstpl.data.ISubject;
import de.gstpl.data.ITimeInterval;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a factory for all known sets.
 * 
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class SetFactory {

    private static final SetFactory singleton = new SetFactory();

    private Map<Class, Integer> sizeMap;

    protected SetFactory() {
        sizeMap = new HashMap<Class, Integer>(5);
        IDBProperties prop = DBFactory.getDefaultDB().getDBProperties();
        sizeMap.put(IWeekTimeIntervalSet.class, prop.getDayNo() * prop.getDayDuration());
        sizeMap.put(IYearSet.class, prop.getWeekNo());
    }

    public static SetFactory get() {
        return singleton;
    }

    public static final Collection<? extends ITimeInterval> allTIs(IWeekTimeIntervalSet ws) {
        return ((WeekTISetImpl1) ws).getAll();
    }

    public PersonSet createPersonSet() {
        return new PersonSet();
    }

    public RoomSet createRoomSet() {
        return new RoomSet();
    }

    public ITimeIntervalGroup createTimeIntervalGroup(int startTime, List<? extends IRoom> availableRooms) {
        return new TimeIntervalGroupImpl(startTime, availableRooms);
    }

    public FeatureSet createFeatureSet() {
        return new FeatureSet();
    }

    /**
     * This method creates a stepfunction for the week timeinterval set.
     */
    public IStepFunction createStepFunctionForWTIS() {
        return new StepFunction(getSize(IWeekTimeIntervalSet.class), 1.5f);
    }

    /**
     * This method creates a stepfunction for the week timeinterval set.
     */
    public IStepFunction createStepFunction(int length, float loadFactor) {
        return new StepFunction(length, loadFactor);
    }

    IStepFunction createStepFunction(WeekTISetImpl1 weekTISet, int length, float loadFactor) {
        StepFunction sf = new StepFunction(length, loadFactor);
        sf.addAllTIsFrom(weekTISet);
        return sf;
    }

    /**
     * This method creates a stepfunction for a yearset.
     */
    public IStepFunction createStepFunction(int size) {
        return new StepFunction(size, 1.5f);
    }

    /**
     * This method creates a IStepFunction which is 0 if specified bs is false
     * and 1 if it is true.
     * The size of the returned IStepFunction will be bs.length
     */
    public IStepFunction createStepFunction(IRaster raster) {
        return new StepFunction(raster, 1.5f);
    }

    public IRaster createBooleanRaster() {
        return createBooleanRaster(50);
    }

    public IRaster createBooleanYearRaster() {
        return createBooleanRaster(getSize(IYearSet.class));
    }

    public IRaster createBooleanWeekRaster() {
        return createBooleanRaster(getSize(IWeekTimeIntervalSet.class));
    }

    public IRaster createBooleanRaster(int length) {
        return new RasterImpl1(length);
    }

    public IYearRaster createYearRaster() {
        return new YearRasterImpl1(getSize(IYearSet.class));
    }

    public IWeekRaster createWeekRaster() {
        return new WeekRasterImpl1(getSize(IWeekTimeIntervalSet.class));
    }

    /**
     * This method creates a IWeekTimeIntervalSet implementation, which is
     * assigned to specified week. I.e. a ITimeInterval's that is added to
     * the IWeekTimeIntervalSet will be rejected if its YearSet is not
     * assigned to this week.
     */
    public IWeekTimeIntervalSet createWeekTISet(int week) {
        int length = getSize(IWeekTimeIntervalSet.class);
        return new WeekTISetImpl1(week, length);
    }

    /**
     * This method returns a IYearSet implementation, which supports
     * serialization. It is large enough to store all assignments of a year
     * and it has all possible assignments.
     */
    public IYearSet createBaseYearSet() {
        int length = getSize(IYearSet.class);
        IYearSet ysi = new YearSetImpl2(length);
        ysi.setAssignments(0, length, true);
        return ysi;
    }

    /**
     * This method returns a IYearSet implementation, which supports
     * composing other YearSet into it.
     * It has no assignments at the beginning and is large enough to
     * store all assignments of a year.
     */
    public IYearSet createComposedYearSet() {
        return new YearSetImpl3(getSize(IYearSet.class));
    }

    public IYearSet createBaseYearSet(Object bytes) {
        return new YearSetImpl2(bytes);
    }

    public ISubjectGroup createSubjectGroup() {
        return new de.gstpl.algo.itc.ITCSubjectGroup();
    }

    public ISubjectGroup createSubjectGroup(ISubject s) {
        ISubjectGroup og = new de.gstpl.algo.itc.ITCSubjectGroup(2);
        og.add(s);
        return og;
    }

    int getSize(Class interfaze) {
        if (!interfaze.isInterface()) {
            throw new IllegalArgumentException("Specified class was not an interface:" + interfaze);
        }
        int ret = sizeMap.get(interfaze);
        assert ret > 0 : interfaze + " returns " + ret;
        return ret;
    }
}
