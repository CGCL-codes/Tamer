package org.starmx.config;

public class NotificationInfo {

    private MBeanInfo emitterMbean;

    private String eventType;

    private String eventClassName;

    private boolean synchronous;

    public NotificationInfo(String eventType, MBeanInfo mbeanInfo, boolean synchronous, String eventClassName) {
        this.emitterMbean = mbeanInfo;
        if (eventType != null) this.eventType = eventType.trim();
        this.synchronous = synchronous;
        this.eventClassName = eventClassName;
    }

    public String getEventType() {
        return eventType;
    }

    public MBeanInfo getEmitterMbean() {
        return emitterMbean;
    }

    public boolean isSynchronous() {
        return synchronous;
    }

    public String getEventClassName() {
        return eventClassName;
    }
}
