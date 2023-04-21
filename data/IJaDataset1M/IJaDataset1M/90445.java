package com.patientis.client.reference;

import java.awt.BorderLayout;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.common.DynamicForm;
import com.patientis.client.service.reference.ReferenceService;
import com.patientis.framework.controls.IControlPanel;
import com.patientis.framework.controls.IFormDisplay;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.client.common.PromptsController;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.model.common.DefaultBaseModel;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.AccessReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.security.AuditModel;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.security.SecurityService;

/**
 * Audit Viewer primary function is as a mediator of actionable messages.
 * 
 */
public class AuditViewerController extends BaseController {

    /**
	 * Last selected
	 */
    private AuditModel selectedAudit = new AuditModel();

    /**
	 * Dynamic
	 */
    private ISPanel formPanel = new ISPanel(new BorderLayout());

    /**
	 * Use getInstance()
	 */
    private AuditViewerController() {
    }

    /**
	 * Get a new instance of the controller
	 * 
	 * @return
	 */
    public static AuditViewerController getInstance() {
        AuditViewerController controller = new AuditViewerController();
        AuditModel AuditModelVariable = new AuditModel();
        controller.setDefaultBaseModel(AuditModelVariable);
        return controller;
    }

    /**
	 * Start the application  
	 * 
	 * @throws Exception
	 */
    public void start() throws Exception {
        startFrame(ApplicationDialogReference.AUDITVIEWERDIALOG);
        loadAccessListResultsByRef(AccessReference.AUDITMODELLIST.getRefId());
        enableOkApplyCancelOnChange();
        setDisableOkUntilChanges(true);
        registerGlobalRefresh(ContextReference.AUDITVIEWERAUDITMODELLISTTABLE);
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (display.getViewRefId() == ViewReference.AUDITVIEWERLISTPANEL.getRefId()) {
            return new ISControlPanel(getFormMediator(), new DefaultBaseModel(), display, getFrame());
        } else if (display.getViewRefId() == ViewReference.EMPTYFORMTEMPLATE.getRefId()) {
            return new ISControlPanel(formPanel);
        } else {
            return super.getControlPanel(viewId);
        }
    }

    /**
	 * Form specific message mediation
	 */
    @Override
    protected void mediateMessages() {
        getFormMediator().register(new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case REFRESHVIEW:
                                if (action.hasContext(ContextReference.AUDITVIEWERAUDITMODELLISTTABLE)) {
                                    loadAccessListResultsByRef(AccessReference.AUDITMODELLIST.getRefId());
                                }
                                return false;
                            case CANCELSUBMITFORM:
                                cancelCloseForm();
                                return true;
                            case CANCELFORMCHANGES:
                                clearAudit(new AuditModel());
                                refreshAllTables();
                                return true;
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(ContextReference.AUDITVIEWERAUDITMODELLISTTABLE)) {
                                    selectedAudit = tableAction.hasSelectedRows() ? (AuditModel) tableAction.getSelectedModel() : new AuditModel();
                                    clearAudit(selectedAudit);
                                    disableOkApplyCancelOnChange();
                                    return false;
                                }
                        }
                }
                return false;
            }

            public String toString() {
                return "AuditBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Get the selected order
	 * 
	 * @return
	 */
    private AuditModel getAudit() {
        return (AuditModel) getDefaultBaseModel();
    }

    /**
	 * Clear the order and replaced with selected order
	 */
    private void clearAudit(AuditModel selectedAudit) throws Exception {
        getAudit().copyAllFrom(selectedAudit);
        disableOkApplyCancelOnChange();
    }

    /**
	 * Add a slot
	 * 
	 * @param action
	 * @return
	 * @throws Exception
	 */
    private boolean addAudit() throws Exception {
        AuditModel newAudit = new AuditModel();
        clearAudit(newAudit);
        enableApplyOk();
        return true;
    }
}
