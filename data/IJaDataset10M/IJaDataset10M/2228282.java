package net.sf.hipster.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A {@link List} that fetches dynamically all tasks that start or end in the
 * next 7 days.
 */
public class UpcomingList extends net.sf.hipster.model.List {

    /**
     * Instantiates a new upcoming list.
     */
    public UpcomingList() {
        super("Next 7 days");
    }

    public String getIconName() {
        return "upcoming";
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HipsterTreeNode> getChildren() {
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        c.add(Calendar.DATE, 7);
        Date nextWeek = c.getTime();
        return Library.getInstance().getSession().createQuery("from Task as task where " + "task.startDate between :today and :nextweek or " + "task.dueDate between :today and :nextweek").setDate("today", today).setDate("nextweek", nextWeek).setCacheable(true).list();
    }
}
