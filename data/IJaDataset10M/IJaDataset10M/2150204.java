package com.safi.workshop.application;

import java.io.File;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import com.safi.workshop.application.ChooseSafiServerWorkspaceData.SafiWorkspaceProfile;

public class EditWorkspaceProfileDialog extends TitleAreaDialog {

    private static final String NAME_REGEX = "^[^\\\\\\./:\\*\\?\\\"<>\\|]{1}[^\\\\/:\\*\\?\\\"<>\\|]{0,254}$";

    private static final String DEFAULT_WORKSPACE_NAME = "workspace";

    private Text descriptionText;

    private Label descriptionLabel;

    private Button browseButton;

    private Text pathText;

    private Label workspacePathLabel;

    private Text nameText;

    private Label workspaceNameLabel;

    private ChooseSafiServerWorkspaceData.SafiWorkspaceProfile profile;

    private List<SafiWorkspaceProfile> existingProfiles;

    /**
   * Create the dialog
   * 
   * @param parentShell
   * @param list
   */
    public EditWorkspaceProfileDialog(Shell parentShell, ChooseSafiServerWorkspaceData.SafiWorkspaceProfile profile, List<SafiWorkspaceProfile> list) {
        super(parentShell);
        this.profile = profile;
        this.existingProfiles = list;
    }

