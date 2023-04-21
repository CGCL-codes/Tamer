package q_impress.pmi.plugin.launchers;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import q_impress.pmi.lib.project.ModelingProject;
import q_impress.pmi.lib.services.ServiceException;
import q_impress.pmi.lib.services.loadsave.LoadingService;
import q_impress.pmi.plugin.utils.IModelReferenceContext;
import q_impress.pmi.plugin.utils.WorkspaceLocationSolver;

public class RootModelConfigurationTab extends AbstractLaunchConfigurationTab {

    private static final String TAB_NAME = "Root Model";

    /** The tab group showing this tab */
    private IModelReferenceContext tabGroup = null;

    /** The control holding the model file path */
    private Text modelFileText = null;

    /** The control holding the launch everything configuration */
    private Button launchEverythingButton = null;

    public RootModelConfigurationTab(IModelReferenceContext tabGroup) {
        this.tabGroup = tabGroup;
    }

    @Override
    public void createControl(Composite parent) {
        Composite topControl = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        topControl.setLayout(layout);
        setControl(topControl);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        Label l = new Label(topControl, SWT.NULL);
        l.setText("Model File");
        modelFileText = new Text(topControl, SWT.BORDER | SWT.SINGLE);
        modelFileText.setLayoutData(gd);
        modelFileText.setEditable(false);
        Button b = new Button(topControl, SWT.PUSH);
        b.setText("Browse...");
        b.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleProjectBrowse();
            }
        });
        launchEverythingButton = new Button(topControl, SWT.CHECK);
        launchEverythingButton.setSelection(true);
        launchEverythingButton.setEnabled(false);
        l = new Label(topControl, SWT.NULL);
        l.setText("Launch All ATOP Models");
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        l.setData(gd);
    }

    private void handleProjectBrowse() {
        ModelingProject model = null;
        ResourceSelectionDialog dialog = new ResourceSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), "Select a modeling project");
        if (dialog.open() == ContainerSelectionDialog.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                IFile res = (IFile) result[0];
                modelFileText.setText(res.getFullPath().toString());
                LoadingService service = new LoadingService();
                InputStream inStream = null;
                try {
                    inStream = res.getContents();
                    service.setInStream(inStream);
                    service.setLocationSolver(new WorkspaceLocationSolver());
                    service.initialize();
                    service.invoke();
                    if (service.getLoadedResource() instanceof ModelingProject) model = (ModelingProject) service.getLoadedResource(); else model = null;
                } catch (CoreException e) {
                    model = null;
                } catch (ServiceException e) {
                    model = null;
                } finally {
                    if (inStream != null) try {
                        inStream.close();
                    } catch (IOException e) {
                    }
                }
                tabGroup.setModel(model);
                setDirty(true);
                this.dialogChanged();
            } else updateStatus("You may select only one resource.");
        }
    }

    /**
	 * Ensures that both text fields are set.
	 */
    private void dialogChanged() {
        IResource modelFile = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getModelFileName()));
        if (getModelFileName().length() == 0) {
            updateStatus("Modeling project must be specified");
            return;
        }
        if (modelFile == null || !(modelFile instanceof IFile)) {
            updateStatus("Modeling project file must exist");
            return;
        }
        if (!modelFile.isAccessible()) {
            updateStatus("Modeling Project must be writable");
            return;
        }
        if (tabGroup.getModel() == null) {
            updateStatus("Invalid resource selected for Modeling project.");
            return;
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        updateLaunchConfigurationDialog();
    }

    public String getModelFileName() {
        return modelFileText.getText();
    }

    public boolean getLaunchEverything() {
        return launchEverythingButton.getSelection();
    }

    @Override
    public String getName() {
        return TAB_NAME;
    }

    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            modelFileText.setText(configuration.getAttribute(ModelLaunchDelegate.VAR_ROOT_MODEL_PATH, ""));
            launchEverythingButton.setEnabled(configuration.getAttribute(ModelLaunchDelegate.VAR_ROOT_LAUNCH_EVERYTHING, true));
        } catch (CoreException e) {
            Dialog d = new ErrorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Configuration Error", "Error while configuring launch.", new Status(IStatus.ERROR, "q_impress", e.getMessage(), e), 0);
            d.open();
        }
        dialogChanged();
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(ModelLaunchDelegate.VAR_ROOT_MODEL_PATH, getModelFileName());
        configuration.setAttribute(ModelLaunchDelegate.VAR_ROOT_LAUNCH_EVERYTHING, getLaunchEverything());
    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(ModelLaunchDelegate.VAR_ROOT_MODEL_PATH, "");
        configuration.setAttribute(ModelLaunchDelegate.VAR_ROOT_LAUNCH_EVERYTHING, true);
    }
}
