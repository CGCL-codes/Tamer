package org.kalypso.nofdpidss.timeseries.wizard.repository.timeseries.worker;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.WorkspaceSync;
import org.kalypso.nofdpidss.timeseries.i18n.Messages;
import org.kalypso.nofdpidss.timeseries.wizard.repository.timeseries.worker.CsvSheetImportDataModel.TSM_KEY;

/**
 * @author Dirk Kuch
 */
public class TsmZmlImportWorker implements ICoreRunnableWithProgress {

    private final CsvSheetImportDataModel m_model;

    public TsmZmlImportWorker(final CsvSheetImportDataModel model) {
        m_model = model;
    }

    /**
   * @see org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress#execute(org.eclipse.core.runtime.IProgressMonitor)
   */
    public IStatus execute(final IProgressMonitor monitor) {
        final File src = (File) m_model.getValue(TSM_KEY.eZmlFile);
        final File dirDest = (File) m_model.getValue(TSM_KEY.eDestinationDir);
        if (!src.exists()) throw new IllegalStateException(Messages.TsmZmlImportWorker_0 + src.getAbsoluteFile());
        if (!dirDest.exists()) throw new IllegalStateException(Messages.TsmZmlImportWorker_1 + dirDest.getAbsoluteFile());
        final File dest = new File(dirDest, src.getName());
        if (dest.exists()) throw new IllegalStateException(Messages.TsmZmlImportWorker_2 + dest.getAbsolutePath());
        try {
            FileUtils.copyFile(src, dest);
        } catch (final IOException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return Status.CANCEL_STATUS;
        }
        final IProject project = NofdpCorePlugin.getProjectManager().getActiveProject();
        WorkspaceSync.sync(project, IResource.DEPTH_INFINITE);
        return Status.OK_STATUS;
    }
}
