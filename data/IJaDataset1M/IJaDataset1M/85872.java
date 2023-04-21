package com.ohioedge.j2ee.api.org.person.ejb;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.ejb.*;
import javax.naming.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ManagedTransientObject;
import com.ohioedge.j2ee.api.person.ejb.*;
import com.ohioedge.j2ee.api.org.ejb.Organization;
import com.ohioedge.j2ee.api.org.ejb.OrganizationHome;
import com.ohioedge.j2ee.api.org.ejb.OrganizationPK;

/**
 * @(#)EmployeeEJB.java	1.350 01/12/03
 * @author Sandeep Dixit
 * @version 1.350, 01/12/03
 * @see     org.j2eebuilder.view.ValueObjectFactory#getDataVO(
 *			java.lang.Object, java.lang.Class)
 * @since OEC1.2
 */
public abstract class EmployeeEJB extends org.j2eebuilder.model.ejb.SignatureAbstract implements EntityBean {

    public Integer getPersonID() {
        return getEmployeeID();
    }

    public Integer getCompanyID() {
        return getOrganizationID();
    }

    public Integer getMechanismID() {
        return getEmployeeID();
    }

    public String getName() {
        return getUsername();
    }

    public String getDescription() {
        StringBuffer buf = new StringBuffer();
        buf.append(getLastName());
        buf.append(", ");
        buf.append(getFirstName());
        return buf.toString();
    }

    public org.j2eebuilder.model.ManagedTransientObject getDataVO() throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        return org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.getDataVO(this.ctx.getPrimaryKey(), this, com.ohioedge.j2ee.api.org.person.EmployeeBean.class);
    }

    public void setDataVO(org.j2eebuilder.model.ManagedTransientObject valueObject, org.j2eebuilder.ComponentDefinition componentDefinition, Integer mechanismID) throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.setDataVO(this, valueObject, componentDefinition);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(mechanismID);
    }

    public void setNamePrefixID(Integer namePrefixID, Integer modifiedBy) {
        setNamePrefixID(namePrefixID);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setFirstName(String firstName, Integer modifiedBy) {
        setFirstName(firstName);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setMiddleInitial(String middleInitial, Integer modifiedBy) {
        setMiddleInitial(middleInitial);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setLastName(String lastName, Integer modifiedBy) {
        setLastName(lastName);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setNameSuffixID(Integer nameSuffixID, Integer modifiedBy) {
        setNameSuffixID(nameSuffixID);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setNameTitleID(Integer nameTitleID, Integer modifiedBy) {
        setNameTitleID(nameTitleID);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setPhone(String phone, Integer modifiedBy) {
        setPhone(phone);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setPhoneExtension(Integer phoneExtension, Integer modifiedBy) {
        setPhoneExtension(phoneExtension);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setPhone2(String phone2, Integer modifiedBy) {
        setPhone2(phone2);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setPhone2Extension(Integer phone2Extension, Integer modifiedBy) {
        setPhone2Extension(phone2Extension);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setFax(String fax, Integer modifiedBy) {
        setFax(fax);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public String getEmailAddress() {
        return getEmail();
    }

    public void setEmailAddress(String email, Integer modifiedBy) {
        setEmail(email);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setEmail(String email, Integer modifiedBy) {
        setEmail(email);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setUsername(String username, Integer modifiedBy) {
        setUsername(username);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setPassword(String password, Integer modifiedBy) {
        setPassword(password);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setSmtp(String smtpServer, String smtpUsername, String smtpPassword, Integer modifiedBy) {
        setSmtpServer(smtpServer);
        setSmtpUsername(smtpUsername);
        setSmtpPassword(smtpPassword);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public EmployeeEJB() {
    }

    public EmployeePK ejbCreate(Integer organizationID, Integer employeeID, Integer namePrefixID, String firstName, String middleInitial, String lastName, Integer nameSuffixID, Integer nameTitleID, String phone, Integer phoneExtension, String phone2, Integer phoneExtension2, String fax, String email, String username, String password, String smtpServer, String smtpUsername, String smtpPassword, Integer createdBy) throws CreateException {
        setOrganizationID(organizationID);
        setEmployeeID(employeeID);
        setNamePrefixID(namePrefixID);
        setFirstName(firstName);
        setMiddleInitial(middleInitial);
        setLastName(lastName);
        setNameSuffixID(nameSuffixID);
        setNameTitleID(nameTitleID);
        setPhone(phone);
        setPhoneExtension(phoneExtension);
        setPhone2(phone2);
        setPhone2Extension(phoneExtension2);
        setFax(fax);
        setEmail(email);
        setUsername(username);
        setPassword(password);
        setSmtpServer(smtpServer);
        setSmtpUsername(smtpUsername);
        setSmtpPassword(smtpPassword);
        setCreatedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setCreatedBy(createdBy);
        return null;
    }

    public void ejbPostCreate(Integer organizationID, Integer employeeID, Integer namePrefixID, String firstName, String middleInitial, String lastName, Integer nameSuffixID, Integer nameTitleID, String phone, Integer phoneExtension, String phone2, Integer phoneExtension2, String fax, String email, String username, String password, String smtpServer, String smtpUsername, String smtpPassword, Integer createdBy) throws CreateException {
    }

    public void ejbActivate() {
    }

    public void ejbLoad() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() throws RemoveException {
    }

    public void ejbStore() {
    }

    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    public void unsetEntityContext() {
        this.ctx = null;
    }

    private EntityContext ctx;

    public abstract Integer getOrganizationID();

    public abstract void setOrganizationID(Integer organizationID);

    public abstract Integer getEmployeeID();

    public abstract void setEmployeeID(Integer employeeID);

    public abstract Integer getNamePrefixID();

    public abstract void setNamePrefixID(Integer namePrefixID);

    public abstract String getFirstName();

    public abstract void setFirstName(String firstName);

    public abstract String getMiddleInitial();

    public abstract void setMiddleInitial(String middleInitial);

    public abstract String getLastName();

    public abstract void setLastName(String lastName);

    public abstract Integer getNameSuffixID();

    public abstract void setNameSuffixID(Integer nameSuffixID);

    public abstract Integer getNameTitleID();

    public abstract void setNameTitleID(Integer nameTitleID);

    public abstract String getPhone();

    public abstract void setPhone(String phone);

    public abstract Integer getPhoneExtension();

    public abstract void setPhoneExtension(Integer phoneExtension);

    public abstract String getPhone2();

    public abstract void setPhone2(String phone2);

    public abstract Integer getPhone2Extension();

    public abstract void setPhone2Extension(Integer phone2Extension);

    public abstract String getFax();

    public abstract void setFax(String fax);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getUsername();

    public abstract void setUsername(String username);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getSmtpServer();

    public abstract void setSmtpServer(String smtpServer);

    public abstract String getSmtpUsername();

    public abstract void setSmtpUsername(String smtpUsername);

    public abstract String getSmtpPassword();

    public abstract void setSmtpPassword(String smtpPassword);
}
