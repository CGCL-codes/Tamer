package org.meruvian.yama.form;

import java.util.Date;
import org.meruvian.yama.entity.Person;

/**
*
* @author vick
*/
public class PersonForm {

    public Person person = new Person();

    public String childId;

    public PersonForm() {
    }

    public PersonForm(Person person) {
        this.person = person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setId(String id) {
        person.setId(id);
    }

    public String getId() {
        return person.getId();
    }

    public void setFirstName(String firstName) {
        person.setFirstName(firstName);
    }

    public String getFirstName() {
        return person.getFirstName();
    }

    public void setLastName(String lastName) {
        person.setLastName(lastName);
    }

    public String getLastName() {
        return person.getLastName();
    }

    public void setGender(Boolean gender) {
        person.setGender(gender);
    }

    public Boolean getGender() {
        return person.getGender();
    }

    public void setBirthDate(Date birthDate) {
        person.setBirthDate(birthDate);
    }

    public Date getBirthDate() {
        return person.getBirthDate();
    }

    public void setAddress(String address) {
        person.setAddress(address);
    }

    public String getAddress() {
        return person.getAddress();
    }

    public void setStatus(String status) {
        person.setStatus(status);
    }

    public String getStatus() {
        return person.getStatus();
    }

    public void setRelation(Person relation) {
        person.setRelation(relation);
    }

    public Person getRelation() {
        return person.getRelation();
    }

    public void setParent(Person parent) {
        person.setParent(parent);
    }

    public Person getParent() {
        return person.getParent();
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
