package com.patientis.client.framework;

import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.controls.IControlPanel;
import com.patientis.framework.controls.IFormDisplay;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.model.common.Converter;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.service.security.SecurityService;

/**
 * Patient mainEntity primary function is as a mediator of actionable messages.
 * 
 */
public class Template1PatientController extends BaseController {

    /**
	 * Use getInstance()
	 */
    private Template1PatientController() {
    }

    /**
	 * Get a new instance of the controller
	 * 
	 * @return
	 */
    public static Template1PatientController getInstance() {
        Template1PatientController controller = new Template1PatientController();
        return controller;
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (Converter.isSame(display.getModelClassName(), PatientModel.class.getName())) {
            if (controllerHasPatient()) {
                return new ISControlPanel(getFormMediator(), getControllerPatient(), display, getFrame());
            } else {
                return new ISControlPanel(new ISPanel());
            }
        } else {
            return super.getControlPanel(viewId);
        }
    }

    /**
	 * mainEntity specific message mediation
	 */
    @Override
    protected void mediateMessages() {
        getFormMediator().register(new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case OKSUBMITFORM:
                                save();
                                return true;
                            case CANCELSUBMITFORM:
                                return true;
                            case SYSTEMREMOVE:
                                removeSelectedTableRows(action.getContextRefId());
                                return true;
                            case SYSTEMADD:
                                addTableRow(action.getContextRefId());
                                enableApplyOk();
                                return true;
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case TABLEROWSELECTED:
                                return true;
                        }
                }
                return false;
            }

            public String toString() {
                return "mainEntityBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Save the mainEntityVariable
	 * 
	 * @throws Exception
	 */
    private void save() throws Exception {
        validateControls();
        saveControllerPatient();
    }
}
