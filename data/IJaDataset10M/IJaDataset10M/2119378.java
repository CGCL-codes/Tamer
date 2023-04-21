package org.jaffa.applications.mylife.admin.components.accesshistorymaintenance.tx;

import org.apache.log4j.Logger;
import java.util.*;
import org.jaffa.persistence.UOW;
import org.jaffa.persistence.Criteria;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.DomainObjectNotFoundException;
import org.jaffa.exceptions.DuplicateKeyException;
import org.jaffa.exceptions.MultipleDomainObjectsFoundException;
import org.jaffa.exceptions.IncompleteKeySpecifiedException;
import org.jaffa.datatypes.ValidationException;
import org.jaffa.applications.mylife.admin.components.accesshistorymaintenance.IAccessHistoryMaintenance;
import org.jaffa.applications.mylife.admin.components.accesshistorymaintenance.dto.*;
import org.jaffa.applications.mylife.admin.domain.AccessHistory;
import org.jaffa.applications.mylife.admin.domain.AccessHistoryMeta;
import org.jaffa.applications.mylife.admin.domain.Content;
import org.jaffa.applications.mylife.admin.domain.ContentMeta;

/** Maintenance for AccessHistory objects.
*/
public class AccessHistoryMaintenanceTx implements IAccessHistoryMaintenance {

    private static Logger log = Logger.getLogger(AccessHistoryMaintenanceTx.class);

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy() {
    }

