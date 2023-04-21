package com.ecmdeveloper.plugin.content.handlers;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchWindow;
import com.ecmdeveloper.plugin.content.jobs.CheckinTrackedFileJob;
import com.ecmdeveloper.plugin.core.model.IObjectStore;
import com.ecmdeveloper.plugin.tracker.model.TrackedFile;

/**
 * @author Ricardo.Belfor
 *
 */
public class CheckinTrackedFileHandler extends AbstractTrackedFileHandler {

    @Override
    protected void handleSelectedFile(IWorkbenchWindow window, TrackedFile trackedFile, IFile file, IObjectStore objectStore) {
        if (trackedFile != null) {
            CheckinTrackedFileJob job = new CheckinTrackedFileJob(trackedFile, file, window, objectStore);
            job.setUser(true);
            job.schedule();
        }
    }
}
