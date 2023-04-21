package de.unkrig.cvstools.operations;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSException;
import org.eclipse.team.internal.ccvs.core.ICVSRemoteFile;
import org.eclipse.team.internal.ccvs.core.ICVSRemoteResource;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.ICVSResource;
import org.eclipse.team.internal.ccvs.core.Policy;
import org.eclipse.team.internal.ccvs.core.client.Command;
import org.eclipse.team.internal.ccvs.core.client.Session;
import org.eclipse.team.internal.ccvs.core.client.Command.LocalOption;
import org.eclipse.team.internal.ccvs.ui.operations.RepositoryLocationOperation;
import org.eclipse.ui.IWorkbenchPart;
import de.unkrig.cvstools.Messages;

@SuppressWarnings("restriction")
public final class DeleteRevisionsOperation extends RepositoryLocationOperation {

    public DeleteRevisionsOperation(IWorkbenchPart part, ICVSRemoteResource[] remoteResources) {
        super(part, remoteResources);
    }

    @Override
    protected String getTaskName() {
        return Messages.DeleteRevisionsOperation_0;
    }

    @Override
    protected void execute(ICVSRepositoryLocation cvsRepositoryLocation, ICVSRemoteResource[] remoteResources, IProgressMonitor monitor) throws CVSException {
        for (ICVSRemoteResource remoteResource : remoteResources) {
            String revision;
            try {
                revision = ((ICVSRemoteFile) remoteResource).getRevision();
            } catch (TeamException e) {
                throw new CVSException(e);
            }
            Session session = new Session(cvsRepositoryLocation, remoteResource.getParent(), true);
            session.open(Policy.subMonitorFor(monitor, 10), true);
            try {
                IStatus status = Command.ADMIN.execute(session, Command.NO_GLOBAL_OPTIONS, new LocalOption[] { new LocalOption("-o", revision) {
                } }, new ICVSResource[] { remoteResource }, null, monitor);
                this.collectStatus(status);
            } finally {
                session.close();
            }
            if (errorsOccurred()) return;
        }
    }
}
