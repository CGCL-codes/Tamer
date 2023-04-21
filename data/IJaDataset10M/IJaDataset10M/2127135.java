package net.mjrz.scheduler.task.constraints;

import net.mjrz.scheduler.task.Schedule;
import net.mjrz.scheduler.task.Schedule.DayOfWeek;
import net.mjrz.scheduler.task.Schedule.ScheduleConstraint;

/**
 * DayConstraint constraints the actual day of the week when a task will be
 * executed
 * 
 * @author mjrz
 * 
 */
public class DayConstraint implements Constraint {

    private static final long serialVersionUID = 1L;

    private DayOfWeek dayOfWeek;

    public DayConstraint(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public ScheduleConstraint getType() {
        return Schedule.ScheduleConstraint.DayConstraint;
    }
}
