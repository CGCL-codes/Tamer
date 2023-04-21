package net.sf.osadm.docbook.plugin.wizards.releasenotes;

import java.util.regex.Pattern;
import net.sf.osadm.docbook.plugin.DocBookDtdVersion;
import net.sf.osadm.docbook.plugin.DocBookPlugin;
import net.sf.osadm.docbook.plugin.IssueTrackingSystem;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class NewSimpleSectionIssueWizardPage extends WizardPage {

    private ISelection selection;

    /** The container (directory) in which the file will be created. */
    private Text containerText;

    /** The selection for the DocBook DTD version. */
    private Combo dtdVersionCombo;

    /** The name of the file (to create). */
    private Text fileText;

    private String fileNamePrefix = "section-";

    private String fileNameExt = ".xml";

    private Text issueId;

    private Text issueTitle;

    private Combo issueSystemCombo;

    /**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
    public NewSimpleSectionIssueWizardPage(ISelection selection) {
        super("wizardPage");
        setTitle("Create new Issue Section");
        setDescription("This wizard creates a new release notes simple section file.");
        ImageDescriptor imageDescriptor = DocBookPlugin.getImageDescriptor("images/eclipse-wizard-page-empty.gif");
        setImageDescriptor(imageDescriptor);
        this.selection = selection;
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("&Container:");
        containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        containerText.setLayoutData(gd);
        containerText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });
        label = new Label(container, SWT.NULL);
        label.setText("DocBook DTD &Version:");
        dtdVersionCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        String[] dtdVersionArray = DocBookDtdVersion.getVersions();
        dtdVersionCombo.setItems(dtdVersionArray);
        dtdVersionCombo.setText(dtdVersionArray[0]);
        label = new Label(container, SWT.NULL);
        label = new Label(container, SWT.NULL);
        label.setText("Issue &System:");
        issueSystemCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        String[] issueSystemArray = IssueTrackingSystem.getSystemNames();
        issueSystemCombo.setItems(issueSystemArray);
        issueSystemCombo.setText(issueSystemArray[0]);
        issueSystemCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                dialogChanged();
                updateFileText();
            }
        });
        label = new Label(container, SWT.NULL);
        label = new Label(container, SWT.NULL);
        label.setText("Issue &Id:");
        issueId = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        issueId.setLayoutData(gd);
        issueId.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
                updateFileText();
            }
        });
        label = new Label(container, SWT.NULL);
        label = new Label(container, SWT.NULL);
        label.setText("Issue &Title:");
        issueTitle = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        issueTitle.setLayoutData(gd);
        issueTitle.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        label = new Label(container, SWT.NULL);
        label = new Label(container, SWT.NULL);
        label.setText("Fi&le name:");
        fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gd);
        fileText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
    }

    private void updateFileText() {
        fileText.setText(fileNamePrefix + getIssueSystem().getAbbreviation() + "-" + getIssueId() + fileNameExt);
    }

    /**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
    private void initialize() {
        if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1) return;
            Object obj = ssel.getFirstElement();
            if (obj instanceof IResource) {
                IContainer container;
                if (obj instanceof IContainer) container = (IContainer) obj; else container = ((IResource) obj).getParent();
                containerText.setText(container.getFullPath().toString());
            }
        }
    }

    /**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
    private void handleBrowse() {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false, "Select new file container");
        if (dialog.open() == ContainerSelectionDialog.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                containerText.setText(((Path) result[0]).toString());
            }
        }
    }

    /**
	 * Ensures that both text fields are set.
	 */
    private void dialogChanged() {
        IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
        if (getContainerName().length() == 0) {
            updateStatus("File container must be specified");
            return;
        }
        if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
            updateStatus("File container must exist");
            return;
        }
        if (!container.isAccessible()) {
            updateStatus("Project must be writable");
            return;
        }
        String issueIdStr = getIssueId();
        if (issueIdStr.length() == 0) {
            updateStatus("The Issue Id is required");
            return;
        }
        IssueTrackingSystem issueSystem = getIssueSystem();
        if (!Pattern.matches(issueSystem.getRegExpStr(), issueIdStr)) {
            updateStatus(issueSystem.getRegExpErrorMsg());
            return;
        }
        String issueTitle = getIssueTitle();
        if (issueTitle.trim().length() == 0) {
            updateStatus("Issue title is required");
            return;
        }
        String fileName = getFileName();
        if (fileName.length() == 0) {
            updateStatus("File name must be specified");
            return;
        }
        if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
            updateStatus("File name must be valid");
            return;
        }
        int dotLoc = fileName.lastIndexOf('.');
        if (dotLoc != -1) {
            String ext = fileName.substring(dotLoc + 1);
            if (ext.equalsIgnoreCase("xml") == false) {
                updateStatus("File extension must be \"xml\"");
                return;
            }
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setMessage(message, IMessageProvider.WARNING);
        setPageComplete(message == null);
    }

    public String getContainerName() {
        return containerText.getText();
    }

    public String getFileName() {
        return fileText.getText();
    }

    public String getDocBookDtdVersion() {
        return dtdVersionCombo.getText();
    }

    public IssueTrackingSystem getIssueSystem() {
        return IssueTrackingSystem.getIssueSystem(issueSystemCombo.getText());
    }

    public String getIssueId() {
        return issueId.getText().trim();
    }

    public String getIssueTitle() {
        return issueTitle.getText().trim();
    }
}
