package org.stonefish.core.data;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ricardo
 */
@Entity
@Table(name = "permissions")
@NamedQueries({ @NamedQuery(name = "Permission.findByIdSection", query = "SELECT p FROM Permission p WHERE p.permissionPK.idSection = :idSection"), @NamedQuery(name = "Permission.findByIdPage", query = "SELECT p FROM Permission p WHERE p.permissionPK.idPage = :idPage"), @NamedQuery(name = "Permission.findByIdPageblock", query = "SELECT p FROM Permission p WHERE p.permissionPK.idPageblock = :idPageblock"), @NamedQuery(name = "Permission.findByIdApplicationinstan", query = "SELECT p FROM Permission p WHERE p.permissionPK.idApplicationinstan = :idApplicationinstan"), @NamedQuery(name = "Permission.findByIdPortalinstance", query = "SELECT p FROM Permission p WHERE p.permissionPK.idPortalinstance = :idPortalinstance"), @NamedQuery(name = "Permission.findByIdPermissiontype", query = "SELECT p FROM Permission p WHERE p.permissionPK.idPermissiontype = :idPermissiontype"), @NamedQuery(name = "Permission.findByIdentifier", query = "SELECT p FROM Permission p WHERE p.permissionPK.identifier = :identifier") })
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected PermissionPK permissionPK;

    public Permission() {
    }

    public Permission(PermissionPK permissionPK) {
        this.permissionPK = permissionPK;
    }

    public Permission(int idSection, int idPage, int idPageblock, int idApplicationinstan, int idPortalinstance, int idPermissiontype, int identifier) {
        this.permissionPK = new PermissionPK(idSection, idPage, idPageblock, idApplicationinstan, idPortalinstance, idPermissiontype, identifier);
    }

    public PermissionPK getPermissionPK() {
        return permissionPK;
    }

    public void setPermissionPK(PermissionPK permissionPK) {
        this.permissionPK = permissionPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permissionPK != null ? permissionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) object;
        if ((this.permissionPK == null && other.permissionPK != null) || (this.permissionPK != null && !this.permissionPK.equals(other.permissionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.stonefish.core.data.Permission[permissionPK=" + permissionPK + "]";
    }
}
