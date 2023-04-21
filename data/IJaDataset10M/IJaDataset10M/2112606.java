package edu.fudan.cse.medlab.event.extraction;

import java.util.Date;

/**
 * �¼��ṹ��Ϣ
 * 
 * @author yiminghe
 * 
 */
public class EventInfo {

    /**
	 * �¼����
	 */
    private String eventName;

    public String getNormalLocation() {
        return normalLocation;
    }

    public void setNormalLocation(String normalLocation) {
        this.normalLocation = normalLocation;
    }

    /**
	 * �¼�����ص�
	 */
    private String location;

    /**
	 * ���򻯺�ĵص㣬ֻ��ʾָ���б�ĵص���ʽ 09-04-19
	 */
    private String normalLocation;

    /**
	 * �¼���ʼ�����ʱ��
	 */
    private Date[] times;

    /**
	 * �¼���Դ��ַ
	 */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date[] getTimes() {
        return times;
    }

    public void setTimes(Date[] times) {
        this.times = times;
    }

    public EventInfo(String eventName, String location, Date[] times, String url) {
        this.eventName = eventName;
        this.location = location;
        this.times = times;
        this.url = url;
    }

    public EventInfo() {
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof EventInfo)) return false;
        EventInfo info = (EventInfo) obj;
        if (!info.getEventName().equals(this.getEventName())) return false;
        if (!info.getLocation().equals(this.getLocation())) return false;
        if (info.getTimes() == null && this.getTimes() == null) return true; else if (info.getTimes() == null || this.getTimes() == null) return false;
        if (info.getTimes().length != this.getTimes().length) return false;
        if (info.getTimes().length > 0 && info.getTimes()[0] != null && !info.getTimes()[0].equals(this.getTimes()[0])) return false;
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("************************\n");
        sb.append("eventName :" + this.eventName + "  \n location :" + this.location + "  \n normalLocation :" + this.normalLocation + "  \n times :" + (this.times != null && this.times.length > 0 ? this.times[0] : " no time !") + " \n url : " + this.getUrl());
        sb.append("\n");
        return sb.toString();
    }
}
