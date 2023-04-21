package org.eclipse.help.ui.internal.preferences;

import org.eclipse.help.ui.internal.Messages;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class AddICDialog extends StatusDialog implements IShellProvider {

    private Group group;

    private Label nameLabel;

    private Text nameText;

    private Label hostLabel;

    private Text hostText;

    private Label pathLabel;

    private Text pathText;

    private Button radio1;

    private Button radio2;

    private Text portText;

    Point shellSize;

    Point shellLocation;

    private String enteredHost;

    private String enteredName;

    private String enteredPort;

    private String enteredPath;

    private boolean enteredUseDefault;

    private Color errorColor;

    private StatusInfo dialogStatus;

    public AddICDialog(Shell parentShell) {
        super(parentShell);
    }

    private Listener changeListener = new Listener() {

        public void handleEvent(Event event) {
            updateValidity();
        }
    };

    protected Control createDialogArea(Composite parent) {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "org.eclipse.help.ui.prefPageHelpContent");
        Composite topComposite = (Composite) super.createDialogArea(parent);
        topComposite.setSize(topComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        Composite topGroup = new Composite(topComposite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        topGroup.setLayout(layout);
        topGroup.setFont(topComposite.getFont());
        topGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        createGroup(parent);
        updateValidity();
        return topComposite;
    }

    public void initializeBounds() {
        shellSize = getInitialSize();
        shellLocation = getInitialLocation(shellSize);
        this.getShell().setBounds(shellLocation.x, shellLocation.y, shellSize.x + 150, shellSize.y - 80);
        this.getShell().setText(Messages.AddICDialog_2);
    }

    private void createGroup(Composite parent) {
        group = new Group(parent, SWT.NONE);
        group.setText(Messages.AddICDialog_3);
        group.setLayout(new GridLayout(1, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        createNameSection(group);
        createHostSection(group);
        createPathSection(group);
        createPortSection(group);
    }

    private void createNameSection(Composite parent) {
        nameLabel = new Label(parent, SWT.NONE);
        nameLabel.setText(Messages.AddICDialog_4);
        nameText = new Text(parent, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        if (nameText.getOrientation() == SWT.RIGHT_TO_LEFT) nameText.setOrientation(SWT.LEFT_TO_RIGHT);
        nameText.addListener(SWT.Modify, changeListener);
    }

    private void createHostSection(Composite parent) {
        hostLabel = new Label(parent, SWT.NONE);
        hostLabel.setText(Messages.AddICDialog_5);
        hostText = new Text(parent, SWT.BORDER);
        hostText.setText("");
        hostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        if (hostText.getOrientation() == SWT.RIGHT_TO_LEFT) hostText.setOrientation(SWT.LEFT_TO_RIGHT);
        hostText.addListener(SWT.Modify, changeListener);
    }

    private void createPathSection(Composite parent) {
        pathLabel = new Label(parent, SWT.NONE);
        pathLabel.setText(Messages.AddICDialog_7);
        pathText = new Text(parent, SWT.BORDER);
        pathText.setText("/help");
        pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        if (pathText.getOrientation() == SWT.RIGHT_TO_LEFT) pathText.setOrientation(SWT.LEFT_TO_RIGHT);
        pathText.addListener(SWT.Modify, changeListener);
    }

    private void createPortSection(Composite parent) {
        Composite portComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        portComposite.setLayout(layout);
        portComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        radio1 = new Button(portComposite, SWT.RADIO);
        radio1.setText(Messages.AddICDialog_9);
        radio1.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 2, 1));
        radio1.addListener(SWT.Selection, changeListener);
        radio1.setSelection(true);
        radio2 = new Button(portComposite, SWT.RADIO);
        radio2.setText(Messages.AddICDialog_10);
        radio2.addListener(SWT.Selection, changeListener);
        portText = new Text(portComposite, SWT.BORDER);
        portText.setLayoutData(new GridData(50, SWT.DEFAULT));
        portText.addListener(SWT.Modify, changeListener);
        portText.setEnabled(false);
    }

    public String getEnteredHost() {
        return enteredHost;
    }

    public String getEnteredPath() {
        return enteredPath;
    }

    public String getEnteredPort() {
        return enteredPort;
    }

    public String getEnteredName() {
        return enteredName;
    }

    public boolean getEnteredDefault() {
        return enteredUseDefault;
    }

    public void okPressed() {
        enteredHost = hostText.getText();
        enteredName = nameText.getText();
        enteredPath = pathText.getText();
        if (radio1.getSelection()) {
            enteredUseDefault = true;
            enteredPort = Messages.AddICDialog_11;
        } else {
            enteredUseDefault = false;
            enteredPort = portText.getText();
        }
        this.setReturnCode(OK);
        if (errorColor != null) {
            errorColor.dispose();
        }
        this.close();
    }

    private void updateValidity() {
        String errorMessage = "";
        boolean errorFound = false;
        dialogStatus = new StatusInfo();
        if (hostText != null && hostText.getText().equals("")) {
            if (!errorFound) {
                errorMessage = Messages.AddICDialog_17;
                dialogStatus.setError(errorMessage);
                errorFound = true;
            }
        }
        if (radio2 != null && radio2.getSelection() == true) {
            try {
                portText.setEnabled(true);
                int port = Integer.parseInt(portText.getText());
                if (port < 0 || port > 65535) {
                    if (!errorFound) {
                        errorMessage = Messages.AddICDialog_19;
                        dialogStatus.setError(errorMessage);
                        errorFound = true;
                    }
                }
            } catch (NumberFormatException e) {
                if (!errorFound) {
                    errorMessage = Messages.AddICDialog_19;
                    dialogStatus.setError(errorMessage);
                    errorFound = true;
                }
            }
        } else {
            if (radio1 != null && radio1.getSelection()) {
                portText.setEnabled(false);
            }
        }
        if (errorFound) {
            this.updateStatus(dialogStatus);
        } else {
            dialogStatus.setOK();
            this.updateStatus(dialogStatus);
        }
    }
}
