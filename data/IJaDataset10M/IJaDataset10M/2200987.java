package org.opentides.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import org.hightides.annotations.util.AnnotationUtil;
import org.opentides.bean.user.SessionUser;
import org.opentides.persistence.listener.EntityDateListener;
import org.opentides.util.CrudUtil;
import org.opentides.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * This is the base class for all entity objects (model) of open-tides.
 * 
 * @author allantan
 */
@MappedSuperclass
@EntityListeners({ EntityDateListener.class })
public abstract class BaseEntity implements Serializable {

    /**
     * Auto-generated class UID.
     */
    private static final long serialVersionUID = -2166505182954839082L;

    /**
     * Class logger.
     */
    private static Logger _log = Logger.getLogger(BaseEntity.class);

    /**
     * Primary key. Annotation is transfered to getter method to allow
     * overridding from subclass.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    /**
     * Create date.
     */
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * Last update date.
     */
    @Column(name = "UPDATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    /**
     * Id of user who created, updated or deleted the entity.
     * Used by AuditLog. 
     */
    @Transient
    private transient Long auditUserId;

    /**
     * Username who created, updated or deleted the entity.
     * Used by AuditLog. 
     */
    @Transient
    private transient String auditUsername;

    /**
     * Office of the user who created, updated or deleted the entity.
     * Used by AuditLog. 
     */
    @Transient
    private transient String auditOfficeName;

    /**
     * Indicator whether to skip audit log or not.
     */
    @Transient
    private transient boolean skipAudit;

    /**
	 * Storage for keeping audit log message.
	 */
    @Transient
    private transient String auditMessage;

    /**
	 * Storage for keeping short audit log message.
	 */
    @Transient
    private transient String friendlyMessage;

    /**
     * Temporary variable for order direction (e.g. ASC or DESC).
     */
    @Transient
    private transient String orderFlow;

    /**
     * Temporary variable for order field
     */
    @Transient
    private transient String orderOption;

    /**
     * Setter method of Id.
     * 
     * @param id primary key
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
	 * Getter method of Id
     * @return
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Checks if object is new (not persisted).
     * 
     * @return true if new, else false.
     */
    public final boolean isNew() {
        return this.getId() == null;
    }

    /**
     * Checks if object is new (not persisted).
     * @See isNew()
     * 
     * @return true if new, else false.
     */
    public final boolean getIsNew() {
        return this.isNew();
    }

    /**
     * Getter method for create date.
     * 
     * @return create date
     */
    public final Date getCreateDate() {
        return this.createDate;
    }

    /**
     * Setter method for create date.
     * 
     * @param createDate create date
     */
    public final void setCreateDate(final Date createDate) {
        if (this.createDate == null) {
            this.createDate = createDate;
        }
    }

    /**
     * Getter method for last update date.
     * 
     * @return last update date.
     */
    public final Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * Setter method for the last update date.
     * 
     * @param updateDate last update date
     */
    public final void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Getter method of user id.
     * 
     * @return the auditUserId
     */
    public final Long getAuditUserId() {
        return this.auditUserId;
    }

    /**
     * Setter method of user id.
     * 
     * @param auditUserId the auditUserId to set
     */
    public final void setAuditUserId(final Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    /**
     * Getter method of username.
     * 
     * @return the auditUsername
     */
    public final String getAuditUsername() {
        return this.auditUsername;
    }

    /**
     * Setter method of username.
     * 
     * @param auditUsername the auditUsername to set
     */
    public final void setAuditUsername(final String auditUsername) {
        this.auditUsername = auditUsername;
    }

    /**
     * Getter method of office name.
     * It is recommended that office is referenced in SystemCodes 
     * under category 'OFFICE'.
     * 
     * @return the auditOfficeName
     */
    public final String getAuditOfficeName() {
        return this.auditOfficeName;
    }

    /**
     * Setter method of office name.
     * It is recommended that office is referenced in SystemCodes 
     * under category 'OFFICE'.
     *
     * @param auditOfficeName the auditOfficeName to set
     */
    public final void setAuditOfficeName(final String auditOfficeName) {
        this.auditOfficeName = auditOfficeName;
    }

    /**
     * Sets the userId based on Acegi Context
     */
    public final void setUserId() {
        if (this.auditUserId == null && SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            final SessionUser user = SecurityUtil.getSessionUser();
            this.auditUserId = user.getRealId();
            this.auditOfficeName = user.getOffice();
            this.auditUsername = user.getUsername();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity other = (BaseEntity) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Default implementation, can be overridden if necessary. Returns the list
     * of field names that are included in search criteria. This method uses
     * reflection and annotation to generate the list of variables declared with
     * isSearchable attribute in high-tides field annotation.
     * 
     */
    public List<String> getSearchProperties() {
        List<String> searchProperties = new ArrayList<String>();
        final List<Field> fields = CrudUtil.getAllFields(this.getClass());
        for (Field field : fields) {
            if (AnnotationUtil.isAnnotatedWith("searchable", field)) {
                searchProperties.add(field.getName());
            }
        }
        return searchProperties;
    }

    /**
     * Default implementation, can be overridden if necessary. Returns the list
     * of field names that are auditable. This method uses
     * reflection and annotation to generate the list of variables declared with
     * isAuditable attribute in high-tides field annotation.
     * 
     */
    public List<AuditableField> getAuditableFields() {
        List<AuditableField> auditableFields = new ArrayList<AuditableField>();
        final List<Field> fields = CrudUtil.getAllFields(this.getClass());
        for (Field field : fields) {
            if ((!Modifier.isTransient(field.getModifiers())) && (AnnotationUtil.isAnnotatedWith("auditable", field))) {
                auditableFields.add(new AuditableField(field.getName()));
            }
        }
        return auditableFields;
    }

    /**
	 * Getter method for skipAudit.
	 *
	 * @return the skipAudit
	 */
    public Boolean isSkipAudit() {
        return skipAudit;
    }

    /**
	 * Setter method for skipAudit.
	 *
	 * @param skipAudit the skipAudit to set
	 */
    public void setSkipAudit(Boolean skipAudit) {
        this.skipAudit = skipAudit;
    }

    /**
	 * Getter method for auditMessage.
	 *
	 * @return the auditMessage
	 */
    public String getAuditMessage() {
        return auditMessage;
    }

    /**
	 * Setter method for auditMessage.
	 *
	 * @param auditMessage the auditMessage to set
	 */
    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    /**
     * Default getReference. Override for specific reference.
     * @return
     */
    public String getReference() {
        return null;
    }

    /**
	 * Getter method for friendlyMessage.
	 *
	 * @return the friendlyMessage
	 */
    public String getFriendlyMessage() {
        return friendlyMessage;
    }

    /**
	 * Setter method for friendlyMessage.
	 *
	 * @param friendlyMessage the friendlyMessage to set
	 */
    public void setFriendlyMessage(String friendlyMessage) {
        this.friendlyMessage = friendlyMessage;
    }

    /**
     * Getter method of order flow.
     * 
     * @return the orderFlow
     */
    public final String getOrderFlow() {
        return this.orderFlow;
    }

    /**
     * Setter method of order flow.
     * 
     * @param orderFlow
     *            the orderFlow to set
     */
    public final void setOrderFlow(final String orderFlow) {
        if ("ASC".equalsIgnoreCase(orderFlow) || "DESC".equalsIgnoreCase(orderFlow)) {
            this.orderFlow = orderFlow;
        } else {
            _log.warn("Attempt to set orderOption with value [" + orderFlow + "] for class [" + this.getClass().getSimpleName() + "].");
        }
    }

    /**
     * Getter method for order option.
     * 
     * @return the orderOption
     */
    public final String getOrderOption() {
        return this.orderOption;
    }

    /**
     * Setter method for order option.
     * 
     * @param orderOption
     *            the orderOption to set
     */
    public final void setOrderOption(final String orderOption) {
        this.orderOption = orderOption;
    }
}
