package com.ivis.xprocess.ui.wizards.tasks;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.RoleType;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.ui.datawrappers.DataCacheManager;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.IProvideRolesOnProject;
import com.ivis.xprocess.ui.datawrappers.IProvideTasks;
import com.ivis.xprocess.ui.datawrappers.process.ProjectPrototypeWrapper;
import com.ivis.xprocess.ui.datawrappers.process.TaskPrototypeWrapper;
import com.ivis.xprocess.ui.datawrappers.project.ProjectWrapper;
import com.ivis.xprocess.ui.datawrappers.project.TaskWrapper;
import com.ivis.xprocess.ui.editors.util.EditorUtil;
import com.ivis.xprocess.ui.factories.creation.ProjectCreationFactory;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.properties.WizardMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.DeletionManager;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.view.providers.ExplorerViewLabelProvider;
import com.ivis.xprocess.ui.wizards.XProcessWizardPage;

public class NewTaskWizardPage extends XProcessWizardPage {

    private int labelWidth = 80;

    private Text projectText;

    private Text parentText;

    private Text nameText;

    private Text roletypesText;

    private Text roleText;

    private Button topLevelTaskButton;

    private Button parentButton;

    private IElementWrapper projectWrapper;

    private IElementWrapper parent;

    private IElementWrapper[] roleTypes;

    private IElementWrapper role;

    private IElementWrapper taskWrapper;

    private Button projectButton;

    protected Set<IElementWrapper> createdTaskWrappers;

    public NewTaskWizardPage(String pageName) {
        super(pageName);
        this.setTitle(WizardMessages.task_wizard_title);
        this.setDescription(WizardMessages.task_wizard_description);
    }

    public NewTaskWizardPage(String pageName, Object selectedObject) {
        super(pageName, selectedObject);
        this.setTitle(WizardMessages.task_wizard_title);
        this.setDescription(WizardMessages.task_wizard_description);
    }

