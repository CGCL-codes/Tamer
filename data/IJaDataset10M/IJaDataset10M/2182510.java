package ru.aslanov.schedule.model;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: Nov 25, 2009 5:52:45 PM
 *
 * @author Sergey Aslanov
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Group extends Entity {

    @NotPersistent
    private UnownReference<Dance> dance = new UnownReference<Dance>(Dance.class);

    @NotPersistent
    private UnownReference<Level> level = new UnownReference<Level>(Level.class);

    @NotPersistent
    private UnownReference<Teacher> teacher1 = new UnownReference<Teacher>(Teacher.class);

    @NotPersistent
    private UnownReference<Teacher> teacher2 = new UnownReference<Teacher>(Teacher.class);

    @NotPersistent
    private UnownReference<Location> location = new UnownReference<Location>(Location.class);

    @Persistent(mappedBy = "group")
    @Element(dependent = "true")
    @Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "dayOfWeekInt, startTime"))
    private List<ScheduleItem> scheduleItems;

    @Persistent
    private Date started;

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "comment")) })
    private I18nString comment = new I18nString();

    @Persistent
    private boolean isOpen;

    @Persistent
    private Boolean privates;

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "title")) })
    private I18nString title = new I18nString();

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "welcome")) })
    private I18nString welcome = new I18nString();

    @Persistent
    private Date until;

    @Persistent
    private Boolean hidden;

    @NotPersistent
    private UnownReference<GCalendar> calendar = new UnownReference<GCalendar>(GCalendar.class);

    @Persistent
    private Schedule schedule;

    @Persistent
    private String days;

    public Group() {
    }

    public Group(Dance dance, Level level, Date started, Teacher teacher1, Teacher teacher2, String comment, boolean open, Location location, List<ScheduleItem> scheduleItems, Boolean privates, String title, String welcome, Date until) {
        setDance(dance);
        setLevel(level);
        this.started = started;
        setTeacher1(teacher1);
        setTeacher2(teacher2);
        setComment(comment);
        isOpen = open;
        setLocation(location);
        this.scheduleItems = scheduleItems;
        this.privates = privates;
        setTitle(title);
        setWelcome(welcome);
        this.until = until;
    }

    @XmlAttribute
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(value = XmlDate2DateAdapter.class)
    public Date getStarted() {
        return started;
    }

    @XmlAttribute
    @XmlIDREF
    public Dance getDance() {
        getDanceKey();
        return dance.getObject();
    }

    public Dance getDance(PersistenceManager pm) {
        getDanceKey();
        return dance.getObject(pm);
    }

    @XmlAttribute
    @XmlIDREF
    public Level getLevel() {
        getLevelKey();
        return level.getObject();
    }

    public Level getLevel(PersistenceManager pm) {
        getLevelKey();
        return level.getObject(pm);
    }

    @XmlAttribute
    @XmlIDREF
    public Teacher getTeacher1() {
        getTeacher1Key();
        return teacher1.getObject();
    }

    public Teacher getTeacher1(PersistenceManager pm) {
        getTeacher1Key();
        return teacher1.getObject(pm);
    }

    @XmlAttribute
    @XmlIDREF
    public Teacher getTeacher2() {
        getTeacher2Key();
        return teacher2.getObject();
    }

    public Teacher getTeacher2(PersistenceManager pm) {
        getTeacher2Key();
        return teacher2.getObject(pm);
    }

    @XmlAttribute(name = "loc")
    @XmlIDREF
    public Location getLocation() {
        getLocationKey();
        return location.getObject();
    }

    public Location getLocation(PersistenceManager pm) {
        getLocationKey();
        return location.getObject(pm);
    }

    @XmlAttribute
    public String getComment() {
        return comment.getValueWithThreadLang();
    }

    @XmlAttribute(name = "open")
    public boolean isOpen() {
        return isOpen;
    }

    @XmlAttribute
    public String getWelcome() {
        return welcome.getValueWithThreadLang();
    }

    public void setWelcome(String welcome) {
        this.welcome.setValueWithThreadLang(welcome);
    }

    @XmlElement(name = "schedule")
    public List<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    @XmlAttribute
    public String getTitle() {
        return title.getValueWithThreadLang();
    }

    @XmlAttribute
    public Boolean isPrivates() {
        return privates;
    }

    @XmlAttribute
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(value = XmlDate2DateAdapter.class)
    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }

    public void setDance(Dance dance) {
        this.dance.setObject(dance);
    }

    public void setLevel(Level level) {
        this.level.setObject(level);
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public void setTeacher1(Teacher teacher1) {
        this.teacher1.setObject(teacher1);
    }

    public void setTeacher2(Teacher teacher2) {
        this.teacher2.setObject(teacher2);
    }

    public void setComment(String comment) {
        this.comment.setValueWithThreadLang(comment);
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setLocation(Location location) {
        this.location.setObject(location);
    }

    public void setScheduleItems(List<ScheduleItem> schedule) {
        this.scheduleItems = schedule;
    }

    public void setPrivates(Boolean privates) {
        this.privates = privates;
    }

    public void setTitle(String title) {
        this.title.setValueWithThreadLang(title);
    }

    @XmlTransient
    @Persistent
    public String getDanceKey() {
        return dance.getEncodedKey();
    }

    public void setDanceKey(String danceKey) {
        this.dance.setEncodedKey(danceKey);
    }

    @XmlTransient
    @Persistent
    public String getLevelKey() {
        return level.getEncodedKey();
    }

    public void setLevelKey(String levelKey) {
        this.level.setEncodedKey(levelKey);
    }

    @XmlTransient
    @Persistent
    public String getTeacher1Key() {
        return teacher1.getEncodedKey();
    }

    public void setTeacher1Key(String teacher1Key) {
        this.teacher1.setEncodedKey(teacher1Key);
    }

    @XmlTransient
    @Persistent
    public String getTeacher2Key() {
        return teacher2.getEncodedKey();
    }

    public void setTeacher2Key(String teacher2Key) {
        this.teacher2.setEncodedKey(teacher2Key);
    }

    @XmlTransient
    @Persistent
    public String getLocationKey() {
        return location.getEncodedKey();
    }

    public void setLocationKey(String locationKey) {
        this.location.setEncodedKey(locationKey);
    }

    @XmlTransient
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Persistent
    @XmlTransient
    public String getCalendarKey() {
        return calendar.getEncodedKey();
    }

    public void setCalendarKey(String gCalendarKey) {
        calendar.setEncodedKey(gCalendarKey);
    }

    @XmlAttribute
    @XmlIDREF
    public GCalendar getCalendar() {
        getCalendarKey();
        return calendar.getObject();
    }

    public GCalendar getCalendar(PersistenceManager pm) {
        getCalendarKey();
        return calendar.getObject(pm);
    }

    @XmlTransient
    public boolean isHidden() {
        return hidden != null && hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @XmlTransient
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void syncDays(String removedItemKey) {
        final List<ScheduleItem> scheduleItems1 = getScheduleItems();
        StringBuffer days = new StringBuffer();
        if (scheduleItems1 != null) {
            for (ScheduleItem scheduleItem : scheduleItems1) {
                if (removedItemKey != null && removedItemKey.equals(scheduleItem.getEncodedKey())) continue;
                if (days.length() > 0) {
                    days.append(",");
                }
                days.append(scheduleItem.getDayOfWeek());
            }
        }
        setDays(days.length() > 0 ? days.toString() : null);
    }

    @Override
    public String toString() {
        return "Group{" + "dance=" + dance + ", level=" + level + ", started='" + started + '\'' + ", teacher1=" + teacher1 + ", teacher2=" + teacher2 + ", comment='" + comment + '\'' + ", isOpen=" + isOpen + ", location=" + location + ", schedule=" + scheduleItems + ", privates=" + privates + ", title='" + title + '\'' + '}';
    }
}
