package nekonet.prevalence.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author bionita
 */
public class NekoNet implements Serializable {

    private Collection<Person> persons;

    public NekoNet() {
        persons = new ArrayList<Person>();
    }

    public Collection<Person> getPersons() {
        return persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    public Person addPerson(Person newPerson) {
        persons.add(newPerson);
        return newPerson;
    }
}
