package com.jguild.devportal.workflow;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to manage workflow operations
 */
public class WorkflowManager {

    @SuppressWarnings({ "MismatchedQueryAndUpdateOfCollection" })
    @Autowired
    private Map<String, WorkflowEngine> engines;

    public WorkflowManager() {
    }

    public WorkflowManager(final String engineName, final WorkflowEngine engine) {
        engines = new HashMap<String, WorkflowEngine>();
        engines.put(engineName, engine);
    }

    public WorkflowManager(final Map<String, WorkflowEngine> engines) {
        this.engines = engines;
    }

    public WorkflowEngine getWorkflowEngine(final String id) {
        final WorkflowEngine workflowEngine = engines.get(id);
        if (workflowEngine == null) {
            throw new WorkflowEngineNotFoundException();
        }
        return workflowEngine;
    }

    public ProcessStatus getProcessStatus(@NotNull final String processId) throws IllegalArgumentException, ProcessNotFoundException {
        final EngineAndProcessId engineAndProcessId = new EngineAndProcessId(processId);
        return engineAndProcessId.engine.getProcessStatus(engineAndProcessId.processId);
    }

    public String getProcessLogs(@NotNull final String processId) throws IllegalArgumentException, ProcessNotFoundException, WorkflowEngineNotFoundException {
        final EngineAndProcessId engineAndProcessId = new EngineAndProcessId(processId);
        return engineAndProcessId.engine.getProcessLogs(engineAndProcessId.processId);
    }

    public class EngineAndProcessId {

        private WorkflowEngine engine;

        private String processId;

        public EngineAndProcessId(final String processId) throws WorkflowEngineNotFoundException {
            final int idx = processId.indexOf(':');
            final int idLen = processId.length();
            if (idx == -1 || idx >= (idLen - 1)) {
                throw new IllegalArgumentException("not a valid process id: " + processId);
            }
            engine = engines.get(processId.substring(0, idx));
            if (engine == null) {
                throw new WorkflowEngineNotFoundException("unable to find workflow engine " + processId.substring(0, idx));
            }
            this.processId = processId.substring(idx + 1, idLen);
        }
    }

    public enum ProcessStatus {

        PENDING, RUNNING, FINISHED, FAILED
    }
}
