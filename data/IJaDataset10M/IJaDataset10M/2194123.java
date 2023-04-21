package com.continuent.tungsten.manager.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.manager.common.EventAddress;
import com.continuent.tungsten.manager.common.ResourceDefinition;
import com.continuent.tungsten.manager.core.ActionResponse;
import com.continuent.tungsten.manager.core.ClusterManagerResponse;
import com.continuent.tungsten.manager.core.ClusterManagerResponseMap;
import com.continuent.tungsten.manager.core.ServiceManager;
import com.continuent.tungsten.manager.core.ServiceManagerState;
import com.continuent.tungsten.manager.core.SuccessCriterion;
import com.continuent.tungsten.manager.exception.HandlerException;
import com.continuent.tungsten.manager.exception.ManagerException;
import com.continuent.tungsten.manager.handler.event.EventIdentifier;
import com.continuent.tungsten.manager.jmx.DynamicMBeanExec;

public class MembershipAddressTest {

    static Logger logger = Logger.getLogger(MembershipAddressTest.class);

    private static TungstenProperties testProperties;

    private static long JOIN_TIMEOUT;

    private static String IDLE_CONNECTION_TIMEOUT;

    private static long EVENT_ROUTE_TIMEOUT;

    public static String testHandlerName;

    @BeforeClass
    public static void setupClass() throws Exception {
        String propertiesFile = System.getProperty("test.properties");
        File f = new File(propertiesFile);
        if (!f.exists()) {
            throw new Exception("Unable to locate test.properties at: " + propertiesFile);
        }
        testProperties = new TungstenProperties();
        FileInputStream fstream = new FileInputStream(propertiesFile);
        testProperties.load(fstream);
        fstream.close();
        testHandlerName = "TestHandler";
        PropertyHelper.createHandlerFile("TestHandler.handler", "com.continuent.tungsten.manager.test.handlers.TestHandler");
        String joinTimeoutString = (String) testProperties.get("JOIN_TIMEOUT");
        JOIN_TIMEOUT = Long.valueOf(joinTimeoutString);
        IDLE_CONNECTION_TIMEOUT = (String) testProperties.get("IDLE_CONNECTION_TIMEOUT");
        String eventRouteTimeoutString = (String) testProperties.get("EVENT_ROUTE_TIMEOUT");
        EVENT_ROUTE_TIMEOUT = Long.valueOf(eventRouteTimeoutString);
    }

