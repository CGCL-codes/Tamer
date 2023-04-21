package Entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mara
 */
@Entity
@Table(name = "event")
@NamedQueries({ @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"), @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"), @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"), @NamedQuery(name = "Event.findByType", query = "SELECT e FROM Event e WHERE e.type = :type"), @NamedQuery(name = "Event.findByPlace", query = "SELECT e FROM Event e WHERE e.place = :place"), @NamedQuery(name = "Event.findByStartTime", query = "SELECT e FROM Event e WHERE e.startTime = :startTime"), @NamedQuery(name = "Event.findByEndTime", query = "SELECT e FROM Event e WHERE e.endTime = :endTime"), @NamedQuery(name = "Event.findBySendNotification", query = "SELECT e FROM Event e WHERE e.sendNotification = :sendNotification"), @NamedQuery(name = "Event.findByCapacity", query = "SELECT e FROM Event e WHERE e.capacity = :capacity"), @NamedQuery(name = "Event.findByCountOfMember", query = "SELECT e FROM Event e WHERE e.countOfMember = :countOfMember") })
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "place")
    private String place;

    @Basic(optional = false)
    @Column(name = "start_Time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Basic(optional = false)
    @Column(name = "end_Time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "send_Notification")
    private Boolean sendNotification;

    @Column(name = "capacity")
    private Integer capacity;

    @Basic(optional = false)
    @Column(name = "count_of_member")
    private int countOfMember;

    @ManyToMany(mappedBy = "eventCollection")
    private Collection<User> userCollection;

    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(String name, String type, String description, String place, Date startTime, Date endTime, Boolean sendNotification, User owner, Integer capacity) {
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.sendNotification = sendNotification;
        this.countOfMember = 1;
        this.user = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCountOfMember() {
        return countOfMember;
    }

    public void setCountOfMember(int countOfMember) {
        this.countOfMember = countOfMember;
    }

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Event[id=" + id + "]";
    }
}
