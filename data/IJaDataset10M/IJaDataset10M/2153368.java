package br.com.visualmidia.ui.wizard.backup;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import br.com.visualmidia.system.GDServer;
import br.com.visualmidia.system.GDSystem;

public class SearchPathPageWizard extends WizardPage {

    private Composite composite;

    private Label labelBackup;

    private Button searchButton;

    private Text pathText;

    private boolean status;

    private Label informationImage;

    private Label informationLabel;

    private Label informationLabelBottom;

    private static String destZipFile;

    public SearchPathPageWizard() {
        super("SearchPath");
    }

    public void createControl(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new FormLayout());
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        composite.setLayoutData(data);
        setControl(composite);
        createContents();
        setPageComplete(false);
    }

    private void createContents() {
        createLabel();
        createSearchButton();
        createPathText();
        if (GDSystem.isServerMode()) {
            createInformationImage();
            createInformationLabel();
            serverInformationVisible(false);
        }
    }

    private void createLabel() {
        labelBackup = new Label(composite, SWT.NONE);
        labelBackup.setText("Digite o caminho: ");
        FormData data = new FormData();
        data.left = new FormAttachment(0, 10);
        data.top = new FormAttachment(0, 5);
        labelBackup.setLayoutData(data);
    }

    private void createPathText() {
        pathText = new Text(composite, SWT.BORDER);
        pathText.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event arg0) {
                if (!pathText.getText().equals("") && ((pathText.getText().endsWith(".gdbk")) || (pathText.getText().endsWith(".zip")) || (pathText.getText().endsWith(".jar")))) {
                    setPageComplete(true);
                } else {
                    setPageComplete(false);
                }
            }
        });
        FormData data = new FormData();
        data.left = new FormAttachment(labelBackup, 5);
        data.right = new FormAttachment(searchButton, -5);
        data.width = 200;
        pathText.setLayoutData(data);
    }

    private void createInformationImage() {
        informationImage = new Label(composite, SWT.NONE);
        informationImage.setImage(new Image(getShell().getDisplay(), "img/warning.png"));
        FormData data = new FormData();
        data.left = new FormAttachment(0, 10);
        data.top = new FormAttachment(pathText, 40);
        informationImage.setLayoutData(data);
    }

    private void createInformationLabel() {
        informationLabel = new Label(composite, SWT.NONE);
        FontData dataFont = new FontData("Arial", 12, SWT.BOLD);
        Font font = new Font(getShell().getDisplay(), dataFont);
        informationLabel.setFont(font);
        informationLabel.setText("ATEN��O: \n");
        FormData data = new FormData();
        data.left = new FormAttachment(informationImage, 25);
        data.top = new FormAttachment(pathText, 55);
        informationLabel.setLayoutData(data);
        informationLabelBottom = new Label(composite, SWT.NONE);
        informationLabelBottom.setText("Feche todos os cliente antes \n" + "de iniciar a restaura��o");
        FormData dataBottom = new FormData();
        dataBottom.left = new FormAttachment(informationImage, 25);
        dataBottom.top = new FormAttachment(informationLabel, 15);
        informationLabelBottom.setLayoutData(dataBottom);
    }

    private void createSearchButton() {
        searchButton = new Button(composite, SWT.NONE);
        searchButton.setText("Procurar");
        FormData data = new FormData();
        data.right = new FormAttachment(100, -5);
        searchButton.setLayoutData(data);
        searchButton.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event arg0) {
                createFileDialog();
            }
        });
    }

    protected void createFileDialog() {
        getShell().getDisplay().syncExec(new Runnable() {

            public void run() {
                FileDialog fileDialog = new FileDialog(getShell(), (status) ? SWT.SAVE : SWT.OPEN);
                fileDialog.setFilterNames(new String[] { "Aqruivo de Backup Gerente Digital (*.gdbk)", "Arquivo zip (*.zip)", "Arquivo jar (*.jar)" });
                fileDialog.setFilterExtensions(new String[] { "*.gdbk", "*.zip", "*.jar" });
                fileDialog.setFileName("Backup.gdbk");
                destZipFile = fileDialog.open();
                if (destZipFile != null) {
                    if (pathText.getText().equals("")) {
                        pathText.setText(destZipFile);
                    } else {
                        pathText.setText("");
                        pathText.setText(destZipFile);
                    }
                }
            }
        });
    }

    public static String getPathFile() {
        return destZipFile;
    }

    public void setStatusDialog(boolean status) {
        this.status = status;
    }

    public void serverInformationVisible(boolean isShow) {
        informationImage.setVisible(isShow);
        informationLabel.setVisible(isShow);
        informationLabelBottom.setVisible(isShow);
    }
}