    /** Persists a new instance of AccessHistory.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The key for the object that gets created.
     */
    public AccessHistoryMaintenanceCreateOutDto create(AccessHistoryMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Input: " + (input != null ? input.toString() : null));
            }
            uow = new UOW();
            preprocess(uow, input);
            duplicateCheck(uow, input);
            validateForeignObjects(uow, input);
            AccessHistory domain = createDomain(uow, input);
            AccessHistoryMaintenanceCreateOutDto output = buildCreateOutDto(uow, domain);
            uow.commit();
            if (log.isDebugEnabled()) {
                log.debug("Output: " + (output != null ? output.toString() : null));
            }
            return output;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Returns the details for AccessHistory.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details. A null indicates, the object was not found.
     */
    public AccessHistoryMaintenanceRetrieveOutDto retrieve(AccessHistoryMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Input: " + (input != null ? input.toString() : null));
            }
            uow = new UOW();
            preprocess(uow, input);
            AccessHistory domain = load(uow, input);
            AccessHistoryMaintenanceRetrieveOutDto output = buildRetrieveOutDto(uow, input, domain);
            if (log.isDebugEnabled()) {
                log.debug("Output: " + (output != null ? output.toString() : null));
            }
            return output;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Updates an existing instance of AccessHistory.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The key for the object that gets created.
     */
    public void update(AccessHistoryMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Input: " + (input != null ? input.toString() : null));
            }
            uow = new UOW();
            preprocess(uow, input);
            AccessHistory domain = load(uow, input);
            validateForeignObjects(uow, input);
            updateDomain(uow, input, domain);
            uow.commit();
            if (log.isDebugEnabled()) {
                log.debug("Successfully updated the domain object");
            }
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Deletes an existing instance of AccessHistory.
     * @param input The key values for the domain object to be deleted.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The key for the object that gets created.
     */
    public void delete(AccessHistoryMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Input: " + (input != null ? input.toString() : null));
            }
            uow = new UOW();
            preprocess(uow, input);
            AccessHistory domain = load(uow, input);
            deleteDomain(uow, input, domain);
            uow.commit();
            if (log.isDebugEnabled()) {
                log.debug("Successfully deleted the domain object");
            }
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Preprocess the input for the create method. */
    private void preprocess(UOW uow, AccessHistoryMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Ensure that a duplicate record is not created. */
    private void duplicateCheck(UOW uow, AccessHistoryMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        Criteria criteria = new Criteria();
        criteria.setTable(AccessHistoryMeta.getName());
        criteria.addCriteria(AccessHistoryMeta.CONTENT_ID, input.getContentId());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_ON, input.getAccessedOn());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_BY, input.getAccessedBy());
        Collection col = uow.query(criteria);
        if (col != null && !col.isEmpty()) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DuplicateKeyException(AccessHistoryMeta.getLabelToken()));
            throw appExps;
        }
    }

    /** Create the domain object and add it to the UOW. */
    private AccessHistory createDomain(UOW uow, AccessHistoryMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        AccessHistory domain = new AccessHistory();
        ApplicationExceptions appExps = null;
        try {
            domain.updateAccessedOn(input.getAccessedOn());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(AccessHistoryMeta.META_ACCESSED_ON.getLabelToken());
            appExps.add(e);
        }
        try {
            domain.updateAccessedBy(input.getAccessedBy());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(AccessHistoryMeta.META_ACCESSED_BY.getLabelToken());
            appExps.add(e);
        }
        try {
            domain.updateRating(input.getRating());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(AccessHistoryMeta.META_RATING.getLabelToken());
            appExps.add(e);
        }
        try {
            domain.updateContentId(input.getContentId());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(AccessHistoryMeta.META_CONTENT_ID.getLabelToken());
            appExps.add(e);
        }
        if (appExps != null && appExps.size() > 0) throw appExps;
        uow.add(domain);
        return domain;
    }

    /** Create the CreateOutDto. */
    private AccessHistoryMaintenanceCreateOutDto buildCreateOutDto(UOW uow, AccessHistory domain) throws FrameworkException, ApplicationExceptions {
        AccessHistoryMaintenanceCreateOutDto output = new AccessHistoryMaintenanceCreateOutDto();
        output.setContentId(domain.getContentId());
        output.setAccessedOn(domain.getAccessedOn());
        output.setAccessedBy(domain.getAccessedBy());
        return output;
    }

    /** Preprocess the input for the retrieve method. */
    private void preprocess(UOW uow, AccessHistoryMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Retrieve the domain object. */
    private AccessHistory load(UOW uow, AccessHistoryMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions {
        AccessHistory domain = null;
        Criteria criteria = new Criteria();
        criteria.setTable(AccessHistoryMeta.getName());
        criteria.addCriteria(AccessHistoryMeta.CONTENT_ID, input.getContentId());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_ON, input.getAccessedOn());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_BY, input.getAccessedBy());
        Iterator itr = uow.query(criteria).iterator();
        if (itr.hasNext()) domain = (AccessHistory) itr.next();
        if (domain == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(AccessHistoryMeta.getLabelToken()));
            throw appExps;
        }
        return domain;
    }

    /** Create the RetrieveOutDto. */
    private AccessHistoryMaintenanceRetrieveOutDto buildRetrieveOutDto(UOW uow, AccessHistoryMaintenanceRetrieveInDto input, AccessHistory domain) throws FrameworkException, ApplicationExceptions {
        AccessHistoryMaintenanceRetrieveOutDto output = new AccessHistoryMaintenanceRetrieveOutDto();
        output.setAccessedOn(domain.getAccessedOn());
        output.setAccessedBy(domain.getAccessedBy());
        output.setRating(domain.getRating());
        addForeignObjectsToRetrieveOut(uow, input, domain, output);
        return output;
    }

    /** Preprocess the input for the update method. */
    private void preprocess(UOW uow, AccessHistoryMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Retrieve the domain object. */
    private AccessHistory load(UOW uow, AccessHistoryMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
        AccessHistory domain = null;
        Criteria criteria = new Criteria();
        criteria.setTable(AccessHistoryMeta.getName());
        criteria.addCriteria(AccessHistoryMeta.CONTENT_ID, input.getContentId());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_ON, input.getAccessedOn());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_BY, input.getAccessedBy());
        Iterator itr = uow.query(criteria).iterator();
        if (itr.hasNext()) domain = (AccessHistory) itr.next();
        if (domain == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(AccessHistoryMeta.getLabelToken()));
            throw appExps;
        }
        return domain;
    }

    /** Update the domain object and add it to the UOW. */
    private void updateDomain(UOW uow, AccessHistoryMaintenanceUpdateInDto input, AccessHistory domain) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = null;
        try {
            domain.updateRating(input.getRating());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(AccessHistoryMeta.META_RATING.getLabelToken());
            appExps.add(e);
        }
        if (appExps != null && appExps.size() > 0) throw appExps;
        uow.update(domain);
    }

    /** Preprocess the input for the delete method. */
    private void preprocess(UOW uow, AccessHistoryMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Retrieve the domain object. */
    private AccessHistory load(UOW uow, AccessHistoryMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions {
        AccessHistory domain = null;
        Criteria criteria = new Criteria();
        criteria.setTable(AccessHistoryMeta.getName());
        criteria.addCriteria(AccessHistoryMeta.CONTENT_ID, input.getContentId());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_ON, input.getAccessedOn());
        criteria.addCriteria(AccessHistoryMeta.ACCESSED_BY, input.getAccessedBy());
        Iterator itr = uow.query(criteria).iterator();
        if (itr.hasNext()) domain = (AccessHistory) itr.next();
        if (domain == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(AccessHistoryMeta.getLabelToken()));
            throw appExps;
        }
        return domain;
    }

    /** Delete the domain object from the domain. */
    private void deleteDomain(UOW uow, AccessHistoryMaintenanceDeleteInDto input, AccessHistory domain) throws FrameworkException, ApplicationExceptions {
        uow.delete(domain);
    }

    /** Add foreign objects to AccessHistoryMaintenanceRetrieveOutDto */
    private void addForeignObjectsToRetrieveOut(UOW uow, AccessHistoryMaintenanceRetrieveInDto input, AccessHistory domain, AccessHistoryMaintenanceRetrieveOutDto output) throws FrameworkException, ApplicationExceptions {
        {
            output.setContentId(domain.getContentId());
        }
    }

    /** This will validate the Foreign Objects. */
    private void validateForeignObjects(UOW uow, AccessHistoryMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = null;
        {
            if (input.getContentId() != null) {
                Criteria criteriaContent = new Criteria();
                criteriaContent.setTable(ContentMeta.getName());
                criteriaContent.addCriteria(ContentMeta.CONTENT_ID, input.getContentId());
                Collection col = uow.query(criteriaContent);
                if (col.isEmpty()) {
                    if (appExps == null) appExps = new ApplicationExceptions();
                    appExps.add(new DomainObjectNotFoundException(ContentMeta.getLabelToken()));
                } else if (col.size() != 1) {
                    if (appExps == null) appExps = new ApplicationExceptions();
                    appExps.add(new MultipleDomainObjectsFoundException(ContentMeta.getLabelToken()));
                }
            }
        }
        if (appExps != null) throw appExps;
    }

    /** This will validate the Foreign Objects. */
    private void validateForeignObjects(UOW uow, AccessHistoryMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = null;
        {
            if (input.getContentId() != null) {
                Criteria criteriaContent = new Criteria();
                criteriaContent.setTable(ContentMeta.getName());
                criteriaContent.addCriteria(ContentMeta.CONTENT_ID, input.getContentId());
                Collection col = uow.query(criteriaContent);
                if (col.isEmpty()) {
                    if (appExps == null) appExps = new ApplicationExceptions();
                    appExps.add(new DomainObjectNotFoundException(ContentMeta.getLabelToken()));
                } else if (col.size() != 1) {
                    if (appExps == null) appExps = new ApplicationExceptions();
                    appExps.add(new MultipleDomainObjectsFoundException(ContentMeta.getLabelToken()));
                }
            }
        }
        if (appExps != null) throw appExps;
    }
}
