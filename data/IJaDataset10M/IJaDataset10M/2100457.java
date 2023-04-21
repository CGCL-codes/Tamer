package org.azrul.epice.rest.dto;

import org.azrul.epice.domain.Person;

/**
 *
 * @author azrulhasni
 */
public class FindPersonResponse extends Response {

    private Person person;

    /**
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }
}
