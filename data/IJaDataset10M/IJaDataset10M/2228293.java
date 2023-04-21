package org.rubypeople.rdt.internal.debug.ui.preferences;

import java.io.File;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiMessages;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiPlugin;
import org.rubypeople.rdt.internal.launching.RubyInterpreter;
import org.rubypeople.rdt.internal.ui.dialogs.StatusDialog;

public class EditInterpreterDialog extends StatusDialog {

    protected RubyInterpreter interpreterToEdit;

    protected Text interpreterNameText, interpreterLocationText;

    protected IStatus[] allStatus = new IStatus[2];

    public EditInterpreterDialog(Shell parentShell, String aDialogTitle) {
        super(parentShell);
        setTitle(aDialogTitle);
    }

    public void setInterpreterToEdit(RubyInterpreter anInterpreter) {
        interpreterToEdit = anInterpreter;
        String interpreterName = interpreterToEdit.getName();
        interpreterNameText.setText(interpreterName != null ? interpreterName : "");
        IPath installLocation = interpreterToEdit.getInstallLocation();
        interpreterLocationText.setText(installLocation != null ? installLocation.toOSString() : "");
    }

    protected void createLocationEntryField(Composite composite) {
        new Label(composite, SWT.NONE).setText(RdtDebugUiMessages.getString("EditInterpreterDialog.rubyInterpreter.path.label"));
        Composite locationComposite = new Composite(composite, SWT.NONE);
        RowLayout locationLayout = new RowLayout();
        locationLayout.marginLeft = 0;
        locationComposite.setLayout(locationLayout);
        interpreterLocationText = new Text(locationComposite, SWT.SINGLE | SWT.BORDER);
        interpreterLocationText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                allStatus[1] = validateInterpreterLocationText();
                updateStatusLine();
            }
        });
        interpreterLocationText.setLayoutData(new RowData(120, SWT.DEFAULT));
        Button browseButton = new Button(composite, SWT.PUSH);
        browseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        browseButton.setText(RdtDebugUiMessages.getString("EditInterpreterDialog.rubyInterpreter.path.browse.button.label"));
        browseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                browseForInstallLocation();
            }
        });
    }

    protected void updateStatusLine() {
        updateStatus(getMostSevereStatus());
    }

    protected IStatus getMostSevereStatus() {
        IStatus max = new Status(0, RdtDebugUiPlugin.PLUGIN_ID, IStatus.OK, "", null);
        for (int i = 0; i < allStatus.length; i++) {
            IStatus curr = allStatus[i];
            if (curr != null) {
                if (curr.matches(IStatus.ERROR)) {
                    return curr;
                }
                if (max == null || curr.getSeverity() > max.getSeverity()) {
                    max = curr;
                }
            }
        }
        return max;
    }

    protected IStatus validateInterpreterLocationText() {
        File path = new File(interpreterLocationText.getText());
        if (path.isFile()) {
            return new Status(IStatus.OK, RdtDebugUiPlugin.PLUGIN_ID, 0, "", null);
        }
        return new Status(IStatus.ERROR, RdtDebugUiPlugin.PLUGIN_ID, 1, RdtDebugUiMessages.getString("EditInterpreterDialog.rubyInterpreter.path.error"), null);
    }

    protected void createNameEntryField(Composite composite) {
        new Label(composite, SWT.NONE).setText(RdtDebugUiMessages.getString("EditInterpreterDialog.rubyInterpreter.name"));
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        interpreterNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        interpreterNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                allStatus[0] = validateInterpreterNameText();
                updateStatusLine();
            }
        });
        interpreterNameText.setLayoutData(gridData);
    }

    protected IStatus validateInterpreterNameText() {
        int status = IStatus.OK;
        String message = "";
        if (interpreterNameText.getText() == null || interpreterNameText.getText().length() <= 0) {
            status = IStatus.ERROR;
            message = RdtDebugUiMessages.getString("EditInterpreterDialog.rubyInterpreter.name.error");
        }
        return new Status(status, RdtDebugUiPlugin.PLUGIN_ID, 0, message, null);
    }

    protected void browseForInstallLocation() {
        FileDialog dialog = new FileDialog(getShell());
        dialog.setFilterPath(interpreterLocationText.getText());
        dialog.setText(RdtDebugUiMessages.getString("EditInterpreterDialog.rubyInterpreter.path.browse.message"));
        String newPath = dialog.open();
        if (newPath != null) interpreterLocationText.setText(newPath);
    }

    protected void okPressed() {
        if (interpreterToEdit == null) interpreterToEdit = new RubyInterpreter(null, null);
        interpreterToEdit.setName(interpreterNameText.getText());
        interpreterToEdit.setInstallLocation(new Path(interpreterLocationText.getText()));
        super.okPressed();
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        createNameEntryField(composite);
        createLocationEntryField(composite);
        return composite;
    }
}
