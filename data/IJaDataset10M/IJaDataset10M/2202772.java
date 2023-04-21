package com.patientis.client.med;

import java.util.List;
import java.util.ArrayList;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.med.MedService;
import com.patientis.client.common.BaseController;
import com.patientis.client.service.security.SecurityService;
import com.patientis.framework.controls.IControlPanel;
import com.patientis.framework.controls.IFormDisplay;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.table.IFilterModel;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.common.DefaultBaseModel;
import com.patientis.model.common.BaseModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.IdentifierModel;
import com.patientis.model.common.ModelReference;
import com.patientis.model.med.MedIdentifierModel;
import com.patientis.model.med.MedModel;
import com.patientis.model.med.MedDoseCheckModel;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.reference.ViewControllerReference;

/**
 * RegistrationController is the MVC controller for the registration application.
 * 
 * Design Patterns: <a href="/functionality/rm/1000053.html">SingletonForm</a>
 * , <a href="/functionality/rm/1000052.html">BaseController</a>
 * , <a href="/functionality/rm/1000060.html">Clipboard</a>
 * 
 */
public class FormularyBuilderController extends BaseController {

    /**
	 * Selected medications
	 */
    private MedModel selectedMed = new MedModel();

    /**
	 * Selected medications
	 */
    private List<MedModel> selectedMeds = new ArrayList<MedModel>();

    /**
	 * Drug level checking by age last displayed
	 */
    private static boolean lastDLCShowAge = true;

    /**
	 * Singleton
	 */
    private FormularyBuilderController() {
    }

    /**
	 * Get the singleton
	 * 
	 * @return
	 */
    public static FormularyBuilderController getInstance() {
        FormularyBuilderController controller = new FormularyBuilderController();
        controller.setDefaultBaseModel(new MedModel());
        return controller;
    }

