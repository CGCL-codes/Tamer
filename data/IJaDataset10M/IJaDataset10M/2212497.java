package com.luxoft.fitpro.plugin.launcher.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import com.luxoft.fitpro.plugin.dialogs.ElementDialog;
import com.luxoft.fitpro.plugin.messages.PluginMessages;
import com.luxoft.fitpro.plugin.properties.DefaultFolderProperties;

/**
 * Java launch configuration tab for Fit.
 */
public class FITRunTab extends JavaLaunchTab {

    /**
     * String representing the target attribute.
     */
    public static final String ATTR_TARGET = "FIT_TARGET";

    /**
     * String representing the source attribute.
     */
    public static final String ATTR_SOURCE = "FIT_SOURCE";

    /**
     * Empty string.
     */
    protected static final String EMPTY_STRING = "";

    /**
     * Project label.
     */
    private Label projectLabel;

    /**
     * User modifiable project text.
     */
    private Text projectText;

    /**
     * Target label.
     */
    private Label targetLabel;

    /**
     * User modifiable target text.
     */
    private Text targetText;

    /**
     * Source label.
     */
    private Label sourceLabel;

    /**
     * User modifiable source text.
     */
    private Text sourceText;

    /**
     * Project button.
     */
    private Button projectButton;

    /**
     * Result path button.
     */
    private Button resultPathButton;

    /**
     * Source path button.
     */
    private Button sourcePathButton;

    /**
     * Listener used to validate page when text is modified.
     */
    private class ModifyTextListener implements ModifyListener {

        /**
         * Validate page when text is modified.
         * 
         * @param event an event containing information about the modify
         */
        public void modifyText(ModifyEvent event) {
            validatePage();
        }
    }

    /**
     * Generic listener used to valid page when text is modified.
     */
    private final ModifyTextListener textListener = new ModifyTextListener();

