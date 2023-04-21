package net.sourceforge.sfeclipse.wizards;

import java.util.HashSet;
import net.sourceforge.sfeclipse.model.SymfonyModule;
import org.eclipse.core.resources.IProject;
import org.eclipse.php.internal.ui.preferences.ProjectSelectionDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;

/**
 * 
 * @author MichalMachowski
 * 
 */
public class SfActionWizardPage extends DataModelWizardPage {

    private Label actionLabel = null;

    private Text actionName = null;

    private Label moduleLabel = null;

    private Text moduleName = null;

    private Button button = null;

    private Button ownFilebutton = null;

    private Button multiFileButton = null;

    private Button createTemplateButton = null;

    private Button validateFuntion = null;

    private Button errorFunction = null;

    private Label returnLabel = null;

    private Combo combo = null;

    private Button preExecute = null;

    private Button postExecute = null;

    private Button secureButton = null;

    private Text credentials = null;

    private Label credentialsLabel = null;

    private Label prjNameLabel;

    private Text prjNameText;

    private Button prjBrowse;

    private IProject project;

    private SymfonyModule module;

    protected SfActionWizardPage(IDataModel model, String pageName) {
        this(model, pageName, null, null);
    }

    protected SfActionWizardPage(IDataModel model, String pageName, IProject project) {
        this(model, pageName, project, null);
    }

    protected SfActionWizardPage(IDataModel model, String pageName, IProject project, SymfonyModule module) {
        super(model, pageName);
        setTitle("Symfony action");
        setDescription("Create symfony action.");
        setPageComplete(false);
        this.project = project;
        this.module = module;
    }

    @Override
    protected Composite createTopLevelComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridData gridData6 = new GridData(GridData.FILL_BOTH);
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData4 = new GridData();
        GridData gridData3 = new GridData();
        GridData gridData2 = new GridData();
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new GridData();
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        prjNameLabel = new Label(composite, SWT.NONE);
        prjNameLabel.setText("Project name:");
        prjNameText = new Text(composite, SWT.BORDER);
        prjNameText.setLayoutData(gridData);
        synchHelper.synchText(prjNameText, SfActionModelDataProvider.PRJ_NAME, null);
        prjBrowse = new Button(composite, SWT.None);
        prjBrowse.setText("Browse");
        prjBrowse.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ProjectSelectionDialog dialog = new ProjectSelectionDialog(getShell(), new HashSet<IProject>());
                dialog.setTitle("Choose symfony project");
                if (dialog.open() == ProjectSelectionDialog.OK) {
                    project = (IProject) dialog.getFirstResult();
                    if (project != null) {
                        prjNameText.setText(project.getName());
                    }
                }
            }
        });
        actionLabel = new Label(composite, SWT.NONE);
        actionLabel.setText("Action");
        actionName = new Text(composite, SWT.BORDER);
        synchHelper.synchText(actionName, SfActionModelDataProvider.ACTION_NAME, null);
        actionName.setLayoutData(gridData1);
        @SuppressWarnings("unused") Label filler = new Label(composite, SWT.NONE);
        moduleLabel = new Label(composite, SWT.NONE);
        moduleLabel.setText("Module");
        moduleName = new Text(composite, SWT.BORDER);
        moduleName.setLayoutData(gridData);
        synchHelper.synchText(moduleName, SfActionModelDataProvider.MODULE_NAME, null);
        button = new Button(composite, SWT.NONE);
        button.setText("Browse");
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (project instanceof IProject) {
                    ModuleSelectionDialog dialog = new ModuleSelectionDialog(getShell(), project);
                    if (dialog.open() == ModuleSelectionDialog.OK) {
                        if (dialog.getFirstResult() instanceof SymfonyModule) {
                            module = (SymfonyModule) dialog.getFirstResult();
                            moduleName.setText(module.getName());
                        }
                    }
                }
            }
        });
        returnLabel = new Label(composite, SWT.NONE);
        returnLabel.setText("Return");
        createCombo(composite);
        filler = new Label(composite, SWT.NONE);
        ownFilebutton = new Button(composite, SWT.RADIO);
        ownFilebutton.setText("Create own file");
        synchHelper.synchRadio(ownFilebutton, SfActionModelDataProvider.ACTION_OWN_FILE, null);
        multiFileButton = new Button(composite, SWT.RADIO);
        multiFileButton.setText("Add to actions file");
        multiFileButton.setSelection(true);
        synchHelper.synchRadio(multiFileButton, SfActionModelDataProvider.ACTION_MULTI_FILE, null);
        filler = new Label(composite, SWT.NONE);
        createTemplateButton = new Button(composite, SWT.CHECK);
        createTemplateButton.setText("Create template");
        createTemplateButton.setSelection(true);
        createTemplateButton.setLayoutData(gridData2);
        synchHelper.synchCheckbox(createTemplateButton, SfActionModelDataProvider.ACTION_CRATE_TEMPLATE, null);
        secureButton = new Button(composite, SWT.CHECK);
        secureButton.setText("Secure");
        synchHelper.synchCheckbox(secureButton, SfActionModelDataProvider.ACTION_SECURE, null);
        filler = new Label(composite, SWT.NONE);
        validateFuntion = new Button(composite, SWT.CHECK);
        validateFuntion.setText("Validate function");
        validateFuntion.setLayoutData(gridData3);
        synchHelper.synchCheckbox(validateFuntion, SfActionModelDataProvider.ACTION_CREATE_VALIDATION_FUNC, null);
        errorFunction = new Button(composite, SWT.CHECK);
        errorFunction.setText("Error function");
        errorFunction.setLayoutData(gridData4);
        synchHelper.synchCheckbox(errorFunction, SfActionModelDataProvider.ACTION_CREATE_ERROR_FUNC, null);
        filler = new Label(composite, SWT.NONE);
        preExecute = new Button(composite, SWT.CHECK);
        preExecute.setText("preExecute");
        synchHelper.synchCheckbox(preExecute, SfActionModelDataProvider.ACTION_CREATE_PREEXEC_FUNC, null);
        postExecute = new Button(composite, SWT.CHECK);
        postExecute.setText("postExecute");
        synchHelper.synchCheckbox(postExecute, SfActionModelDataProvider.ACTION_CREATE_POSTEXEC_FUNC, null);
        filler = new Label(composite, SWT.NONE);
        credentialsLabel = new Label(composite, SWT.NONE);
        credentialsLabel.setText("Credentials");
        credentials = new Text(composite, SWT.BORDER);
        credentials.setLayoutData(gridData6);
        synchHelper.synchText(credentials, SfActionModelDataProvider.ACTION_CREDENTIALS, null);
        composite.setLayout(gridLayout);
        if (project != null) {
            prjNameText.setText(project.getName());
        }
        return composite;
    }

    /**
	 * This method initializes combo
	 * 
	 */
    private void createCombo(Composite composite) {
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        combo = new Combo(composite, SWT.NONE);
        combo.setLayoutData(gridData5);
        combo.setItems(new String[] { "Success", "Error", "None" });
        synchHelper.synchCombo(combo, SfActionModelDataProvider.ACTION_RETURN, null);
    }

    @Override
    protected String[] getValidationPropertyNames() {
        return new String[] {};
    }
}