    /**
   * Create contents of the dialog
   * 
   * @param parent
   */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginTop = 10;
        gridLayout.marginRight = 10;
        gridLayout.marginLeft = 10;
        gridLayout.marginBottom = 10;
        container.setLayout(gridLayout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        workspaceNameLabel = new Label(container, SWT.NONE);
        workspaceNameLabel.setText("Workspace Name:");
        nameText = new Text(container, SWT.BORDER);
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                validate();
            }
        });
        nameText.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(final FocusEvent e) {
                validate();
            }
        });
        final GridData gd_nameText = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        nameText.setLayoutData(gd_nameText);
        workspacePathLabel = new Label(container, SWT.NONE);
        workspacePathLabel.setText("Workspace Path:");
        pathText = new Text(container, SWT.BORDER);
        pathText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                validate();
            }
        });
        pathText.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(final FocusEvent e) {
                validate();
            }
        });
        final GridData gd_pathText = new GridData(SWT.FILL, SWT.CENTER, true, false);
        pathText.setLayoutData(gd_pathText);
        browseButton = new Button(container, SWT.NONE);
        browseButton.setText("Browse...");
        browseButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell());
                dialog.setText(IDEWorkbenchMessages.ChooseWorkspaceDialog_directoryBrowserTitle);
                dialog.setMessage(IDEWorkbenchMessages.ChooseWorkspaceDialog_directoryBrowserMessage);
                dialog.setFilterPath(pathText.getText());
                String dir = dialog.open();
                if (dir != null) {
                    pathText.setText(TextProcessor.process(dir));
                }
                validate();
            }
        });
        descriptionLabel = new Label(container, SWT.NONE);
        descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        descriptionLabel.setText("Description:");
        descriptionText = new Text(container, SWT.MULTI | SWT.BORDER);
        descriptionText.setTextLimit(256);
        final GridData gd_descriptionText = new GridData(SWT.FILL, SWT.FILL, true, true);
        descriptionText.setLayoutData(gd_descriptionText);
        new Label(container, SWT.NONE);
        if (profile != null) {
            nameText.setText(profile.getName() == null ? "" : profile.getName());
            pathText.setText(profile.getPath() == null ? "" : profile.getPath());
            descriptionText.setText(profile.getDescription() == null ? "" : profile.getDescription());
        }
        if (StringUtils.isBlank(pathText.getText())) {
            final String userDir = System.getProperty("user.dir");
            if (profile != null && StringUtils.isNotBlank(profile.getName())) {
                pathText.setText(userDir + File.separator + profile.getName());
            } else {
                final String wsName = getAvailableWorkspaceName(userDir);
                String wsPath = null;
                if (!userDir.endsWith(File.separator)) wsPath = userDir + File.separator + wsName; else wsPath = userDir + wsName;
                pathText.setText(wsPath);
                nameText.setText(wsName);
            }
        }
        validate();
        setTitle("SafiServer Workspace Editor");
        return area;
    }

    private String getAvailableWorkspaceName(String prefix) {
        File dir = new File(prefix);
        int i = 1;
        String curr = "";
        while (true) {
            File f = new File(dir, DEFAULT_WORKSPACE_NAME + curr);
            if (f.exists() || profileExists(f.getName())) {
                curr = String.valueOf(i++);
                continue;
            }
            return DEFAULT_WORKSPACE_NAME + curr;
        }
    }

    private boolean profileExists(String name) {
        for (SafiWorkspaceProfile profile : existingProfiles) {
            if (StringUtils.equals(profile.getName(), name)) {
                return true;
            }
        }
        return false;
    }

    private boolean validate() {
        final Button okButton = getButton(IDialogConstants.OK_ID);
        boolean valid = true;
        setMessage("Enter SafiServer Workspace information", IMessageProvider.NONE);
        if (StringUtils.isBlank(nameText.getText())) {
            if (profile != null) setMessage("Name cannot be blank", IMessageProvider.ERROR); else setMessage("Please enter a Workspace name", IMessageProvider.INFORMATION);
            valid = false;
        } else if (!nameText.getText().trim().matches(NAME_REGEX)) {
            setMessage("Name cannot contain any of the following characters \\/:*?\"<>|", IMessageProvider.ERROR);
            valid = false;
        } else {
            for (SafiWorkspaceProfile profile : existingProfiles) {
                if (StringUtils.equals(profile.getName(), nameText.getText()) && profile != this.profile) {
                    setMessage("Profile " + profile.getName() + " already exists", IMessageProvider.ERROR);
                    valid = false;
                    break;
                }
            }
        }
        if (valid && StringUtils.isBlank(pathText.getText())) {
            if (profile != null) setMessage("Path cannot be blank", IMessageProvider.ERROR); else setMessage("Please enter a valid path", IMessageProvider.INFORMATION);
            valid = false;
        } else if (valid && !new File(pathText.getText().trim()).getName().matches(NAME_REGEX)) {
            setMessage("File names cannot contain any of the following characters \\/:*?\"<>|", IMessageProvider.ERROR);
            valid = false;
        } else if (valid) {
            File f = new File(pathText.getText());
            for (SafiWorkspaceProfile profile : existingProfiles) {
                if (f.equals(new File(profile.getPath())) && profile != this.profile) {
                    setMessage("Workspace already exists at " + f.getAbsolutePath(), IMessageProvider.ERROR);
                    valid = false;
                    break;
                }
            }
            if (valid && profile == null && f.exists() && !f.isDirectory()) {
                setMessage("File exists at " + f.getAbsolutePath() + " and is not a directory.", IMessageProvider.ERROR);
                valid = false;
            } else if (valid && f.exists() && !f.canWrite()) {
                setMessage("Directory " + f.getAbsolutePath() + " is read-only.", IMessageProvider.ERROR);
                valid = false;
            }
        }
        if (valid) setMessage("");
        if (okButton != null && okButton.getEnabled() != valid) okButton.setEnabled(valid);
        return valid;
    }

    /**
   * Create contents of the button bar
   * 
   * @param parent
   */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void okPressed() {
        if (profile == null) {
            profile = new ChooseSafiServerWorkspaceData.SafiWorkspaceProfile();
        }
        profile.setName(nameText.getText());
        profile.setDescription(descriptionText.getText());
        profile.setPath(pathText.getText());
        super.okPressed();
    }

    /**
   * Return the initial size of the dialog
   */
    @Override
    protected Point getInitialSize() {
        return new Point(500, 375);
    }

    public ChooseSafiServerWorkspaceData.SafiWorkspaceProfile getProfile() {
        return profile;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("SafiServer Workspace Editor");
    }
}
