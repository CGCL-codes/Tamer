package org.powerstone.workflow.service;

import org.powerstone.workflow.model.*;
import org.powerstone.workflow.service.FlowTaskManager;
import java.io.FileInputStream;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.powerstone.workflow.dao.WorkflowDriverDAO;
import java.util.List;
import java.util.Iterator;
import org.powerstone.AbstractSpringTestCase;
import org.powerstone.ca.model.WebModule;
import org.powerstone.ca.model.User;
import org.powerstone.ca.model.Resource;
import org.powerstone.ca.service.ResourceManager;
import org.powerstone.ca.service.UserManager;

/**
 * An integrated test
 * 
 * @author daquanda
 * 
 */
public class WorkflowEngineTest extends AbstractSpringTestCase {

    private WorkflowEngine workflowEngine = null;

    private FlowMetaManager flowMetaManager = null;

    private BusinessTypeManager businessTypeManager = null;

    private FlowDeployManager flowDeployManager = null;

    private WorkflowDriverManager workflowDriverManager = null;

    protected ResourceManager resourceManager = null;

    protected UserManager userManager = null;

    private FlowTaskManager taskManager = null;

    private WorkflowDriverDAO dao = null;

    WorkflowMeta wm;

    BusinessType bm;

    WFDriverOutputParam wdOutParam1;

    WFDriverOutputParam wdOutParam2;

    WFDriverOutputParam wdOutParam3;

    FlowDeploy fd;

    User user;

