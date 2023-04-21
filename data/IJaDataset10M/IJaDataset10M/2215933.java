package net.admin4j.ui.servlets;

import java.util.HashMap;
import java.util.Map;
import net.admin4j.entity.ExecutionPoint;
import net.admin4j.util.HttpServletRequestMock;
import net.admin4j.util.HttpServletResponseMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestHotSpotDisplayServlet extends BaseServletTestSupport {

    private HotSpotDisplayServlet servlet;

    private HttpServletRequestMock request;

    private HttpServletResponseMock response;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new HotSpotDisplayServlet();
        request = new HttpServletRequestMock();
        response = new HttpServletResponseMock();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDisplayHotSpotPage() throws Exception {
        String selectedDisplayOption = "";
        Map<StackTraceElement, ExecutionPoint> fullExecutionMap = new HashMap<StackTraceElement, ExecutionPoint>();
        Map<StackTraceElement, ExecutionPoint> executionMap = new HashMap<StackTraceElement, ExecutionPoint>();
        Map<StackTraceElement, ExecutionPoint> blockedExecutionMap = new HashMap<StackTraceElement, ExecutionPoint>();
        StackTraceElement element = new StackTraceElement("org.jmu.MadeUpClassName", "ltff", "org.jmu.MadeUpClassName.java", 0);
        ExecutionPoint point = new ExecutionPoint(element);
        point.addCalledStackTraceElement(element);
        point.addCallingStackTraceElement(element);
        point.addBlockingSynchronizedClassName("org.jmu.Fu");
        point.addBlockingSynchronizedClassName("org.jmu.Bar");
        fullExecutionMap.put(element, point);
        executionMap.put(element, point);
        blockedExecutionMap.put(element, point);
        servlet.displayHotSpotPage(request, response, selectedDisplayOption, fullExecutionMap, executionMap, blockedExecutionMap);
        System.out.println("response:\n" + response.getOutputStream());
        System.out.println(point.getBlockingSynchronizedClassList());
    }
}
