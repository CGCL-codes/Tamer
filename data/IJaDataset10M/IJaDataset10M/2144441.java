package org.parallelj.mda.generator.controlflow.ui;

import static org.eclipse.jface.dialogs.IDialogConstants.CANCEL_LABEL;
import static org.eclipse.jface.dialogs.IDialogConstants.ENTRY_FIELD_WIDTH;
import static org.eclipse.jface.dialogs.IDialogConstants.OK_ID;
import static org.eclipse.jface.dialogs.IDialogConstants.OK_LABEL;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.parallelj.mda.generator.controlflow.Activator;
import org.parallelj.mda.generator.controlflow.util.Constants;

/**
 * SelectionDialog, adapted for ParallelJ, since it is able to retrieve all
 * Diagram (*.ecmd) models from a project
 * 
 * @author Atos Worldline
 * 
 */
public abstract class SelectionDialog extends MessageDialog {

    private static final String DIALOG_SETTINGS_SECTION = "ClasDiagramsDialogSettings";

    private static final String DIALOG_ORIGIN_X = "DIALOG_X_ORIGIN";

    private static final String DIALOG_ORIGIN_Y = "DIALOG_Y_ORIGIN";

    private static final String DIALOG_WIDTH = "DIALOG_WIDTH";

    private static final String DIALOG_HEIGHT = "DIALOG_HEIGHT";

    private static final String TOGGLE_SELECTED = "TOGGLE_SELECTED";

    private Button allButton, selectButton;

    protected CheckboxTableViewer modelsTable;

    protected IProject currentProject = null;

    protected List<IFile> selectedModels = null;

    protected List<IFile> allModels = null;

    private SelectionDialogMessages customMessages = null;

    protected IWorkbenchWindow window = null;

    /**
	 * Creates a new class diagrams dialog.
	 * 
	 * @param window
	 *            the window to create it in
	 * @param selection
	 *            the currently selected project
	 */
    public SelectionDialog(IWorkbenchWindow window, IProject selectedProject, SelectionDialogMessages customMessages) {
        super(window.getShell(), customMessages.getDialogTitle(), null, customMessages.getDialogMessage(), QUESTION, new String[] { OK_LABEL, CANCEL_LABEL }, 0);
        this.currentProject = selectedProject;
        this.selectedModels = new ArrayList<IFile>(3);
        this.allModels = new ArrayList<IFile>(3);
        this.customMessages = customMessages;
        this.window = window;
        setShellStyle(SWT.RESIZE | getShellStyle());
    }

    protected void buttonPressed(int buttonId) {
        final boolean openAll = allButton.getSelection();
        super.buttonPressed(buttonId);
        if (buttonId != OK_ID) {
            return;
        }
        performOperation(openAll);
    }

