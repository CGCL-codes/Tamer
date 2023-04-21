package com.acv.webapp.common;

import java.util.HashMap;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import com.acv.dao.common.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public abstract class BaseActionTestCase extends TestCase {

    private static final Logger log = Logger.getLogger(BaseActionTestCase.class);

    protected static final XmlWebApplicationContext ctx;

    protected MockHttpServletRequest request = new MockHttpServletRequest();

    static {
        String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
        String[] paths = { "classpath*:/" + pkg + "/applicationContext-*.xml", "classpath*:META-INF/applicationContext-*.xml", "/WEB-INF/action-servlet.xml" };
        ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(paths);
        ctx.setServletContext(new MockServletContext(""));
        ctx.refresh();
    }

    protected void setUp() throws Exception {
        LocalizedTextUtil.addDefaultResourceBundle(Constants.BUNDLE_KEY);
        ActionContext.getContext().setSession(new HashMap());
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");
        ServletActionContext.setRequest(request);
    }

    protected void tearDown() throws Exception {
        ActionContext.getContext().setSession(null);
    }
}
