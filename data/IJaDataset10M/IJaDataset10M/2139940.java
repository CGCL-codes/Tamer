package ru.aslanov.schedule.model;

import javax.jdo.annotations.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by IntelliJ IDEA.
 * Created: Nov 25, 2009 5:51:20 PM
 *
 * @author Sergey Aslanov
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Location extends Entity {

    @Persistent
    private String id;

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "name")) })
    private I18nString name = new I18nString();

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "address")) })
    private I18nString address = new I18nString();

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "metro")) })
    private I18nString metro = new I18nString();

    @Persistent
    @Embedded(members = { @Persistent(name = "dbValue", columns = @Column(name = "comments")) })
    private I18nString comments = new I18nString();

    @Persistent
    private Schedule schedule;

    public Location() {
    }

    public Location(String id, String name, String address, String metro, String comments) {
        this.id = id;
        this.setName(name);
        this.setAddress(address);
        this.setMetro(metro);
        this.setComments(comments);
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return id != null ? id : getLocalId();
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return address.getValueWithThreadLang();
    }

    public String getName() {
        return name.getValueWithThreadLang();
    }

    public void setName(String name) {
        this.name.setValueWithThreadLang(name);
    }

    public String getAddress() {
        return address.getValueWithThreadLang();
    }

    public void setAddress(String address) {
        this.address.setValueWithThreadLang(address);
    }

    public String getMetro() {
        return metro.getValueWithThreadLang();
    }

    public void setMetro(String metro) {
        this.metro.setValueWithThreadLang(metro);
    }

    @XmlElement(name = "description")
    public String getComments() {
        return comments.getValueWithThreadLang();
    }

    public void setComments(String comments) {
        this.comments.setValueWithThreadLang(comments);
    }

    @XmlTransient
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
