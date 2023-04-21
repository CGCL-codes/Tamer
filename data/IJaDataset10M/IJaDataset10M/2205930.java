package org.jaffa.applications.mylife.admin.components.userlookup.ui;

import org.jaffa.components.lookup.LookupComponent;
import java.util.*;
import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.middleware.Factory;
import org.jaffa.datatypes.*;
import org.jaffa.metadata.*;
import org.jaffa.components.finder.*;
import org.jaffa.components.maint.*;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.components.codehelper.ICodeHelper;
import org.jaffa.components.codehelper.dto.*;
import org.jaffa.applications.mylife.admin.components.userlookup.IUserLookup;
import org.jaffa.applications.mylife.admin.components.userlookup.dto.UserLookupInDto;
import org.jaffa.applications.mylife.admin.components.userlookup.dto.UserLookupOutDto;
import org.jaffa.applications.mylife.admin.domain.UserMeta;
import org.jaffa.applications.mylife.admin.components.usermaintenance.ui.UserMaintenanceComponent;
import org.jaffa.applications.mylife.admin.components.userviewer.ui.UserViewerComponent;
import org.jaffa.applications.mylife.admin.components.usermaintenance.ui.UserMaintenanceComponent;
import org.jaffa.applications.mylife.admin.components.usermaintenance.ui.UserMaintenanceComponent;

/** The controller for the UserLookup.
 */
public class UserLookupComponent extends LookupComponent {

    private static Logger log = Logger.getLogger(UserLookupComponent.class);

    private String m_userName = null;

    private String m_userNameDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_firstName = null;

    private String m_firstNameDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_lastName = null;

    private String m_lastNameDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_password = null;

    private String m_passwordDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_status = null;

    private String m_statusDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_eMailAddress = null;

    private String m_eMailAddressDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_createdOn = null;

    private String m_createdOnDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_createdBy = null;

    private String m_createdByDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_lastUpdatedOn = null;

    private String m_lastUpdatedOnDd = CriteriaField.RELATIONAL_EQUALS;

    private String m_lastUpdatedBy = null;

    private String m_lastUpdatedByDd = CriteriaField.RELATIONAL_EQUALS;

    private UserLookupOutDto m_outputDto = null;

    private IUserLookup m_tx = null;

    private UserMaintenanceComponent m_createComponent = null;

    private ICreateListener m_createListener = null;

    private UserMaintenanceComponent m_updateComponent = null;

    private IUpdateListener m_updateListener = null;

    private UserMaintenanceComponent m_deleteComponent = null;

    private IDeleteListener m_deleteListener = null;

    public UserLookupComponent() {
        super();
        super.setSortDropDown("UserName");
    }

    /** This should be invoked when done with the component.
     */
    public void quit() {
        if (m_tx != null) {
            m_tx.destroy();
            m_tx = null;
        }
        if (m_createComponent != null) {
            m_createComponent.quit();
            m_createComponent = null;
        }
        m_createListener = null;
        if (m_updateComponent != null) {
            m_updateComponent.quit();
            m_updateComponent = null;
        }
        m_updateListener = null;
        if (m_deleteComponent != null) {
            m_deleteComponent.quit();
            m_deleteComponent = null;
        }
        m_deleteListener = null;
        m_outputDto = null;
        super.quit();
    }

    /** Getter for property userName.
     * @return Value of property userName.
     */
    public String getUserName() {
        return m_userName;
    }

    /** Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(String userName) {
        m_userName = userName;
    }

    /** Getter for property userNameDd.
     * @return Value of property userNameDd.
     */
    public String getUserNameDd() {
        return m_userNameDd;
    }

    /** Setter for property userNameDd.
     * @param userNameDd New value of property userNameDd.
     */
    public void setUserNameDd(String userNameDd) {
        m_userNameDd = userNameDd;
    }

    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public String getFirstName() {
        return m_firstName;
    }

    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(String firstName) {
        m_firstName = firstName;
    }

    /** Getter for property firstNameDd.
     * @return Value of property firstNameDd.
     */
    public String getFirstNameDd() {
        return m_firstNameDd;
    }

    /** Setter for property firstNameDd.
     * @param firstNameDd New value of property firstNameDd.
     */
    public void setFirstNameDd(String firstNameDd) {
        m_firstNameDd = firstNameDd;
    }

    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public String getLastName() {
        return m_lastName;
    }

    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(String lastName) {
        m_lastName = lastName;
    }

    /** Getter for property lastNameDd.
     * @return Value of property lastNameDd.
     */
    public String getLastNameDd() {
        return m_lastNameDd;
    }

