package edu.ba.library.management.user;

import java.util.HashSet;
import java.util.Set;

public class Role {

    protected int id;

    protected String label;

    protected Set<PersonOfContact> personsOfContact = new HashSet<PersonOfContact>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<PersonOfContact> getPersonsOfContact() {
        return personsOfContact;
    }

    public void setPersonsOfContact(Set<PersonOfContact> personsOfContact) {
        this.personsOfContact = personsOfContact;
    }

    public void Role() {
    }

    public void Role(String label) {
        this.setLabel(label);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (obj instanceof Role) {
            return getLabel().equals(((Role) obj).getLabel());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getLabel().hashCode();
    }
}
