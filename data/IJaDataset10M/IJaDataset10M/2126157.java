package br.gov.framework.demoiselle.view.faces.util;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.easymock.EasyMock;

public class ManagedBeanUtilStub {

    @SuppressWarnings("unchecked")
    public static void configure() {
        HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
        replay(response);
        HttpSession session = EasyMock.createNiceMock(HttpSession.class);
        session.setAttribute("Test", null);
        session.setAttribute("ReportName", null);
        Enumeration<String> en = EasyMock.createMock(Enumeration.class);
        expect(en.hasMoreElements()).andReturn(true).times(2);
        expect(en.hasMoreElements()).andReturn(false).times(1);
        expect(en.nextElement()).andReturn("Test").times(1);
        expect(en.nextElement()).andReturn("ReportName").anyTimes();
        replay(en);
        expect(session.getAttributeNames()).andReturn(en);
        replay(session);
        expect(request.getSession()).andReturn(session).anyTimes();
        expect(request.getParameter("ReportName")).andReturn("reportTest");
        expect(request.getContextPath()).andReturn("/test");
        expect(request.getLocalPort()).andReturn(1212);
        expect(request.getLocalName()).andReturn("0.0.0.0");
        replay(request);
        ManagedBeanUtil.setRequest(request);
        ManagedBeanUtil.setResponse(response);
    }

    @SuppressWarnings("unchecked")
    public static void configureReportNameNull() {
        HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
        replay(response);
        HttpSession session = EasyMock.createNiceMock(HttpSession.class);
        session.setAttribute("Test", null);
        Enumeration<String> en = EasyMock.createMock(Enumeration.class);
        expect(en.hasMoreElements()).andReturn(true).times(1);
        expect(en.hasMoreElements()).andReturn(false).times(1);
        expect(en.nextElement()).andReturn("Test").times(1);
        replay(en);
        expect(session.getAttributeNames()).andReturn(en);
        replay(session);
        expect(request.getSession()).andReturn(session).anyTimes();
        expect(request.getParameter("ReportName")).andReturn(null);
        expect(request.getLocalName()).andReturn("0.0.0.0");
        replay(request);
        ManagedBeanUtil.setRequest(request);
        ManagedBeanUtil.setResponse(response);
    }

    @SuppressWarnings("unchecked")
    public static void configureIOException() {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        HttpServletResponse response = EasyMock.createNiceMock(HttpServletResponse.class);
        replay(response);
        HttpSession session = EasyMock.createNiceMock(HttpSession.class);
        session.setAttribute("Test", null);
        Enumeration<String> en = EasyMock.createMock(Enumeration.class);
        expect(en.hasMoreElements()).andReturn(true).times(1);
        expect(en.hasMoreElements()).andReturn(false).times(1);
        expect(en.nextElement()).andReturn("Test").times(1);
        replay(en);
        expect(session.getAttributeNames()).andReturn(en);
        replay(session);
        EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
        expect(request.getParameter("ReportName")).andReturn(null).times(1).andThrow(new Exception()).times(1).andReturn("null");
        expect(request.getLocalName()).andReturn("0.0.0.0");
        replay(request);
        ManagedBeanUtil.setRequest(request);
        ManagedBeanUtil.setResponse(response);
    }
}
