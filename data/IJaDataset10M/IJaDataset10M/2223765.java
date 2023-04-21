package org.ourgrid.broker.controller.operations;

import java.util.Map;
import org.ourgrid.broker.commands.SchedulerData;
import org.ourgrid.broker.scheduler.LoggerAdapter;
import org.ourgrid.broker.scheduler.extensions.GenericTransferHandle;
import org.ourgrid.broker.scheduler.workqueue.xmlcreator.LoggerXMLCreator;
import org.ourgrid.common.executor.Executor;
import org.ourgrid.common.executor.ExecutorException;
import org.ourgrid.common.executor.ExecutorFactory;
import org.ourgrid.common.executor.ExecutorHandle;
import org.ourgrid.common.executor.ExecutorResult;
import org.ourgrid.common.interfaces.to.GridProcessErrorTypes;
import org.ourgrid.common.interfaces.to.GridProcessHandle;
import org.ourgrid.common.replicaexecutor.SabotageCheckResult;
import org.ourgrid.worker.controller.GridProcessError;

/**
 * This class implements a mechanism to check sabotage acts.
 */
public class SabotageCheckOperation extends AbstractOperation {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final String command;

    private final String executionDirectory;

    private final Map<String, String> envVars;

    private SabotageCheckResult sabotageResult;

    /**
	 * @param replicaHandle
	 * @param requestID
	 * @param worker
	 * @param workerClient
	 * @param command
	 * @param executionDirectory
	 * @param envVars
	 * @param executor
	 */
    public SabotageCheckOperation(GridProcessHandle replicaHandle, long requestID, String workerID, String command, String executionDirectory, Map<String, String> envVars) {
        super(replicaHandle, requestID, workerID);
        this.command = command;
        this.executionDirectory = executionDirectory;
        this.envVars = envVars;
    }

    public void run() {
        SchedulerData loggerData = new SchedulerData(getXmlCreator(LoggerXMLCreator.class).getXML("Running sabotage check command: " + command + " replica: " + getGridProcessHandle(), LoggerXMLCreator.DEBUG));
        getCollector().addData(loggerData);
        ExecutorHandle handle;
        ExecutorResult result = null;
        Executor executor = new ExecutorFactory(new LoggerAdapter()).buildNewNativeExecutor();
        try {
            handle = executor.execute(executionDirectory, command, envVars);
            result = executor.getResult(handle);
            sabotageResult = new SabotageCheckResult(result.getExitValue() != 0, result, null);
        } catch (ExecutorException e) {
            sabotageResult = new SabotageCheckResult(false, result, new GridProcessError(e, GridProcessErrorTypes.EXECUTION_ERROR));
        }
    }

    /**
	 * 
	 * @return The sabotage check command.
	 */
    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "]. sabotage check command: " + command;
    }

    /**
	 * @return the sabotageResult
	 */
    public SabotageCheckResult getSabotageResult() {
        return sabotageResult;
    }

    @Override
    public GenericTransferHandle getHandle() {
        return null;
    }
}
