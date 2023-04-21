package oopex.eclipselink1.jpax.relationships;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import oopex.eclipselink1.jpax.relationships.model.Address;
import oopex.eclipselink1.jpax.relationships.model.Person;

public class OneToOneUnidirectionalJoinColumnMain {

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
            Person person = new Person();
            person.setFirstName("Jesse");
            person.setLastName("James");
            Address address = new Address();
            address.setStreet("Main Road 12");
            address.setCity("Oakwood");
            person.setAddress(address);
            entityManager.persist(person);
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
                System.out.println("  with address: " + person.getAddress());
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
                person.setFirstName("Carl");
                Address address = new Address();
                address.setCity("Austin");
                address.setStreet("Silver Avenue 21");
                person.setAddress(address);
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    private static void delete(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("DELETE FROM Person p");
            query.executeUpdate();
            query = entityManager.createQuery("DELETE FROM Address a");
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }
}
