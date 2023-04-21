package net.sf.webwarp.util.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.webwarp.util.web.ResourceFilter;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ResourceFilterTest {

    private ByteArrayOutputStream bos;

    @Before
    public void installAppender() {
        Logger log = Logger.getLogger(ResourceFilter.class);
        Layout layout = new SimpleLayout();
        bos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(bos);
        WriterAppender appender = new WriterAppender(layout, pw);
        log.addAppender(appender);
    }

    @Test
    public void testDefault() throws ServletException, IOException {
        String resource = "/clock.gif";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/orca/resources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        FilterConfig config = new MockFilterConfig();
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(getBytes("/defaulttheme" + resource), getBytes(response));
        Assert.assertEquals("image/gif", response.getContentType());
    }

    @Test
    public void testSetDefault() throws ServletException, IOException {
        String resource = "/clock.gif";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/orca/resources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter(ResourceFilter.PATH_KEY, ResourceFilter.DEFAULT_PATH);
        config.addInitParameter(ResourceFilter.THEME_NAME_KEY, ResourceFilter.DEFAULT_THEME);
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(getBytes("/defaulttheme" + resource), getBytes(response));
        Assert.assertEquals("image/gif", response.getContentType());
    }

    @Test
    public void testSetNonDefault() throws ServletException, IOException {
        String resource = "/clock.gif";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/myResources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter(ResourceFilter.PATH_KEY, "/myResources");
        config.addInitParameter(ResourceFilter.THEME_NAME_KEY, "/myTheme");
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(getBytes("/defaulttheme" + resource), getBytes(response));
        Assert.assertEquals("image/gif", response.getContentType());
    }

    @Test
    public void testWrongPath() throws ServletException, IOException {
        String resource = "/clock.gif";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/myResources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter(ResourceFilter.PATH_KEY, "/wrongPath");
        config.addInitParameter(ResourceFilter.THEME_NAME_KEY, "/myTheme");
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(new byte[0], getBytes(response));
        Assert.assertEquals("FATAL - the url pattern must match: /wrongPath found uri: /webcontext/myResources/clock.gif" + IOUtils.LINE_SEPARATOR, getLogs());
    }

    @Test
    public void testNoFileSuffix() throws ServletException, IOException {
        String resource = "/clock";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/myResources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter(ResourceFilter.PATH_KEY, "/myResources");
        config.addInitParameter(ResourceFilter.THEME_NAME_KEY, "/myTheme");
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(new byte[0], getBytes(response));
        Assert.assertEquals("FATAL - no file suffix found for resource: /webcontext/myResources/clock" + IOUtils.LINE_SEPARATOR, getLogs());
    }

    @Test
    public void testWrongMimeType() throws ServletException, IOException {
        String resource = "/clock.abc";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/myResources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter(ResourceFilter.PATH_KEY, "/myResources");
        config.addInitParameter(ResourceFilter.THEME_NAME_KEY, "/myTheme");
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(new byte[0], getBytes(response));
    }

    @Test
    public void testInexistentResource() throws ServletException, IOException {
        String resource = "/notExists.gif";
        HttpServletRequest request = new MockHttpServletRequest("POST", "/webcontext/myResources" + resource);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter(ResourceFilter.PATH_KEY, "/myResources");
        config.addInitParameter(ResourceFilter.THEME_NAME_KEY, "/myTheme");
        ResourceFilter filter = new ResourceFilter();
        filter.init(config);
        filter.doFilter(request, response, chain);
        Assert.assertArrayEquals(new byte[0], getBytes(response));
        Assert.assertEquals("FATAL - error loading resource: /notExists.gif" + IOUtils.LINE_SEPARATOR, getLogs());
    }

    @After
    public void removeAppenders() {
        Logger log = Logger.getLogger(ResourceFilter.class);
        log.removeAllAppenders();
    }

    private String getLogs() {
        return new String(bos.toByteArray());
    }

    private byte[] getBytes(String resource) throws IOException {
        InputStream is = ResourceFilterTest.class.getResourceAsStream(resource);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(is, out);
        return out.toByteArray();
    }

    private byte[] getBytes(HttpServletResponse response) throws IOException {
        DelegatingServletOutputStream outputStream = (DelegatingServletOutputStream) response.getOutputStream();
        ByteArrayOutputStream stream = (ByteArrayOutputStream) outputStream.getTargetStream();
        return stream.toByteArray();
    }
}
