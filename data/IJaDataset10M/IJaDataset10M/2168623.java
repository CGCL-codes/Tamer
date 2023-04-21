package com.android.ide.eclipse.adt.internal.launch;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestParser;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestParser.Activity;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import java.util.ArrayList;

/**
 * Class for the main launch configuration tab.
 */
public class MainLaunchConfigTab extends AbstractLaunchConfigurationTab {

    /**
     * 
     */
    public static final String LAUNCH_TAB_IMAGE = "mainLaunchTab.png";

    protected static final String EMPTY_STRING = "";

    protected Text mProjText;

    private Button mProjButton;

    private Combo mActivityCombo;

    private final ArrayList<Activity> mActivities = new ArrayList<Activity>();

    private WidgetListener mListener = new WidgetListener();

    private Button mDefaultActionButton;

    private Button mActivityActionButton;

    private Button mDoNothingActionButton;

    private int mLaunchAction = LaunchConfigDelegate.DEFAULT_LAUNCH_ACTION;

    private ProjectChooserHelper mProjectChooserHelper;

    /**
     * A listener which handles widget change events for the controls in this
     * tab.
     */
    private class WidgetListener implements ModifyListener, SelectionListener {

        public void modifyText(ModifyEvent e) {
            IProject project = checkParameters();
            loadActivities(project);
            setDirty(true);
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            Object source = e.getSource();
            if (source == mProjButton) {
                handleProjectButtonSelected();
            } else {
                checkParameters();
            }
        }
    }

    public MainLaunchConfigTab() {
    }

