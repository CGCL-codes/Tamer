package dk.kamstruplinnet.projecttransfer;

import java.io.File;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author jl
 */
public abstract class AbstractWizardProjectPathPage extends WizardPage {

    private final class LocationModifyListener implements Listener {

        public void handleEvent(Event e) {
            updatePageComplete();
        }
    }

    private final class ProjectLocationSelectListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent event) {
            handleLocationBrowseButtonPressed();
        }
    }

    private static String previouslyBrowsedDirectory = "";

    private static String lastLocation;

    protected static final int SIZING_TEXT_FIELD_WIDTH = 250;

    protected static final int SIZING_INDENTATION_WIDTH = 10;

    private Text locationPathField;

    protected Listener locationModifyListener = new LocationModifyListener();

    public AbstractWizardProjectPathPage(String pageName) {
        super(pageName);
        setPageComplete(false);
        setTitle(getPageTitle());
        setDescription(getPageDescription());
    }

    /**
	 * Method getPageDescription.
	 * @return String
	 */
    protected abstract String getPageDescription();

    /**
	 * Method getPageTitle.
	 * @return String
	 */
    protected abstract String getPageTitle();

    /**
	 * @see org.eclipse.jface.dialogs.DialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setFont(parent.getFont());
        createProjectLocationGroup(composite);
        validatePage();
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

    /**
	 * Creates the project location specification controls.
	 *
	 * @param parent the parent composite
	 */
    private final void createProjectLocationGroup(Composite parent) {
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(parent.getFont());
        createUserSpecifiedProjectLocationGroup(projectGroup);
    }

    protected abstract String getContentsLabelText();

    /**
	 * Creates the project location specification controls.
	 *
	 * @param projectGroup the parent composite
	 * @param boolean - the initial enabled state of the widgets created
	 */
    protected void createUserSpecifiedProjectLocationGroup(Composite projectGroup) {
        Font dialogFont = projectGroup.getFont();
        Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
        projectContentsLabel.setText(getContentsLabelText());
        projectContentsLabel.setFont(dialogFont);
        this.locationPathField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        this.locationPathField.setLayoutData(data);
        this.locationPathField.setFont(dialogFont);
        if (lastLocation != null) {
            this.locationPathField.setText(lastLocation);
        }
        createBrowseButton(projectGroup, dialogFont);
        addLocationPathFieldListener(locationModifyListener);
        updatePageComplete();
    }

    protected void addLocationPathFieldListener(Listener listener) {
        locationPathField.addListener(SWT.Modify, listener);
    }

    protected void createBrowseButton(Composite projectGroup, Font dialogFont) {
        Button browseButton = new Button(projectGroup, SWT.PUSH);
        browseButton.setText(Messages.getString("AbstractWizardProjectPathPage.browse"));
        browseButton.setFont(dialogFont);
        browseButton.setLayoutData(getButtonLayoutData());
        browseButton.addSelectionListener(new ProjectLocationSelectListener());
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            locationPathField.setFocus();
        }
    }

    /**
	 * Returns whether this page's controls currently all contain valid
	 * values.
	 *
	 * @return <code>true</code> if all controls are valid, and
	 *   <code>false</code> if at least one is invalid
	 */
    protected boolean validatePage() {
        return validatePath(locationPathField, false);
    }

    protected boolean validatePath(Text pathField, boolean allowEmpty) {
        String pathFieldContents = "";
        if (pathField != null) {
            pathFieldContents = pathField.getText().trim();
        }
        if (!(allowEmpty && pathFieldContents.equals(""))) {
            if (pathFieldContents.equals("")) {
                setErrorMessage(null);
                setMessage(Messages.getString("AbstractWizardProjectPathPage.pathEmpty"));
                return false;
            }
            IPath path = new Path("");
            if (!path.isValidPath(pathFieldContents)) {
                setErrorMessage(Messages.getString("AbstractWizardProjectPathPage.invalidPath"));
                return false;
            }
            File dir = new File(pathFieldContents);
            if (!dir.isDirectory()) {
                setErrorMessage(Messages.getString("AbstractWizardProjectPathPage.notADirectoryPath"));
                return false;
            }
        }
        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    protected void updatePageComplete() {
        setPageComplete(validatePage());
    }

    /**
	 * Returns the value of the project location field
	 * with leading and trailing spaces removed.
	 *
	 * @return the project location directory in the field
	 */
    protected String getProjectLocationFieldValue() {
        return trimPath(locationPathField.getText());
    }

    /**
	 * @return
	 */
    protected String trimPath(String path) {
        Path result = new Path(path);
        return result.addTrailingSeparator().toString();
    }

    /**
	 *  Open an appropriate directory browser
	 */
    void handleLocationBrowseButtonPressed() {
        String selectedDirectory = browseDirectory(locationPathField, previouslyBrowsedDirectory);
        if (selectedDirectory != null) previouslyBrowsedDirectory = selectedDirectory;
    }

    protected String browseDirectory(Text pathField, String directory) {
        DirectoryDialog dialog = new DirectoryDialog(pathField.getShell());
        dialog.setMessage(Messages.getString("AbstractWizardProjectPathPage.directory"));
        String dirName = pathField.getText().trim();
        if (dirName.length() == 0) {
            dirName = directory;
        }
        if (dirName.length() == 0) {
            dialog.setFilterPath(getWorkspace().getRoot().getLocation().toOSString());
        } else {
            File path = new File(dirName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(dirName).toOSString());
            }
        }
        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory);
        }
        return selectedDirectory;
    }

    private IWorkspace getWorkspace() {
        IWorkspace workspace = ProjectTransferPlugin.getWorkspace();
        return workspace;
    }

    public IWizardPage getNextPage() {
        if (locationPathField != null) {
            lastLocation = locationPathField.getText();
        }
        return super.getNextPage();
    }

    protected void createSpacer(Composite projectGroup) {
        Composite spacer = new Composite(projectGroup, SWT.NONE);
        GridData spacerGridData = new GridData();
        spacerGridData.heightHint = 20;
        spacer.setLayoutData(spacerGridData);
    }

    protected void createExplanationArea(Composite projectGroup, Font dialogFont, String[] explanationTextIds) {
        createSpacer(projectGroup);
        Composite explanationsGroup = new Composite(projectGroup, SWT.BORDER);
        GridData explanationsGridData = new GridData(GridData.FILL_BOTH);
        explanationsGridData.horizontalSpan = 3;
        explanationsGridData.grabExcessVerticalSpace = true;
        explanationsGroup.setLayoutData(explanationsGridData);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.verticalSpacing = 10;
        explanationsGroup.setLayout(gridLayout);
        for (int i = 0; i < explanationTextIds.length; i++) {
            createExplanationLabel(explanationsGroup, dialogFont, explanationTextIds[i]);
        }
    }

    private void createExplanationLabel(Composite group, Font dialogFont, String messageId) {
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 200;
        Label explanationLabel = new Label(group, SWT.WRAP);
        explanationLabel.setText(Messages.getString(messageId));
        explanationLabel.setFont(dialogFont);
        explanationLabel.setLayoutData(data);
    }

    protected GridData getButtonLayoutData() {
        GridData browseButtonData = new GridData();
        browseButtonData.heightHint = convertVerticalDLUsToPixels(IDialogConstants.BUTTON_HEIGHT);
        browseButtonData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        return browseButtonData;
    }
}
