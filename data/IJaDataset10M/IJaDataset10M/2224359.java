package com.dcivision.calendar.bean;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.TzId;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;

/**
  CalendarRecord.java

  This class is the data access bean for table "CALENDAR_PERFERENCE".

  @author          Scott Tong
  @company         DCIVision Limited
  @creation date   18/08/2003
  @version         $Revision: 1.19 $
*/
public class CalendarRecord extends AbstractBaseObject implements Comparable {

    public static final String REVISION = "$Revision: 1.19 $";

    static final long serialVersionUID = -8457145376991022643L;

    public static final String MEETING_LIST_CALENDAR_BY_WEEK = "MW";

    public static final String NAV_MODE_LIST_BY_DAY = "LD";

    public static final String NAV_MODE_LIST_BY_WEEK = "LW";

    public static final String NAV_MODE_LIST_BY_MONTH = "LM";

    public static final String NAV_MODE_LIST_TODO = "LT";

    public static final String NAV_MODE_LIST_EVENT = "LE";

    public static final String NAV_MODE_LIST_SEARCH = "LS";

    public static final String NAV_MODE_LIST = "L";

    public static final String EVENT_TYPE_CALENDAR_TODO = "CT";

    public static final String EVENT_TYPE_CALENDAR_EVENT = "CE";

    public static final String EVENT_TYPE_CALENDAR_MEETING = "CM";

    public static final String SHARE_TYPE_PUBLIC = "U";

    public static final String SHARE_TYPE_PRIVATE = "V";

    public static final String SHARE_TYPE_SHOW_TITLE = "T";

    public static final String REMINDER_TYPE_SYSTEM = "S";

    public static final String REMINDER_TYPE_EMAIL = "E";

    public static final String OBJECT_TYPE_WORKFLOW = "W";

    private Integer ownerID = null;

    private Integer sourceID = null;

    private Integer parentID = null;

    private Integer companyID = null;

    private Integer updateAlertID = null;

    private String sourceType = null;

    private String activityType = null;

    private String eventType = null;

    private String title = null;

    private String detail = null;

    private String purpose = null;

    private Timestamp datetime = null;

    private Timestamp endtime = null;

    private String isWholeDay = null;

    private String priority = null;

    private String status = null;

    private String venue = null;

    private String alertSubject = null;

    private String shareType = null;

    private Integer alertType = null;

    private Integer reminderAmount = null;

    private String reminderType = null;

    private String report = null;

    private String recordStatus = null;

    private String startDate = null;

    private String[] staffIDsToShare = null;

    private String isRecurrence = null;

    private String notifyWay = null;

    private String needReply = null;

    private Timestamp recurStartDate = null;

    private Timestamp recurEndDate = null;

    private Integer repeatTimes = null;

    private String recurType = null;

    private Integer repeatOn = null;

    private String repeatType = null;

    private String repeatTypeDay = null;

    private Integer occurType = null;

    private Integer occurWeekDay = null;

    private Integer occurMonth = null;

    private int subMeetingNum = 0;

    private boolean isInvitation = false;

    private boolean isAcceptance = false;

    private String objectType = null;

    private Integer objectID = null;

    public CalendarRecord() {
        super();
    }

