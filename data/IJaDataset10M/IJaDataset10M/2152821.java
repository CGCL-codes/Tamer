package com.patientis.client.security.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.Ostermiller.util.StringTokenizer;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.common.PromptsController;
import com.patientis.client.external.ImportArchetype;
import com.patientis.client.forms.FormScreenListController;
import com.patientis.client.forms.FormTypeBuilderController;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.reference.ReferenceService;
import com.patientis.client.service.security.SecurityService;
import com.patientis.client.service.system.SystemService;
import com.patientis.client.state.State;
import com.patientis.ejb.security.ApplicationViewNotCreatedForViewRefException;
import com.patientis.framework.cache.CacheLocal;
import com.patientis.framework.concurrency.SwingWorker;
import com.patientis.framework.controls.ISPanel;
import com.patientis.framework.controls.ITextSearch;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.framework.scripting.ControlScriptFactory;
import com.patientis.framework.scripting.IControlScript;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.References;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.framework.utility.FileSystemUtil;
import com.patientis.framework.utility.SerializeUtil;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.clinical.FormTypeScreenModel;
import com.patientis.model.clinical.RecordItemModel;
import com.patientis.model.common.BaseModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DatabaseTableFieldModel;
import com.patientis.model.common.DatabaseTableModel;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.IdentifierModel;
import com.patientis.model.common.ModelField;
import com.patientis.model.common.ModelReference;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.patient.VisitModel;
import com.patientis.model.reference.AppControlClassNameReference;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ClientCacheItemReference;
import com.patientis.model.reference.ContentTypeReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.ControlStateFieldTypeReference;
import com.patientis.model.reference.ControlTypeReference;
import com.patientis.model.reference.DatabaseTableReference;
import com.patientis.model.reference.FormDisplayTypeReference;
import com.patientis.model.reference.FormGroupReference;
import com.patientis.model.reference.FormTypeReference;
import com.patientis.model.reference.PatientItemReference;
import com.patientis.model.reference.RecordItemDomainReference;
import com.patientis.model.reference.RefModel;
import com.patientis.model.reference.ReferenceGroupReference;
import com.patientis.model.reference.ValueDataTypeReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.reference.ViewTypeReference;
import com.patientis.model.reference.VisitItemReference;
import com.patientis.model.security.ApplicationControlColumnModel;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.security.ApplicationViewModel;

/**
 * 
 * RegistrationController is the MVC controller for the registration application.
 * 
 * Design Patterns: <a href="/functionality/rm/1000053.html">SingletonForm</a>
 * , <a href="/functionality/rm/1000052.html">BaseController</a>
 * , <a href="/functionality/rm/1000060.html">Clipboard</a>
 * 
 */
public class RegistrationFormWizardListController extends BaseController implements ICustomFormBuilder {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected long formGroupRefId = 0L;

    protected ApplicationViewModel applicationView = new ApplicationViewModel();

    protected FormBuilderController controller = null;

    protected static List<DatabaseTableModel> tableModels = null;

    protected FormTypeModel selectedFormType = null;

    protected List<FormTypeModel> selectedFormTypes = new ArrayList<FormTypeModel>();

    protected FormTypeScreenModel selectedScreen = null;

    protected enum CreateControl {

        TAB, SEPARATOR, EXISTINGCONTROL, NEWCONTROL, NEWDETAILCONTROL, PATIENTITEMS, ARCHETYPE, SINGLENOTE, CUSTOMGROUP
    }

    ;

    protected RefModel defaultChoice = new RefModel();

    protected int defaultChoiceCount = 0;

    protected RefModel domainRef = new RefModel();

    /**
	 * 
	 */
    private static String lastCommandScript = null;

    /**
	 * Singleton
	 */
    public RegistrationFormWizardListController() {
    }

    /**
	 * Get the singleton
	 * 
	 * @return
	 */
    public static RegistrationFormWizardListController getInstance() {
        RegistrationFormWizardListController controller = new RegistrationFormWizardListController();
        controller.selectedFormType = new FormTypeModel();
        controller.selectedScreen = controller.selectedFormType.giveFormTypeScreen();
        controller.setDefaultBaseModel(controller.selectedFormType);
        return controller;
    }

