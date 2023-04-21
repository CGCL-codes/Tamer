package ca.ucalgary.cpsc.ebe.fitClipse.ui.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.PropertyPage;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipsePlugin;
import ca.ucalgary.cpsc.ebe.fitClipse.runner.FITTestConfiguration;
import ca.ucalgary.cpsc.ebe.fitClipse.runner.FitManager;

public class ClassPathProperties extends PropertyPage {

    ClassPathPropertiesController pageController;

    Composite top = null;

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

    @Override
    protected Control createContents(Composite parent) {
        pageController = new ClassPathPropertiesController((IProject) this.getElement());
        this.addChildControls(parent);
        pageController.loadClassPath(classPathList, false);
        pageController.loadTestSourcePath(txtTestLocation, false);
        pageController.loadTestResultPath(txtResultLocation, false);
        return top;
    }

    public void init(IWorkbench workbench) {
    }

    protected void performDefaults() {
        pageController.loadClassPath(classPathList, true);
        pageController.loadTestSourcePath(txtTestLocation, true);
        pageController.loadTestResultPath(txtResultLocation, true);
        super.performDefaults();
    }

    public void performApply() {
        FITTestConfiguration config = new FITTestConfiguration((IProject) this.getElement());
        config.setClassPath(classPathList.getItems());
        config.setTestResultLoc(txtResultLocation.getText().trim());
        config.setTestSourceLoc(txtTestLocation.getText().trim());
        pageController.performApply(config, getShell());
        super.performApply();
    }

    public boolean performOk() {
        FITTestConfiguration config = new FITTestConfiguration((IProject) this.getElement());
        config.setClassPath(classPathList.getItems());
        config.setTestResultLoc(txtResultLocation.getText().trim());
        config.setTestSourceLoc(txtTestLocation.getText().trim());
        pageController.performOK(config, getShell());
        return super.performOk();
    }

    private void addChildControls(Composite parent) {
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new GridLayout());
        createClassPathGroup();
        createFitSettingsGroup();
    }

    private void createClassPathGroup() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.heightHint = -1;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        classPathGroup = new Group(top, SWT.NONE);
        classPathGroup.setText("Class Path");
        classPathGroup.setLayoutData(gridData);
        classPathList = new List(classPathGroup, SWT.MULTI | SWT.FILL);
        classPathList.setBounds(new org.eclipse.swt.graphics.Rectangle(15, 15, 288, 187));
        btnBrowseClassPath = new Button(classPathGroup, SWT.NONE);
        btnBrowseClassPath.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 210, 80, 19));
        btnBrowseClassPath.setText("Browse");
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
        btnAddPath.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String classPath = comboProjectPath.getText().trim();
                classPathList.add(classPath);
            }
        });
        btnRemovePath = new Button(classPathGroup, SWT.NONE);
        btnRemovePath.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 15, 91, 23));
        btnRemovePath.setText("Remove Path");
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
        fitSettingsGroup = new Group(top, SWT.NONE);
        fitSettingsGroup.setText("Fit Resources Location");
        lblTestLocation = new Label(fitSettingsGroup, SWT.NONE);
        lblTestLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(9, 16, 67, 13));
        lblTestLocation.setText("Test Location");
        txtTestLocation = new Text(fitSettingsGroup, SWT.BORDER);
        txtTestLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(105, 15, 196, 19));
        btnBrowseTestLocation = new Button(fitSettingsGroup, SWT.NONE);
        btnBrowseTestLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 15, 91, 19));
        btnBrowseTestLocation.setText("Browse");
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
        btnBrowseResultLocation = new Button(fitSettingsGroup, SWT.NONE);
        btnBrowseResultLocation.setBounds(new org.eclipse.swt.graphics.Rectangle(315, 38, 91, 19));
        btnBrowseResultLocation.setText("Browse");
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
}
