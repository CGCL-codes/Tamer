package com.tchepannou.rails.engine.container;

import com.tchepannou.rails.core.api.MailController;
import com.tchepannou.rails.core.api.ServiceContext;
import com.tchepannou.rails.core.api.Service;
import com.tchepannou.rails.core.service.MailService;
import com.tchepannou.rails.core.service.OptionService;
import com.tchepannou.rails.core.service.RenderService;
import com.tchepannou.rails.core.impl.PropertiesOptionService;
import com.tchepannou.rails.core.impl.VelocityRenderService;
import com.tchepannou.rails.engine.Engine;
import com.tchepannou.rails.mail.TestMailController;
import com.tchepannou.rails.mock.servlet.MockServletContext;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import junit.framework.TestCase;

/**
 *
 * @author herve
 */
public class MailControllerContainerTest extends TestCase implements MailService {

    private Engine _cc;

    private MailController _controller;

    public MailControllerContainerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _cc = new Engine();
        _cc.setBasePackage("com.tchepannou.rails");
        _cc.setServletContext(new MockServletContext());
        register(RenderService.class, new VelocityRenderService());
        register(OptionService.class, new PropertiesOptionService());
        register(MailService.class, this);
    }

    public void testTXT() throws Exception {
        MailControllerContainer container = new MailControllerContainer();
        container.init(_cc);
        MailController mc = new TestMailController();
        mc.setFrom("ray.sponsible@google.com");
        mc.addTo("herve.tchepannou@gmail.com");
        container.deliver("/test/hello.txt", "herve");
        assertNotNull("no message sent", _controller);
        assertEquals("from", mc.getFrom(), _controller.getFrom());
        assertTrue("to", _controller.getTo().contains("herve.tchepannou@gmail.com"));
        assertEquals("subject", "Subject", _controller.getSubject());
        assertEquals("body", "Hello herve", _controller.getBody());
        assertEquals("contentType", "text/plain", _controller.getContentType());
        assertEquals("encoding", "utf-8", _controller.getEncoding());
    }

    public void testHTML() throws Exception {
        MailControllerContainer container = new MailControllerContainer();
        container.init(_cc);
        MailController mc = new TestMailController();
        mc.setFrom("ray.sponsible@google.com");
        mc.addTo("herve.tchepannou@gmail.com");
        container.deliver("/test/hello.html", "herve");
        assertNotNull("no message sent", _controller);
        assertEquals("from", mc.getFrom(), _controller.getFrom());
        assertTrue("to", _controller.getTo().contains("herve.tchepannou@gmail.com"));
        assertEquals("subject", "Subject", _controller.getSubject());
        assertEquals("body", "<html><body>Hello </b>herve</b></body></html>", _controller.getBody());
        assertEquals("contentType", "text/html", _controller.getContentType());
        assertEquals("encoding", "utf-8", _controller.getEncoding());
    }

    private void register(Class<? extends Service> clazz, Service srv) {
        _cc.registerService(clazz, srv);
        ServiceContext sc = _cc.createServiceContext();
        srv.init(sc);
    }

    public void send(MailController controller) throws MessagingException {
        _controller = controller;
    }

    public void init(ServiceContext context) {
    }

    public void destroy() {
    }
}
