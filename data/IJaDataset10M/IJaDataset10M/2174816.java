package com.google.gdt.eclipse.designer.launch;

import com.google.gdt.eclipse.designer.common.Constants;
import com.google.gdt.eclipse.designer.common.GwtLabelProvider;
import com.google.gdt.eclipse.designer.util.ModuleDescription;
import com.google.gdt.eclipse.designer.util.Utils;
import com.google.gdt.eclipse.designer.wizards.model.common.AbstractGwtComposite;
import com.google.gdt.eclipse.designer.wizards.model.common.IMessageContainer;
import org.eclipse.wb.internal.core.DesignerPlugin;
import org.eclipse.wb.internal.core.utils.dialogfields.ComboDialogField;
import org.eclipse.wb.internal.core.utils.dialogfields.DialogField;
import org.eclipse.wb.internal.core.utils.dialogfields.DialogFieldUtils;
import org.eclipse.wb.internal.core.utils.dialogfields.IStringButtonAdapter;
import org.eclipse.wb.internal.core.utils.dialogfields.StringButtonDialogField;
import org.eclipse.wb.internal.core.utils.dialogfields.StringButtonDirectoryDialogField;
import org.eclipse.wb.internal.core.utils.ui.GridDataFactory;
import org.eclipse.wb.internal.core.utils.ui.GridLayoutFactory;
import org.eclipse.wb.internal.core.utils.ui.UiUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import java.util.List;

/**
 * @author lobas_av
 * @coverage gwt.launch
 */
public class CompilerMainTab extends JavaLaunchTab {

    private TabComposite m_composite;

    public void createControl(Composite parent) {
        m_composite = new TabComposite(parent, SWT.NONE, m_messageContainer);
        setControl(m_composite);
    }

    private static class TabComposite extends AbstractGwtComposite {

        private final StringButtonDialogField m_projectField;

        private final StringButtonDialogField m_moduleField;

        private final ComboDialogField m_logLevelField;

        private final StringButtonDirectoryDialogField m_dirGenField;

        private final StringButtonDirectoryDialogField m_dirOutField;

        private final ComboDialogField m_compilerStyleField;

        public TabComposite(Composite parent, int style, IMessageContainer messageContainer) {
            super(parent, style, messageContainer);
            int numColumns = 3;
            GridLayoutFactory.create(this).columns(numColumns);
            {
                m_projectField = new StringButtonDialogField(new IStringButtonAdapter() {

                    public void changeControlPressed(DialogField field) {
                        chooseProject();
                    }
                });
                m_projectField.setDialogFieldListener(m_validateListener);
                m_projectField.setLabelText("Project:");
                m_projectField.setButtonLabel("&Browse...");
                DialogFieldUtils.fillControls(this, m_projectField, numColumns, 60);
            }
            {
                m_moduleField = new StringButtonDialogField(new IStringButtonAdapter() {

                    public void changeControlPressed(DialogField field) {
                        chooseModule();
                    }
                });
                m_moduleField.setDialogFieldListener(m_validateListener);
                m_moduleField.setButtonLabel("&Search...");
                doCreateField(this, m_moduleField, numColumns, "Module:", null);
            }
            {
                Group compilerGroup = new Group(this, SWT.NONE);
                GridLayoutFactory.create(compilerGroup).columns(numColumns);
                GridDataFactory.create(compilerGroup).spanH(numColumns).fillH();
                compilerGroup.setText("GWTCompiler flags (hover over label for description)");
                {
                    m_logLevelField = new ComboDialogField(SWT.READ_ONLY);
                    m_logLevelField.setItems(new String[] { "ERROR", "WARN", "INFO", "TRACE", "DEBUG", "SPAM", "ALL" });
                    doCreateField(compilerGroup, m_logLevelField, numColumns, "-logLevel", "The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM, or ALL");
                    UiUtils.setVisibleItemCount(m_logLevelField.getComboControl(null), 10);
                }
                {
                    m_dirGenField = new StringButtonDirectoryDialogField();
                    doCreateField(compilerGroup, m_dirGenField, numColumns, "-gen", "The directory into which generated files will be written for review");
                }
                {
                    m_dirOutField = new StringButtonDirectoryDialogField();
                    doCreateField(compilerGroup, m_dirOutField, numColumns, "-out", "The directory to write output files into (defaults to current)");
                }
                {
                    m_compilerStyleField = new ComboDialogField(SWT.READ_ONLY);
                    m_compilerStyleField.setItems(new String[] { "OBFUSCATED", "PRETTY", "DETAILED" });
                    doCreateField(compilerGroup, m_compilerStyleField, numColumns, "-style", "Script output style: OBFUSCATED, PRETTY, or DETAILED (defaults to OBFUSCATED)");
                }
            }
        }

        private void doCreateField(Composite parent, DialogField field, int columns, String label, String tooltip) {
            field.setLabelText(label);
            field.setDialogFieldListener(m_validateListener);
            DialogFieldUtils.fillControls(parent, field, columns, 60);
            if (tooltip != null) {
                field.getLabelControl(null).setToolTipText(tooltip);
            }
        }

        @Override
        protected String validate() {
            IJavaProject javaProject = getProject();
            {
                String projectName = m_projectField.getText();
                if (projectName.length() == 0) {
                    return "Enter GWT project name";
                }
                if (javaProject == null) {
                    return "Project " + projectName + " does not exist";
                }
                if (!Utils.isGWTProject(javaProject)) {
                    return "Project " + projectName + " is not GWT project";
                }
            }
            {
                String moduleName = m_moduleField.getText();
                if (moduleName.length() == 0) {
                    return "Enter GWT module";
                }
                try {
                    boolean goodModule = false;
                    {
                        for (ModuleDescription module : Utils.getModules(javaProject)) {
                            if (module.getId().equals(moduleName)) {
                                goodModule = true;
                            }
                        }
                    }
                    if (!goodModule) {
                        return "Module " + moduleName + " does not exist";
                    }
                } catch (Throwable e) {
                    DesignerPlugin.log(e);
                }
            }
            return null;
        }

