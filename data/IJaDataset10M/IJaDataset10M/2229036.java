package net.sf.jsog.spring;

import net.sf.jsog.factory.bean.BeanJsogFactory;
import java.util.HashMap;
import java.util.Map;
import org.easymock.Capture;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import javax.servlet.ServletOutputStream;
import net.sf.jsog.JSOG;
import org.springframework.http.MediaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author jrodriguez
 */
public class JsogViewTest {

    public static class TestBean {

        private String foo = "foovalue";

        private String bar = "barvalue";

        public String getFoo() {
            return foo;
        }

        public String getBar() {
            return bar;
        }
    }

    JsogView instance;

    HttpServletRequest request;

    HttpServletResponse response;

    @Before
    public void setUp() {
        instance = new JsogView();
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testRenderMergedOutputModel() throws Exception {
        String encoding = "ISO-8859-1";
        JSOG expected = new JSOG("foobar");
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("JSOG", expected);
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelCustomContentType() throws Exception {
        String encoding = "ISO-8859-1";
        JSOG expected = new JSOG("foobar");
        MediaType contentType = MediaType.TEXT_PLAIN;
        instance.setOutputContentType(contentType);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("JSOG", expected);
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelCustomEncodingString() throws Exception {
        String encoding = "UTF-8";
        JSOG expected = new JSOG("foobar");
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("JSOG", expected);
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos);
        instance.setEncoding(encoding);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelCustomEncodingCharset() throws Exception {
        String encoding = "UTF-8";
        JSOG expected = new JSOG("foobar");
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("JSOG", expected);
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos);
        instance.setEncoding(Charset.forName(encoding));
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelJSONP() throws Exception {
        String encoding = "ISO-8859-1";
        String callback = "foo";
        JSOG expectedJson = new JSOG("foobar");
        String expected = callback + "(" + expectedJson + ")";
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("JSOG", expectedJson);
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(callback);
        replay(request, response, sos);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        String actual = new String(out.getValue(), encoding);
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelJSONPCustomCallback() throws Exception {
        String encoding = "ISO-8859-1";
        String callback = "foo";
        String callbackParamName = "bar";
        JSOG expectedJson = new JSOG("foobar");
        String expected = callback + "(" + expectedJson + ")";
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("JSOG", expectedJson);
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter(callbackParamName)).andReturn(callback);
        replay(request, response, sos);
        instance.setJsonpCallbackParam(callbackParamName);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        String actual = new String(out.getValue(), encoding);
        assertEquals(actual, expected);
    }

    /**
     * This tests that complex models can be rendered properly.
     * A complex model is one that doesn't have "JSOG" as it's only key (excepting BindingResult values).
     * @throws Exception
     */
    @Test
    public void testRenderMergedOutputModelComplex() throws Exception {
        String encoding = "ISO-8859-1";
        JSOG expected = JSOG.object("foo", "foovalue").put("bar", "barvalue").put("obj", JSOG.object());
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("foo", "foovalue");
        model.put("bar", "barvalue");
        model.put("obj", JSOG.object());
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelBean() throws Exception {
        String encoding = "ISO-8859-1";
        JSOG expected = JSOG.object("bean", JSOG.object().put("foo", "foovalue").put("bar", "barvalue"));
        MediaType contentType = MediaType.APPLICATION_JSON;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("bean", new TestBean());
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }

    @Test
    public void testRenderMergedOutputModelBeanCustomBeanJsogFactory() throws Exception {
        String encoding = "ISO-8859-1";
        JSOG beanJsog = JSOG.object("foo", "foovalue").put("bar", "barvalue");
        JSOG expected = JSOG.object("bean", beanJsog);
        MediaType contentType = MediaType.APPLICATION_JSON;
        BeanJsogFactory bjf = createMock(BeanJsogFactory.class);
        instance.setBeanJsogFactory(bjf);
        expect(bjf.create(isA(TestBean.class))).andReturn(beanJsog);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("bean", new TestBean());
        ServletOutputStream sos = createMock(ServletOutputStream.class);
        expect(response.getOutputStream()).andReturn(sos);
        Capture<byte[]> out = new Capture<byte[]>();
        sos.write(capture(out));
        expectLastCall();
        sos.flush();
        expectLastCall();
        sos.close();
        expectLastCall();
        response.setContentType(contentType.toString());
        expectLastCall();
        response.setCharacterEncoding(encoding);
        expectLastCall();
        response.setContentLength(expected.toString().getBytes(encoding).length);
        expectLastCall();
        expect(request.getParameter("callback")).andReturn(null);
        replay(request, response, sos, bjf);
        instance.renderMergedOutputModel(model, request, response);
        verify(request, response, sos, bjf);
        assertTrue(out.hasCaptured());
        JSOG actual = JSOG.parse(new String(out.getValue(), encoding));
        assertEquals(actual, expected);
    }
}
