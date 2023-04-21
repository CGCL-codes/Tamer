package org.remotercp.ui.provisioning.editor.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.remotercp.common.tracker.IGenericServiceListener;
import org.remotercp.domain.provisioning.exception.RemoteOperationException;
import org.remotercp.domain.provisioning.service.IProvisioningService;
import org.remotercp.domain.provisioning.status.ProvisioningResult;
import org.remotercp.domain.provisioning.version.IVersionedId;
import org.remotercp.ui.errorhandling.view.ErrorView;
import org.remotercp.ui.provisioning.ProvisioningActivator;
import org.remotercp.ui.provisioning.editor.ProvisioningEditorInput;
import org.remotercp.ui.provisioning.editor.ui.tree.InstalledFeaturesTreeCreator;
import org.remotercp.ui.provisioning.editor.ui.tree.nodes.CommonFeaturesTreeNode;
import org.remotercp.ui.provisioning.editor.ui.tree.nodes.DifferentFeaturesTreeNode;

public class ProvisioningEditor extends EditorPart implements IGenericServiceListener<IProvisioningService> {

    public static final String ID = "org.remotercp.ui.provisioning.ProvisioningEditor";

    private TabItem installedFeatureTabItem;

    private TabItem availableFeaturesTabItem;

    private TabFolder featuresFolder;

    private StackLayout installedFeaturesStackLayout;

    private StackLayout availableFeaturesStackLayout;

    private List<IProvisioningService> installedFeaturesServiceList;

    private InstalledFeaturesUI installedFeaturesComposite;

    private UpdateFeaturesTool updateFeaturesTool;

    private P2AvailableSoftwareTool availableSoftwareTool;

    private Composite installedFeaturesMainComposite;

    private ProvisioningResultUI updateProgressReportComposite;

    private ID[] userIDs;

    private Composite availableFeaturesMainComposite;

    private ProvisioningResultUI installProgressReportComposite;

    protected enum ProvisioningOperations {

        UPDATE, UNINSTALL, INSTALL
    }

    ;

    public ProvisioningEditor() {
        this.installedFeaturesStackLayout = new StackLayout();
        this.availableFeaturesStackLayout = new StackLayout();
        installedFeaturesServiceList = Collections.synchronizedList(new ArrayList<IProvisioningService>());
    }

    public void bindService(IProvisioningService service) {
        this.installedFeaturesServiceList.add(service);
    }

    public void unbindService(IProvisioningService service) {
        this.installedFeaturesServiceList.remove(service);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        userIDs = ((ProvisioningEditorInput) getEditorInput()).getUserIDs();
        ProvisioningActivator.getDefault().addInstallFeatureServiceListener(this, userIDs);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite main = new Composite(parent, SWT.None);
        main.setLayout(new GridLayout(1, false));
        GridDataFactory.fillDefaults().grab(true, true).applyTo(main);
        {
            this.featuresFolder = new TabFolder(main, SWT.BORDER);
            GridDataFactory.fillDefaults().grab(true, true).applyTo(this.featuresFolder);
            {
                this.installedFeatureTabItem = new TabItem(this.featuresFolder, SWT.BORDER);
                this.installedFeatureTabItem.setText("Installed Features");
                installedFeaturesMainComposite = new Composite(this.featuresFolder, SWT.None);
                installedFeaturesMainComposite.setLayout(installedFeaturesStackLayout);
                GridDataFactory.fillDefaults().grab(true, true).applyTo(installedFeaturesMainComposite);
                {
                    this.installedFeaturesComposite = new InstalledFeaturesUI(this.installedFeaturesMainComposite, SWT.None);
                    for (IProvisioningService service : this.installedFeaturesServiceList) {
                        this.installedFeaturesComposite.bindService(service);
                    }
                    this.updateFeaturesTool = new UpdateFeaturesTool(this.installedFeaturesMainComposite, userIDs);
                    this.updateProgressReportComposite = new ProvisioningResultUI(this.installedFeaturesMainComposite, SWT.None);
                }
                this.installedFeatureTabItem.setControl(installedFeaturesMainComposite);
                installedFeaturesStackLayout.topControl = installedFeaturesComposite.getMainControl();
                installedFeaturesMainComposite.layout();
            }
            {
                this.availableFeaturesTabItem = new TabItem(this.featuresFolder, SWT.BORDER);
                this.availableFeaturesTabItem.setText("Available Features");
                {
                    this.availableFeaturesMainComposite = new Composite(this.featuresFolder, SWT.None);
                    this.availableFeaturesMainComposite.setLayout(this.availableFeaturesStackLayout);
                    GridDataFactory.fillDefaults().grab(true, true).applyTo(availableFeaturesMainComposite);
                    {
                        availableSoftwareTool = new P2AvailableSoftwareTool(availableFeaturesMainComposite, SWT.None, userIDs);
                        this.installProgressReportComposite = new ProvisioningResultUI(availableFeaturesMainComposite, SWT.None);
                    }
                    this.availableFeaturesTabItem.setControl(availableFeaturesMainComposite);
                    availableFeaturesStackLayout.topControl = availableSoftwareTool.getMainControl();
                    availableFeaturesMainComposite.layout();
                }
            }
        }
        findInstalledFeatures();
        createListener();
    }

