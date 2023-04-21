package net.sf.balm.workflow.core;

import java.util.Map;
import org.jbpm.graph.exe.ProcessInstance;

/**
 * @author dz
 */
public interface ProcessService {

    /**
     * @param processInstanceId
     * @param vars
     */
    public void saveProcessVariables(final String processInstanceId, final Map vars);

    /**
     * @param processDefinitionId
     * @return
     */
    public ProcessInstance createProcessInstance(final String processDefinitionId);

    /**
     * @param processDefinitionId
     * @param vars
     * @return
     */
    public ProcessInstance createProcessInstance(final String processDefinitionId, final Map vars);

    /**
     * @param processDefinitionId
     * @param vars
     * @return
     */
    public ProcessInstance startProcessInstance(final String processDefinitionId, final Map vars);

    /**
     * @param processInstance
     * @param vars
     * @return
     */
    public ProcessInstance startProcessInstance(final ProcessInstance processInstance, final Map vars);

    /**
     * @param processInstanceId
     * @return
     */
    public ProcessInstance suspendProcessInstance(final String processInstanceId);

    /**
     * @param processInstanceId
     * @return
     */
    public ProcessInstance resumeProcessInstance(final String processInstanceId);

    /**
     * @param processInstanceId
     * @return
     */
    public ProcessInstance terminateProcessInstance(final String processInstanceId);
}
