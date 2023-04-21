package gleam.executive.workflow;

import gleam.executive.model.Project;
import gleam.executive.service.ProjectManager;
import gleam.executive.workflow.manager.WorkflowManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.util.StringUtils;
import gleam.executive.workflow.util.JPDLConstants;

public class ChangeActorsTest extends BaseWorkflowServiceTestCase {

    private static WorkflowManager workflowManager = null;

    private static final String PROCESS_ARCHIVE = "manualAnnotation.zip";

    private static ProcessDefinition processDefinition = null;

    private static long processInstanceId;

    private static String[] annotators = { "agaton", "thomas", "niraj" };

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

    public void testChangeActorsProcessDefinition() throws Exception {
        log.debug("#######################################");
        log.debug("#  testChangeActorsProcessDefinition()            #");
        log.debug("#######################################");
        String processesDirLocation = processesDir + "/";
        String processArchiveLocation = processesDirLocation + PROCESS_ARCHIVE;
        log.debug("processesArchiveLocation " + processArchiveLocation);
        InputStream inputStream = new FileInputStream(processArchiveLocation);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        processDefinition = ProcessDefinition.parseParZipInputStream(zipInputStream);
        log.debug("processDefinition " + processDefinition.getName());
        if (zipInputStream != null) zipInputStream.close();
        if (inputStream != null) inputStream.close();
        workflowManager.deployProcessDefinition(processDefinition);
        setComplete();
    }

    public void testChangeActorsProcessInstanceStart() throws Exception {
        log.debug("#######################################");
        log.debug("#  testChangeActorsProcessInstanceStart()         #");
        log.debug("#######################################");
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put(JPDLConstants.INITIATOR, WorkflowTestData.INITIATOR);
        variableMap.put(JPDLConstants.MODE, JPDLConstants.TEST_MODE);
        variableMap.put(JPDLConstants.CORPUS_ID, "corpusX");
        variableMap.put(JPDLConstants.ANNOTATORS_PER_DOCUMENT, "2");
        variableMap.put(JPDLConstants.ANNOTATOR_HAS_TO_BE_UNIQUE_FOR_DOCUMENT, "on");
        variableMap.put(JPDLConstants.ANONYMOUS_ANNOTATION, "on");
        variableMap.put(JPDLConstants.CAN_CANCEL, "on");
        String annotatorCSVList = StringUtils.arrayToCommaDelimitedString(annotators);
        variableMap.put(JPDLConstants.ANNOTATOR_CSV_LIST, annotatorCSVList);
        ProcessInstance processInstance = workflowManager.createStartProcessInstance(processDefinition.getId(), variableMap, null);
        processInstanceId = processInstance.getId();
        assertNotNull("Instance ID should not be null", processInstanceId);
        int numOfActiveProcessInstances = workflowManager.findUncompletedProcessInstancesForProcessDefinition(processDefinition.getId());
        assertEquals(1, numOfActiveProcessInstances);
        setComplete();
    }

    public ProcessInstance loadProcessInstance() throws Exception {
        return workflowManager.getProcessInstanceForUpdate(processInstanceId);
    }

    public void testChangeActorsCreatingTasks() throws Exception {
        log.debug("#######################################");
        log.debug("#  testChangeActorsCreatingTasks()                #");
        log.debug("#######################################");
        ProcessInstance processInstance = loadProcessInstance();
        List<TaskInstance> taskInstances = workflowManager.findTaskInstancesForProcessInstance(processInstance.getId());
        assertEquals(4, taskInstances.size());
        setComplete();
    }

    public void testChangeActorsPooledActors() throws Exception {
        log.debug("#######################################");
        log.debug("#  testChangeActorsPooledActors()                #");
        log.debug("#######################################");
        ProcessInstance processInstance = loadProcessInstance();
        List<TaskInstance> taskInstances = workflowManager.findPendingPooledAnnotationTaskInstances("agaton");
        assertEquals(4, taskInstances.size());
        setComplete();
    }

    public void testChangeActorsRemoveActorFromPool() throws Exception {
        log.debug("#######################################");
        log.debug("#  testChangeActorsRemoveActorFromPools()                #");
        log.debug("#######################################");
        ProcessInstance processInstance = loadProcessInstance();
        String oldActorCSVList = (String) processInstance.getContextInstance().getVariables().get(JPDLConstants.ANNOTATOR_CSV_LIST);
        String[] oldActors = StringUtils.commaDelimitedListToStringArray(oldActorCSVList);
        assertEquals(3, oldActors.length);
        String[] newActors = { "niraj", "thomas" };
        workflowManager.updatePooledActors(JPDLConstants.ANNOTATOR_CSV_LIST, newActors, processInstance);
        List<TaskInstance> taskInstances = workflowManager.findPendingPooledAnnotationTaskInstances("agaton");
        assertEquals(0, taskInstances.size());
        setComplete();
    }

    public void testChangeActorsAddActorInPool() throws Exception {
        log.debug("#######################################");
        log.debug("#  testChangeActorsAddActorInPool()                #");
        log.debug("#######################################");
        ProcessInstance processInstance = loadProcessInstance();
        String oldActorCSVList = (String) processInstance.getContextInstance().getVariables().get(JPDLConstants.ANNOTATOR_CSV_LIST);
        String[] oldActors = StringUtils.commaDelimitedListToStringArray(oldActorCSVList);
        assertEquals(2, oldActors.length);
        String[] newActors = { "niraj", "thomas", "milan" };
        workflowManager.updatePooledActors(JPDLConstants.ANNOTATOR_CSV_LIST, newActors, processInstance);
        List<TaskInstance> taskInstances = workflowManager.findPendingPooledAnnotationTaskInstances("milan");
        assertEquals(4, taskInstances.size());
        setComplete();
    }
}
