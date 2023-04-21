package org.apache.roller.presentation.forms;

import java.io.Serializable;
import java.util.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.roller.RollerException;
import org.apache.roller.pojos.UserData;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class UserForm extends ActionForm implements Serializable {

    public UserForm() {
    }

    public UserForm(UserData dataHolder, Locale locale) throws RollerException {
        copyFrom(dataHolder, locale);
    }

    public List getPermissions() {
        return permissions;
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public void copyTo(UserData dataHolder, Locale locale) throws RollerException {
        dataHolder.setEnabled(enabled);
        dataHolder.setId(id);
        dataHolder.setUserName(userName);
        dataHolder.setPassword(password);
        dataHolder.setFullName(fullName);
        dataHolder.setEmailAddress(emailAddress);
        dataHolder.setDateCreated(dateCreated);
        dataHolder.setLocale(this.locale);
        dataHolder.setTimeZone(timeZone);
    }

    public void copyFrom(UserData dataHolder, Locale locale) throws RollerException {
        enabled = dataHolder.getEnabled();
        id = dataHolder.getId();
        userName = dataHolder.getUserName();
        password = dataHolder.getPassword();
        fullName = dataHolder.getFullName();
        emailAddress = dataHolder.getEmailAddress();
        dateCreated = dataHolder.getDateCreated();
        this.locale = dataHolder.getLocale();
        timeZone = dataHolder.getTimeZone();
    }

    public void doReset(ActionMapping mapping, ServletRequest request) {
        enabled = null;
        id = null;
        userName = null;
        password = null;
        fullName = null;
        emailAddress = null;
        dateCreated = null;
        locale = null;
        timeZone = null;
    }

    public void reset(ActionMapping mapping, ServletRequest request) {
        doReset(mapping, request);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        doReset(mapping, request);
    }

    protected List permissions;

    protected Boolean enabled;

    protected String id;

    protected String userName;

    protected String password;

    protected String fullName;

    protected String emailAddress;

    protected Date dateCreated;

    protected String locale;

    protected String timeZone;

    protected Set roles;
}
