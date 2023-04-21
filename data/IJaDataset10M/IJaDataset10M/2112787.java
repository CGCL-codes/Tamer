package jgnash.engine.dao;

import java.util.List;
import jgnash.engine.recurring.Reminder;

/**
 * Reminder DAO Interface
 *
 * @author Craig Cavanaugh
 * @version $Id: RecurringDAO.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public interface RecurringDAO {

    public List<Reminder> getReminderList();

    public boolean addReminder(Reminder reminder);

    public void refreshReminder(Reminder reminder);

    public boolean updateReminder(Reminder reminder);
}
