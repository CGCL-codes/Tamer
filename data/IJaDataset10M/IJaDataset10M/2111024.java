package br.gov.frameworkdemoiselle.internal.implementation;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.annotation.ViewScoped;
import br.gov.frameworkdemoiselle.util.Faces;
import br.gov.frameworkdemoiselle.util.Reflections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Reflections.class, Faces.class })
public class ParameterImplTest {

    private ParameterImpl<Long> param;

    private HttpServletRequest request;

    private InjectionPoint ip;

    private Converter converter;

    private Annotated annotated;

    private Name name;

    private HttpSession session;

    private Member member;

    @Before
    public void before() {
        ip = createMock(InjectionPoint.class);
        request = createMock(HttpServletRequest.class);
        session = createMock(HttpSession.class);
        annotated = createMock(Annotated.class);
        name = createMock(Name.class);
        converter = createMock(Converter.class);
        member = createMock(Member.class);
        mockStatic(Reflections.class);
        mockStatic(Faces.class);
    }

    private void prepareForTestWithKeyFromNameAnnotation() {
        expect(ip.getAnnotated()).andReturn(annotated).anyTimes();
        expect(ip.getMember()).andReturn(null);
        expect(annotated.isAnnotationPresent(Name.class)).andReturn(true);
        expect(annotated.getAnnotation(Name.class)).andReturn(name);
        expect(name.value()).andReturn("name");
        expect(Reflections.getGenericTypeArgument(EasyMock.anyObject(Member.class), EasyMock.anyInt())).andReturn(Object.class);
        expect(Faces.getConverter(EasyMock.anyObject(Class.class))).andReturn(converter);
    }

    @Test
    public void testConstructorCase1() {
        this.prepareForTestWithKeyFromNameAnnotation();
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("name", param.getKey());
        assertEquals(Object.class, Whitebox.getInternalState(param, "type"));
        assertEquals(converter, param.getConverter());
        verifyAll();
    }

    @Test
    public void testConstructorCase2() {
        expect(member.getName()).andReturn("memberName");
        expect(ip.getAnnotated()).andReturn(annotated).anyTimes();
        expect(ip.getMember()).andReturn(member).anyTimes();
        expect(annotated.isAnnotationPresent(Name.class)).andReturn(false);
        expect(Reflections.getGenericTypeArgument(EasyMock.anyObject(Member.class), EasyMock.anyInt())).andReturn(Object.class);
        expect(Faces.getConverter(EasyMock.anyObject(Class.class))).andReturn(converter);
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("memberName", param.getKey());
        assertEquals(Object.class, Whitebox.getInternalState(param, "type"));
        assertEquals(converter, param.getConverter());
        verifyAll();
    }

    @Test
    public void testGetValueWhenSessionScopedAndParameterValueNotNull() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(Faces.convert("1", converter)).andReturn("return");
        expect(request.getSession()).andReturn(session).anyTimes();
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn("1");
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(true);
        expect(session.getAttribute("name")).andReturn("return");
        session.setAttribute("name", "return");
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("return", param.getValue());
        verifyAll();
    }

    @Test
    public void testGetValueWhenSessionScopedAndParameterValueNull() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(request.getSession()).andReturn(session).anyTimes();
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn(null);
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(true);
        expect(session.getAttribute("name")).andReturn("return");
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("return", param.getValue());
        verifyAll();
    }

    @Test
    public void testGetValueWhenRequestScoped() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(true);
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn("1");
        expect(Faces.convert("1", converter)).andReturn("return");
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("return", param.getValue());
        verifyAll();
    }

    @Test
    public void testGetValueWhenViewScopedWithParamValueNotNull() {
        this.prepareForTestWithKeyFromNameAnnotation();
        Map<String, Object> map = new HashMap<String, Object>();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(ViewScoped.class)).andReturn(true);
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn("1");
        expect(Faces.getViewMap()).andReturn(map);
        expect(Faces.convert("1", converter)).andReturn("return");
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("return", param.getValue());
        assertEquals("return", map.get("name"));
        verifyAll();
    }

    @Test
    public void testGetValueWhenViewScopedWithParamValueNull() {
        this.prepareForTestWithKeyFromNameAnnotation();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "ops");
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(ViewScoped.class)).andReturn(true);
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn(null);
        expect(Faces.getViewMap()).andReturn(map);
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("ops", param.getValue());
        assertEquals("ops", map.get("name"));
        verifyAll();
    }

    @Test
    public void testGetValueElseWithValueNull() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(ViewScoped.class)).andReturn(false);
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn("1");
        expect(Faces.convert("1", converter)).andReturn("return");
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        assertEquals("return", param.getValue());
        verifyAll();
    }

    @Test
    public void testGetValueElseWithValueNotNull() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(ViewScoped.class)).andReturn(false);
        expect(request.getParameter(EasyMock.anyObject(String.class))).andReturn("1");
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        Whitebox.setInternalState(param, "value", "myvalue");
        assertEquals("myvalue", param.getValue());
        verifyAll();
    }

    @Test
    public void testSetValueIsSessionScoped() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(true);
        expect(request.getSession()).andReturn(session);
        session.setAttribute("name", 1L);
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        param.setValue(1L);
        verifyAll();
    }

    @Test
    public void testSetValueIsViewScoped() {
        this.prepareForTestWithKeyFromNameAnnotation();
        Map<String, Object> map = new HashMap<String, Object>();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(ViewScoped.class)).andReturn(true);
        expect(Faces.getViewMap()).andReturn(map);
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        param.setValue(1L);
        assertEquals(1L, map.get("name"));
        verifyAll();
    }

    @Test
    public void testSetValueElse() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(ViewScoped.class)).andReturn(false);
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        param.setValue(1L);
        assertEquals(1L, Whitebox.getInternalState(param, "value"));
        verifyAll();
    }

    @Test
    public void testOthers() {
        this.prepareForTestWithKeyFromNameAnnotation();
        expect(annotated.isAnnotationPresent(SessionScoped.class)).andReturn(false);
        expect(annotated.isAnnotationPresent(RequestScoped.class)).andReturn(true);
        replayAll();
        param = new ParameterImpl<Long>(ip, request);
        param.setValue(1L);
        verifyAll();
    }
}
