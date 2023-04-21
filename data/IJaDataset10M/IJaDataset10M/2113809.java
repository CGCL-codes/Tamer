package util.schedule;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Schedule {

    private static final Logger logger = Logger.getLogger("ogs.schedule");

    private final SortedMap<Integer, SortedSet<ScheduleItem>> items = new TreeMap<Integer, SortedSet<ScheduleItem>>();

    private Date dateStopFrom = null;

    private Date dateStopTo = null;

    private Date dateArchive = null;

    private Date dateGenerate = null;

    private int turn = 1;

    public final boolean isTimeToArchive(Date date) {
        return dateArchive != null && date.after(dateArchive);
    }

    private SortedSet<ScheduleItem> getScheduleItems(int turn) {
        SortedMap<Integer, SortedSet<ScheduleItem>> head = items.headMap(turn + 1);
        if (head.isEmpty()) return null;
        return head.get(head.lastKey());
    }

    public final int[] getScheduleLimits() {
        int[] limits = new int[items.size()];
        int i = 0;
        for (int turn : items.keySet()) limits[i++] = turn;
        return limits;
    }

    public final Collection<ScheduleItem> getScheduleItemsFrom(int turn) {
        return Collections.unmodifiableCollection(items.get(turn));
    }

    public final void addScheduleItem(ScheduleItem item, int from, int to) {
        split(from);
        if (to > 0) split(to + 1);
        for (Map.Entry<Integer, SortedSet<ScheduleItem>> entry : items.entrySet()) {
            int turn = entry.getKey();
            if (from <= turn && (to <= 0 || turn <= to)) entry.getValue().add(item);
        }
    }

    private void split(int turn) {
        if (items.containsKey(turn)) return;
        SortedSet<ScheduleItem> treeSet = new TreeSet<ScheduleItem>();
        SortedMap<Integer, SortedSet<ScheduleItem>> head = items.headMap(turn);
        if (!head.isEmpty()) treeSet.addAll(items.get(head.lastKey()));
        items.put(turn, treeSet);
    }

    public final void clearScheduleItems() {
        items.clear();
    }

    /**
	 * Getter for property dateStopFrom.
	 *
	 * @return Value of property dateStopFrom.
	 */
    public final Date getDateStopFrom() {
        return dateStopFrom;
    }

    /**
	 * Getter for property dateStopTo.
	 *
	 * @return Value of property dateStopTo.
	 */
    public final Date getDateStopTo() {
        return dateStopTo;
    }

    public final void setDateStop(Date dateStopFrom, Date dateStopTo) {
        this.dateStopFrom = dateStopFrom;
        this.dateStopTo = dateStopTo;
    }

    public final boolean hasStopPeriod(Date date) {
        return dateStopFrom != null && (dateStopTo == null || dateStopTo.after(date));
    }

    public final boolean isDateInsideStopPeriod(Date date) {
        return dateStopFrom != null && date.after(dateStopFrom) && (dateStopTo == null || date.before(dateStopTo));
    }

    /**
	 * Getter for property dateArchive.
	 *
	 * @return Value of property dateArchive.
	 */
    public final Date getDateArchive() {
        return dateArchive;
    }

    /**
	 * Setter for property dateArchive.
	 *
	 * @param dateArchive New value of property dateArchive.
	 */
    public final void setDateArchive(Date dateArchive) {
        this.dateArchive = dateArchive;
        if (dateArchive != null) dateGenerate = null;
    }

    public Date getDateGenerate() {
        return dateGenerate;
    }

    public void setDateGenerate(Date date, int turn) {
        dateGenerate = date;
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isTimeToGenerate(Date date) {
        return dateGenerate != null && date.after(dateGenerate);
    }

    public boolean isTooLateToGenerate(Date date, int pause) {
        return dateGenerate != null && date.getTime() > dateGenerate.getTime() + (long) pause * 60000L;
    }

    public void updateNextScheduleDate(int turn) {
        updateNextScheduleDate(turn, null);
    }

    public void updateNextScheduleDate(int turn, Date after) {
        dateGenerate = calcNextScheduleDate(turn, after);
        this.turn = turn;
    }

    public Date calcNextScheduleDate(int turn, Date after) {
        if (items.isEmpty()) return null;
        SortedSet<ScheduleItem> curItems = getScheduleItems(turn);
        if (curItems == null || curItems.isEmpty()) return null;
        Date result = calcNextScheduleDate(after, curItems);
        SortedSet<ScheduleItem> prevItems = getScheduleItems(turn - 1);
        if (prevItems != null && !prevItems.isEmpty()) {
            Date datePrevGenerate = calcNextScheduleDate(after, prevItems);
            if (datePrevGenerate != null && datePrevGenerate.after(result)) result = datePrevGenerate;
        }
        List<Holiday> holidays = Holiday.getHolidays();
        while (true) {
            for (Holiday holiday : holidays) if (holiday.contains(result)) {
                result = calcNextScheduleDate(holiday.endDate(result), curItems);
                break;
            }
            if (!isDateInsideStopPeriod(result)) return result;
            if (dateStopTo == null) return null;
            result = calcNextScheduleDate(dateStopTo, curItems);
        }
    }

    private static Date calcNextScheduleDate(Date after, SortedSet<ScheduleItem> curItems) {
        Calendar calendar = Calendar.getInstance(Locale.ROOT);
        if (after != null) calendar.setTime(after);
        ScheduleItem from = new ScheduleItem(calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 1);
        SortedSet<ScheduleItem> tail = curItems.tailSet(from);
        ScheduleItem nextItem;
        if (tail.isEmpty()) {
            nextItem = curItems.first();
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        } else {
            nextItem = tail.first();
        }
        calendar.set(Calendar.DAY_OF_WEEK, nextItem.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, nextItem.getHour());
        calendar.set(Calendar.MINUTE, nextItem.getMinute());
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public Schedule copy() {
        Schedule sch = new Schedule();
        for (Map.Entry<Integer, SortedSet<ScheduleItem>> entry : items.entrySet()) sch.items.put(entry.getKey(), new TreeSet<ScheduleItem>(entry.getValue()));
        return sch;
    }

    public static Schedule getSchedule(File dir) {
        Schedule sch;
        try {
            sch = ScheduleIO.load(dir);
        } catch (Exception err) {
            logger.log(Level.SEVERE, "Load schedule for game " + dir.getName(), err);
            sch = new Schedule();
        }
        return sch;
    }

    public static void commitSchedule(File dir, Schedule sch) {
        try {
            ScheduleIO.save(dir, sch);
        } catch (Exception err) {
            logger.log(Level.SEVERE, "Save schedule for game " + dir.getName(), err);
        }
    }
}