        /**
     * Choose GWT project using dialog.
     */
        private void chooseProject() {
            ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
            ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), labelProvider);
            dialog.setTitle("Project Selection");
            dialog.setMessage("Select a GWT project:");
            try {
                dialog.setElements(Utils.getGWTProjects().toArray());
            } catch (Throwable e) {
                DesignerPlugin.log(e);
            }
            {
                IJavaProject project = getProject();
                if (project != null) {
                    dialog.setInitialSelections(new Object[] { project });
                }
            }
            if (dialog.open() == Window.OK) {
                IJavaProject project = (IJavaProject) dialog.getFirstResult();
                m_projectField.setText(project.getElementName());
            }
        }

        /**
     * Return the {@link IJavaProject} corresponding to the project name in the project name text
     * field, or <code>null</code> if the text does not match a project name.
     */
        private IJavaProject getProject() {
            String projectName = m_projectField.getText().trim();
            if (projectName.length() == 0) {
                return null;
            }
            IJavaProject javaProject = Utils.getJavaModel().getJavaProject(projectName);
            if (!javaProject.exists()) {
                return null;
            }
            return javaProject;
        }

        /**
     * Choose a module using dialog.
     */
        private void chooseModule() {
            IJavaProject javaProject = getProject();
            if (javaProject == null) {
                return;
            }
            ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), GwtLabelProvider.INSTANCE);
            dialog.setTitle("Module Selection");
            dialog.setMessage("Select a GWT module:");
            try {
                List<ModuleDescription> modules = Utils.getModules(javaProject);
                dialog.setElements(modules.toArray());
            } catch (Throwable e) {
                DesignerPlugin.log(e);
            }
            if (dialog.open() == Window.OK) {
                ModuleDescription moduleDescription = (ModuleDescription) dialog.getFirstResult();
                String moduleId = moduleDescription.getId();
                m_moduleField.setText(moduleId);
            }
        }

        /**
     * Initializes this composite from given configuration.
     */
        public void initializeFrom(ILaunchConfiguration configuration) {
            try {
                m_projectField.setTextWithoutUpdate(configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""));
                m_moduleField.setTextWithoutUpdate(configuration.getAttribute(Constants.LAUNCH_ATTR_MODULE, ""));
                m_logLevelField.setTextWithoutUpdate(configuration.getAttribute(Constants.LAUNCH_ATTR_LOG_LEVEL, ""));
                m_dirGenField.setTextWithoutUpdate(configuration.getAttribute(Constants.LAUNCH_ATTR_DIR_GEN, ""));
                m_dirOutField.setTextWithoutUpdate(configuration.getAttribute(Constants.LAUNCH_ATTR_DIR_WAR, ""));
                m_compilerStyleField.setTextWithoutUpdate(configuration.getAttribute(Constants.LAUNCH_ATTR_STYLE, ""));
                validateAll();
            } catch (Throwable e) {
                DesignerPlugin.log(e);
            }
        }

        /**
     * Updates given configuration using values entered in this composite.
     */
        public void applyTo(ILaunchConfigurationWorkingCopy configuration) {
            mapResources(configuration);
            configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, m_projectField.getText());
            configuration.setAttribute(Constants.LAUNCH_ATTR_MODULE, m_moduleField.getText());
            configuration.setAttribute(Constants.LAUNCH_ATTR_LOG_LEVEL, m_logLevelField.getText());
            configuration.setAttribute(Constants.LAUNCH_ATTR_DIR_GEN, m_dirGenField.getText());
            configuration.setAttribute(Constants.LAUNCH_ATTR_DIR_WAR, m_dirOutField.getText());
            configuration.setAttribute(Constants.LAUNCH_ATTR_STYLE, m_compilerStyleField.getText());
        }

        /**
     * Maps the configuration to associated java project. So, user will be asked about deleting
     * launch configuration during project delete.
     */
        private void mapResources(ILaunchConfigurationWorkingCopy configuration) {
            IJavaProject javaProject = getProject();
            IResource[] resources = null;
            if (javaProject != null) {
                resources = new IResource[] { javaProject.getProject() };
            }
            configuration.setMappedResources(resources);
        }
    }

    /**
   * {@link IMessageContainer} for this launch configuration tab.
   */
    private final IMessageContainer m_messageContainer = new IMessageContainer() {

        public void setErrorMessage(String message) {
            CompilerMainTab.this.setErrorMessage(message);
            CompilerMainTab.this.updateLaunchConfigurationDialog();
        }
    };

    @Override
    public boolean isValid(ILaunchConfiguration launchConfig) {
        return getErrorMessage() == null;
    }

    public String getName() {
        return "Main";
    }

    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
    }

    @Override
    public void initializeFrom(ILaunchConfiguration config) {
        m_composite.initializeFrom(config);
        super.initializeFrom(config);
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        m_composite.applyTo(configuration);
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(Constants.LAUNCH_ATTR_LOG_LEVEL, "INFO");
        configuration.setAttribute(Constants.LAUNCH_ATTR_DIR_GEN, "");
        configuration.setAttribute(Constants.LAUNCH_ATTR_DIR_WAR, "www");
        configuration.setAttribute(Constants.LAUNCH_ATTR_STYLE, "OBFUSCATED");
    }
}
