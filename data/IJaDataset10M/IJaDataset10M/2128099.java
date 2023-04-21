package com.fisoft.phucsinh.phucsinhsrv.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author vantinh
 */
@Entity
@Table(name = "mrole", schema = "")
@NamedQueries({ @NamedQuery(name = "RoleEntity.findAll", query = "SELECT g FROM RoleEntity g"), @NamedQuery(name = "RoleEntity.findByRoleID", query = "SELECT g FROM RoleEntity g WHERE g.roleID = :roleID") })
public class RoleEntity implements Serializable, ICommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "RoleID", nullable = false, length = 30)
    private String roleID;

    @Column(name = "RoleDesc", length = 200)
    private String roleDesc;

    @JoinColumn(name = "ModuleID", referencedColumnName = "ModuleID")
    @ManyToOne
    private ModuleEntity module;

    @Column(name = "DependentRole", nullable = false, length = 30)
    private String dependentRole;

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    @Basic(optional = false)
    @Column(name = "Creater", nullable = false, length = 16)
    private String creater;

    @Basic(optional = false)
    @Column(name = "CreateDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Updater", length = 16)
    private String updater;

    @Column(name = "UpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "Attribute1", length = 250)
    private String attribute1;

    @Column(name = "Attribute2", length = 250)
    private String attribute2;

    @Column(name = "Attribute3", length = 250)
    private String attribute3;

    @Column(name = "Attribute4", length = 250)
    private String attribute4;

    @Column(name = "Attribute5", length = 250)
    private String attribute5;

    @Version
    @Column(name = "Version", nullable = false)
    private int version;

    @ManyToMany(mappedBy = "roleEntityCollection")
    private Collection<GroupEntity> groupEntityCollection = new ArrayList<GroupEntity>();

    public RoleEntity() {
    }

    public RoleEntity(String roleID) {
        this.roleID = roleID;
    }

    public RoleEntity(String roleID, String creater, Date createDate) {
        this.roleID = roleID;
        this.creater = creater;
        this.createDate = createDate;
    }

    public String getID() {
        return roleID;
    }

    public void setID(Object roleID) {
        this.roleID = (String) roleID;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getDependentRole() {
        return dependentRole;
    }

    public void setDependentRole(String dependentRole) {
        this.dependentRole = dependentRole;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public int getVersion() {
        return version;
    }

    public Integer getActiveStatus() {
        return EntityStatus.ACTIVE.getValue();
    }

    public void setActiveStatus(Integer activeStatus) {
    }

    public Collection<GroupEntity> getGroupEntityCollection() {
        return groupEntityCollection;
    }

    public void setGroupEntityCollection(Collection<GroupEntity> groupEntityCollection) {
        this.groupEntityCollection = groupEntityCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleID != null ? roleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoleEntity)) {
            return false;
        }
        RoleEntity other = (RoleEntity) object;
        if ((this.roleID == null && other.roleID != null) || (this.roleID != null && !this.roleID.equals(other.roleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fisoft.mass.massprosrv.entity.RoleEntity[roleID=" + roleID + "]";
    }
}
