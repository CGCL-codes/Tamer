package org.endeavour.mgmt.view;

import org.endeavour.mgmt.controller.ProjectMemberAssignmentsController;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TaskMaintenance;
import org.endeavour.mgmt.controller.UseCaseMaintenance;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.PriorityListModel;
import org.endeavour.mgmt.view.model.ProjectMembersListModel;
import org.endeavour.mgmt.view.model.StatusListModel;
import org.endeavour.mgmt.view.model.TypeListModel;
import thinwire.ui.Button;
import thinwire.ui.Label;
import thinwire.ui.WebBrowser;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.event.PropertyChangeEvent;
import thinwire.ui.event.PropertyChangeListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class ProjectMemberAssignmentsReportView extends PanelComponent implements ActionListener, PropertyChangeListener {

    private DropDownGridBoxComponent projectMembersDrowpDown = null;

    private DropDownGridBoxComponent typeDropDown = null;

    private DropDownGridBoxComponent statusDropDown = null;

    private DropDownGridBoxComponent priorityDropDown = null;

    private ProjectMemberAssignmentsController projectMemberAssignmentController = null;

    private StatusListModel statusListModel = null;

    private Button okButton = null;

    private PanelComponent fieldsPanel = null;

    private WebBrowser webBrowser = null;

    private TypeListModel typesListModel = null;

    private PriorityListModel priorityListModel = null;

    public ProjectMemberAssignmentsReportView() {
        this.projectMemberAssignmentController = new ProjectMemberAssignmentsController();
        super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 30, 0 } }, 5, 5));
        PanelComponent theParametersPanel = new PanelComponent();
        theParametersPanel.setLayout(new TableLayout(new double[][] { { 820, 0 }, { 50 } }, 0, 5));
        this.fieldsPanel = new PanelComponent();
        this.fieldsPanel.setLayout(new TableLayout(new double[][] { { 100, 130, 30, 130, 40, 130, 50, 130 }, { 20 } }, 0, 5));
        Label theProjectMemberLabel = new Label(IViewConstants.RB.getString("project_member.lbl") + ":");
        this.fieldsPanel.add(theProjectMemberLabel.setLimit("0, 0"));
        ProjectMembersListModel theProjectMembersListModel = null;
        Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
        if (theProjectId != null) {
            theProjectMembersListModel = new ProjectMembersListModel(this.projectMemberAssignmentController.getProjectMembers(), true);
        } else {
            theProjectMembersListModel = new ProjectMembersListModel();
        }
        this.projectMembersDrowpDown = new DropDownGridBoxComponent(theProjectMembersListModel, 0);
        this.projectMembersDrowpDown.setText(ProjectMembersListModel.ALL);
        this.fieldsPanel.add(this.projectMembersDrowpDown.setLimit("1, 0"));
        Label theTypeLabel = new Label(IViewConstants.RB.getString("type.lbl") + ":");
        this.fieldsPanel.add(theTypeLabel.setLimit("2, 0"));
        this.typesListModel = new TypeListModel();
        this.typeDropDown = new DropDownGridBoxComponent(this.typesListModel, 0);
        this.typeDropDown.addPropertyChangeListener(DropDownGridBoxComponent.PROPERTY_TEXT, this);
        this.typeDropDown.setSelectedRowObject(4);
        this.fieldsPanel.add(typeDropDown.setLimit("3, 0"));
        Label theStatusLabel = new Label(IViewConstants.RB.getString("status.lbl") + ":");
        this.fieldsPanel.add(theStatusLabel.setLimit("4, 0"));
        this.statusListModel = new StatusListModel(true, true);
        this.statusDropDown = new DropDownGridBoxComponent(this.statusListModel, 0);
        this.statusDropDown.setEnabled(false);
        this.statusDropDown.setSelectedRowObject(6);
        this.fieldsPanel.add(this.statusDropDown.setLimit("5, 0"));
        Label thePriorityLabel = new Label(IViewConstants.RB.getString("priority.lbl") + ":");
        this.fieldsPanel.add(thePriorityLabel.setLimit("6, 0"));
        this.priorityListModel = new PriorityListModel(true);
        this.priorityDropDown = new DropDownGridBoxComponent(this.priorityListModel, 0);
        this.priorityDropDown.setSelectedRowObject(3);
        this.fieldsPanel.add(this.priorityDropDown.setLimit("7, 0"));
        PanelComponent theButtonPanel = new PanelComponent();
        theButtonPanel.setLayout(new TableLayout(new double[][] { { 100, 0 }, { 25 } }, 0, 5));
        this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
        this.okButton.addActionListener(Button.ACTION_CLICK, this);
        this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
        this.okButton.setEnabled(theProjectId != null);
        theButtonPanel.add(okButton.setLimit("0, 0"));
        theParametersPanel.add(this.fieldsPanel.setLimit("0, 0"));
        theParametersPanel.add(theButtonPanel.setLimit("1, 0"));
        PanelComponent theHeaderPanel = new PanelComponent();
        theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));
        Label theLogo = new Label();
        theLogo.getStyle().getBackground().setImage(IViewConstants.REPORT_ICON);
        theHeaderPanel.add(theLogo.setLimit("0, 0"));
        Label theHeaderLabel = new Label(IViewConstants.RB.getString("project_member_assignments.lbl"));
        theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
        theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));
        super.add(theHeaderPanel.setLimit("0, 0"));
        super.add(theParametersPanel.setLimit("0, 1"));
        this.webBrowser = new WebBrowser();
        super.add(this.webBrowser.setLimit("0, 2"));
    }

    public void propertyChange(PropertyChangeEvent aEvt) {
        String theDescription = (String) aEvt.getNewValue();
        this.fieldsPanel.remove(this.statusDropDown);
        String theType = this.typesListModel.getValueByDescription(theDescription);
        if (theType.equals(UseCaseMaintenance.LABEL) || theType.equals(TaskMaintenance.LABEL)) {
            this.statusListModel = new StatusListModel(false, true);
            this.statusDropDown = new DropDownGridBoxComponent(this.statusListModel, 0);
            this.statusDropDown.setSelectedRowObject(3);
        } else {
            this.statusListModel = new StatusListModel(true, true);
            this.statusDropDown = new DropDownGridBoxComponent(this.statusListModel, 0);
            this.statusDropDown.setSelectedRowObject(6);
        }
        this.statusDropDown.setEnabled(!theType.equals(StatusListModel.ALL));
        this.fieldsPanel.add(this.statusDropDown.setLimit("5, 0"));
    }

    public void actionPerformed(ActionEvent aEvt) {
        SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
        theSecurityMaintenance.startUnitOfWork();
        Integer theProjectMember = this.projectMembersDrowpDown.getSelectedRowId();
        String theType = this.typesListModel.getValueByDescription(this.typeDropDown.getText());
        String theStatus = this.statusListModel.getValueByDescription(this.statusDropDown.getText());
        String thePriority = this.priorityListModel.getValueByDescription(this.priorityDropDown.getText());
        Integer theProject = MainView.getProjectDropDown().getSelectedRowId();
        this.webBrowser.setLocation(null);
        this.webBrowser.setLocation(this.projectMemberAssignmentController.createReportLocation(theProjectMember, theType, theStatus, thePriority, theProject));
        theSecurityMaintenance.endUnitOfWork();
    }
}
