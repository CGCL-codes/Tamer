package com.enerjy.common.eclipse;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;
import com.enerjy.common.EnerjyException;

class VerifyJob extends Job {

    VerifyJob() {
        super("Enerjy workspace verification");
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        ILock lock = Activator.getInstance().getJobLock();
        lock.acquire();
        try {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = workspace.getRoot();
            WorkspaceVerifier walker = new WorkspaceVerifier(Activator.getInstance().getAllProxies());
            try {
                root.accept(walker);
                walker.finish();
            } catch (CoreException e) {
                throw new EnerjyException("Error traversing resources", e);
            }
            if (walker.hasWork()) {
                ResourceJob job = new ResourceJob(walker.getModifications());
                job.schedule();
            }
            return Status.OK_STATUS;
        } finally {
            lock.release();
        }
    }
}
