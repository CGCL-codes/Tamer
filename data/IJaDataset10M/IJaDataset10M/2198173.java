package cz.cvut.phone.core.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Frantisek Hradil
 */
public class NewEventDTO implements Serializable {

    private Integer newEventID;

    private Integer personID;

    private Timestamp eventDate;

    private String eventText;

    private Timestamp expirationDate;

    public Integer getNewEventID() {
        return newEventID;
    }

    public void setNewEventID(Integer newEventID) {
        this.newEventID = newEventID;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
}
