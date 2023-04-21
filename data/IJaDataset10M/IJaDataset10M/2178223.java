package org.systemsbiology.apps.gui.client.widget.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.systemsbiology.apps.gui.client.constants.FileType;
import org.systemsbiology.apps.gui.client.constants.OutputFileConstants;
import org.systemsbiology.apps.gui.client.constants.PipelineStep;
import org.systemsbiology.apps.gui.client.controller.IController;
import org.systemsbiology.apps.gui.client.controller.request.GetTransitionListValidatorSetupRequest;
import org.systemsbiology.apps.gui.client.controller.request.ProjectSaveRequest;
import org.systemsbiology.apps.gui.client.controller.request.StepCancelRequest;
import org.systemsbiology.apps.gui.client.controller.request.TransitionValidatorRequest;
import org.systemsbiology.apps.gui.client.data.Model;
import org.systemsbiology.apps.gui.client.data.project.IProjectModelListener;
import org.systemsbiology.apps.gui.client.util.FileUtils;
import org.systemsbiology.apps.gui.client.widget.fileselector.SelectedFileListWidget;
import org.systemsbiology.apps.gui.client.widget.fileselector.SelectedFileWidget;
import org.systemsbiology.apps.gui.client.widget.fileselector.UploadFileWidget;
import org.systemsbiology.apps.gui.client.widget.general.GuiButton;
import org.systemsbiology.apps.gui.client.widget.general.VerticalOptionList;
import org.systemsbiology.apps.gui.client.widget.project.setupInfo.transitionListValidator.SetupInfoWidget;
import org.systemsbiology.apps.gui.domain.ATAQSProject;
import org.systemsbiology.apps.gui.domain.Algorithm;
import org.systemsbiology.apps.gui.domain.CandidateProteinList;
import org.systemsbiology.apps.gui.domain.Status;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorRunInfo;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorSetup;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorSetup.LCMS_OPTION;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;

/**
 * Details panel for configuring a transition list.
 * Executing this stage will allow for a server side script to run, validating the transitions
 * 
 * @author Mark Christiansen
 * @author Chris Kwok
 *
 */
public class TransitionListValidatorDetails extends AbstractStepDetails implements IProjectModelListener {

    /**
	 * A widget for displaying the results of a validated set of transitions
	 * @author Chris Kwok
	 *
	 */
    public class TLVResultWidget {

        private GuiButton dlValidatorScoreBtn;

        private GuiButton dlFDRBtn;

        private GuiButton dlAuditBtn;

        private ImageListWidget dlSRMPhophetPlotWidget;

        private int row;

        /**
		 * Create a transition list validator result widget
		 * @param startingRow starting row
		 */
        public TLVResultWidget(int startingRow) {
            setRow(startingRow);
            init();
        }

        private void init() {
            dlSRMPhophetPlotWidget = new ImageListWidget("SRM Prophet Plot");
            dlValidatorScoreBtn = dlValidatorScoreBtn();
            dlFDRBtn = dlFDRBtn();
            dlAuditBtn = dlAuditBtn();
            bottomFlexTable.setWidget(row++, 0, dlSRMPhophetPlotWidget);
            bottomFlexTable.setWidget(row++, 0, dlValidatorScoreBtn);
            bottomFlexTable.setWidget(row++, 0, dlFDRBtn);
            bottomFlexTable.setWidget(row++, 0, dlAuditBtn);
        }

