package org.kalypso.nofdpidss.timeseries.wizard.repository.hydrograph;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.timeseries.wizard.repository.hydrograph.pages.PageSelectCSV;
import org.kalypso.nofdpidss.timeseries.wizard.repository.hydrograph.worker.ImportHydrographWorker;
import org.kalypso.repository.file.FileItem;

/**
 * @author Dirk Kuch
 */
public class WizardImportHydrograph extends Wizard implements IWorkbenchWizard {

    private PageSelectCSV m_page;

    private final FileItem m_item;

    public WizardImportHydrograph(final FileItem item) {
        m_item = item;
        setHelpAvailable(false);
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
    @Override
    public void addPages() {
        m_page = new PageSelectCSV(m_item);
        addPage(m_page);
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
    @Override
    public boolean performFinish() {
        try {
            final ImportHydrographWorker worker = new ImportHydrographWorker(m_page);
            worker.execute(new NullProgressMonitor());
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return false;
        }
        return true;
    }

    /**
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   *      org.eclipse.jface.viewers.IStructuredSelection)
   */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }
}
