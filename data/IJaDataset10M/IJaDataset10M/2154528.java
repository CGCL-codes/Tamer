package org.radrails.rails.internal.ui.raketasks;

import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.radrails.rails.core.IRailsConstants;
import org.radrails.rails.internal.core.RailsPlugin;
import org.radrails.rails.internal.ui.RailsUIMessages;
import org.rubypeople.rdt.launching.IVMInstall;
import org.rubypeople.rdt.launching.IVMInstallChangedListener;
import org.rubypeople.rdt.launching.RubyRuntime;

public class RakeTasksView extends ViewPart implements Observer, IVMInstallChangedListener, IPropertyChangeListener {

    private StackLayout fViewLayout;

    private Composite fRakeTasksView;

    private Label fSelectGemView;

    private Label fSelectRailsProjectView;

    private Composite fParent;

    private Combo fTasksCombo;

    private Text fParamText;

    private Text fDescripText;

    private Map fTasks;

    public RakeTasksView() {
        super();
    }

    public void createPartControl(Composite parent) {
        fParent = parent;
        fViewLayout = new StackLayout();
        parent.setLayout(fViewLayout);
        fRakeTasksView = new Composite(parent, SWT.NULL);
        fRakeTasksView.setLayout(new GridLayout(2, false));
        fRakeTasksView.setLayoutData(new GridData(GridData.FILL_BOTH));
        createRakeControls(fRakeTasksView);
        fSelectGemView = new Label(parent, SWT.NULL);
        fSelectGemView.setText(RailsUIMessages.SpecifyRakePath_message);
        fSelectRailsProjectView = new Label(parent, SWT.NULL);
        fSelectRailsProjectView.setText(RailsUIMessages.SelectRailsProject_message);
        if (RailsPlugin.getInstance().getRakePath() == null || RailsPlugin.getInstance().getRakePath().equals("")) {
            fViewLayout.topControl = fSelectGemView;
        } else {
            IProject selected = RakeTasksHelper.getSelectedRailsProject();
            if (selected != null) {
                fViewLayout.topControl = fRakeTasksView;
            } else {
                fViewLayout.topControl = fSelectRailsProjectView;
            }
        }
        parent.layout();
        updateRakeTasks();
        RailsPlugin.getInstance().addProjectObserver(this);
        RubyRuntime.addVMInstallChangedListener(this);
        RailsPlugin.getInstance().getPluginPreferences().addPropertyChangeListener(this);
    }

    protected void createRakeControls(Composite parent) {
        Composite comp = new Composite(parent, SWT.NULL);
        GridLayout compLayout = new GridLayout();
        compLayout.numColumns = 3;
        comp.setLayout(compLayout);
        GridData compLayoutData = new GridData(GridData.FILL_BOTH);
        comp.setLayoutData(compLayoutData);
        fTasksCombo = new Combo(comp, SWT.DROP_DOWN);
        GridData tasksComboData = new GridData();
        tasksComboData.widthHint = 200;
        fTasksCombo.setLayoutData(tasksComboData);
        fTasksCombo.setVisibleItemCount(20);
        fTasksCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                Combo c = (Combo) e.widget;
                String descrip = (String) fTasks.get(c.getText());
                fDescripText.setText(descrip);
            }
        });
        fParamText = new Text(comp, SWT.BORDER);
        GridData paramTextData = new GridData(GridData.FILL_HORIZONTAL);
        fParamText.setLayoutData(paramTextData);
        fParamText.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.character == SWT.CR) {
                    runRakeTask();
                }
            }
        });
        Button genButton = new Button(comp, SWT.PUSH);
        genButton.setText("Go");
        genButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                runRakeTask();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        new Label(comp, SWT.NULL);
        fDescripText = new Text(comp, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
        fDescripText.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    public void dispose() {
        super.dispose();
        RailsPlugin.getInstance().deleteProjectObserver(this);
        RubyRuntime.removeVMInstallChangedListener(this);
    }

    private void runRakeTask() {
        IProject project = RakeTasksHelper.getSelectedRailsProject();
        if (project != null) {
            RakeTasksHelper.runRakeTask(project, fTasksCombo.getText(), fParamText.getText());
        }
    }

    protected void updateRakeTasks() {
        Job j = new Job("Update rake tasks") {

            protected IStatus run(IProgressMonitor monitor) {
                monitor.beginTask("Loading rake tasks", 2);
                PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                    public void run() {
                        fDescripText.setText("Please wait, loading rake tasks...");
                    }
                });
                monitor.worked(1);
                fTasks = RakeTasksHelper.getTasks();
                PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                    public void run() {
                        Collection sortedItems = new TreeSet(fTasks.keySet());
                        fTasksCombo.setItems((String[]) sortedItems.toArray(new String[] {}));
                        fDescripText.setText("");
                    }
                });
                monitor.worked(1);
                monitor.done();
                return Status.OK_STATUS;
            }
        };
        j.schedule();
    }

    public void setFocus() {
        fTasksCombo.setFocus();
    }

    public void update(Observable o, Object arg) {
        if (!fParent.isDisposed()) {
            if (arg == null && RakeTasksHelper.getSelectedRailsProject() == null) {
                fViewLayout.topControl = fSelectRailsProjectView;
            } else if (RailsPlugin.getInstance().getRakePath() == null || RailsPlugin.getInstance().getRakePath().equals("")) {
                fViewLayout.topControl = fSelectGemView;
            } else {
                fViewLayout.topControl = fRakeTasksView;
            }
            fParent.layout();
        }
    }

    public void defaultVMInstallChanged(IVMInstall previous, IVMInstall current) {
        handlePossibleRakeChange(RailsPlugin.getInstance().getRakePath());
    }

    public void vmAdded(IVMInstall newVm) {
    }

    public void vmChanged(org.rubypeople.rdt.launching.PropertyChangeEvent event) {
    }

    public void vmRemoved(IVMInstall removedVm) {
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getProperty().equals(IRailsConstants.PREF_RAKE_PATH)) {
            handlePossibleRakeChange(event.getNewValue());
        }
    }

    private void handlePossibleRakeChange(Object value) {
        if (!fParent.isDisposed()) {
            if (value == null || value.equals("")) {
                fViewLayout.topControl = fSelectGemView;
                fParent.layout();
            } else {
                fViewLayout.topControl = fRakeTasksView;
                fParent.layout();
            }
        }
    }
}
