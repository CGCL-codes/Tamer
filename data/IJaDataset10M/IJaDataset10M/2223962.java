package gemini.basic.manager.impl;

import java.util.ArrayList;
import gemini.basic.dao.AddressDao;
import gemini.basic.dao.IdentityCardDao;
import gemini.basic.dao.PersonDao;
import gemini.basic.exception.IdentityExistedException;
import gemini.basic.manager.PersonManager;
import gemini.basic.model.Address;
import gemini.basic.model.IdentityCard;
import gemini.basic.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonManagerImpl implements PersonManager {

    private final Logger logger = LoggerFactory.getLogger(PersonManager.class);

    private PersonDao personDao;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    private IdentityCardDao identityCardDao;

    public void setIdentityCardDao(IdentityCardDao identityCardDao) {
        this.identityCardDao = identityCardDao;
    }

    private AddressDao addressDao;

    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public Person saveOrUpdate(Person person) throws IdentityExistedException {
        logger.debug("============== validate data");
        boolean existedIdentity = identityCardDao.existedIdentity(person.getIdentityCard().getNumber());
        if (existedIdentity) {
            throw new IdentityExistedException();
        }
        logger.debug("============== save or update person");
        Person result;
        if (!person.getAddress().isPersisted()) {
            logger.debug("============== save address");
            Address returnAdd = addressDao.saveOrUpdate(person.getAddress(), true);
            person.setAddress(returnAdd);
        }
        if (!person.getIdentityCard().isPersisted()) {
            logger.debug("============== save identity");
            IdentityCard returnIdentity = identityCardDao.saveOrUpdate(person.getIdentityCard(), true);
            person.setIdentityCard(returnIdentity);
        }
        if (person.getPerson() != null) {
            Person spouse = personDao.getById(person.getPerson().getId());
            person.setPerson(spouse);
            spouse.setPersons(new ArrayList<Person>(1));
            spouse.getPersons().add(person);
        }
        logger.debug("============== update to db");
        result = personDao.saveOrUpdate(person, true);
        return result;
    }

    @Override
    public Person getById(int personId) {
        Person result = personDao.getById(personId);
        return result;
    }
}
