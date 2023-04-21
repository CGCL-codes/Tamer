package oopex.eclipselink1.jpa.relationships;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import oopex.eclipselink1.jpa.relationships.model.Address;
import oopex.eclipselink1.jpa.relationships.model.Person;

public class OneToManyNaturallyOrderedMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        try {
            System.out.println("*** insert ***");
            insert(entityManagerFactory);
            System.out.println("*** query ***");
            query(entityManagerFactory);
            System.out.println("*** update ***");
            update(entityManagerFactory);
            System.out.println("*** verify ***");
            verify(entityManagerFactory);
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
            Person person1 = new Person("Anne", "Smith");
            Address address1 = new Address("Main Road 2", "Oakwood");
            address1.setPerson(person1);
            person1.getAddresses().add(address1);
            Address address2 = new Address("Sunshine Boulevard 3", "Austin");
            address2.setPerson(person1);
            person1.getAddresses().add(address2);
            entityManager.persist(person1);
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
            for (Person person : collection) {
                Address address = new Address("First Road 1", "Addison");
                address.setPerson(person);
                person.getAddresses().add(0, address);
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
    public static void verify(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection = (Collection<Person>) query.getResultList();
            for (Person person : collection) {
                System.out.println("found: " + person);
                List<String> expectedCities = new ArrayList<String>(Arrays.asList("Addison", "Oakwood", "Austin"));
                for (Address address : person.getAddresses()) {
                    String expected = expectedCities.remove(0);
                    if (!expected.equals(address.getCity())) {
                        System.out.format("  with address: %s (ERROR expected city: %s%n)", address, expected);
                    } else {
                        System.out.format("  with address: %s%n", address);
                    }
                }
            }
        } finally {
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
