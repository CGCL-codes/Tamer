package com.techstar.framework.service.workflow.bus;

import java.util.ArrayList;
import java.util.List;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.dao.DataAccessException;
import com.techstar.framework.service.workflow.bus.dto.TaskDto;
import com.techstar.framework.service.workflow.dao.IWorkflowDao;

public class WorkflowService implements IWorkflowService {

    private IWorkflowDao workFlowDao;

    public void setWorkFlowDao(IWorkflowDao workFlowDao) {
        this.workFlowDao = workFlowDao;
    }

    public List findPooledTaskInstances(String actorId) throws DataAccessException {
        List taskList = new ArrayList();
        List list = this.workFlowDao.findPooledTaskInstances(actorId);
        for (int i = 0; i < list.size(); i++) {
            TaskDto task = new TaskDto();
            TaskInstance instance = (TaskInstance) list.get(i);
            task.setActorId(instance.getActorId());
            task.setActorName("test");
            task.setBusinessId((String) instance.getContextInstance().getVariable("businessId"));
            task.setCreateDate(instance.getCreate().toString());
            task.setTaskName(instance.getName());
            task.setTaskId(Long.toString(instance.getId()));
            task.setProcessId(Long.toString(instance.getTaskMgmtInstance().getProcessInstance().getId()));
            taskList.add(task);
        }
        return taskList;
    }

    public void start(String processDefinition) throws DataAccessException {
        this.workFlowDao.start(processDefinition);
    }

    public void start(String processDefinition, String businessId, String preUserId, final String[] curUserIds) throws DataAccessException {
        this.workFlowDao.start(processDefinition, businessId, preUserId, curUserIds);
    }

    public void suspend(long processInstance) throws DataAccessException {
        this.workFlowDao.suspend(processInstance);
    }

    public void end(long processInstance) throws DataAccessException {
        this.workFlowDao.end(processInstance);
    }

    public void endTask(long taskInstance, String preUserId, String[] curUserIds) throws DataAccessException {
        this.workFlowDao.endTask(taskInstance, preUserId, curUserIds);
    }

    public void endTask(long taskInstance, String transition, String preUserId, String[] curUserIds) throws DataAccessException {
        this.workFlowDao.endTask(taskInstance, transition, preUserId, curUserIds);
    }

    public String retrieveByteArraysOfGpd(long taskInstanceId) throws DataAccessException {
        return this.workFlowDao.retrieveByteArraysOfGpd(taskInstanceId);
    }

    public List findTransitionLogs(long taskInstanceId) throws DataAccessException {
        return this.workFlowDao.findTransitionLogs(taskInstanceId);
    }

    public String isOverForCountersign(long taskInstanceId, String roleId, int isApprove) throws DataAccessException {
        return this.workFlowDao.isOverForCountersign(taskInstanceId, roleId, isApprove);
    }

    public void endWaitingByProcessInstance(long processInstanceId) throws DataAccessException {
        this.workFlowDao.endWaitingByProcessInstance(processInstanceId);
    }

    public void endWaitingByTaskInstance(long taskInstanceId) throws DataAccessException {
        this.workFlowDao.endWaitingByTaskInstance(taskInstanceId);
    }
}