    /** Setter for property lastNameDd.
     * @param lastNameDd New value of property lastNameDd.
     */
    public void setLastNameDd(String lastNameDd) {
        m_lastNameDd = lastNameDd;
    }

    /** Getter for property password.
     * @return Value of property password.
     */
    public String getPassword() {
        return m_password;
    }

    /** Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(String password) {
        m_password = password;
    }

    /** Getter for property passwordDd.
     * @return Value of property passwordDd.
     */
    public String getPasswordDd() {
        return m_passwordDd;
    }

    /** Setter for property passwordDd.
     * @param passwordDd New value of property passwordDd.
     */
    public void setPasswordDd(String passwordDd) {
        m_passwordDd = passwordDd;
    }

    /** Getter for property status.
     * @return Value of property status.
     */
    public String getStatus() {
        return m_status;
    }

    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(String status) {
        m_status = status;
    }

    /** Getter for property statusDd.
     * @return Value of property statusDd.
     */
    public String getStatusDd() {
        return m_statusDd;
    }

    /** Setter for property statusDd.
     * @param statusDd New value of property statusDd.
     */
    public void setStatusDd(String statusDd) {
        m_statusDd = statusDd;
    }

    /** Getter for property eMailAddress.
     * @return Value of property eMailAddress.
     */
    public String getEMailAddress() {
        return m_eMailAddress;
    }

    /** Setter for property eMailAddress.
     * @param eMailAddress New value of property eMailAddress.
     */
    public void setEMailAddress(String eMailAddress) {
        m_eMailAddress = eMailAddress;
    }

    /** Getter for property eMailAddressDd.
     * @return Value of property eMailAddressDd.
     */
    public String getEMailAddressDd() {
        return m_eMailAddressDd;
    }

    /** Setter for property eMailAddressDd.
     * @param eMailAddressDd New value of property eMailAddressDd.
     */
    public void setEMailAddressDd(String eMailAddressDd) {
        m_eMailAddressDd = eMailAddressDd;
    }

    /** Getter for property createdOn.
     * @return Value of property createdOn.
     */
    public String getCreatedOn() {
        return m_createdOn;
    }

    /** Setter for property createdOn.
     * @param createdOn New value of property createdOn.
     */
    public void setCreatedOn(String createdOn) {
        m_createdOn = createdOn;
    }

    /** Getter for property createdOnDd.
     * @return Value of property createdOnDd.
     */
    public String getCreatedOnDd() {
        return m_createdOnDd;
    }

    /** Setter for property createdOnDd.
     * @param createdOnDd New value of property createdOnDd.
     */
    public void setCreatedOnDd(String createdOnDd) {
        m_createdOnDd = createdOnDd;
    }

    /** Getter for property createdBy.
     * @return Value of property createdBy.
     */
    public String getCreatedBy() {
        return m_createdBy;
    }

    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(String createdBy) {
        m_createdBy = createdBy;
    }

    /** Getter for property createdByDd.
     * @return Value of property createdByDd.
     */
    public String getCreatedByDd() {
        return m_createdByDd;
    }

    /** Setter for property createdByDd.
     * @param createdByDd New value of property createdByDd.
     */
    public void setCreatedByDd(String createdByDd) {
        m_createdByDd = createdByDd;
    }

    /** Getter for property lastUpdatedOn.
     * @return Value of property lastUpdatedOn.
     */
    public String getLastUpdatedOn() {
        return m_lastUpdatedOn;
    }

    /** Setter for property lastUpdatedOn.
     * @param lastUpdatedOn New value of property lastUpdatedOn.
     */
    public void setLastUpdatedOn(String lastUpdatedOn) {
        m_lastUpdatedOn = lastUpdatedOn;
    }

    /** Getter for property lastUpdatedOnDd.
     * @return Value of property lastUpdatedOnDd.
     */
    public String getLastUpdatedOnDd() {
        return m_lastUpdatedOnDd;
    }

    /** Setter for property lastUpdatedOnDd.
     * @param lastUpdatedOnDd New value of property lastUpdatedOnDd.
     */
    public void setLastUpdatedOnDd(String lastUpdatedOnDd) {
        m_lastUpdatedOnDd = lastUpdatedOnDd;
    }

    /** Getter for property lastUpdatedBy.
     * @return Value of property lastUpdatedBy.
     */
    public String getLastUpdatedBy() {
        return m_lastUpdatedBy;
    }

    /** Setter for property lastUpdatedBy.
     * @param lastUpdatedBy New value of property lastUpdatedBy.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        m_lastUpdatedBy = lastUpdatedBy;
    }

    /** Getter for property lastUpdatedByDd.
     * @return Value of property lastUpdatedByDd.
     */
    public String getLastUpdatedByDd() {
        return m_lastUpdatedByDd;
    }

