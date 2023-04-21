package gleam.executive.workflow;

import java.util.Iterator;
import java.util.List;
import gleam.executive.model.Project;
import gleam.executive.service.ProjectManager;
import gleam.executive.workflow.manager.WorkflowManager;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;

public class CleanupTest extends BaseWorkflowServiceTestCase {

    private static WorkflowManager workflowManager = null;

    private static ProjectManager projectManager = null;

    public void testCleanAll() throws Exception {
        workflowManager = (WorkflowManager) applicationContext.getBean("workflowManager");
        projectManager = (ProjectManager) applicationContext.getBean("projectManager");
        List<ProcessInstance> processInstances = workflowManager.findAllProcessInstancesExcludingSubProcessInstances();
        Iterator<ProcessInstance> it = processInstances.iterator();
        while (it.hasNext()) {
            long processInstanceId = it.next().getId();
            log.debug("try to delete process instance " + processInstanceId);
            workflowManager.cancelProcessInstance(processInstanceId);
            log.debug("deleted process instance " + processInstanceId);
        }
        List processDefinitions = workflowManager.findAllProcessDefinitions();
        Iterator<ProcessDefinition> itr = processDefinitions.iterator();
        while (itr.hasNext()) {
            workflowManager.undeployProcessDefinition(itr.next());
        }
        List<Project> projects = projectManager.getProjects();
        Iterator<Project> it1 = projects.iterator();
        while (it1.hasNext()) {
            long projectId = it1.next().getId();
            log.debug("try to delete project " + projectId);
            projectManager.removeProject(projectId);
            log.debug("deleted project " + projectId);
        }
        setComplete();
    }
}
