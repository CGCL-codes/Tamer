package org.iceinn.icefamily.model;

import java.util.LinkedList;
import java.util.List;
import org.iceinn.icefamily.model.element.Name;

/**
 * @author Lionel FLAHAUT
 * 
 */
public class ListOfPersons {

    private static ListOfPersons single = null;

    private List<Person> persons;

    private ListOfPersons() {
        persons = new LinkedList<Person>();
    }

    /**
	 * Singleton access
	 * @return
	 */
    public static synchronized ListOfPersons current() {
        if (single == null) {
            single = new ListOfPersons();
        }
        return single;
    }

    /**
	 * Utility method. Add a new date and returns reference to the new object.
	 * Reference may be <code>null</code> if add failed.
	 * 
	 * @param name
	 * @return
	 */
    public Person add(Name surname, Name name) {
        Person p = new Person(surname, name);
        if (add(p)) {
            return p;
        } else {
            return null;
        }
    }

    public boolean add(Person e) {
        return persons.add(e);
    }

    public boolean contains(Object o) {
        return persons.contains(o);
    }

    public Person get(int index) {
        return persons.get(index);
    }

    public Person remove(int index) {
        return persons.remove(index);
    }

    public boolean remove(Object o) {
        return persons.remove(o);
    }

    public Person set(int index, Person element) {
        return persons.set(index, element);
    }

    public int size() {
        return persons.size();
    }

    /**
	 * @return Returns the dates.
	 */
    public List<Person> getPersons() {
        return persons;
    }

    /**
	 * @param dates
	 *            The dates to set.
	 */
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
