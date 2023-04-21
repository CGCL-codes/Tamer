package vqwiki;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a single reminder in a VQWiki reminders list.
 *
 * @author Robert E Brewer
 * @version 0.1
 */
public class WikiReminder implements java.io.Serializable {

    protected String userName = "";

    protected Date remindDate = new Date();

    /**
     *
     */
    public WikiReminder() {
    }

    /**
     *
     */
    public WikiReminder(String newUserName) {
        this.userName = newUserName;
    }

    /**
     *
     */
    public WikiReminder(String newUserName, Date newRemindDate) {
        this.userName = newUserName;
        this.remindDate = newRemindDate;
    }

    /**
     *
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     *
     */
    public void setUserName(String newUserName) {
        this.userName = newUserName;
    }

    /**
     *
     */
    public Date getRemindDate() {
        return this.remindDate;
    }

    /**
     *
     */
    public void setRemindDate(Date newRemindDate) {
        this.remindDate = newRemindDate;
    }

    /**
     *
     */
    public boolean remindDateEquals(Date dateToCompare) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(this.remindDate);
        cal2.setTime(dateToCompare);
        if (cal1.get(Calendar.DATE) != cal2.get(Calendar.DATE) || cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) || cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            return false;
        }
        return true;
    }

    /**
     *
     */
    public boolean equals(Object o) {
        if (!(o instanceof WikiReminder)) return false;
        WikiReminder c = (WikiReminder) o;
        if (!this.userName.equals(c.getUserName())) return false;
        return remindDateEquals(c.getRemindDate());
    }
}
