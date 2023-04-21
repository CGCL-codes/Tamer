package com.brrus.icbinabr.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

@Entity
public class TimeSlot {

    @Id
    @Column(name = "timeslot_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    @ManyToOne(optional = false)
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Position position;

    private Date startDateTime;

    private Date endDateTime;

    private int numberVolunteers;

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<TimeSlotSignup> timeSlotSignups = new HashSet<TimeSlotSignup>();

    public TimeSlot() {
    }

    public TimeSlot(Date startDateTime, Date endDateTime, int numberVolunteers) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.numberVolunteers = numberVolunteers;
    }

    public Long getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public int getNumberVolunteers() {
        return numberVolunteers;
    }

    public Set<TimeSlotSignup> getTimeSlotSignups() {
        return timeSlotSignups;
    }

    public Object[] getTimeSlotSignupsArray() {
        return timeSlotSignups.toArray();
    }

    public int getNumOpenSignups() {
        return (numberVolunteers - timeSlotSignups.size());
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    void setPosition(Position position) {
        this.position = position;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setNumberVolunteers(int numberVolunteers) {
        if (numberVolunteers < timeSlotSignups.size()) {
            throw new RuntimeException("Cannot reduce number of volunteers needed below current number of signups");
        }
        this.numberVolunteers = numberVolunteers;
    }

    void setTimeSlotSignups(Set<TimeSlotSignup> timeSlotSignups) {
        this.timeSlotSignups = timeSlotSignups;
    }

    public TimeSlotSignup addSignup(TimeSlotSignup timeSlotSignup) {
        if (isFull()) {
            throw new RuntimeException("TimeSlot is already completely full.");
        }
        timeSlotSignups.add(timeSlotSignup);
        return timeSlotSignup;
    }

    public void removeSignup(TimeSlotSignup timeSlotSignup) {
        if (!timeSlotSignups.contains(timeSlotSignup)) {
            throw new IllegalArgumentException("UserAccount is not signed up for the time slot.");
        }
        timeSlotSignups.remove(timeSlotSignup);
    }

    public boolean isFull() {
        return (timeSlotSignups.size() >= numberVolunteers);
    }

    public boolean isNoTimeSlotSignups() {
        return (timeSlotSignups.size() == 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        TimeSlot pObj = (TimeSlot) obj;
        return (this.getId().equals(pObj.getId()));
    }

    public String toXml() {
        Document response = DocumentFactory.getInstance().createDocument();
        response.add(toDocElement());
        return response.asXML();
    }

    public Element toDocElement() {
        Element root = DocumentFactory.getInstance().createElement("timeSlot");
        root.addAttribute("id", String.valueOf(getId()));
        root.addAttribute("position", getPosition().getId().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z");
        root.addAttribute("startDateTime", sdf.format(getStartDateTime()));
        root.addAttribute("endDateTime", sdf.format(getEndDateTime()));
        root.addAttribute("numVolunteers", String.valueOf(getNumberVolunteers()));
        return root;
    }

    public static TimeSlot fromXml(String xml) {
        try {
            Document doc = DocumentHelper.parseText(xml);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z");
            Date startDateTime = sdf.parse(doc.selectSingleNode("/timeSlot/@startDateTime").getStringValue());
            Date endDateTime = sdf.parse(doc.selectSingleNode("/timeSlot/@endDateTime").getStringValue());
            int numVolunteers = Integer.parseInt(doc.selectSingleNode("/timeSlot/@numVolunteers").getStringValue());
            TimeSlot timeSlot = new TimeSlot(startDateTime, endDateTime, numVolunteers);
            timeSlot.id = Long.parseLong(doc.selectSingleNode("/timeSlot/@id").getStringValue());
            return timeSlot;
        } catch (DocumentException x) {
            throw new RuntimeException("Invalid item xml: " + xml, x);
        } catch (ParseException x) {
            throw new RuntimeException("Invalid item xml: " + xml, x);
        }
    }
}
