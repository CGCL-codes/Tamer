package mpower_hibernate;

import java.util.*;

/**
 *
 * @author etkivbe
 */
public class UserPerson {

    private long mappingID;

    private User user;

    private Person person;

    private String system;

    public UserPerson() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public long getMappingID() {
        return mappingID;
    }

    public void setMappingID(long mappingID) {
        this.mappingID = mappingID;
    }
}
