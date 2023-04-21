package com.techstar.dmis.service.workflow.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.exe.Assignable;
import com.techstar.dmis.service.workflow.impl.helper.DimsWorkflowHelper;
import com.techstar.framework.service.workflow.IAssignment;

/**
 * 工作流操作类
 * @author 
 * @date
 */
public class DDAccidentBriefApprove implements IAssignment {

    public void assign(Assignable arg0, ExecutionContext arg1) throws Exception {
        arg1.getTaskInstance().setBussId((String) arg1.getContextInstance().getVariable("businessId"));
        long taskId = arg1.getTaskInstance().getTask().getId();
        String taskRoles = "";
        String agencyRoles = "";
        Connection connection = arg1.getJbpmContext().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select TASK_ROLE,AGENCY_ROLE from JBPM_EXT_PERMISSION where task_id='" + taskId + "' ");
        while (resultSet.next()) {
            taskRoles = resultSet.getString("TASK_ROLE");
            agencyRoles = resultSet.getString("AGENCY_ROLE");
        }
        resultSet.close();
        statement.close();
        String[] currentUserIds = DimsWorkflowHelper.getCurrentUsers(taskRoles, agencyRoles);
        arg0.setPooledActors(currentUserIds);
    }
}
