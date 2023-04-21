package com.byterefinery.rmbench.dialogs.export;

import java.io.File;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.byterefinery.rmbench.dialogs.Messages;

/**
 * page that lets the user choose the directory for model export
 * @author cse
 */
class ExportDirectoryPage extends WizardPage {

    private Text directoryText;

    protected ExportDirectoryPage() {
        super(ExportDirectoryPage.class.getName());
        setTitle(Messages.ExportWizard_mainTitle);
        setDescription(Messages.ExportWizard_chooseDirDescription);
    }

    public File getDirectory() {
        return new File(directoryText.getText());
    }

    public void createControl(Composite parent) {
        Composite control = new Composite(parent, SWT.NONE);
        control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        control.setLayout(new GridLayout(3, false));
        Label label = new Label(control, SWT.NONE);
        label.setText(Messages.ExportWizard_outputDirectory);
        directoryText = new Text(control, SWT.BORDER);
        directoryText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        directoryText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setPageComplete(directoryText.getCharCount() > 0);
            }
        });
        Button button = new Button(control, SWT.PUSH);
        button.setText(Messages.ExportWizard_chooseDirectory);
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell());
                String path = dialog.open();
                if (path != null) directoryText.setText(path);
            }
        });
        setPageComplete(false);
        setControl(control);
    }
}
