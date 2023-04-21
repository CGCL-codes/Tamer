package net.techwatch.guice.dao;

import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import net.techwatch.guice.domain.Person;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PersonDaoJpaImpl implements PersonDao {

    public EntityManager getManager() {
        return manager;
    }

    @Inject
    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    private EntityManager manager;

    @Override
    public Collection<Person> findByFirstName(String firstName) {
        Query query = manager.createQuery("select p from Person as p where firstName=:name");
        query.setParameter("", firstName);
        return query.getResultList();
    }

    @Override
    public Person findById(long id) {
        return manager.find(Person.class, id);
    }

    @Override
    public Collection<Person> findByLastName(String lastName) {
        Query query = manager.createQuery("select p from Person as p where lastName=:name");
        query.setParameter("", lastName);
        return query.getResultList();
    }

    @Override
    public Collection<Person> getAll() {
        Query query = manager.createQuery("select p from Person as p");
        return query.getResultList();
    }

    @Override
    public Person create(String lastName, String firstName, Date birthDate) {
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        Person p = new Person();
        p.setLastName(lastName);
        p.setFirstName(firstName);
        p.setBirthDate(birthDate);
        manager.persist(p);
        tx.commit();
        return p;
    }
}
