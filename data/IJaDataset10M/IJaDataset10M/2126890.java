package oopex.openjpa2.jpa2.fields;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import oopex.openjpa2.jpa2.fields.model.Person;
import oopex.openjpa2.jpa2.fields.enums.Gender;

public class Enumerations2Main {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        try {
            System.out.println("*** insert ***");
            insert(entityManagerFactory);
            System.out.println("*** JPQL query ***");
            jpqlquery(entityManagerFactory);
            System.out.println("*** update ***");
            update(entityManagerFactory);
            System.out.println("*** criteria query ***");
            criteriaquery(entityManagerFactory);
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
            person.setName("Jesse James");
            person.setGender(Gender.MALE);
            entityManager.persist(person);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    public static void jpqlquery(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.gender = :gender", Person.class);
            query.setParameter("gender", Gender.MALE);
            Collection<Person> collection = query.getResultList();
            for (Person person : collection) {
                System.out.println("found: " + person);
            }
        } finally {
            entityManager.close();
        }
    }

    public static void criteriaquery(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            Root<Person> personR = query.from(Person.class);
            Predicate genderP = cb.equal(personR.get("gender"), cb.literal(Gender.UNKNOWN));
            query.select(personR).where(genderP);
            Collection<Person> collection = entityManager.createQuery(query).getResultList();
            for (Person person : collection) {
                System.out.println("found: " + person);
            }
        } finally {
            entityManager.close();
        }
    }

    private static void update(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p", Person.class);
            Collection<Person> collection = query.getResultList();
            for (Person person : collection) {
                person.setGender(Gender.UNKNOWN);
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
            entityManager.createQuery("DELETE FROM Person p").executeUpdate();
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }
}
