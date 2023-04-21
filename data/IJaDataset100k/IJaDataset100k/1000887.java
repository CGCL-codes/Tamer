package org.kalypso.nofdpidss.timeseries.wizard.repository.folder;

import java.io.File;
import java.io.IOException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.timeseries.i18n.Messages;
import org.kalypso.ogc.sensor.zml.repository.ZmlObservationRepository;

/**
 * @author Dirk Kuch
 */
public class WizardTimeSeriesAddRepositoryFolder extends Wizard implements IWorkbenchWizard {

    private PageSetFolderName m_pageFolderName;

    private final ZmlObservationRepository m_repos;

    private final File m_base;

    public WizardTimeSeriesAddRepositoryFolder(final ZmlObservationRepository repos, final File file) {
        m_repos = repos;
        m_base = file;
        setHelpAvailable(false);
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
    @Override
    public void addPages() {
        setWindowTitle(Messages.WizardTimeSeriesAddRepositoryFolder_0);
        m_pageFolderName = new PageSetFolderName(m_base);
        addPage(m_pageFolderName);
    }

    /**
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   *      org.eclipse.jface.viewers.IStructuredSelection)
   */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
    @Override
    public boolean performFinish() {
        final File file = new File(m_base, m_pageFolderName.m_tName.getText());
        try {
            m_repos.makeItem(file);
        } catch (final IOException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return false;
        }
        return true;
    }
}
