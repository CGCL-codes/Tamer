package oopex.hibernate3.jpa.relationships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import oopex.hibernate3.jpa.relationships.model.Address;
import oopex.hibernate3.jpa.relationships.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManyToManyOrderedMain {

    private static final Logger LOGGER = LoggerFactory.getLogger("oopex.sample");

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        try {
            LOGGER.info("*** insert ***");
            insert(entityManagerFactory);
            LOGGER.info("*** query ***");
            query(entityManagerFactory);
            LOGGER.info("*** update ***");
            update(entityManagerFactory);
            LOGGER.info("*** query ***");
            query(entityManagerFactory);
            LOGGER.info("*** shuffle ***");
            shuffle(entityManagerFactory);
            LOGGER.info("*** query ***");
            query(entityManagerFactory);
            LOGGER.info("*** delete ***");
            delete(entityManagerFactory);
        } finally {
            entityManagerFactory.close();
            LOGGER.info("*** finished ***");
        }
    }

    private static void insert(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Person person1 = new Person();
            person1.setFirstName("Jesse");
            person1.setLastName("James");
            Person person2 = new Person();
            person2.setFirstName("John");
            person2.setLastName("Cline");
            Address address1 = new Address();
            address1.setStreet("Main Road 12");
            address1.setCity("Oakwood");
            Address address2 = new Address();
            address2.setStreet("Sunshine Boulevard 211");
            address2.setCity("Austin");
            Address address3 = new Address();
            address3.setStreet("Camino De Las Cabras 212");
            address3.setCity("San Jose");
            person1.getAddresses().add(address1);
            person1.getAddresses().add(address2);
            person2.getAddresses().add(address3);
            entityManager.persist(person1);
            entityManager.persist(person2);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static void query(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection = (Collection<Person>) query.getResultList();
            for (Person person : collection) {
                LOGGER.info("found: " + person);
                for (Address address : person.getAddresses()) {
                    LOGGER.info("  with address: " + address);
                }
            }
        } finally {
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static void update(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection = (Collection<Person>) query.getResultList();
            Address address = new Address();
            address.setStreet("Oak Lane 12");
            address.setCity("Dallas");
            for (Person person : collection) {
                person.getAddresses().add(address);
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static void shuffle(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection = (Collection<Person>) query.getResultList();
            Iterator<Person> iterator = collection.iterator();
            Person person1 = iterator.next();
            Person person2 = iterator.next();
            List<Address> addresses1 = person1.getAddresses();
            List<Address> addresses2 = person2.getAddresses();
            person1.setAddresses(addresses2);
            person2.setAddresses(addresses1);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    private static void delete(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection = (Collection<Person>) query.getResultList();
            for (Person person : collection) {
                List<Address> addresses = new ArrayList<Address>(person.getAddresses());
                person.getAddresses().clear();
                for (Address address : addresses) {
                    entityManager.remove(address);
                }
                entityManager.remove(person);
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }
}
