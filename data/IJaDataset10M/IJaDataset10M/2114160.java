package oopex.hibernate3.jpa.queries;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import oopex.hibernate3.jpa.queries.model.Address;
import oopex.hibernate3.jpa.queries.model.Person;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.HibernateEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectParametersMain {

    private static Person jesse;

    private static Person carla;

    private static Address oakwood;

    private static Address jamestown;

    private static final Logger LOGGER = LoggerFactory.getLogger("oopex.sample");

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        try {
            LOGGER.info("*** insert ***");
            insert(entityManagerFactory);
            LOGGER.info("*** query with JPQL ***");
            querywithjpql(entityManagerFactory);
            LOGGER.info("*** query with criteria ***");
            querywithcriteria(entityManagerFactory);
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
            jesse = new Person("Jesse", "James");
            oakwood = new Address("Main Road 12", "Oakwood");
            jesse.getAddresses().add(oakwood);
            oakwood.setPerson(jesse);
            carla = new Person("Carla", "Espinosa");
            jamestown = new Address("Ranch Road 12", "Jamestown");
            carla.getAddresses().add(jamestown);
            jamestown.setPerson(carla);
            entityManager.persist(jesse);
            entityManager.persist(carla);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static void querywithjpql(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query1 = entityManager.createQuery("SELECT a FROM Address a WHERE a.person = :person");
            query1.setParameter("person", jesse);
            Collection<Address> collection1 = (Collection<Address>) query1.getResultList();
            for (Address address : collection1) {
                LOGGER.info("Address of jesse: " + address);
            }
            Query query2 = entityManager.createQuery("SELECT p FROM Person p WHERE :address MEMBER OF p.addresses");
            query2.setParameter("address", oakwood);
            Collection<Person> collection2 = (Collection<Person>) query2.getResultList();
            for (Person person : collection2) {
                LOGGER.info("Person with address oakwood: " + person);
            }
        } finally {
            entityManager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static void querywithcriteria(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        HibernateEntityManager hibernateEntityManager = HibernateEntityManager.class.cast(entityManager);
        Session session = hibernateEntityManager.getSession();
        try {
            Criteria query1 = session.createCriteria(Address.class);
            query1.add(Restrictions.eq("person", jesse));
            Collection<Address> collection1 = query1.list();
            for (Address address : collection1) {
                LOGGER.info("Address of jesse: " + address);
            }
            Criteria query2 = session.createCriteria(Person.class);
            query2.createCriteria("addresses").add(Restrictions.idEq(oakwood.getId()));
            Collection<Person> collection2 = query2.list();
            for (Person person : collection2) {
                LOGGER.info("Person with address oakwood: " + person);
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
            Query pQuery = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> pCollection = (Collection<Person>) pQuery.getResultList();
            for (Person person : pCollection) {
                entityManager.remove(person);
            }
            Query aQuery = entityManager.createQuery("SELECT a FROM Address a");
            Collection<Address> aCollection = (Collection<Address>) aQuery.getResultList();
            for (Address address : aCollection) {
                entityManager.remove(address);
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
