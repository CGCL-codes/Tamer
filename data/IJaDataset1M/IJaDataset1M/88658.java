package com.ivis.xprocess.ui.wizards.tasks;

import java.util.HashMap;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import com.ivis.xprocess.core.Constraint;
import com.ivis.xprocess.core.WorkPackage;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.Constraint.DatedItemType;
import com.ivis.xprocess.core.exceptions.CyclicConstraintException;
import com.ivis.xprocess.ui.datawrappers.DataCacheManager;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.ITask;
import com.ivis.xprocess.ui.datawrappers.process.PatternWrapper;
import com.ivis.xprocess.ui.datawrappers.project.IManageTime;
import com.ivis.xprocess.ui.dialogs.selection.WorkPackageSelectionDialog;
import com.ivis.xprocess.ui.factories.creation.ElementCreationFactory;
import com.ivis.xprocess.ui.properties.WizardMessages;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.widgets.DateWithButton;
import com.ivis.xprocess.ui.wizards.XProcessWizardPage;
import com.ivis.xprocess.util.Day;

public class NewConstraintWizardPage extends XProcessWizardPage {

    private Xtask task;

    private Button startMustAfterButton;

    private Button endMustAfterButton;

    private int spacer = 25;

    private int componentWidth = 150;

    private Composite dynamicComposite;

    private Button taskEventButton;

    private Button calendarDateButton;

    private Combo typeCombo;

    private HashMap<Integer, DatedItemType> datedItemTypes = new HashMap<Integer, DatedItemType>();

    private DateWithButton dateWithButton;

    private Spinner offSetSpinner;

    private IElementWrapper workPackageWrapper;

    private Constraint constraint;

    private Text workPackageText;

    private int selectionIndex = -1;

    private Day calendarDay = null;

    private int offSet = 0;

    private boolean onStart = true;

    private boolean taskEvent = true;

    private Constraint newConstraint;

    private Group eventTypeGroup;

    private DatedItemType defaultDatedItemType = DatedItemType.FORECAST_END;

    private boolean offsetChanged = false;

    private Object destinationObject;

    private boolean internalChange = false;

    protected NewConstraintWizardPage(String pageName) {
        super(pageName);
        this.setTitle(WizardMessages.constraint_wizard_title);
        this.setDescription(WizardMessages.constraint_wizard_description);
    }

    public NewConstraintWizardPage(String pageName, Object selectedObject, boolean onStart) {
        super(pageName, selectedObject);
        if (pageName == WizardMessages.constraint_wizard_edit_pagename) {
            this.setDescription(WizardMessages.constraint_wizard_edit_description);
        } else {
            this.setDescription(WizardMessages.constraint_wizard_description);
        }
        this.setTitle(WizardMessages.constraint_wizard_title);
        this.onStart = onStart;
    }

