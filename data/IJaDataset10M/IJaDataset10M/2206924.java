package net.flitech.xplanner.service;

import net.flitech.xplanner.TestConstant;
import net.flitech.xplanner.XPlannerException;
import net.flitech.xplanner.dao.PersonDAO;
import net.flitech.xplanner.model.Person;
import net.flitech.xplanner.service.impl.PersonManagerImpl;
import net.flitech.xplanner.test.XPlannerMockTestCase;
import org.jmock.cglib.Mock;
import org.xplanner.soap.PersonData;

/**
 * Unit tests for {@link PersonManager}.
 * 
 * @author Steven Doolan
 * @see net.flitech.xplanner.service.PersonManager
 */
public class PersonManagerUnitTest extends XPlannerMockTestCase {

    private Mock mockXPlannerPersonDAO;

    private PersonDAO xPlannerPersonDAO;

    private PersonManager manager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockXPlannerPersonDAO = new Mock(PersonDAO.class);
        xPlannerPersonDAO = (PersonDAO) mockXPlannerPersonDAO.proxy();
        manager = new PersonManagerImpl();
        ((PersonManagerImpl) manager).setPersonDAO(xPlannerPersonDAO);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mockXPlannerPersonDAO.verify();
    }

    public void testFindPersonByUsername() throws XPlannerException {
        PersonData personData = new PersonData(2, null, TestConstant.TEST_EMAIL, TestConstant.TEST_INITIALS, TestConstant.TEST_NAME, null, TestConstant.TEST_USERID);
        mockXPlannerPersonDAO.expects(once()).method("findPersonByUsername").with(eq(TestConstant.TEST_USERID)).will(returnValue(personData));
        Person person = manager.findPersonByUsername(TestConstant.TEST_USERID);
        assertNotNull("person must not be null", person);
        Person expected = new Person(2);
        assertEquals(expected, person);
    }
}
