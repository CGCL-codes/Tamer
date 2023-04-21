package org.btb.timer.core;

import java.io.Serializable;

/**
 * Storage object for the timers data.
 * @author Stanislav Milev
 * @date 16.10.2008
 */
public class TaskO implements Serializable {

    private static final long serialVersionUID = 817955233404847856L;

    private String taskName;

    private String taskDescription;

    private int minutes;

    private int hours;

    private int days;

    private int seconds;

    /**
	 * Constructor.
	 */
    public TaskO() {
        taskName = "";
        taskDescription = "";
        minutes = 0;
        hours = 0;
        days = 0;
        seconds = 0;
    }

    /**
	 * Constructor.
	 * @param taskName
	 */
    public TaskO(String taskName) {
        this.taskName = taskName;
        taskDescription = "";
        minutes = 0;
        hours = 0;
        days = 0;
        seconds = 0;
    }

    /**
	 * Constructor.
	 * @param taskName
	 * @param minutes
	 * @param hours
	 * @param days
	 * @param seconds
	 */
    public TaskO(String taskName, int minutes, int hours, int days, int seconds) {
        this.taskName = taskName;
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;
        this.seconds = seconds;
        taskDescription = "";
    }

    /**
	 * Constructor.
	 * @param taskName
	 * @param minutes
	 * @param hours
	 * @param days
	 * @param taskDescription
	 * @param seconds
	 */
    public TaskO(String taskName, String taskDescription, int minutes, int hours, int days, int seconds) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;
        this.seconds = seconds;
    }

    /**
	 * @return the taskName
	 */
    public String getTaskName() {
        return taskName;
    }

    /**
	 * @param taskName the taskName to set
	 */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
	 * @return the minutes
	 */
    public int getMinutes() {
        return minutes;
    }

    /**
	 * @param minutes the minutes to set
	 */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
	 * @return the hours
	 */
    public int getHours() {
        return hours;
    }

    /**
	 * @param hours the hours to set
	 */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
	 * @return the days
	 */
    public int getDays() {
        return days;
    }

    /**
	 * @param days the days to set
	 */
    public void setDays(int days) {
        this.days = days;
    }

    public String toString() {
        return taskName;
    }

    /**
	 * @return the taskDescription
	 */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
	 * @param taskDescription the taskDescription to set
	 */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
	 * @return the seconds
	 */
    public int getSeconds() {
        return seconds;
    }

    /**
	 * @param seconds the seconds to set
	 */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
