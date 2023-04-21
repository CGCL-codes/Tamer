package net.onlinepresence.opos.domain.service;

import java.util.List;
import net.onlinepresence.opos.domain.Membership;
import net.onlinepresence.opos.domain.Person;

public interface Persons {

    public void update(Person p);

    public void addPerson(Person p);

    public void removePerson(Person p);

    public Person getPerson(String username);

    public List<Person> getPersons();

    void deleteApplicationMemberhsip(Membership mem);

    boolean existsPerson(String username);
}
