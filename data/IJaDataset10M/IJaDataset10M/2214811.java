package com.jettmarks.bkthn.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class MonthYear implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    private static int[] daysPerMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    @OneToOne(mappedBy = "currentMonthYear")
    private static MonthYear currentMonthYear = null;

    @Id
    @GeneratedValue
    private Integer id;

    private String monthYear;

    @OneToMany
    private Set<LogEntry> logEntries = new HashSet<LogEntry>(0);

    @OneToMany
    private Set<Pledge> pledges = new HashSet<Pledge>(0);

    public MonthYear() {
    }

    public MonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public MonthYear(String monthYear, Set<LogEntry> logEntries, Set<Pledge> pledges) {
        this.monthYear = monthYear;
        this.logEntries = logEntries;
        this.pledges = pledges;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonthYear() {
        return this.monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Set<LogEntry> getLogEntries() {
        return this.logEntries;
    }

    public void setLogEntries(Set<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    public Set<Pledge> getPledges() {
        return this.pledges;
    }

    public void setPledges(Set<Pledge> pledges) {
        this.pledges = pledges;
    }

    /**
   * Constructs a <code>String</code> with all attributes
   * in name = value format.
   *
   * @return a <code>String</code> representation 
   * of this object.
   */
    public String toString() {
        final String TAB = "  ";
        StringBuffer retValue = new StringBuffer();
        retValue.append("MonthYear ( ").append(super.toString()).append(TAB).append("id = ").append(this.id).append(TAB).append("monthYear = ").append(this.monthYear).append(TAB).append("logEntries = ").append(this.logEntries).append(TAB).append("pledges = ").append(this.pledges).append(TAB).append(" )");
        return retValue.toString();
    }

    public MonthYear getCurrentMonthYear() {
        if (currentMonthYear == null) {
            resetCurrentMonthYear();
        }
        return currentMonthYear;
    }

    public static void resetCurrentMonthYear() {
        currentMonthYear = new MonthYear(dateFormat.format(new Date()));
    }

    /**
   * Gets the remaining days in the current month.
   * 
   * @return
   */
    public static int getRemainingDays() {
        Calendar today = GregorianCalendar.getInstance();
        today.setTime(new Date());
        int month = today.get(Calendar.MONTH);
        int daysInMonth = daysPerMonth[month];
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        return daysInMonth - dayOfMonth + 1;
    }
}
