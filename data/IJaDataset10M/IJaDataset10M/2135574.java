package com.tchepannou.rails.engine;

import com.tchepannou.rails.TestUtil;
import com.tchepannou.rails.core.service.OptionService;
import com.tchepannou.rails.core.service.RenderService;
import com.tchepannou.rails.core.service.UserService;
import com.tchepannou.rails.mock.servlet.MockServletContext;
import junit.framework.TestCase;

/**
 *
 * @author herve
 */
public class EngineConfiguratorTest extends TestCase {

    public EngineConfiguratorTest(String testName) {
        super(testName);
    }

    public void testConfigure() throws Exception {
        Engine engine = new Engine();
        EngineConfigurator cfg = new EngineConfigurator();
        MockServletContext sc = new MockServletContext();
        cfg.configure(engine, sc);
        assertEquals("loginURL", "/login", engine.getLoginURL());
        assertEquals("basePackage", "com.tchepannou.rails", engine.getBasePackage());
        assertNotNull("service[UserService]", engine.findService(UserService.class));
        assertNotNull("service[OptionService]", engine.findService(OptionService.class));
        assertNotNull("service[RenderService]", engine.findService(RenderService.class));
        assertEquals("utilClass", TestUtil.class, engine.getUtilClass());
        assertEquals("actionInterceptors", 2, engine.getActionInterceptors().size());
        assertEquals("jobsInteceptors", 2, engine.getJobInterceptors().size());
        assertEquals("mailInteceptors", 3, engine.getMailInterceptors().size());
        assertEquals("messsageInteceptors", 2, engine.getMessageInterceptors().size());
    }
}
