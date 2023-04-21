package com.patientis.client.forms;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.ControlScriptFactory;
import com.patientis.framework.scripting.IControlScript;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.JavaScript;
import com.patientis.framework.scripting.References;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.framework.utility.ClipboardUtil;
import com.patientis.framework.controls.IComponent;
import com.patientis.framework.controls.IControlPanel;
import com.patientis.framework.controls.IFormDisplay;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.model.billing.ChargeItemModel;
import com.patientis.model.billing.ChargeModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.patient.ViewPatientModel;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.ControlValueActionReference;
import com.patientis.model.reference.FormGroupReference;
import com.patientis.model.reference.FormTypeReference;
import com.patientis.model.reference.RefModel;
import com.patientis.model.reference.TermCategoryReference;
import com.patientis.model.reference.ValueDataTypeReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.security.ApplicationControlValueModel;
import com.patientis.model.security.ApplicationViewModel;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.common.WizardController;
import com.patientis.client.common.WizardScreen;
import com.patientis.client.service.patient.PatientService;
import com.patientis.client.service.security.SecurityService;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordDetailModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.clinical.FormTypeScreenModel;
import com.patientis.model.clinical.RecordItemModel;
import com.patientis.model.clinical.ResultMissingPatientException;
import com.patientis.client.service.billing.BillingService;
import com.patientis.client.service.clinical.ClinicalService;

/**
 * Patient FormModel primary function is as a mediator of actionable messages.
 * 
 */
public class PatientWizardFormController extends BaseController {

    /**
	 * Last selected
	 */
    private FormModel original = new FormModel();

    /**
	 * 
	 */
    private PatientModel patient = null;

    /**
	 * 
	 */
    private PatientModel originalPatient = null;

    /**
	 * 
	 */
    private FormTypeModel formType = null;

    /**
	 * 
	 */
    private boolean savePatient = false;

    /**
	 * 
	 */
    private ISControlPanel controlPanel = null;

    /**
	 * 
	 */
    private ISPanel parentPanel = null;

    /**
	  * Rebuild pane
	  */
    private WizardController wizard = null;

    /**
	 * Use getInstance()
	 */
    private PatientWizardFormController() {
    }

    /**
	 * Get a new instance of the controller
	 * 
	 * @return
	 */
    public static PatientWizardFormController getInstance() {
        PatientWizardFormController controller = new PatientWizardFormController();
        controller.setDefaultBaseModel(new FormModel());
        return controller;
    }

