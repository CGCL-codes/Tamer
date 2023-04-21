package org.fosterapet.dao;

import java.util.*;
import org.greatlogic.gae.dao.DAOAnnotations.EntityProperty;
import org.greatlogic.gae.dao.*;
import org.greatlogic.gae.dao.DAOEnums.EDAOAction;

public class Person extends DAOBase {

    @EntityProperty
    private Date dateOfBirth;

    @EntityProperty(nullable = false, unique = true)
    private long domainUserId;

    @EntityProperty(maxLength = 30, nullable = false)
    private String nameFirst;

    @EntityProperty(maxLength = 30, nullable = false)
    private String nameLast;

    @EntityProperty(maxLength = 50)
    private String phoneNumberHome;

    @EntityProperty(maxLength = 50)
    private String phoneNumberMobile;

    @EntityProperty(maxLength = 50)
    private String phoneNumberOffice;

    @EntityProperty(listPropertyType = EPropertyType.LongList, nullable = false)
    private List<Long> roleIds;

    public Person(final EDAOAction action, final Object propertyValues, final String... foreignKeyNames) throws Exception {
        super(action, propertyValues, foreignKeyNames);
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDomainUserId(long domainUserId) {
        this.domainUserId = domainUserId;
    }

    public long getDomainUserId() {
        return domainUserId;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPhoneNumberHome() {
        return phoneNumberHome;
    }

    public void setPhoneNumberHome(String phoneNumberHome) {
        this.phoneNumberHome = phoneNumberHome;
    }

    public String getPhoneNumberMobile() {
        return phoneNumberMobile;
    }

    public void setPhoneNumberMobile(String phoneNumberMobile) {
        this.phoneNumberMobile = phoneNumberMobile;
    }

    public String getPhoneNumberOffice() {
        return phoneNumberOffice;
    }

    public void setPhoneNumberOffice(String phoneNumberOffice) {
        this.phoneNumberOffice = phoneNumberOffice;
    }
}
