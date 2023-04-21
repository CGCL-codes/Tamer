package com.monad.homerun.action;

import java.util.ArrayList;
import com.monad.homerun.base.DataObject;
import com.monad.homerun.core.GlobalProps;

/**
 * The Schedule class holds an ordered list of schedule entries for a
 * designated interval (currently, only a week). These entries currently
 * include only time bindings, viz. actions set to start, run, or stop
 * at a designated time.
 */
public class Schedule extends DataObject {

    private static final long serialVersionUID = 3016537169590393315L;

    public static final String NO_NEXT_SCHEDULE = "none";

    public static final String SELF_NEXT_SCHEDULE = "self";

    private int schedLength = GlobalProps.MINS_PER_DAY * 7;

    private int repeat = 0;

    private String nextSched = SELF_NEXT_SCHEDULE;

    private ArrayList<TimeBinding> entryList = null;

    public Schedule() {
        entryList = new ArrayList<TimeBinding>();
    }

    public Schedule(String schedName) {
        super(schedName);
        entryList = new ArrayList<TimeBinding>();
    }

    public Schedule(String schedName, int repeat, String nextSched, TimeBinding[] entries) {
        super(schedName);
        this.repeat = repeat;
        this.nextSched = nextSched;
        loadEntries(entries);
    }

    public Schedule(Schedule schedule) {
        this(schedule.getName());
        this.repeat = schedule.repeat;
        this.nextSched = schedule.nextSched;
        loadEntries(schedule.getEntries());
    }

    /**
     * Loads schedule entries into this schedule.
     * 
     * @param entries
     *        array of TimeBinding entries
     */
    public void loadEntries(TimeBinding[] entries) {
        if (GlobalProps.DEBUG) {
            System.out.println("LoadEntries size: " + entries.length);
        }
        entryList = new ArrayList<TimeBinding>(entries.length);
        for (TimeBinding entry : entries) {
            addEntry(entry);
        }
    }

    /**
     * Adds a time binding as an entry in the schedule
     * 
     * @param entry a time binding
     */
    public void addEntry(TimeBinding entry) {
        timeInsert(entry, entryList);
    }

    /**
     * Removes a time binding entry from the schedule
     * 
     * @param entry the time binding to remove
     */
    public void removeEntry(TimeBinding entry) {
        entryList.remove(entry);
    }

    private void timeInsert(TimeBinding entry, ArrayList<TimeBinding> list) {
        int size = list.size();
        if (size > 0) {
            int index = size - 1;
            short entryTime = entry.getTime();
            short compTime = list.get(index).getTime();
            if (entryTime >= compTime) {
                list.add(index + 1, entry);
            } else {
                boolean added = false;
                while (compTime > entryTime && index > 0) {
                    compTime = list.get(--index).getTime();
                    if (entryTime >= compTime) {
                        list.add(index + 1, entry);
                        added = true;
                        break;
                    }
                    if (index == 0) {
                        break;
                    }
                }
                if (!added) {
                    list.add(0, entry);
                }
            }
        } else {
            list.add(entry);
        }
    }

    /**
     * Returns all the schedule entries in a time-ordered array
     * 
     * @return a time-ordered array of time bindings
     */
    public TimeBinding[] getEntries() {
        return entryList.toArray(new TimeBinding[0]);
    }

    /**
     * ReturnS the index of the first entry with the indicated time,
     * or -1 if there are none.
     * 
     * @param time
     *        schedule entry time in minute-of-week format
     * @return the first index in the schedule for this time,
     *         or -1
     */
    public int getFirstIndexAt(short time) {
        int size = entryList.size();
        if (size == 0) {
            return -1;
        }
        int testIdx = entryList.size() / 2;
        short testTime = entryList.get(testIdx).getTime();
        if (GlobalProps.DEBUG) {
            System.out.println("GFI time: " + time + " testTime: " + testTime + " testIdx: " + testIdx);
        }
        if (size == 1) {
            return (testTime == time) ? testIdx : -1;
        }
        if (testTime < time) {
            while (testTime < time) {
                if (++testIdx > size - 1) {
                    return -1;
                }
                testTime = entryList.get(testIdx).getTime();
            }
            return (testTime == time) ? testIdx : -1;
        } else if (testTime > time) {
            while (testTime > time) {
                if (--testIdx < 0) {
                    return -1;
                }
                testTime = entryList.get(testIdx).getTime();
            }
            if (testTime < time) {
                return -1;
            }
            if (testIdx == 0) {
                return 0;
            }
            while (testTime == time) {
                if (--testIdx < 0) {
                    break;
                }
                testTime = entryList.get(testIdx).getTime();
            }
            return testIdx + 1;
        } else {
            while (testTime == time) {
                if (--testIdx == 0) {
                    break;
                }
                testTime = entryList.get(testIdx).getTime();
            }
            return testIdx + 1;
        }
    }

    /**
     * Returns a schedule entry at the passed index
     * 
     * @param index
     *        the index in the schedule
     * @return binding
     *         the time binding at the index, or null if no such index
     */
    public TimeBinding getEntry(int index) {
        if (index >= 0 && index < entryList.size()) {
            return entryList.get(index);
        }
        return null;
    }

    /**
     * Removes a schedule entry at specified index position
     * 
     * @param index
     *        position in schedule array
     */
    public void removeEntry(int index) {
        if (index >= 0 && index < entryList.size()) {
            entryList.remove(index);
        }
    }

    /**
     * Returns the repetition count of the schedule
     * 
     * @return count
     *         how many times the schedule should be repeated
     */
    public int getRepeat() {
        return repeat;
    }

    /**
     * Returns the number of entries in the schedule
     * 
     * @return num
     *         the number of schedule entries
     */
    public int getNumEntries() {
        return entryList.size();
    }

    /**
     * Returns the number of minutes in the schedule
     * 
     * @return length
     *         the length of the schedule in minutes
     */
    public int getLength() {
        return schedLength;
    }

    /**
     * Assigns the number of repetitions for this schedule
     * to run (unless stopped). When completed, the
     * next schedule is run if defined.
     * 
     * @param repeat
     *        number of times to repeat schedule
     */
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    /**
     * Returns the name of the schedule that follows this one
     * 
     * @return successor
     *         the schedule to succeed this one
     */
    public String getNextSchedule() {
        return nextSched;
    }

    /**
     * Assigns the successor schedule. Use 'none' for no successor,
     * or 'self' for the schedule to succeed itself.
     * 
     * @param nextSched
     *        the name of the next schedule
     */
    public void setNextSchedule(String nextSched) {
        this.nextSched = nextSched;
    }
}
