package com.cp.vaultclipse.ui.views.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import com.cp.vaultclipse.VaultClipsePlugin;
import com.cp.vaultclipse.dto.VaultTransactionDTO;
import com.cp.vaultclipse.dto.VaultTransactionItemDTO;
import com.cp.vaultclipse.i18n.Localization;
import com.cp.vaultclipse.preferences.PropertyStore;
import com.cp.vaultclipse.svc.VaultSvc;
import com.cp.vaultclipse.ui.views.MergeTreeView;
import com.cp.vaultclipse.ui.views.tree.MergeContentProvider;
import com.cp.vaultclipse.ui.views.tree.MergeTreeFile;
import com.cp.vaultclipse.ui.wizard.ExportWizard;
import com.cp.vaultclipse.ui.wizard.WizardMessages;

/**
 * Invoked when the checkout menu item is selected.
 * 
 * @author daniel.klco
 */
public class Checkout extends Action {

    protected Localization local = Localization.get(ExportWizard.class);

    private static final Logger log = Logger.getLogger(Checkout.class);

    /**
	 * Constructor for checkout action.
	 */
    public Checkout() {
        super();
    }

    /**
	 * @see IActionDelegate#run(IAction)
	 */
    public void run() {
        MergeTreeView view = MergeTreeView.get();
        final TreeViewer viewer = view.getViewer();
        final ISelection selection = viewer.getSelection();
        final Shell shell = view.getSite().getShell();
        PropertyStore prop = ((MergeContentProvider) viewer.getContentProvider()).getPropertyStore();
        List<VaultTransactionItemDTO> items = new ArrayList<VaultTransactionItemDTO>();
        if (VaultClipsePlugin.getDefault().getWorkbench().saveAllEditors(true)) {
            if (selection instanceof ITreeSelection) {
                ITreeSelection treeSelection = (ITreeSelection) selection;
                Iterator<?> selected = treeSelection.iterator();
                while (selected.hasNext()) {
                    Object selectionObj = selected.next();
                    if (selectionObj instanceof MergeTreeFile) {
                        VaultTransactionItemDTO item = new VaultTransactionItemDTO();
                        MergeTreeFile file = (MergeTreeFile) selectionObj;
                        if (".content.xml".equals(file.getName())) {
                            file = file.getParentFolder();
                        }
                        File localFile = file.getLocalFile();
                        if (localFile == null) {
                            try {
                                localFile = createLocalFile(file);
                            } catch (IOException e) {
                                log.warn("Exception creating local file", e);
                                continue;
                            }
                        }
                        item.setSelectedFile(localFile);
                        item.setTreePath(treeSelection.getPathsFor(selectionObj)[0]);
                        item.setJcrPath(file.getJCRPath());
                        items.add(item);
                    }
                }
            }
        }
        VaultTransactionDTO vtDTO = new VaultTransactionDTO();
        vtDTO.loadFromPropertyStore(prop);
        vtDTO.setItems(items);
        final VaultSvc vaultService = new VaultSvc(vtDTO);
        if (!vaultService.validateVaultDir()) {
            MessageDialog.openError(shell, local.getString(WizardMessages.VAULT_PATH_INCORRECT_TITLE.getKey()), local.getString(WizardMessages.VAULT_PATH_INCORRECT_MESSAGE.getKey()));
        }
        final String jobName = getJobName(vtDTO.getItems());
        log.debug("Creating job");
        Job job = new Job(jobName) {

            protected IStatus run(IProgressMonitor monitor) {
                log.debug("VaultClipse Checkout started");
                try {
                    vaultService.export(monitor, true);
                    if (selection instanceof ITreeSelection) {
                        ITreeSelection treeSelection = (ITreeSelection) selection;
                        Iterator<?> selected = treeSelection.iterator();
                        while (selected.hasNext()) {
                            Object selectionObj = selected.next();
                            if (selectionObj instanceof MergeTreeFile) {
                                MergeTreeFile file = (MergeTreeFile) selectionObj;
                                file.setMerged(true);
                            }
                        }
                    }
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            viewer.refresh();
                        }
                    });
                    log.debug("VaultClipse Checkout complete");
                    return Status.OK_STATUS;
                } catch (IllegalArgumentException e) {
                    log.error("Illegal argument exception.", e);
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            MessageDialog.openError(shell, local.getString(WizardMessages.ARGUMENT_EXCEPTION_TITLE.getKey()), local.getString(WizardMessages.ARGUMENT_EXCEPTION_MESSAGE.getKey()));
                        }
                    });
                    return Status.CANCEL_STATUS;
                } catch (IOException e) {
                    log.error("File IO Exception", e);
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            MessageDialog.openError(shell, local.getString(WizardMessages.FILE_IO_EXCEPTION_TITLE.getKey()), local.getString(WizardMessages.FILE_IO_EXCEPTION_MESSAGE.getKey()));
                        }
                    });
                    return Status.CANCEL_STATUS;
                } catch (final RuntimeException re) {
                    log.error("Vault failed", re);
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            MessageDialog.openError(shell, local.getString(WizardMessages.VAULT_FAILED_TITLE.getKey()), local.getString(WizardMessages.VAULT_FAILED_MESSAGE.getKey()) + re.getMessage());
                        }
                    });
                    return Status.CANCEL_STATUS;
                } catch (Exception e) {
                    log.error("Unexpected Exception occurred.", e);
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            MessageDialog.openError(shell, local.getString(WizardMessages.UNEXPECTED_EXCEPTION_TITLE.getKey()), local.getString(WizardMessages.UNEXPECTED_EXCEPTION_MESSAGE.getKey()));
                        }
                    });
                    return Status.CANCEL_STATUS;
                }
            }
        };
        job.addJobChangeListener(new JobChangeAdapter() {

            public void done(IJobChangeEvent event) {
                if (event.getResult().isOK()) {
                    log.info(jobName + " " + local.getString(WizardMessages.COMPLETED_SUCCESSFULLY.getKey()));
                } else {
                    log.info(jobName + " " + local.getString(WizardMessages.FAILED.getKey()));
                }
            }
        });
        job.setSystem(false);
        job.setUser(true);
        job.schedule();
    }

    protected File createLocalFile(MergeTreeFile file) throws IOException {
        if (file.getLocalFile() != null) {
            return file.getLocalFile();
        } else {
            File parentFile = createLocalFile(file.getParentFolder());
            File localFile = new File(parentFile.getAbsolutePath() + File.separator + file.getFileName());
            if (file.getName().contains(".")) {
                localFile.createNewFile();
            } else {
                localFile.mkdir();
            }
            return localFile;
        }
    }

    /**
	 * Builds the job name for the current job.
	 * 
	 * @param items
	 *            the list of items to be handled in this job
	 * @return the job name
	 */
    protected String getJobName(List<VaultTransactionItemDTO> items) {
        String name = "Checkout ";
        for (int i = 0; i < items.size(); i++) {
            name += items.get(i).getJcrPath();
            if (i != items.size() - 1) {
                name += ", ";
            }
        }
        return name;
    }
}
