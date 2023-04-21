package org.endeavour.mgmt.view;

import java.util.Observable;
import java.util.Observer;
import org.endeavour.mgmt.controller.IPrivileges;
import org.endeavour.mgmt.controller.ITaskMaintenance;
import org.endeavour.mgmt.controller.ProjectMaintenance;
import org.endeavour.mgmt.controller.SecurityMaintenance;
import org.endeavour.mgmt.controller.TaskMaintenance;
import org.endeavour.mgmt.view.components.DropDownGridBoxComponent;
import org.endeavour.mgmt.view.components.GridBoxComponent;
import org.endeavour.mgmt.view.components.PanelComponent;
import org.endeavour.mgmt.view.model.PriorityListModel;
import org.endeavour.mgmt.view.model.StatusListModel;
import org.endeavour.mgmt.view.model.TasksListModel;
import thinwire.ui.Button;
import thinwire.ui.GridBox;
import thinwire.ui.Label;
import thinwire.ui.MessageBox;
import thinwire.ui.TextField;
import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;
import thinwire.ui.layout.TableLayout;
import thinwire.ui.style.Font;

public class TasksListView extends PanelComponent implements ActionListener, Observer {

    private ITaskMaintenance taskMaintenance = null;

    private GridBoxComponent tasksGrid = null;

    private Button newButton = null;

    private Button editButton = null;

    private Button deleteButton = null;

    private TasksListModel tasksModel = null;

    private TextField nameTextField = null;

    private TextField numberTextField = null;

    private DropDownGridBoxComponent statusDropDown = null;

    private DropDownGridBoxComponent priorityDropDown = null;

    private Button okButton = null;

    private PanelComponent fieldsPanel = null;

    private boolean isSearchAllowed = false;

    private StatusListModel statusModel = null;

    private PriorityListModel priorityModel = null;

