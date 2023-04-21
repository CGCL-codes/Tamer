package org.jaffa.applications.mylife.admin.components.changehistorymaintenance.ui;

import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.FormBase;
import javax.servlet.http.HttpServletRequest;
import org.jaffa.presentation.portlet.widgets.model.*;
import org.jaffa.presentation.portlet.widgets.controller.*;
import org.jaffa.metadata.*;
import org.jaffa.datatypes.*;
import java.util.*;
import org.jaffa.util.StringHelper;
import org.jaffa.components.codehelper.dto.*;
import org.jaffa.components.finder.*;
import org.jaffa.applications.mylife.admin.domain.ChangeHistoryMeta;
import org.jaffa.applications.mylife.admin.domain.ContentMeta;

/** The FormBean class to support the maintenance jsp of the ChangeHistoryMaintenance.
 */
public class ChangeHistoryMaintenanceForm extends FormBase {

    /** The name constant used for determining the corresponding jsp through the struts-config file.
     */
    public static final String NAME = "admin_changeHistoryMaintenance";

    private static Logger log = Logger.getLogger(ChangeHistoryMaintenanceForm.class);

    private DateTimeModel w_updatedOn = null;

    private EditBoxModel w_updatedBy = null;

    private EditBoxModel w_contentId = null;

    /** Is this update mode, if so the key is display only, else this is create mode and the key is editable.
     * @return true if record is being updated, false if created.
     */
    public boolean isUpdateMode() {
        return ((ChangeHistoryMaintenanceComponent) getComponent()).isUpdateMode();
    }

    /** This method is invoked by the FormTag. It gets the data from the component.
     */
    public void initForm() {
    }

    /** Getter for property updatedOn.
     * @return Value of property updatedOn.
     */
    public DateTime getUpdatedOn() {
        return ((ChangeHistoryMaintenanceComponent) getComponent()).getUpdatedOn();
    }

    /** Setter for property updatedOn.
     * @param updatedOn New value of property updatedOn.
     */
    public void setUpdatedOn(DateTime updatedOn) {
        ((ChangeHistoryMaintenanceComponent) getComponent()).setUpdatedOn(updatedOn);
    }

    /** Getter for property updatedOn. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property updatedOn.
     */
    public DateTimeModel getUpdatedOnWM() {
        if (w_updatedOn == null) {
            w_updatedOn = (DateTimeModel) getWidgetCache().getModel("updatedOn");
            if (w_updatedOn == null) {
                w_updatedOn = new DateTimeModel(getUpdatedOn(), (DateTimeFieldMetaData) ChangeHistoryMeta.META_UPDATED_ON);
                getWidgetCache().addModel("updatedOn", w_updatedOn);
            }
        }
        return w_updatedOn;
    }

    /** Setter for property updatedOn. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property updatedOn.
     */
    public void setUpdatedOnWV(String value) {
        DateTimeController.updateModel(value, getUpdatedOnWM());
    }

    /** Getter for property updatedBy.
     * @return Value of property updatedBy.
     */
    public String getUpdatedBy() {
        return ((ChangeHistoryMaintenanceComponent) getComponent()).getUpdatedBy();
    }

    /** Setter for property updatedBy.
     * @param updatedBy New value of property updatedBy.
     */
    public void setUpdatedBy(String updatedBy) {
        ((ChangeHistoryMaintenanceComponent) getComponent()).setUpdatedBy(updatedBy);
    }

    /** Getter for property updatedBy. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property updatedBy.
     */
    public EditBoxModel getUpdatedByWM() {
        if (w_updatedBy == null) {
            w_updatedBy = (EditBoxModel) getWidgetCache().getModel("updatedBy");
            if (w_updatedBy == null) {
                if (getUpdatedBy() != null) w_updatedBy = new EditBoxModel(getUpdatedBy(), ChangeHistoryMeta.META_UPDATED_BY); else w_updatedBy = new EditBoxModel(ChangeHistoryMeta.META_UPDATED_BY);
                getWidgetCache().addModel("updatedBy", w_updatedBy);
            }
        }
        return w_updatedBy;
    }

    /** Setter for property updatedBy. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property updatedBy.
     */
    public void setUpdatedByWV(String value) {
        EditBoxController.updateModel(value, getUpdatedByWM());
    }

    /** Getter for property contentId.
     * @return Value of property contentId.
     */
    public String getContentId() {
        return ((ChangeHistoryMaintenanceComponent) getComponent()).getContentId();
    }

    /** Setter for property contentId.
     * @param contentId New value of property contentId.
     */
    public void setContentId(String contentId) {
        ((ChangeHistoryMaintenanceComponent) getComponent()).setContentId(contentId);
    }

    /** Getter for property contentId. This is invoked by the custom tag, when the jsp is rendered, to get the current value.
     * @return Value of property contentId.
     */
    public EditBoxModel getContentIdWM() {
        if (w_contentId == null) {
            w_contentId = (EditBoxModel) getWidgetCache().getModel("contentId");
            if (w_contentId == null) {
                if (getContentId() != null) w_contentId = new EditBoxModel(getContentId(), ContentMeta.META_CONTENT_ID); else w_contentId = new EditBoxModel(ContentMeta.META_CONTENT_ID);
                getWidgetCache().addModel("contentId", w_contentId);
            }
        }
        return w_contentId;
    }

    /** Setter for property contentId. This is invoked by the servlet, when a post is done on the Criteria screen.
     * @param value New value of property contentId.
     */
    public void setContentIdWV(String value) {
        EditBoxController.updateModel(value, getContentIdWM());
    }

    /** This method should be invoked to ensure a valid state of the FormBean. It will validate the data in the models and set the corresponding properties.
     * Errors will be raised in the FormBean, if any validation fails.
     * @param request The request stream
     * @return A true indicates validations went through successfully. */
    public boolean doValidate(HttpServletRequest request) {
        boolean valid = true;
        try {
            boolean checkMandatory = false;
            DateTime value = getUpdatedOnWM().getValue();
            value = FieldValidator.validate(value, (DateTimeFieldMetaData) ChangeHistoryMeta.META_UPDATED_ON, checkMandatory);
            setUpdatedOn(value);
        } catch (ValidationException e) {
            valid = false;
            raiseError(request, ChangeHistoryMeta.META_UPDATED_ON.getLabelToken(), e);
        }
        try {
            boolean checkMandatory = false;
            String htmlString = getUpdatedByWM().getValue();
            if (htmlString != null && htmlString.length() == 0) htmlString = null;
            String value = (String) FieldValidator.validateData(htmlString, (StringFieldMetaData) ChangeHistoryMeta.META_UPDATED_BY, checkMandatory);
            setUpdatedBy(value);
        } catch (ValidationException e) {
            valid = false;
            raiseError(request, ChangeHistoryMeta.META_UPDATED_BY.getLabelToken(), e);
        }
        try {
            boolean checkMandatory = true;
            String htmlString = getContentIdWM().getValue();
            if (htmlString != null && htmlString.length() == 0) htmlString = null;
            String value = (String) FieldValidator.validateData(htmlString, (StringFieldMetaData) ContentMeta.META_CONTENT_ID, checkMandatory);
            setContentId(value);
        } catch (ValidationException e) {
            valid = false;
            raiseError(request, ContentMeta.META_CONTENT_ID.getLabelToken(), e);
        }
        return valid;
    }
}
