package fr.gfi.gfinet.server.service;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fr.gfi.gfinet.server.ContactService;
import fr.gfi.gfinet.server.ContactServiceException;
import fr.gfi.gfinet.server.dao.ContactDao;
import fr.gfi.gfinet.server.error.ServiceMessageCode;
import fr.gfi.gfinet.server.info.Contact;

/**
 * Implementation of ContactService interface.
 * @author Long John
 * @since 25/10/07
 */
public class ContactServiceImpl implements ContactService {

    protected static final Log logger = LogFactory.getLog(ContactServiceImpl.class);

    protected ContactDao contactDao;

    /**
	 * @see fr.gfi.gfinet.server.ContactService#listContact()
	 */
    public List<Contact> listContacts() throws ContactServiceException {
        logger.info("Start of listContacts()");
        List<Contact> result = null;
        String message = null;
        Throwable exc = null;
        try {
            result = contactDao.list();
        } catch (Throwable ex) {
            message = ServiceMessageCode.COS_0003.description();
            exc = ex;
        }
        if (message != null) {
            throwException(message, exc);
        }
        logger.info("End of listContacts()");
        return result;
    }

    /**
	 * @see fr.gfi.gfinet.server.ContactService#saveContact(fr.gfi.gfinet.server.info.Contact)
	 */
    public void saveContact(Contact contact) throws ContactServiceException {
        logger.info("Start of saveContact() " + contact);
        String message = null;
        Throwable exc = null;
        try {
            if (contact == null) {
                message = ServiceMessageCode.COS_0001.description() + " " + ServiceMessageCode.GNR_0002.description();
            } else if (contact.getId() == null) {
                contactDao.persist(contact);
            } else {
                contactDao.update(contact);
            }
        } catch (Throwable ex) {
            message = ServiceMessageCode.COS_0001.description();
            exc = ex;
        }
        if (message != null) {
            throwException(message, exc);
        }
        logger.info("Start of saveContact(). " + contact);
    }

    /**
	 * @see fr.gfi.gfinet.server.ContactService#deleteContact(fr.gfi.gfinet.server.info.Contact)
	 */
    public void deleteContact(Long id) throws ContactServiceException {
        logger.info("Start of deleteContact(). id = " + id);
        String message = null;
        Throwable exc = null;
        Object[] msgArg = { id };
        try {
            Contact contact = contactDao.findById(id);
            if (contact == null) {
                message = ServiceMessageCode.COS_0002.description(msgArg) + " " + ServiceMessageCode.GNR_0004.description();
            } else {
                contactDao.remove(contact);
            }
        } catch (Throwable ex) {
            message = ServiceMessageCode.COS_0002.description(msgArg);
            exc = ex;
        }
        if (message != null) {
            throwException(message, exc);
        }
        logger.info("End of deleteContact(). id = " + id);
    }

    /**
	 * @see fr.gfi.gfinet.server.ContactService#getContact(java.lang.Long)
	 */
    public Contact getContact(Long id) throws ContactServiceException {
        logger.info("Start de getContact(). id = " + id);
        String message = null;
        Throwable exc = null;
        Object[] msgArg = { id };
        Contact contact = null;
        try {
            contact = contactDao.findById(id);
        } catch (Throwable ex) {
            message = ServiceMessageCode.COS_0004.description(msgArg);
            exc = ex;
        }
        if (message != null) {
            throwException(message, exc);
        }
        logger.info("End of getContact(). id = " + id);
        return contact;
    }

    /**
	 * @see fr.gfi.gfinet.server.ContactService#getContact(java.lang.String)
	 */
    public Contact getContact(String name) throws ContactServiceException {
        logger.info("Start of getContact(). name = " + name);
        String message = null;
        Throwable exc = null;
        Object[] msgArg = { name };
        Contact contacts = null;
        try {
            contacts = contactDao.findByName(name);
        } catch (Exception ex) {
            message = ServiceMessageCode.COS_0004.description(msgArg);
            exc = ex;
        }
        if (message != null) {
            throwException(message, exc);
        }
        logger.info("End of getContact(). name = " + name);
        return contacts;
    }

    /**
	 * Jette une exception avec le message donn� et �venutellement l'exception qui la provoqu�e.
	 * @param message
	 * @param exc
	 * @throws ContactServiceException
	 */
    public void throwException(String message, Throwable exc) throws ContactServiceException {
        if (exc == null) {
            logger.error(message);
        } else {
            logger.error(message, exc);
        }
        throw new ContactServiceException(message, exc);
    }

    /**
	 * @see fr.gfi.gfinet.server.ProjectService#searchContacts(java.util.HashMap)
	 */
    public void searchContacts(HashMap<String, Object> criterions) throws ContactServiceException {
    }

    /**
	 * @param obj the ContactDao to be injected
	 */
    public void setContactDao(ContactDao obj) {
        this.contactDao = obj;
    }
}
