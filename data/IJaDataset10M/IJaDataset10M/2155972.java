package org.wfp.rita.test.hibernate;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import org.wfp.rita.db.HibernateProxyValidator;
import org.wfp.rita.test.base.HibernateTestBase;

/**
 * ... we annotate our hibernate classes on field level with validator
 * annotations (getter and setter are generated in our project). Hibernate
 * validator then tries to retrive the value directly by accessing the field
 * it self. But if the instance is a proxied object (due to lazy loading),
 * fields don't hold the correct value (in fact they all set to null) and
 * validator then reports an invalid value if field is annotated with not
 * null validator.
 * 
 * @link http://opensource.atlassian.com/projects/hibernate/browse/HV-64
 * @link http://opensource.atlassian.com/projects/hibernate/browse/HV-76
 * 
 * Change HibernateTestBase to org.hibernate.test.annotations.TestCase to
 * run under Hibernate.
 *
 * @author Chris Wilson
 * @author Lucien Weller, I think (original testcase.tar.gz)
 */
public class HibernateValidatorFieldAnnotationsAndProxiesTest extends HibernateTestBase {

    @Entity(name = "person")
    @Table(name = "person")
    static class Person {

        @Id
        @Column(name = "id", updatable = false)
        @GenericGenerator(name = "generator", strategy = "increment")
        @GeneratedValue(generator = "generator")
        private int id;

        @Column(name = "firstname")
        @NotNull(message = "requiredValueMissing")
        @NotEmpty(message = "requiredValueMissing")
        private String firstname;

        @Column(name = "lastname")
        @NotNull(message = "requiredValueMissing")
        @NotEmpty(message = "requiredValueMissing")
        private String lastname;

        /**
         * Return the value of field {@link #id}.
         * 
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * Sets the value of field {@link #id}.
         * 
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Return the value of field {@link #firstname}.
         * 
         * @return the firstname
         */
        public String getFirstname() {
            return firstname;
        }

        /**
         * Sets the value of field {@link #firstname}.
         * 
         * @param firstname the firstname to set
         */
        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        /**
         * Return the value of field {@link #lastname}.
         * 
         * @return the lastname
         */
        public String getLastname() {
            return lastname;
        }

        /**
         * Sets the value of field {@link #lastname}.
         * 
         * @param lastname the lastname to set
         */
        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }

    protected Class[] getMappings() {
        return new Class[] { Person.class };
    }

    private Person DONALD_DUCK;

    public void setUp() throws Exception {
        super.setUp();
        Transaction transaction = null;
        try {
            Session session = openSession();
            transaction = session.beginTransaction();
            DONALD_DUCK = new Person();
            DONALD_DUCK.setFirstname("Donald");
            DONALD_DUCK.setLastname("Duck");
            session.save(DONALD_DUCK);
            transaction.commit();
            transaction = null;
        } finally {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * This test runs successfully.
     * 
     * @throws Exception
     */
    public void testSuccessful() {
        Session session = openSession();
        Person person = (Person) session.load(Person.class, DONALD_DUCK.getId(), LockMode.FORCE);
        Hibernate.initialize(person);
        Set cvs = validator.validate(person);
        assertEquals(cvs.toString(), 0, cvs.size());
        session.close();
    }

    /**
     * This test fails even if person is in fact valid.
     */
    public void testFailing() {
        Session session = openSession();
        Person person = (Person) session.load(Person.class, DONALD_DUCK.getId(), LockMode.NONE);
        Hibernate.initialize(person);
        Set cvs = validator.validate(person);
        assertEquals(cvs.toString(), 0, cvs.size());
        session.close();
    }

    /**
     * This test passes because it uses the workaround
     * @link HibernateProxyValidator instead of the default @link Validator.
     */
    public void testWorkaround() {
        Session session = openSession();
        Person person = (Person) session.load(Person.class, DONALD_DUCK.getId(), LockMode.NONE);
        Hibernate.initialize(person);
        Set cvs = HibernateProxyValidator.getDefaultValidator().validate(person);
        assertEquals(cvs.toString(), 0, cvs.size());
        session.close();
    }
}
