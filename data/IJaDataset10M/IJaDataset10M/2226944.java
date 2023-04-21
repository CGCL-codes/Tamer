package org.jaffa.applications.mylife.admin.components.contentmaintenance.ui;

import org.apache.log4j.Logger;
import org.jaffa.components.maint.MaintComponent;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.middleware.Factory;
import org.jaffa.components.dto.HeaderDto;
import org.jaffa.util.BeanHelper;
import org.jaffa.components.codehelper.ICodeHelper;
import org.jaffa.components.codehelper.dto.*;
import org.jaffa.components.finder.*;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.applications.mylife.admin.components.contentmaintenance.IContentMaintenance;
import org.jaffa.applications.mylife.admin.components.contentmaintenance.dto.*;

/** The controller for the ContentMaintenance.
 */
public class ContentMaintenanceComponent extends MaintComponent {

    private static Logger log = Logger.getLogger(ContentMaintenanceComponent.class);

    private HeaderDto m_headerDto = null;

    private IContentMaintenance m_tx = null;

    private String m_contentId = null;

    private String m_type = null;

    private org.jaffa.datatypes.DateTime m_createdOn = null;

    private String m_createdBy = null;

    private org.jaffa.datatypes.DateOnly m_eventDate = null;

    private String m_author = null;

    private String m_body = null;

    /** This should be invoked when done with the component.
     */
    public void quit() {
        if (m_tx != null) {
            m_tx.destroy();
            m_tx = null;
        }
        super.quit();
    }

    /** Getter for property contentId.
     * @return Value of property contentId.
     */
    public String getContentId() {
        return m_contentId;
    }

    /** Setter for property contentId.
     * @param contentId New value of property contentId.
     */
    public void setContentId(String contentId) {
        m_contentId = contentId;
    }

    /** Getter for property type.
     * @return Value of property type.
     */
    public String getType() {
        return m_type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     */
    public void setType(String type) {
        m_type = type;
    }

    /** Getter for property createdOn.
     * @return Value of property createdOn.
     */
    public org.jaffa.datatypes.DateTime getCreatedOn() {
        return m_createdOn;
    }

    /** Setter for property createdOn.
     * @param createdOn New value of property createdOn.
     */
    public void setCreatedOn(org.jaffa.datatypes.DateTime createdOn) {
        m_createdOn = createdOn;
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

    /** Getter for property eventDate.
     * @return Value of property eventDate.
     */
    public org.jaffa.datatypes.DateOnly getEventDate() {
        return m_eventDate;
    }

    /** Setter for property eventDate.
     * @param eventDate New value of property eventDate.
     */
    public void setEventDate(org.jaffa.datatypes.DateOnly eventDate) {
        m_eventDate = eventDate;
    }

    /** Getter for property author.
     * @return Value of property author.
     */
    public String getAuthor() {
        return m_author;
    }

    /** Setter for property author.
     * @param author New value of property author.
     */
    public void setAuthor(String author) {
        m_author = author;
    }

    /** Getter for property body.
     * @return Value of property body.
     */
    public String getBody() {
        return m_body;
    }

    /** Setter for property body.
     * @param body New value of property body.
     */
    public void setBody(String body) {
        m_body = body;
    }

    /** Based on the mode and input parameters, this will either delete the domain object, or initialize the screen for updates, or bring up a blank screen.
     * @throws ApplicationExceptions Indicates some functional error.
     * @throws FrameworkException Indicates some system error.
     * @return The FormKey.
     */
    public FormKey display() throws ApplicationExceptions, FrameworkException {
        if (isDeleteMode()) {
            delete();
        } else if (isUpdateMode()) {
            initDropDownCodes();
            retrieve();
        } else {
            initDropDownCodes();
            initializeData();
        }
        if (isDeleteMode()) return getReturnToFormKey(); else return new FormKey(ContentMaintenanceForm.NAME, getComponentId());
    }

    /** This will invoke the create method on the transaction to create a new domain object.
     * @throws ApplicationExceptions Indicates some functional error.
     * @throws FrameworkException Indicates some system error.
     */
    public void create() throws ApplicationExceptions, FrameworkException {
        ContentMaintenanceCreateInDto input = new ContentMaintenanceCreateInDto();
        input.setHeaderDto(createHeaderDto());
        input.setContentId(getContentId());
        input.setType(getType());
        input.setCreatedOn(getCreatedOn());
        input.setCreatedBy(getCreatedBy());
        input.setEventDate(getEventDate());
        input.setAuthor(getAuthor());
        input.setBody(getBody());
        createTx().create(input);
        invokeCreateListeners();
    }

    /** This will invoke the update method on the transaction to update an existing domain object.
     * @throws ApplicationExceptions Indicates some functional error.
     * @throws FrameworkException Indicates some system error.
     */
    public void update() throws ApplicationExceptions, FrameworkException {
        ContentMaintenanceUpdateInDto input = new ContentMaintenanceUpdateInDto();
        input.setHeaderDto(createHeaderDto());
        input.setContentId(getContentId());
        input.setType(getType());
        input.setCreatedOn(getCreatedOn());
        input.setCreatedBy(getCreatedBy());
        input.setEventDate(getEventDate());
        input.setAuthor(getAuthor());
        input.setBody(getBody());
        createTx().update(input);
        invokeUpdateListeners();
    }

    /** This will invoke the delete method on the transaction to delete an existing domain object.
     * @throws ApplicationExceptions Indicates some functional error.
     * @throws FrameworkException Indicates some system error.
     */
    public void delete() throws ApplicationExceptions, FrameworkException {
        ContentMaintenanceDeleteInDto input = new ContentMaintenanceDeleteInDto();
        input.setHeaderDto(createHeaderDto());
        input.setContentId(getContentId());
        createTx().delete(input);
        invokeDeleteListeners();
    }

    /** This will invoke the retrieve method on the transaction to retrieve an existing domain object.
     * @throws ApplicationExceptions Indicates some functional error.
     * @throws FrameworkException Indicates some system error.
     */
    public void retrieve() throws ApplicationExceptions, FrameworkException {
        getUserSession().getWidgetCache(getComponentId()).clear();
        ContentMaintenanceRetrieveInDto input = new ContentMaintenanceRetrieveInDto();
        input.setHeaderDto(createHeaderDto());
        input.setContentId(getContentId());
        ContentMaintenanceRetrieveOutDto output = createTx().retrieve(input);
        if (output != null) {
            setContentId(output.getContentId());
            setType(output.getType());
            setCreatedOn(output.getCreatedOn());
            setCreatedBy(output.getCreatedBy());
            setEventDate(output.getEventDate());
            setAuthor(output.getAuthor());
            setBody(output.getBody());
        }
    }

    public void initializeData() {
    }

    private IContentMaintenance createTx() throws FrameworkException {
        if (m_tx == null) m_tx = (IContentMaintenance) Factory.createObject(IContentMaintenance.class);
        return m_tx;
    }

    private HeaderDto createHeaderDto() {
        if (m_headerDto == null) {
            m_headerDto = new HeaderDto();
            m_headerDto.setUserId(getUserSession().getUserId());
        }
        return m_headerDto;
    }

    /** This will retrieve the set of codes for dropdowns, if any are required
     */
    private void initDropDownCodes() throws ApplicationExceptions, FrameworkException {
        ApplicationExceptions appExps = null;
        CodeHelperInDto input = null;
    }
}