    /**
	 * Start the application  
	 * 
	 * @throws Exception
	 */
    public void start(Component parentFrameOrDialog, FormModel existing, PatientModel patient, FormTypeModel formType) throws Exception {
        this.formType = formType;
        if (formType.getFormGroupRef().isId(FormGroupReference.REGISTRATIONFORMS.getRefId()) || formType.isPatientModelUpdate()) {
            this.originalPatient = patient;
            savePatient = true;
        }
        this.patient = patient;
        getFormModel().copyAllFrom(existing);
        getFormModel().resetModified();
        for (FormRecordModel rec : getFormModel().getRecords()) {
            rec.resetModified();
        }
        getFormModel().setPatientId(patient.getId());
        getFormModel().addQueryModel(patient);
        original = existing;
        setControllerPatient(patient);
        wizard = WizardController.getInstance(this, getFormModel());
        List<WizardScreen> screens = new ArrayList<WizardScreen>();
        for (final FormTypeScreenModel formScreen : formType.getSortedScreens()) {
            final WizardScreen screen = new WizardScreen();
            screen.setViewRefId((int) formScreen.getViewRef().getId());
            screen.setDisplay(SecurityService.getApplicationViewByRef(formScreen.getViewRef().getId()));
            screens.add(screen);
        }
        wizard.setScreens(screens);
        startDialog(ApplicationDialogReference.ORDERWIZARDDIALOG, parentFrameOrDialog, true);
        if (formType.getPreferredHeight() > 0 && formType.getPreferredWidth() > 0) {
            getDialogModel().setSize(new Dimension((int) formType.getPreferredWidth(), (int) formType.getPreferredHeight()));
        }
        getDialog().setTitle(formType.getFormTypeRef().getDisplay());
        wizard.setup();
        enableOkApplyCancelOnChange();
        setDisableOkUntilChanges(true);
        displayDialog();
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (display.getViewRefId() == ViewReference.EMPTYFORMTEMPLATE.getRefId()) {
            ISControlPanel controlPanel = new ISControlPanel(wizard.getWizardPanel());
            return controlPanel;
        } else if (Converter.isSame(display.getModelClassName(), PatientModel.class.getName())) {
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
	 * FormModel specific message mediation
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
                                cancelCloseForm();
                                return true;
                            case SYSTEMREMOVE:
                                removeSelectedTableRows(action.getContextRefId());
                                return true;
                            case SYSTEMADD:
                                addTableRow(action.getContextRefId());
                                enableApplyOk();
                                return true;
                            default:
                                if (wizard.receive(event, value)) {
                                    return true;
                                }
                                return false;
                        }
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
                return "FormModelBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Get the selected order
	 * 
	 * @return
	 */
    public FormModel getFormModel() {
        return (FormModel) getDefaultBaseModel();
    }

    /**
	 * Save the FormModelVariable
	 * 
	 * @throws Exception
	 */
    private void save() throws Exception {
        for (FormRecordModel record : getFormModel().getRecords()) {
            RecordItemModel recordItem = ClinicalService.getRecordItemForRecordItemRefId(record.getRecordItemRef().getId());
            record.setRecordItemRef(recordItem.getRecordItemRef());
            if (record.getDataTypeRef().isNew()) {
                record.setDataTypeRef(recordItem.getDataTypeRef());
            }
            if (record.getRecordDt().isNull()) {
                record.setRecordDt(getFormModel().getFormDt());
            }
        }
        validateControls();
        if (formType.getFormGroupRef().isId(FormGroupReference.REGISTRATIONFORMS.getRefId())) {
        } else {
            if (patient.getId() == 0L || patient.hasNoVisit()) {
                throw new ResultMissingPatientException();
            }
        }
        for (FormRecordModel record : getFormModel().getRecords()) {
            record.setPatientId(patient.getId());
            record.setVisitId(patient.giveVisit().getId());
        }
        if (savePatient) {
            ClinicalService.store(patient, getFormModel());
        } else {
            ClinicalService.store(patient.getId(), patient.giveVisit().getId(), getFormModel());
        }
        original.copyAllFrom(getFormModel());
        okCloseForm();
        globalRefreshTable(ContextReference.PATIENTCHARTTABBEDPANE);
    }

    /**
	 * 
	 * @throws Exception
	 */
    private void debugReport() throws Exception {
        StringBuffer sb = new StringBuffer(1024 * 4);
        sb.append("<?xml version=\"1.0\"?>\n" + "<Report>\n   \n");
        ViewPatientModel patient = PatientService.getViewPatient(getFormModel().getPatientId());
        for (FormRecordModel record : getFormModel().getRecords()) {
            sb.append("<Detail>\n");
            sb.append(record.getReportXml());
            sb.append(patient.getReportXml());
            sb.append(getFormModel().getReportXml());
            sb.append("\n</Detail>\n");
        }
        sb.append("</Report>");
        Log.debug(sb.toString());
        ClipboardUtil.setClipboard(sb.toString());
    }

    /**
	 * Remove new records not filled out
	 */
    private void removeBlankRecords() {
        List<FormRecordModel> removeList = new ArrayList<FormRecordModel>();
        for (FormRecordModel record : getFormModel().getRecords()) {
            if (record.isNew()) {
                if (!record.isValueModified() && record.getDetails().size() == 0) {
                    removeList.add(record);
                }
            }
        }
        getFormModel().getRecords().removeAll(removeList);
    }

    /**
	 * TODO wizards should be moved into base controller so override code like this not needed
	 * 
	 * Get the value from the component with the specified context
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
    public IComponent getComponent(int contextRefId) throws Exception {
        IComponent component = super.getComponent(contextRefId);
        if (component == null) {
            return wizard.getComponent(contextRefId);
        } else {
            return component;
        }
    }
}