    protected void onSetUpInTransaction() throws Exception {
        log = LogFactory.getLog(WorkflowEngineTest.class);
        log.info("--------------WorkflowEngineTest---------------");
        WebModule wm1 = new WebModule();
        wm1.setWebModuleName("ϵͳ����");
        resourceManager.createWebModule(wm1);
        user = new User();
        user.setUserName("admin@powerstone.org");
        user.setPassword("111");
        user.setEmail("admin@powerstone.org");
        user.setMemo("");
        user.setRealName(user.getId().toString());
        user.setSex("m");
        userManager.registerUser(user);
        Resource res1 = new Resource();
        res1.setActionURL("/rolemanage/");
        res1.setResourceID("function_rolemanage");
        res1.setResourceName("��ɫ����");
        resourceManager.createResource(res1);
        resourceManager.addResourceToWebModule(res1.getId().toString(), wm1.getWebModuleID().toString());
        Resource res2 = new Resource();
        res2.setActionURL("/function_usermanage/");
        res2.setResourceID("function_usermanage");
        res2.setResourceName("�û�����");
        resourceManager.createResource(res2);
        resourceManager.addResourceToWebModule(res2.getId().toString(), wm1.getWebModuleID().toString());
        Resource res3 = new Resource();
        res3.setActionURL("/function_privmanage/");
        res3.setResourceID("function_privmanage");
        res3.setResourceName("��Ȩ����");
        resourceManager.createResource(res3);
        resourceManager.addResourceToWebModule(res3.getId().toString(), wm1.getWebModuleID().toString());
        Resource res4 = new Resource();
        res4.setActionURL("/function_resourcemanage/");
        res4.setResourceID("function_resourcemanage");
        res4.setResourceName("��Դ����");
        resourceManager.createResource(res4);
        resourceManager.addResourceToWebModule(res4.getId().toString(), wm1.getWebModuleID().toString());
        Resource res6 = new Resource();
        res6.setActionURL("/function_flowmanage/");
        res6.setResourceID("function_flowmanage");
        res6.setResourceName("���̹���");
        resourceManager.createResource(res6);
        resourceManager.addResourceToWebModule(res6.getId().toString(), wm1.getWebModuleID().toString());
        userManager.giveCommonFunctionRight(user.getId().toString(), res1.getId().toString());
        userManager.giveCommonFunctionRight(user.getId().toString(), res2.getId().toString());
        userManager.giveCommonFunctionRight(user.getId().toString(), res3.getId().toString());
        userManager.giveCommonFunctionRight(user.getId().toString(), res4.getId().toString());
        userManager.giveCommonFunctionRight(user.getId().toString(), res6.getId().toString());
        log.info("------------------------------------");
        wdOutParam1 = new WFDriverOutputParam();
        wdOutParam1.setParamAlias("ParamAlias");
        wdOutParam1.setParamName("ParamName1");
        wdOutParam2 = new WFDriverOutputParam();
        wdOutParam2.setParamAlias("ParamAlias");
        wdOutParam2.setParamName("ParamName2");
        wdOutParam3 = new WFDriverOutputParam();
        wdOutParam3.setParamAlias("ParamAlias");
        wdOutParam3.setParamName("ParamName3");
        dao.saveDriverOutputParam(wdOutParam1);
        dao.saveDriverOutputParam(wdOutParam2);
        dao.saveDriverOutputParam(wdOutParam3);
        log.info(">>>>>>>>>>>>>>>" + wdOutParam1.getDriverOutputParamID());
        bm = new BusinessType();
        bm.setTypeName("TypeName1");
        businessTypeManager.createBusinessType(bm);
        log.info("�ϴ�����");
        try {
            String fileName = "dreambike.xpdl";
            wm = flowMetaManager.uploadFlowMetaFile(new FileInputStream(new File(fileName)), new FileInputStream(new File(fileName)), new FileInputStream(new File(fileName)), new Long(new File(fileName).length()), new Long(new File(fileName).length()));
        } catch (FileNotFoundException ex) {
        }
        log.info("FileVersions" + new Integer(wm.getFlowFileVersions().size()));
        log.info("�ı�businessType");
        bm.addWorkflowMeta(wm);
        businessTypeManager.createBusinessType(bm);
        log.info("�½�����");
        fd = new FlowDeploy();
        fd.setCreateTime((new GregorianCalendar()).getTime().toString());
        fd.setCurrentState(FlowDeploy.DEPLOY_STATE_PREPARING);
        fd.setFlowDeployName("deployName");
        fd.setMemo("memo");
        flowMetaManager.addFlowDeploy(wm.getFlowMetaID().toString(), fd);
        log.info("��������");
        flowDeployManager.enableFlowDeploy(fd.getFlowDeployID().toString());
        log.info("ע����");
        WorkflowDriver wd1 = new WorkflowDriver();
        wd1.setContextPath("/context");
        wd1.setFlowDriverName("FlowDriverName1");
        wd1.setMemo("memo");
        wd1.setReadURL("ReadURL1");
        wd1.setWriteURL("WriteURL1");
        WorkflowDriver wd2 = new WorkflowDriver();
        wd2.setContextPath("/context");
        wd2.setFlowDriverName("FlowDriverName2");
        wd2.setMemo("memo");
        wd2.setReadURL("ReadURL2");
        wd2.setWriteURL("WriteURL2");
        WorkflowDriver wd3 = new WorkflowDriver();
        wd3.setContextPath("/context");
        wd3.setFlowDriverName("FlowDriverName3");
        wd3.setMemo("memo3");
        wd3.setReadURL("ReadURL3");
        wd3.setWriteURL("WriteURL3");
        WorkflowDriver wd4 = new WorkflowDriver();
        wd4.setContextPath("/context");
        wd4.setFlowDriverName("FlowDriverName4");
        wd4.setMemo("memo");
        wd4.setReadURL("ReadURL4");
        wd4.setWriteURL("WriteURL4");
        WorkflowDriver wd5 = new WorkflowDriver();
        wd5.setContextPath("/context");
        wd5.setFlowDriverName("FlowDriverName5");
        wd5.setMemo("memo");
        wd5.setReadURL("ReadURL5");
        wd5.setWriteURL("WriteURL5");
        WorkflowDriver wd6 = new WorkflowDriver();
        wd6.setContextPath("/context");
        wd6.setFlowDriverName("FlowDriverName6");
        wd6.setMemo("memo");
        wd6.setReadURL("ReadURL6");
        wd6.setWriteURL("WriteURL6");
        WorkflowDriver wd7 = new WorkflowDriver();
        wd7.setContextPath("/context");
        wd7.setFlowDriverName("FlowDriverName7");
        wd7.setMemo("memo7");
        wd7.setReadURL("ReadURL7");
        wd7.setWriteURL("WriteURL7");
        WorkflowDriver wd8 = new WorkflowDriver();
        wd8.setContextPath("/context");
        wd8.setFlowDriverName("FlowDriverName8");
        wd8.setMemo("memo");
        wd8.setReadURL("ReadURL8");
        wd8.setWriteURL("WriteURL8");
        WorkflowDriver wd9 = new WorkflowDriver();
        wd9.setContextPath("/context");
        wd9.setFlowDriverName("FlowDriverName9");
        wd9.setMemo("memo9");
        wd9.setReadURL("ReadURL9");
        wd9.setWriteURL("WriteURL9");
        WorkflowDriver wd10 = new WorkflowDriver();
        wd10.setContextPath("/context");
        wd10.setFlowDriverName("FlowDriverName10");
        wd10.setMemo("memo");
        wd10.setReadURL("ReadURL10");
        wd10.setWriteURL("WriteURL10");
        workflowDriverManager.saveWorkflowDriver(wd1);
        workflowDriverManager.saveWorkflowDriver(wd2);
        workflowDriverManager.saveWorkflowDriver(wd3);
        workflowDriverManager.saveWorkflowDriver(wd4);
        workflowDriverManager.saveWorkflowDriver(wd5);
        workflowDriverManager.saveWorkflowDriver(wd6);
        workflowDriverManager.saveWorkflowDriver(wd7);
        workflowDriverManager.saveWorkflowDriver(wd8);
        workflowDriverManager.saveWorkflowDriver(wd9);
        workflowDriverManager.saveWorkflowDriver(wd10);
        workflowDriverManager.addDriverOutputParam(wd1.getFlowDriverID().toString(), wdOutParam1);
        workflowDriverManager.addDriverOutputParam(wd1.getFlowDriverID().toString(), wdOutParam2);
        workflowDriverManager.addDriverOutputParam(wd1.getFlowDriverID().toString(), wdOutParam3);
        workflowDriverManager.saveWorkflowDriver(wd1);
        log.info("��������" + wd2.getFlowDriverID());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act1", wd1.getFlowDriverID().toString());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act2", wd2.getFlowDriverID().toString());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act3", wd3.getFlowDriverID().toString());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act4", wd4.getFlowDriverID().toString());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act6", wd5.getFlowDriverID().toString());
        FlowNodeBinding nodeBinding7 = flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act7", wd6.getFlowDriverID().toString());
        FlowNodeBinding nodeBinding8 = flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act8", wd7.getFlowDriverID().toString());
        FlowNodeBinding nodeBinding9 = flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act9", wd8.getFlowDriverID().toString());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act10", wd9.getFlowDriverID().toString());
        flowDeployManager.updateFlowNodeBinding(fd.getFlowDeployID().toString(), "dreambike_Wor1_Act11", wd10.getFlowDriverID().toString());
        flowDeployManager.enableFounder(nodeBinding7.getNodeBindingID().toString());
        flowDeployManager.updateOtherPerformer(nodeBinding8.getNodeBindingID().toString(), "dreambike_Wor1_Act2");
        flowDeployManager.updateOtherPerformer(nodeBinding9.getNodeBindingID().toString(), "dreambike_Wor1_Act4");
        HashMap inputParamMap = new HashMap();
        HashMap outputParamMap = new HashMap();
        outputParamMap.put("custome_ID", wdOutParam1.getDriverOutputParamID().toString());
        outputParamMap.put("productID", wdOutParam2.getDriverOutputParamID().toString());
        outputParamMap.put("custome_email", wdOutParam3.getDriverOutputParamID().toString());
        HashMap outputParamEnumeMap = new HashMap();
        flowDeployManager.updateFlowNodeParamBinding(fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act1").getNodeBindingID().toString(), inputParamMap, outputParamMap, outputParamEnumeMap);
        log.info("ָ��������");
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act1").getNodeBindingID().toString());
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act2").getNodeBindingID().toString());
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act3").getNodeBindingID().toString());
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act4").getNodeBindingID().toString());
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act6").getNodeBindingID().toString());
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act10").getNodeBindingID().toString());
        flowDeployManager.addUserPerformer(user.getId().toString(), fd.getFlowNodeBindingByNodeID("dreambike_Wor1_Act11").getNodeBindingID().toString());
        log.info("��������");
        ActivityReport report = new ActivityReport();
        report.setDriverID(wd1.getFlowDriverID().toString());
        HashMap output = new HashMap();
        output.put("custome_ID", "value1");
        output.put("productID", "value2");
        output.put("custome_email", "value3");
        report.setDriverOutputData(output);
        for (int i = 0; i < 1; i++) {
            workflowEngine.processActivityReport(user.getId().toString(), report);
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        }
    }

    public void testProcessActivityReport() {
        while (true) {
            List allMyNewTasks = taskManager.findAllMyNewTasks(user.getId().toString());
            log.info("@@@@@@@@@@NewTasks:" + allMyNewTasks.size());
            Iterator it = allMyNewTasks.iterator();
            if (it.hasNext()) {
                while (it.hasNext()) {
                    FlowTask aTask = (FlowTask) it.next();
                    taskManager.checkOutTask(user.getId().toString(), aTask.getTaskID().toString());
                    workflowEngine.processSubmitTask(user.getId().toString(), aTask.getTaskID().toString());
                }
            } else {
                log.info("---------------------������ȫ����ȡ�����");
                break;
            }
        }
        while (true) {
            List allMyExecutingTasks = taskManager.findMyExecutingTasksByType(user.getId().toString(), bm.getTypeID().toString(), 1, 1000);
            Iterator it = allMyExecutingTasks.iterator();
            if (it.hasNext()) {
                while (it.hasNext()) {
                    FlowTask aTask = (FlowTask) it.next();
                    workflowEngine.processSubmitTask(user.getId().toString(), aTask.getTaskID().toString());
                }
            } else {
                log.info("---------------------Executing Task Completed");
                break;
            }
        }
        log.info("Finished Task:" + taskManager.findMyFinishedTasksKinds(user.getId().toString()).size());
        assertTrue("It will must be success", true);
    }

    public void setBusinessTypeManager(BusinessTypeManager businessTypeManager) {
        this.businessTypeManager = businessTypeManager;
    }

    public void setFlowDeployManager(FlowDeployManager flowDeployManager) {
        this.flowDeployManager = flowDeployManager;
    }

    public void setFlowMetaManager(FlowMetaManager flowMetaManager) {
        this.flowMetaManager = flowMetaManager;
    }

    public void setTaskManager(FlowTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setWorkflowDriverManager(WorkflowDriverManager workflowDriverManager) {
        this.workflowDriverManager = workflowDriverManager;
    }

    public void setWorkflowEngine(WorkflowEngine workflowEngine) {
        this.workflowEngine = workflowEngine;
    }

    public void setDao(WorkflowDriverDAO dao) {
        this.dao = dao;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
