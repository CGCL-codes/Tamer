package org.endeavour.mgmt.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.endeavour.mgmt.controller.IBasicInfoMaintenance;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.WorkProductMaintenance;
import org.endeavour.mgmt.view.components.DialogComponent;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.components.TabFolderComponent;
import org.endeavour.mgmt.view.components.TabSheetComponent;
import org.endeavour.mgmt.view.model.StageStatusListModel;
import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextArea;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;

@SuppressWarnings("unchecked")
public class ProjectView extends DialogComponent implements ActionListener {

    private Label descriptionLabel = null;

    private TextArea descriptionTextArea = null;

    private BasicInfoPanel basicInfoPanel = null;

    private Label statusLabel = null;

    private DropDownGridBoxComponent statusDropDown = null;

    private Button saveButton = null;

    private Button cancelButton = null;

    private ProjectMaintenance projectMaintenance = null;

    private ProjectMembersTabSheet projectMembersTabSheet = null;

    private StageStatusListModel statusListModel = null;

    public ProjectView(ProjectMaintenance aProjectMaintenance) {
        this.projectMaintenance = aProjectMaintenance;
        this.projectMaintenance.startUnitOfWork();
        this.projectMaintenance.reset();
        super.setTitle(IViewConstants.RB.getString("project.lbl"));
        super.setSize(520, 500);
        super.centerDialog();
        super.setLayout(new TableLayout(new double[][] { { 0 }, { 0, 25 } }, 5, 5));
        TabFolderComponent theTabFolder = new TabFolderComponent();
        TabSheetComponent theProjectTab = new TabSheetComponent(IViewConstants.RB.getString("project.lbl"));
        theProjectTab.setImage(IViewConstants.PROJECTS_ICON);
        theProjectTab.setLayout(new TableLayout(new double[][] { { 0 }, { 125, 0 } }, 5));
        this.basicInfoPanel = new BasicInfoPanel(new Date(), new Date());
        this.basicInfoPanel.setProgressDropDownStatus(false);
        theProjectTab.add(this.basicInfoPanel.setLimit("0, 0"));
        PanelComponent theDetailsPanel = new PanelComponent();
        theDetailsPanel.setLayout(new TableLayout(new double[][] { { 75, 0 }, { 20, 0 } }, 5, 5));
        theProjectTab.add(theDetailsPanel.setLimit("0, 1"));
        this.statusLabel = new Label(IViewConstants.RB.getString("status.lbl") + ":");
        theDetailsPanel.add(this.statusLabel.setLimit("0, 0"));
        this.statusListModel = new StageStatusListModel();
        this.statusDropDown = new DropDownGridBoxComponent(this.statusListModel, 0);
        this.statusDropDown.selectFirstElement();
        theDetailsPanel.add(this.statusDropDown.setLimit("1, 0"));
        this.descriptionLabel = new Label(IViewConstants.RB.getString("description.lbl") + ":");
        theDetailsPanel.add(this.descriptionLabel.setLimit("0, 1"));
        this.descriptionTextArea = new TextArea();
        theDetailsPanel.add(this.descriptionTextArea.setLimit("1, 1"));
        theTabFolder.add(theProjectTab);
        this.projectMembersTabSheet = new ProjectMembersTabSheet(IViewConstants.RB.getString("project_members.lbl"));
        this.projectMembersTabSheet.setImage(IViewConstants.USERS_ICON);
        this.projectMembersTabSheet.setUnassignedProjectMembers(this.projectMaintenance.getAllUnassignedProjectMembers());
        theTabFolder.add(this.projectMembersTabSheet);
        super.add(theTabFolder.setLimit("0, 0"));
        PanelComponent theButtonsPanel = new PanelComponent();
        theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100 }, { 0 } }, 0, 5));
        this.saveButton = new Button(IViewConstants.SAVE_BUTTON_LABEL);
        this.saveButton.addActionListener(Button.ACTION_CLICK, this);
        this.saveButton.setImage(IViewConstants.SAVE_BUTTON_ICON);
        theButtonsPanel.add(this.saveButton.setLimit("1, 0"));
        this.cancelButton = new Button(IViewConstants.CANCEL_BUTTON_LABEL);
        this.cancelButton.addActionListener(Button.ACTION_CLICK, this);
        this.cancelButton.setImage(IViewConstants.CANCEL_BUTTON_ICON);
        theButtonsPanel.add(this.cancelButton.setLimit("2, 0"));
        super.add(theButtonsPanel.setLimit("0, 1"));
        this.setButtonsStatus();
        this.setVisible(true);
    }

    public ProjectView(Integer aProjectId, ProjectMaintenance aProjectMaintenance) {
        this(aProjectMaintenance);
        this.viewProject(aProjectId);
    }

    private void viewProject(Integer aProjectId) {
        Map<String, Object> theData = this.projectMaintenance.getProjectDataBy(aProjectId);
        this.basicInfoPanel.setData(theData);
        String theDescription = (String) theData.get(ProjectMaintenance.DESCRIPTION);
        String theStatus = (String) theData.get(ProjectMaintenance.STATUS);
        this.statusDropDown.setText(this.statusListModel.getDescriptionByValue(theStatus));
        this.descriptionTextArea.setText(theDescription);
        this.projectMembersTabSheet.setAssignedProjectMembers((List) theData.get(WorkProductMaintenance.PROJECT_MEMBERS));
        this.projectMembersTabSheet.setUnassignedProjectMembers(this.projectMaintenance.getAllUnassignedProjectMembers());
        super.setTitle(IViewConstants.RB.getString("project.lbl") + " - " + (String) theData.get(IBasicInfoMaintenance.NAME));
    }

    public void actionPerformed(ActionEvent aEvt) {
        Button theSource = (Button) aEvt.getSource();
        if (theSource.getText().equals(IViewConstants.SAVE_BUTTON_LABEL)) {
            int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.SAVE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
            if (theResult == IViewConstants.YES) {
                this.saveProject();
            }
        }
        if (theSource.getText().equals(IViewConstants.CANCEL_BUTTON_LABEL)) {
            this.setVisible(false);
        }
    }

    private void saveProject() {
        Map<String, Object> theData = new HashMap<String, Object>();
        this.basicInfoPanel.getData(theData);
        theData.put(ProjectMaintenance.PROJECT_MEMBERS, this.projectMembersTabSheet.getAssignedProjectMembers());
        theData.put(ProjectMaintenance.DESCRIPTION, this.descriptionTextArea.getText());
        theData.put(ProjectMaintenance.STATUS, this.statusListModel.getValueByDescription(this.statusDropDown.getText()));
        List<String> theErrors = this.projectMaintenance.saveProject(theData);
        if (theErrors.isEmpty()) {
            this.setVisible(false);
        } else {
            this.viewErrors(theErrors);
        }
    }

    private void viewErrors(List<String> aErrors) {
        StringBuffer theErrorMessages = new StringBuffer();
        for (String theError : aErrors) {
            theErrorMessages.append(theError);
            theErrorMessages.append("\n");
        }
        MessageBox.confirm(null, IViewConstants.ERROR_DIALOG_TITLE, theErrorMessages.toString());
    }

    public void setVisible(boolean aVisible) {
        if (!aVisible) {
            this.projectMaintenance.endUnitOfWork();
        }
        super.setVisible(aVisible);
    }

    private void setButtonsStatus() {
        SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
        boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.PLANNING_EDIT);
        this.saveButton.setEnabled(hasPrivilege);
    }
}