    private ClusterManagerResponse firstMemberResponse(ActionResponse actionResponse, ClusterManagerResponse.Status status) {
        logger.debug("Parsing ActionResponseEvent: " + actionResponse.toString());
        Assert.assertTrue(actionResponse != null);
        Assert.assertEquals(0, actionResponse.getWaiting());
        ClusterManagerResponseMap responses = actionResponse.getMemberResponses();
        Assert.assertTrue(responses.size() > 0);
        ClusterManagerResponse clusterManagerResponse = responses.firstResponse();
        Assert.assertTrue(clusterManagerResponse.getStatus() == status);
        return clusterManagerResponse;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void membershipAddressTest() throws Exception {
        PropertyConfigurator.configure(Log4jHelper.createLog4jProperties("MembershipAddressTest.membershipAddressTest"));
        ServiceManager managerInstance = new ServiceManager();
        managerInstance.startup();
        String groupName = "group1";
        String memberName = "member1";
        int weight = 5;
        managerInstance.join(memberName, groupName, weight, JOIN_TIMEOUT);
        Assert.assertTrue(managerInstance.getStateEnum() == ServiceManagerState.ONLINE);
        TungstenProperties connectionProperties = new TungstenProperties();
        connectionProperties.put("IDLE_CONNECTION_TIMEOUT", IDLE_CONNECTION_TIMEOUT);
        long connectionID = managerInstance.open(connectionProperties);
        EventAddress eventAddress = new EventAddress(groupName, memberName, "default");
        EventIdentifier availableRequestIdentifier = managerInstance.execute(connectionID, eventAddress, "available", null, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        ActionResponse actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, availableRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        HashMap responseHashMap = (HashMap) firstMemberResponse(actionResponse, ClusterManagerResponse.Status.SUCCESS).getResult();
        List handlers = (List) responseHashMap.get(memberName);
        Assert.assertNotNull(responseHashMap);
        if (handlers.isEmpty()) {
            Assert.fail("no handlers found");
        }
        boolean found = false;
        Iterator handlersIterator = handlers.iterator();
        while (handlersIterator.hasNext()) {
            String handlerToFind = (String) handlersIterator.next();
            if (handlerToFind.equals(testHandlerName)) {
                found = true;
            }
        }
        Assert.assertTrue(found);
        eventAddress = new EventAddress("resource://group1/member1/default");
        HashMap<String, String> requestHashMap = new HashMap<String, String>();
        requestHashMap.put("resource", "TestHandler");
        EventIdentifier activateRequestIdentifier = managerInstance.execute(connectionID, eventAddress, "activate", requestHashMap, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, activateRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        firstMemberResponse(actionResponse, ClusterManagerResponse.Status.SUCCESS);
        eventAddress = new EventAddress("resource://group1/member1/TestHandler");
        String actionString = "TestHandler";
        EventIdentifier handlerRequestIdentifier = managerInstance.execute(connectionID, eventAddress, actionString, null, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, handlerRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        HashMap response = (HashMap) firstMemberResponse(actionResponse, ClusterManagerResponse.Status.SUCCESS).getResult();
        Assert.assertEquals(3, response.size());
        eventAddress = new EventAddress("resource://group1/member1/default");
        EventIdentifier activeRequestIdentifier = managerInstance.execute(connectionID, eventAddress, "active", null, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, activeRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        responseHashMap = (HashMap) firstMemberResponse(actionResponse, ClusterManagerResponse.Status.SUCCESS).getResult();
        Assert.assertEquals(1, responseHashMap.size());
        ResourceDefinition rd = (ResourceDefinition) responseHashMap.get("TestHandler");
        System.out.println(rd.toString());
        Assert.assertEquals(memberName, rd.getMemberName());
        Assert.assertEquals("com.continuent.tungsten.manager.test.handlers.TestHandler", rd.getHandlerName());
        Assert.assertEquals("TestHandler", rd.getResourceName());
        eventAddress = new EventAddress("resource://group1/member1/default");
        requestHashMap = new HashMap<String, String>();
        requestHashMap.put("resource", "TestHandler");
        EventIdentifier dectivateRequestIdentifier = managerInstance.execute(connectionID, eventAddress, "deactivate", requestHashMap, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, dectivateRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        firstMemberResponse(actionResponse, ClusterManagerResponse.Status.SUCCESS);
        eventAddress = new EventAddress("resource://group1/member1/TestHandler");
        actionString = "TestHandler";
        handlerRequestIdentifier = managerInstance.execute(connectionID, eventAddress, actionString, null, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, handlerRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        ManagerException me = (ManagerException) firstMemberResponse(actionResponse, ClusterManagerResponse.Status.FAIL_EXCEPTION).getException();
        Assert.assertEquals(me.getType(), ManagerException.Type.RESOURCE_NOT_ACTIVATED);
        eventAddress = new EventAddress("resource://group1/member1/default");
        requestHashMap = new HashMap<String, String>();
        requestHashMap.put("resource", "TestHandler");
        activateRequestIdentifier = managerInstance.execute(connectionID, eventAddress, "activa", requestHashMap, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, activateRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        HandlerException he = (HandlerException) firstMemberResponse(actionResponse, ClusterManagerResponse.Status.FAIL_EXCEPTION).getException();
        Assert.assertEquals(HandlerException.Type.COMMAND, he.type);
        requestHashMap = new HashMap<String, String>();
        requestHashMap.put("resource", "badname");
        activateRequestIdentifier = managerInstance.execute(connectionID, eventAddress, "activate", requestHashMap, SuccessCriterion.ALL_MEMBERS, DynamicMBeanExec.NO_TIMEOUT, true).getEventID();
        actionResponse = HandlerManagerHelper.getCompleteActionResponse(managerInstance, connectionID, activateRequestIdentifier, EVENT_ROUTE_TIMEOUT);
        he = (HandlerException) firstMemberResponse(actionResponse, ClusterManagerResponse.Status.FAIL_EXCEPTION).getException();
        Assert.assertEquals(HandlerException.Type.HANDLER, he.type);
    }
}