    /**
	 * Start the application with the patient model 
	 * 
	 * @param patient patient to display
	 * @throws Exception
	 */
    public void start(Component frameOrDialog, long formGroupRefId) throws Exception {
        this.formGroupRefId = formGroupRefId;
        startDialog(ApplicationDialogReference.FORMWIZARDDIALOG, frameOrDialog, false);
        refreshList();
        SwingWorker sw = new SwingWorker(null) {

            @Override
            protected void doNonUILogic() throws Exception {
                tableModels = ReferenceService.getTableModels();
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
            }
        };
        sw.start();
        displayDialog();
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
                            case DOFORMSTART:
                                getDialog().setTitle(ServiceUtility.getRefDisplay(formGroupRefId));
                                return false;
                            case CANCELSUBMITFORM:
                                cancelCloseForm();
                                return true;
                            case FORMTYPELISTPASTECONTROLS:
                                pasteControls();
                                return true;
                            case EXECUTECOMMANDSCRIPT:
                                controlCommand();
                                return true;
                            case SYSTEMDUPLICATE:
                                duplicateView();
                                return true;
                            case SYSTEMREMOVE:
                                if (removeForm()) {
                                    removeSelectedTableRows(action.getContextRefId());
                                }
                                break;
                            case EXECUTEDEFAULTACTION:
                            case SYSTEMEDIT:
                                return editFormType();
                            case SYSTEMADD:
                                addForm();
                                refreshList();
                                break;
                            case FORMWIZARDSCREENS:
                                FormScreenListController controller = FormScreenListController.getInstance();
                                if (selectedFormType.isNotNew()) {
                                    controller.start(selectedFormType, getFrame());
                                    cancelCloseForm();
                                }
                                break;
                            case SYSTEMPROPERTIES:
                                FormTypeBuilderController ftController = FormTypeBuilderController.getInstance();
                                if (selectedFormType.isNotNew()) {
                                    ftController.start(selectedFormType);
                                }
                                break;
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case EXECUTEDEFAULTACTION:
                            case SYSTEMEDIT:
                                if (tableAction.hasContext(ContextReference.FORMWIZARDLISTPANEL)) {
                                    try {
                                        return editFormType();
                                    } catch (ISCancelActionException cancel) {
                                    }
                                }
                                return false;
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(ContextReference.FORMWIZARDLISTPANEL)) {
                                    selectedFormTypes = new ArrayList<FormTypeModel>();
                                    if (tableAction.hasSelectedRows()) {
                                        Object selectedValue = tableAction.getSelectedModel();
                                        if (selectedValue instanceof FormTypeModel) {
                                            selectedFormType = (FormTypeModel) selectedValue;
                                        }
                                        for (IBaseModel m : tableAction.getSelectedModels()) {
                                            if (m instanceof FormTypeModel) {
                                                selectedFormTypes.add((FormTypeModel) m);
                                            }
                                        }
                                    }
                                    return false;
                                }
                                break;
                        }
                        break;
                }
                return false;
            }

            public String toString() {
                return "RegistrationController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Start the application with the patient model 
	 * 
	 * @param patient patient to display
	 * @throws Exception
	 */
    public void refreshList() throws Exception {
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public boolean removeForm() throws Exception {
        if (selectedFormType.isNotNew()) {
            if (PromptsController.questionIsYesForYesNo(getFrame(), "Delete " + selectedFormType.getFormTypeRef().getDisplay(), ", promptTitle))," + "Yes to delete")) {
                selectedFormType.setDeleted();
                ClinicalService.store(selectedFormType);
                return true;
            }
        }
        return false;
    }

    /**
	 * 
	 * @throws Exception
	 */
    public void addForm() throws Exception {
        String formName = PromptsController.getInput(getFrameOrDialog(), "Enter the form name", "");
        if (Converter.isNotEmpty(formName)) {
            if (ServiceUtility.getRef(FormTypeReference.groupName(), RefModel.getDefaultRefKey(formName)).isNew()) {
                applicationView = new ApplicationViewModel();
                applicationView.setViewRef(new DisplayModel(0L, formName, 0L));
                controller = FormBuilderController.getInstance();
                applicationView.setModelClassName("com.patientis.model.clinical.FormModel");
                applicationView.setFormControlColumns(3);
                applicationView.setViewTypeRef(new DisplayModel(ViewTypeReference.CONTROLS.getRefId()));
                controller.start(applicationView, this, formGroupRefId);
                selectedFormType = new FormTypeModel();
                selectedScreen = selectedFormType.giveFormTypeScreen();
                selectedScreen.setScreenName(formName);
                cancelCloseForm();
            } else {
                PromptsController.warning(formName + " exists, please use a new form name", "Form exists");
            }
        }
    }

    /**
	 * 
	 * @return
	 */
    public List<RefModel> getControlChoices() {
        List<RefModel> refs = new ArrayList<RefModel>();
        refs.add(new RefModel(new DisplayModel(CreateControl.TAB.ordinal(), "Tab", 0)));
        refs.add(new RefModel(new DisplayModel(CreateControl.SEPARATOR.ordinal(), "Separator", 0)));
        refs.add(new RefModel(new DisplayModel(CreateControl.EXISTINGCONTROL.ordinal(), "Existing item", 0)));
        refs.add(new RefModel(new DisplayModel(CreateControl.NEWCONTROL.ordinal(), "New item", 0)));
        refs.add(new RefModel(new DisplayModel(CreateControl.SINGLENOTE.ordinal(), "New note", 0)));
        refs.add(new RefModel(new DisplayModel(CreateControl.ARCHETYPE.ordinal(), "Archetype", 0)));
        refs.add(new RefModel(new DisplayModel(CreateControl.CUSTOMGROUP.ordinal(), "New Custom Value List", 0)));
        return refs;
    }

    /**
	 * Add a new control
	 */
    public List<ApplicationControlModel> addItem(Component frameOrDialog) throws ISCancelActionException, Exception {
        List<ApplicationControlModel> controls = new ArrayList<ApplicationControlModel>();
        RefModel choice = getChoice();
        if (choice.isNotNew()) {
            switch(CreateControl.class.getEnumConstants()[choice.getId().intValue()]) {
                case SEPARATOR:
                    controls.add(getSeparator());
                    break;
                case TAB:
                    controls.add(getTab());
                    break;
                case NEWCONTROL:
                    controls.addAll(getCustomControl(frameOrDialog, new RecordItemModel()));
                    break;
                case ARCHETYPE:
                    controls.addAll(getArchetype(frameOrDialog));
                    break;
                case SINGLENOTE:
                    controls.addAll(getSingleNote(frameOrDialog));
                    break;
                case CUSTOMGROUP:
                    createNewCustomReferenceGroup();
                    break;
            }
        } else {
            throw new ISCancelActionException();
        }
        controller.refreshPanel();
        for (ApplicationControlModel acm : controls) {
            acm.setId(0L);
        }
        return controls;
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public RefModel getChoice() throws Exception {
        RefModel choice = new RefModel();
        if (defaultChoiceCount > 2) {
            choice = defaultChoice;
        } else {
            choice = ServiceUtility.selectReference(getFrameOrDialog(), getControlChoices());
        }
        if (defaultChoice.getId().longValue() == choice.getId().longValue()) {
            defaultChoiceCount++;
        } else {
            defaultChoiceCount = 0;
        }
        defaultChoice = choice;
        return choice;
    }

    /**
	 * 
	 * @param pattern TODO
	 * @throws Exception
	 */
    public static void buildControlList(String pattern, List<RefModel> controlRefs, List<ApplicationControlModel> controlList, boolean browseDatamodel) throws Exception {
        VisitModel visit = new VisitModel();
        if (browseDatamodel) {
            buildControlList(pattern, true, "Patients", controlRefs, controlList, new PatientModel(), "");
            buildControlList(pattern, false, "Visit", controlRefs, controlList, visit, "");
        }
        for (ApplicationViewModel avm : getRegistrationTemplateViews()) {
            for (ApplicationControlModel acm : avm.getApplicationControls()) {
                if (Converter.isEmpty(pattern) || Converter.contains(acm.getApplicationControlName(), pattern, false) || Converter.contains(acm.getLabel(), pattern, false)) {
                    controlList.add(acm);
                    controlRefs.add(new RefModel(new DisplayModel(acm.getId(), acm.getLabel() + " (" + avm.getViewRef().getDisplay() + ")", 0)));
                }
            }
        }
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public static List<ApplicationViewModel> getRegistrationTemplateViews() throws Exception {
        List<ApplicationViewModel> views = new ArrayList<ApplicationViewModel>();
        List<Long> formTypeRefIds = ClinicalService.getFormTypeRefIdsByFormGroupRefId(FormGroupReference.REGISTRATIONSECTIONTEMPLATE.getRefId());
        formTypeRefIds.addAll(ClinicalService.getFormTypeRefIdsByFormGroupRefId(FormGroupReference.REGISTRATIONFORMS.getRefId()));
        for (Long formTypeRefId : formTypeRefIds) {
            FormTypeModel formType = ClinicalService.getFormTypeForFormTypeRefId(formTypeRefId);
            for (FormTypeScreenModel screen : formType.getScreens()) {
                ApplicationViewModel avm = SecurityService.getApplicationViewByRef(screen.getViewRef().getId());
                views.add(avm);
            }
        }
        return views;
    }

    /**
	 * Get the registration control
	 * 
	 * @return
	 */
    public static List<ApplicationControlModel> getExistingControls(ApplicationViewModel avm, Component frameOrDialog, long formGroupRefId) throws Exception {
        List<ApplicationControlModel> selectedControls = new ArrayList<ApplicationControlModel>();
        final List<RefModel> controlRefs = new ArrayList<RefModel>(4096);
        if (controlRefs.size() == 0) {
            controlRefs.addAll(SecurityService.getExistingApplicationControls(formGroupRefId));
            if (formGroupRefId != FormGroupReference.CLINICALSECTIONTEMPLATE.getRefId()) {
                controlRefs.addAll(SecurityService.getExistingApplicationControls(FormGroupReference.CLINICALSECTIONTEMPLATE.getRefId()));
            }
            if (formGroupRefId != FormGroupReference.REGISTRATIONSECTIONTEMPLATE.getRefId()) {
                controlRefs.addAll(SecurityService.getExistingApplicationControls(FormGroupReference.REGISTRATIONSECTIONTEMPLATE.getRefId()));
            }
            if (formGroupRefId != FormGroupReference.ACTIVELIST.getRefId()) {
                controlRefs.addAll(SecurityService.getExistingApplicationControls(FormGroupReference.ACTIVELIST.getRefId()));
            }
        }
        List<ApplicationControlColumnModel> columns = new ArrayList<ApplicationControlColumnModel>();
        columns.add(ApplicationControlColumnModel.newColumn("Form", ModelReference.REFS_SHORTDISPLAY, "java.lang.String", 50));
        columns.add(ApplicationControlColumnModel.newColumn("Control", ModelReference.REFS_DISPLAY, "java.lang.String", 50));
        columns.add(ApplicationControlColumnModel.newColumn("Column", ModelReference.REFS_DESCRIPTION, "java.lang.String", 50));
        columns.add(ApplicationControlColumnModel.newColumn("Label", ModelReference.REFS_REFKEY, "java.lang.String", 50));
        List<RefModel> selectedRefs = ServiceUtility.selectReferences(frameOrDialog, controlRefs, "Select existing control", 0L, null, columns, new ITextSearch() {

            @Override
            public List<Comparable> getMatches(String pattern) throws Exception {
                List<RefModel> matchRefs = new ArrayList<RefModel>();
                for (RefModel ref : controlRefs) {
                    if ((Converter.isEmpty(pattern) && matchRefs.size() < 100) || (ref.getDisplay() != null && ref.getDisplay().toLowerCase().contains(pattern.toLowerCase())) || (ref.getShortDisplay() != null && ref.getShortDisplay().toLowerCase().contains(pattern.toLowerCase())) || (ref.getRefKey() != null && ref.getRefKey().toLowerCase().contains(pattern.toLowerCase())) || (ref.getDescription() != null && ref.getDescription().toLowerCase().contains(pattern.toLowerCase()))) {
                        matchRefs.add(ref);
                    }
                }
                return BaseModel.toComparableValues(matchRefs);
            }
        }, null);
        boolean link = false;
        if (selectedRefs.size() == 1) {
            ApplicationControlModel selectedControl = SecurityService.getApplicationControl(selectedRefs.get(0).getId());
            if (selectedControl.getClassName().contains("Table")) {
                link = PromptsController.questionIsYesForYesNo(frameOrDialog, "Do you wish to link the control instead of making a copy?", "Create a link");
            }
        }
        for (RefModel ref : selectedRefs) {
            if (link) {
                ApplicationControlModel copy = SecurityService.getApplicationControl(ref.getId());
                ApplicationControlModel acm = new ApplicationControlModel();
                acm.setApplicationControlName(FormBuilderController.getUniqueControlName(avm, ref.getDisplay() + " (link)"));
                acm.setLinkApplicationControlId(ref.getId());
                acm.setClassName(copy.getClassName());
                acm.setControlTypeRef(ReferenceService.getDisplayModel(ControlTypeReference.CONTROL.getRefId()));
                selectedControls.add(acm);
            } else {
                ApplicationControlModel acm = SecurityService.getApplicationControl(ref.getId()).createNewCopy();
                acm.setApplicationControlName(FormBuilderController.getUniqueControlName(avm, acm.getApplicationControlName()));
                selectedControls.add(acm);
            }
        }
        return selectedControls;
    }

    /**
	 * Get the registration control
	 * 
	 * @return
	 */
    public List<ApplicationControlModel> getArchetype(Component frameOrDialog) throws Exception {
        List<ApplicationControlModel> selectedControls = new ArrayList<ApplicationControlModel>();
        return selectedControls;
    }

    /**
	 * Get the registration control
	 * 
	 * @return
	 */
    public List<ApplicationControlModel> getSingleNote(Component frameOrDialog) throws Exception {
        List<ApplicationControlModel> selectedControls = new ArrayList<ApplicationControlModel>();
        return selectedControls;
    }

    /**
	 * Build the application control list
	 * @param pattern TODO
	 * @param refs
	 * @param parentModel
	 * @throws Exception
	 */
    public static void buildControlList(String pattern, boolean topLevel, String pickPrefix, List<RefModel> refs, List<ApplicationControlModel> controlList, IBaseModel parentModel, String modelScript) throws Exception {
        for (ModelField fld : parentModel.getFields()) {
            if (isSkippedField(pattern, parentModel, fld)) {
                continue;
            }
            if (topLevel) {
                String modelName = Converter.cutOff(parentModel.getClass().getSimpleName(), "Model".length());
                addControlToLists(modelName, refs, controlList, fld, modelScript);
            } else {
                addControlToLists(pickPrefix, refs, controlList, fld, modelScript);
            }
        }
        for (IBaseModel childModel : parentModel.getDefinedChildModels()) {
            if (childModel.getSourceRefGroup() != null) {
                List<RefModel> displays = ReferenceService.getReference(childModel.getSourceRefGroup());
                for (RefModel display : displays) {
                    String modelSnippet = "";
                    String parentRefLookups = "";
                    int start = modelScript.indexOf("model.");
                    if (start > -1) {
                        int end = modelScript.indexOf(";", start);
                        if (end > start) {
                            modelSnippet = modelScript.substring(start, end);
                            int refStart = modelScript.indexOf("scripting);") + "scripting):".length() + 1;
                            int refEnd = start - 1;
                            parentRefLookups = modelScript.substring(refStart, refEnd);
                        }
                    }
                    String childModelScript = createModelScript(modelSnippet, display, getGiveMethod(parentModel, childModel), parentRefLookups);
                    String childPickPrefix = pickPrefix + " " + display.getDisplay() + " " + Converter.cutOff(childModel.getClass().getSimpleName(), "Model".length());
                    buildControlList(pattern, false, childPickPrefix, refs, controlList, childModel, childModelScript);
                }
            } else {
            }
        }
    }

    /**
	 * Create the model script
	 * 
	 * @param parentModelScript
	 * @param ref
	 * @param model
	 * @return
	 * @throws Exception
	 */
    private static String createModelScript(String parentModelScript, RefModel ref, String giveMethod, String parentRefLookups) throws Exception {
        String modelOrParentScript = Converter.isEmpty(parentModelScript) ? "model" : parentModelScript;
        String childModelScript = "importPackage(Packages.com.patientis.framework.scripting);\n\n" + parentRefLookups + ref.getRefKey().toLowerCase() + "RefId = ServiceUtility.getRefId(\"" + ref.getReferenceGroup() + "\",\"" + ref.getRefKey() + "\");\n\n" + modelOrParentScript + "." + giveMethod + "(" + ref.getRefKey().toLowerCase() + "RefId);";
        return childModelScript;
    }

    /**
	 * giveAddress etc
	 * 
	 * @param parentModel
	 * @param childModel
	 * @return
	 * @throws Exception
	 */
    private static String getGiveMethod(IBaseModel parentModel, IBaseModel childModel) throws Exception {
        if (tableModels == null) {
            tableModels = ReferenceService.getTableModels();
        }
        for (DatabaseTableModel tableModel : tableModels) {
            if ((tableModel.getModelName() + "Model").equalsIgnoreCase(parentModel.getClass().getSimpleName())) {
                for (DatabaseTableFieldModel field : tableModel.getTableFields()) {
                    if (field.getFieldModelName().equalsIgnoreCase(childModel.getClass().getSimpleName())) {
                        return "give" + field.getFieldModelName().substring(0, field.getFieldModelName().length() - 5);
                    }
                }
                break;
            }
        }
        return "";
    }

    /**
	 * 
	 * @param refs
	 * @param controlList
	 * @param fld
	 * @param modelScript
	 * @throws Exception
	 */
    public static void addControlToLists(String pickPrefix, List<RefModel> refs, List<ApplicationControlModel> controlList, ModelField fld, String modelScript) throws Exception {
        ApplicationControlModel acm = createControl(pickPrefix, fld);
        acm.setId((long) State.getUniqueContextId());
        acm.setModelScript(modelScript);
        controlList.add(acm);
        String cntrlDisplay = fld.getFieldName();
        if (Converter.isNotEmpty(fld.getColumnName())) {
            cntrlDisplay = getColumnDisplay(fld.getColumnName());
        }
        if (Converter.isNotEmpty(pickPrefix)) {
            cntrlDisplay = pickPrefix + " " + getColumnDisplay(fld.getColumnName());
        }
        DisplayModel display = new DisplayModel(acm.getId(), cntrlDisplay, 0L);
        refs.add(new RefModel(display));
    }

    /**
	 * 
	 * @param columnName
	 * @return
	 */
    private static String getColumnDisplay(String columnName) {
        String display = columnName.toLowerCase().replace("_", " ");
        display = display.replace(" ref id", "");
        display = display.replace(" ind", " (yes|no)");
        if (columnName.endsWith("_dt")) {
            display = "Date of " + display.replace(" dt", "");
        }
        if (columnName.endsWith("_ref_id")) {
            display = display + " (pick list)";
        }
        if (Converter.isNotEmpty(display)) {
            return display.substring(0, 1).toUpperCase() + display.substring(1);
        } else {
            return "";
        }
    }

    /**
	 * 
	 * @param columnName
	 * @return
	 */
    private static String getLabel(String columnName) {
        String display = columnName.toLowerCase().replace("_", " ");
        display = display.replace(" ref id", "");
        display = display.replace(" ind", "");
        if (columnName.endsWith("_dt")) {
            display = display.replace(" dt", " date");
        }
        if (Converter.isNotEmpty(display)) {
            return display.substring(0, 1).toUpperCase() + display.substring(1) + ":";
        } else {
            return "";
        }
    }

    /**
	 * Create control
	 * 
	 * @param modelRefId
	 * @return
	 */
    public static ApplicationControlModel createControl(String prefix, ModelField field) throws Exception {
        ApplicationControlModel acm = new ApplicationControlModel();
        acm.setLabel(getLabel(field.getColumnName()));
        String col = field.getColumnName().toLowerCase().replace("_", " ");
        acm.setApplicationControlName(Converter.convertDisplayString(prefix + " " + col));
        acm.setUnitHeight(1);
        acm.setUnitWidth(1);
        acm.setControlTypeRef(new DisplayModel(ControlTypeReference.CONTROL.getRefId()));
        acm.setModelRef(new DisplayModel(field.getModelReference(), ServiceUtility.getRefDisplay(field.getModelReference()), 0L));
        if (field.getFieldClass().equals(String.class)) {
            acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISTEXTFIELD.getRefId()));
        } else if (field.getFieldClass().equals(DisplayModel.class)) {
            if (Converter.isNotEmpty(field.getReferenceGroup())) {
                acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISCOMBOBOX.getRefId()));
                acm.setComboGroupRef(ServiceUtility.getRef(ReferenceGroupReference.groupName(), RefModel.getDefaultRefKey(field.getReferenceGroup())));
            } else {
                acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISINTEGERTEXTFIELD.getRefId()));
            }
        } else if (field.getFieldClass().equals(DateTimeModel.class)) {
            acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISCALENDAR.getRefId()));
        } else if (field.getFieldClass().equals(Integer.class) || field.getFieldClass().equals(Long.class)) {
            if (field.getColumnName().endsWith("_ind")) {
                acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISCHECKBOX.getRefId()));
            } else {
                acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISINTEGERTEXTFIELD.getRefId()));
            }
        } else if (field.getFieldClass().equals(Double.class)) {
            acm.setClassName(ServiceUtility.getRefDisplay(AppControlClassNameReference.ISDOUBLETEXTFIELD.getRefId()));
        } else {
            new Exception(String.valueOf(field.getFieldClass())).printStackTrace();
        }
        return acm;
    }

    /**
	 * 
	 * @return
	 */
    public ApplicationControlModel getTab() throws Exception {
        String tabName = PromptsController.getInput(getFrameOrDialog(), "Enter label for tab", "Tab name");
        if (Converter.isNotEmpty(tabName)) {
            ApplicationControlModel acm = new ApplicationControlModel();
            acm.setLabel(tabName);
            acm.setApplicationControlName(FormBuilderController.getUniqueControlName(applicationView, tabName));
            acm.setControlTypeRef(new DisplayModel(ControlTypeReference.TAB.getRefId()));
            return acm;
        } else {
            throw new ISCancelActionException();
        }
    }

    /**
	 * 
	 * @return
	 */
    public ApplicationControlModel getSeparator() throws Exception {
        String separatorName = PromptsController.getInput(getFrameOrDialog(), "Enter label for separator", "Separator name");
        if (Converter.isNotEmpty(separatorName)) {
            ApplicationControlModel acm = new ApplicationControlModel();
            acm.setLabel(separatorName);
            acm.setUnitWidth(9);
            acm.setApplicationControlName(FormBuilderController.getUniqueControlName(applicationView, separatorName));
            acm.setControlTypeRef(new DisplayModel(ControlTypeReference.SEPARATOR.getRefId()));
            return acm;
        } else {
            throw new ISCancelActionException();
        }
    }

    /**
	 * TODO setting to show system fields
	 * @param pattern TODO
	 * @param field
	 * 
	 * @return
	 */
    private static boolean isSkippedField(String pattern, IBaseModel model, ModelField field) {
        if (field.getColumnName().equalsIgnoreCase("last_name")) {
            int x = 0;
        }
        if (Converter.isNotEmpty(field.getColumnName())) {
            if (field.getColumnName().toLowerCase().equals("system_ref_id")) return true;
            if (field.getColumnName().toLowerCase().equals("system_sequence")) return true;
            if (field.getColumnName().toLowerCase().equals("insert_dt")) return true;
            if (field.getColumnName().toLowerCase().equals("insert_user_ref_id")) return true;
            if (field.getColumnName().toLowerCase().equals("update_dt")) return true;
            if (field.getColumnName().toLowerCase().equals("update_user_ref_id")) return true;
            if (field.getColumnName().toLowerCase().equals("active_ind")) return true;
            if (field.getColumnName().toLowerCase().equals("deleted_ind")) return true;
            if (field.getColumnName().toLowerCase().equals("version")) return true;
            if (field.getColumnName().toLowerCase().endsWith("_sound")) return true;
            if (field.getColumnName().toLowerCase().endsWith("_index")) return true;
            if (field.getColumnName().toLowerCase().endsWith("_dt_offset")) return true;
            if (field.getColumnName().toLowerCase().endsWith("_id")) {
                if (!field.getColumnName().toLowerCase().endsWith("_ref_id")) return true;
            }
            if (Converter.isNotEmpty(field.getReferenceGroup()) && Converter.isSame(field.getReferenceGroup(), model.getSourceRefGroup())) return true;
        }
        if (model.getClass().equals(IdentifierModel.class) && !field.getColumnName().equalsIgnoreCase("idvalue")) {
            return true;
        }
        if (pattern != null && Converter.isNotEmpty(pattern) && field.getFieldName() != null && !field.getFieldName().contains(pattern)) {
            return true;
        }
        return false;
    }

    /**
	 * create the form type
	 */
    public void storeApplicationView(boolean newView, DisplayModel viewRef, long formGroupRefId) throws Exception {
        if (newView) {
            FormTypeModel formType = new FormTypeModel();
            if (selectedScreen == null) {
                selectedScreen = formType.giveFormTypeScreen();
                if (Converter.isEmpty(selectedScreen.getScreenName())) {
                    selectedScreen.setScreenName(viewRef.getDisplay());
                }
            } else {
                if (formType.getScreens().size() == 0) {
                    formType.getScreens().add(selectedScreen);
                }
            }
            selectedScreen.setViewRef(viewRef);
            formType.setFormGroupRef(new DisplayModel(formGroupRefId));
            formType.setFormTypeRef(new DisplayModel(0L, viewRef.getDisplay(), 0L));
            ClinicalService.store(formType);
        } else {
        }
    }

    /**
	 * Create a new result
	 */
    public boolean editFormType() throws Exception {
        if (selectedFormType != null) {
            controller = FormBuilderController.getInstance();
            if (selectedFormType.getScreens().size() > 1) {
                if (!selectScreen()) {
                    return false;
                }
            } else {
                selectedScreen = selectedFormType.giveFormTypeScreen();
            }
            try {
                applicationView = SecurityService.getApplicationViewByRefNoCache(selectedScreen.getViewRef().getId());
            } catch (ApplicationViewNotCreatedForViewRefException ex) {
                if (PromptsController.questionIsYesForYesNo(getFrameOrDialog(), "View missing for id " + selectedScreen.getViewRef().getId() + " - create a new view?", "Create missing view?")) {
                    applicationView = new ApplicationViewModel();
                    applicationView.setViewRef(new DisplayModel(selectedScreen.getViewRef().getId()));
                    applicationView.setModelClassName("com.patientis.model.clinical.FormModel");
                    applicationView.setFormControlColumns(3);
                    applicationView.setViewTypeRef(new DisplayModel(ViewTypeReference.CONTROLS.getRefId()));
                    long id = SecurityService.store(applicationView);
                    applicationView.setId(id);
                } else {
                    return false;
                }
            }
            controller.start(applicationView, this, formGroupRefId);
            return true;
        }
        return false;
    }

    /**
	 * 
	 */
    public boolean selectScreen() throws Exception {
        List<RefModel> refs = new ArrayList<RefModel>();
        for (FormTypeScreenModel screen : selectedFormType.getScreens()) {
            RefModel ref = new RefModel(new DisplayModel(screen.getViewRef().getId(), screen.getScreenName(), 0L));
            ref.setDisplaySequence(screen.getScreenSequence());
            refs.add(ref);
        }
        RefModel viewRef = ServiceUtility.selectReference(getFrameOrDialog(), refs);
        if (viewRef.isNew()) {
            return false;
        } else {
            for (FormTypeScreenModel screen : selectedFormType.getScreens()) {
                if (screen.getViewRef().isId(viewRef.getId())) {
                    selectedScreen = screen;
                    return true;
                }
            }
            return false;
        }
    }

    /**
	 * Get the registration control
	 * 
	 * @return
	 */
    public List<ApplicationControlModel> getCustomControl(Component frameOrDialog, RecordItemModel parentItem) throws Exception {
        return null;
    }

    /**
	 * Extend patient 
	 * 
	 * @param acm
	 * @throws Exception
	 */
    public static ApplicationControlModel customControl(String label, String applicationControlName, int modelRefId, RefModel dataTypeRef, String itemReferenceGroup, String giveMethod, boolean multiLine) throws Exception {
        ApplicationControlModel acm = new ApplicationControlModel();
        acm.setApplicationControlName(applicationControlName);
        acm.setLabel(label);
        acm.setControlTypeRef(new DisplayModel(ControlTypeReference.CONTROL.getRefId()));
        acm.setModelRef(ReferenceService.getDisplayModel(modelRefId));
        if (dataTypeRef.getId().intValue() == ValueDataTypeReference.STRING.getRefId()) {
            acm.setClassName(ReferenceService.getDisplay(AppControlClassNameReference.ISTEXTFIELD.getRefId()));
            if (multiLine) {
                acm.setClassName(ReferenceService.getDisplay(AppControlClassNameReference.ISEDITORPANE.getRefId()));
                acm.setUnitHeight(5);
                acm.setScrollpane();
            }
        } else if (dataTypeRef.getId().intValue() == ValueDataTypeReference.INTEGER.getRefId()) {
            acm.setClassName(ReferenceService.getDisplay(AppControlClassNameReference.ISINTEGERTEXTFIELD.getRefId()));
        } else if (dataTypeRef.getId().intValue() == ValueDataTypeReference.DOUBLE.getRefId()) {
            acm.setClassName(ReferenceService.getDisplay(AppControlClassNameReference.ISDOUBLETEXTFIELD.getRefId()));
        } else if (dataTypeRef.getId().intValue() == ValueDataTypeReference.DATE.getRefId()) {
            acm.setClassName(ReferenceService.getDisplay(AppControlClassNameReference.ISCALENDAR.getRefId()));
        } else if (dataTypeRef.getId().intValue() == ValueDataTypeReference.BOOLEAN.getRefId()) {
            acm.setClassName(ReferenceService.getDisplay(AppControlClassNameReference.ISCHECKBOX.getRefId()));
            acm.setInitScript("component.setLabel(\"" + label + "\");");
            acm.setLabel(null);
        } else {
            throw new Exception();
        }
        if (Converter.isNotEmpty(itemReferenceGroup)) {
            String script = "importPackage(Packages.com.patientis.framework.scripting);\n\n" + "model." + giveMethod + "(ServiceUtility.getRefId(\"" + itemReferenceGroup + "\", \"" + RefModel.getDefaultRefKey(applicationControlName) + "\"));";
            if (ReferenceService.getDisplayModelNoCache(itemReferenceGroup, RefModel.getDefaultRefKey(applicationControlName)).isNew()) {
                RefModel ref = new RefModel();
                ref.setReferenceGroup(itemReferenceGroup);
                ref.setDisplay(applicationControlName);
                ReferenceService.store(ref);
            }
            acm.setModelScript(script);
        }
        return acm;
    }

    /**
	 * @see com.patientis.client.security.forms.ICustomFormBuilder#getCustomPanelDisplay()
	 */
    public ISPanel getCustomPanelDisplay() throws Exception {
        return null;
    }

    /**
	 * @see com.patientis.client.security.forms.ICustomFormBuilder#hasCustomPanelDisplay()
	 */
    public boolean hasCustomPanelDisplay() {
        return false;
    }

    /**
	 * 
	 */
    public void selectionChanged(List<ApplicationControlModel> controls) {
    }

    /**
	 * Import applicationview
	 * 
	 * @throws Exception
	 */
    public boolean exportForm() throws Exception {
        if (selectedFormType != null) {
            applicationView = SecurityService.getApplicationViewByRefNoCache(selectedScreen.getViewRef().getId());
            File file = PromptsController.promptSelectPatientOSFile(getFrame(), "PatientOS form export");
            String parentDir = file.getParent();
            String filename = file.getName();
            if (file.getName().contains(".")) {
                filename = filename.substring(0, filename.indexOf("."));
            }
            SerializeUtil.serializeSingleXML(new File(parentDir + "/" + filename + "_view.pos"), applicationView);
            SerializeUtil.serializeSingleXML(new File(parentDir + "/" + filename + "_form.pos"), selectedFormType);
        }
        return true;
    }

    /**
	 * Import applicationview
	 * 
	 * @throws Exception
	 */
    public boolean importForm() throws Exception {
        File file = PromptsController.promptSelectPatientOSFile(getFrame(), "Form file");
        if (file == null) {
            return true;
        }
        Object o = SerializeUtil.deserializeSingleXML(file);
        if (o == null) {
            return true;
        }
        FormTypeModel formType = null;
        if (o instanceof FormTypeModel) {
            formType = (FormTypeModel) o;
        } else {
            PromptsController.warning("Invalid selection " + o.getClass(), "Error");
            return true;
        }
        file = PromptsController.promptSelectPatientOSFile(getFrame(), "View file");
        o = SerializeUtil.deserializeSingleXML(file);
        if (o == null) {
            return true;
        }
        if (o instanceof ApplicationViewModel) {
            applicationView = ((ApplicationViewModel) o).createNewCopy();
        } else {
            PromptsController.warning("Invalid selection " + o.getClass(), "Error");
            return true;
        }
        if (applicationView != null) {
            file = PromptsController.promptSelectImage(getFrame(), "Image file");
            long fileId = SystemService.storeFile(file.getAbsolutePath(), ContentTypeReference.IMAGEPNG.getRefId());
            selectedFormType = formType.createNewCopy();
            selectedScreen.setFileId(fileId);
            controller = FormBuilderController.getInstance();
            controller.start(applicationView, this, formGroupRefId);
            cancelCloseForm();
        }
        return true;
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public RefModel getItemDomain(Component frameOrDialog) throws Exception {
        List<RefModel> domainRefs = new ArrayList<RefModel>();
        RefModel newRef = new RefModel();
        long newDomainRefId = State.getUniqueContextId();
        newRef.setId(newDomainRefId);
        newRef.setDisplay("(New domain...)");
        List<RefModel> existingDomainRefs = ReferenceService.getReference(RecordItemDomainReference.groupName());
        domainRefs.add(newRef);
        domainRefs.addAll(existingDomainRefs);
        RefModel domainRef = selectReference(domainRefs, "Select the domain group for the new control");
        String newDomainName = null;
        if (domainRef.getId() == newDomainRefId) {
            newDomainName = PromptsController.getInput(frameOrDialog, "Enter the new domain name");
        }
        if (newDomainName != null) {
            RefModel ref = new RefModel();
            ref.setReferenceGroup(RecordItemDomainReference.groupName());
            ref.setDisplay(newDomainName);
            long refId = ReferenceService.store(ref);
            return ReferenceService.getReference(refId);
        } else {
            return domainRef;
        }
    }

    /**
	 * Get the registration control
	 * 
	 * @return
	 */
    public List<ApplicationControlModel> getDetailControl(Component frameOrDialog) throws Exception {
        return getCustomControl(frameOrDialog, new RecordItemModel());
    }

    public DisplayModel createNewCustomReferenceGroup() throws Exception {
        return new DisplayModel();
    }

    /**
	 * Get the registration control
	 * 
	 * @return
	 */
    public static List<ApplicationControlModel> getExistingSections(ApplicationViewModel avm, Component frameOrDialog) throws Exception {
        List<ApplicationControlModel> selectedControls = new ArrayList<ApplicationControlModel>();
        List<DisplayModel> sectionDisplays = SecurityService.getTemplateSections();
        List<RefModel> sectionRefs = RefModel.toRefList(sectionDisplays);
        List<RefModel> selectedRefs = ServiceUtility.selectReferences(frameOrDialog, sectionRefs, "Select section", 0);
        for (RefModel ref : selectedRefs) {
            if (ref.isNew()) {
                throw new ISCancelActionException();
            } else {
                ApplicationControlModel acm = new ApplicationControlModel();
                acm.setApplicationControlName(FormBuilderController.getUniqueControlName(avm, ref.getDisplay()));
                acm.setControlTypeRef(ReferenceService.getDisplayModel(ControlTypeReference.SECTION.getRefId()));
                acm.setSectionViewRef(new DisplayModel(ref));
                acm.setSectionLayout(BorderLayout.NORTH);
                acm.setLabel(ref.getDisplay());
                acm.setUseFullSection();
                selectedControls.add(acm);
            }
        }
        return selectedControls;
    }

    /**
	 * 
	 */
    public void pasteControls() throws Exception {
        List<ApplicationControlModel> copiedControls = FormBuilderController.getCopiedControls();
        if (copiedControls.size() == 0) {
            PromptsController.warning("No controls in buffer to paste into forms", "Copy controls in editor first");
        } else {
            if (selectedFormTypes.size() == 0) {
                PromptsController.warning("No form types selected", "Select empty forms to paste controls into");
            } else {
                if (PromptsController.questionIsYesForYesNo(getFrameOrDialog(), "Paste " + copiedControls.size() + " controls into the empty " + selectedFormTypes.size() + " selected forms?", "Add controls to forms")) {
                    for (FormTypeModel formType : selectedFormTypes) {
                        ApplicationViewModel avm = SecurityService.getApplicationViewByRefNoCache(formType.giveFormTypeScreen().getViewRef().getId());
                        if (avm.getApplicationControls().size() == 0) {
                            for (ApplicationControlModel acm : copiedControls) {
                                avm.getApplicationControls().add(acm.createNewCopy());
                            }
                        }
                        SecurityService.store(avm);
                    }
                }
            }
        }
    }

    /**
	 * 
	 * @return
	 */
    private List<ApplicationViewModel> getSelectedApplicationViews() throws Exception {
        List<ApplicationViewModel> views = new ArrayList<ApplicationViewModel>();
        for (FormTypeModel formType : selectedFormTypes) {
            for (FormTypeScreenModel screen : formType.getScreens()) {
                ApplicationViewModel avm = SecurityService.getApplicationViewByRefNoCache(screen.getViewRef().getId());
                views.add(avm);
            }
        }
        return views;
    }

    private void duplicateView() throws Exception {
        if (selectedFormTypes.size() != 1) {
            PromptsController.warning("Select one form to duplicate", "Select a form");
        } else {
            String formTypes = PromptsController.getInputPane("Enter a unique form name per line", getFrameOrDialog());
            StringTokenizer st = new StringTokenizer(formTypes, "\n");
            List<String> formNames = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                if (Converter.isNotEmpty(s)) {
                    formNames.add(s.trim());
                }
            }
            List<Long> ids = duplicateForms(selectedFormType, formNames);
            for (Long id : ids) {
                FormTypeModel newFormType = ClinicalService.getFormType(id);
                insertTableRow(ContextReference.FORMWIZARDLISTPANEL.getRefId(), newFormType);
            }
        }
    }

    /**
	 * 
	 * @param formType
	 * @param names
	 */
    private List<Long> duplicateForms(FormTypeModel formType, List<String> names) throws Exception {
        List<Long> formTypeIds = new ArrayList<Long>();
        for (String name : names) {
            if (ReferenceService.getDisplayModel(ViewReference.groupName(), RefModel.getDefaultRefKey(name)).isNotNew()) {
                PromptsController.warning(name + " is already used in a view", "Names must be unique");
            } else if (ReferenceService.getDisplayModel(FormTypeReference.groupName(), RefModel.getDefaultRefKey(name)).isNotNew()) {
                PromptsController.warning(name + " is already used", "Names must be unique");
            }
        }
        for (String formTypeName : names) {
            FormTypeModel newFormType = formType.createNewCopy();
            newFormType.setFormTypeRef(new DisplayModel(0, formTypeName, 0));
            for (FormTypeScreenModel screen : newFormType.getScreens()) {
                long oldViewRefId = screen.getViewRef().getId();
                ApplicationViewModel oldView = SecurityService.getApplicationViewByRefNoCache(oldViewRefId);
                ApplicationViewModel copy = oldView.createNewCopy();
                RefModel ref = new RefModel();
                ref.setReferenceGroup(ViewReference.groupName());
                ref.setDisplay(formTypeName + (screen.getScreenSequence() > 0 ? ("" + screen.getScreenSequence()) : ""));
                long refId = ReferenceService.store(ref);
                ref.setId(refId);
                copy.setViewRef(new DisplayModel(ref));
                long applicationViewId = SecurityService.store(copy);
                if (applicationViewId > 0) {
                    screen.setViewRef(new DisplayModel(refId));
                }
            }
            formTypeIds.add(ClinicalService.store(newFormType));
        }
        return formTypeIds;
    }

    /**
	 * 
	 * @throws Exception
	 */
    private void controlCommand() throws Exception {
        String script = PromptsController.getInputPane("Enter script to execute e.g. control.setLocked(); view.setBlah() or formtype.setBlah()", getFrameOrDialog(), lastCommandScript == null ? "" : lastCommandScript);
        if (Converter.isNotEmpty(script)) {
            if (script.contains("control.")) {
                if (!PromptsController.questionIsYesForYesNo(getFrameOrDialog(), "Apply to script to " + ApplicationViewModel.getActiveControlCount(getSelectedApplicationViews()) + " active controls?", "Press Yes to run the script")) {
                    return;
                }
            }
            for (FormTypeModel formType : selectedFormTypes) {
                for (FormTypeScreenModel screen : formType.getScreens()) {
                    ApplicationViewModel avm = SecurityService.getApplicationViewByRefNoCache(screen.getViewRef().getId());
                    if (script.contains("control.")) {
                        for (ApplicationControlModel acm : avm.getSortedControls()) {
                            if (acm.isActive()) {
                                References refs = getControllerReferences();
                                refs.addCustomRef("control", acm);
                                refs.addCustomRef("view", avm);
                                refs.addCustomRef("formtype", formType);
                                IControlScript controlScript = ControlScriptFactory.getJavaScript();
                                controlScript.addReferences(refs);
                                String output = Converter.convertString(controlScript.executeThrow(script, String.class, script));
                            }
                        }
                    } else {
                        References refs = getControllerReferences();
                        refs.addCustomRef("view", avm);
                        refs.addCustomRef("formtype", formType);
                        IControlScript controlScript = ControlScriptFactory.getJavaScript();
                        controlScript.addReferences(refs);
                        String output = Converter.convertString(controlScript.executeThrow(script, String.class, script));
                    }
                    if (script.contains("view.") || script.contains("control.")) {
                        SecurityService.store(avm);
                    }
                }
                if (script.contains("formtype.")) {
                    ClinicalService.store(formType);
                }
            }
            lastCommandScript = script;
        }
    }
}
