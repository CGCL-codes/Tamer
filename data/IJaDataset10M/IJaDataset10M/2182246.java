package ca.ucalgary.cpsc.ebe.fitClipse.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import ca.ucalgary.cpsc.ebe.fitClipse.runner.FITTestConfiguration;
import ca.ucalgary.cpsc.ebe.fitClipse.runner.FitManager;
import ca.ucalgary.cpsc.ebe.fitClipse.util.Constants;

public class CheckAsFitProjectClassPathPage extends WizardPage {

    private CheckAsFitProjectWizardController wizardController = null;

    private Group fitSettingsGroup = null;

    private Group classPathGroup = null;

    private List classPathList = null;

    private Button btnAddPath = null;

    private Button btnRemovePath = null;

    private Button btnBrowseClassPath = null;

    private Button btnBrowseTestLocation = null;

    private Button btnBrowseResultLocation = null;

    private Combo comboProjectPath = null;

    private Label lblTestLocation = null;

    private Label lblResultLocation = null;

    private Text txtTestLocation = null;

    private Text txtResultLocation = null;

    String selectedSrcDir = null;

    String SelectedResultDir = null;

    String selectedClassPathDir = null;

    private String filterPath = "C:\\";

    private IStructuredSelection selection;

    private Composite top;

    private Button btnApplyDefault = null;

    protected CheckAsFitProjectClassPathPage(IStructuredSelection selection) {
        super("ClassPath Configuration Page");
        setTitle("Set Class Path for running FIT tests.");
        this.selection = selection;
        IProject project = (IProject) selection.getFirstElement();
        wizardController = new CheckAsFitProjectWizardController(project);
    }

