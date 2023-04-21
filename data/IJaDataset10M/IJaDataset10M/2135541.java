package org.qcmylyn.core.attribute;

import static org.qcmylyn.core.QcMylynCorePlugin.log;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.jqc.QcProjectConnectedSession;
import org.jqc.QcSessionResult;
import org.qcmylyn.core.QcMylynCorePlugin;
import org.qcmylyn.core.QcRepositoryConfigurationFactory;
import org.qcmylyn.core.QcRepositoryConnector;
import org.qcmylyn.core.attribute.operations.BugSynchronizer;
import org.qcmylyn.core.attribute.operations.TaskSynchronizer;
import org.qcmylyn.core.messages.Messages;
import org.qctools4j.exception.QcException;

/**
 * TODO Insert class description
 *
 * @author usf02000
 *
 */
public class QcDefectDataHandler extends AbstractTaskDataHandler {

    private final QcRepositoryConnector connector;

    @Override
    public boolean canInitializeSubTaskData(final TaskRepository taskRepository, final ITask task) {
        return super.canInitializeSubTaskData(taskRepository, task);
    }

    @Override
    public boolean canGetMultiTaskData(final TaskRepository taskRepository) {
        return super.canGetMultiTaskData(taskRepository);
    }

    @Override
    public boolean initializeTaskData(final TaskRepository repository, final TaskData data, final ITaskMapping initializationData, final IProgressMonitor monitor) throws CoreException {
        if (log.isDebugEnabled()) {
            log.debug(Messages.QcBugDataHandler_Init_Task_Data);
        }
        try {
            final TaskSynchronizer defectFactory = new TaskSynchronizer(repository, Messages.QcBugDataHandler_Init_Task_Data, monitor) {

                @Override
                public Collection<TaskData> execute(final QcProjectConnectedSession qcProjectConnectedSession) throws QcException, QcException {
                    return Collections.singleton(updateTaskData(qcProjectConnectedSession, data));
                }
            };
            return connector.useConnection(repository, defectFactory).getData() != null;
        } catch (final QcException e) {
            QcMylynCorePlugin.getDefault().error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public RepositoryResponse postTaskData(final TaskRepository repository, final TaskData taskData, final Set<TaskAttribute> oldAttributes, final IProgressMonitor monitor) throws CoreException {
        try {
            final QcSessionResult<? extends RepositoryResponse> useConnection = connector.useConnection(repository, new BugSynchronizer(repository, "", monitor) {

                @Override
                public RepositoryResponse execute(final QcProjectConnectedSession qcProjectConnectedSession) throws QcException, QcException {
                    return updateBugData(qcProjectConnectedSession, taskData, oldAttributes);
                }
            });
            return useConnection.getData();
        } catch (final QcException e) {
            QcMylynCorePlugin.getDefault().error(e.getMessage(), e);
            throw new CoreException(QcMylynCorePlugin.toStatus(e, repository));
        }
    }

    @Override
    public void getMultiTaskData(final TaskRepository repository, final Set<String> taskIds, final TaskDataCollector collector, final IProgressMonitor monitor) throws CoreException {
        try {
            connector.useConnection(repository, new TaskSynchronizer(repository, Messages.QcBugDataHandler_Getting_Multiple_Task, monitor) {

                @Override
                public Collection<TaskData> execute(final QcProjectConnectedSession lCon) throws QcException {
                    return updateTaskData(lCon, taskIds, collector);
                }
            });
        } catch (final QcException e) {
            log.warn("Error getting multiple tasks", e);
        }
    }

    @Override
    public boolean initializeSubTaskData(final TaskRepository repository, final TaskData taskData, final TaskData parentTaskData, final IProgressMonitor monitor) throws CoreException {
        final TaskMapper lMapper = new TaskMapper(parentTaskData);
        initializeTaskData(repository, taskData, lMapper, monitor);
        new TaskMapper(taskData).merge(lMapper);
        return true;
    }

    public QcRepositoryConnector getConnector() {
        return connector;
    }

    public QcDefectDataHandler(final QcRepositoryConnector connector) {
        this.connector = connector;
    }

    @Override
    public TaskAttributeMapper getAttributeMapper(final TaskRepository taskRepository) {
        return new QcBugAttributeMapper(taskRepository);
    }

    private void addOperation(final TaskRepository pRepository, final TaskData pTaskData, final QcOperationEnum pOpcode) {
        final TaskAttribute createOperation = pOpcode.createOperation(pTaskData);
        if (QcOperationEnum.RESOLVE.equals(pOpcode)) {
            final String[] lStatuses = QcRepositoryConfigurationFactory.getConfiguration(pRepository).getStatusList();
            for (final String lStatus : lStatuses) {
                createOperation.putOption(lStatus, lStatus);
            }
        }
    }
}
