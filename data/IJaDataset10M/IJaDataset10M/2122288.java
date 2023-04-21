package com.patientis.client.billing;

import java.awt.BorderLayout;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.common.DynamicForm;
import com.patientis.client.service.billing.BillingService;
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
import com.patientis.model.billing.InvoiceFormatModel;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.security.SecurityService;

/**
 * Invoice Format Builder primary function is as a mediator of actionable messages.
 * 
 */
public class InvoiceFormatBuilderController extends BaseController {

    /**
	 * Last selected
	 */
    private InvoiceFormatModel selectedInvoiceFormat = new InvoiceFormatModel();

    /**
	 * Dynamic
	 */
    private ISPanel formPanel = new ISPanel(new BorderLayout());

    /**
	 * Use getInstance()
	 */
    private InvoiceFormatBuilderController() {
    }

    /**
	 * Get a new instance of the controller
	 * 
	 * @return
	 */
    public static InvoiceFormatBuilderController getInstance() {
        InvoiceFormatBuilderController controller = new InvoiceFormatBuilderController();
        InvoiceFormatModel InvoiceFormatModelVariable = new InvoiceFormatModel();
        controller.setDefaultBaseModel(InvoiceFormatModelVariable);
        return controller;
    }

    /**
	 * Start the application  
	 * 
	 * @throws Exception
	 */
    public void start() throws Exception {
        startFrame(ApplicationDialogReference.INVOICEFORMATBUILDERDIALOG);
        loadAccessListResultsByRef(AccessReference.INVOICEFORMATMODELLIST.getRefId());
        enableOkApplyCancelOnChange();
        setDisableOkUntilChanges(true);
        registerGlobalRefresh(ContextReference.INVOICEFORMATBUILDERINVOICEFORMATMODELLISTTABLE);
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (display.getViewRefId() == ViewReference.INVOICEFORMATBUILDERLISTPANEL.getRefId()) {
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
                                if (action.hasContext(ContextReference.INVOICEFORMATBUILDERINVOICEFORMATMODELLISTTABLE)) {
                                    loadAccessListResultsByRef(AccessReference.INVOICEFORMATMODELLIST.getRefId());
                                }
                                return false;
                            case CANCELSUBMITFORM:
                                cancelCloseForm();
                                return true;
                            case APPLYFORMCHANGES:
                                save();
                                return true;
                            case CANCELFORMCHANGES:
                                clearInvoiceFormat(new InvoiceFormatModel());
                                refreshAllTables();
                                return true;
                            case SYSTEMADD:
                                if (action.hasContext(ContextReference.INVOICEFORMATBUILDERINVOICEFORMATMODELLISTTABLE)) {
                                    return addInvoiceFormat();
                                }
                                return false;
                            case SYSTEMREMOVE:
                                if (action.hasContext(ContextReference.INVOICEFORMATBUILDERINVOICEFORMATMODELLISTTABLE)) {
                                    return removeInvoiceFormat();
                                } else {
                                    removeSelectedTableRows(action.getContextRefId());
                                    enableApplyOk();
                                    return true;
                                }
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(ContextReference.INVOICEFORMATBUILDERINVOICEFORMATMODELLISTTABLE)) {
                                    selectedInvoiceFormat = tableAction.hasSelectedRows() ? (InvoiceFormatModel) tableAction.getSelectedModel() : new InvoiceFormatModel();
                                    clearInvoiceFormat(selectedInvoiceFormat);
                                    disableOkApplyCancelOnChange();
                                    return false;
                                }
                        }
                }
                return false;
            }

            public String toString() {
                return "InvoiceFormatBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Get the selected order
	 * 
	 * @return
	 */
    private InvoiceFormatModel getInvoiceFormat() {
        return (InvoiceFormatModel) getDefaultBaseModel();
    }

    /**
	 * Clear the order and replaced with selected order
	 */
    private void clearInvoiceFormat(InvoiceFormatModel selectedInvoiceFormat) throws Exception {
        getInvoiceFormat().copyAllFrom(selectedInvoiceFormat);
        disableOkApplyCancelOnChange();
    }

    /**
	 * Save the InvoiceFormatModelVariable
	 * 
	 * @throws Exception
	 */
    private void save() throws Exception {
        validateControls();
        BillingService.store(getInvoiceFormat());
        loadAccessListResultsByRef(AccessReference.INVOICEFORMATMODELLIST.getRefId());
        clearInvoiceFormat(new InvoiceFormatModel());
        disableOkApplyCancelOnChange();
    }

    /**
	 * Add a slot
	 * 
	 * @param action
	 * @return
	 * @throws Exception
	 */
    private boolean addInvoiceFormat() throws Exception {
        InvoiceFormatModel newInvoiceFormat = new InvoiceFormatModel();
        clearInvoiceFormat(newInvoiceFormat);
        enableApplyOk();
        return true;
    }

    /**
	 * Remove the selected appointment type
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean removeInvoiceFormat() throws Exception {
        if (selectedInvoiceFormat.isNotNew()) {
            if (PromptsController.questionIsOKCancelOK(getFrame(), "Remove " + selectedInvoiceFormat.getDisplayListText(), "Remove")) {
                selectedInvoiceFormat.setDeleted();
                BillingService.store(selectedInvoiceFormat);
                loadAccessListResultsByRef(AccessReference.INVOICEFORMATMODELLIST.getRefId());
            }
        }
        return false;
    }
}