    public void createControl(Composite parent) {
        try {
            addChildControls(parent);
            wizardController.loadClassPath(classPathList, false);
            wizardController.loadTestSourcePath(txtTestLocation, false);
            wizardController.loadTestResultPath(txtResultLocation, false);
            setControl(top);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addChildControls(Composite parent) {
        top = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;
        top.setLayout(layout);
        createClassPathGroup();
        createFitSettingsGroup();
        GridData gridData = new GridData(SWT.None);
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.END;
        btnApplyDefault = new Button(top, SWT.PUSH);
        btnApplyDefault.setLayoutData(gridData);
        btnApplyDefault.setSize(100, 23);
        btnApplyDefault.setText("Restore Default");
        btnApplyDefault.setToolTipText("Apply default settings for Class Path.");
        btnApplyDefault.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                wizardController.loadClassPath(classPathList, true);
                wizardController.loadTestSourcePath(txtTestLocation, true);
                wizardController.loadTestResultPath(txtResultLocation, true);
            }
        });
    }

    private void createClassPathGroup() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.heightHint = -1;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData.horizontalSpan = 2;
        classPathGroup = new Group(top, SWT.NONE);
        classPathGroup.setText("Class Path");
        classPathGroup.setLayoutData(gridData);
        classPathList = new List(classPathGroup, SWT.MULTI | SWT.FILL);
        classPathList.setBounds(new org.eclipse.swt.graphics.Rectangle(15, 15, 288, 187));
        classPathList.setToolTipText(Constants.FITCLIPSE_TOOLTIP_PROPERTIES_FIT_ClassPath);
        btnBrowseClassPath = new Button(classPathGroup, SWT.NONE);
        btnBrowseClassPath.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 210, 80, 19));
        btnBrowseClassPath.setText("Browse");
        btnBrowseClassPath.setToolTipText("Browse the class path from system directory");
        btnBrowseClassPath.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                DirectoryDialog directoryDialog = new DirectoryDialog(top.getShell());
                directoryDialog.setFilterPath(filterPath);
                directoryDialog.setMessage("Please select the location for Test Results and click OK");
                String dir = directoryDialog.open();
                if (dir != null) {
                    selectedClassPathDir = dir;
                    filterPath = dir;
                    comboProjectPath.setText(selectedClassPathDir);
                }
            }
        });
        btnAddPath = new Button(classPathGroup, SWT.NONE);
        btnAddPath.setBounds(new org.eclipse.swt.graphics.Rectangle(400, 210, 80, 19));
        btnAddPath.setText("Add Path");
        btnAddPath.setToolTipText("Add the selected path to classpath list");
        btnAddPath.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String classPath = comboProjectPath.getText().trim();
                classPathList.add(classPath);
            }
        });
        btnRemovePath = new Button(classPathGroup, SWT.NONE);
        btnRemovePath.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 15, 91, 23));
        btnRemovePath.setText("Remove Path");
        btnRemovePath.setToolTipText("Remove the selected path from class path list");
        btnRemovePath.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String[] paths = classPathList.getSelection();
                FitManager Fit = FitManager.getFitManager();
                for (String s : paths) {
                    classPathList.remove(s);
                    Fit.removeClassPath(s);
                }
            }
        });
        createComboProjectPath();
    }

    private void createComboProjectPath() {
        comboProjectPath = new Combo(classPathGroup, SWT.NONE);
        comboProjectPath.setBounds(new org.eclipse.swt.graphics.Rectangle(15, 210, 286, 16));
    }

    private void createFitSettingsGroup() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.heightHint = -1;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridData.horizontalSpan = 2;
        fitSettingsGroup = new Group(top, SWT.NONE);
        fitSettingsGroup.setLayoutData(gridData);
        fitSettingsGroup.setText("Fit Resources Location");
        lblTestLocation = new Label(fitSettingsGroup, SWT.NONE);
        lblTestLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(9, 16, 67, 13));
        lblTestLocation.setText("Test Location");
        txtTestLocation = new Text(fitSettingsGroup, SWT.BORDER);
        txtTestLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(105, 15, 196, 19));
        txtTestLocation.setToolTipText(Constants.FITCLIPSE_TOOLTIP_PROPERTIES_FIT_TEST_LOCATION);
        btnBrowseTestLocation = new Button(fitSettingsGroup, SWT.NONE);
        btnBrowseTestLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 15, 91, 19));
        btnBrowseTestLocation.setText("Browse");
        btnBrowseTestLocation.setToolTipText("Browse the location for saving FIT tests");
        btnBrowseTestLocation.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                DirectoryDialog directoryDialog = new DirectoryDialog(top.getShell());
                directoryDialog.setFilterPath(filterPath);
                directoryDialog.setMessage("Please select the location for Fit Tests and click OK");
                String dir = directoryDialog.open();
                if (dir != null) {
                    selectedSrcDir = dir;
                    filterPath = dir;
                    txtTestLocation.setText(selectedSrcDir);
                }
            }
        });
        lblResultLocation = new Label(fitSettingsGroup, SWT.NONE);
        lblResultLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(9, 38, 82, 16));
        lblResultLocation.setText("Result Location");
        txtResultLocation = new Text(fitSettingsGroup, SWT.BORDER);
        txtResultLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(105, 38, 196, 19));
        txtResultLocation.setToolTipText(Constants.FITCLIPSE_TOOLTIP_PROPERTIES_FIT_TEST_RESULT_LOCATION);
        btnBrowseResultLocation = new Button(fitSettingsGroup, SWT.NONE);
        btnBrowseResultLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 38, 91, 19));
        btnBrowseResultLocation.setText("Browse");
        btnBrowseResultLocation.setToolTipText("Browse the location for saving FIT test results");
        btnBrowseResultLocation.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                DirectoryDialog directoryDialog = new DirectoryDialog(top.getShell());
                directoryDialog.setFilterPath(filterPath);
                directoryDialog.setMessage("Please select the location for Test Results and click OK");
                String dir = directoryDialog.open();
                if (dir != null) {
                    SelectedResultDir = dir;
                    filterPath = dir;
                    txtResultLocation.setText(SelectedResultDir);
                }
            }
        });
    }

    public void Finish() {
        FITTestConfiguration config = new FITTestConfiguration((IProject) selection.getFirstElement());
        config.setClassPath(classPathList.getItems());
        config.setTestResultLoc(txtResultLocation.getText().trim());
        config.setTestSourceLoc(txtTestLocation.getText().trim());
        wizardController.persistClassPathConfiguration(config, getShell());
    }
}
