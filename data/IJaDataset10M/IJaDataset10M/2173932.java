package org.bpmsuite.dto;

import java.util.Collection;
import java.util.Map;
import org.bpmsuite.constants.DataTransferObjectParams;
import org.bpmsuite.dto.bpm.BpmTask;
import org.bpmsuite.vo.hrmetadata.Employee;
import org.bpmsuite.vo.protocol.ProtocolEntry;

/**
 * @author Dirk Weiser
 */
public abstract class AbstractBpmTaskFactory implements DataTransferObjectFactory {

    protected void setDefaultValues(BpmTask bpmTask, Map parameter) throws DataTransferObjectException {
        Employee actor = null;
        Map actorPool = null;
        String processDefinitionName = null;
        Long processInstanceId = null;
        Long taskInstanceId = null;
        String taskName = null;
        Map processVariables = null;
        ProtocolEntry protocolEntry = null;
        String leavingTransition;
        Collection leavingTransitions = null;
        if ((parameter.get(DataTransferObjectParams.ACTOR) == null && parameter.get(DataTransferObjectParams.ACTOR_POOL) == null) || parameter.get(DataTransferObjectParams.PROCESSDEFINITION_NAME) == null || parameter.get(DataTransferObjectParams.PROCESS_INSTANCE_ID) == null || parameter.get(DataTransferObjectParams.TASK_INSTANCE_ID) == null || parameter.get(DataTransferObjectParams.TASK_NAME) == null || parameter.get(DataTransferObjectParams.DEFAULT_LEAVING_TRANSITION_NAME) == null || parameter.get(DataTransferObjectParams.LEAVING_TRANSITIONS) == null) {
            throw new DataTransferObjectException("Mandatory parameter is null!");
        } else {
            actor = (Employee) parameter.get(DataTransferObjectParams.ACTOR);
            processDefinitionName = (String) parameter.get(DataTransferObjectParams.PROCESSDEFINITION_NAME);
            processInstanceId = (Long) parameter.get(DataTransferObjectParams.PROCESS_INSTANCE_ID);
            taskInstanceId = (Long) parameter.get(DataTransferObjectParams.TASK_INSTANCE_ID);
            taskName = (String) parameter.get(DataTransferObjectParams.TASK_NAME);
            leavingTransition = (String) parameter.get(DataTransferObjectParams.DEFAULT_LEAVING_TRANSITION_NAME);
            leavingTransitions = (Collection) parameter.get(DataTransferObjectParams.LEAVING_TRANSITIONS);
            if (parameter.get(DataTransferObjectParams.ACTOR_POOL) != null) {
                actorPool = (Map) parameter.get(DataTransferObjectParams.ACTOR_POOL);
            }
            if (parameter.get(DataTransferObjectParams.PROCESS_VARIABLE_MAP) != null) {
                processVariables = (Map) parameter.get(DataTransferObjectParams.PROCESS_VARIABLE_MAP);
            }
            if (parameter.get(DataTransferObjectParams.PROTOCOL_ENTRY) != null) {
                protocolEntry = (ProtocolEntry) parameter.get(DataTransferObjectParams.PROTOCOL_ENTRY);
            }
        }
        bpmTask.setActor(actor);
        bpmTask.setActorPool(actorPool);
        bpmTask.setProcessDefinitionName(processDefinitionName);
        bpmTask.setProcessInstanceId(processInstanceId);
        bpmTask.setTaskInstanceId(taskInstanceId);
        bpmTask.setTaskName(taskName);
        bpmTask.setLeavingTransition(leavingTransition);
        bpmTask.setLeavingTransitions(leavingTransitions);
        bpmTask.setProcessVariables(processVariables);
        bpmTask.setProtocolEntry(protocolEntry);
    }
}