    private void createListener() {
        installedFeaturesComposite.addButtonListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleCheckUpdates();
            }
        }, InstalledFeaturesUI.Buttons.CHECK_FOR_UPDATES);
        installedFeaturesComposite.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("UNINSTALL")) {
                    ProvisioningEditor.this.updateProgressReportComposite.setInput((List<ProvisioningResult>) evt.getNewValue());
                    ProvisioningEditor.this.installedFeaturesStackLayout.topControl = updateProgressReportComposite.getMainControl();
                    ProvisioningEditor.this.installedFeaturesMainComposite.layout();
                }
            }
        });
        updateFeaturesTool.addButtonListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ProvisioningEditor.this.installedFeaturesStackLayout.topControl = installedFeaturesComposite.getMainControl();
                ProvisioningEditor.this.installedFeaturesMainComposite.layout();
            }
        }, UpdateFeaturesTool.Buttons.BACK);
        updateFeaturesTool.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("UPDATE")) {
                    ProvisioningEditor.this.updateProgressReportComposite.setInput((List<ProvisioningResult>) evt.getNewValue());
                    ProvisioningEditor.this.installedFeaturesStackLayout.topControl = updateProgressReportComposite.getMainControl();
                    ProvisioningEditor.this.installedFeaturesMainComposite.layout();
                }
            }
        });
        availableSoftwareTool.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                ProvisioningEditor.this.installProgressReportComposite.setInput((List<ProvisioningResult>) evt.getNewValue());
                ProvisioningEditor.this.availableFeaturesStackLayout.topControl = installProgressReportComposite.getMainControl();
                ProvisioningEditor.this.availableFeaturesMainComposite.layout();
            }
        });
    }

    protected void findInstalledFeatures() {
        Job getRemoteFeaturesJob = new Job("Retrieve remote  components") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                if (installedFeaturesServiceList.size() < userIDs.length) {
                    List<IStatus> status = findUnsatisfiedRemoteServicesForClients();
                    ErrorView.addError(status);
                } else {
                    handleInstalledFeatures(installedFeaturesServiceList, monitor);
                }
                return Status.OK_STATUS;
            }
        };
        getRemoteFeaturesJob.setUser(true);
        getRemoteFeaturesJob.schedule();
    }

    private List<IStatus> findUnsatisfiedRemoteServicesForClients() {
        List<IStatus> resultList = new ArrayList<IStatus>();
        List<ID> userList = Arrays.asList(userIDs);
        if (installedFeaturesServiceList.isEmpty()) {
            for (ID userID : userList) {
                IStatus status = new Status(IStatus.WARNING, ProvisioningActivator.PLUGIN_ID, "Unable to find IInstallFeaturesService for user: " + userID.getName());
                resultList.add(status);
            }
        } else {
            for (IProvisioningService installService : installedFeaturesServiceList) {
                try {
                    ID clientId = installService.getClientId();
                    if (!userList.contains(clientId)) {
                        IStatus status = new Status(IStatus.WARNING, ProvisioningActivator.PLUGIN_ID, "Unable to find IInstallFeaturesService for client: " + clientId.getName());
                        resultList.add(status);
                    }
                } catch (RemoteOperationException e) {
                    IStatus error = new Status(IStatus.ERROR, ProvisioningActivator.PLUGIN_ID, "Error while retrieving client id", e);
                    resultList.add(error);
                }
            }
        }
        return resultList;
    }

    private void handleCheckUpdates() {
        if (canPerformUpdateCheck(installedFeaturesComposite.getInstallableUnits())) {
            Collection<IVersionedId> featuresToUpdate = getFeaturesToUpate(installedFeaturesComposite.getInstallableUnits());
            this.updateFeaturesTool.setFeaturesToUpdate(featuresToUpdate);
            this.installedFeaturesStackLayout.topControl = this.updateFeaturesTool.getMainControl();
            this.installedFeaturesMainComposite.layout();
        }
    }

    private Collection<IVersionedId> getFeaturesToUpate(Set<CommonFeaturesTreeNode> featureToUpdateNodes) {
        Collection<IVersionedId> featuresToUpdate = new ArrayList<IVersionedId>();
        for (CommonFeaturesTreeNode node : featureToUpdateNodes) {
            featuresToUpdate.add((IVersionedId) node.getValue());
        }
        return featuresToUpdate;
    }

    private boolean canPerformUpdateCheck(Set<CommonFeaturesTreeNode> selectedFeatures) {
        if (selectedFeatures.size() > 1) {
            MessageBox unsupported = new MessageBox(installedFeaturesComposite.getMainControl().getShell(), SWT.ICON_ERROR);
            unsupported.setMessage("Multiple feature selections are not supported yet.");
            unsupported.open();
            return false;
        } else {
            return true;
        }
    }

    protected void handleInstalledFeatures(List<IProvisioningService> serviceList, IProgressMonitor monitor) {
        monitor.beginTask("Receive remote installed features", serviceList.size());
        InstalledFeaturesTreeCreator featuresHelper = new InstalledFeaturesTreeCreator();
        Collection<IStatus> errors = featuresHelper.handleInstalledFeatures(serviceList, monitor);
        if (!errors.isEmpty()) {
            ErrorView.addError(errors);
        }
        final Collection<CommonFeaturesTreeNode> commonFeaturesNodes = featuresHelper.getCommonFeaturesNodes();
        final Collection<DifferentFeaturesTreeNode> differentFeaturesNodes = featuresHelper.getDifferentFeaturesNodes();
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                installedFeaturesComposite.setCommonFeaturesInput(commonFeaturesNodes);
                installedFeaturesComposite.setDifferentFeaturesInput(differentFeaturesNodes);
            }
        });
    }

    @Override
    public void setFocus() {
        this.featuresFolder.setFocus();
    }
}