    @Override
    public boolean canFlipToNextPage() {
        if (!multipleCheckBox.getSelection()) {
            return false;
        }
        return isPageComplete();
    }

    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        container.setLayout(gridLayout);
        Label label = new Label(container, SWT.NONE);
        label.setText(WizardMessages.project_field);
        GridData data = new GridData();
        data.widthHint = labelWidth;
        label.setLayoutData(data);
        projectText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        projectText.setLayoutData(data);
        projectButton = new Button(container, SWT.PUSH);
        projectButton.setText(WizardMessages.browse_button);
        projectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new ExplorerViewLabelProvider());
                dialog.setTitle(WizardMessages.project_selectiondialog_title);
                dialog.setMessage(DialogMessages.select_element_dialog_message);
                dialog.setElements(ElementUtil.getAllProjects());
                if (dialog.open() == Window.OK) {
                    if (dialog.getFirstResult() != null) {
                        if (dialog.getFirstResult() instanceof ProjectWrapper) {
                            projectWrapper = (IElementWrapper) dialog.getFirstResult();
                            projectText.setText(projectWrapper.getLabel());
                            if (projectWrapper instanceof IProvideTasks) {
                                if (((IProvideTasks) projectWrapper).getTasks().length == 0) {
                                    topLevelTaskButton.setSelection(true);
                                } else {
                                    topLevelTaskButton.setSelection(false);
                                }
                            }
                            parentText.setEnabled(!topLevelTaskButton.getSelection());
                            parentButton.setEnabled(!topLevelTaskButton.getSelection());
                        } else {
                            System.err.println("Project Selection dialog did not return a Project, it returned - " + dialog.getFirstResult());
                        }
                    }
                }
                checkData();
            }
        });
        new Label(container, SWT.NONE);
        topLevelTaskButton = new Button(container, SWT.CHECK);
        topLevelTaskButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                parentText.setEnabled(!topLevelTaskButton.getSelection());
                parentButton.setEnabled(!topLevelTaskButton.getSelection());
                checkData();
            }
        });
        topLevelTaskButton.setText(WizardMessages.wizard_create_as_top_level_task);
        data = new GridData();
        data.horizontalSpan = 2;
        topLevelTaskButton.setLayoutData(data);
        Label parentLabel = new Label(container, SWT.NONE);
        parentLabel.setText(WizardMessages.task_wizard_parentfield);
        data = new GridData();
        data.widthHint = labelWidth;
        parentLabel.setLayoutData(data);
        parentText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        parentText.setLayoutData(data);
        parentButton = new Button(container, SWT.PUSH);
        parentButton.setText(WizardMessages.browse_button);
        parentButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new ExplorerViewLabelProvider());
                dialog.setTitle(WizardMessages.task_wizard_parentselectiondialog_title);
                dialog.setMessage(DialogMessages.select_element_dialog_message);
                dialog.setElements(ElementUtil.getTasksContainedBy(projectWrapper));
                if (dialog.open() == Window.OK) {
                    if (dialog.getFirstResult() != null) {
                        parentText.setText(((IElementWrapper) dialog.getFirstResult()).getElement().getLabel());
                        NewTaskWizardPage.this.parent = (IElementWrapper) dialog.getFirstResult();
                    }
                }
                checkData();
            }
        });
        Label nameLabel = new Label(container, SWT.NONE);
        nameLabel.setText(WizardMessages.name_field);
        data = new GridData();
        data.widthHint = labelWidth;
        nameLabel.setLayoutData(data);
        nameText = new Text(container, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 2;
        nameText.setLayoutData(data);
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkData();
            }
        });
        new Label(container, SWT.NONE);
        createSeparatorCombo(container, WizardMessages.task_wizard_multiple_label, WizardMessages.wizard_separator_label);
        new Label(container, SWT.NONE);
        createOpenEditorField(container);
        Label blankerLabel = new Label(container, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 3;
        blankerLabel.setLayoutData(data);
        Label optionalLabel = new Label(container, SWT.NONE);
        optionalLabel.setText(WizardMessages.task_optional_label);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = 3;
        optionalLabel.setLayoutData(data);
        Label roleTypeLabel = new Label(container, SWT.NONE);
        roleTypeLabel.setText(WizardMessages.roletype_field);
        data = new GridData();
        data.widthHint = labelWidth;
        roleTypeLabel.setLayoutData(data);
        roletypesText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        roletypesText.setLayoutData(data);
        Button roletypeButton = new Button(container, SWT.PUSH);
        roletypeButton.setText(WizardMessages.browse_button);
        roletypeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new ExplorerViewLabelProvider());
                dialog.setTitle(WizardMessages.roletype_selectiondialog_title);
                dialog.setMessage(DialogMessages.select_element_dialog_message);
                dialog.setMultipleSelection(true);
                dialog.setElements(ElementUtil.getRoletypes());
                if (dialog.open() == Window.OK) {
                    String txt = "";
                    if (dialog.getFirstResult() != null) {
                        roleTypes = new IElementWrapper[dialog.getResult().length];
                        for (int i = 0; i < dialog.getResult().length; i++) {
                            roleTypes[i] = (IElementWrapper) dialog.getResult()[i];
                            if (txt.length() > 0) {
                                txt += ", ";
                            }
                            txt += roleTypes[i].getElement().getLabel();
                        }
                    } else {
                        roleTypes = null;
                    }
                    roletypesText.setText(txt);
                }
                checkData();
            }
        });
        Label roleLabel = new Label(container, SWT.NONE);
        roleLabel.setText(WizardMessages.role_field);
        data = new GridData();
        data.widthHint = labelWidth;
        roleLabel.setLayoutData(data);
        roleText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        roleText.setLayoutData(data);
        Button roleButton = new Button(container, SWT.PUSH);
        roleButton.setText(WizardMessages.browse_button);
        roleButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if ((projectWrapper != null) && projectWrapper instanceof IProvideRolesOnProject) {
                    ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new ExplorerViewLabelProvider());
                    dialog.setTitle(WizardMessages.role_selectiondialog_title);
                    dialog.setMessage(DialogMessages.select_element_dialog_message);
                    IProvideRolesOnProject provideRolesOnProject = (IProvideRolesOnProject) projectWrapper;
                    dialog.setElements(provideRolesOnProject.getRolesOnProject());
                    if (dialog.open() == Window.OK) {
                        if (dialog.getFirstResult() != null) {
                            role = (IElementWrapper) dialog.getFirstResult();
                            roleText.setText(role.getLabel());
                        } else {
                            role = null;
                        }
                    }
                    checkData();
                }
            }
        });
        setControl(container);
        setupData();
        setupTestHarness();
    }

    @Override
    protected void setupData() {
        if ((selectedObject != null) && selectedObject instanceof ProjectWrapper) {
            projectWrapper = (ProjectWrapper) selectedObject;
            projectText.setText(projectWrapper.getElement().getLabel());
            topLevelTaskButton.setSelection(true);
            parentText.setEnabled(false);
            parentButton.setEnabled(false);
            nameText.setFocus();
        } else if ((selectedObject != null) && selectedObject instanceof ProjectPrototypeWrapper) {
            projectWrapper = (ProjectPrototypeWrapper) selectedObject;
            projectText.setText(projectWrapper.getElement().getLabel());
            topLevelTaskButton.setSelection(true);
            parentText.setEnabled(false);
            parentButton.setEnabled(false);
            nameText.setFocus();
        } else if ((selectedObject != null) && selectedObject instanceof TaskWrapper) {
            Xtask task = ((TaskWrapper) selectedObject).getTask();
            if (task.getContainedIn() instanceof Xproject) {
                projectWrapper = DataCacheManager.getWrapperByElement(task.getContainedIn());
                projectText.setText(projectWrapper.getElement().getLabel());
            }
            parent = (TaskWrapper) selectedObject;
            String string = task.getLabel();
            parentText.setText(string);
            nameText.setFocus();
        } else if ((selectedObject != null) && selectedObject instanceof TaskPrototypeWrapper) {
            Xtask task = (Xtask) ((TaskPrototypeWrapper) selectedObject).getElement();
            parent = (TaskPrototypeWrapper) selectedObject;
            String string = task.getLabel();
            parentText.setText(string);
            nameText.setFocus();
        }
        checkData();
    }

    @Override
    public void checkData() {
        if (projectText.getText().length() == 0) {
            this.setErrorMessage(WizardMessages.error_no_project);
            if (!topLevelTaskButton.getSelection()) {
                parentText.setEnabled(false);
                parentButton.setEnabled(false);
            }
            setPageComplete(false);
            return;
        }
        if (multipleCheckBox.getSelection() && (separatorCombo.getText().length() == 0)) {
            this.setErrorMessage(WizardMessages.error_no_separator_specified);
            setPageComplete(false);
            return;
        }
        if (!topLevelTaskButton.getSelection() && (parentText.getText().length() == 0)) {
            this.setErrorMessage(WizardMessages.error_no_parent);
            setPageComplete(false);
            return;
        }
        if (!topLevelTaskButton.getSelection() && (parentText.getText().length() == 0)) {
            this.setErrorMessage(WizardMessages.invalid_parent);
            setPageComplete(false);
            return;
        }
        if ((this.getErrorMessage() != null) && (this.getErrorMessage().length() > 0)) {
            this.setErrorMessage(null);
        }
        if (nameText.getText().length() == 0) {
            this.setErrorMessage(WizardMessages.error_no_task_name);
            setPageComplete(false);
            return;
        }
        setPageComplete(true);
        setDirty(true);
        if (getNextPage() instanceof TaskPreviewWizardPage) {
            ((TaskPreviewWizardPage) getNextPage()).setupData();
        }
        getWizard().getContainer().updateButtons();
    }

    public String getTaskName() {
        return nameText.getText();
    }

    protected IElementWrapper getParent() {
        if (isTopLevel()) {
            return projectWrapper;
        }
        return parent;
    }

    protected boolean isTopLevel() {
        return topLevelTaskButton.getSelection();
    }

    /**
     * Setting up the wizard page for Abbot
     */
    private void setupTestHarness() {
        TestHarness.name(nameText, TestHarness.NEWTASK_NAMEFIELD);
        TestHarness.name(projectButton, TestHarness.NEWTASK_PROJECT_BUTTON);
        TestHarness.name(parentButton, TestHarness.NEWTASK_PARENT_BUTTON);
    }

    @Override
    public boolean save() {
        createdTaskWrappers = new HashSet<IElementWrapper>();
        if (selectedObject == null) {
            selectedObject = this.getParent();
        }
        if (multipleCheckBox.getSelection()) {
            IElementWrapper parent = this.getParent();
            String separator = separatorCombo.getText();
            StringTokenizer stringTokenizer = new StringTokenizer(this.getTaskName(), separator);
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                IElementWrapper wrapper = ProjectCreationFactory.getInstance().createTask(parent, token);
                createdTaskWrappers.add(wrapper);
                if (taskWrapper == null) {
                    taskWrapper = wrapper;
                }
            }
        } else {
            if (selectedObject instanceof ProjectWrapper || selectedObject instanceof TaskWrapper || selectedObject instanceof ProjectPrototypeWrapper) {
                IElementWrapper parent = this.getParent();
                taskWrapper = ProjectCreationFactory.getInstance().createTask(parent, this.getTaskName());
                createdTaskWrappers.add(taskWrapper);
            }
        }
        if ((roleTypes != null) && (roleTypes.length > 0) && (taskWrapper != null)) {
            Xtask task = (Xtask) taskWrapper.getElement();
            int percentage = 100 / roleTypes.length;
            for (int i = 0; i < roleTypes.length; i++) {
                RequiredResource requiredResource = task.createRequiredResource((RoleType) roleTypes[i].getElement());
                requiredResource.setMaximumConcurrentAssignments(1);
                requiredResource.setNominalAmount(percentage);
                requiredResource.setRanking(1);
                ChangeEventFactory.startChangeRecording(taskWrapper);
                ChangeEventFactory.addChange(taskWrapper, ChangeEvent.FIELDS_CHANGED);
                ChangeEventFactory.addPropertyChange(taskWrapper, "/Xtask.getRequiredResources");
                ChangeEventFactory.addPropertyChange(taskWrapper, "RequiredResources");
                ChangeEventFactory.saveChanges();
                ChangeEventFactory.stopChangeRecording();
            }
        }
        if ((taskWrapper != null) && this.openEditor()) {
            EditorUtil.getInstance().openEditor(taskWrapper);
        }
        setDirty(false);
        return true;
    }

    @Override
    public void cancel() {
        if (taskWrapper != null) {
            DeletionManager.delete(taskWrapper);
        }
    }

    public Iterable<IElementWrapper> getCreatedTasks() {
        return createdTaskWrappers;
    }
}