    public Integer getOwnerID() {
        return (this.ownerID);
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getSourceID() {
        return (this.sourceID);
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getParentID() {
        return (this.parentID);
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Integer getCompanyID() {
        return (this.companyID);
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getUpdateAlertID() {
        return (this.updateAlertID);
    }

    public void setUpdateAlertID(Integer updateAlertID) {
        this.updateAlertID = updateAlertID;
    }

    public String getSourceType() {
        return (this.sourceType);
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getActivityType() {
        return (this.activityType);
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getEventType() {
        return (this.eventType);
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return (this.title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return (this.detail);
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPurpose() {
        return (this.purpose);
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Timestamp getDatetime() {
        return (this.datetime);
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public Timestamp getEndtime() {
        return (this.endtime);
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public String getIsWholeDay() {
        return (this.isWholeDay);
    }

    public void setIsWholeDay(String isWholeDay) {
        this.isWholeDay = isWholeDay;
    }

    public String getIsRecurrence() {
        return (this.isRecurrence);
    }

    public void setIsRecurrence(String isRecurrence) {
        this.isRecurrence = isRecurrence;
    }

    public String getPriority() {
        return (this.priority);
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return (this.status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenue() {
        return (this.venue);
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getShareType() {
        return (this.shareType);
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public Integer getAlertType() {
        return (this.alertType);
    }

    public void setAlertType(Integer alertType) {
        this.alertType = alertType;
    }

    public Integer getReminderAmount() {
        return (this.reminderAmount);
    }

    public void setReminderAmount(Integer reminderAmount) {
        this.reminderAmount = reminderAmount;
    }

    public String getReminderType() {
        return (this.reminderType);
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getReport() {
        return (this.report);
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getRecordStatus() {
        return (this.recordStatus);
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public void setStaffIDsToShare(String[] staffIDsToShare) {
        this.staffIDsToShare = staffIDsToShare;
    }

    public String[] getStaffIDsToShare() {
        return (this.staffIDsToShare);
    }

    public String getNotifyWay() {
        return (this.notifyWay);
    }

    public void setNotifyWay(String notifyWay) {
        this.notifyWay = notifyWay;
    }

    public String getNeedReply() {
        return (this.needReply);
    }

    public void setNeedReply(String needReply) {
        this.needReply = needReply;
    }

    public Timestamp getRecurStartDate() {
        return (this.recurStartDate);
    }

    public void setRecurStartDate(Timestamp recurStartDate) {
        this.recurStartDate = recurStartDate;
    }

    public Timestamp getRecurEndDate() {
        return (this.recurEndDate);
    }

    public void setRecurEndDate(Timestamp recurEndDate) {
        this.recurEndDate = recurEndDate;
    }

    public Integer getRepeatTimes() {
        return (this.repeatTimes);
    }

    public void setRepeatTimes(Integer repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public String getRecurType() {
        return (this.recurType);
    }

    public void setRecurType(String recurType) {
        this.recurType = recurType;
    }

    public Integer getRepeatOn() {
        return (this.repeatOn);
    }

    public void setRepeatOn(Integer repeatOn) {
        this.repeatOn = repeatOn;
    }

    public String getRepeatType() {
        return (this.repeatType);
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public String getRepeatTypeDay() {
        return (this.repeatTypeDay);
    }

    public void setRepeatTypeDay(String repeatTypeDay) {
        this.repeatTypeDay = repeatTypeDay;
    }

    public Integer getOccurType() {
        return (this.occurType);
    }

    public void setOccurType(Integer occurType) {
        this.occurType = occurType;
    }

    public Integer getOccurWeekDay() {
        return (this.occurWeekDay);
    }

    public void setOccurWeekDay(Integer occurWeekDay) {
        this.occurWeekDay = occurWeekDay;
    }

    public Integer getOccurMonth() {
        return (this.occurMonth);
    }

    public void setOccurMonth(Integer occurMonth) {
        this.occurMonth = occurMonth;
    }

    public int getSubMeetingNum() {
        return (this.subMeetingNum);
    }

    public void setSubMeetingNum(int subMeetingNum) {
        this.subMeetingNum = subMeetingNum;
    }

    public boolean getIsInvitation() {
        return (this.isInvitation);
    }

    public void setIsInvitation(boolean isInvitation) {
        this.isInvitation = isInvitation;
    }

    public String toString() {
        return (this.getClass().getName() + "[id:" + toString(id) + "|ownerID:" + toString(ownerID) + "|sourceID:" + toString(sourceID) + "|companyID:" + toString(companyID) + "|updateAlertID:" + toString(updateAlertID) + "|sourceType:" + toString(sourceType) + "|activityType:" + toString(activityType) + "|eventType:" + toString(eventType) + "|title:" + toString(title) + "|detail:" + toString(detail) + "|purpose:" + toString(purpose) + "|datetime:" + toString(datetime) + "|endtime:" + toString(endtime) + "|isWholeDay:" + toString(isWholeDay) + "|isRecurrence:" + toString(isRecurrence) + "|priority:" + toString(priority) + "|status:" + toString(status) + "|venue:" + toString(venue) + "|shareType:" + toString(shareType) + "|alertType:" + toString(alertType) + "|reminderAmount:" + toString(reminderAmount) + "|reminderType:" + toString(reminderType) + "|report:" + toString(report) + "|notifyWay:" + toString(notifyWay) + "|needReply:" + toString(needReply) + "|recordStatus:" + toString(recordStatus) + "|creatorID:" + toString(creatorID) + "|createDate:" + toString(createDate) + "|updaterID:" + toString(updaterID) + "|updateDate:" + toString(updateDate) + "|creatorName:" + toString(creatorName) + "|updaterName:" + toString(updaterName) + "]");
    }

    public Object clone() {
        CalendarRecord obj = new CalendarRecord();
        obj.setID(this.getID());
        obj.setOwnerID(this.getOwnerID());
        obj.setSourceID(this.getSourceID());
        obj.setParentID(this.getParentID());
        obj.setCompanyID(this.getCompanyID());
        obj.setUpdateAlertID(this.updateAlertID);
        obj.setSourceType(this.getSourceType());
        obj.setActivityType(this.getActivityType());
        obj.setEventType(this.getEventType());
        obj.setTitle(this.getTitle());
        obj.setDetail(this.getDetail());
        obj.setPurpose(this.getPurpose());
        obj.setDatetime(this.getDatetime());
        obj.setEndtime(this.getEndtime());
        obj.setIsWholeDay(this.getIsWholeDay());
        obj.setPriority(this.getPriority());
        obj.setStatus(this.getStatus());
        obj.setVenue(this.getVenue());
        obj.setShareType(this.getShareType());
        obj.setAlertType(this.getAlertType());
        obj.setReminderAmount(this.getReminderAmount());
        obj.setReminderType(this.getReminderType());
        obj.setReport(this.getReport());
        obj.setRecordStatus(this.getRecordStatus());
        obj.setCreatorID(this.getCreatorID());
        obj.setCreateDate(this.getCreateDate());
        obj.setUpdaterID(this.getUpdaterID());
        obj.setUpdateDate(this.getUpdateDate());
        obj.setCreatorName(this.getCreatorName());
        obj.setUpdaterName(this.getUpdaterName());
        obj.setNotifyWay(this.getNotifyWay());
        obj.setNeedReply(this.getNeedReply());
        obj.setIsRecurrence(this.getIsRecurrence());
        obj.setRecurStartDate(this.getRecurStartDate());
        obj.setRecurEndDate(this.getRecurEndDate());
        obj.setRepeatTimes(this.getRepeatTimes());
        obj.setRecurType(this.getRecurType());
        obj.setRepeatOn(this.getRepeatOn());
        obj.setRepeatType(this.getRepeatType());
        obj.setRepeatTypeDay(this.getRepeatTypeDay());
        obj.setOccurType(this.getOccurType());
        obj.setOccurWeekDay(this.getOccurWeekDay());
        obj.setOccurMonth(this.getOccurMonth());
        obj.setUpdateCount(this.getUpdateCount());
        obj.setSubMeetingNum(this.getSubMeetingNum());
        obj.setObjectID(this.getObjectID());
        obj.setObjectType(this.getObjectType());
        return (obj);
    }

    public boolean equals(Object obj) {
        return (obj != null && obj.getClass().getName().equals(this.getClass().getName()) && ((AbstractBaseObject) obj).getID() != null && ((AbstractBaseObject) obj).getID().equals(this.getID()));
    }

    public String getSubTitle(int digitCount) {
        String title = this.title;
        String tempTitle = this.title;
        if (title.length() < digitCount) return this.title; else tempTitle = title.substring(0, digitCount) + "...";
        return (tempTitle);
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public int compareTo(Object o) {
        CalendarRecord calendarRecord = (CalendarRecord) o;
        if (Utility.isEmpty(datetime) || Utility.isEmpty(calendarRecord.getDatetime())) {
            return 1;
        }
        return this.datetime.compareTo(calendarRecord.getDatetime());
    }

    public static boolean isActivity(CalendarRecord calendarRecord) {
        Timestamp currency = new Timestamp(new Date().getTime());
        if (Utility.isEmpty(calendarRecord.datetime) || Utility.isEmpty(calendarRecord.endtime)) {
            return false;
        }
        if (currency.compareTo(calendarRecord.datetime) > 0 && currency.compareTo(calendarRecord.endtime) < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getIsAcceptance() {
        return isAcceptance;
    }

    public void setIsAcceptance(boolean isAcceptance) {
        this.isAcceptance = isAcceptance;
    }

    public Calendar expToIcalendar() {
        Calendar calendar = new Calendar();
        try {
            String eventType = this.getEventType();
            String title = this.getTitle();
            String location = this.getVenue();
            String detail = this.getDetail();
            String priority = this.getPriority();
            String shareType = this.getShareType();
            Timestamp startTime = this.getDatetime();
            Timestamp endTime = this.getEndtime();
            java.util.Calendar startCal = java.util.Calendar.getInstance();
            java.util.Calendar endCal = java.util.Calendar.getInstance();
            startCal.setTime(startTime);
            endCal.setTime(endTime);
            int offset = startCal.get(java.util.Calendar.ZONE_OFFSET);
            startTime = new Timestamp(startCal.getTimeInMillis() - offset);
            endTime = new Timestamp(endCal.getTimeInMillis() - offset);
            Property method;
            if (CalendarRecord.EVENT_TYPE_CALENDAR_MEETING.equals(eventType)) {
                method = Method.REQUEST;
            } else {
                method = Method.PUBLISH;
            }
            Property iPriority;
            if ("1".equals(priority)) {
                iPriority = Priority.LOW;
            } else if ("3".equals(priority)) {
                iPriority = Priority.HIGH;
            } else {
                iPriority = Priority.MEDIUM;
            }
            String userEmail = UserInfoFactory.getUserEmailAddress(this.creatorID);
            URI emailUri;
            if (Utility.isEmpty(userEmail)) {
                emailUri = new URI("");
            } else {
                emailUri = new URI("MAILTO:" + userEmail);
            }
            Property iSharedType = "U".equals(shareType) ? Clazz.PUBLIC : Clazz.PRIVATE;
            calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(method);
            VEvent event = new VEvent(startTime, endTime, title);
            VTimeZone tz = VTimeZone.getDefault();
            TzId tzParam = new TzId(tz.getProperties().getProperty(Property.TZID).getValue());
            event.getProperties().add(new Uid(Math.random() + ""));
            event.getProperties().add(new Location(location));
            event.getProperties().add(new Description(detail));
            event.getProperties().add(iPriority);
            event.getProperties().add(iSharedType);
            if (!Utility.isEmpty(userEmail)) {
                event.getProperties().add(new Organizer(emailUri));
            }
            calendar.getComponents().add(event);
        } catch (Exception e) {
        }
        return calendar;
    }

    public byte[] toByteAry() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(expToIcalendar(), byteArrayOutputStream);
        } catch (Exception e) {
        }
        return byteArrayOutputStream.toByteArray();
    }

    public String getAlertSubject() {
        return alertSubject;
    }

    public void setAlertSubject(String alertSubject) {
        this.alertSubject = alertSubject;
    }
}
