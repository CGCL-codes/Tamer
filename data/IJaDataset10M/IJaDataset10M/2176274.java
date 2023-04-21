package oopex.openjpa1.jpa.relationships;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import oopex.openjpa1.jpa.relationships.model.Person;
import oopex.openjpa1.jpa.relationships.model.Address;

public class ManyToManyBidirectionalMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        try {
            System.out.println("*** insert ***");
            insert(entityManagerFactory);
            System.out.println("*** query ***");
            query(entityManagerFactory);
            System.out.println("*** update ***");
            update(entityManagerFactory);
            System.out.println("*** query ***");
            query(entityManagerFactory);
            System.out.println("*** shuffle ***");
            shuffle(entityManagerFactory);
            System.out.println("*** query ***");
            query(entityManagerFactory);
            System.out.println("*** delete ***");
            delete(entityManagerFactory);
        } finally {
            entityManagerFactory.close();
            System.out.println("*** finished ***");
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
            address1.getPersons().add(person1);
            person1.getAddresses().add(address2);
            address2.getPersons().add(person1);
            person2.getAddresses().add(address3);
            address3.getPersons().add(person2);
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
                System.out.println("found: " + person);
                for (Address address : person.getAddresses()) {
                    System.out.println("  with address: " + address);
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
                address.getPersons().add(person);
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
            Set<Address> addresses1 = person1.getAddresses();
            Set<Address> addresses2 = person2.getAddresses();
            for (Address address : addresses1) {
                address.getPersons().clear();
            }
            for (Address address : addresses2) {
                address.getPersons().clear();
            }
            person1.setAddresses(addresses2);
            person2.setAddresses(addresses1);
            for (Address address : person1.getAddresses()) {
                address.getPersons().add(person1);
            }
            for (Address address : person2.getAddresses()) {
                address.getPersons().add(person2);
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
    private static void delete(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection = (Collection<Person>) query.getResultList();
            for (Person person : collection) {
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