    /**
     * @param parent folder where Fit HTML source is located
     */
    public final void createControl(final Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);
        GridLayout topLayout = new GridLayout();
        comp.setLayout(topLayout);
        GridData gridData;
        Composite pathComp = new Composite(comp, SWT.NONE);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        pathComp.setLayoutData(gridData);
        GridLayout pathLayout = new GridLayout();
        pathLayout.marginHeight = 0;
        pathLayout.marginWidth = 0;
        pathLayout.numColumns = 3;
        pathComp.setLayout(pathLayout);
        projectLabel = new Label(pathComp, SWT.NONE);
        projectLabel.setText(PluginMessages.getMessage("fit.runner.project"));
        projectText = new Text(pathComp, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        projectText.setLayoutData(gridData);
        projectText.addModifyListener(textListener);
        projectButton = createPushButton(pathComp, PluginMessages.getMessage("fit.runner.browse"), null);
        projectButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                handleProjectButtonSelected();
            }
        });
        targetLabel = new Label(pathComp, SWT.NONE);
        targetLabel.setText(PluginMessages.getMessage("fit.runner.result_folder"));
        targetText = new Text(pathComp, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        targetText.setLayoutData(gridData);
        targetText.addModifyListener(textListener);
        resultPathButton = createPushButton(pathComp, PluginMessages.getMessage("fit.runner.browse"), null);
        resultPathButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent event) {
            }

            public void widgetSelected(SelectionEvent event) {
                String folder = selectFileOrFolder(true);
                if (folder != null) {
                    targetText.setText(folder);
                }
            }
        });
        sourceLabel = new Label(pathComp, SWT.NONE);
        sourceLabel.setText(PluginMessages.getMessage("fit.runner.source"));
        sourceText = new Text(pathComp, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        sourceText.setLayoutData(gridData);
        sourceText.addModifyListener(textListener);
        sourcePathButton = createPushButton(pathComp, PluginMessages.getMessage("fit.runner.browse"), null);
        sourcePathButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent event) {
            }

            public void widgetSelected(SelectionEvent event) {
                String folder = selectFileOrFolder(false);
                if (folder != null) {
                    sourceText.setText(folder);
                }
            }
        });
        String inlineHelp = PluginMessages.getMessage("fit.runner.src_can_be_a_Fit_test_file_folder_of_Fit_tsts_or_suite");
        Label help = new Label(pathComp, SWT.NONE);
        help.setText(inlineHelp);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        help.setLayoutData(gridData);
        Dialog.applyDialogFont(parent);
    }

    /**
     * Sets the name of the project text after project is selected.
     */
    private void handleProjectButtonSelected() {
        IJavaProject project = selectJavaProject();
        if (project != null) {
            projectText.setText(project.getElementName());
            targetText.setText(DefaultFolderProperties.getDefaultOutputLocation(project.getResource()));
        }
    }

    /**
     * Returns name of launch configuration tab.
     * 
     * @return name of launch configuration tab.
     */
    public String getName() {
        return PluginMessages.getMessage("fit.runner.fit_runner");
    }

    /**
     * {@inheritDoc}
     */
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectText.getText());
        configuration.setAttribute(ATTR_TARGET, targetText.getText());
        configuration.setAttribute(ATTR_SOURCE, sourceText.getText());
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        IJavaElement javaElement = getContext();
        if (javaElement == null) {
            configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
        } else {
            initializeJavaProject(javaElement, configuration);
            configuration.setAttribute(ATTR_TARGET, DefaultFolderProperties.getDefaultOutputLocation(javaElement.getJavaProject().getResource()));
        }
        configuration.setAttribute(ATTR_SOURCE, EMPTY_STRING);
    }

    /**
     * {@inheritDoc}
     */
    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            String project = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
            projectText.setText(project);
        } catch (CoreException ce) {
            projectText.setText(null);
        }
        try {
            String target = configuration.getAttribute(ATTR_TARGET, EMPTY_STRING);
            targetText.setText(target);
        } catch (CoreException ce) {
            targetText.setText(EMPTY_STRING);
        }
        try {
            String source = configuration.getAttribute(ATTR_SOURCE, EMPTY_STRING);
            sourceText.setText(source);
        } catch (CoreException ce) {
            sourceText.setText(EMPTY_STRING);
        }
    }

    /**
     * Returns the project given the project's name.
     * 
     * @param name the name of the project
     * @return project
     */
    public static IProject getProject(String name) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    }

    /**
     * Handles selecting and returning a java project.
     * 
     * @return java project
     */
    private IJavaProject selectJavaProject() {
        IJavaProject[] projects;
        try {
            projects = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
        } catch (JavaModelException e) {
            projects = new IJavaProject[0];
        }
        ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
        dialog.setTitle(PluginMessages.getMessage("fit.runner.run_fit_tests"));
        dialog.setMessage(PluginMessages.getMessage("fit.runner.select_a_project"));
        dialog.setElements(projects);
        IJavaProject javaProject = getJavaProject();
        if (javaProject != null) {
            dialog.setInitialSelections(new Object[] { javaProject });
        }
        if (dialog.open() == Window.OK) {
            return (IJavaProject) dialog.getFirstResult();
        }
        return null;
    }

    /**
     * Returns a java project, if valid.
     * 
     * @return java project
     */
    private IJavaProject getJavaProject() {
        String projectName = projectText.getText().trim();
        if (projectName.length() == 0) {
            return null;
        }
        IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
        return javaModel.getJavaProject(projectName);
    }

    /**
     * Validates all user modified information on tab.
     */
    private void validatePage() {
        setMessage(null);
        setErrorMessage(null);
        boolean projectExists = projectExists(projectText.getText());
        resultPathButton.setEnabled(projectExists);
        sourcePathButton.setEnabled(projectExists);
        if (projectExists) {
            sourceExists(sourceText.getText(), PluginMessages.getMessage("fit.runner.please_specify_a_source_file_or_folder"));
            targetExists(targetText.getText(), PluginMessages.getMessage("fit.runner.please_specify_a_target_folder"));
        }
        updateLaunchConfigurationDialog();
        setDirty(false);
    }

    /**
     * Returns true if source file or folder name exists.
     * 
     * @param fileFolderName file of folder name
     * @param errorMessage error message to display
     * @return boolean true if source file or folder name exists
     */
    private boolean sourceExists(String fileFolderName, String errorMessage) {
        if (fileFolderName == null || fileFolderName.equals("")) {
            setErrorMessage(errorMessage);
            return false;
        }
        try {
            IProject project = FITRunTab.getProject(projectText.getText());
            if (!project.exists()) {
                setErrorMessage(errorMessage);
                return false;
            }
            IFolder folder = project.getFolder(fileFolderName);
            if (!folder.exists()) {
                IFile file = project.getFile(fileFolderName);
                if (!file.exists()) {
                    setErrorMessage(PluginMessages.getMessage("fit.runner.src_fl_or_fdr{0}doesn't_exist", fileFolderName));
                    return false;
                }
            }
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    /**
     * Returns true if project name exists.
     * 
     * @param projectName project name
     * @return boolean true if project name exists
     */
    private boolean projectExists(String projectName) {
        if (projectName == null || projectName.trim().equals(EMPTY_STRING)) {
            setErrorMessage(PluginMessages.getMessage("fit.runner.project_not_specified"));
            return false;
        }
        try {
            IProject project = FITRunTab.getProject(projectName);
            if (!project.exists()) {
                setErrorMessage(PluginMessages.getMessage("fit.runner.project_{0}_does_not_exist", projectName));
                return false;
            }
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    /**
     * Returns true if target folder name exists.
     * 
     * @param folderName folder name
     * @param errorMessage error message to display
     * @return boolean true if target folder name exists
     */
    private boolean targetExists(String folderName, String errorMessage) {
        if (folderName == null || folderName.equals("")) {
            setErrorMessage(errorMessage);
            return false;
        }
        try {
            IProject project = FITRunTab.getProject(projectText.getText());
            IFolder folder = project.getFolder(folderName);
            if (!folder.exists()) {
                setErrorMessage(PluginMessages.getMessage("fit.runner.folder_{0}_does_not_exist", folderName));
                return false;
            }
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    /**
     * Return name of selected file or folder.
     * 
     * @param folderOnly flag indicating if provider is dealing with folders only
     * @return name of file or folder
     */
    private String selectFileOrFolder(final boolean folderOnly) {
        IProject project = getProject(projectText.getText().trim());
        ElementDialog dialog = new ElementDialog(getShell(), project, folderOnly);
        dialog.setAllowMultiple(false);
        dialog.setTitle(PluginMessages.getMessage("fit.runner.run_fit_tests"));
        if (folderOnly) {
            dialog.setMessage(PluginMessages.getMessage("fit.runner.select_a_folder"));
        } else {
            dialog.setMessage(PluginMessages.getMessage("fit.runner.select_a_file_or_folder"));
        }
        dialog.setValidator(getSelectionValidator(folderOnly));
        if (dialog.open() == Window.OK) {
            IResource resource = (IResource) dialog.getFirstResult();
            if (resource != null) {
                return resource.getFullPath().removeFirstSegments(1).toString();
            }
        }
        return null;
    }

    /**
     * Returns a selection validator to validate whether selection is a valid file or folder.
     * 
     * @param folderOnly flag indicating whether selection is required to be a folder only
     * @return selection status validator
     */
    private ISelectionStatusValidator getSelectionValidator(final boolean folderOnly) {
        ISelectionStatusValidator validator = new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                final IStatus fgErrorStatus = new Status(IStatus.ERROR, "org.eclipse.core.runtime", 1, "", null);
                final IStatus fgOKStatus = Status.OK_STATUS;
                if (selection.length == 0) {
                    return fgErrorStatus;
                }
                for (int i = 0; i < selection.length; i++) {
                    Object obj = selection[i];
                    if (folderOnly) {
                        if (!(obj instanceof IFolder)) {
                            return fgErrorStatus;
                        }
                    } else {
                        if (!(obj instanceof IFolder) && !(obj instanceof IFile)) {
                            return fgErrorStatus;
                        }
                    }
                }
                return fgOKStatus;
            }
        };
        return validator;
    }
}
