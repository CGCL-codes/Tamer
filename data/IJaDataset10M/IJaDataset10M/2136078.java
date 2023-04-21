package com.workplacesystems.queuj.schedule;

import com.workplacesystems.queuj.occurrence.ScheduleFactory;

/**
 *
 * @author dave
 */
public class ScheduleFactoryImpl extends ScheduleFactory {

    private static final ScheduleFactoryImpl instance = new ScheduleFactoryImpl();

    /**
     * Creates a new instance of ScheduleFactoryImpl
     */
    private ScheduleFactoryImpl() {
    }

    public static ScheduleFactory getInstance() {
        return instance;
    }

    @Override
    protected AbsoluteScheduleBuilder newAbsoluteScheduleBuilder(ScheduleSetter schedule_setter) {
        return new AbsoluteScheduleBuilder(schedule_setter);
    }

    @Override
    protected RelativeScheduleBuilder newRelativeScheduleBuilder(ScheduleSetter schedule_setter) {
        return new RelativeScheduleBuilder(schedule_setter);
    }

    @Override
    protected MinutelyScheduleBuilder newMinutelyScheduleBuilder(ScheduleSetter schedule_setter) {
        return new MinutelyScheduleBuilder(schedule_setter);
    }

    @Override
    protected HourlyScheduleBuilder newHourlyScheduleBuilder(ScheduleSetter schedule_setter) {
        return new HourlyScheduleBuilder(schedule_setter);
    }

    @Override
    protected DailyScheduleBuilder newDailyScheduleBuilder(ScheduleSetter schedule_setter) {
        return new DailyScheduleBuilder(schedule_setter);
    }

    @Override
    protected WeeklyScheduleBuilder newWeeklyScheduleBuilder(ScheduleSetter schedule_setter) {
        return new WeeklyScheduleBuilder(schedule_setter);
    }

    @Override
    protected MonthlyScheduleBuilder newMonthlyScheduleBuilder(ScheduleSetter schedule_setter) {
        return new MonthlyScheduleBuilder(schedule_setter);
    }

    @Override
    protected YearlyScheduleBuilder newYearlyScheduleBuilder(ScheduleSetter schedule_setter) {
        return new YearlyScheduleBuilder(schedule_setter);
    }
}
