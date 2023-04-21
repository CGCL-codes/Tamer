package eu.roelbouwman.personLib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import eu.roelbouwman.personLib.dao.DAOFactory;
import eu.roelbouwman.personLib.model.address.EmailAddressHome;
import eu.roelbouwman.personLib.model.address.MailAddress;
import eu.roelbouwman.personLib.model.person.Gender;
import eu.roelbouwman.personLib.model.person.Group;
import eu.roelbouwman.personLib.model.person.Name;
import eu.roelbouwman.personLib.model.person.Person;
import eu.roelbouwman.personLib.model.person.PersonDAO;
import eu.roelbouwman.personLib.model.person.SurName;
import eu.roelbouwman.personLib.utils.LogUtil;

/**
 * The Class TestPerson.
 */
public class TestPerson extends TestCase {

    /** The db4o factory. */
    DAOFactory db4oFactory = DAOFactory.getDAOFactory(DAOFactory.DB4O);

    /** The p DAO. */
    PersonDAO pDAO = db4oFactory.getPersonDAO();

    /** The date format. */
    SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");

    /** The logger. */
    Logger logger = LogUtil.getLogger(TestPerson.class);

    /**
	 * Instantiates a new test person.
	 * 
	 * @param arg0 the arg0
	 */
    public TestPerson(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test insert person.
	 */
    public void testInsertPerson() {
        logger.debug("----------------------begin-------------------");
        Person p = null, p1 = null;
        try {
            p = new Person(new EmailAddressHome("roelbouwman@gmail.com"), dateFormat.parse("1-1-2007"), new Name("Roel", new SurName("Bouwman", null), null), "nl", Gender.MALE);
            p1 = new Person(new EmailAddressHome("lottebouwman@gmail.com"), null, new Name("Lotte", new SurName("Bouwman", null), null), "nl", Gender.FEMALE);
        } catch (ParseException e) {
        }
        MailAddress m = new MailAddress("Street", "1", null, "90109", "New York", null, null);
        p.addAddress(m);
        Group fam = new Group("family", "my relatives");
        Group work = new Group("work", "co workers");
        p.addGroup(fam);
        p.addGroup(work);
        p1.addGroup(fam);
        p1.addGroup(work);
        p1.addGroup(fam);
        p1.addGroup(work);
        p.setPhoto("/home/roel/fotos/roel.jpg");
        assertFalse(pDAO.updatePerson(p));
        assertFalse(pDAO.updatePerson(p1));
        logger.debug("----------------------end-------------------");
    }

    /**
	 * Test get all persons.
	 */
    public void testGetAllPersons() {
        logger.debug("----------------------begin-------------------");
        Set<Person> pers = new TreeSet<Person>();
        List<Person> p = pDAO.getPersons();
        for (Person p1 : p) {
            pers.add(p1);
        }
        assertEquals(pDAO.getPersons().size(), 2);
        logger.debug("----------------------end-------------------");
    }

    private Person getPerson() {
        return pDAO.findPersonBySurname("Bouw").get(0);
    }

    private Person getPersonByMail() {
        return pDAO.findPersonByEmail("roelbouwman@gmail.com").get(0);
    }

    /**
	 * Test update person.
	 */
    public void testUpdatePerson() {
        logger.debug("----------------------begin-------------------");
        Person p = getPerson();
        p.getName().setFirstName("Jan");
        pDAO.updatePerson(p);
        Iterator<Person> iter = pDAO.getPersons().iterator();
        while (iter.hasNext()) {
            Person pIter = iter.next();
            logger.debug(pIter.getName().getSurName().getSurName() + ", " + pIter.getName().getFirstName());
        }
        logger.debug("----------------------end-------------------");
    }

    /**
	 * Test find person by email.
	 */
    public void testFindPersonByEmail() {
        logger.debug("----------------------begin-------------------");
        assertNotNull(pDAO.findPersonByEmail("roelbouwman@gmail.com"));
        logger.debug("----------------------end-------------------");
    }

    /**
	 * Test get all groups.
	 */
    public void testGetAllGroups() {
        logger.debug("----------------------begin-------------------");
        Person p = getPersonByMail();
        assertEquals(p.getGroups().size(), 2);
        logger.debug("----------------------end-------------------");
    }

    public void testGetAllMailAdresses() {
        Iterator<MailAddress> iter = pDAO.getMailAddresses().iterator();
        while (iter.hasNext()) {
            logger.debug("address = " + iter.next().getPostalCode());
        }
    }

    /**
	 * Test delete person.
	 */
    public void testDeletePerson() {
        logger.debug("----------------------begin-------------------");
        Person pDelete = getPerson();
        assertTrue(pDAO.deletePerson(pDelete));
        logger.debug("----------------------end-------------------");
    }
}
