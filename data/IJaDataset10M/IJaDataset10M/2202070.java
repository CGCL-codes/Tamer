package org.formaria.editor.eclipse.newproject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (aria).
 */
public class FileNamesWizardPage extends WizardPage {

    private float dlux, dluy;

    private Label ProjectName = null;

    private Text stylesEdit = null;

    private Label label = null;

    private Text framesFileEdit = null;

    private Label label1 = null;

    private Label label21 = null;

    private Label label22 = null;

    private Label synthLabel = null;

    private Text modelFileEdit = null;

    private Text validationFileEdit = null;

    private Text synthEdit = null;

    private Combo lafCombo = null;

    private Button picStyleBtn = null;

    private Button picFramesBtn = null;

    private Button picModelEdit = null;

    private Button picValidationsBtn = null;

    private Button picSynthEdit = null;

    private NewAriaProjectWizard wizard;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public FileNamesWizardPage(NewAriaProjectWizard wiz) {
        super("FileNamesWizardPage");
        wizard = wiz;
        setTitle("File name settings");
        setDescription("Set the file names for various application resources");
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        initializeDialogUnits(container);
        dlux = ((float) convertWidthInCharsToPixels(100)) / 400.0F;
        dluy = ((float) convertHeightInCharsToPixels(100)) / 800.0F;
        ProjectName = new Label(container, SWT.RIGHT);
        ProjectName.setBounds(getDluRect(20, 16, 120, 14));
        ProjectName.setText("Styles:");
        stylesEdit = new Text(container, SWT.BORDER);
        stylesEdit.setBounds(getDluRect(150, 15, 300, 19));
        label = new Label(container, SWT.RIGHT);
        label.setBounds(getDluRect(20, 43, 120, 13));
        label.setText("Frames:");
        framesFileEdit = new Text(container, SWT.BORDER);
        framesFileEdit.setBounds(getDluRect(150, 40, 300, 19));
        label1 = new Label(container, SWT.RIGHT);
        label1.setBounds(getDluRect(20, 65, 120, 12));
        label1.setText("Model data:");
        label21 = new Label(container, SWT.RIGHT);
        label21.setBounds(getDluRect(21, 90, 120, 13));
        label21.setText("Validation rules:");
        label22 = new Label(container, SWT.RIGHT);
        label22.setBounds(getDluRect(21, 120, 120, 13));
        label22.setText("Look and feel:");
        synthLabel = new Label(container, SWT.RIGHT);
        synthLabel.setBounds(getDluRect(20, 143, 120, 13));
        synthLabel.setText("Synth file:");
        createCombo(container);
        modelFileEdit = new Text(container, SWT.BORDER);
        modelFileEdit.setBounds(getDluRect(150, 65, 300, 19));
        validationFileEdit = new Text(container, SWT.BORDER);
        validationFileEdit.setBounds(getDluRect(150, 90, 300, 19));
        synthEdit = new Text(container, SWT.BORDER);
        synthEdit.setBounds(getDluRect(151, 140, 301, 19));
        picStyleBtn = new Button(container, SWT.NONE);
        picStyleBtn.setBounds(getDluRect(460, 15, 24, 23));
        picStyleBtn.setText("...");
        picStyleBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse(e);
            }
        });
        picFramesBtn = new Button(container, SWT.NONE);
        picFramesBtn.setBounds(getDluRect(461, 42, 24, 23));
        picFramesBtn.setText("...");
        picFramesBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse(e);
            }
        });
        picModelEdit = new Button(container, SWT.NONE);
        picModelEdit.setBounds(getDluRect(461, 67, 24, 23));
        picModelEdit.setText("...");
        picModelEdit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse(e);
            }
        });
        picValidationsBtn = new Button(container, SWT.NONE);
        picValidationsBtn.setBounds(getDluRect(461, 92, 24, 23));
        picValidationsBtn.setText("...");
        picValidationsBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse(e);
            }
        });
        picSynthEdit = new Button(container, SWT.NONE);
        picSynthEdit.setBounds(getDluRect(462, 141, 24, 23));
        picSynthEdit.setText("...");
        picSynthEdit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleBrowse(e);
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
    }

    /**
     * container method initializes combo    
     *
     */
    private void createCombo(Composite container) {
        lafCombo = new Combo(container, SWT.NONE);
        lafCombo.setBounds(getDluRect(150, 115, 302, 21));
        lafCombo.add("System");
        lafCombo.add("Synth");
        lafCombo.add("JGoodies");
        lafCombo.add("Windows");
        lafCombo.add("Substance");
        lafCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                String selLaf = lafCombo.getItem(lafCombo.getSelectionIndex());
                boolean selected = selLaf.equals("Synth");
                synthEdit.setVisible(selected);
                picSynthEdit.setVisible(selected);
                synthLabel.setVisible(selected);
            }
        });
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */
    private void initialize() {
        stylesEdit.setText(NewAriaProjectWizard.PROP_STYLE_FILE);
        framesFileEdit.setText(NewAriaProjectWizard.PROP_FRAMES_FILE);
        modelFileEdit.setText(NewAriaProjectWizard.PROP_MODEL_FILE);
        validationFileEdit.setText(NewAriaProjectWizard.PROP_VALIDATION_FILE);
        lafCombo.select(0);
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */
    private void handleBrowse(SelectionEvent e) {
        FileDialog dialog = new FileDialog(getShell());
        String[] xmlExtensions = { ".xml" };
        String[] synthExtensions = { ".xsynth" };
        dialog.setFilterExtensions(e.getSource() == picSynthEdit ? synthExtensions : xmlExtensions);
        dialog.open();
        String selectedFile = dialog.getFileName();
        if ((selectedFile != null) && (selectedFile.length() > 0)) {
            if (e.getSource() == picStyleBtn) {
                stylesEdit.setText(selectedFile);
            } else if (e.getSource() == picFramesBtn) {
                framesFileEdit.setText(selectedFile);
            } else if (e.getSource() == picModelEdit) {
                modelFileEdit.setText(selectedFile);
            } else if (e.getSource() == picValidationsBtn) {
                validationFileEdit.setText(selectedFile);
            } else if (e.getSource() == picSynthEdit) {
                synthEdit.setText(selectedFile);
            }
        }
        dialogChanged();
    }

    /**
     * Ensures that both text fields are set.
     */
    private void dialogChanged() {
        saveState();
        updateStatus(null);
    }

    public void saveState() {
        wizard.setWizardProperty("PROP_STYLE_FILE", stylesEdit.getText());
        wizard.setWizardProperty("PROP_MODEL_FILE", modelFileEdit.getText());
        wizard.setWizardProperty("PROP_FRAMES_FILE", framesFileEdit.getText());
        wizard.setWizardProperty("PROP_VALIDATION_FILE", validationFileEdit.getText());
        wizard.setWizardProperty("PROP_SYNTH_FILE", synthEdit.getText());
        wizard.setWizardProperty("PROP_LAF", lafCombo.getItem(lafCombo.getSelectionIndex()));
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public Rectangle getDluRect(int x, int y, int w, int h) {
        return new Rectangle((int) (0.5F + x * dlux / 1.25F), (int) (0.5F + y * dluy / 1.625F), (int) (0.5F + w * dlux / 1.25F), (int) (0.5F + h * dluy / 1.625));
    }
}
