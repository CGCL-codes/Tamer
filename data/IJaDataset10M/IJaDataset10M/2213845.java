package org.nakedobjects.example.pim.dom;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.value.DateTime;

public class Appointment extends AbstractDomainObject {

    private DateTime time;

    private String description;

    private Person with;

    private Email email;

    public String title() {
        return description;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
        objectChanged();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        objectChanged();
    }

    public Person getWith() {
        return with;
    }

    public void setWith(Person with) {
        this.with = with;
        objectChanged();
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
        objectChanged();
    }
}
