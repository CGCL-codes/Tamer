package org.javason.jsonrpc;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.javason.json.BeanToJsonSerializer;
import junit.framework.TestCase;

public class TestJsonRpcConverter extends TestCase {

    public void testJsonRpcConverterInterface() {
        System.out.println("RUNNING TestJsonRpcConverter.testJsonRpcConverterInterface()");
        String requestURI = "/world";
        MockHttpServletRequest request = new MockHttpServletRequest("POST", requestURI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setPathInfo(requestURI);
        JSONObject doSomething = new JSONObject();
        JSONObject testId = new JSONObject();
        try {
            testId.put("someCtx", false);
            doSomething.put("id", testId);
            doSomething.put("method", "testMe");
            JSONArray param1 = new JSONArray();
            ConverterTestBean tjb = new ConverterTestBean();
            tjb.setBigDec(BigDecimal.valueOf(1.1));
            tjb.setBigInt(BigInteger.valueOf(2));
            JSONObject paramObj = BeanToJsonSerializer.serializeToJsonObject(tjb);
            System.out.println(paramObj.toString());
            param1.add(0, paramObj);
            param1.add(1, BigDecimal.valueOf(3.2));
            doSomething.put("params", param1);
            request.setContent(doSomething.toString().getBytes("UTF-8"));
        } catch (JSONException e2) {
            e2.printStackTrace();
            assertTrue(false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        try {
            JsonRpcServiceExporter exporter = new JsonRpcServiceExporter();
            exporter.setServiceBean(new ConverterTestInterface());
            ModelAndView mv = exporter.handleRequest(request, response);
            JsonRpcServiceView iv = new JsonRpcServiceView();
            iv.render(mv.getModel(), request, response);
            System.out.println(response.getContentAsString());
            JSONObject responseObj = JSONObject.fromObject(response.getContentAsString());
            assertTrue(responseObj.has("result"));
            assertTrue(JSONUtils.isNull(responseObj.opt("error")));
            assertTrue(responseObj.getJSONObject("id").toString().equals(testId.toString()));
            assertTrue(JSONUtils.isNull(responseObj.opt("result")));
        } catch (Exception e1) {
            e1.printStackTrace();
            assertTrue(false);
        }
        System.out.println("--");
    }
}
