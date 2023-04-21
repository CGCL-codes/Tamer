package org.fosterapet.test.ui;

import static org.junit.Assert.*;
import org.fosterapet.dao.*;
import org.fosterapet.ui.ServletReqEnums.EActionHandler;
import org.fosterapet.ui.ServletReqEnums.EServletReqParam;
import org.greatlogic.gae.*;
import org.greatlogic.gae.GLLog.EGLLogLevel;
import org.greatlogic.gae.dao.DAOEnums.EDAOAction;
import org.greatlogic.gae.dao.*;
import org.json.*;
import org.junit.*;
import org.junit.Test;

public class GetContextTest implements IClientRequestReceiver {

    @Before
    public void setUp() throws Exception {
        GLLog.fine("");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        GLLog.fine("");
    }

    @After
    public void tearDown() throws Exception {
        GLLog.fine("");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        GLLog.fine("");
    }

    @Override
    public void processClientRequest(final ClientRequest clientRequest) {
    }

    @Test
    public void test() throws Exception {
        EGLLogLevel saveLogLevel = GLLog.setLogLevel(EGLLogLevel.Fine);
        try {
            String baseURL = "http://localhost:8888";
            ClientRequestThread clientRequestThread = new ClientRequestThread("OrgTest", this);
            ClientRequest clientRequest;
            JSONObject responseJSONObject;
            JSONArray locTypeJSONArray;
            int numberOfLocTypes = 10;
            for (int locTypeCount = 0; locTypeCount < numberOfLocTypes; ++locTypeCount) {
                clientRequest = new ClientRequest("", baseURL + EActionHandler.LocTypePutCreate.getURL());
                clientRequest.setParam(EServletReqParam.LocType_desc, "Location Type " + (locTypeCount + 1));
                clientRequest.setParam(EServletReqParam.LocType_shortDesc, "LocType" + (locTypeCount + 1));
                clientRequestThread.submitRequest(clientRequest, -1);
            }
            clientRequest = new ClientRequest("", baseURL + EActionHandler.LocTypeGetList.getURL());
            clientRequestThread.submitRequest(clientRequest, -1);
            responseJSONObject = new JSONObject(clientRequest.getResponseLineList().get(0));
            locTypeJSONArray = responseJSONObject.getJSONArray("locTypes");
            for (int locTypeIndex = 0; locTypeIndex < locTypeJSONArray.length(); ++locTypeIndex) {
                LocType locType = DAOHelper.newDAOInstance(EDAOAction.Get, LocType.class, (JSONObject) locTypeJSONArray.get(locTypeIndex));
                GLLog.fine(locType.toString());
            }
            clientRequest = new ClientRequest("", baseURL + EActionHandler.GetContext.getURL());
            clientRequestThread.submitRequest(clientRequest, -1);
            GLLog.info("Request:" + clientRequest);
            GLLog.info("Response:" + clientRequest.getResponseLineList());
            assertTrue(clientRequest.getResponseLineList().size() == 1);
            responseJSONObject = new JSONObject(clientRequest.getResponseLineList().get(0));
            GLLog.info("Response JSON:" + responseJSONObject.toString());
            locTypeJSONArray = responseJSONObject.getJSONArray("locTypes");
            GLLog.info("Response LocTypes:" + locTypeJSONArray);
            for (int locTypeIndex = 0; locTypeIndex < locTypeJSONArray.length(); ++locTypeIndex) {
                GLLog.info(locTypeJSONArray.get(locTypeIndex).toString());
            }
        } finally {
            GLLog.setLogLevel(saveLogLevel);
        }
    }
}
