package com.tinywebgears.tuatara.core.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.appengine.api.datastore.Key;
import com.tinywebgears.tuatara.core.model.Business;
import com.tinywebgears.tuatara.core.model.ContactIF;
import com.tinywebgears.tuatara.core.model.Person;
import com.tinywebgears.tuatara.core.model.Phone;
import com.tinywebgears.tuatara.core.model.PhoneAssignment;
import com.tinywebgears.tuatara.webapp.gui.model.AddPhoneProperties;
import com.tinywebgears.tuatara.webapp.gui.model.PhoneAssignmentProperties;
import com.tinywebgears.tuatara.webapp.gui.model.PhoneProperties;

public class ContactRepository implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(ContactRepository.class);

    public Integer getCount(Key userKey, Set<Key> exportingUsers) throws DataPersistenceException {
        logger.debug("ContactRepository.getCount - userKey: " + userKey + " exportingUsers: " + exportingUsers);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Integer count = 0;
            Query query = pm.newQuery(Person.class);
            query.setOrdering("surname asc, givenNames asc");
            Integer personsCount = (Integer) RepositoryUtils.setFilterAndCount(query, null, null, null, userKey, exportingUsers);
            count += personsCount;
            query = pm.newQuery(Business.class);
            query.setOrdering("mainName asc, complementaryName asc");
            Integer businessesCount = (Integer) RepositoryUtils.setFilterAndCount(query, null, null, null, userKey, exportingUsers);
            count += businessesCount;
            return count;
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public Collection<ContactIF> getAll(Key userKey, Set<Key> exportingUsers) throws DataPersistenceException {
        logger.debug("ContactRepository.getAll - userKey: " + userKey + " exportingUsers: " + exportingUsers);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            List<ContactIF> allContacts = new ArrayList<ContactIF>();
            Query query = pm.newQuery(Person.class);
            query.setOrdering("surname asc, givenNames asc");
            List<Person> persons = (List<Person>) RepositoryUtils.setFilterAndQuery(query, null, null, null, userKey, exportingUsers);
            persons.size();
            allContacts.addAll(persons);
            query = pm.newQuery(Business.class);
            query.setOrdering("mainName asc, complementaryName asc");
            List<Business> businesses = (List<Business>) RepositoryUtils.setFilterAndQuery(query, null, null, null, userKey, exportingUsers);
            businesses.size();
            allContacts.addAll(businesses);
            return allContacts;
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public ContactIF getByKey(Key contactKey) throws DataPersistenceException {
        logger.debug("ContactRepository.getByKey - key: " + contactKey);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            pm.getFetchPlan().addGroup("getphones");
            ContactIF contact = getContact(contactKey, pm);
            return contact;
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public Integer deleteByKeys(Collection<Key> keys, Key userKey, Set<Key> exportingUsers) throws DataPersistenceException {
        logger.debug("ContactRepository.deleteByKey - keys: " + keys);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Collection<Key> personKeys = new ArrayList<Key>();
            Collection<Key> businessKeys = new ArrayList<Key>();
            for (Key key : keys) {
                if (key.getKind().equals(Person.class.getSimpleName())) personKeys.add(key); else if (key.getKind().equals(Business.class.getSimpleName())) businessKeys.add(key);
            }
            Map<String, Object> values = new HashMap<String, Object>();
            String filterString = "keysParam.contains(key)";
            String paramsString = Collection.class.getName() + " keysParam";
            values.put("keysParam", keys);
            Query query1 = pm.newQuery(Person.class);
            RepositoryUtils.setFilterAndDelete(query1, filterString, paramsString, values, userKey, exportingUsers);
            Query query2 = pm.newQuery(Business.class);
            RepositoryUtils.setFilterAndDelete(query2, filterString, paramsString, values, userKey, exportingUsers);
            return personKeys.size() + businessKeys.size();
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public void removeAllPhoneAssignments(Key contactKey) throws DataPersistenceException {
        logger.debug("ContactRepository.removeAllPhoneAssignments - contactKey: " + contactKey);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            ContactIF contact = getContact(contactKey, pm);
            contact.getPhoneAssignments().clear();
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public void modifyPhoneAssignment(Key contactKey, Key phoneAssignmentKey, PhoneAssignmentProperties properties, PhoneProperties phoneProperties) throws DataPersistenceException {
        logger.debug("ContactRepository.modifyPhoneAssignment - contactKey: " + contactKey + " phoneAssignmentKey: " + phoneAssignmentKey);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            ContactIF contact = getContact(contactKey, pm);
            for (PhoneAssignment entry : contact.getPhoneAssignments()) if (entry.getKey().equals(phoneAssignmentKey)) {
                if (properties != null) entry.setProperties(properties);
                if (phoneProperties != null && entry.getPhone() != null) entry.getPhone().setProperties(phoneProperties);
            }
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public ContactIF addPhones(Key contactKey, Collection<AddPhoneProperties> addPhonePropertiesList) throws DataPersistenceException {
        logger.debug("ContactRepository.addPhones - contactKey: " + contactKey);
        return addPhones(contactKey, addPhonePropertiesList, false);
    }

    public ContactIF replacePhones(Key contactKey, Collection<AddPhoneProperties> addPhonePropertiesList) throws DataPersistenceException {
        logger.debug("ContactRepository.replacePhones - contactKey: " + contactKey);
        return addPhones(contactKey, addPhonePropertiesList, true);
    }

    public ContactIF addPhones(Key contactKey, Collection<AddPhoneProperties> addPhonePropertiesList, Boolean replace) throws DataPersistenceException {
        logger.debug("ContactRepository.addPhones - contactKey: " + contactKey);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            ContactIF contact = getContact(contactKey, pm);
            if (replace) contact.getPhoneAssignments().clear();
            for (AddPhoneProperties addPhoneProperties : addPhonePropertiesList) {
                Phone phone = new Phone(addPhoneProperties.getPhone());
                PhoneAssignmentProperties phoneAssignmentProperties = addPhoneProperties.getPhoneAssignment();
                logger.debug("Adding " + addPhoneProperties.getPhone() + " as " + addPhoneProperties.getPhoneAssignment() + " for " + contactKey);
                if (phoneAssignmentProperties == null) phoneAssignmentProperties = new PhoneAssignmentProperties(null, null);
                PhoneAssignment phoneAssignment = new PhoneAssignment(phoneAssignmentProperties.getLabel(), phoneAssignmentProperties.getComment(), contact.getKey(), phone);
                phone.setPhoneAssignment(phoneAssignment);
                phoneAssignment.setOwnerKey(contact.getOwnerKey());
                contact.getPhoneAssignments().add(phoneAssignment);
            }
            return contact;
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    public void removePhoneAssignment(Key contactKey, Key phoneAssignmentKey) throws DataPersistenceException {
        logger.debug("ContactRepository.removePhoneAssignment - contactKey: " + contactKey + " phoneAssignmentKey: " + phoneAssignmentKey);
        PersistenceManagerFactory pmfInstance = PMFHolder.get();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            ContactIF contact = getContact(contactKey, pm);
            PhoneAssignment matchedEntry = null;
            for (PhoneAssignment entry : contact.getPhoneAssignments()) if (entry.getKey().equals(phoneAssignmentKey)) matchedEntry = entry;
            if (matchedEntry != null) contact.getPhoneAssignments().remove(matchedEntry);
        } catch (Throwable e) {
            throw new DataPersistenceException(e.getLocalizedMessage(), e);
        } finally {
            pm.close();
        }
    }

    private ContactIF getContact(Key contactKey, PersistenceManager pm) throws DataPersistenceException {
        if (contactKey.getKind().equals(Person.class.getSimpleName())) return (Person) pm.getObjectById(Person.class, contactKey); else if (contactKey.getKind().equals(Business.class.getSimpleName())) return (Business) pm.getObjectById(Business.class, contactKey); else throw new DataPersistenceException("Invalid type for a contact.");
    }
}