    /** Setter for property lastUpdatedByDd.
     * @param lastUpdatedByDd New value of property lastUpdatedByDd.
     */
    public void setLastUpdatedByDd(String lastUpdatedByDd) {
        m_lastUpdatedByDd = lastUpdatedByDd;
    }

    /** Getter for property outputDto.
     * @return Value of property outputDto.
     */
    public UserLookupOutDto getUserLookupOutDto() {
        return m_outputDto;
    }

    /** If the displayResultsScreen property has not been set or has been set to false, it will return the FormKey for the Criteria screen.
     * If the displayResultsScreen property has been set to true, then a Search will be performed & the FormKey for the Results screen will be returned.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Criteria screen.
     */
    public FormKey display() throws ApplicationExceptions, FrameworkException {
        if (getDisplayResultsScreen() != null && getDisplayResultsScreen().booleanValue()) return displayResults(); else return displayCriteria();
    }

    /** Returns the FormKey for the Criteria screen.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Criteria screen.
     */
    public FormKey displayCriteria() throws ApplicationExceptions, FrameworkException {
        initCriteriaScreen();
        return new FormKey(UserLookupCriteriaForm.NAME, getComponentId());
    }

    /** Performs a search and returns the FormKey for the Results screen.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Results screen.
     */
    public FormKey displayResults() throws ApplicationExceptions, FrameworkException {
        doInquiry();
        return new FormKey(UserLookupResultsForm.NAME, getComponentId());
    }

