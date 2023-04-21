package it.webscience.kpeople.client;

import net.sf.json.JSONObject;
import org.junit.Test;
import junit.framework.Assert;
import it.webscience.kpeople.client.activiti.ActivitiClient;
import it.webscience.kpeople.client.activiti.IActivitiClient;
import it.webscience.kpeople.client.activiti.IActivitiConstants;
import it.webscience.kpeople.client.activiti.object.ActivitiProcessInstance;
import it.webscience.kpeople.client.activiti.object.ActivitiTaskPerformResponse;
import it.webscience.kpeople.client.activiti.object.ActivitiUserTaskList;

public class TestClient {

    private String patternRequestor = "utente1@kpeople.webscience.it";

    private String patternProvider = "utente2@kpeople.webscience.it";

    private String httpPass = "password";

    private String processDefinitionId = "richiestaContributoFlowAdv:1:116";

    private String nTest = "2";

    private String title = "TITLE" + nTest;

    private String description = "DESCRIPTION" + nTest;

    private String contribution = "CONTRIBUTO" + nTest;

    private String motivation = "MOTIVAZIONE" + nTest;

    private String approva = "true";

    @Test
    public void testRichiestaContributoFlow2() throws Exception {
        String processInstanceId = startActivitiProcessTest(processDefinitionId);
        Assert.assertNotSame(processInstanceId, "");
        String taskInserisciContributo = listTasksAssignee(patternProvider);
        executeActivityTest_InserisceContributo(taskInserisciContributo);
        String taskApprovaContributo = listTasksAssignee(patternRequestor);
        executeActivityTest_ApprovaContributo(taskApprovaContributo);
        String taskVisualizzaContributo = listTasksAssignee(patternProvider);
        executeActivityTest_VisualizzaContributo(taskVisualizzaContributo);
    }

    private String startActivitiProcessTest(String processDefinitionId) throws Exception {
        IActivitiClient client = new ActivitiClient(patternRequestor, httpPass);
        JSONObject jSonObjectInput = new JSONObject();
        jSonObjectInput.put("processDefinitionId", processDefinitionId);
        jSonObjectInput.put("patternProvider", patternProvider);
        jSonObjectInput.put("patternProvider_type", "User");
        jSonObjectInput.put("patternProvider_required", "true");
        jSonObjectInput.put("patternRequestor", patternRequestor);
        jSonObjectInput.put("patternRequestor_type", "User");
        jSonObjectInput.put("patternRequestor_required", "true");
        jSonObjectInput.put("patternTitle", title);
        jSonObjectInput.put("patternDescription", description);
        String jSonParams = jSonObjectInput.toString();
        ActivitiProcessInstance processInstance = client.startActivitiProcessObj(jSonParams);
        String processInst = processInstance.getId();
        Assert.assertNotSame("", processInst);
        return processInst;
    }

    private String listTasksAssignee(String user) throws Exception {
        IActivitiClient client = new ActivitiClient(user, httpPass);
        ActivitiUserTaskList tasks = client.listTasksAssigneeObj(user);
        String ret = tasks.getData().get(0).getId();
        Assert.assertNotSame("", ret);
        Assert.assertEquals(tasks.getData().size(), 1);
        return ret;
    }

    private void executeActivityTest_InserisceContributo(String pTaskId) throws Exception {
        IActivitiClient client = new ActivitiClient(patternProvider, httpPass);
        JSONObject jSonObjectInput = new JSONObject();
        jSonObjectInput.put("patternDescription", description);
        String strJson = jSonObjectInput.toString();
        ActivitiTaskPerformResponse resp = client.performTaskOperationObj(pTaskId, strJson, IActivitiConstants.ACTIVITI_SERVICE_TASK_OPERATION_COMPLETE);
        Assert.assertEquals(resp.isSuccess(), true);
    }

    private void executeActivityTest_ApprovaContributo(String pTaskId) throws Exception {
        IActivitiClient client = new ActivitiClient(patternRequestor, httpPass);
        JSONObject jSonObjectInput = new JSONObject();
        jSonObjectInput.put("contributeApproved", approva);
        jSonObjectInput.put("contributeApproved_type", "Boolean");
        jSonObjectInput.put("contributeMotivation", motivation);
        String strJson = jSonObjectInput.toString();
        ActivitiTaskPerformResponse resp = client.performTaskOperationObj(pTaskId, strJson, IActivitiConstants.ACTIVITI_SERVICE_TASK_OPERATION_COMPLETE);
        Assert.assertEquals(resp.isSuccess(), true);
    }

    private void executeActivityTest_VisualizzaContributo(String pTaskId) throws Exception {
        IActivitiClient client = new ActivitiClient(patternRequestor, httpPass);
        String strJson = "";
        ActivitiTaskPerformResponse resp = client.performTaskOperationObj(pTaskId, strJson, IActivitiConstants.ACTIVITI_SERVICE_TASK_OPERATION_COMPLETE);
        Assert.assertEquals(resp.isSuccess(), true);
    }
}
