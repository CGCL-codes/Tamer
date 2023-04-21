package com.eptica.ias.models.documents.documentattachmentgroup;

import java.io.*;
import java.lang.Comparable;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import com.eptica.ias.util.*;
import com.eptica.ias.models.documents.documentattachment.*;
import com.eptica.ias.models.documents.documentattachmentgrouplbl.*;

/**
 * Map the columns of the document_attachment_group table.
 * Supports many-to-one methods expected by Hibernate.
 * Supports one-to-many methods expected by Hibernate.
 *
 * The corresponding hibernate mapping file is located
 * under src/main/resources, under the same package hierarchy.
 */
public class DocumentAttachmentGroupModel implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(DocumentAttachmentGroupModel.class);

    private String documentAttachmentGroupId = null;

    private Integer version = null;

    private String esGroupId = null;

    private String esParentGroupId = null;

    private String parentGroupId = null;

    private java.util.Date creationDate = null;

    private java.util.Date modifiedDate = null;

    private Boolean isDraft = null;

    private String esAccountGroupId = null;

    private DocumentAttachmentGroupModel parentGroupIdDocumentAttachmentGroup = null;

    private Collection<DocumentAttachmentModel> documentAttachments = new HashSet<DocumentAttachmentModel>();

    private Collection<DocumentAttachmentGroupModel> parentGroupIdDocumentAttachmentGroups = new HashSet<DocumentAttachmentGroupModel>();

    private Collection<DocumentAttachmentGroupLblModel> documentAttachmentGroupLbls = new HashSet<DocumentAttachmentGroupLblModel>();

    /**
     * Helper method to know whether the primary key is set or not,
     * that is if getDocumentAttachmentGroupId() is not null and has a length greater than 0.
     * @return true if the primary key is set, false otherwise
     */
    public boolean hasPrimaryKey() {
        return getDocumentAttachmentGroupId() != null && getDocumentAttachmentGroupId().length() > 0;
    }

    /**
     * Setter for the field documentAttachmentGroupId that maps the column document_attachment_group_id.
     * Null value is not accepted by the underlying database.
     * TODO: set a description for document_attachment_group.document_attachment_group_id
     * @param documentAttachmentGroupId the documentAttachmentGroupId
     */
    public void setDocumentAttachmentGroupId(String documentAttachmentGroupId) {
        this.documentAttachmentGroupId = documentAttachmentGroupId;
    }

    /**
     * Getter for the field documentAttachmentGroupId that maps the column document_attachment_group_id.
     * @return the documentAttachmentGroupId.
     */
    public String getDocumentAttachmentGroupId() {
        return documentAttachmentGroupId;
    }

    /**
     * Helper method to know if the documentAttachmentGroupId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasDocumentAttachmentGroupId() {
        return getDocumentAttachmentGroupId() != null && getDocumentAttachmentGroupId().length() > 0;
    }

    /**
     * @see #hasDocumentAttachmentGroupId()
     */
    public boolean getHasDocumentAttachmentGroupId() {
        return hasDocumentAttachmentGroupId();
    }

    /**
     * Setter for the field version that maps the column version.
     * It is used for optimistic locking by hibernate.
     * Null value is accepted by the underlying database.
     * TODO: set a description for document_attachment_group.version
     * @param version the version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Getter for the field version that maps the column version.
     * It is used for optimistic locking by hibernate.
     * @return the version.
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Setter for the field esGroupId that maps the column es_group_id.
     * Null value is not accepted by the underlying database.
     * TODO: set a description for document_attachment_group.es_group_id
     * @param esGroupId the esGroupId
     */
    public void setEsGroupId(String esGroupId) {
        this.esGroupId = esGroupId;
    }

    /**
     * Getter for the field esGroupId that maps the column es_group_id.
     * @return the esGroupId.
     */
    public String getEsGroupId() {
        return esGroupId;
    }

    /**
     * Helper method to know if the esGroupId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasEsGroupId() {
        return getEsGroupId() != null && getEsGroupId().length() > 0;
    }

    /**
     * @see #hasEsGroupId()
     */
    public boolean getHasEsGroupId() {
        return hasEsGroupId();
    }

    /**
     * Setter for the field esParentGroupId that maps the column es_parent_group_id.
     * Null value is accepted by the underlying database.
     * TODO: set a description for document_attachment_group.es_parent_group_id
     * @param esParentGroupId the esParentGroupId
     */
    public void setEsParentGroupId(String esParentGroupId) {
        this.esParentGroupId = esParentGroupId;
    }

    /**
     * Getter for the field esParentGroupId that maps the column es_parent_group_id.
     * @return the esParentGroupId.
     */
    public String getEsParentGroupId() {
        return esParentGroupId;
    }

    /**
     * Helper method to know if the esParentGroupId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasEsParentGroupId() {
        return getEsParentGroupId() != null && getEsParentGroupId().length() > 0;
    }

    /**
     * @see #hasEsParentGroupId()
     */
    public boolean getHasEsParentGroupId() {
        return hasEsParentGroupId();
    }

    /**
     * Setter for the field parentGroupId that maps the column parent_group_id.
     * Null value is accepted by the underlying database.
     * This method will not persit the field, it is provided as a way to pass this value to a search facility (such as a DAO).
     * To persist this field you must use instead the setParentGroupIdDocumentAttachmentGroup setter.
     * Note that the setParentGroupIdDocumentAttachmentGroup setter set also this field.
     * TODO: set a description for document_attachment_group.parent_group_id
     * @param parentGroupId the parentGroupId
     */
    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    /**
     * Getter for the field parentGroupId that maps the column parent_group_id.
     * @return the parentGroupId.
     */
    public String getParentGroupId() {
        return parentGroupId;
    }

    /**
     * Helper method to know if the parentGroupId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasParentGroupId() {
        return getParentGroupId() != null && getParentGroupId().length() > 0;
    }

    /**
     * @see #hasParentGroupId()
     */
    public boolean getHasParentGroupId() {
        return hasParentGroupId();
    }

    /**
     * Setter for the field creationDate that maps the column creation_date.
     * Null value is accepted by the underlying database.
     * TODO: set a description for document_attachment_group.creation_date
     * @param creationDate the creationDate
     */
    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Getter for the field creationDate that maps the column creation_date.
     * @return the creationDate.
     */
    public java.util.Date getCreationDate() {
        return creationDate;
    }

    /**
     * Helper method to know if the creationDate is not null .<br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    /**
     * @see #hasCreationDate()
     */
    public boolean getHasCreationDate() {
        return hasCreationDate();
    }

    /**
     * Returns this creationDate date as a localized string.<br>
     * @return the creationDate localized value
     */
    public String getLocalizedCreationDate() {
        if (hasCreationDate()) {
            return DateUtil.getLocalizedDate(getCreationDate());
        } else {
            return "";
        }
    }

    /**
     * Setter for the field modifiedDate that maps the column modified_date.
     * Null value is accepted by the underlying database.
     * TODO: set a description for document_attachment_group.modified_date
     * @param modifiedDate the modifiedDate
     */
    public void setModifiedDate(java.util.Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * Getter for the field modifiedDate that maps the column modified_date.
     * @return the modifiedDate.
     */
    public java.util.Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Helper method to know if the modifiedDate is not null .<br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasModifiedDate() {
        return getModifiedDate() != null;
    }

    /**
     * @see #hasModifiedDate()
     */
    public boolean getHasModifiedDate() {
        return hasModifiedDate();
    }

    /**
     * Returns this modifiedDate date as a localized string.<br>
     * @return the modifiedDate localized value
     */
    public String getLocalizedModifiedDate() {
        if (hasModifiedDate()) {
            return DateUtil.getLocalizedDate(getModifiedDate());
        } else {
            return "";
        }
    }

    /**
     * Setter for the field isDraft that maps the column is_draft.
     * Null value is accepted by the underlying database.
     * TODO: set a description for document_attachment_group.is_draft
     * @param isDraft the isDraft
     */
    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    /**
     * Getter for the field isDraft that maps the column is_draft.
     * @return the isDraft.
     */
    public Boolean getIsDraft() {
        return isDraft;
    }

    /**
     * Setter for the field esAccountGroupId that maps the column es_account_group_id.
     * Null value is not accepted by the underlying database.
     * TODO: set a description for document_attachment_group.es_account_group_id
     * @param esAccountGroupId the esAccountGroupId
     */
    public void setEsAccountGroupId(String esAccountGroupId) {
        this.esAccountGroupId = esAccountGroupId;
    }

    /**
     * Getter for the field esAccountGroupId that maps the column es_account_group_id.
     * @return the esAccountGroupId.
     */
    public String getEsAccountGroupId() {
        return esAccountGroupId;
    }

    /**
     * Helper method to know if the esAccountGroupId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasEsAccountGroupId() {
        return getEsAccountGroupId() != null && getEsAccountGroupId().length() > 0;
    }

    /**
     * @see #hasEsAccountGroupId()
     */
    public boolean getHasEsAccountGroupId() {
        return hasEsAccountGroupId();
    }

    /**
     * Set the parentGroupIdDocumentAttachmentGroup without adding this documentAttachmentGroup on the passed parentGroupIdDocumentAttachmentGroup.<br>
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by DocumentAttachmentGroupModel
     */
    public void setParentGroupIdDocumentAttachmentGroup(DocumentAttachmentGroupModel parentGroupIdDocumentAttachmentGroup) {
        this.parentGroupIdDocumentAttachmentGroup = parentGroupIdDocumentAttachmentGroup;
        if (parentGroupIdDocumentAttachmentGroup != null) {
            setParentGroupId(parentGroupIdDocumentAttachmentGroup.getDocumentAttachmentGroupId());
        } else {
            setParentGroupId(null);
        }
    }

    /**
     * Return the parentGroupIdDocumentAttachmentGroup
     */
    public DocumentAttachmentGroupModel getParentGroupIdDocumentAttachmentGroup() {
        return parentGroupIdDocumentAttachmentGroup;
    }

    /**
     * Helper method to know if the parentGroupIdDocumentAttachmentGroup has been set
     */
    public boolean hasParentGroupIdDocumentAttachmentGroup() {
        return getParentGroupIdDocumentAttachmentGroup() != null;
    }

    /**
     * @see #hasParentGroupIdDocumentAttachmentGroup()
     */
    public boolean getHasParentGroupIdDocumentAttachmentGroup() {
        return hasParentGroupIdDocumentAttachmentGroup();
    }

    /**
     * Set the DocumentAttachmentModel collection.<br>
     * It is recommended to use the helper method addDocumentAttachment / removeDocumentAttachment<br>
     * if you want to preserve referential integrity at the object level.
     *
     * @param documentAttachments collection
     */
    public void setDocumentAttachments(Collection<DocumentAttachmentModel> documentAttachments) {
        this.documentAttachments = documentAttachments;
    }

    /**
     * Get the DocumentAttachmentModel collection
     * @return the documentAttachments collection
     */
    public Collection<DocumentAttachmentModel> getDocumentAttachments() {
        return documentAttachments;
    }

    /**
     * Helper method to know if the documentAttachmentGroup has been set
     */
    public boolean hasDocumentAttachments() {
        return getDocumentAttachments() != null && getDocumentAttachments().size() > 0;
    }

    /**
     * @see #hasDocumentAttachments()
     */
    public boolean getHasDocumentAttachments() {
        return hasDocumentAttachments();
    }

    /**
     * Helper method to add the pass documentAttachment to the documentAttachments Collection<br>
     * and set this documentAttachmentGroup on the passed documentAttachment to preserve referential<br>
     * integrity at the object level.
     *
     * @param documentAttachment the to add
     * @return true if the documentAttachment could be added to the documentAttachments collection, false otherwise
     */
    public boolean addDocumentAttachment(DocumentAttachmentModel documentAttachment) {
        boolean addedOK = getDocumentAttachments().add(documentAttachment);
        if (addedOK) {
            documentAttachment.setDocumentAttachmentGroup((DocumentAttachmentGroupModel) this);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("add returned false");
            }
        }
        return addedOK;
    }

    /**
     * Helper method to add the collection of documentAttachment to the documentAttachments Collection<br>
     * and set this documentAttachmentGroup on the passed documentAttachment to preserve referential<br>
     * integrity at the object level.
     *
     * @param documentAttachments the colletion to add
     * @return true if the collection could be added to the documentAttachments collection, false otherwise
     */
    public boolean addDocumentAttachments(Collection<DocumentAttachmentModel> documentAttachments) {
        boolean addedOK = getDocumentAttachments().addAll(documentAttachments);
        if (addedOK) {
            for (DocumentAttachmentModel documentAttachment : documentAttachments) {
                documentAttachment.setDocumentAttachmentGroup((DocumentAttachmentGroupModel) this);
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("add returned false");
            }
        }
        return addedOK;
    }

    /**
     * Helper method to determine if the passed documentAttachment is already present in the documentAttachments collection.
     *
     * @param documentAttachment the to check.
     * @return true if the documentAttachments collection contains the passed documentAttachment, false otherwise.
     */
    public boolean containsDocumentAttachment(DocumentAttachmentModel documentAttachment) {
        if (getDocumentAttachments() == null) {
            return false;
        }
        return getDocumentAttachments().contains(documentAttachment);
    }

    /**
     * Helper method to remove the passed documentAttachment from the documentAttachments collection and unset<br>
     * this documentAttachmentGroup from the passed documentAttachment to preserve referential integrity at the object level.
     *
     * @param documentAttachment the to remove
     * @return true if the documentAttachment could be removed from the documentAttachments collection, false otherwise
     */
    public boolean removeDocumentAttachment(DocumentAttachmentModel documentAttachment) {
        boolean removedOK = getDocumentAttachments().remove(documentAttachment);
        if (removedOK) {
            documentAttachment.setDocumentAttachmentGroup(null);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("remove returned false");
            }
        }
        return removedOK;
    }

    /**
     * Helper method to remove the passed documentAttachment from the documentAttachments collection and unset<br>
     * this documentAttachmentGroup from the passed documentAttachment to preserve referential integrity at the object level.
     *
     * @param documentAttachments the collection to remove
     * @return true if the collection could be removed from the documentAttachments collection, false otherwise
     */
    public boolean removeDocumentAttachments(Collection<DocumentAttachmentModel> documentAttachments) {
        boolean removedOK = getDocumentAttachments().removeAll(documentAttachments);
        if (removedOK) {
            for (DocumentAttachmentModel documentAttachment : documentAttachments) {
                documentAttachment.setDocumentAttachmentGroup(null);
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("remove returned false");
            }
        }
        return removedOK;
    }

    /**
     * Clear the DocumentAttachmentModel Collection.
     * Preserve the referencial integrity.
     */
    public void removeDocumentAttachments() {
        for (DocumentAttachmentModel documentAttachment : getDocumentAttachments()) {
            documentAttachment.setDocumentAttachmentGroup(null);
        }
        getDocumentAttachments().clear();
    }

    /**
     * Set the DocumentAttachmentGroupModel collection.<br>
     * It is recommended to use the helper method addParentGroupIdDocumentAttachmentGroup / removeParentGroupIdDocumentAttachmentGroup<br>
     * if you want to preserve referential integrity at the object level.
     *
     * @param documentAttachmentGroups collection
     */
    public void setParentGroupIdDocumentAttachmentGroups(Collection<DocumentAttachmentGroupModel> documentAttachmentGroups) {
        this.parentGroupIdDocumentAttachmentGroups = documentAttachmentGroups;
    }

    /**
     * Get the DocumentAttachmentGroupModel collection
     * @return the documentAttachmentGroups collection
     */
    public Collection<DocumentAttachmentGroupModel> getParentGroupIdDocumentAttachmentGroups() {
        return parentGroupIdDocumentAttachmentGroups;
    }

    /**
     * Helper method to know if the parentGroupIdDocumentAttachmentGroup has been set
     */
    public boolean hasParentGroupIdDocumentAttachmentGroups() {
        return getParentGroupIdDocumentAttachmentGroups() != null && getParentGroupIdDocumentAttachmentGroups().size() > 0;
    }

    /**
     * @see #hasParentGroupIdDocumentAttachmentGroups()
     */
    public boolean getHasParentGroupIdDocumentAttachmentGroups() {
        return hasParentGroupIdDocumentAttachmentGroups();
    }

    /**
     * Helper method to add the pass documentAttachmentGroup to the documentAttachmentGroups Collection<br>
     * and set this documentAttachmentGroup on the passed documentAttachmentGroup to preserve referential<br>
     * integrity at the object level.
     *
     * @param documentAttachmentGroup the to add
     * @return true if the documentAttachmentGroup could be added to the documentAttachmentGroups collection, false otherwise
     */
    public boolean addParentGroupIdDocumentAttachmentGroup(DocumentAttachmentGroupModel documentAttachmentGroup) {
        boolean addedOK = getParentGroupIdDocumentAttachmentGroups().add(documentAttachmentGroup);
        if (addedOK) {
            documentAttachmentGroup.setParentGroupIdDocumentAttachmentGroup((DocumentAttachmentGroupModel) this);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("add returned false");
            }
        }
        return addedOK;
    }

    /**
     * Helper method to add the collection of documentAttachmentGroup to the documentAttachmentGroups Collection<br>
     * and set this documentAttachmentGroup on the passed documentAttachmentGroup to preserve referential<br>
     * integrity at the object level.
     *
     * @param documentAttachmentGroups the colletion to add
     * @return true if the collection could be added to the documentAttachmentGroups collection, false otherwise
     */
    public boolean addParentGroupIdDocumentAttachmentGroups(Collection<DocumentAttachmentGroupModel> documentAttachmentGroups) {
        boolean addedOK = getParentGroupIdDocumentAttachmentGroups().addAll(documentAttachmentGroups);
        if (addedOK) {
            for (DocumentAttachmentGroupModel documentAttachmentGroup : documentAttachmentGroups) {
                documentAttachmentGroup.setParentGroupIdDocumentAttachmentGroup((DocumentAttachmentGroupModel) this);
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("add returned false");
            }
        }
        return addedOK;
    }

    /**
     * Helper method to determine if the passed documentAttachmentGroup is already present in the documentAttachmentGroups collection.
     *
     * @param documentAttachmentGroup the to check.
     * @return true if the documentAttachmentGroups collection contains the passed documentAttachmentGroup, false otherwise.
     */
    public boolean containsParentGroupIdDocumentAttachmentGroup(DocumentAttachmentGroupModel documentAttachmentGroup) {
        if (getParentGroupIdDocumentAttachmentGroups() == null) {
            return false;
        }
        return getParentGroupIdDocumentAttachmentGroups().contains(documentAttachmentGroup);
    }

    /**
     * Helper method to remove the passed documentAttachmentGroup from the documentAttachmentGroups collection and unset<br>
     * this documentAttachmentGroup from the passed documentAttachmentGroup to preserve referential integrity at the object level.
     *
     * @param documentAttachmentGroup the to remove
     * @return true if the documentAttachmentGroup could be removed from the documentAttachmentGroups collection, false otherwise
     */
    public boolean removeParentGroupIdDocumentAttachmentGroup(DocumentAttachmentGroupModel documentAttachmentGroup) {
        boolean removedOK = getParentGroupIdDocumentAttachmentGroups().remove(documentAttachmentGroup);
        if (removedOK) {
            documentAttachmentGroup.setParentGroupIdDocumentAttachmentGroup(null);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("remove returned false");
            }
        }
        return removedOK;
    }

    /**
     * Helper method to remove the passed documentAttachmentGroup from the documentAttachmentGroups collection and unset<br>
     * this documentAttachmentGroup from the passed documentAttachmentGroup to preserve referential integrity at the object level.
     *
     * @param documentAttachmentGroups the collection to remove
     * @return true if the collection could be removed from the documentAttachmentGroups collection, false otherwise
     */
    public boolean removeParentGroupIdDocumentAttachmentGroups(Collection<DocumentAttachmentGroupModel> documentAttachmentGroups) {
        boolean removedOK = getParentGroupIdDocumentAttachmentGroups().removeAll(documentAttachmentGroups);
        if (removedOK) {
            for (DocumentAttachmentGroupModel documentAttachmentGroup : documentAttachmentGroups) {
                documentAttachmentGroup.setParentGroupIdDocumentAttachmentGroup(null);
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("remove returned false");
            }
        }
        return removedOK;
    }

    /**
     * Clear the DocumentAttachmentGroupModel Collection.
     * Preserve the referencial integrity.
     */
    public void removeParentGroupIdDocumentAttachmentGroups() {
        for (DocumentAttachmentGroupModel documentAttachmentGroup : getParentGroupIdDocumentAttachmentGroups()) {
            documentAttachmentGroup.setParentGroupIdDocumentAttachmentGroup(null);
        }
        getParentGroupIdDocumentAttachmentGroups().clear();
    }

    /**
     * Set the DocumentAttachmentGroupLblModel collection.<br>
     * It is recommended to use the helper method addDocumentAttachmentGroupLbl / removeDocumentAttachmentGroupLbl<br>
     * if you want to preserve referential integrity at the object level.
     *
     * @param documentAttachmentGroupLbls collection
     */
    public void setDocumentAttachmentGroupLbls(Collection<DocumentAttachmentGroupLblModel> documentAttachmentGroupLbls) {
        this.documentAttachmentGroupLbls = documentAttachmentGroupLbls;
    }

    /**
     * Get the DocumentAttachmentGroupLblModel collection
     * @return the documentAttachmentGroupLbls collection
     */
    public Collection<DocumentAttachmentGroupLblModel> getDocumentAttachmentGroupLbls() {
        return documentAttachmentGroupLbls;
    }

    /**
     * Helper method to know if the documentAttachmentGroup has been set
     */
    public boolean hasDocumentAttachmentGroupLbls() {
        return getDocumentAttachmentGroupLbls() != null && getDocumentAttachmentGroupLbls().size() > 0;
    }

    /**
     * @see #hasDocumentAttachmentGroupLbls()
     */
    public boolean getHasDocumentAttachmentGroupLbls() {
        return hasDocumentAttachmentGroupLbls();
    }

    /**
     * Helper method to add the pass documentAttachmentGroupLbl to the documentAttachmentGroupLbls Collection<br>
     * and set this documentAttachmentGroup on the passed documentAttachmentGroupLbl to preserve referential<br>
     * integrity at the object level.
     *
     * @param documentAttachmentGroupLbl the to add
     * @return true if the documentAttachmentGroupLbl could be added to the documentAttachmentGroupLbls collection, false otherwise
     */
    public boolean addDocumentAttachmentGroupLbl(DocumentAttachmentGroupLblModel documentAttachmentGroupLbl) {
        boolean addedOK = getDocumentAttachmentGroupLbls().add(documentAttachmentGroupLbl);
        if (addedOK) {
            documentAttachmentGroupLbl.setDocumentAttachmentGroup((DocumentAttachmentGroupModel) this);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("add returned false");
            }
        }
        return addedOK;
    }

    /**
     * Helper method to add the collection of documentAttachmentGroupLbl to the documentAttachmentGroupLbls Collection<br>
     * and set this documentAttachmentGroup on the passed documentAttachmentGroupLbl to preserve referential<br>
     * integrity at the object level.
     *
     * @param documentAttachmentGroupLbls the colletion to add
     * @return true if the collection could be added to the documentAttachmentGroupLbls collection, false otherwise
     */
    public boolean addDocumentAttachmentGroupLbls(Collection<DocumentAttachmentGroupLblModel> documentAttachmentGroupLbls) {
        boolean addedOK = getDocumentAttachmentGroupLbls().addAll(documentAttachmentGroupLbls);
        if (addedOK) {
            for (DocumentAttachmentGroupLblModel documentAttachmentGroupLbl : documentAttachmentGroupLbls) {
                documentAttachmentGroupLbl.setDocumentAttachmentGroup((DocumentAttachmentGroupModel) this);
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("add returned false");
            }
        }
        return addedOK;
    }

    /**
     * Helper method to determine if the passed documentAttachmentGroupLbl is already present in the documentAttachmentGroupLbls collection.
     *
     * @param documentAttachmentGroupLbl the to check.
     * @return true if the documentAttachmentGroupLbls collection contains the passed documentAttachmentGroupLbl, false otherwise.
     */
    public boolean containsDocumentAttachmentGroupLbl(DocumentAttachmentGroupLblModel documentAttachmentGroupLbl) {
        if (getDocumentAttachmentGroupLbls() == null) {
            return false;
        }
        return getDocumentAttachmentGroupLbls().contains(documentAttachmentGroupLbl);
    }

    /**
     * Helper method to remove the passed documentAttachmentGroupLbl from the documentAttachmentGroupLbls collection and unset<br>
     * this documentAttachmentGroup from the passed documentAttachmentGroupLbl to preserve referential integrity at the object level.
     *
     * @param documentAttachmentGroupLbl the to remove
     * @return true if the documentAttachmentGroupLbl could be removed from the documentAttachmentGroupLbls collection, false otherwise
     */
    public boolean removeDocumentAttachmentGroupLbl(DocumentAttachmentGroupLblModel documentAttachmentGroupLbl) {
        boolean removedOK = getDocumentAttachmentGroupLbls().remove(documentAttachmentGroupLbl);
        if (removedOK) {
            documentAttachmentGroupLbl.setDocumentAttachmentGroup(null);
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("remove returned false");
            }
        }
        return removedOK;
    }

    /**
     * Helper method to remove the passed documentAttachmentGroupLbl from the documentAttachmentGroupLbls collection and unset<br>
     * this documentAttachmentGroup from the passed documentAttachmentGroupLbl to preserve referential integrity at the object level.
     *
     * @param documentAttachmentGroupLbls the collection to remove
     * @return true if the collection could be removed from the documentAttachmentGroupLbls collection, false otherwise
     */
    public boolean removeDocumentAttachmentGroupLbls(Collection<DocumentAttachmentGroupLblModel> documentAttachmentGroupLbls) {
        boolean removedOK = getDocumentAttachmentGroupLbls().removeAll(documentAttachmentGroupLbls);
        if (removedOK) {
            for (DocumentAttachmentGroupLblModel documentAttachmentGroupLbl : documentAttachmentGroupLbls) {
                documentAttachmentGroupLbl.setDocumentAttachmentGroup(null);
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("remove returned false");
            }
        }
        return removedOK;
    }

    /**
     * Clear the DocumentAttachmentGroupLblModel Collection.
     * Preserve the referencial integrity.
     */
    public void removeDocumentAttachmentGroupLbls() {
        for (DocumentAttachmentGroupLblModel documentAttachmentGroupLbl : getDocumentAttachmentGroupLbls()) {
            documentAttachmentGroupLbl.setDocumentAttachmentGroup(null);
        }
        getDocumentAttachmentGroupLbls().clear();
    }

    private boolean _freezeUseUidInEquals = false;

    private boolean _useUidInEquals = true;

    private java.rmi.dgc.VMID _uidInEquals = null;

    private void setEqualsAndHashcodeStrategy() {
        if (_freezeUseUidInEquals == false) {
            _freezeUseUidInEquals = true;
            _useUidInEquals = useUidInEquals();
            if (_useUidInEquals) {
                _uidInEquals = new java.rmi.dgc.VMID();
            }
        }
    }

    private boolean useUidInEquals() {
        return hasPrimaryKey();
    }

    /**
     * Please read discussion about object identity at <a href="http://www.hibernate.org/109.html">http://www.hibernate.org/109.html</a>
     * @see java.lang.Object#equals(Object)
     * @see #setEqualsAndHashcodeStrategy()
     * @return true if the equals, false otherwise
     */
    public boolean equals(Object documentAttachmentGroup) {
        if (this == documentAttachmentGroup) {
            return true;
        }
        if (documentAttachmentGroup == null) {
            return false;
        }
        if (!(documentAttachmentGroup instanceof DocumentAttachmentGroupModel)) {
            return false;
        }
        DocumentAttachmentGroupModel other = (DocumentAttachmentGroupModel) documentAttachmentGroup;
        setEqualsAndHashcodeStrategy();
        if (this._useUidInEquals != other._useUidInEquals && other._freezeUseUidInEquals == true) {
            if (logger.isErrorEnabled()) {
                logger.error("Limit case reached in equals strategy. Developper, fix me", new Exception("stack trace"));
            }
            throw new IllegalStateException("Limit case reached in equals strategy. Developper, fix me");
        }
        if (_useUidInEquals) {
            boolean eq = _uidInEquals.equals(other._uidInEquals);
            if (eq) {
                if (hashCode() != other.hashCode()) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Limit case reached in equals strategy. Developper, fix me", new Exception("stack trace"));
                    }
                }
            }
            return eq;
        } else {
            boolean eq = getDocumentAttachmentGroupId().equals(other.getDocumentAttachmentGroupId());
            if (eq) {
                if (hashCode() != other.hashCode()) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Limit case reached in equals strategy. Developper, fix me", new Exception("stack trace"));
                    }
                }
            }
            return eq;
        }
    }

    /**
     * When two objects are equals, their hashcode must be equal too
     * @see #equals(Object)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        setEqualsAndHashcodeStrategy();
        if (_useUidInEquals) {
            return _uidInEquals.hashCode();
        } else {
            if (hasPrimaryKey()) {
                return getDocumentAttachmentGroupId().hashCode();
            } else {
                return super.hashCode();
            }
        }
    }

    /**
     * String representation of the current $modelClass
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (hasDocumentAttachmentGroupId()) {
            ret.append("document_attachment_group.document_attachment_group_id=[").append(getDocumentAttachmentGroupId()).append("]\n");
        }
        ret.append("document_attachment_group.version=[").append(getVersion()).append("]\n");
        if (hasEsGroupId()) {
            ret.append("document_attachment_group.es_group_id=[").append(getEsGroupId()).append("]\n");
        }
        if (hasEsParentGroupId()) {
            ret.append("document_attachment_group.es_parent_group_id=[").append(getEsParentGroupId()).append("]\n");
        }
        if (hasParentGroupId()) {
            ret.append("document_attachment_group.parent_group_id=[").append(getParentGroupId()).append("]\n");
        }
        if (hasCreationDate()) {
            ret.append("document_attachment_group.creation_date=[").append(getLocalizedCreationDate()).append("]\n");
        }
        if (hasModifiedDate()) {
            ret.append("document_attachment_group.modified_date=[").append(getLocalizedModifiedDate()).append("]\n");
        }
        ret.append("document_attachment_group.is_draft=[").append(getIsDraft()).append("]\n");
        if (hasEsAccountGroupId()) {
            ret.append("document_attachment_group.es_account_group_id=[").append(getEsAccountGroupId()).append("]\n");
        }
        return ret.toString();
    }

    /**
     * A simple unique one-line String representation of this model.
     * Can be used in a select html form tag.
     */
    public String toDisplayString() {
        StringBuilder ret = new StringBuilder("");
        if (hasDocumentAttachmentGroupId()) {
            ret.append(getDocumentAttachmentGroupId()).append(" ");
        }
        if (ret.toString().length() > 64) {
            return ret.toString().substring(0, 64) + "...";
        }
        if (ret.toString().trim().length() == 0) {
            return "hashcode=" + hashCode();
        }
        return ret.toString();
    }

    /**
     * compare this object to the passed instance
     *
     * @param object the object to compare with
     * @return a integer
     */
    public int compareTo(Object object) {
        DocumentAttachmentGroupModel obj = (DocumentAttachmentGroupModel) object;
        return new CompareToBuilder().append(getDocumentAttachmentGroupId(), obj.getDocumentAttachmentGroupId()).append(getVersion(), obj.getVersion()).append(getEsGroupId(), obj.getEsGroupId()).append(getEsParentGroupId(), obj.getEsParentGroupId()).append(getParentGroupId(), obj.getParentGroupId()).append(getCreationDate(), obj.getCreationDate()).append(getModifiedDate(), obj.getModifiedDate()).append(getIsDraft(), obj.getIsDraft()).append(getEsAccountGroupId(), obj.getEsAccountGroupId()).toComparison();
    }
}