        private GuiButton dlFDRBtn() {
            GuiButton btn = new GuiButton("Download FDR");
            btn.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent sender) {
                    Long fileID = Model.getAccessModel().getTLVSetup().getScoredTransitionFileId();
                    fileID = (fileID == null) ? -1 : fileID;
                    String url = FileUtils.get().getFileUrl(fileID.toString(), OutputFileConstants.CSV);
                    Window.open(url, "_blank", "");
                }
            });
            return btn;
        }

        private GuiButton dlAuditBtn() {
            GuiButton btn = new GuiButton("Download AuDIT output");
            btn.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent sender) {
                    Long fileID = Model.getAccessModel().getTLVSetup().getAuditFileId();
                    fileID = (fileID == null) ? -1 : fileID;
                    String url = FileUtils.get().getFileUrl(fileID.toString(), OutputFileConstants.CSV);
                    Window.open(url, "_blank", "");
                }
            });
            return btn;
        }

        private GuiButton dlValidatorScoreBtn() {
            GuiButton btn = new GuiButton("Download Validator Score");
            btn.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent sender) {
                    Long fileID = Model.getAccessModel().getTLVSetup().getAllPeakGroupXlsFileId();
                    fileID = (fileID == null) ? -1 : fileID;
                    String url = FileUtils.get().getFileUrl(fileID.toString(), OutputFileConstants.CSV);
                    Window.open(url, "_blank", "");
                }
            });
            return btn;
        }

        private List<String[]> makeImageList() {
            Map<Long, String> fileIds = new HashMap<Long, String>();
            TransitionListValidatorSetup tlvSetup = Model.getAccessModel().getTLVSetup();
            fileIds.put(tlvSetup.getFdrFileId(), "FDR");
            fileIds.put(tlvSetup.getRocFileId(), "ROC");
            fileIds.put(tlvSetup.getSeparationBarsFileId(), "SeperationBars");
            List<String[]> imageInfoList = new ArrayList<String[]>();
            for (Long fileID : fileIds.keySet()) {
                fileID = (fileID == null) ? -1 : fileID;
                String url = FileUtils.get().getFileUrl(fileID.toString(), OutputFileConstants.PNG);
                String[] tmp = new String[3];
                tmp[0] = fileIds.get(fileID);
                tmp[1] = url;
                tmp[2] = url;
                imageInfoList.add(tmp);
            }
            return imageInfoList;
        }

        /**
		 * Update images
		 */
        public void updateImageWidgetInfos() {
            this.dlFDRBtn.setEnabled(true);
            this.dlValidatorScoreBtn.setEnabled(true);
            this.dlAuditBtn.setEnabled(true);
            this.dlSRMPhophetPlotWidget.setImageInfos(makeImageList());
            this.dlSRMPhophetPlotWidget.showImages();
        }

        /**
	     * Remove results
	     */
        public void removeResults() {
            this.dlFDRBtn.setEnabled(false);
            this.dlValidatorScoreBtn.setEnabled(false);
            this.dlAuditBtn.setEnabled(false);
            this.dlSRMPhophetPlotWidget.removeImages();
        }

        /**
		 * Set the row 
		 * @param row row
		 */
        public void setRow(int row) {
            this.row = row;
        }

        /**
		 * Get the row
		 * @return row
		 */
        public int getRow() {
            return row;
        }
    }

    private SelectedFileListWidget lcmsFilesList;

    private SelectedFileWidget pepxmlFileWidget;

    private UploadFileWidget transitionFileWidget;

    private SetupInfoWidget setupInfoWidget;

    private TLVResultWidget tlvResultWidget;

    private VerticalOptionList lcmsFileTypeSelector;

    private VerticalOptionList algorithmSelector;

    private HTMLTable table;

    /**
     * Transition List Validator Details
     * @param controller RPC controller
     * @param mediator mediator for project widgets
     */
    public TransitionListValidatorDetails(IController controller, ProjectWidgetsMediator mediator) {
        super(controller, mediator);
        Model.getAccessModel().addListener(this);
    }

    protected int getNumGridRows() {
        return 7;
    }

    protected int getNumGridCols() {
        return 3;
    }

    protected PipelineStep getPipelineStep() {
        return PipelineStep.TRAN_LIST_VAL;
    }

    protected void initProgramsWidgets(int row) {
        table = new FlexTable();
        table.setCellPadding(5);
        this.initLcmsFileTypeSelector(row++);
        this.initLcmsFileList(row++);
        this.initPepxmlFile(row++);
        this.initTransitionFile(row++);
        this.initAlgorithmSelector(row++);
        this.initSetupInfoWidget(row);
        this.mainPanel.add(table);
    }

    protected String getExeButtonText() {
        return "Run Transition List Validator";
    }

    private void initLcmsFileTypeSelector(int row) {
        this.lcmsFileTypeSelector = new VerticalOptionList(table, row, 0, "LCMS file type: ", TransitionListValidatorSetup.LCMS_OPTION.getDisplayNames());
        lcmsFileTypeSelector.setTitleStyle("grayText");
        lcmsFileTypeSelector.addToTitleStyle("boldText");
        lcmsFileTypeSelector.setEditable(false);
        lcmsFileTypeSelector.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                TransitionListValidatorDetails.this.lcmsFilesList.clearFiles();
                TransitionListValidatorDetails.this.setupInfoWidget.clearFiles();
                switch(lcmsFileTypeSelector.getSelectedOptionIndex()) {
                    case 0:
                        TransitionListValidatorDetails.this.lcmsFilesList.setFileType(FileType.MZXML);
                        TransitionListValidatorDetails.this.setupInfoWidget.setFileType(FileType.MZXML);
                        break;
                    case 1:
                        TransitionListValidatorDetails.this.lcmsFilesList.setFileType(FileType.MZML);
                        TransitionListValidatorDetails.this.setupInfoWidget.setFileType(FileType.MZML);
                        break;
                    default:
                        TransitionListValidatorDetails.this.lcmsFilesList.setFileType(FileType.MZXML);
                        TransitionListValidatorDetails.this.setupInfoWidget.setFileType(FileType.MZXML);
                        break;
                }
            }
        });
    }

    private void initLcmsFileList(int row) {
        if (lcmsFilesList == null) {
            lcmsFilesList = new SelectedFileListWidget(table, row, 0, "Choose:", FileType.MZXML);
            lcmsFilesList.setController(controller);
        }
        lcmsFilesList.clearFiles();
        lcmsFilesList.setTitleStyle("grayText");
        lcmsFilesList.addToTitleStyle("boldText");
    }

    private void initPepxmlFile(int row) {
        if (this.pepxmlFileWidget == null) {
            pepxmlFileWidget = new SelectedFileWidget(table, row, 0, "pepXML file: ", FileType.PEPXML, true);
            pepxmlFileWidget.setController(controller);
        }
        pepxmlFileWidget.setTitleStyle("grayText");
        pepxmlFileWidget.addToTitleStyle("boldText");
    }

    private void initTransitionFile(int row) {
        if (this.transitionFileWidget == null) {
            transitionFileWidget = new UploadFileWidget(table, row, 0, "transition file: ", FileType.CSV, true) {

                @Override
                public void onUploadFileFailure() {
                    Window.alert("Failed to upload transition file: " + this.getFileClientPath() + "\nConfigure a transition list before continuing.");
                }

                @Override
                public void onUploadFileSuccess() {
                }
            };
        }
        transitionFileWidget.clearFiles();
        transitionFileWidget.setTitleStyle("grayText");
        transitionFileWidget.addToTitleStyle("boldText");
    }

    private void initAlgorithmSelector(int row) {
        algorithmSelector = new VerticalOptionList(table, row++, 0, "Algorithm: ", Model.getSettingsModel().getAlgorithmNames(this.getPipelineStep().getId()));
        algorithmSelector.setTitleStyle("grayText");
        algorithmSelector.addToTitleStyle("boldText");
        algorithmSelector.setEditable(false);
    }

    private void initSetupInfoWidget(int row) {
        if (setupInfoWidget == null) {
            setupInfoWidget = new SetupInfoWidget(null, table, row);
            setupInfoWidget.setFileType(FileType.MZXML);
        }
    }

    /**
     * Retrieve TLVSetup Information from Model and update necessary widgets
     */
    public void updateTransitionListValidatorSetupWidgets() {
        ATAQSProject project = Model.getAccessModel().getProject();
        TransitionListValidatorSetup tlvSetup = project.getTlvSetup();
        this.updateLcmsFileTypeSelector(tlvSetup);
        this.updateLcmsFileListAndSetupInfoWidget(tlvSetup);
        this.updatePepxmlFile(tlvSetup);
        this.updateTransitionFile(tlvSetup);
        this.updateAlgorithmSelector(tlvSetup);
    }

    /**
     * Clear widgets depending on data from TLVSetup
     */
    public void clearTransitionListValidatorSetupWidgets() {
    }

    @Override
    protected GuiButton cancelButton() {
        final GuiButton b = new GuiButton("Cancel");
        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent sender) {
                b.setEnabled(false);
                mediator.onEditCanceled();
                controller.handleRequest(new StepCancelRequest(Model.getAccessModel().getProject(), getPipelineStep()), TransitionListValidatorDetails.this);
            }
        });
        return b;
    }

    public void updateProjectStatus() {
        super.updateProjectStatus();
        this.updateTransitionListValidatorSetupWidgets();
    }

    protected void setProjectForResultsWidget() {
        ATAQSProject project = Model.getAccessModel().getProject();
        TransitionListValidatorSetup tlvSetup = project.getTlvSetup();
        Status status = this.getStepStatus();
        this.tlvResultWidget.removeResults();
        if (tlvSetup == null) return; else if (status.gui_needs_results()) {
            mediator.showBusyWidget();
            project.setTvStatus(Status.DONE.getId());
            ProjectSaveRequest saveRequest = new ProjectSaveRequest(project);
            controller.handleRequest(saveRequest);
            controller.handleRequest(new GetTransitionListValidatorSetupRequest(project), this);
        } else if (status.done()) {
            this.tlvResultWidget.updateImageWidgetInfos();
        }
    }

    protected Status getStepStatus() {
        ATAQSProject project = Model.getAccessModel().getProject();
        return Status.getStatus(project.getTvStatus());
    }

    protected void executeStep() {
        TransitionListValidatorSetup tlvsetup = this.setupInfoWidget.getTransitionListValidatorSetup();
        tlvsetup.setLcmsOptionId(this.lcmsFileTypeSelector.getSelectedOptionIndex());
        for (Algorithm algor : Model.getSettingsModel().getAlgorithms(this.getPipelineStep().getId())) {
            if (algor.getAlgorithmDisplayName().equals(this.algorithmSelector.getCurrentOption())) {
                tlvsetup.setAlgorithm(algor.getAlgorithmExecutorName());
                break;
            }
        }
        if (this.pepxmlFileWidget.getFile() != null && !this.pepxmlFileWidget.getFile().isEmpty()) tlvsetup.setPepxmlFile(this.pepxmlFileWidget.getFile());
        super.executeStep();
        this.tlvResultWidget.removeResults();
        controller.handleRequest(new TransitionValidatorRequest(Model.getAccessModel().getProject(), true), this);
    }

    public boolean saveSetup() {
        if (!this.confirmAction()) return false;
        if (this.transitionFileWidget.uploadFile()) {
            String errRunListMsg = this.setupInfoWidget.checkValidRunList();
            if (errRunListMsg != null) {
                Window.alert(errRunListMsg);
                return false;
            }
            TransitionListValidatorSetup tlvsetup = this.setupInfoWidget.getTransitionListValidatorSetup();
            tlvsetup.setTransitionFile(this.transitionFileWidget.getFileClientPath());
            tlvsetup.setTempTransitionFile(this.transitionFileWidget.getFileServerPath());
            if ((tlvsetup.getTransitionFile() == null) || tlvsetup.getTempTransitionFile() == null) {
                Window.alert("You must specify a transition file before running the validator");
                return false;
            }
            if (tlvsetup.getTlvRunInfos().isEmpty()) {
                Window.alert("You must specify LCMS files before running the validator");
                return false;
            }
            this.executeStep();
            return true;
        }
        return false;
    }

    private void updateLcmsFileTypeSelector(TransitionListValidatorSetup tlvSetup) {
        if (tlvSetup.getLcmsOptionId() == null) this.lcmsFileTypeSelector.setOptionAtIndex(0); else this.lcmsFileTypeSelector.setOptionAtIndex(tlvSetup.getLcmsOptionId());
    }

    private void updatePepxmlFile(TransitionListValidatorSetup tlvSetup) {
        if (tlvSetup == null || tlvSetup.getPepxmlFile() == null) pepxmlFileWidget.clearFiles(); else pepxmlFileWidget.setFile(tlvSetup.getPepxmlFile());
        pepxmlFileWidget.setTitleStyle("grayText");
        pepxmlFileWidget.addToTitleStyle("boldText");
    }

    private void updateTransitionFile(TransitionListValidatorSetup tlvSetup) {
        transitionFileWidget.clearFiles();
        if (tlvSetup != null && tlvSetup.getTransitionFile() != null) transitionFileWidget.setFileClientPath(tlvSetup.getTransitionFile());
        transitionFileWidget.setTitleStyle("grayText");
        transitionFileWidget.addToTitleStyle("boldText");
    }

    private void updateLcmsFileListAndSetupInfoWidget(TransitionListValidatorSetup tlvSetup) {
        if (lcmsFilesList != null) lcmsFilesList.removeFileListDisplayer(setupInfoWidget);
        lcmsFilesList.clearFiles();
        setupInfoWidget.clearFiles();
        if (tlvSetup != null && tlvSetup.getTlvRunInfos() != null && !tlvSetup.getTlvRunInfos().isEmpty()) {
            for (TransitionListValidatorRunInfo runInfo : tlvSetup.getTlvRunInfos()) {
                lcmsFilesList.addFile(runInfo.getFileName(), true);
            }
        }
        if (tlvSetup != null && tlvSetup.getLcmsOptionId() != null) {
            lcmsFilesList.setFileType(LCMS_OPTION.getById(tlvSetup.getLcmsOptionId()).getFileType());
            setupInfoWidget.setFileType(LCMS_OPTION.getById(tlvSetup.getLcmsOptionId()).getFileType());
        }
        lcmsFilesList.setTitleStyle("grayText");
        lcmsFilesList.addToTitleStyle("boldText");
        setupInfoWidget.setTransitionListValidatorSetup(tlvSetup);
        if (lcmsFilesList != null) lcmsFilesList.addFileListDisplayer(setupInfoWidget);
    }

    private void updateAlgorithmSelector(TransitionListValidatorSetup tlvSetup) {
        if (tlvSetup.getAlgorithm() == null) this.algorithmSelector.setOptionAtIndex(0); else {
            for (Algorithm algor : Model.getSettingsModel().getAlgorithms(this.getPipelineStep().getId())) {
                if (algor.getAlgorithmExecutorName().equals(tlvSetup.getAlgorithm())) {
                    this.algorithmSelector.setCurrentOption(algor.getAlgorithmDisplayName());
                    break;
                }
            }
        }
    }

    @Override
    public void revertToSaved() {
        super.revertToSaved();
        updateTransitionListValidatorSetupWidgets();
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        this.algorithmSelector.setEditable(editable);
        this.lcmsFileTypeSelector.setEditable(editable);
        this.lcmsFilesList.setEditable(editable);
        this.pepxmlFileWidget.setEditable(editable);
        this.transitionFileWidget.setEditable(editable);
        this.setupInfoWidget.setEditable(editable);
    }

    @Override
    protected void addResultsWidgets(int startingRow) {
        tlvResultWidget = new TLVResultWidget(startingRow);
    }

    /**
     * Widget containing transition list validator results
     * @return widget with transition list validator results
     */
    public TLVResultWidget getTlvResultWidget() {
        return tlvResultWidget;
    }

    public void onProteinListGeneratorSetupImported() {
    }

    public void onProteinListImported(CandidateProteinList protList) {
    }

    public void onTransitionListGeneratorSetupImported() {
    }

    public void onTransitionListPublisherSetupImported(boolean removeBusyWidget) {
    }

    public void onTransitionListValidatorSetupImported() {
        this.updateProjectStatus();
        PipelineStep step = this.getPipelineStep();
        if (mediator.isBusyWidgetShown() && Model.getAccessModel().getPipelineStep().equals(step)) {
            this.mediator.hideBusyWidget();
        }
    }
}