    /**
	 * Start the application with the patient model 
	 * 
	 * @param medication patient to display
	 * @throws Exception
	 */
    public void start() throws Exception {
        startFrame(ApplicationDialogReference.FORMULARYBUILDERDIALOG);
        List<MedModel> meds = MedService.getMatchingMeds("%", 50);
        replaceTableRows(ContextReference.FORMULARYSEARCHRESULTSTABLE, BaseModel.toTableRows(meds));
        enableOkApplyCancelOnChange();
        setDisableOkUntilChanges(true);
        showAgeHideWeightColumns(lastDLCShowAge);
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (display.getViewRefId() == ViewReference.FORMULARYBUILDERDETAILSFORM.getRefId()) {
            return new ISControlPanel(getFormMediator(), getMedication(), display, getFrame());
        } else if (display.getViewRefId() == ViewReference.FORMULARYLISTSEARCHRESULTS.getRefId()) {
            return new ISControlPanel(getFormMediator(), new DefaultBaseModel(), display, getFrame());
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
                            case CANCELSUBMITFORM:
                                cancelCloseForm();
                                return true;
                            case APPLYFORMCHANGES:
                                save();
                                showAgeHideWeightColumns(lastDLCShowAge);
                                disableOkApplyCancelOnChange();
                                return true;
                            case CANCELFORMCHANGES:
                                selectMedication(new MedModel());
                                refreshAllTablesExcept(ContextReference.FORMULARYSEARCHRESULTSTABLE);
                                reapplyOkApplyCancelOnChange();
                                disableOkApplyCancelOnChange();
                                return true;
                            case SYSTEMRUN:
                                runCommandScript(String.valueOf(action.getValue()), ViewControllerReference.FORMULARYBUILDERCONTROLLER, BaseModel.toIBaseModelList(selectedMeds));
                                return true;
                            case TABLEREMOVEFILTER:
                                removeFilter();
                                return true;
                            case FORMULARYBUILDERDRUGLEVELCHECKBYAGE:
                                return showAgeHideWeightColumns(true);
                            case FORMULARYBUILDERDRUGLEVELCHECKBYWEIGHT:
                                return showAgeHideWeightColumns(false);
                            case EXECUTESEARCH:
                                search(String.valueOf(action.getValue()));
                                return true;
                            case SYSTEMREMOVE:
                                removeSelectedTableRows(action.getContextRefId());
                                return true;
                            case SYSTEMADD:
                                enableApplyOk();
                                showAgeHideWeightColumns(lastDLCShowAge);
                                editLastRow(ContextReference.DOSERANGECHECKTABLE.getRefId());
                                return true;
                            case FORMULARYBUILDERNEWMEDICATION:
                                MedModel newMed = new MedModel();
                                newMed.getIdentifiers().add(new MedIdentifierModel());
                                selectMedication(newMed);
                                selectTab(0);
                                setFocusOn(ContextReference.FOCUSONADDNEW);
                                return true;
                            case SYSTEMEDITSETTINGS:
                                return editSettings();
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(ContextReference.FORMULARYSEARCHRESULTSTABLE)) {
                                    selectedMeds.clear();
                                    selectedMed = new MedModel();
                                    if (tableAction.hasSelectedRows()) {
                                        BaseModel.toList(tableAction.getSelectedModels(), selectedMeds);
                                        selectedMed = (MedModel) tableAction.getSelectedModel();
                                        selectedMed.copyAllFrom(MedService.getMed(selectedMed.getId()));
                                        selectMedication(selectedMed);
                                    }
                                }
                        }
                }
                return false;
            }

            public String toString() {
                return "FormularyBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * If showAge is true age columns are displayed and weight hidden
	 * TODO user selecting one option should default for additional meds
	 * 
	 * @param showAge
	 * @return
	 */
    private boolean showAgeHideWeightColumns(boolean showAge) throws Exception {
        if (showAge) {
            showTableColumns(ContextReference.DOSERANGECHECKTABLE, ModelReference.MEDDOSECHECKS_RANGEMINAGE, ModelReference.MEDDOSECHECKS_RANGEMINAGEUNITREFID, ModelReference.MEDDOSECHECKS_RANGEMAXAGE, ModelReference.MEDDOSECHECKS_RANGEMAXAGEUNITREFID);
            hideTableColumns(ContextReference.DOSERANGECHECKTABLE.getRefId(), ModelReference.MEDDOSECHECKS_RANGEMINWEIGHT, ModelReference.MEDDOSECHECKS_RANGEMINWEIGHTUNITREFID, ModelReference.MEDDOSECHECKS_RANGEMAXWEIGHT, ModelReference.MEDDOSECHECKS_RANGEMAXWEIGHTUNITREFID);
            setValueForControlWithAction(ActionReference.FORMULARYBUILDERDRUGLEVELCHECKBYWEIGHT, 0);
            setValueForControlWithAction(ActionReference.TABLEREMOVEFILTER, 0);
            setValueForControlWithAction(ActionReference.FORMULARYBUILDERDRUGLEVELCHECKBYAGE, 1);
            lastDLCShowAge = true;
            applyTableFilter(ContextReference.DOSERANGECHECKTABLE, "[1-9]", 2, getAgeOrWeightOverrideFilter());
        } else {
            hideTableColumns(ContextReference.DOSERANGECHECKTABLE, ModelReference.MEDDOSECHECKS_RANGEMINAGE, ModelReference.MEDDOSECHECKS_RANGEMINAGEUNITREFID, ModelReference.MEDDOSECHECKS_RANGEMAXAGE, ModelReference.MEDDOSECHECKS_RANGEMAXAGEUNITREFID);
            showTableColumns(ContextReference.DOSERANGECHECKTABLE, ModelReference.MEDDOSECHECKS_RANGEMINWEIGHT, ModelReference.MEDDOSECHECKS_RANGEMINWEIGHTUNITREFID, ModelReference.MEDDOSECHECKS_RANGEMAXWEIGHT, ModelReference.MEDDOSECHECKS_RANGEMAXWEIGHTUNITREFID);
            setValueForControlWithAction(ActionReference.FORMULARYBUILDERDRUGLEVELCHECKBYAGE, 0);
            setValueForControlWithAction(ActionReference.TABLEREMOVEFILTER, 0);
            setValueForControlWithAction(ActionReference.FORMULARYBUILDERDRUGLEVELCHECKBYWEIGHT, 1);
            lastDLCShowAge = false;
            applyTableFilter(ContextReference.DOSERANGECHECKTABLE, "[1-9]", 6, getAgeOrWeightOverrideFilter());
        }
        return true;
    }

    /**
	 * Return a filter which keeps the new model
	 * 
	 * @return
	 */
    public IFilterModel getAgeOrWeightOverrideFilter() {
        return new IFilterModel() {

            public boolean test(IBaseModel model) {
                MedDoseCheckModel doseCheck = (MedDoseCheckModel) model;
                return model.isNew() || (doseCheck.isAgeRangeUndefined() && doseCheck.isWeightRangeUndefined());
            }
        };
    }

    /**
	 * Search by generic or trade name
	 * 
	 * @param searchString
	 * @throws Exception
	 */
    private void search(String searchString) throws Exception {
        List<MedModel> meds = MedService.getMatchingMeds("%", Converter.isNotEmpty(searchString) ? 100 : 50);
        replaceTableRows(ContextReference.FORMULARYSEARCHRESULTSTABLE, BaseModel.toTableRows(meds));
    }

    /**
	 * Copy med to form and make this the original
	 * 
	 * @param originalMed
	 * @throws Exception
	 */
    private void selectMedication(MedModel originalMed) throws Exception {
        getMedication().getIdentifiers().clear();
        getMedication().clear();
        MedModel med = MedService.getMed(originalMed.getId());
        getMedication().copyAllFrom(med);
        reapplyOkApplyCancelOnChange();
        refreshAllTablesExcept(ContextReference.FORMULARYSEARCHRESULTSTABLE);
        showAgeHideWeightColumns(lastDLCShowAge);
        disableOkApplyCancelOnChange();
    }

    /**
	 * 
	 * @return
	 */
    public MedModel getMedication() {
        return (MedModel) getDefaultBaseModel();
    }

    /**
	 * Save the medication
	 * 
	 * @throws Exception
	 */
    private void save() throws Exception {
        getMedication().validateDataModel();
        long medicationId = MedService.store(getMedication());
        if (getMedication().isNew()) {
            MedModel med = MedService.getMed(medicationId);
            insertTableRow(ContextReference.FORMULARYSEARCHRESULTSTABLE, med);
        }
        refreshAllTablesExcept(ContextReference.FORMULARYSEARCHRESULTSTABLE);
    }

    /**
	 * Show all columns and remove filter
	 * 
	 * @throws Exception
	 */
    private void removeFilter() throws Exception {
        showAllTableColumns(ContextReference.DOSERANGECHECKTABLE);
        removeTableFilter(ContextReference.DOSERANGECHECKTABLE);
        setValueForControlWithAction(ActionReference.FORMULARYBUILDERDRUGLEVELCHECKBYWEIGHT, 0);
        setValueForControlWithAction(ActionReference.FORMULARYBUILDERDRUGLEVELCHECKBYAGE, 0);
        setValueForControlWithAction(ActionReference.TABLEREMOVEFILTER, 1);
    }

    /**
	 * Create a new result
	 */
    public boolean editSettings() throws Exception {
        if (selectedMed != null && selectedMed.getCustomControllerRef().isNotNew()) {
            long settingsFormId = ServiceUtility.editCustomControllerSettings(getFrameOrDialog(), selectedMed.getSettingsFormId(), selectedMed.getCustomControllerRef().getId());
            if (settingsFormId > 0L) {
                MedModel med = MedService.getMed(selectedMed.getId());
                if (med.getSettingsFormId() == 0L) {
                    med.setSettingsFormId(settingsFormId);
                    MedService.store(med);
                    med.setSettingsFormId(settingsFormId);
                }
            }
            return true;
        }
        return false;
    }
}
