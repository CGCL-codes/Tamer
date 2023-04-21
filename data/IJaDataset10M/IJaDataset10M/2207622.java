package org.deved.antlride.ui.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.deved.antlride.core.AntlrCore;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

public class AntlrPromptFileDialog extends Dialog {

    private String fTitle;

    private Object fResult;

    private Text textFile;

    public AntlrPromptFileDialog(String title, Shell shell) {
        super(shell);
        fTitle = title;
    }

    public String getFile() {
        if (fResult instanceof String) {
            return (String) fResult;
        }
        if (fResult instanceof IResource) {
            IResource resource = (IResource) fResult;
            return resource.getLocation().toOSString();
        }
        return "";
    }

    public String getFileContent() {
        String filename = getFile();
        if (filename != null) {
            File file = new File(filename);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                StringBuilder builder = new StringBuilder();
                final String NL = System.getProperty("line.separator");
                while (line != null) {
                    builder.append(line);
                    builder.append(NL);
                    line = reader.readLine();
                }
                return builder.length() == 0 ? null : builder.toString();
            } catch (IOException ex) {
                AntlrCore.error(ex);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return null;
    }

    private void updateTextFile() {
        textFile.setText(getFile());
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        applyDialogFont(composite);
        Label label = new Label(composite, SWT.NONE);
        label.setText("File: ");
        GridData gd = new GridData();
        textFile = new Text(composite, SWT.READ_ONLY | SWT.BORDER);
        gd.widthHint = 300;
        textFile.setLayoutData(gd);
        Button button = new Button(composite, SWT.PUSH);
        button.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(getShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
                int open = dialog.open();
                if (open == Dialog.OK) {
                    fResult = dialog.getFirstResult();
                    updateTextFile();
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        button.setText("Select File...");
        gd = new GridData();
        gd.grabExcessHorizontalSpace = false;
        gd.horizontalAlignment = SWT.END;
        button.setLayoutData(gd);
        button = new Button(composite, SWT.PUSH);
        button.setText("Select External File...");
        button.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                String file = dialog.open();
                if (file != null) {
                    fResult = file;
                    updateTextFile();
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        gd = new GridData();
        gd.grabExcessHorizontalSpace = false;
        gd.horizontalAlignment = SWT.END;
        gd.horizontalSpan = 3;
        button.setLayoutData(gd);
        getShell().setText(fTitle);
        setBlockOnOpen(true);
        return composite;
    }
}
