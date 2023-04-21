package org.getalp.jibiki.dao;

import java.util.List;
import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.service.GenericManager;
import org.getalp.jibiki.model.Person;
import org.springframework.dao.DataAccessException;

public class PersonDaoTest extends BaseDaoTestCase {

    private PersonDao personDao = null;

    private GenericManager<Person, Long> personManager = null;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setPersonManager(GenericManager<Person, Long> personManager) {
        this.personManager = personManager;
    }

    public void testFindPersonByLastName() throws Exception {
        List<Person> people = personDao.findByLastName("Serasset");
        assertTrue(people.size() > 0);
    }

    public void testAddAndRemovePerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Country");
        person.setLastName("Bry");
        person = personDao.save(person);
        flush();
        person = personDao.get(person.getId());
        assertEquals("Country", person.getFirstName());
        assertNotNull(person.getId());
        log.debug("removing person...");
        personDao.remove(person.getId());
        flush();
        try {
            personDao.get(person.getId());
            fail("Person found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }

    public void testPersonManager() throws Exception {
        Person person = new Person();
        person.setFirstName("Country");
        person.setLastName("Bry");
        person = personManager.save(person);
        flush();
        person = personManager.get(person.getId());
        assertEquals("Country", person.getFirstName());
        assertNotNull(person.getId());
        log.debug("removing person...");
        personManager.remove(person.getId());
        flush();
        try {
            personManager.get(person.getId());
            fail("Person found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
}
