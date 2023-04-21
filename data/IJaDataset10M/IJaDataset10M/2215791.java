package org.kalypso.nofdpidss.profiles.wizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress;
import org.kalypso.contribs.eclipse.jface.operation.RunnableContextHelper;
import org.kalypso.nofdpidss.profiles.i18n.Messages;
import org.kalypso.nofdpidss.profiles.wizard.tripple.ProfileImportData;
import org.kalypso.nofdpidss.profiles.wizard.tripple.builder.IProfileImportBuilder;
import org.kalypso.nofdpidss.profiles.wizard.tripple.builder.TripleImportBuilder;
import org.kalypso.ogc.gml.mapmodel.CommandableWorkspace;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class WizardImportProfiles extends Wizard implements IWorkbenchWizard {

    private final ProfileImportData m_data;

    private IProfileImportBuilder m_builder;

    private final CommandableWorkspace m_workspace;

    public WizardImportProfiles(final SelectionEvent e, final Feature feature, final CommandableWorkspace workspace) {
        m_workspace = workspace;
        m_data = new ProfileImportData(e, feature);
        setNeedsProgressMonitor(true);
        setHelpAvailable(false);
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
    @Override
    public void addPages() {
        setWindowTitle(Messages.WizardImportProfiles_0);
        m_builder = new TripleImportBuilder(this, m_data, m_workspace);
        m_builder.addPages();
    }

    /**
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   *      org.eclipse.jface.viewers.IStructuredSelection)
   */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }

    @Override
    public boolean performFinish() {
        final ICoreRunnableWithProgress worker = m_builder.getPerformFinishWorker();
        final IStatus status = RunnableContextHelper.execute(getContainer(), true, false, worker);
        ErrorDialog.openError(getShell(), getWindowTitle(), Messages.WizardImportProfiles_1, status);
        if (status.isOK()) return true;
        return false;
    }
}
