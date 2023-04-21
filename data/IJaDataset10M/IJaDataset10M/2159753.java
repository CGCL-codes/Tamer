package ru.pit.homemoney.test.dao;

import org.junit.Test;
import ru.pit.homemoney.dao.PersonDao;
import ru.pit.homemoney.domain.value.PersonVO;
import ru.pit.homemoney.test.SpringBasedTest;

/**
 * Test Cases for {@link PersonDao}
 *
 * @author P.Salnikov (p.salnikov@gmail.com)
 * @version $Revision: 96 $
 */
public class PersonDaoTest extends SpringBasedTest {

    private PersonDao personDao = null;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    /**
   * get existent PersonVO
   */
    @Test
    public void testPersonGetOk() {
        Long id = new Long(1);
        PersonVO person = personDao.findById(id);
        assertNotNull("can't get existent person", person);
        assertEquals("index.id", id, person.getId());
        assertEquals("index.name", "Пётр", person.getName());
    }

    /**
   * save PersonVO
   *
   */
    @Test
    public void testPersonSave() {
        PersonVO person = new PersonVO();
        person.setName("Персона");
        personDao.save(person);
        PersonVO personTest = personDao.findById(person.getId());
        assertNotNull("can't get saved person", personTest);
    }

    /**
   * delete existent PersonVO
   */
    @Test
    public void testPersonDelete() {
        Long id = new Long(1);
        PersonVO person = personDao.findById(id);
        personDao.delete(person);
        PersonVO personTest = personDao.findById(id);
        assertNull("got deleted index", personTest);
    }
}
