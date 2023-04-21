package org.eclipse.mylyn.internal.eplanner.core;

import java.io.InputStream;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * 
 * @author edge
 *
 */
public class EPlannerTaskAttachmentHandler extends AbstractTaskAttachmentHandler {

    @SuppressWarnings("unused")
    private final EPlannerRepositoryConnector connector;

    public EPlannerTaskAttachmentHandler(EPlannerRepositoryConnector connector) {
        this.connector = connector;
    }

    @Override
    public InputStream getContent(TaskRepository repository, ITask task, TaskAttribute attachmentAttribute, IProgressMonitor monitor) throws CoreException {
        return null;
    }

    @Override
    public boolean canGetContent(TaskRepository repository, ITask task) {
        return false;
    }

    @Override
    public boolean canPostContent(TaskRepository repository, ITask task) {
        return false;
    }

    @Override
    public void postContent(TaskRepository repository, ITask task, AbstractTaskAttachmentSource source, String comment, TaskAttribute attachmentAttribute, IProgressMonitor monitor) throws CoreException {
    }
}
