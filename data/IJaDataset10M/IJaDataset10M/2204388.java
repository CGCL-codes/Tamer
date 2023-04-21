package org.kompiro.readviewer.ui.wizards;

import java.io.File;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExportServicesPage extends WizardPage {

    private static final String TITLE = "Percs Setting Export";

    private Label fileLabel;

    private Text fileText;

    private Button fileButton;

    protected ExportServicesPage() {
        super(TITLE);
        setTitle(TITLE);
        setDescription("Select export Percs services setting file path.");
    }

    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.None);
        GridLayoutFactory.fillDefaults().numColumns(3).applyTo(comp);
        fileLabel = new Label(comp, SWT.NONE);
        fileLabel.setText("Export Path:");
        fileText = new Text(comp, SWT.BORDER);
        fileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fileText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                boolean validFilePath = isValidFilePath();
                if (!validFilePath) {
                    setErrorMessage("Please select legal parent directory.");
                } else {
                    setErrorMessage(null);
                }
                setPageComplete(validFilePath);
            }
        });
        fileButton = new Button(comp, SWT.BORDER);
        fileButton.setText("Select");
        fileButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
                dialog.setFileName("percs_services.xml");
                String fileName = dialog.open();
                if (fileName != null) {
                    fileText.setText(fileName);
                    setPageComplete(isValidFilePath());
                }
            }

            public void widgetSelected(SelectionEvent e) {
                widgetDefaultSelected(e);
            }
        });
        setControl(comp);
    }

    @Override
    public boolean isPageComplete() {
        return isValidFilePath();
    }

    private boolean isValidFilePath() {
        String path = fileText.getText();
        if (path == null || "".equals(path)) return false;
        File file = new File(path);
        File parentFile = file.getParentFile();
        return parentFile != null && parentFile.isDirectory() && parentFile.exists();
    }

    public File getFile() {
        return new File(fileText.getText());
    }

    @Override
    public void dispose() {
        fileLabel.dispose();
        fileText.dispose();
        fileButton.dispose();
        super.dispose();
    }

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        WizardDialog dialog = new WizardDialog(shell, new ExportWizard());
        dialog.open();
        display.dispose();
    }
}
