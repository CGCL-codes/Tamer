package org.slasoi.businessManager.common.api;

import java.util.Date;

public class AdjustmentNotificationType {

    private String notificationId;

    private String slaID;

    private String notifier;

    private EventType type;

    private NotificationType notification;

    private Date reportTime;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getSlaID() {
        return slaID;
    }

    public void setSlaID(String slaID) {
        this.slaID = slaID;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public NotificationType getNotification() {
        return notification;
    }

    public void setNotification(NotificationType notification) {
        this.notification = notification;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
}
