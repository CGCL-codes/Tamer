package com.firescrum.dailymeeting.service;

public interface IServiceDailyMeetingScheduler {

    public void startScheduler();

    public void stopScheduler();

    public void scheduleDailyMeeting(Long productId, int scheduleTimeHour, int scheduleTimeMin, int reminderMin);
}
