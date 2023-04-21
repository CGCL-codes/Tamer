package com.ohioedge.j2ee.api.org.hierarchy.ejb;

/**
 * @(#)OrganizationHierarchyPK.java	1.350 01/12/03
 * Primary Key
 * @version 1.3.1
 * @since OEC1.2
 */
public class OrganizationHierarchyPK implements java.io.Serializable {

    public OrganizationHierarchyPK(Integer organizationHierarchyID) {
        this.organizationHierarchyID = organizationHierarchyID;
    }

    public OrganizationHierarchyPK() {
    }

    public String toString() {
        return String.valueOf(this.organizationHierarchyID);
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if (pk instanceof OrganizationHierarchyPK) return this.hashCode() == ((OrganizationHierarchyPK) pk).hashCode();
        return false;
    }

    public Integer organizationHierarchyID;

    public Integer getOrganizationHierarchyID() {
        return organizationHierarchyID;
    }

    public void setOrganizationHierarchyID(Integer organizationHierarchyID) {
        this.organizationHierarchyID = organizationHierarchyID;
    }
}
