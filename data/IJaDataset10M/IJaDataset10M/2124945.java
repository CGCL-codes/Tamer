package net.flitech.xplanner.service.impl;

import org.xplanner.soap.PersonData;
import net.flitech.xplanner.XPlannerException;
import net.flitech.xplanner.XPlannerObject;
import net.flitech.xplanner.dao.PersonDAO;
import net.flitech.xplanner.model.Person;
import net.flitech.xplanner.service.PersonManager;

/**
 * A collection of methods for working with {@link Person} objects.
 * 
 * @author Steven Doolan
 * @see net.flitech.xplanner.service.PersonManager
 * @see net.flitech.xplanner.service.PersonManagerUnitTest
 */
public class PersonManagerImpl extends XPlannerObject implements PersonManager {

    private PersonDAO personDAO;

    public void setPersonDAO(PersonDAO xPlannerPersonDAO) {
        personDAO = xPlannerPersonDAO;
    }

    public Person findPersonByUsername(String username) throws XPlannerException {
        PersonData personData = personDAO.findPersonByUsername(username);
        return new Person(personData.getId());
    }
}