    private void doInquiry() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        UserLookupInDto inputDto = new UserLookupInDto();
        inputDto.setMaxRecords(getMaxRecords());
        if (getUserName() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getUserNameDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getUserNameDd())) inputDto.setUserName(StringCriteriaField.getStringCriteriaField(getUserNameDd(), getUserName(), null));
        if (getFirstName() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getFirstNameDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getFirstNameDd())) inputDto.setFirstName(StringCriteriaField.getStringCriteriaField(getFirstNameDd(), getFirstName(), null));
        if (getLastName() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getLastNameDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getLastNameDd())) inputDto.setLastName(StringCriteriaField.getStringCriteriaField(getLastNameDd(), getLastName(), null));
        if (getPassword() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getPasswordDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getPasswordDd())) inputDto.setPassword(StringCriteriaField.getStringCriteriaField(getPasswordDd(), getPassword(), null));
        if (getStatus() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getStatusDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getStatusDd())) inputDto.setStatus(StringCriteriaField.getStringCriteriaField(getStatusDd(), getStatus(), null));
        if (getEMailAddress() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getEMailAddressDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getEMailAddressDd())) inputDto.setEMailAddress(StringCriteriaField.getStringCriteriaField(getEMailAddressDd(), getEMailAddress(), null));
        try {
            if (getCreatedOn() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getCreatedOnDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getCreatedOnDd())) inputDto.setCreatedOn(DateTimeCriteriaField.getDateTimeCriteriaField(getCreatedOnDd(), getCreatedOn(), null));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(UserMeta.META_CREATED_ON.getLabelToken());
            appExps.add(e);
        }
        if (getCreatedBy() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getCreatedByDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getCreatedByDd())) inputDto.setCreatedBy(StringCriteriaField.getStringCriteriaField(getCreatedByDd(), getCreatedBy(), null));
        try {
            if (getLastUpdatedOn() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getLastUpdatedOnDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getLastUpdatedOnDd())) inputDto.setLastUpdatedOn(DateTimeCriteriaField.getDateTimeCriteriaField(getLastUpdatedOnDd(), getLastUpdatedOn(), null));
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            e.setField(UserMeta.META_LAST_UPDATED_ON.getLabelToken());
            appExps.add(e);
        }
        if (getLastUpdatedBy() != null || CriteriaField.RELATIONAL_IS_NULL.equals(getLastUpdatedByDd()) || CriteriaField.RELATIONAL_IS_NOT_NULL.equals(getLastUpdatedByDd())) inputDto.setLastUpdatedBy(StringCriteriaField.getStringCriteriaField(getLastUpdatedByDd(), getLastUpdatedBy(), null));
        if (appExps != null && appExps.size() > 0) throw appExps;
        inputDto.setHeaderDto(getHeaderDto());
        addSortCriteria(inputDto);
        if (m_tx == null) m_tx = (IUserLookup) Factory.createObject(IUserLookup.class);
        m_outputDto = m_tx.find(inputDto);
        invokeFinderListener();
    }

    /** Calls the Admin.UserMaintenance component for creating a new User object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createFromCriteria() throws ApplicationExceptions, FrameworkException {
        return createObject(new FormKey(UserLookupCriteriaForm.NAME, getComponentId()));
    }

    /** Calls the Admin.UserMaintenance component for creating a new User object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createFromResults() throws ApplicationExceptions, FrameworkException {
        return createObject(new FormKey(UserLookupResultsForm.NAME, getComponentId()));
    }

    /** Calls the Admin.UserMaintenance component for creating a new User object.
     * @param formKey The FormKey object for the screen invoking this method
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Create screen.
     */
    public FormKey createObject(FormKey formKey) throws ApplicationExceptions, FrameworkException {
        if (m_createComponent == null || !m_createComponent.isActive()) m_createComponent = (UserMaintenanceComponent) run("Admin.UserMaintenance");
        m_createComponent.setReturnToFormKey(formKey);
        if (m_outputDto != null) addListeners(m_createComponent);
        if (m_createComponent instanceof IMaintComponent) ((IMaintComponent) m_createComponent).setMode(IMaintComponent.MODE_CREATE);
        return m_createComponent.display();
    }

    private ICreateListener getCreateListener() {
        if (m_createListener == null) {
            m_createListener = new ICreateListener() {

                public void createDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Create", e);
                    }
                }
            };
        }
        return m_createListener;
    }

    /** Calls the Admin.UserViewer component for viewing the User object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the View screen.
     */
    public FormKey viewObject(String userName) throws ApplicationExceptions, FrameworkException {
        UserViewerComponent viewComponent = (UserViewerComponent) run("Admin.UserViewer");
        viewComponent.setReturnToFormKey(FormKey.getCloseBrowserFormKey());
        viewComponent.setUserName(userName);
        return viewComponent.display();
    }

    /** Calls the Admin.UserMaintenance component for updating the User object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Update screen.
     */
    public FormKey updateObject(String userName) throws ApplicationExceptions, FrameworkException {
        if (m_updateComponent == null || !m_updateComponent.isActive()) {
            m_updateComponent = (UserMaintenanceComponent) run("Admin.UserMaintenance");
            m_updateComponent.setReturnToFormKey(new FormKey(UserLookupResultsForm.NAME, getComponentId()));
            addListeners(m_updateComponent);
        }
        m_updateComponent.setUserName(userName);
        if (m_updateComponent instanceof IMaintComponent) ((IMaintComponent) m_updateComponent).setMode(IMaintComponent.MODE_UPDATE);
        return m_updateComponent.display();
    }

    private IUpdateListener getUpdateListener() {
        if (m_updateListener == null) {
            m_updateListener = new IUpdateListener() {

                public void updateDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Update", e);
                    }
                }
            };
        }
        return m_updateListener;
    }

    /** Calls the Admin.UserMaintenance component for deleting the User object.
     * @throws ApplicationExceptions This will be thrown in case any invalid data has been set.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey for the Delete screen.
     */
    public FormKey deleteObject(String userName) throws ApplicationExceptions, FrameworkException {
        if (m_deleteComponent == null || !m_deleteComponent.isActive()) {
            m_deleteComponent = (UserMaintenanceComponent) run("Admin.UserMaintenance");
            m_deleteComponent.setReturnToFormKey(new FormKey(UserLookupResultsForm.NAME, getComponentId()));
            addListeners(m_deleteComponent);
        }
        m_deleteComponent.setUserName(userName);
        if (m_deleteComponent instanceof IMaintComponent) ((IMaintComponent) m_deleteComponent).setMode(IMaintComponent.MODE_DELETE);
        return m_deleteComponent.display();
    }

    private IDeleteListener getDeleteListener() {
        if (m_deleteListener == null) {
            m_deleteListener = new IDeleteListener() {

                public void deleteDone(EventObject source) {
                    try {
                        doInquiry();
                    } catch (Exception e) {
                        log.warn("Error in refreshing the Results screen after the Delete", e);
                    }
                }
            };
        }
        return m_deleteListener;
    }

    private void addListeners(Component comp) {
        if (comp instanceof ICreateComponent) ((ICreateComponent) comp).addCreateListener(getCreateListener());
        if (comp instanceof IUpdateComponent) ((IUpdateComponent) comp).addUpdateListener(getUpdateListener());
        if (comp instanceof IDeleteComponent) ((IDeleteComponent) comp).addDeleteListener(getDeleteListener());
    }

    /** This will retrieve the set of codes for dropdowns, if any are required
     */
    private void initCriteriaScreen() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        CodeHelperInDto input = null;
    }
}
