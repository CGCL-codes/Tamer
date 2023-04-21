package com.liferay.jbpm.util;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

/**
 * <a href="WorkflowUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Charles May
 *
 */
public class WorkflowUtil {

    public static void initDefinition(ProcessDefinition definition) {
        Hibernate.initialize(definition);
    }

    public static void initInstance(ProcessInstance instance) {
        Hibernate.initialize(instance.getProcessDefinition());
        Hibernate.initialize(instance.getRootToken());
        Hibernate.initialize(instance.getRootToken().getNode());
    }

    public static void initInstances(List instances) {
        Iterator itr = instances.iterator();
        while (itr.hasNext()) {
            ProcessInstance instance = (ProcessInstance) itr.next();
            initInstance(instance);
        }
    }

    public static void initTask(TaskInstance task) {
        ProcessInstance instance = task.getToken().getProcessInstance();
        Hibernate.initialize(task);
        Hibernate.initialize(instance);
        Hibernate.initialize(instance.getProcessDefinition());
    }

    public static void initTasks(List tasks) {
        Iterator itr = tasks.iterator();
        while (itr.hasNext()) {
            TaskInstance task = (TaskInstance) itr.next();
            initTask(task);
        }
    }

    public static void initToken(Token token) {
        Hibernate.initialize(token.getNode());
    }

    public static void initTokens(List tokens) {
        Iterator itr = tokens.iterator();
        while (itr.hasNext()) {
            Token token = (Token) itr.next();
            initToken(token);
        }
    }
}
