package com.ubb.damate.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the event database table.
 * 
 */
@Entity
@Table(name = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EventName", unique = true, nullable = false, length = 128)
    private String eventName;

    @OneToMany(mappedBy = "event")
    private Set<UserEvent> userEvents;

    public Event() {
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Set<UserEvent> getUserEvents() {
        return this.userEvents;
    }

    public void setUserEvents(Set<UserEvent> userEvents) {
        this.userEvents = userEvents;
    }
}
