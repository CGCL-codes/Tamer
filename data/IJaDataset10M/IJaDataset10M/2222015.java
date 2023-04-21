package com.ibm.celldt.debug.launch.ui.tabs;

import java.net.URL;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.ibm.celldt.debug.CellDebugPlugin;
import com.ibm.celldt.debug.launch.CellDebugRemoteDebugger;
import com.ibm.celldt.debug.launch.ICellDebugLaunchConstants;
import com.ibm.celldt.debug.ui.DebugUiPlugin;
import com.ibm.celldt.environment.EnvironmentPlugin;

/**
 * 
 * @author Ricardo M. Matinata
 * @since 1.1
 */
public class CellTargetEnvironmentTab extends AbstractLaunchConfigurationTab {

    private Combo configs;

    private Combo dbgConfigs;

    private Text dbgBin;

    private Text dbgPort;

    private Text wdir;

    private Button remoteLaunch;

    private boolean canFinish = false;

    private String errorMsg1 = "Please select a Target";

    private String errorMsg2 = "Please select a Remote Debugger Configuration";

    private String errorMsg3 = "Please select a Valid Debugger Binary Name";

    private String errorMsg4 = "Please select a Valid Debugger Port Name";

    private String errorMsg5 = "Invalid Port Value";

    private int selection = -1;

    private int dbgSelection = -1;

    private String dbgId;

    public CellTargetEnvironmentTab() {
        super();
    }

