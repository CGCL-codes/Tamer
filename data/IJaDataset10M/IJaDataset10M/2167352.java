package org.demis.orc.person;

import java.util.Collection;

/**
  * DAO (Data Access Object) interface for Person.
  */
public interface PersonDAO {

    public Person findById(java.lang.String personId);

    public Collection<Person> findByExemple(Person person);

    public int findCount(final Person person);

    public void save(Person person);

    public void saveAll(final Collection<Person> persons);

    public void delete(Person person);

    public void deleteAll(final Collection<Person> persons);
}