    public void createControl(Composite parent) {
        mProjectChooserHelper = new ProjectChooserHelper(parent.getShell());
        Font font = parent.getFont();
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);
        GridLayout topLayout = new GridLayout();
        topLayout.verticalSpacing = 0;
        comp.setLayout(topLayout);
        comp.setFont(font);
        createProjectEditor(comp);
        createVerticalSpacer(comp, 1);
        Group group = new Group(comp, SWT.NONE);
        group.setText("Launch Action:");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setFont(font);
        mDefaultActionButton = new Button(group, SWT.RADIO);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        mDefaultActionButton.setLayoutData(gd);
        mDefaultActionButton.setText("Launch Default Activity");
        mDefaultActionButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (mDefaultActionButton.getSelection() == true) {
                    mLaunchAction = LaunchConfigDelegate.ACTION_DEFAULT;
                    mActivityCombo.setEnabled(false);
                    checkParameters();
                }
            }
        });
        mActivityActionButton = new Button(group, SWT.RADIO);
        mActivityActionButton.setText("Launch:");
        mActivityActionButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (mActivityActionButton.getSelection() == true) {
                    mLaunchAction = LaunchConfigDelegate.ACTION_ACTIVITY;
                    mActivityCombo.setEnabled(true);
                    checkParameters();
                }
            }
        });
        mActivityCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        mActivityCombo.setLayoutData(gd);
        mActivityCombo.clearSelection();
        mActivityCombo.setEnabled(false);
        mActivityCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkParameters();
            }
        });
        mDoNothingActionButton = new Button(group, SWT.RADIO);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        mDoNothingActionButton.setLayoutData(gd);
        mDoNothingActionButton.setText("Do Nothing");
        mDoNothingActionButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (mDoNothingActionButton.getSelection() == true) {
                    mLaunchAction = LaunchConfigDelegate.ACTION_DO_NOTHING;
                    mActivityCombo.setEnabled(false);
                    checkParameters();
                }
            }
        });
    }

    public String getName() {
        return "Android";
    }

    @Override
    public Image getImage() {
        return AdtPlugin.getImageLoader().loadImage(LAUNCH_TAB_IMAGE, null);
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, mProjText.getText());
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_ALLOW_TERMINATE, true);
        configuration.setAttribute(LaunchConfigDelegate.ATTR_LAUNCH_ACTION, mLaunchAction);
        int selection = mActivityCombo.getSelectionIndex();
        if (mActivities != null && selection >= 0 && selection < mActivities.size()) {
            configuration.setAttribute(LaunchConfigDelegate.ATTR_ACTIVITY, mActivities.get(selection).getName());
        }
        mapResources(configuration);
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(LaunchConfigDelegate.ATTR_LAUNCH_ACTION, LaunchConfigDelegate.DEFAULT_LAUNCH_ACTION);
    }

    /**
     * Creates the widgets for specifying a main type.
     *
     * @param parent the parent composite
     */
    protected void createProjectEditor(Composite parent) {
        Font font = parent.getFont();
        Group group = new Group(parent, SWT.NONE);
        group.setText("Project:");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setFont(font);
        mProjText = new Text(group, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        mProjText.setLayoutData(gd);
        mProjText.setFont(font);
        mProjText.addModifyListener(mListener);
        mProjButton = createPushButton(group, "Browse...", null);
        mProjButton.addSelectionListener(mListener);
    }

    /**
     * returns the default listener from this class. For all subclasses this
     * listener will only provide the functi Jaonality of updating the current
     * tab
     *
     * @return a widget listener
     */
    protected WidgetListener getDefaultListener() {
        return mListener;
    }

    /**
     * Return the {@link IJavaProject} corresponding to the project name in the project
     * name text field, or null if the text does not match a project name.
     * @param javaModel the Java Model object corresponding for the current workspace root.
     * @return a IJavaProject object or null.
     */
    protected IJavaProject getJavaProject(IJavaModel javaModel) {
        String projectName = mProjText.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        return javaModel.getJavaProject(projectName);
    }

    /**
     * Show a dialog that lets the user select a project. This in turn provides
     * context for the main type, allowing the user to key a main type name, or
     * constraining the search for main types to the specified project.
     */
    protected void handleProjectButtonSelected() {
        IJavaProject javaProject = mProjectChooserHelper.chooseJavaProject(mProjText.getText().trim());
        if (javaProject == null) {
            return;
        }
        String projectName = javaProject.getElementName();
        mProjText.setText(projectName);
        IProject project = javaProject.getProject();
        loadActivities(project);
    }

    /**
     * Initializes this tab's controls with values from the given
     * launch configuration. This method is called when
     * a configuration is selected to view or edit, after this
     * tab's control has been created.
     * 
     * @param config launch configuration
     * 
     * @see ILaunchConfigurationTab
     */
    public void initializeFrom(ILaunchConfiguration config) {
        String projectName = EMPTY_STRING;
        try {
            projectName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, EMPTY_STRING);
        } catch (CoreException ce) {
        }
        mProjText.setText(projectName);
        IProject proj = mProjectChooserHelper.getAndroidProject(projectName);
        loadActivities(proj);
        mLaunchAction = LaunchConfigDelegate.DEFAULT_LAUNCH_ACTION;
        try {
            mLaunchAction = config.getAttribute(LaunchConfigDelegate.ATTR_LAUNCH_ACTION, mLaunchAction);
        } catch (CoreException e) {
        }
        mDefaultActionButton.setSelection(mLaunchAction == LaunchConfigDelegate.ACTION_DEFAULT);
        mActivityActionButton.setSelection(mLaunchAction == LaunchConfigDelegate.ACTION_ACTIVITY);
        mDoNothingActionButton.setSelection(mLaunchAction == LaunchConfigDelegate.ACTION_DO_NOTHING);
        String activityName = EMPTY_STRING;
        try {
            activityName = config.getAttribute(LaunchConfigDelegate.ATTR_ACTIVITY, EMPTY_STRING);
        } catch (CoreException ce) {
        }
        if (mLaunchAction != LaunchConfigDelegate.ACTION_ACTIVITY) {
            mActivityCombo.setEnabled(false);
            mActivityCombo.clearSelection();
        } else {
            mActivityCombo.setEnabled(true);
            if (activityName == null || activityName.equals(EMPTY_STRING)) {
                mActivityCombo.clearSelection();
            } else if (mActivities != null && mActivities.size() > 0) {
                boolean found = false;
                for (int i = 0; i < mActivities.size(); i++) {
                    if (activityName.equals(mActivities.get(i).getName())) {
                        found = true;
                        mActivityCombo.select(i);
                        break;
                    }
                }
                if (found == false) {
                    mActivityCombo.clearSelection();
                }
            }
        }
    }

    /**
     * Associates the launch config and the project. This allows Eclipse to delete the launch
     * config when the project is deleted.
     *
     * @param config the launch config working copy.
     */
    protected void mapResources(ILaunchConfigurationWorkingCopy config) {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IJavaModel javaModel = JavaCore.create(workspaceRoot);
        IJavaProject javaProject = getJavaProject(javaModel);
        IResource[] resources = null;
        if (javaProject != null) {
            resources = AndroidLaunchController.getResourcesToMap(javaProject.getProject());
        }
        config.setMappedResources(resources);
    }

    /**
     * Loads the ui with the activities of the specified project, and stores the
     * activities in <code>mActivities</code>.
     * <p/>
     * First activity is selected by default if present.
     * 
     * @param project The project to load the activities from.
     */
    private void loadActivities(IProject project) {
        if (project != null) {
            try {
                AndroidManifestParser manifestParser = AndroidManifestParser.parse(BaseProjectHelper.getJavaProject(project), null, true, false);
                if (manifestParser != null) {
                    Activity[] activities = manifestParser.getActivities();
                    mActivities.clear();
                    mActivityCombo.removeAll();
                    for (Activity activity : activities) {
                        if (activity.isExported() && activity.hasAction()) {
                            mActivities.add(activity);
                            mActivityCombo.add(activity.getName());
                        }
                    }
                    if (mActivities.size() > 0) {
                        if (mLaunchAction == LaunchConfigDelegate.ACTION_ACTIVITY) {
                            mActivityCombo.setEnabled(true);
                        }
                    } else {
                        mActivityCombo.setEnabled(false);
                    }
                    mActivityCombo.clearSelection();
                    return;
                }
            } catch (CoreException e) {
            }
        }
        mActivityCombo.removeAll();
        mActivities.clear();
    }

    /**
     * Checks the parameters for correctness, and update the error message and buttons.
     * @return the current IProject of this launch config.
     */
    private IProject checkParameters() {
        try {
            String text = mProjText.getText();
            if (text.length() == 0) {
                setErrorMessage("Project Name is required!");
            } else if (text.matches("[a-zA-Z0-9_ \\.-]+") == false) {
                setErrorMessage("Project name contains unsupported characters!");
            } else {
                IJavaProject[] projects = mProjectChooserHelper.getAndroidProjects(null);
                IProject found = null;
                for (IJavaProject javaProject : projects) {
                    if (javaProject.getProject().getName().equals(text)) {
                        found = javaProject.getProject();
                        break;
                    }
                }
                if (found != null) {
                    setErrorMessage(null);
                } else {
                    setErrorMessage(String.format("There is no android project named '%1$s'", text));
                }
                return found;
            }
        } finally {
            updateLaunchConfigurationDialog();
        }
        return null;
    }
}
