package com.qcs.eduquill.sl.objectmodel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sreekanth
 */
@Entity
@Table(name = "page_schema")
@NamedQueries({ @NamedQuery(name = "PAGE_SCHEMA.findAll", query = "SELECT p FROM PAGE_SCHEMA p"), @NamedQuery(name = "PAGE_SCHEMA.findByTableName", query = "SELECT p FROM PAGE_SCHEMA p WHERE p.tableName = :tableName"), @NamedQuery(name = "PAGE_SCHEMA.findByPageSchema", query = "SELECT p FROM PAGE_SCHEMA p WHERE p.pageSchema = :pageSchema"), @NamedQuery(name = "PAGE_SCHEMA.findByCreatedOn", query = "SELECT p FROM PAGE_SCHEMA p WHERE p.createdOn = :createdOn"), @NamedQuery(name = "PAGE_SCHEMA.findByCreatedBy", query = "SELECT p FROM PAGE_SCHEMA p WHERE p.createdBy = :createdBy") })
public class PAGE_SCHEMA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "table_name")
    private String tableName;

    @Column(name = "page_schema")
    private String pageSchema;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "organization_id")
    private Long organizationId;

    public PAGE_SCHEMA() {
    }

    public PAGE_SCHEMA(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPageSchema() {
        return pageSchema;
    }

    public void setPageSchema(String pageSchema) {
        this.pageSchema = pageSchema;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tableName != null ? tableName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PAGE_SCHEMA)) {
            return false;
        }
        PAGE_SCHEMA other = (PAGE_SCHEMA) object;
        if ((this.tableName == null && other.tableName != null) || (this.tableName != null && !this.tableName.equals(other.tableName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qcs.eduquill.sl.objectmodel.PAGE_SCHEMA[tableName=" + tableName + "]";
    }
}