    class CheckModifyListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
            updateLaunchConfigurationDialog();
        }

        public void widgetSelected(SelectionEvent e) {
            updateLaunchConfigurationDialog();
        }
    }

    private void verify() {
        canFinish = true;
        selection = configs.getSelectionIndex();
        if (!(selection > -1)) {
            canFinish = false;
            setErrorMessage(errorMsg1);
        }
        if (remoteLaunch.getSelection()) {
            wdir.setEnabled(true);
            dbgConfigs.setEnabled(true);
            dbgBin.setEnabled(true);
            dbgPort.setEnabled(true);
            int oldSelection = dbgSelection;
            dbgSelection = dbgConfigs.getSelectionIndex();
            if (dbgSelection > -1) {
                if (oldSelection != dbgSelection) {
                    String current = dbgConfigs.getItem(dbgSelection);
                    CellDebugRemoteDebugger dbg = CellDebugPlugin.getDefault().getRemoteDbgConfigByName(current);
                    dbgId = dbg.getDebuggerId();
                    dbgBin.setText(dbg.getDebugConfig().getDbgBinaryName());
                    dbgPort.setText(dbg.getDebugConfig().getDbgPort());
                } else {
                    String value = dbgBin.getText();
                    if (value != null && !value.equals("")) {
                        value = null;
                        value = dbgPort.getText();
                        if (value == null) {
                            canFinish = false;
                            setErrorMessage(errorMsg4);
                        } else if (value.equals("")) {
                            canFinish = false;
                            setErrorMessage(errorMsg4);
                        } else {
                            try {
                                Integer.valueOf(value);
                            } catch (NumberFormatException ex) {
                                canFinish = false;
                                setErrorMessage(errorMsg5);
                            }
                        }
                    } else {
                        canFinish = false;
                        setErrorMessage(errorMsg3);
                    }
                }
            } else {
                canFinish = false;
                setErrorMessage(errorMsg2);
            }
        } else {
            wdir.setEnabled(false);
            dbgConfigs.setEnabled(false);
            dbgBin.setEnabled(false);
            dbgPort.setEnabled(false);
        }
    }

    public void createControl(Composite parent) {
        GridLayout topLayout = new GridLayout();
        Composite topControl = new Composite(parent, SWT.NONE);
        topControl.setLayout(topLayout);
        setControl(topControl);
        createTargetEnvGroup(topControl);
        createBooleanGroup(topControl);
        createDbgSelectGroup(topControl);
        createRemoteToolsGroup(topControl);
        CheckModifyListener check = new CheckModifyListener();
        configs.addSelectionListener(check);
        dbgConfigs.addSelectionListener(check);
        remoteLaunch.addSelectionListener(check);
        ModifyListener ml = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateLaunchConfigurationDialog();
            }
        };
        wdir.addModifyListener(ml);
        dbgBin.addModifyListener(ml);
        dbgPort.addModifyListener(ml);
    }

    private void createBooleanGroup(Composite parent) {
        GridLayout groupLayout = new GridLayout();
        groupLayout.verticalSpacing = 3;
        groupLayout.horizontalSpacing = 3;
        groupLayout.numColumns = 3;
        groupLayout.makeColumnsEqualWidth = false;
        Group group = new Group(parent, SWT.NONE);
        group.setText("Target Environment Enablement");
        group.setLayout(groupLayout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        remoteLaunch = createCheckButton(group, "Launch gdbserver");
    }

    private void createDbgSelectGroup(Composite parent) {
        GridLayout groupLayout = new GridLayout();
        groupLayout.verticalSpacing = 3;
        groupLayout.horizontalSpacing = 3;
        groupLayout.numColumns = 1;
        groupLayout.makeColumnsEqualWidth = false;
        Group group = new Group(parent, SWT.NONE);
        group.setText("gdbserver Configuration");
        group.setLayout(groupLayout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        dbgConfigs = createShortDropDownRow(group, "gdbserver Selection");
        GridLayout textLayout = new GridLayout();
        textLayout.verticalSpacing = 3;
        textLayout.horizontalSpacing = 3;
        textLayout.numColumns = 1;
        textLayout.makeColumnsEqualWidth = false;
        Composite comp = new Composite(group, SWT.NONE);
        comp.setLayout(textLayout);
        comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        dbgBin = createTextRow(comp, "gdbserver Binary", null);
        dbgPort = createTextRow(comp, "gdbserver Port", null);
    }

    private void createTargetEnvGroup(Composite parent) {
        GridLayout groupLayout = new GridLayout();
        groupLayout.verticalSpacing = 3;
        groupLayout.horizontalSpacing = 3;
        groupLayout.numColumns = 1;
        groupLayout.makeColumnsEqualWidth = false;
        Group group = new Group(parent, SWT.NONE);
        group.setText("Target Environment Configuration Selection");
        group.setLayout(groupLayout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        configs = createShortDropDownRow(group, "Target");
    }

    public static Combo createShortDropDownRow(Composite parent, String labelString) {
        GridLayout rowLayout = new GridLayout();
        rowLayout.verticalSpacing = 1;
        rowLayout.marginWidth = 0;
        rowLayout.marginHeight = 0;
        rowLayout.numColumns = 2;
        Composite row = new Composite(parent, SWT.NONE);
        row.setLayout(rowLayout);
        row.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label label = new Label(row, SWT.NONE);
        label.setText(labelString + ":");
        Combo combo = new Combo(row, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return combo;
    }

    public static Text createTextRow(Composite parent, String labelString, String valueString) {
        GridLayout rowLayout = new GridLayout();
        rowLayout.verticalSpacing = 1;
        rowLayout.marginWidth = 0;
        rowLayout.marginHeight = 0;
        rowLayout.numColumns = 2;
        Composite row = new Composite(parent, SWT.NONE);
        row.setLayout(rowLayout);
        row.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label label = new Label(row, SWT.NONE);
        label.setText(labelString + ":");
        Text text = new Text(row, SWT.SINGLE | SWT.BORDER);
        if (valueString == null) valueString = "";
        text.setText(valueString);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return text;
    }

    private void createRemoteToolsGroup(Composite parent) {
        GridLayout groupLayout = new GridLayout();
        groupLayout.verticalSpacing = 3;
        groupLayout.horizontalSpacing = 3;
        groupLayout.numColumns = 1;
        groupLayout.makeColumnsEqualWidth = false;
        Group group = new Group(parent, SWT.NONE);
        group.setText("Target Environment Deploy settings");
        group.setLayout(groupLayout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        wdir = createTextRow(group, "Remote Working Dir", null);
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        initTargetEnv(configuration);
        initRemoteLaunchConfig(configuration);
        initDbgConfig(configuration);
        verify();
    }

    private void initTargetEnv(ILaunchConfiguration configuration) {
        configs.removeAll();
        String current;
        try {
            current = configuration.getAttribute(ICellDebugLaunchConstants.TARGET_ENV_SELECTED, "");
        } catch (CoreException e) {
            current = "";
        }
        int selected = -1;
        String[] targets = EnvironmentPlugin.getDefault().getTargetsManager().getAllConfigNames();
        for (int i = 0; i < targets.length; i++) {
            configs.add(targets[i]);
            if (current.equals(targets[i])) {
                selected = i;
            }
        }
        if (selected > -1) {
            configs.select(selected);
            selection = selected;
        }
    }

    private void initRemoteLaunchConfig(ILaunchConfiguration configuration) {
        int remote = 1;
        try {
            remote = configuration.getAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH, 1);
        } catch (CoreException e) {
            remote = 1;
        }
        if (remote > 0) {
            remoteLaunch.setSelection(true);
        } else {
            remoteLaunch.setSelection(false);
        }
        String current = "";
        try {
            current = configuration.getAttribute(ICellDebugLaunchConstants.TARGET_WDIR, "");
        } catch (CoreException e) {
            current = "";
        }
        wdir.setText(current);
    }

    private void initDbgConfig(ILaunchConfiguration configuration) {
        dbgConfigs.removeAll();
        String current;
        try {
            current = configuration.getAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_CFG, "");
        } catch (CoreException e) {
            current = "";
        }
        int selected = -1;
        CellDebugPlugin plugin = CellDebugPlugin.getDefault();
        String[] targets = plugin.getRemoteDbgNames();
        for (int i = 0; i < targets.length; i++) {
            dbgConfigs.add(targets[i]);
            if (current.equals(targets[i])) {
                selected = i;
            }
        }
        if (selected > -1) {
            dbgConfigs.select(selected);
            dbgSelection = selected;
            try {
                dbgBin.setText(configuration.getAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_BINARY, CellDebugPlugin.getDefault().getRemoteDbgConfigByName(current).getDebugConfig().getDbgBinaryName()));
            } catch (CoreException e) {
            }
            try {
                dbgPort.setText(Integer.toString(configuration.getAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_PORT, Integer.valueOf(CellDebugPlugin.getDefault().getRemoteDbgConfigByName(current).getDebugConfig().getDbgPort()).intValue())));
            } catch (CoreException e) {
            }
            dbgId = CellDebugPlugin.getDefault().getRemoteDbgConfigByName(current).getDebuggerId();
        }
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        if (selection > -1) configuration.setAttribute(ICellDebugLaunchConstants.TARGET_ENV_SELECTED, configs.getItem(selection));
        configuration.setAttribute(ICellDebugLaunchConstants.TARGET_WDIR, wdir.getText());
        configuration.setAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH, remoteLaunch.getSelection() ? 1 : 0);
        configuration.setAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_BINARY, dbgBin.getText());
        try {
            configuration.setAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_PORT, Integer.valueOf(dbgPort.getText()).intValue());
        } catch (NumberFormatException ex) {
            configuration.setAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_PORT, 0);
        }
        configuration.setAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_DBGID, dbgId);
        if (dbgSelection > -1) configuration.setAttribute(ICellDebugLaunchConstants.TARGET_REMOTELAUNCH_SELECTED_CFG, dbgConfigs.getItem(dbgSelection));
    }

    public String getName() {
        return "Target Selection";
    }

    public Image getImage() {
        URL url = DebugUiPlugin.getDefault().getBundle().getEntry("/icons/iprocess.gif");
        ImageDescriptor imageMon = ImageDescriptor.createFromURL(url);
        return imageMon.createImage();
    }

    public boolean canSave() {
        return super.canSave();
    }

    public boolean isValid(ILaunchConfiguration launchConfig) {
        setErrorMessage(null);
        setMessage(null);
        verify();
        if (super.isValid(launchConfig)) {
            if (canFinish) {
                return true;
            }
        }
        return false;
    }
}
