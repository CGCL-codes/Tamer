package org.springframework.webflow.context.servlet;

import java.util.LinkedHashMap;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.webflow.context.servlet.ParameterBasedFlowUrlHandler;
import org.springframework.webflow.core.collection.CollectionUtils;
import org.springframework.webflow.core.collection.LocalAttributeMap;

public class ParameterBasedFlowUrlHandlerTests extends TestCase {

    private ParameterBasedFlowUrlHandler urlHandler = new ParameterBasedFlowUrlHandler();

    private MockHttpServletRequest request = new MockHttpServletRequest();

    public void testGetFlowId() {
        request.addParameter("_flowId", "foo");
        assertEquals("foo", urlHandler.getFlowId(request));
    }

    public void testGetFlowExecutionKey() {
        request.addParameter("_flowExecutionKey", "12345");
        assertEquals("12345", urlHandler.getFlowExecutionKey(request));
    }

    public void testCreateFlowDefinitionUrl() {
        request.setContextPath("/springtravel");
        request.setServletPath("/app");
        request.setPathInfo("/flows");
        String url = urlHandler.createFlowDefinitionUrl("bookHotel", null, request);
        assertEquals("/springtravel/app/flows?_flowId=bookHotel", url);
    }

    public void testCreateFlowDefinitionUrlEmptyInput() {
        request.setContextPath("/springtravel");
        request.setServletPath("/app");
        request.setPathInfo("/flows");
        String url = urlHandler.createFlowDefinitionUrl("bookHotel", CollectionUtils.EMPTY_ATTRIBUTE_MAP, request);
        assertEquals("/springtravel/app/flows?_flowId=bookHotel", url);
    }

    public void testCreateFlowDefinitionUrlWithFlowInput() {
        request.setContextPath("/springtravel");
        request.setServletPath("/app");
        request.setPathInfo("/flows");
        LocalAttributeMap input = new LocalAttributeMap(new LinkedHashMap());
        input.put("foo", "bar");
        input.put("bar", "needs encoding");
        input.put("baz", new Integer(1));
        input.put("boop", null);
        String url = urlHandler.createFlowDefinitionUrl("bookHotel", input, request);
        assertEquals("/springtravel/app/flows?_flowId=bookHotel&foo=bar&bar=needs+encoding&baz=1&boop=", url);
    }

    public void testCreateFlowExecutionUrl() {
        request.setContextPath("/springtravel");
        request.setServletPath("/app");
        request.setPathInfo("/flows");
        String url = urlHandler.createFlowExecutionUrl("bookHotel", "12345", request);
        assertEquals("/springtravel/app/flows?_flowId=bookHotel&_flowExecutionKey=12345", url);
    }
}
