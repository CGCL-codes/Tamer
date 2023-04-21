package com.ideo.sweetdevria.proxy.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Classe event
 */
public class Event implements Comparable {

    private Integer id;

    private String label;

    private String description;

    private String geo;

    private Integer type;

    private String tags;

    private Boolean fullDay;

    private Date startDate;

    private Date endDate;

    private String privacy;

    private String data;

    private Integer agendaId;

    private Integer ownerId;

    private List permissions = new ArrayList();

    private List concurrents = new ArrayList();

    ;

    private List infoPos = null;

    /**
	 * Constructor Event
	 * @param Label Label if the Event
	 * @param description Description of the Evenet
	 * @param geo Geolocalisation of the Event
	 * @param type Type of the Event 
	 * @param tags Tags of the Event
	 * @param fullDay True if a full Day Event, else false
	 * @param startDate Start Date of the Event
	 * @param endDate End Date of the Event
	 * @param privacy Privacy of the Event
	 * @param data Data of the Event
	 * @parma agendaId Agenda Id of the Event
	 * @param ownerId Owner Id of the Event
	 */
    public Event(String label, String description, String geo, Integer type, String tags, Boolean fullDay, Date startDate, Date endDate, String privacy, String data, Integer agendaId, Integer ownerId) {
        this.label = label;
        this.description = description;
        this.geo = geo;
        this.type = type;
        this.tags = tags;
        this.fullDay = fullDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.privacy = privacy;
        this.data = data;
        this.agendaId = agendaId;
        this.ownerId = ownerId;
    }

    /**
	 * Minimal Constructor Event
	 * @param startDate
	 * @param endDate
	 * @param privacy
	 * @param data
	 * @param agendaId
	 * @param ownerId
	 */
    public Event(Date startDate, Date endDate, String privacy, String data, Integer agendaId, Integer ownerId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.privacy = privacy;
        this.data = data;
        this.agendaId = agendaId;
        this.ownerId = ownerId;
    }

    /**
	 * Default constructor Event
	 */
    public Event() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCategory() {
        return tags;
    }

    public void setCategory(String category) {
        this.tags = category;
    }

    public Boolean getFullDay() {
        return fullDay;
    }

    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Integer agendaId) {
        this.agendaId = agendaId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public List getPermissions() {
        return permissions;
    }

    public void addPermission(String permission) {
        if (!permissions.contains(permission)) permissions.add(permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public static Date stringToDate(String sDate, String sFormat) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        return sdf.parse(sDate);
    }

    public boolean isEndDateAfterStartDate() {
        return endDate.after(startDate);
    }

    public void removeDetails() {
        this.label = "Private";
        this.description = "You are not allowed to see details";
        this.data = "{}";
    }

    public static List getFieldList() {
        return Arrays.asList(new String[] { "id", "label", "type", "description", "geo", "startDateYear", "startDateMonth", "startDateDay", "startDateHour", "startDateMinute", "endDateYear", "endDateMonth", "endDateDay", "endDateHour", "endDateMinute", "fullDay", "tags", "privacy", "data", "agendaId", "ownerId", "permissions", "infoPos" });
    }

    public List asList() {
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(this.startDate);
        Calendar endDate = new GregorianCalendar();
        endDate.setTime(this.endDate);
        return Arrays.asList(new Object[] { id, label, type, description, geo, new Integer(startDate.get(Calendar.YEAR)), new Integer(startDate.get(Calendar.MONTH)), new Integer(startDate.get(Calendar.DAY_OF_MONTH)), new Integer(startDate.get(Calendar.HOUR_OF_DAY)), new Integer(startDate.get(Calendar.MINUTE)), new Integer(endDate.get(Calendar.YEAR)), new Integer(endDate.get(Calendar.MONTH)), new Integer(endDate.get(Calendar.DAY_OF_MONTH)), new Integer(endDate.get(Calendar.HOUR_OF_DAY)), new Integer(endDate.get(Calendar.MINUTE)), fullDay, tags, Privacy.getPrivacyIndex(privacy), data, agendaId, ownerId, permissions, infoPos });
    }

    public int compareTo(Object evt) {
        Event event = (Event) evt;
        if (this.getStartDate().before(event.getStartDate())) {
            return -1;
        }
        if (this.getStartDate().after(event.getStartDate())) {
            return 1;
        } else return 0;
    }

    public boolean isMultiDay() {
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(this.startDate);
        Calendar endDate = new GregorianCalendar();
        endDate.setTime(this.endDate);
        return (endDate.get(GregorianCalendar.DATE) - startDate.get(GregorianCalendar.DATE)) > 0;
    }

    public List getConcurrents() {
        return concurrents;
    }

    public void setConcurrents(List concurrents) {
        this.concurrents = concurrents;
    }

    public void addConcurrent(Event concurrent) {
        this.concurrents.add(concurrent);
    }

    public List getInfoPos() {
        return infoPos;
    }

    public void setInfoPos(List infoPos) {
        this.infoPos = infoPos;
    }
}