    protected Control createCustomArea(Composite parent) {
        Composite area = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;
        area.setLayout(layout);
        area.setLayoutData(new GridData(GridData.FILL_BOTH));
        SelectionListener updateEnablement = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                updateEnablement();
            }
        };
        IDialogSettings settings = getDialogSettings(DIALOG_SETTINGS_SECTION);
        boolean selectSelectedButton = settings.getBoolean(TOGGLE_SELECTED);
        allButton = new Button(area, SWT.RADIO);
        String projectName = currentProject != null ? currentProject.getName() : null;
        allButton.setText(this.customMessages.getAllButtonMessage(projectName));
        allButton.setSelection(!selectSelectedButton);
        allButton.addSelectionListener(updateEnablement);
        selectButton = new Button(area, SWT.RADIO);
        selectButton.setText(this.customMessages.getSelectButtonMessage());
        selectButton.setSelection(selectSelectedButton);
        selectButton.addSelectionListener(updateEnablement);
        createmodelSelectionTable(area);
        modelsTable.getTable().setEnabled(selectSelectedButton);
        return area;
    }

    private void searchModelsInFolder(IFolder folder) {
        if (folder.exists()) {
            try {
                for (IResource resource : folder.members()) {
                    if (resource instanceof IFile && Constants.CFLOW_MODEL_DIAGRAM_EXTENSION.equals(((IFile) resource).getFileExtension())) {
                        modelsTable.add(resource);
                        allModels.add((IFile) resource);
                    } else if (resource instanceof IFolder) {
                        searchModelsInFolder((IFolder) resource);
                    }
                }
            } catch (CoreException ce) {
                Activator.getInstance().logError("No models found in folder " + folder, ce);
            }
        }
    }

    private void createmodelSelectionTable(Composite radioGroup) {
        modelsTable = CheckboxTableViewer.newCheckList(radioGroup, SWT.BORDER);
        modelsTable.setLabelProvider(new SelectionDialogLabelProvider());
        IFolder folder = this.currentProject.getFolder(Constants.Dirs.DIR_MAIN_RESOURCES);
        searchModelsInFolder(folder);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        data.widthHint = ENTRY_FIELD_WIDTH;
        data.heightHint = ENTRY_FIELD_WIDTH;
        modelsTable.getTable().setLayoutData(data);
        modelsTable.getTable().setEnabled(selectButton.getSelection());
        modelsTable.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                if (event.getChecked() && !selectedModels.contains(event.getElement())) {
                    selectedModels.add((IFile) event.getElement());
                } else if (!event.getChecked() && selectedModels.contains(event.getElement())) {
                    selectedModels.remove((IFile) event.getElement());
                }
                updateEnablement();
            }
        });
    }

    /**
	 * Performs the actual operation.
	 */
    protected abstract void performOperation(boolean allSelected);

    /**
	 * Updates the enablement of the dialog's ok button based on the current
	 * choices in the dialog.
	 */
    protected void updateEnablement() {
        modelsTable.getTable().setEnabled(selectButton.getSelection());
        boolean enabled = allButton.getSelection() || selectedModels.size() > 0;
        getButton(OK).setEnabled(enabled);
    }

    public boolean close() {
        persistDialogSettings(getShell(), DIALOG_SETTINGS_SECTION);
        return super.close();
    }

    protected Point getInitialLocation(Point initialSize) {
        Point p = getInitialLocation(DIALOG_SETTINGS_SECTION);
        return p != null ? p : super.getInitialLocation(initialSize);
    }

    protected Point getInitialSize() {
        Point p = super.getInitialSize();
        return getInitialSize(DIALOG_SETTINGS_SECTION, p);
    }

    /**
	 * Returns the initial location which is persisted in the Ant UI Plugin
	 * dialog settings under the provided dialog setttings section name. If
	 * location is not persisted in the settings, the <code>null</code> is
	 * returned.
	 * 
	 * @param dialogSettingsSectionName
	 *            The name of the dialog settings section
	 * @return The initial location or <code>null</code>
	 */
    public Point getInitialLocation(String dialogSettingsSectionName) {
        IDialogSettings settings = getDialogSettings(dialogSettingsSectionName);
        try {
            int x = settings.getInt(DIALOG_ORIGIN_X);
            int y = settings.getInt(DIALOG_ORIGIN_Y);
            return new Point(x, y);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    private IDialogSettings getDialogSettings(String dialogSettingsSectionName) {
        IDialogSettings settings = Activator.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(dialogSettingsSectionName);
        if (section == null) {
            section = settings.addNewSection(dialogSettingsSectionName);
        }
        return section;
    }

    /**
	 * Persists the location and dimensions of the shell and other user settings
	 * in the plugin's dialog settings under the provided dialog settings
	 * section name
	 * 
	 * @param shell
	 *            The shell whose geometry is to be stored
	 * @param dialogSettingsSectionName
	 *            The name of the dialog settings section
	 */
    private void persistDialogSettings(Shell shell, String dialogSettingsSectionName) {
        Point shellLocation = shell.getLocation();
        Point shellSize = shell.getSize();
        IDialogSettings settings = getDialogSettings(dialogSettingsSectionName);
        settings.put(DIALOG_ORIGIN_X, shellLocation.x);
        settings.put(DIALOG_ORIGIN_Y, shellLocation.y);
        settings.put(DIALOG_WIDTH, shellSize.x);
        settings.put(DIALOG_HEIGHT, shellSize.y);
        settings.put(TOGGLE_SELECTED, selectButton.getSelection());
    }

    /**
	 * Returns the initial size which is the larger of the
	 * <code>initialSize</code> or the size persisted in the Ant UI Plugin
	 * dialog settings under the provided dialog setttings section name. If no
	 * size is persisted in the settings, the <code>initialSize</code> is
	 * returned.
	 * 
	 * @param initialSize
	 *            The initialSize to compare against
	 * @param dialogSettingsSectionName
	 *            The name of the dialog settings section
	 * @return the initial size
	 */
    private Point getInitialSize(String dialogSettingsSectionName, Point initialSize) {
        IDialogSettings settings = getDialogSettings(dialogSettingsSectionName);
        try {
            int x, y;
            x = settings.getInt(DIALOG_WIDTH);
            y = settings.getInt(DIALOG_HEIGHT);
            return new Point(Math.max(x, initialSize.x), Math.max(y, initialSize.y));
        } catch (NumberFormatException e) {
        }
        return initialSize;
    }
}
