package com.esp.model;

/**
 * @author Echo
 *
 */
public class Buddy {

    String person_id = "unamed";

    String buddy_id = null;

    Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getBuddy_id() {
        return buddy_id;
    }

    public void setBuddy_id(String buddy_id) {
        this.buddy_id = buddy_id;
    }
}