    public NewConstraintWizardPage(String pageName, Object selectedObject, Object destinationObject, boolean onStart) {
        this(pageName, selectedObject, onStart);
        this.destinationObject = destinationObject;
    }

    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);
        Label label = new Label(container, SWT.NONE);
        if (selectedObject instanceof Xtask) {
            task = (Xtask) selectedObject;
            label.setText(task.getName() + " Constraint:");
        }
        GridData layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        label.setLayoutData(layoutData);
        label = new Label(container, SWT.NONE);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        label.setLayoutData(layoutData);
        setControl(container);
        label = new Label(container, SWT.NONE);
        layoutData = new GridData();
        layoutData.widthHint = spacer;
        label.setLayoutData(layoutData);
        startMustAfterButton = new Button(container, SWT.RADIO);
        startMustAfterButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                endMustAfterButton.setSelection(!startMustAfterButton.getSelection());
                onStart = startMustAfterButton.getSelection();
                checkData();
            }
        });
        startMustAfterButton.setText("Start must be after");
        label = new Label(container, SWT.NONE);
        layoutData = new GridData();
        layoutData.widthHint = spacer;
        label.setLayoutData(layoutData);
        endMustAfterButton = new Button(container, SWT.RADIO);
        endMustAfterButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                startMustAfterButton.setSelection(!endMustAfterButton.getSelection());
                onStart = startMustAfterButton.getSelection();
                checkData();
            }
        });
        endMustAfterButton.setText("End must be after");
        if (onStart) {
            startMustAfterButton.setSelection(true);
        } else {
            endMustAfterButton.setSelection(true);
        }
        new Label(container, SWT.NONE);
        eventTypeGroup = new Group(container, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        layoutData.widthHint = componentWidth;
        eventTypeGroup.setLayout(new GridLayout());
        eventTypeGroup.setLayoutData(layoutData);
        taskEventButton = new Button(eventTypeGroup, SWT.RADIO);
        taskEventButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                calendarDateButton.setSelection(!taskEventButton.getSelection());
                taskEvent = taskEventButton.getSelection();
                setUpDynamicComposite();
                checkData();
                checkOffset();
            }
        });
        taskEventButton.setText("Task event");
        taskEventButton.setSelection(true);
        calendarDateButton = new Button(eventTypeGroup, SWT.RADIO);
        calendarDateButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                taskEventButton.setSelection(!calendarDateButton.getSelection());
                taskEvent = taskEventButton.getSelection();
                setUpDynamicComposite();
                checkData();
            }
        });
        calendarDateButton.setText("Calendar date");
        label = new Label(eventTypeGroup, SWT.NONE);
        layoutData = new GridData();
        layoutData.widthHint = spacer;
        label.setLayoutData(layoutData);
        dynamicComposite = new Composite(eventTypeGroup, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        layoutData.widthHint = componentWidth + 20;
        gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        dynamicComposite.setLayout(gridLayout);
        dynamicComposite.setLayoutData(layoutData);
        setUpDynamicComposite();
        setupData();
        setupTestHarness();
    }

    private void setUpDynamicComposite() {
        for (Control control : dynamicComposite.getChildren()) {
            control.dispose();
        }
        if (taskEventButton.getSelection()) {
            typeCombo = new Combo(dynamicComposite, SWT.READ_ONLY);
            typeCombo.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    selectionIndex = typeCombo.getSelectionIndex();
                    checkData();
                    if (selectedObject != null) {
                        checkOffset();
                    }
                }
            });
            GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
            typeCombo.setLayoutData(layoutData);
            int indexOfDefaultDatedItem = -1;
            int counter = 0;
            for (DatedItemType datedItemType : DatedItemType.values()) {
                if (datedItemType != DatedItemType.CALENDAR_DATE) {
                    typeCombo.add(datedItemType.name());
                    datedItemTypes.put(new Integer(counter), datedItemType);
                    if (datedItemType == defaultDatedItemType) {
                        indexOfDefaultDatedItem = counter;
                    }
                    counter++;
                }
            }
            if (indexOfDefaultDatedItem != -1) {
                typeCombo.select(indexOfDefaultDatedItem);
                selectionIndex = indexOfDefaultDatedItem;
            } else {
                typeCombo.select(0);
                selectionIndex = 0;
            }
            Label label = new Label(dynamicComposite, SWT.NONE);
            label.setText("of Task/Timebox/Project:");
            Composite workPackageSelectioncomposite = new Composite(dynamicComposite, SWT.NONE);
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginWidth = 0;
            gridLayout.numColumns = 2;
            workPackageSelectioncomposite.setLayout(gridLayout);
            layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
            workPackageSelectioncomposite.setLayoutData(layoutData);
            workPackageText = new Text(workPackageSelectioncomposite, SWT.BORDER | SWT.READ_ONLY);
            layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
            workPackageText.setLayoutData(layoutData);
            if (workPackageWrapper != null) {
                workPackageText.setText(workPackageWrapper.getLabel());
            }
            final Button workPackageBrowseButton = new Button(workPackageSelectioncomposite, SWT.PUSH);
            workPackageBrowseButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    WorkPackageSelectionDialog workPackageSelectionDialog = null;
                    if (task.getContainedIn() instanceof Xprocess) {
                        IElementWrapper patternWrapper = ElementUtil.getPatternFor(DataCacheManager.getWrapperByElement(task));
                        if ((patternWrapper != null) && patternWrapper instanceof PatternWrapper) {
                            workPackageSelectionDialog = new WorkPackageSelectionDialog(workPackageBrowseButton.getShell(), task, (PatternWrapper) patternWrapper);
                        }
                    } else {
                        if (task.getContainedIn() instanceof Xproject) {
                            Xproject project = (Xproject) task.getContainedIn();
                            if (project.getContainedIn() instanceof Xprocess) {
                                Xprocess xprocess = (Xprocess) project.getContainedIn();
                                PatternWrapper patternWrapper = (PatternWrapper) DataCacheManager.getWrapperByElement(xprocess.getPatternContaining(project));
                                workPackageSelectionDialog = new WorkPackageSelectionDialog(workPackageBrowseButton.getShell(), task, patternWrapper);
                            } else {
                                workPackageSelectionDialog = new WorkPackageSelectionDialog(workPackageBrowseButton.getShell(), task);
                            }
                        }
                    }
                    if (workPackageSelectionDialog != null) {
                        if (workPackageSelectionDialog.open() == IDialogConstants.OK_ID) {
                            workPackageWrapper = workPackageSelectionDialog.getSelectedElementWrapper();
                            workPackageText.setText(workPackageWrapper.getLabel());
                            checkData();
                        }
                    }
                }
            });
            workPackageBrowseButton.setText(WizardMessages.browse_button);
            Composite spinnerComposite = new Composite(dynamicComposite, SWT.NONE);
            GridLayout spinnerGridLayout = new GridLayout();
            spinnerGridLayout.numColumns = 3;
            spinnerGridLayout.marginWidth = 0;
            spinnerComposite.setLayout(spinnerGridLayout);
            Label spinnerLabel = new Label(spinnerComposite, SWT.NONE);
            spinnerLabel.setText("Offset by:");
            offSetSpinner = new Spinner(spinnerComposite, SWT.NONE);
            offSetSpinner.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    if (!internalChange) {
                        offsetChanged = true;
                    }
                    offSet = offSetSpinner.getSelection();
                    checkData();
                }
            });
            layoutData = new GridData(GridData.FILL_HORIZONTAL);
            offSetSpinner.setLayoutData(layoutData);
            offSetSpinner.setDigits(0);
            offSetSpinner.setMaximum(Integer.MAX_VALUE);
            offSetSpinner.setMinimum(Integer.MIN_VALUE);
            Label secondSpinnerLabel = new Label(spinnerComposite, SWT.NONE);
            secondSpinnerLabel.setText("days");
            checkOffset();
            TestHarness.name(workPackageBrowseButton, TestHarness.CONSTRAINTWIZARD_WORKPACKAGEBROWSEBUTTON);
        } else {
            dateWithButton = new DateWithButton(dynamicComposite, SWT.NONE, SWT.BORDER, SWT.NONE, "Select a Calendar date:");
            dateWithButton.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    calendarDay = dateWithButton.getDay();
                    checkData();
                }
            });
            if (calendarDay != null) {
                dateWithButton.setDay(calendarDay);
            }
            GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
            dateWithButton.setLayoutData(layoutData);
            TestHarness.name(dateWithButton, TestHarness.CONSTRAINTWIZARD_CALENDARDATE);
        }
        container.layout(true, true);
    }

    @Override
    protected void setupData() {
        if (selectedObject instanceof Xtask) {
            task = (Xtask) selectedObject;
        }
        if (selectedObject instanceof ITask) {
            task = ((ITask) selectedObject).getTask();
        }
        if ((destinationObject != null) && destinationObject instanceof IElementWrapper) {
            workPackageWrapper = (IElementWrapper) destinationObject;
            if (workPackageWrapper instanceof IManageTime) {
                IManageTime manageTime = (IManageTime) workPackageWrapper;
                workPackageWrapper = DataCacheManager.getWrapperByElement(manageTime.getTask());
            }
            workPackageText.setText(workPackageWrapper.getLabel());
            calendarDateButton.setSelection(false);
            calendarDateButton.setEnabled(false);
            taskEventButton.setSelection(true);
            taskEventButton.setEnabled(true);
            taskEvent = true;
        }
        if (constraint != null) {
            if (constraint.isOnStart()) {
                startMustAfterButton.setSelection(true);
                startMustAfterButton.setEnabled(true);
                endMustAfterButton.setSelection(false);
                endMustAfterButton.setEnabled(false);
            } else {
                startMustAfterButton.setSelection(false);
                startMustAfterButton.setEnabled(false);
                endMustAfterButton.setSelection(true);
                endMustAfterButton.setEnabled(true);
            }
            DatedItemType datedItemType = constraint.getDatedItemType();
            if (!datedItemType.name().equals(DatedItemType.CALENDAR_DATE.name())) {
                for (Integer index : datedItemTypes.keySet()) {
                    if (datedItemTypes.get(index).equals(datedItemType.name())) {
                        typeCombo.select(index);
                        break;
                    }
                }
                workPackageWrapper = DataCacheManager.getWrapperByElement(constraint.getWorkPackage());
                workPackageText.setText(constraint.getWorkPackage().getLabel());
                offSetSpinner.setSelection(constraint.getOffset());
                calendarDateButton.setSelection(false);
                calendarDateButton.setEnabled(false);
                taskEventButton.setSelection(true);
                taskEventButton.setEnabled(true);
                taskEvent = true;
            } else {
                taskEventButton.setEnabled(false);
                calendarDateButton.setSelection(true);
                taskEventButton.setSelection(false);
                setUpDynamicComposite();
                dateWithButton.setDay(constraint.getCalendarDay());
                taskEvent = false;
            }
        }
        checkData();
    }

    @Override
    public void checkData() {
        String errorMessage = "";
        if ((this.getErrorMessage() != null) && (this.getErrorMessage().length() > 0)) {
            this.setErrorMessage(null);
        }
        if (!taskEvent && (dateWithButton.getDay() == null)) {
            errorMessage = WizardMessages.constraint_wizard_no_calendar_date;
        }
        if (taskEvent && (workPackageWrapper != null)) {
            WorkPackage workPackage = (WorkPackage) workPackageWrapper.getElement();
            if (workPackage instanceof Xtask) {
                Xtask taskToDependOn = (Xtask) workPackage;
                if (task.dependsOn(taskToDependOn)) {
                    if (constraint != null) {
                        if (!dependencyCausedByConstraint(task, taskToDependOn)) {
                            errorMessage = task.getLabel() + " " + WizardMessages.constraint_wizard_already_dependson + " " + taskToDependOn.getLabel();
                        }
                    } else {
                        errorMessage = task.getLabel() + " " + WizardMessages.constraint_wizard_already_dependson + " " + taskToDependOn.getLabel();
                    }
                }
                if (taskToDependOn.dependsOn(task)) {
                    errorMessage = WizardMessages.constraint_wizard_eventtask_already_dependson_prefix + " " + taskToDependOn.getLabel() + " " + WizardMessages.constraint_wizard_eventtask_already_dependson_postfix;
                }
                if (workPackage instanceof Xproject) {
                    if (task.getContainedIn().equals(workPackage)) {
                        DatedItemType datedItemType = datedItemTypes.get(new Integer(selectionIndex));
                        if ((datedItemType != DatedItemType.TARGET_START) && (datedItemType != DatedItemType.TARGET_END)) {
                            errorMessage = WizardMessages.constraint_wizard_dependency_on_project;
                        }
                    }
                }
            }
        }
        if (taskEvent && (workPackageWrapper == null)) {
            errorMessage = WizardMessages.constraint_wizard_no_task_event;
        }
        if (errorMessage.length() > 0) {
            this.setErrorMessage(errorMessage);
        }
        setPageComplete(errorMessage.length() == 0);
    }

    /**
     * Setting up the wizard page for Abbot
     *
     * N.B: not all the TestHarness is set up here, for the dynamic widgets
     * their TestHarness is registered when created - see setUpDynamicComposite.
     */
    private void setupTestHarness() {
        TestHarness.name(calendarDateButton, TestHarness.CONSTRAINTWIZARD_CALENDARBUTTON);
        TestHarness.name(taskEventButton, TestHarness.CONSTRAINTWIZARD_TASKEVENTBUTTON);
    }

    @Override
    public boolean save() {
        WorkPackage workPackage = null;
        DatedItemType datedItemType = datedItemTypes.get(new Integer(selectionIndex));
        try {
            if (constraint == null) {
                if (taskEvent && (workPackageWrapper != null)) {
                    workPackage = (WorkPackage) workPackageWrapper.getElement();
                    newConstraint = ElementCreationFactory.getInstance().createConstraint(task, datedItemType, calendarDay, workPackage, offSet, onStart);
                } else {
                    datedItemType = DatedItemType.CALENDAR_DATE;
                    newConstraint = ElementCreationFactory.getInstance().createConstraint(task, datedItemType, calendarDay, workPackage, 0, onStart);
                }
                setDirty(true);
            } else {
                if (taskEvent && (workPackageWrapper != null)) {
                    workPackage = (WorkPackage) workPackageWrapper.getElement();
                    if (workPackage != constraint.getWorkPackage()) {
                        constraint.setWorkPackage(workPackage);
                        setDirty(true);
                    }
                    if (datedItemType != constraint.getDatedItemType()) {
                        constraint.setDatedItemType(datedItemType);
                        setDirty(true);
                    }
                    if (offSet != constraint.getOffset()) {
                        constraint.setOffset(offSet);
                        setDirty(true);
                    }
                } else {
                    if (!constraint.getCalendarDay().equals(calendarDay)) {
                        constraint.setCalendarDay(calendarDay);
                        setDirty(true);
                    }
                }
            }
        } catch (CyclicConstraintException e) {
            setErrorMessage(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void cancel() {
        if (newConstraint != null) {
            task.removeConstraint(newConstraint);
            IElementWrapper elementWrapper = ChangeEventFactory.startChangeRecording(task);
            ChangeEventFactory.addChange(elementWrapper, ChangeEvent.NEW_ELEMENT);
            ChangeEventFactory.addPropertyChange(elementWrapper, "CONSTRAINTS");
            ChangeEventFactory.saveChanges();
            ChangeEventFactory.stopChangeRecording();
        }
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public Constraint getConstraint() {
        if (constraint == null) {
            return newConstraint;
        }
        return constraint;
    }

    private void checkOffset() {
        if (offsetChanged || offSetSpinner.isDisposed()) {
            return;
        }
        DatedItemType datedItemType = datedItemTypes.get(new Integer(selectionIndex));
        internalChange = true;
        if (datedItemType != DatedItemType.CALENDAR_DATE) {
            if (offSetSpinner.getSelection() == 0) {
                offSetSpinner.setSelection(1);
            }
        } else {
            offSetSpinner.setSelection(0);
        }
        internalChange = false;
    }

    private boolean dependencyCausedByConstraint(Xtask task, Xtask taskToDependOn) {
        for (Constraint constraintToCheck : task.getConstraints()) {
            if (constraintToCheck.getWorkPackage() == taskToDependOn) {
                if (constraintToCheck == constraint) {
                    return true;
                }
            }
        }
        return false;
    }
}
