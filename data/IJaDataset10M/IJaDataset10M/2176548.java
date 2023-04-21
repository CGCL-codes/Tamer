package de.icehorsetools.dataAccess.hibernate.objects;

import java.util.HashSet;
import java.util.Set;
import de.icehorsetools.dataAccess.objects.Person;
import de.icehorsetools.dataAccess.objects.Role;

/**

 */
public class GroupHBM extends AbstractBaseEntity {

    /**
     *
     */
    private String description;

    /**
     * list of roles who own this group
     */
    private Set<Role> roles = new HashSet(0);

    /**
     * list of person who own this group
     */
    private Set<Person> persons = new HashSet(0);

    public GroupHBM() {
    }

    public GroupHBM(String description, Set roles, Set persons) {
        this.description = description;
        this.roles = roles;
        this.persons = persons;
    }

    /**
     *
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *      * list of roles who own this group
     */
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     *      * list of person who own this group
     */
    public Set<Person> getPersons() {
        return this.persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
}
