package oopex.hibernate3.jpa.queries;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import oopex.hibernate3.jpa.queries.model.ForProfitCompany;
import oopex.hibernate3.jpa.queries.model.NonProfitCompany;
import oopex.hibernate3.jpa.queries.model.Person;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.ejb.HibernateEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DowncastsMain {

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
            Person person1 = new Person("Carla Peters");
            Person person2 = new Person("John Reader");
            NonProfitCompany company1 = new NonProfitCompany("Cats in need", "animal wellfare");
            ForProfitCompany company2 = new ForProfitCompany("Oil Ltd.", "oil production");
            person1.setEmployer(company1);
            person2.setEmployer(company2);
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
    public static void querywithjpql(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT p FROM Person p, ForProfitCompany company WHERE company.sector LIKE '%oil%' AND p.employer = company");
            List<Person> list = query.getResultList();
            for (Person person : list) {
                LOGGER.info(String.format("result: [%s, employer: [%s]]", person, person.getEmployer()));
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
            Criteria query = session.createCriteria(Person.class);
            DetachedCriteria downCastEmployer = DetachedCriteria.forClass(ForProfitCompany.class, "thisForProfitCompany").add(Restrictions.like("sector", "%oil%")).setProjection(Projections.id());
            query.createAlias("employer", "employer").add(Subqueries.propertyEq("employer.id", downCastEmployer));
            Collection<Person> collection = query.list();
            for (Person person : collection) {
                LOGGER.info(String.format("result: [%s, employer: [%s]]", person, person.getEmployer()));
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
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }
}
