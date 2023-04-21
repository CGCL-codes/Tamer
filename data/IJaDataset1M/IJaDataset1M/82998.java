package com.ohioedge.j2ee.api.org.doc.ejb;

/**
 * @(#)DirectoryEJB.java 1.350 01/12/03
 * @version 1.3.1
 * @see org.j2eebuilder.view.ValueObjectFactory#getDataVO(java.lang.Object,
 *      java.lang.Class)
 * @since OEC1.2
 */
public class DirectoryBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    public DirectoryBean() {
    }

    public Integer directoryID;

    public String name;

    public String description;

    public String path;

    public Integer organizationID;

    public Integer mechanismID;

    public Integer getDirectoryID() {
        return directoryID;
    }

    public void setDirectoryID(Integer directoryID) {
        this.directoryID = directoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Integer organizationID) {
        this.organizationID = organizationID;
    }

    public Integer getMechanismID() {
        return mechanismID;
    }

    public void setMechanismID(Integer mechanismID) {
        this.mechanismID = mechanismID;
    }
}
