package org.jaffa.transaction.domain;

import org.apache.log4j.Logger;
import java.util.*;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;
import org.jaffa.datatypes.*;
import org.jaffa.datatypes.adapters.*;
import org.jaffa.metadata.*;
import org.jaffa.rules.RulesEngine;
import org.jaffa.persistence.*;
import org.jaffa.persistence.exceptions.*;
import org.jaffa.security.SecurityManager;
import org.jaffa.util.*;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.RelatedDomainObjectFoundException;
import org.jaffa.exceptions.DuplicateKeyException;
import org.jaffa.datatypes.exceptions.InvalidForeignKeyException;
import org.jaffa.exceptions.ApplicationExceptions;

/**
 * Auto Generated Persistent class for the J_TRANS_SWEEPER$VIEW table.
 * @author  Auto-Generated
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "J_TRANS_SWEEPER$VIEW")
@NamedQueries({ @NamedQuery(name = "TransactionSweeperView.findAll", query = "SELECT t FROM TransactionSweeperView t"), @NamedQuery(name = "TransactionSweeperView.findById", query = "SELECT t FROM TransactionSweeperView t WHERE t.id = :id"), @NamedQuery(name = "TransactionSweeperView.findByCreatedOn", query = "SELECT t FROM TransactionSweeperView t WHERE t.createdOn = :createdOn"), @NamedQuery(name = "TransactionSweeperView.findByCreatedBy", query = "SELECT t FROM TransactionSweeperView t WHERE t.createdBy = :createdBy") })
public class TransactionSweeperView extends Persistent {

    private static final Logger log = Logger.getLogger(TransactionSweeperView.class);

    private static final long serialVersionUID = 1L;

    /** Holds value of property id. */
    @XmlElement(name = "id")
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private java.lang.String id;

    /** Holds value of property createdOn. */
    @XmlElement(name = "createdOn")
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    @Column(name = "CREATED_ON")
    private Timestamp createdOn;

    /** Holds value of property createdBy. */
    @XmlElement(name = "createdBy")
    @Column(name = "CREATED_BY")
    private java.lang.String createdBy;

    /** Check if the domain object exists for the input Primary Key.
     * @return true if the domain object exists for the input Primary Key.
     * @throws FrameworkException Indicates some system error
     */
    public static boolean exists(UOW uow, java.lang.String id) throws FrameworkException {
        boolean localUow = false;
        try {
            if (uow == null || !uow.isActive()) {
                uow = new UOW();
                localUow = true;
            }
            boolean exists = false;
            Criteria criteria = findByPKCriteria(id);
            criteria.addFunction(Criteria.FUNCTION_COUNT, null, Criteria.ID_FUNCTION_COUNT);
            Iterator itr = uow.query(criteria).iterator();
            if (itr.hasNext()) {
                Number count = (Number) ((Map) itr.next()).get(Criteria.ID_FUNCTION_COUNT);
                exists = count != null && count.intValue() > 0;
            }
            return exists;
        } finally {
            if (localUow && uow != null) uow.rollback();
        }
    }

    /** Returns the domain object for the input Primary Key.
     * @return the domain object for the input Primary Key. A null is returned if the domain object is not found.
     * @throws FrameworkException Indicates some system error
     */
    public static TransactionSweeperView findByPK(UOW uow, java.lang.String id) throws FrameworkException {
        boolean localUow = false;
        try {
            if (uow == null || !uow.isActive()) {
                uow = new UOW();
                localUow = true;
            }
            Criteria criteria = findByPKCriteria(id);
            Iterator itr = uow.query(criteria).iterator();
            if (itr.hasNext()) return (TransactionSweeperView) itr.next(); else return null;
        } finally {
            if (localUow && uow != null) uow.rollback();
        }
    }

    /** Returns a Criteria object for retrieving the domain object based on the input Primary Key.
     * @return a Criteria object for retrieving the domain object based on the input Primary Key.
     */
    public static Criteria findByPKCriteria(java.lang.String id) {
        Criteria criteria = new Criteria();
        criteria.setTable(TransactionSweeperViewMeta.getName());
        criteria.addCriteria(TransactionSweeperViewMeta.ID, id);
        return criteria;
    }

    /** Getter for property id.
     * @return Value of property id.
     */
    public java.lang.String getId() {
        return this.id;
    }

    /** Use this method to update the property id.
     * This method will do nothing and simply return if the input value is the same as the current value.
     * Validation will be performed on the input value.
     * This will try to lock the underlying database row, in case CAUTIOUS locking is specified at the time of query.
     * @param id New value of property id.
     * @throws ValidationException if an invalid value is passed.
     * @throws UpdatePrimaryKeyException if this domain object was loaded from the database.
     * @throws ReadOnlyObjectException if a Read-Only object is updated.
     * @throws AlreadyLockedObjectException if the underlying database row is already locked by another process.
     * @throws FrameworkException Indicates some system error
     */
    public void setId(java.lang.String id) throws ValidationException, UpdatePrimaryKeyException, ReadOnlyObjectException, AlreadyLockedObjectException, FrameworkException {
        if (this.id == null ? id == null : this.id.equals(id)) return;
        if (isDatabaseOccurence()) throw new UpdatePrimaryKeyException();
        id = validateId(id);
        super.update();
        super.addInitialValue(TransactionSweeperViewMeta.ID, this.id);
        this.id = id;
    }

    /** Use this method to validate a value for the property id.
     * @param id Value to be validated for the property id.
     * @throws ValidationException if an invalid value is passed
     * @throws FrameworkException Indicates some system error
     */
    public java.lang.String validateId(java.lang.String id) throws ValidationException, FrameworkException {
        id = FieldValidator.validate(id, (StringFieldMetaData) TransactionSweeperViewMeta.META_ID, true);
        RulesEngine.doAllValidationsForDomainField(TransactionSweeperViewMeta.getName(), TransactionSweeperViewMeta.ID, id, this.getUOW());
        return id;
    }

    /** Getter for property createdOn.
     * @return Value of property createdOn.
     */
    public org.jaffa.datatypes.DateTime getCreatedOn() {
        return this.createdOn != null ? new DateTime(this.createdOn) : null;
    }

    /** Use this method to update the property createdOn.
     * This method will do nothing and simply return if the input value is the same as the current value.
     * Validation will be performed on the input value.
     * This will try to lock the underlying database row, in case CAUTIOUS locking is specified at the time of query.
     * @param createdOn New value of property createdOn.
     * @throws ValidationException if an invalid value is passed.
     * @throws ReadOnlyObjectException if a Read-Only object is updated.
     * @throws AlreadyLockedObjectException if the underlying database row is already locked by another process.
     * @throws FrameworkException Indicates some system error
     */
    public void setCreatedOn(org.jaffa.datatypes.DateTime createdOn) throws ValidationException, ReadOnlyObjectException, AlreadyLockedObjectException, FrameworkException {
        if (this.createdOn == null ? createdOn == null : new DateTime(this.createdOn).equals(createdOn)) return;
        createdOn = validateCreatedOn(createdOn);
        super.update();
        super.addInitialValue(TransactionSweeperViewMeta.CREATED_ON, getCreatedOn());
        this.createdOn = createdOn != null ? createdOn.timestamp() : null;
    }

    /** Use this method to validate a value for the property createdOn.
     * @param createdOn Value to be validated for the property createdOn.
     * @throws ValidationException if an invalid value is passed
     * @throws FrameworkException Indicates some system error
     */
    public org.jaffa.datatypes.DateTime validateCreatedOn(org.jaffa.datatypes.DateTime createdOn) throws ValidationException, FrameworkException {
        createdOn = FieldValidator.validate(createdOn, (DateTimeFieldMetaData) TransactionSweeperViewMeta.META_CREATED_ON, true);
        RulesEngine.doAllValidationsForDomainField(TransactionSweeperViewMeta.getName(), TransactionSweeperViewMeta.CREATED_ON, createdOn, this.getUOW());
        return createdOn;
    }

    /** Getter for property createdBy.
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return this.createdBy;
    }

    /** Use this method to update the property createdBy.
     * This method will do nothing and simply return if the input value is the same as the current value.
     * Validation will be performed on the input value.
     * This will try to lock the underlying database row, in case CAUTIOUS locking is specified at the time of query.
     * @param createdBy New value of property createdBy.
     * @throws ValidationException if an invalid value is passed.
     * @throws ReadOnlyObjectException if a Read-Only object is updated.
     * @throws AlreadyLockedObjectException if the underlying database row is already locked by another process.
     * @throws FrameworkException Indicates some system error
     */
    public void setCreatedBy(java.lang.String createdBy) throws ValidationException, ReadOnlyObjectException, AlreadyLockedObjectException, FrameworkException {
        if (this.createdBy == null ? createdBy == null : this.createdBy.equals(createdBy)) return;
        createdBy = validateCreatedBy(createdBy);
        super.update();
        super.addInitialValue(TransactionSweeperViewMeta.CREATED_BY, this.createdBy);
        this.createdBy = createdBy;
    }

    /** Use this method to validate a value for the property createdBy.
     * @param createdBy Value to be validated for the property createdBy.
     * @throws ValidationException if an invalid value is passed
     * @throws FrameworkException Indicates some system error
     */
    public java.lang.String validateCreatedBy(java.lang.String createdBy) throws ValidationException, FrameworkException {
        createdBy = FieldValidator.validate(createdBy, (StringFieldMetaData) TransactionSweeperViewMeta.META_CREATED_BY, true);
        RulesEngine.doAllValidationsForDomainField(TransactionSweeperViewMeta.getName(), TransactionSweeperViewMeta.CREATED_BY, createdBy, this.getUOW());
        return createdBy;
    }

    /** This returns the diagnostic information.
    * @return the diagnostic information.
    */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<TransactionSweeperView>");
        buf.append("<id>");
        if (this.id != null) buf.append(this.id);
        buf.append("</id>");
        buf.append("<createdOn>");
        if (this.createdOn != null) buf.append(this.createdOn);
        buf.append("</createdOn>");
        buf.append("<createdBy>");
        if (this.createdBy != null) buf.append(this.createdBy);
        buf.append("</createdBy>");
        buf.append(super.toString());
        buf.append("</TransactionSweeperView>");
        return buf.toString();
    }

    /** Returns a clone of the object.
     * @throws CloneNotSupportedException if cloning is not supported. This should never happen.
     * @return a clone of the object.
     */
    public Object clone() throws CloneNotSupportedException {
        TransactionSweeperView obj = (TransactionSweeperView) super.clone();
        return obj;
    }

    /** This method ensures that the modified foreign-keys are valid.
     * @throws ApplicationExceptions if an invalid foreign key is set.
     * @throws FrameworkException Indicates some system error
     */
    public void performForeignKeyValidations() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = new ApplicationExceptions();
        if (appExps.size() > 0) throw appExps;
    }

    /** This method is triggered by the UOW, before adding this object to the Delete-Store.
     * This will raise an exception if any associated/aggregated objects exist.
     * This will cascade delete all composite objects.
     * @throws PreDeleteFailedException if any error occurs during the process.
     */
    public void performPreDeleteReferentialIntegrity() throws PreDeleteFailedException {
    }

    /** This method is triggered by the UOW, before adding this object to the Add-Store, but after a UOW has been associated to the object.
     * It ensures that the primary-key is unique and that the foreign-keys are valid.
     * @throws PreAddFailedException if any error occurs during the process.
     */
    public void preAdd() throws PreAddFailedException {
        try {
            if (getCreatedBy() == null && SecurityManager.getPrincipal() != null && SecurityManager.getPrincipal().getName() != null) setCreatedBy(SecurityManager.getPrincipal().getName());
        } catch (ValidationException e) {
            throw new PreAddFailedException(new String[] { "StampType:CreatedUserId Error" }, e);
        } catch (FrameworkException e) {
            throw new PreAddFailedException(new String[] { "StampType:CreatedUserId Error" }, e);
        }
        try {
            if (getCreatedOn() == null) setCreatedOn(new DateTime());
        } catch (ValidationException e) {
            throw new PreAddFailedException(new String[] { "StampType:CreatedDateTime Error" }, e);
        } catch (FrameworkException e) {
            throw new PreAddFailedException(new String[] { "StampType:CreatedDateTime Error" }, e);
        }
        super.preAdd();
    }
}