    public TasksListView(ITaskMaintenance aTaskMaintenance, boolean aIsSarchAllowed) {
        this.isSearchAllowed = aIsSarchAllowed;
        if (isSearchAllowed) {
            super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 30, 0, 25 } }, 5, 5));
        } else {
            super.setLayout(new TableLayout(new double[][] { { 0 }, { 20, 0, 25 } }, 5, 5));
        }
        this.initializeControllers(aTaskMaintenance);
        PanelComponent theHeaderPanel = new PanelComponent();
        theHeaderPanel.setLayout(new TableLayout(new double[][] { { 20, 0 }, { 20 } }, 0, 0));
        Label theLogo = new Label();
        theLogo.getStyle().getBackground().setImage(IViewConstants.TASKS_ICON);
        theHeaderPanel.add(theLogo.setLimit("0, 0"));
        Label theHeaderLabel = new Label(IViewConstants.RB.getString("tasks.lbl"));
        theHeaderLabel.getStyle().setProperty(Font.PROPERTY_FONT_BOLD, true);
        theHeaderPanel.add(theHeaderLabel.setLimit("1, 0"));
        super.add(theHeaderPanel.setLimit("0, 0"));
        PanelComponent theParametersPanel = new PanelComponent();
        theParametersPanel.setLayout(new TableLayout(new double[][] { { 820, 0 }, { 50 } }, 0, 5));
        if (isSearchAllowed) {
            this.fieldsPanel = new PanelComponent();
            this.fieldsPanel.setLayout(new TableLayout(new double[][] { { 45, 150, 40, 150, 45, 250, 55, 50 }, { 20 } }, 0, 5));
            Label thePriorityLabel = new Label(IViewConstants.RB.getString("priority.lbl") + ":");
            this.fieldsPanel.add(thePriorityLabel.setLimit("0, 0"));
            this.priorityModel = new PriorityListModel(true);
            this.priorityDropDown = new DropDownGridBoxComponent(this.priorityModel, 0);
            this.priorityDropDown.setSelectedRowObject(3);
            this.fieldsPanel.add(this.priorityDropDown.setLimit("1, 0"));
            Label theStatusLabel = new Label(IViewConstants.RB.getString("status.lbl") + ":");
            this.fieldsPanel.add(theStatusLabel.setLimit("2, 0"));
            this.statusModel = new StatusListModel(true, true);
            this.statusDropDown = new DropDownGridBoxComponent(this.statusModel, 0);
            this.statusDropDown.setSelectedRowObject(6);
            this.fieldsPanel.add(this.statusDropDown.setLimit("3, 0"));
            Label theNameLabel = new Label(IViewConstants.RB.getString("name.lbl") + ":");
            this.fieldsPanel.add(theNameLabel.setLimit("4, 0"));
            this.nameTextField = new TextField();
            this.fieldsPanel.add(nameTextField.setLimit("5, 0"));
            Label theNumberLabel = new Label(IViewConstants.RB.getString("task_number.lbl"));
            this.fieldsPanel.add(theNumberLabel.setLimit("6, 0"));
            this.numberTextField = new TextField();
            this.numberTextField.setEditMask("#########");
            this.fieldsPanel.add(this.numberTextField.setLimit("7, 0"));
            Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
            PanelComponent theButtonPanel = new PanelComponent();
            theButtonPanel.setLayout(new TableLayout(new double[][] { { 100, 0 }, { 25 } }, 0, 5));
            this.okButton = new Button(IViewConstants.OK_BUTTON_LABEL);
            this.okButton.addActionListener(Button.ACTION_CLICK, this);
            this.okButton.setImage(IViewConstants.OK_BUTTON_ICON);
            this.okButton.setEnabled(theProjectId != null);
            theButtonPanel.add(okButton.setLimit("0, 0"));
            theParametersPanel.add(this.fieldsPanel.setLimit("0, 0"));
            theParametersPanel.add(theButtonPanel.setLimit("1, 0"));
            super.add(theParametersPanel.setLimit("0, 1"));
        }
        this.initializeTasksGrid();
        PanelComponent theButtonsPanel = new PanelComponent();
        theButtonsPanel.setLayout(new TableLayout(new double[][] { { 0, 100, 100, 100 }, { 0 } }, 0, 5));
        this.newButton = new Button(IViewConstants.NEW_BUTTON_LABEL);
        this.newButton.addActionListener(Button.ACTION_CLICK, this);
        this.newButton.setImage(IViewConstants.NEW_BUTTON_ICON);
        theButtonsPanel.add(this.newButton.setLimit("1, 0"));
        this.editButton = new Button(IViewConstants.EDIT_BUTTON_LABEL);
        this.editButton.addActionListener(Button.ACTION_CLICK, this);
        this.editButton.setImage(IViewConstants.EDIT_BUTTON_ICON);
        theButtonsPanel.add(this.editButton.setLimit("2, 0"));
        this.deleteButton = new Button(IViewConstants.DELETE_BUTTON_LABEL);
        this.deleteButton.addActionListener(Button.ACTION_CLICK, this);
        this.deleteButton.setImage(IViewConstants.DELETE_BUTTON_ICON);
        theButtonsPanel.add(this.deleteButton.setLimit("3, 0"));
        this.setButtonsStatus();
        super.add(theButtonsPanel.setLimit(isSearchAllowed ? "0, 3" : "0, 2"));
    }

    private void setButtonsStatus() {
        boolean isEnabled = MainView.getProjectDropDown().getSelectedRowId() != null;
        this.editButton.setEnabled(isEnabled && !this.tasksGrid.getRows().isEmpty());
        SecurityMaintenance theSecurityMaintenance = SecurityMaintenance.getInstance();
        boolean hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_DELETE);
        this.deleteButton.setEnabled(isEnabled && !this.tasksGrid.getRows().isEmpty() && hasPrivilege);
        hasPrivilege = theSecurityMaintenance.hasPrivilege(IPrivileges.REQUIREMENTS_EDIT);
        this.newButton.setEnabled(isEnabled && hasPrivilege);
    }

    private void initializeControllers(ITaskMaintenance aTaskMaintenance) {
        if (aTaskMaintenance == null) {
            Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
            ProjectMaintenance theProjectMaintenance = new ProjectMaintenance();
            this.taskMaintenance = new TaskMaintenance(theProjectId != null ? theProjectMaintenance.retrieveProjectBy(theProjectId) : null);
        } else {
            this.taskMaintenance = aTaskMaintenance;
        }
        this.taskMaintenance.addObserver(this);
    }

    private void initializeTasksGrid() {
        Integer theProjectId = MainView.getProjectDropDown().getSelectedRowId();
        if (theProjectId != null && !this.isSearchAllowed) {
            this.tasksModel = new TasksListModel(this.taskMaintenance.getTasks());
        } else {
            this.tasksModel = new TasksListModel();
        }
        this.tasksGrid = new GridBoxComponent(this.tasksModel);
        this.tasksGrid.addActionListener(GridBox.ACTION_DOUBLE_CLICK, this);
        this.tasksGrid.setColumnWidth(0, 50);
        this.tasksGrid.setColumnWidth(1, 170);
        super.add(this.tasksGrid.setLimit(this.isSearchAllowed ? "0, 2" : "0, 1"));
    }

    private void viewTasks() {
        if (this.isSearchAllowed) {
            String thePriority = this.priorityModel.getValueByDescription(this.priorityDropDown.getText());
            String theStatus = this.statusModel.getValueByDescription(this.statusDropDown.getText());
            String theName = this.nameTextField.getText();
            String theNumber = this.numberTextField.getText();
            this.tasksModel.setData(this.taskMaintenance.getTasksBy(thePriority, theStatus, theName, theNumber));
        } else {
            this.tasksModel.setData(this.taskMaintenance.getTasks());
        }
        Integer theId = this.taskMaintenance.getSelectedWorkProductId();
        if (theId != null) {
            this.tasksGrid.setSelectedRowById(theId);
        }
        this.setButtonsStatus();
    }

    public void actionPerformed(ActionEvent aEvt) {
        Object theSource = aEvt.getSource();
        if (theSource instanceof GridBox.Range) {
            this.viewTask();
        }
        if (theSource instanceof Button) {
            Button theButton = (Button) theSource;
            if (theButton.getText().equals(IViewConstants.NEW_BUTTON_LABEL)) {
                new TaskView(this.taskMaintenance);
            }
            if (theButton.getText().equals(IViewConstants.EDIT_BUTTON_LABEL)) {
                this.viewTask();
            }
            if (theButton.getText().equals(IViewConstants.DELETE_BUTTON_LABEL)) {
                this.deleteTask();
            }
            if (theButton.getText().equals(IViewConstants.OK_BUTTON_LABEL)) {
                this.taskMaintenance.startUnitOfWork();
                this.taskMaintenance.reset();
                this.viewTasks();
                this.taskMaintenance.endUnitOfWork();
            }
        }
    }

    private void viewTask() {
        Integer theTaskId = this.tasksGrid.getSelectedRowId();
        if (theTaskId != null) {
            this.tasksGrid.setEnabled(false);
            new TaskView(theTaskId, this.taskMaintenance);
            this.tasksGrid.setEnabled(true);
        }
    }

    private void deleteTask() {
        this.taskMaintenance.startUnitOfWork();
        Integer theTaskId = this.tasksGrid.getSelectedRowId();
        if (theTaskId != null) {
            int theResult = MessageBox.confirm(null, IViewConstants.WARNING_DIALOG_TITLE, IViewConstants.DELETE_DIALOG_MESSAGE, IViewConstants.WARNING_DIALOG_BUTTONS);
            if (theResult == IViewConstants.YES) {
                this.taskMaintenance.reset();
                this.taskMaintenance.deleteTask(theTaskId);
            }
        }
        this.taskMaintenance.endUnitOfWork();
    }

    public void update(Observable aObservable, Object aObject) {
        this.viewTasks();
    }

    public int getRowCount() {
        return this.tasksModel.getRowCount();
    }
}
