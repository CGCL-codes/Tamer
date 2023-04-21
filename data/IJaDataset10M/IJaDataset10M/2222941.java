package org.vikamine.app.rcp.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.FilteredList;
import org.vikamine.app.rcp.Activator;

/**
 * Wizard page to manage data sets to import.
 * 
 * @author Martin Becker
 * 
 */
public class DatasetsPage extends WizardPage {

    /** The Constant FILTER_EXTENSIONS. */
    public static final String[] FILTER_EXTENSIONS = new String[] { "*.jar;*.xml;*.csv;*.xls;*.arff;*.log", "*.jar", "*.xml", "*.csv", "*.xls", "*.arff", "*.log" };

    /**
     * Listener to trigger file selection when the add button is clicked.
     */
    private SelectionListener addDatasetsListener = new SelectionListener() {

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            String indexFile = datasetFileDialog.open();
            if (indexFile == null) return;
            File dir = new File(indexFile).getParentFile();
            for (String file : datasetFileDialog.getFileNames()) {
                datasetFiles.add(new File(dir, file).getAbsolutePath());
            }
            datasetFileList.setElements(datasetFiles.toArray());
            validatePage();
        }
    };

    /**
     * Listener to trigger removing selected data set files from the data set
     * file list.
     */
    private SelectionListener removeDatasetsListener = new SelectionListener() {

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            for (Object file : datasetFileList.getSelection()) {
                datasetFiles.remove(file);
            }
            datasetFileList.setElements(datasetFiles.toArray());
            validatePage();
        }
    };

    /** Resource bundle (plugin.properties). */
    private ResourceBundle resourceBundle = Platform.getResourceBundle(Platform.getBundle(Activator.PLUGIN_ID));

    /** File dialog to call when adding data sets. */
    private FileDialog datasetFileDialog;

    /** SWT widget representing the data file list. */
    private FilteredList datasetFileList;

    /** The data set file list. */
    private ArrayList<String> datasetFiles;

    /**
     * Constructor.
     * 
     * @param pageName
     *            page name
     */
    protected DatasetsPage(String pageName) {
        super(pageName);
        setTitle(resourceBundle.getString("vikamine.wizards.newProjectWizard.DatasetsPage.title"));
        setDescription(resourceBundle.getString("vikamine.wizards.newProjectWizard.DatasetsPage.description"));
    }

    @Override
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        createDatasetsGroup(composite);
        setControl(composite);
    }

    /**
     * Creates the group (SWT widget) taking care of data set management.
     * 
     * @param composite
     *            the composite to add the data set management to
     */
    private void createDatasetsGroup(Composite composite) {
        datasetFiles = new ArrayList<String>();
        datasetFileDialog = new FileDialog(getShell(), SWT.MULTI);
        datasetFileDialog.setFilterExtensions(FILTER_EXTENSIONS);
        Group datasetGroup = new Group(composite, SWT.NONE);
        datasetGroup.setText(resourceBundle.getString("vikamine.wizards.newProjectWizard.DatasetsPage.datasets"));
        GridLayout datasetGroupLayout = new GridLayout(2, false);
        datasetGroup.setLayout(datasetGroupLayout);
        GridData datasetGroupGridData = new GridData(GridData.FILL_HORIZONTAL);
        datasetGroup.setLayoutData(datasetGroupGridData);
        LabelProvider labelProvider = new LabelProvider();
        datasetFileList = new FilteredList(datasetGroup, SWT.MULTI | SWT.BORDER, labelProvider, true, true, true);
        GridData datasetGroupFileListGridData = new GridData(GridData.FILL_BOTH);
        datasetGroupFileListGridData.verticalSpan = 3;
        datasetFileList.setLayoutData(datasetGroupFileListGridData);
        Button datasetGroupAddButton = new Button(datasetGroup, SWT.PUSH | SWT.FILL);
        datasetGroupAddButton.setText(resourceBundle.getString("vikamine.wizards.newProjectWizard.DatasetsPage.addButton"));
        datasetGroupAddButton.addSelectionListener(addDatasetsListener);
        GridData datasetGroupAddButtonGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
        datasetGroupAddButton.setLayoutData(datasetGroupAddButtonGridData);
        Button datasetGroupRemoveButton = new Button(datasetGroup, SWT.PUSH);
        datasetGroupRemoveButton.setText(resourceBundle.getString("vikamine.wizards.newProjectWizard.DatasetsPage.removeButton"));
        datasetGroupRemoveButton.addSelectionListener(removeDatasetsListener);
        GridData datasetGroupRemoveButtonGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
        datasetGroupRemoveButton.setLayoutData(datasetGroupRemoveButtonGridData);
    }

    /**
     * Returns all data set files as a String array.
     * 
     * @return all data set files as a String array
     */
    public String[] getDatasetFiles() {
        return datasetFiles.toArray(new String[] {});
    }

    /**
     * Validates page. Sets page as complete and takes care of setting
     * appropriate messages.
     */
    private void validatePage() {
        if (datasetFiles.size() > 0) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
            setMessage(resourceBundle.getString("vikamine.wizards.newProjectWizard.DatasetsPage.information"), DialogPage.INFORMATION);
        }
    }
}
