package com.abso.sunlight.explorer.wizards;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * Abstract base class for the page to be included in a export wizard.
 */
public abstract class ExportFileWizardPage extends WizardPage implements Listener {

    /** Key used to store the destination file setting. */
    private static final String TO_FILE_SETTING = "TO_FILE";

    /** Key used to store if legislators' photos must be included. */
    private static final String INCLUDE_PHOTOS_SETTING = "INCLUDE_PHOTOS";

    /** Indicates if the initial setting is to export all the legislators instead of the selected ones. */
    private boolean initialExportAll;

    /** If <code>true</code> the option to export the selection is available. */
    private boolean enableExportSelection;

    /** The text field with the output file path. */
    private Text toFileText;

    /** The button to browse in the file system. */
    private Button browseButton;

    /** The button to export all legislators. */
    private Button allRadioButton;

    /** The button to export selected legislators. */
    private Button selectedRadioButton;

    /** The checkbox to export also photos. */
    private Button photosCheckBox;

    /**
     * Constructs a new wizard page. 
     * 
     * @param pageName   the name of the page.
     */
    public ExportFileWizardPage(String pageName) {
        super(pageName);
    }

    /**
     * Sets the export options.
     * 
     * @param initialExportAll   indicates if the initial setting is to export all the legislators instead of the selected ones.
     * @param enableExportSelection   if <code>true</code> the option to export the selection is available.
     */
    public void setExportOptions(boolean initialExportAll, boolean enableExportSelection) {
        this.initialExportAll = initialExportAll;
        this.enableExportSelection = enableExportSelection;
    }

    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        Composite mainArea = new Composite(parent, SWT.NONE);
        mainArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainArea.setLayout(new GridLayout(1, false));
        Composite toFileArea = new Composite(mainArea, SWT.NONE);
        toFileArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        toFileArea.setLayout(new GridLayout(3, false));
        Label toFileLabel = new Label(toFileArea, SWT.NONE);
        toFileLabel.setText("To file:");
        toFileLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        toFileText = new Text(toFileArea, SWT.SINGLE | SWT.BORDER);
        toFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        toFileText.addListener(SWT.Modify, this);
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String initialToFilePath = settings.get(TO_FILE_SETTING);
            if (!StringUtils.isBlank(initialToFilePath)) {
                toFileText.setText(initialToFilePath);
                toFileText.selectAll();
            }
        }
        browseButton = new Button(toFileArea, SWT.PUSH);
        browseButton.setText("Browse...");
        browseButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        browseButton.addListener(SWT.Selection, this);
        Group exportGroup = new Group(mainArea, SWT.NONE);
        exportGroup.setText("Export");
        exportGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        exportGroup.setLayout(new GridLayout(1, false));
        allRadioButton = createRadioButton("All Legislators", exportGroup);
        selectedRadioButton = createRadioButton("Selection", exportGroup);
        if (initialExportAll || !enableExportSelection) {
            allRadioButton.setSelection(true);
        } else {
            selectedRadioButton.setSelection(true);
        }
        if (!enableExportSelection) {
            allRadioButton.setEnabled(false);
            selectedRadioButton.setEnabled(false);
        }
        if (supportsPhotos()) {
            Group photoGroup = new Group(mainArea, SWT.NONE);
            photoGroup.setText("Photos");
            photoGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            photoGroup.setLayout(new GridLayout(1, false));
            photosCheckBox = createCheckBox("Include Photos", photoGroup);
            photosCheckBox.setSelection(true);
            if (settings != null) {
                boolean initialIncludePhotos = settings.getBoolean(INCLUDE_PHOTOS_SETTING);
                photosCheckBox.setSelection(initialIncludePhotos);
            }
        }
        setControl(mainArea);
        setPageComplete(detectPageComplete(false));
        setTitle(getWizardPageTitle());
        setMessage(getDefaultMessage());
        Dialog.applyDialogFont(mainArea);
    }

    /**
     * Creates a radio button.
     * 
     * @param text
     *            the button's text.
     * @param parent
     *            the parent control.
     * @return the newly created radio button.
     */
    private Button createRadioButton(String text, Composite parent) {
        Button radioButton = new Button(parent, SWT.RADIO);
        radioButton.setText(text);
        return radioButton;
    }

    /**
     * Creates a radio button.
     * 
     * @param text
     *            the button's text.
     * @param parent
     *            the parent control.
     * @return the newly created radio button.
     */
    private Button createCheckBox(String text, Composite parent) {
        Button checkBox = new Button(parent, SWT.CHECK);
        checkBox.setText(text);
        return checkBox;
    }

    /**
     * Handles a widget's event.
     * 
     * @param event
     *            the widget's event.
     */
    public void handleEvent(Event event) {
        Widget source = event.widget;
        if (source == toFileText) {
            handleToFileTextModifiedEvent();
        } else if (source == browseButton) {
            handleBrowseButtonPressed();
        }
    }

    /** Handles the event generated by the click on the browse button. */
    private void handleBrowseButtonPressed() {
        FileDialog dialog = new FileDialog(getContainer().getShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] { "*." + getExtension() });
        dialog.setText(getSaveDialogTitle());
        String file = dialog.open();
        if (file != null) {
            setErrorMessage(null);
            IPath fPath = new Path(file);
            if (fPath.getFileExtension() == null) {
                fPath = fPath.addFileExtension(getExtension());
            } else if (!fPath.getFileExtension().equals(getExtension())) {
                fPath = fPath.addFileExtension(getExtension());
            }
            toFileText.setText(fPath.toOSString());
        }
    }

    /** Handles the event generated by a change on the file text. */
    private void handleToFileTextModifiedEvent() {
        setPageComplete(detectPageComplete(true));
    }

    /**
     * Detects if the page is complete.
     * 
     * @param showErrorMessage
     *            indicates if a error message must be shown if the page is incomplete.
     * @return if the page is complete.
     */
    private boolean detectPageComplete(boolean showErrorMessage) {
        String filepath = toFileText.getText().trim();
        if (filepath.equals("")) {
            if (showErrorMessage) {
                setErrorMessage("Please specify a destination file.");
            }
            return false;
        }
        IPath path = new Path(filepath);
        if (!path.removeLastSegments(1).toFile().exists()) {
            if (showErrorMessage) {
                setErrorMessage("The destination directory does not exist.");
            }
            return false;
        }
        setErrorMessage(null);
        setMessage(getDefaultMessage());
        return true;
    }

    /**
     * Returns the selected destination file.
     * 
     * @return the selected destination file.
     */
    public File getDestinationFile() {
        return new File(toFileText.getText());
    }

    /**
     * Returns if all input legislators must be exported.
     * 
     * @return <code>true</code> to export all input legislators, <code>false</code> to export only the selected ones.
     */
    public boolean isExportAll() {
        return allRadioButton.getSelection();
    }

    /**
     * Returns if legislators' photos must be included in the exported data.
     * 
     * @return <code>true</code> to export also photos.
     */
    public boolean isIncludePhotos() {
        return supportsPhotos() && photosCheckBox.getSelection();
    }

    /**
     * Returns the title of the wizard's page.
     * 
     * @return the title of the wizard's page.
     */
    protected abstract String getWizardPageTitle();

    /**
     * Returns the exported file extension.
     * 
     * @return the extension of exported files.
     */
    protected abstract String getExtension();

    /**
     * Returns the title of the file dialog handling the selection of the file to save.
     * 
     * @return the dialog's title.
     */
    protected abstract String getSaveDialogTitle();

    /**
     * Returns the default (initial) page message.
     * 
     * @return the default message.
     */
    protected abstract String getDefaultMessage();

    /**
     * Returns if the export supports the inclusion of photos.
     * 
     * @return if the exported output supports the inclusion of photos.
     */
    protected abstract boolean supportsPhotos();

    /** Saves the page's settings. */
    public void saveSettings() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            settings.put(TO_FILE_SETTING, getDestinationFile().getAbsolutePath());
            if (supportsPhotos()) {
                settings.put(INCLUDE_PHOTOS_SETTING, photosCheckBox.getSelection());
            }
        }
    }
}
