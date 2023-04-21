package gleam.executive.workflow;

import gleam.executive.Constants;
import gleam.executive.model.WebAppBean;
import gleam.executive.workflow.manager.WorkflowManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;
import gleam.executive.workflow.util.JPDLConstants;
import gleam.executive.workflow.util.WorkflowUtil;

public class MatrixwareProcessDefinitionUpload extends BaseWorkflowServiceTestCase {

    private static WorkflowManager workflowManager = null;

    private static ProcessDefinition processDefinition = null;

    private static ProcessDefinition setupProcessDefinition = null;

    private static ProcessDefinition automaticAnnotationProcessDefinition = null;

    private static ProcessDefinition manualAnnotationProcessDefinition = null;

    private static ProcessDefinition reviewProcessDefinition = null;

    private static long processInstanceId;

    public void testSetupProcessDefinition() throws Exception {
        log.debug("#######################################");
        log.debug("#testSetupProcessDefinition()#");
        log.debug("#######################################");
        String processesDirLocation = processesDir + "/";
        String processArchiveLocation = processesDirLocation + WorkflowTestData.SUB_PROJECT_SETUP_ARCHIVE;
        log.debug("processesArchiveLocation " + processArchiveLocation);
        InputStream inputStream = new FileInputStream(processArchiveLocation);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        setupProcessDefinition = ProcessDefinition.parseParZipInputStream(zipInputStream);
        log.debug("setupProcessDefinition name " + setupProcessDefinition.getName());
        if (zipInputStream != null) zipInputStream.close();
        if (inputStream != null) inputStream.close();
        workflowManager = (WorkflowManager) applicationContext.getBean("workflowManager");
        workflowManager.deployProcessDefinition(setupProcessDefinition);
        log.debug(" deployed setupProcessDefinition ID " + setupProcessDefinition.getId());
        setComplete();
    }

    public void testAutomaticAnnotationProcessDefinition() throws Exception {
        log.debug("#######################################");
        log.debug("#testAutomaticAnnotationProcessDefinition()#");
        log.debug("#######################################");
        String processesDirLocation = processesDir + "/";
        String processArchiveLocation = processesDirLocation + WorkflowTestData.SUB_AUTOMATIC_ANNOTATION_ARCHIVE;
        log.debug("processesArchiveLocation " + processArchiveLocation);
        InputStream inputStream = new FileInputStream(processArchiveLocation);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        automaticAnnotationProcessDefinition = ProcessDefinition.parseParZipInputStream(zipInputStream);
        log.debug("automaticAnnotationProcessDefinition name " + automaticAnnotationProcessDefinition.getName());
        if (zipInputStream != null) zipInputStream.close();
        if (inputStream != null) inputStream.close();
        workflowManager = (WorkflowManager) applicationContext.getBean("workflowManager");
        workflowManager.deployProcessDefinition(automaticAnnotationProcessDefinition);
        log.debug(" deployed automaticAnnotationProcessDefinition ID " + automaticAnnotationProcessDefinition.getId());
        setComplete();
    }

    public void testManualAnnotationProcessDefinition() throws Exception {
        log.debug("#######################################");
        log.debug("#testManualAnnotationProcessDefinition()#");
        log.debug("#######################################");
        String processesDirLocation = processesDir + "/";
        String processArchiveLocation = processesDirLocation + WorkflowTestData.SUB_MANUAL_ANNOTATION_ARCHIVE;
        log.debug("processesArchiveLocation " + processArchiveLocation);
        InputStream inputStream = new FileInputStream(processArchiveLocation);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        manualAnnotationProcessDefinition = ProcessDefinition.parseParZipInputStream(zipInputStream);
        log.debug("manualAnnotationProcessDefinition name " + manualAnnotationProcessDefinition.getName());
        if (zipInputStream != null) zipInputStream.close();
        if (inputStream != null) inputStream.close();
        workflowManager = (WorkflowManager) applicationContext.getBean("workflowManager");
        workflowManager.deployProcessDefinition(manualAnnotationProcessDefinition);
        log.debug(" deployed manualAnnotationProcessDefinition ID " + manualAnnotationProcessDefinition.getId());
        setComplete();
    }

    public void testReviewProcessDefinition() throws Exception {
        log.debug("#######################################");
        log.debug("#testReviewProcessDefinition()#");
        log.debug("#######################################");
        String processesDirLocation = processesDir + "/";
        String processArchiveLocation = processesDirLocation + WorkflowTestData.SUB_REVIEW_ARCHIVE;
        log.debug("processesArchiveLocation " + processArchiveLocation);
        InputStream inputStream = new FileInputStream(processArchiveLocation);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        reviewProcessDefinition = ProcessDefinition.parseParZipInputStream(zipInputStream);
        log.debug("reviewProcessDefinition name " + reviewProcessDefinition.getName());
        if (zipInputStream != null) zipInputStream.close();
        if (inputStream != null) inputStream.close();
        workflowManager = (WorkflowManager) applicationContext.getBean("workflowManager");
        workflowManager.deployProcessDefinition(reviewProcessDefinition);
        log.debug(" deployed reviewProcessDefinition ID " + reviewProcessDefinition.getId());
        setComplete();
    }

    public void testMainProcessDefinition() throws Exception {
        log.debug("#######################################");
        log.debug("#  testMainProcessDefinition()  #");
        log.debug("#######################################");
        String processesDirLocation = processesDir + "/";
        String processArchiveLocation = processesDirLocation + WorkflowTestData.PROCESS_ARCHIVE;
        log.debug("processesArchiveLocation " + processArchiveLocation);
        InputStream inputStream = new FileInputStream(processArchiveLocation);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        processDefinition = ProcessDefinition.parseParZipInputStream(zipInputStream);
        log.debug("processDefinition " + processDefinition.getName());
        if (zipInputStream != null) zipInputStream.close();
        if (inputStream != null) inputStream.close();
        workflowManager = (WorkflowManager) applicationContext.getBean("workflowManager");
        processDefinition.setName(Constants.MAIN_PROCESS_DEFINITION_NAME);
        workflowManager.deployProcessDefinition(processDefinition);
        log.debug("deployed processDefinition ID " + processDefinition.getId());
        log.debug("deployed processDefinition NAME " + processDefinition.getName());
        setComplete();
    }

    public ProcessInstance loadProcessInstance() throws Exception {
        return workflowManager.getProcessInstanceForUpdate(processInstanceId);
    }

    public ProcessInstance loadSubProcessInstance(long subProcessDefinitionId) throws Exception {
        List<ProcessInstance> subProcessInstances = workflowManager.findAllProcessInstances(subProcessDefinitionId);
        if (subProcessInstances == null || subProcessInstances.size() == 0) {
            return null;
        } else {
            return (ProcessInstance) subProcessInstances.iterator().next();
        }
    }
}
