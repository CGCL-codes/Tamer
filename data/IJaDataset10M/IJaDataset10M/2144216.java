package org.perfectjpattern.jee.integration.dao;

import java.util.*;
import junit.framework.*;
import org.perfectjpattern.example.datamodel.*;
import org.perfectjpattern.jee.api.integration.dao.*;
import org.slf4j.*;

/**
 * Test Suite for the {@link HibernateGenericDao} implementation.
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $ $Date: Nov 26, 2007 9:31:46 PM $
 */
public class TestJpaGenericDao extends TestCase {

    public void testCreate() {
        IBaseDao<Long, Person> myPersonDao = JpaDaoFactory.getInstance().createDao(Person.class);
        Person myGiovanni = new Person("Giovanni", 32);
        myPersonDao.create(myGiovanni);
        List<Person> myPersons = myPersonDao.findAll();
        assertNotNull("create seems not to work, record was not found.", myPersons);
        assertEquals("create seems not to work, record was not found.", 1, myPersons.size());
        assertEquals("create seems not to work, record was not found.", "Giovanni", myPersons.get(0).getName());
    }

    public void testUpdate() {
        IBaseDao<Long, Person> myPersonDao = JpaDaoFactory.getInstance().createDao(Person.class);
        Person myRosa = new Person("Rosa", 27);
        myPersonDao.create(myRosa);
        List<Person> myPersons = myPersonDao.findAll();
        Person myUpdate = myPersons.get(0);
        myUpdate.setAge(28);
        myPersonDao.update(myUpdate);
        myPersons = myPersonDao.findAll();
        assertNotNull("create seems not to work, record was not found.", myPersons);
        assertEquals("create seems not to work, record was not found.", "Rosa", myPersons.get(0).getName());
        assertEquals("create seems not to work, record was not found.", 28, myPersons.get(0).getAge());
    }

    public void testDelete() {
        IBaseDao<Long, Person> myPersonDao = JpaDaoFactory.getInstance().createDao(Person.class);
        Person myErnesto = new Person("Ernesto", 35);
        myPersonDao.create(myErnesto);
        myPersonDao.getTransaction().commit();
        Long myId = myErnesto.getId();
        Person myDelete = myPersonDao.findById(myId);
        myPersonDao.delete(myDelete);
        boolean myContains = myPersonDao.contains(myDelete);
        assertFalse("There must be no Elements left matching the deleted.", myContains);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        IBaseDao<Long, Person> myPersonDao = JpaDaoFactory.getInstance().createDao(Person.class);
        ITransaction myTransaction = myPersonDao.getTransaction();
        if (myTransaction.isActive()) {
            myTransaction.rollback();
        }
        if (myPersonDao.count() > 0) {
            myPersonDao.deleteAll();
            myTransaction.commit();
        }
    }

    /**
     * Provides logging services for this class.
     */
    @SuppressWarnings("unused")
    private final Logger theLogger = LoggerFactory.getLogger(this.getClass());
}
