package org.remus.infomngmnt.efs.ui;

import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.remus.infomngmnt.common.ui.UIUtil;
import org.remus.infomngmnt.common.ui.image.ResourceManager;
import org.remus.infomngmnt.efs.EFSActivator;
import org.remus.infomngmnt.efs.extension.AbstractSecurityProvider;
import org.remus.infomngmnt.efs.extension.ISecurityProviderExtension;

@SuppressWarnings("restriction")
public class EncryptedProjectWizardPage extends WizardPage {

    private Text descriptionText;

    private Table table;

    private Text text;

    private TableViewer tableViewer;

    private Button initializeProviderButton;

    private Label statusImage;

    private Label statusLabelState;

    protected AbstractSecurityProvider currentSecurityProvider;

    /**
	 * Create the wizard
	 */
    public EncryptedProjectWizardPage() {
        super("wizardPage");
        setTitle("New encrypted Information project");
        setDescription("This wizards creates a new project with encrytable content.");
    }

    /**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setImageDescriptor(ResourceManager.getPluginImageDescriptor(EFSActivator.getDefault(), "icons/iconexperience/wizards/new_encryptedproject_wizard.png"));
        setTitle("New encrypted project");
        final Group projectPropertiesGroup = new Group(container, SWT.NONE);
        projectPropertiesGroup.setText("Project properties");
        final GridData gd_projectPropertiesGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
        projectPropertiesGroup.setLayoutData(gd_projectPropertiesGroup);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        projectPropertiesGroup.setLayout(gridLayout);
        final Label projectNameLabel = new Label(projectPropertiesGroup, SWT.NONE);
        projectNameLabel.setText("Project name");
        this.text = new Text(projectPropertiesGroup, SWT.BORDER);
        this.text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.text.addListener(SWT.Modify, new Listener() {

            public void handleEvent(final Event event) {
                boolean valid = validatePage();
                setPageComplete(valid);
            }
        });
        final Group group_1 = new Group(container, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        group_1.setLayout(gridLayout_1);
        group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group_1.setText("Security");
        final SashForm sashForm = new SashForm(group_1, SWT.NONE);
        final Composite composite = new Composite(sashForm, SWT.NONE);
        composite.setLayout(new GridLayout());
        final Label availableSecurityaddinsLabel = new Label(composite, SWT.NONE);
        availableSecurityaddinsLabel.setText("Available Security-Addins");
        this.tableViewer = new TableViewer(composite, SWT.BORDER);
        this.table = this.tableViewer.getTable();
        this.table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.tableViewer.setContentProvider(UIUtil.getArrayContentProviderInstance());
        this.tableViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(final Object element) {
                return ((AbstractSecurityProvider) element).getName();
            }
        });
        this.tableViewer.setInput(EFSActivator.getDefault().getService(ISecurityProviderExtension.class).getItems());
        this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(final SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    EncryptedProjectWizardPage.this.descriptionText.setText("");
                    EncryptedProjectWizardPage.this.statusLabelState.setImage(null);
                } else {
                    EncryptedProjectWizardPage.this.currentSecurityProvider = (AbstractSecurityProvider) ((IStructuredSelection) event.getSelection()).getFirstElement();
                    checkProviderInitState();
                }
                EncryptedProjectWizardPage.this.initializeProviderButton.setEnabled(!event.getSelection().isEmpty());
                EncryptedProjectWizardPage.this.descriptionText.setText(EncryptedProjectWizardPage.this.currentSecurityProvider.getDescription());
                boolean valid = validatePage();
                setPageComplete(valid);
            }
        });
        final Composite composite_1 = new Composite(sashForm, SWT.NONE);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        composite_1.setLayout(gridLayout_2);
        final Label descriptionLabel = new Label(composite_1, SWT.NONE);
        descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        descriptionLabel.setText("Description");
        this.descriptionText = new Text(composite_1, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
        final GridData gd_descriptionText = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        this.descriptionText.setLayoutData(gd_descriptionText);
        this.descriptionText.setEditable(false);
        this.statusImage = new Label(composite_1, SWT.NONE);
        GridData layoutData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        layoutData.heightHint = 16;
        layoutData.widthHint = 16;
        this.statusImage.setLayoutData(layoutData);
        this.statusLabelState = new Label(composite_1, SWT.NONE);
        this.statusLabelState.setLayoutData(new GridData());
        this.statusLabelState.setText("Nothing selected.");
        this.initializeProviderButton = new Button(composite_1, SWT.NONE);
        this.initializeProviderButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                EncryptedProjectWizardPage.this.currentSecurityProvider.initProvider(getShell());
                checkProviderInitState();
                boolean valid = validatePage();
                setPageComplete(valid);
            }
        });
        final GridData gd_initializeProviderButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        this.initializeProviderButton.setLayoutData(gd_initializeProviderButton);
        this.initializeProviderButton.setText("Initialize provider");
        sashForm.setWeights(new int[] { 1, 1 });
        setControl(container);
        setPageComplete(false);
    }

    private boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();
        String projectFieldContents = this.text.getText().trim();
        if (projectFieldContents.equals("")) {
            setErrorMessage(null);
            setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
            return false;
        }
        IStatus nameStatus = workspace.validateName(projectFieldContents, IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }
        IProject handle = getProjectHandle();
        if (handle.exists()) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
            return false;
        }
        ISelection selection = this.tableViewer.getSelection();
        if (((IStructuredSelection) selection).getFirstElement() == null) {
            setErrorMessage("Security provider required");
            return false;
        }
        if (!((AbstractSecurityProvider) ((IStructuredSelection) selection).getFirstElement()).isInitialized()) {
            setErrorMessage("Selected security provider is not initialized.");
            return false;
        }
        setErrorMessage(null);
        return true;
    }

    public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(this.text.getText().trim());
    }

    public URI getProjectUri() {
        String scheme = this.currentSecurityProvider.getScheme();
        URI locationUri = ResourcesPlugin.getWorkspace().getRoot().getLocationURI();
        String path = locationUri.getPath() + "/" + getProjectHandle().getName();
        try {
            return new URI(scheme, locationUri.getUserInfo(), locationUri.getHost(), locationUri.getPort(), path, locationUri.getQuery(), locationUri.getFragment());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkProviderInitState() {
        if (EncryptedProjectWizardPage.this.currentSecurityProvider.isInitialized()) {
            EncryptedProjectWizardPage.this.statusLabelState.setText("Initialized");
            EncryptedProjectWizardPage.this.statusImage.setImage(ResourceManager.getPluginImage(EFSActivator.getDefault(), "icons/iconexperience/ok.png"));
        } else {
            EncryptedProjectWizardPage.this.statusLabelState.setText("Not initialized");
            EncryptedProjectWizardPage.this.statusImage.setImage(ResourceManager.getPluginImage(EFSActivator.getDefault(), "icons/iconexperience/sign_warning.png"));
        }
    }
}
