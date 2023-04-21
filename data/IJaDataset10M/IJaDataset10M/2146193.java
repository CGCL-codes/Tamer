package org.echarts.edt.launcher.ui;

import org.echarts.edt.launcher.config.ProjectConfig;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class E4ssLaunchConfigurationTab extends org.eclipse.debug.ui.AbstractLaunchConfigurationTab {

    private Text txtEchartsDistributionHome;

    private Text txtProjectRootDir;

    private Text txtVersion;

    private Text txtPythonExecutable;

    private Text txtRootJavaPackageName;

    private Text txtProjectName;

    @Override
    public void createControl(Composite parent) {
        Composite me = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        me.setLayout(layout);
        me.setLayoutData(gd);
        me.setFont(parent.getFont());
        setControl(me);
        makeLabelControl(me, "ECharts Distribution Home");
        this.txtEchartsDistributionHome = new Text(me, SWT.BORDER);
        makeLabelControl(me, "Project Name");
        this.txtProjectName = new Text(me, SWT.BORDER);
        makeLabelControl(me, "Project Root Dir");
        this.txtProjectRootDir = new Text(me, SWT.BORDER);
        makeLabelControl(me, "Version");
        this.txtVersion = new Text(me, SWT.BORDER);
        makeLabelControl(me, "Python Executable");
        this.txtPythonExecutable = new Text(me, SWT.BORDER);
        makeLabelControl(me, "Root Java Package Name");
        this.txtRootJavaPackageName = new Text(me, SWT.BORDER);
    }

    private void makeLabelControl(Composite me, String labl) {
        Label lblNewLabel1 = new Label(me, SWT.NONE);
        lblNewLabel1.setText(labl);
    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(ProjectConfig.ECHARTS_DISTRIBUTION_HOME, "/Users/srhenderson/projects/att_echarts/EChartsCPL");
        configuration.setAttribute(ProjectConfig.PROJECT_NAME, "examples");
        configuration.setAttribute(ProjectConfig.PROJECT_ROOT_DIR, "/Users/srhenderson/projects/att_echarts/EChartsCPL/runtime/java/src");
        configuration.setAttribute(ProjectConfig.VERSION, "0.0.0.1");
        configuration.setAttribute(ProjectConfig.PYTHON_EXECUTABLE, "/sw/bin/python");
        configuration.setAttribute(ProjectConfig.ROOT_JAVA_PACKAGE_NAME, "examples");
    }

    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        this.txtEchartsDistributionHome.setText(getString(configuration, ProjectConfig.ECHARTS_DISTRIBUTION_HOME));
        this.txtProjectName.setText(getString(configuration, ProjectConfig.PROJECT_NAME));
        this.txtProjectRootDir.setText(getString(configuration, ProjectConfig.PROJECT_ROOT_DIR));
        this.txtVersion.setText(getString(configuration, ProjectConfig.VERSION));
        this.txtPythonExecutable.setText(getString(configuration, ProjectConfig.PYTHON_EXECUTABLE));
        this.txtRootJavaPackageName.setText(getString(configuration, ProjectConfig.ROOT_JAVA_PACKAGE_NAME));
    }

    private String getString(ILaunchConfiguration configuration, String attributeName) {
        try {
            return configuration.getAttribute(attributeName, "");
        } catch (CoreException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        setConfigAttribute(configuration, ProjectConfig.ECHARTS_DISTRIBUTION_HOME, this.txtEchartsDistributionHome);
        setConfigAttribute(configuration, ProjectConfig.PROJECT_NAME, this.txtProjectName);
        setConfigAttribute(configuration, ProjectConfig.PROJECT_ROOT_DIR, this.txtProjectRootDir);
        setConfigAttribute(configuration, ProjectConfig.VERSION, this.txtVersion);
        setConfigAttribute(configuration, ProjectConfig.PYTHON_EXECUTABLE, this.txtPythonExecutable);
        setConfigAttribute(configuration, ProjectConfig.ROOT_JAVA_PACKAGE_NAME, this.txtRootJavaPackageName);
    }

    private void setConfigAttribute(ILaunchConfigurationWorkingCopy configuration, String attributeName, Text txtField) {
        configuration.setAttribute(attributeName, txtField.getText());
    }

    @Override
    public String getName() {
        return "E4SS Runtime Settings";
    }
}
