package com.voxdei.voxcontentSE.DAO.vdContent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.voxdei.voxcontentSE.DAO.basic.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * VdContent is a mapping of vd_content Table.
 * @author Michael Salgado
 * @company VoxDei.
*/
@Entity
@Table(name = "vd_content")
public class VdContent implements Serializable, Basic {

    private static final long serialVersionUID = 2874354886066287825L;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "METADATA", nullable = false)
    private String metadata;

    @Column(name = "HITS", nullable = false)
    private Long hits;

    @Column(name = "METADESC")
    private String metadesc;

    @Column(name = "METAKEY")
    private String metakey;

    @Column(name = "ORDERNUM")
    private Long ordernum;

    @Column(name = "ID_PARENT")
    private Long idParent;

    @Column(name = "VERSION", nullable = false)
    private Long version;

    @Column(name = "PERMISSIONS", nullable = false)
    private Long permissions;

    @Column(name = "PARAMETERS", nullable = false)
    private String parameters;

    @Column(name = "UNPUBLISH_UNTIL")
    private java.util.Date unpublishUntil;

    @Column(name = "PUBLISH_SINCE", nullable = false)
    private java.util.Date publishSince;

    @Column(name = "CHECKED_DATE")
    private java.util.Date checkedDate;

    @Column(name = "CHECKED_OUT", nullable = false)
    private Boolean checkedOut;

    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;

    @Column(name = "LAST_UPDATE")
    private java.util.Date lastUpdate;

    @Column(name = "AUTHOR_ALIAS")
    private String authorAlias;

    @Column(name = "AUTHOR_ID", nullable = false)
    private Long authorId;

    @Column(name = "CREATED_DATE", nullable = false)
    private java.util.Date createdDate;

    @Column(name = "ID_", nullable = false)
    private Long id_;

    @Column(name = "ID_SECTION", nullable = false)
    private Long idSection;

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

    @Column(name = "FULL_TEXT", nullable = false)
    private String fullText;

    @Column(name = "INTRO", nullable = false)
    private String intro;

    @Column(name = "ALIAS", nullable = false)
    private String alias;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * Constructor
     */
    public VdContent() {
    }

    /**
     * Getter method for type.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.TYPE</li>
     * <li>column size: 32</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for type.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to type
     */
    public void setType(String newVal) {
        if ((newVal != null && type != null && (newVal.compareTo(type) == 0)) || (newVal == null && type == null)) {
            return;
        }
        type = newVal;
    }

    /**
     * Getter method for metadata.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.METADATA</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of metadata
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * Setter method for metadata.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to metadata
     */
    public void setMetadata(String newVal) {
        if ((newVal != null && metadata != null && (newVal.compareTo(metadata) == 0)) || (newVal == null && metadata == null)) {
            return;
        }
        metadata = newVal;
    }

    /**
     * Getter method for hits.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.HITS</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of hits
     */
    public Long getHits() {
        return hits;
    }

    /**
     * Setter method for hits.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to hits
     */
    public void setHits(Long newVal) {
        if ((newVal != null && hits != null && (newVal.compareTo(hits) == 0)) || (newVal == null && hits == null)) {
            return;
        }
        hits = newVal;
    }

    /**
     * Setter method for hits.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to hits
     */
    public void setHits(long newVal) {
        setHits(new Long(newVal));
    }

    /**
     * Getter method for metadesc.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.METADESC</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of metadesc
     */
    public String getMetadesc() {
        return metadesc;
    }

    /**
     * Setter method for metadesc.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to metadesc
     */
    public void setMetadesc(String newVal) {
        if ((newVal != null && metadesc != null && (newVal.compareTo(metadesc) == 0)) || (newVal == null && metadesc == null)) {
            return;
        }
        metadesc = newVal;
    }

    /**
     * Getter method for metakey.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.METAKEY</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of metakey
     */
    public String getMetakey() {
        return metakey;
    }

    /**
     * Setter method for metakey.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to metakey
     */
    public void setMetakey(String newVal) {
        if ((newVal != null && metakey != null && (newVal.compareTo(metakey) == 0)) || (newVal == null && metakey == null)) {
            return;
        }
        metakey = newVal;
    }

    /**
     * Getter method for ordernum.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ORDERNUM</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of ordernum
     */
    public Long getOrdernum() {
        return ordernum;
    }

    /**
     * Setter method for ordernum.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to ordernum
     */
    public void setOrdernum(Long newVal) {
        if ((newVal != null && ordernum != null && (newVal.compareTo(ordernum) == 0)) || (newVal == null && ordernum == null)) {
            return;
        }
        ordernum = newVal;
    }

    /**
     * Setter method for ordernum.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to ordernum
     */
    public void setOrdernum(long newVal) {
        setOrdernum(new Long(newVal));
    }

    /**
     * Getter method for idParent.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ID_PARENT</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of idParent
     */
    public Long getIdParent() {
        return idParent;
    }

    /**
     * Setter method for idParent.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to idParent
     */
    public void setIdParent(Long newVal) {
        if ((newVal != null && idParent != null && (newVal.compareTo(idParent) == 0)) || (newVal == null && idParent == null)) {
            return;
        }
        idParent = newVal;
    }

    /**
     * Setter method for idParent.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to idParent
     */
    public void setIdParent(long newVal) {
        setIdParent(new Long(newVal));
    }

    /**
     * Getter method for version.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.VERSION</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Setter method for version.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to version
     */
    public void setVersion(Long newVal) {
        if ((newVal != null && version != null && (newVal.compareTo(version) == 0)) || (newVal == null && version == null)) {
            return;
        }
        version = newVal;
    }

    /**
     * Setter method for version.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to version
     */
    public void setVersion(long newVal) {
        setVersion(new Long(newVal));
    }

    /**
     * Getter method for permissions.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.PERMISSIONS</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of permissions
     */
    public Long getPermissions() {
        return permissions;
    }

    /**
     * Setter method for permissions.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to permissions
     */
    public void setPermissions(Long newVal) {
        if ((newVal != null && permissions != null && (newVal.compareTo(permissions) == 0)) || (newVal == null && permissions == null)) {
            return;
        }
        permissions = newVal;
    }

    /**
     * Setter method for permissions.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to permissions
     */
    public void setPermissions(long newVal) {
        setPermissions(new Long(newVal));
    }

    /**
     * Getter method for parameters.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.PARAMETERS</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Setter method for parameters.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to parameters
     */
    public void setParameters(String newVal) {
        if ((newVal != null && parameters != null && (newVal.compareTo(parameters) == 0)) || (newVal == null && parameters == null)) {
            return;
        }
        parameters = newVal;
    }

    /**
     * Getter method for unpublishUntil.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.UNPUBLISH_UNTIL</li>
     * <li>column size: 19</li>
     * <li>jdbc type returned by the driver: Types.TIMESTAMP</li>
     * </ul>
     *
     * @return the value of unpublishUntil
     */
    public java.util.Date getUnpublishUntil() {
        return unpublishUntil;
    }

    /**
     * Setter method for unpublishUntil.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to unpublishUntil
     */
    public void setUnpublishUntil(java.util.Date newVal) {
        if ((newVal != null && unpublishUntil != null && (newVal.compareTo(unpublishUntil) == 0)) || (newVal == null && unpublishUntil == null)) {
            return;
        }
        unpublishUntil = newVal;
    }

    /**
     * Setter method for unpublishUntil.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to unpublishUntil
     */
    public void setUnpublishUntil(long newVal) {
        setUnpublishUntil(new java.util.Date(newVal));
    }

    /**
     * Getter method for publishSince.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.PUBLISH_SINCE</li>
     * <li>column size: 19</li>
     * <li>jdbc type returned by the driver: Types.TIMESTAMP</li>
     * </ul>
     *
     * @return the value of publishSince
     */
    public java.util.Date getPublishSince() {
        return publishSince;
    }

    /**
     * Setter method for publishSince.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to publishSince
     */
    public void setPublishSince(java.util.Date newVal) {
        if ((newVal != null && publishSince != null && (newVal.compareTo(publishSince) == 0)) || (newVal == null && publishSince == null)) {
            return;
        }
        publishSince = newVal;
    }

    /**
     * Setter method for publishSince.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to publishSince
     */
    public void setPublishSince(long newVal) {
        setPublishSince(new java.util.Date(newVal));
    }

    /**
     * Getter method for checkedDate.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.CHECKED_DATE</li>
     * <li>column size: 19</li>
     * <li>jdbc type returned by the driver: Types.TIMESTAMP</li>
     * </ul>
     *
     * @return the value of checkedDate
     */
    public java.util.Date getCheckedDate() {
        return checkedDate;
    }

    /**
     * Setter method for checkedDate.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to checkedDate
     */
    public void setCheckedDate(java.util.Date newVal) {
        if ((newVal != null && checkedDate != null && (newVal.compareTo(checkedDate) == 0)) || (newVal == null && checkedDate == null)) {
            return;
        }
        checkedDate = newVal;
    }

    /**
     * Setter method for checkedDate.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to checkedDate
     */
    public void setCheckedDate(long newVal) {
        setCheckedDate(new java.util.Date(newVal));
    }

    /**
     * Getter method for checkedOut.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.CHECKED_OUT</li>
     * <li>column size: 0</li>
     * <li>jdbc type returned by the driver: Types.BIT</li>
     * </ul>
     *
     * @return the value of checkedOut
     */
    public Boolean getCheckedOut() {
        return checkedOut;
    }

    /**
     * Setter method for checkedOut.
     * <br>
     * Attention, there will be no comparison with current value which
     * means calling this method will mark the field as 'modified' in all cases.
     *
     * @param newVal the new value to be assigned to checkedOut
     */
    public void setCheckedOut(Boolean newVal) {
        if ((newVal != null && checkedOut != null && newVal.equals(checkedOut)) || (newVal == null && checkedOut == null)) {
            return;
        }
        checkedOut = newVal;
    }

    /**
     * Setter method for checkedOut.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to checkedOut
     */
    public void setCheckedOut(boolean newVal) {
        setCheckedOut(new Boolean(newVal));
    }

    /**
     * Getter method for modifiedBy.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.MODIFIED_BY</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of modifiedBy
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Setter method for modifiedBy.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to modifiedBy
     */
    public void setModifiedBy(Long newVal) {
        if ((newVal != null && modifiedBy != null && (newVal.compareTo(modifiedBy) == 0)) || (newVal == null && modifiedBy == null)) {
            return;
        }
        modifiedBy = newVal;
    }

    /**
     * Setter method for modifiedBy.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to modifiedBy
     */
    public void setModifiedBy(long newVal) {
        setModifiedBy(new Long(newVal));
    }

    /**
     * Getter method for lastUpdate.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.LAST_UPDATE</li>
     * <li>column size: 19</li>
     * <li>jdbc type returned by the driver: Types.TIMESTAMP</li>
     * </ul>
     *
     * @return the value of lastUpdate
     */
    public java.util.Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Setter method for lastUpdate.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to lastUpdate
     */
    public void setLastUpdate(java.util.Date newVal) {
        if ((newVal != null && lastUpdate != null && (newVal.compareTo(lastUpdate) == 0)) || (newVal == null && lastUpdate == null)) {
            return;
        }
        lastUpdate = newVal;
    }

    /**
     * Setter method for lastUpdate.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to lastUpdate
     */
    public void setLastUpdate(long newVal) {
        setLastUpdate(new java.util.Date(newVal));
    }

    /**
     * Getter method for authorAlias.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.AUTHOR_ALIAS</li>
     * <li>column size: 255</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of authorAlias
     */
    public String getAuthorAlias() {
        return authorAlias;
    }

    /**
     * Setter method for authorAlias.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to authorAlias
     */
    public void setAuthorAlias(String newVal) {
        if ((newVal != null && authorAlias != null && (newVal.compareTo(authorAlias) == 0)) || (newVal == null && authorAlias == null)) {
            return;
        }
        authorAlias = newVal;
    }

    /**
     * Getter method for authorId.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.AUTHOR_ID</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of authorId
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     * Setter method for authorId.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to authorId
     */
    public void setAuthorId(Long newVal) {
        if ((newVal != null && authorId != null && (newVal.compareTo(authorId) == 0)) || (newVal == null && authorId == null)) {
            return;
        }
        authorId = newVal;
    }

    /**
     * Setter method for authorId.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to authorId
     */
    public void setAuthorId(long newVal) {
        setAuthorId(new Long(newVal));
    }

    /**
     * Getter method for createdDate.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.CREATED_DATE</li>
     * <li>column size: 19</li>
     * <li>jdbc type returned by the driver: Types.TIMESTAMP</li>
     * </ul>
     *
     * @return the value of createdDate
     */
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter method for createdDate.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to createdDate
     */
    public void setCreatedDate(java.util.Date newVal) {
        if ((newVal != null && createdDate != null && (newVal.compareTo(createdDate) == 0)) || (newVal == null && createdDate == null)) {
            return;
        }
        createdDate = newVal;
    }

    /**
     * Setter method for createdDate.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to createdDate
     */
    public void setCreatedDate(long newVal) {
        setCreatedDate(new java.util.Date(newVal));
    }

    /**
     * Getter method for id_.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ID_</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of id_
     */
    public Long getId_() {
        return id_;
    }

    /**
     * Setter method for id_.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to id_
     */
    public void setId_(Long newVal) {
        if ((newVal != null && id_ != null && (newVal.compareTo(id_) == 0)) || (newVal == null && id_ == null)) {
            return;
        }
        id_ = newVal;
    }

    /**
     * Setter method for id_.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to id_
     */
    public void setId_(long newVal) {
        setId_(new Long(newVal));
    }

    /**
     * Getter method for idSection.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ID_SECTION</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of idSection
     */
    public Long getIdSection() {
        return idSection;
    }

    /**
     * Setter method for idSection.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to idSection
     */
    public void setIdSection(Long newVal) {
        if ((newVal != null && idSection != null && (newVal.compareTo(idSection) == 0)) || (newVal == null && idSection == null)) {
            return;
        }
        idSection = newVal;
    }

    /**
     * Setter method for idSection.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to idSection
     */
    public void setIdSection(long newVal) {
        setIdSection(new Long(newVal));
    }

    /**
     * Getter method for enabled.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ENABLED</li>
     * <li>column size: 0</li>
     * <li>jdbc type returned by the driver: Types.BIT</li>
     * </ul>
     *
     * @return the value of enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Setter method for enabled.
     * <br>
     * Attention, there will be no comparison with current value which
     * means calling this method will mark the field as 'modified' in all cases.
     *
     * @param newVal the new value to be assigned to enabled
     */
    public void setEnabled(Boolean newVal) {
        if ((newVal != null && enabled != null && newVal.equals(enabled)) || (newVal == null && enabled == null)) {
            return;
        }
        enabled = newVal;
    }

    /**
     * Setter method for enabled.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to enabled
     */
    public void setEnabled(boolean newVal) {
        setEnabled(new Boolean(newVal));
    }

    /**
     * Getter method for fullText.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.FULL_TEXT</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of fullText
     */
    public String getFullText() {
        return fullText;
    }

    /**
     * Setter method for fullText.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to fullText
     */
    public void setFullText(String newVal) {
        if ((newVal != null && fullText != null && (newVal.compareTo(fullText) == 0)) || (newVal == null && fullText == null)) {
            return;
        }
        fullText = newVal;
    }

    /**
     * Getter method for intro.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.INTRO</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of intro
     */
    public String getIntro() {
        return intro;
    }

    /**
     * Setter method for intro.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to intro
     */
    public void setIntro(String newVal) {
        if ((newVal != null && intro != null && (newVal.compareTo(intro) == 0)) || (newVal == null && intro == null)) {
            return;
        }
        intro = newVal;
    }

    /**
     * Getter method for alias.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ALIAS</li>
     * <li>column size: 255</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Setter method for alias.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to alias
     */
    public void setAlias(String newVal) {
        if ((newVal != null && alias != null && (newVal.compareTo(alias) == 0)) || (newVal == null && alias == null)) {
            return;
        }
        alias = newVal;
    }

    /**
     * Getter method for title.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.TITLE</li>
     * <li>column size: 255</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter method for title.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to title
     */
    public void setTitle(String newVal) {
        if ((newVal != null && title != null && (newVal.compareTo(title) == 0)) || (newVal == null && title == null)) {
            return;
        }
        title = newVal;
    }

    /**
     * Getter method for id.
     * <br>
     * PRIMARY KEY.<br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_content.ID</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for id.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to id
     */
    public void setId(Long newVal) {
        if ((newVal != null && id != null && (newVal.compareTo(id) == 0)) || (newVal == null && id == null)) {
            return;
        }
        id = newVal;
    }

    /**
     * Setter method for id.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to id
     */
    public void setId(long newVal) {
        setId(new Long(newVal));
    }

    /**
     * Copies the passed bean into the current bean.
     *
     * @param bean the bean to copy into the current bean
     */
    public void copy(VdContent bean) {
        setType(bean.getType());
        setMetadata(bean.getMetadata());
        setHits(bean.getHits());
        setMetadesc(bean.getMetadesc());
        setMetakey(bean.getMetakey());
        setOrdernum(bean.getOrdernum());
        setIdParent(bean.getIdParent());
        setVersion(bean.getVersion());
        setPermissions(bean.getPermissions());
        setParameters(bean.getParameters());
        setUnpublishUntil(bean.getUnpublishUntil());
        setPublishSince(bean.getPublishSince());
        setCheckedDate(bean.getCheckedDate());
        setCheckedOut(bean.getCheckedOut());
        setModifiedBy(bean.getModifiedBy());
        setLastUpdate(bean.getLastUpdate());
        setAuthorAlias(bean.getAuthorAlias());
        setAuthorId(bean.getAuthorId());
        setCreatedDate(bean.getCreatedDate());
        setId_(bean.getId_());
        setIdSection(bean.getIdSection());
        setEnabled(bean.getEnabled());
        setFullText(bean.getFullText());
        setIntro(bean.getIntro());
        setAlias(bean.getAlias());
        setTitle(bean.getTitle());
        setId(bean.getId());
    }

    /**
     * return a dictionnary of the object
     */
    @Override
    public Map<String, String> getDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("type", getType() == null ? "" : getType().toString());
        dictionnary.put("metadata", getMetadata() == null ? "" : getMetadata().toString());
        dictionnary.put("hits", getHits() == null ? "" : getHits().toString());
        dictionnary.put("metadesc", getMetadesc() == null ? "" : getMetadesc().toString());
        dictionnary.put("metakey", getMetakey() == null ? "" : getMetakey().toString());
        dictionnary.put("ordernum", getOrdernum() == null ? "" : getOrdernum().toString());
        dictionnary.put("id_parent", getIdParent() == null ? "" : getIdParent().toString());
        dictionnary.put("version", getVersion() == null ? "" : getVersion().toString());
        dictionnary.put("permissions", getPermissions() == null ? "" : getPermissions().toString());
        dictionnary.put("parameters", getParameters() == null ? "" : getParameters().toString());
        dictionnary.put("unpublish_until", getUnpublishUntil() == null ? "" : getUnpublishUntil().toString());
        dictionnary.put("publish_since", getPublishSince() == null ? "" : getPublishSince().toString());
        dictionnary.put("checked_date", getCheckedDate() == null ? "" : getCheckedDate().toString());
        dictionnary.put("checked_out", getCheckedOut() == null ? "" : getCheckedOut().toString());
        dictionnary.put("modified_by", getModifiedBy() == null ? "" : getModifiedBy().toString());
        dictionnary.put("last_update", getLastUpdate() == null ? "" : getLastUpdate().toString());
        dictionnary.put("author_alias", getAuthorAlias() == null ? "" : getAuthorAlias().toString());
        dictionnary.put("author_id", getAuthorId() == null ? "" : getAuthorId().toString());
        dictionnary.put("created_date", getCreatedDate() == null ? "" : getCreatedDate().toString());
        dictionnary.put("id_", getId_() == null ? "" : getId_().toString());
        dictionnary.put("id_section", getIdSection() == null ? "" : getIdSection().toString());
        dictionnary.put("enabled", getEnabled() == null ? "" : getEnabled().toString());
        dictionnary.put("full_text", getFullText() == null ? "" : getFullText().toString());
        dictionnary.put("intro", getIntro() == null ? "" : getIntro().toString());
        dictionnary.put("alias", getAlias() == null ? "" : getAlias().toString());
        dictionnary.put("title", getTitle() == null ? "" : getTitle().toString());
        dictionnary.put("id", getId() == null ? "" : getId().toString());
        return dictionnary;
    }

    /**
     * return a dictionnary of the pk columns
     */
    @Override
    public Map<String, String> getPkDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("id", getId() == null ? "" : getId().toString());
        return dictionnary;
    }

    /**
     * return a the value string representation of the given field
     */
    @Override
    public String getValue(String column) {
        if (null == column || "".equals(column)) {
            return "";
        } else if ("TYPE".equalsIgnoreCase(column) || "type".equalsIgnoreCase(column)) {
            return getType() == null ? "" : getType().toString();
        } else if ("METADATA".equalsIgnoreCase(column) || "metadata".equalsIgnoreCase(column)) {
            return getMetadata() == null ? "" : getMetadata().toString();
        } else if ("HITS".equalsIgnoreCase(column) || "hits".equalsIgnoreCase(column)) {
            return getHits() == null ? "" : getHits().toString();
        } else if ("METADESC".equalsIgnoreCase(column) || "metadesc".equalsIgnoreCase(column)) {
            return getMetadesc() == null ? "" : getMetadesc().toString();
        } else if ("METAKEY".equalsIgnoreCase(column) || "metakey".equalsIgnoreCase(column)) {
            return getMetakey() == null ? "" : getMetakey().toString();
        } else if ("ORDERNUM".equalsIgnoreCase(column) || "ordernum".equalsIgnoreCase(column)) {
            return getOrdernum() == null ? "" : getOrdernum().toString();
        } else if ("ID_PARENT".equalsIgnoreCase(column) || "idParent".equalsIgnoreCase(column)) {
            return getIdParent() == null ? "" : getIdParent().toString();
        } else if ("VERSION".equalsIgnoreCase(column) || "version".equalsIgnoreCase(column)) {
            return getVersion() == null ? "" : getVersion().toString();
        } else if ("PERMISSIONS".equalsIgnoreCase(column) || "permissions".equalsIgnoreCase(column)) {
            return getPermissions() == null ? "" : getPermissions().toString();
        } else if ("PARAMETERS".equalsIgnoreCase(column) || "parameters".equalsIgnoreCase(column)) {
            return getParameters() == null ? "" : getParameters().toString();
        } else if ("UNPUBLISH_UNTIL".equalsIgnoreCase(column) || "unpublishUntil".equalsIgnoreCase(column)) {
            return getUnpublishUntil() == null ? "" : getUnpublishUntil().toString();
        } else if ("PUBLISH_SINCE".equalsIgnoreCase(column) || "publishSince".equalsIgnoreCase(column)) {
            return getPublishSince() == null ? "" : getPublishSince().toString();
        } else if ("CHECKED_DATE".equalsIgnoreCase(column) || "checkedDate".equalsIgnoreCase(column)) {
            return getCheckedDate() == null ? "" : getCheckedDate().toString();
        } else if ("CHECKED_OUT".equalsIgnoreCase(column) || "checkedOut".equalsIgnoreCase(column)) {
            return getCheckedOut() == null ? "" : getCheckedOut().toString();
        } else if ("MODIFIED_BY".equalsIgnoreCase(column) || "modifiedBy".equalsIgnoreCase(column)) {
            return getModifiedBy() == null ? "" : getModifiedBy().toString();
        } else if ("LAST_UPDATE".equalsIgnoreCase(column) || "lastUpdate".equalsIgnoreCase(column)) {
            return getLastUpdate() == null ? "" : getLastUpdate().toString();
        } else if ("AUTHOR_ALIAS".equalsIgnoreCase(column) || "authorAlias".equalsIgnoreCase(column)) {
            return getAuthorAlias() == null ? "" : getAuthorAlias().toString();
        } else if ("AUTHOR_ID".equalsIgnoreCase(column) || "authorId".equalsIgnoreCase(column)) {
            return getAuthorId() == null ? "" : getAuthorId().toString();
        } else if ("CREATED_DATE".equalsIgnoreCase(column) || "createdDate".equalsIgnoreCase(column)) {
            return getCreatedDate() == null ? "" : getCreatedDate().toString();
        } else if ("ID_".equalsIgnoreCase(column) || "id_".equalsIgnoreCase(column)) {
            return getId_() == null ? "" : getId_().toString();
        } else if ("ID_SECTION".equalsIgnoreCase(column) || "idSection".equalsIgnoreCase(column)) {
            return getIdSection() == null ? "" : getIdSection().toString();
        } else if ("ENABLED".equalsIgnoreCase(column) || "enabled".equalsIgnoreCase(column)) {
            return getEnabled() == null ? "" : getEnabled().toString();
        } else if ("FULL_TEXT".equalsIgnoreCase(column) || "fullText".equalsIgnoreCase(column)) {
            return getFullText() == null ? "" : getFullText().toString();
        } else if ("INTRO".equalsIgnoreCase(column) || "intro".equalsIgnoreCase(column)) {
            return getIntro() == null ? "" : getIntro().toString();
        } else if ("ALIAS".equalsIgnoreCase(column) || "alias".equalsIgnoreCase(column)) {
            return getAlias() == null ? "" : getAlias().toString();
        } else if ("TITLE".equalsIgnoreCase(column) || "title".equalsIgnoreCase(column)) {
            return getTitle() == null ? "" : getTitle().toString();
        } else if ("ID".equalsIgnoreCase(column) || "id".equalsIgnoreCase(column)) {
            return getId() == null ? "" : getId().toString();
        }
        return "";
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VdContent)) {
            return false;
        }
        VdContent obj = (VdContent) object;
        return new EqualsBuilder().append(getType(), obj.getType()).append(getMetadata(), obj.getMetadata()).append(getHits(), obj.getHits()).append(getMetadesc(), obj.getMetadesc()).append(getMetakey(), obj.getMetakey()).append(getOrdernum(), obj.getOrdernum()).append(getIdParent(), obj.getIdParent()).append(getVersion(), obj.getVersion()).append(getPermissions(), obj.getPermissions()).append(getParameters(), obj.getParameters()).append(getUnpublishUntil(), obj.getUnpublishUntil()).append(getPublishSince(), obj.getPublishSince()).append(getCheckedDate(), obj.getCheckedDate()).append(getCheckedOut(), obj.getCheckedOut()).append(getModifiedBy(), obj.getModifiedBy()).append(getLastUpdate(), obj.getLastUpdate()).append(getAuthorAlias(), obj.getAuthorAlias()).append(getAuthorId(), obj.getAuthorId()).append(getCreatedDate(), obj.getCreatedDate()).append(getId_(), obj.getId_()).append(getIdSection(), obj.getIdSection()).append(getEnabled(), obj.getEnabled()).append(getFullText(), obj.getFullText()).append(getIntro(), obj.getIntro()).append(getAlias(), obj.getAlias()).append(getTitle(), obj.getTitle()).append(getId(), obj.getId()).isEquals();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-82280557, -700257973).append(getType()).append(getMetadata()).append(getHits()).append(getMetadesc()).append(getMetakey()).append(getOrdernum()).append(getIdParent()).append(getVersion()).append(getPermissions()).append(getParameters()).append(getUnpublishUntil()).append(getPublishSince()).append(getCheckedDate()).append(getCheckedOut()).append(getModifiedBy()).append(getLastUpdate()).append(getAuthorAlias()).append(getAuthorId()).append(getCreatedDate()).append(getId_()).append(getIdSection()).append(getEnabled()).append(getFullText()).append(getIntro()).append(getAlias()).append(getTitle()).append(getId()).toHashCode();
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return toString(ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
	 * you can use the following styles:
	 * <li>ToStringStyle.DEFAULT_STYLE</li>
	 * <li>ToStringStyle.MULTI_LINE_STYLE</li>
     * <li>ToStringStyle.NO_FIELD_NAMES_STYLE</li>
     * <li>ToStringStyle.SHORT_PREFIX_STYLE</li>
     * <li>ToStringStyle.SIMPLE_STYLE</li>
	 */
    public String toString(ToStringStyle style) {
        return new ToStringBuilder(this, style).append("TYPE", getType()).append("METADATA", getMetadata()).append("HITS", getHits()).append("METADESC", getMetadesc()).append("METAKEY", getMetakey()).append("ORDERNUM", getOrdernum()).append("ID_PARENT", getIdParent()).append("VERSION", getVersion()).append("PERMISSIONS", getPermissions()).append("PARAMETERS", getParameters()).append("UNPUBLISH_UNTIL", getUnpublishUntil()).append("PUBLISH_SINCE", getPublishSince()).append("CHECKED_DATE", getCheckedDate()).append("CHECKED_OUT", getCheckedOut()).append("MODIFIED_BY", getModifiedBy()).append("LAST_UPDATE", getLastUpdate()).append("AUTHOR_ALIAS", getAuthorAlias()).append("AUTHOR_ID", getAuthorId()).append("CREATED_DATE", getCreatedDate()).append("ID_", getId_()).append("ID_SECTION", getIdSection()).append("ENABLED", getEnabled()).append("FULL_TEXT", getFullText()).append("INTRO", getIntro()).append("ALIAS", getAlias()).append("TITLE", getTitle()).append("ID", getId()).toString();
    }

    public int compareTo(Object object) {
        VdContent obj = (VdContent) object;
        return new CompareToBuilder().append(getType(), obj.getType()).append(getMetadata(), obj.getMetadata()).append(getHits(), obj.getHits()).append(getMetadesc(), obj.getMetadesc()).append(getMetakey(), obj.getMetakey()).append(getOrdernum(), obj.getOrdernum()).append(getIdParent(), obj.getIdParent()).append(getVersion(), obj.getVersion()).append(getPermissions(), obj.getPermissions()).append(getParameters(), obj.getParameters()).append(getUnpublishUntil(), obj.getUnpublishUntil()).append(getPublishSince(), obj.getPublishSince()).append(getCheckedDate(), obj.getCheckedDate()).append(getCheckedOut(), obj.getCheckedOut()).append(getModifiedBy(), obj.getModifiedBy()).append(getLastUpdate(), obj.getLastUpdate()).append(getAuthorAlias(), obj.getAuthorAlias()).append(getAuthorId(), obj.getAuthorId()).append(getCreatedDate(), obj.getCreatedDate()).append(getId_(), obj.getId_()).append(getIdSection(), obj.getIdSection()).append(getEnabled(), obj.getEnabled()).append(getFullText(), obj.getFullText()).append(getIntro(), obj.getIntro()).append(getAlias(), obj.getAlias()).append(getTitle(), obj.getTitle()).append(getId(), obj.getId()).toComparison();
    }
}
