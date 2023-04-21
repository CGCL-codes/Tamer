package net.techwatch.guice.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import net.techwatch.guice.domain.Person;

public class PersonDaoImpl implements PersonDao {

    private static final Collection<Person> persons;

    static {
        persons = new ArrayList<Person>();
        Calendar c = Calendar.getInstance();
        c.set(1977, 0, 3);
        Person p1 = new Person();
        p1.setId(1l);
        p1.setFirstName("David");
        p1.setLastName("Durand");
        p1.setBirthDate(c.getTime());
        persons.add(p1);
        c.set(1976, 10, 4);
        Person p2 = new Person();
        p2.setId(2l);
        p2.setFirstName("Norbert");
        p2.setLastName("Dupont");
        p2.setBirthDate(c.getTime());
        persons.add(p2);
        c.set(1981, 7, 23);
        Person p3 = new Person();
        p3.setId(3l);
        p3.setFirstName("David");
        p3.setLastName("Durand");
        p3.setBirthDate(c.getTime());
        persons.add(p3);
    }

    public Collection<Person> findByFirstName(String firstName) {
        Collection<Person> result = new ArrayList<Person>();
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName)) result.add(person);
        }
        return result;
    }

    public Person findById(long id) {
        for (Person person : persons) {
            if (person.getId() == id) return person;
        }
        return null;
    }

    public Collection<Person> findByLastName(String lastName) {
        Collection<Person> result = new ArrayList<Person>();
        for (Person person : persons) {
            if (person.getLastName().equals(lastName)) result.add(person);
        }
        return result;
    }

    public Collection<Person> getAll() {
        return Collections.unmodifiableCollection(persons);
    }

    @Override
    public Person create(String lastName, String firstName, Date birthDate) {
        Person p = new Person();
        p.setLastName(lastName);
        p.setFirstName(firstName);
        p.setBirthDate(birthDate);
        return p;
    }
}
