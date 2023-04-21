package com.stehno.spring.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import com.stehno.spring.beans.ILazyBeanContainer;

public class LazyCommandControllerTest extends MockObjectTestCase {

    private static final String BEANID = "theCommand";

    private static final String MAPPINGID = "commandMapping";

    private static final String COMMAND_NAME_PARAM = "commandNameParam";

    private Mock mockAppCtx, mockCommand;

    public void testHandleRequestWithBeanContainer() throws Exception {
        final Mock mockBeanContainer = new Mock(ILazyBeanContainer.class);
        mockBeanContainer.expects(once()).method("getApplicationContext").withNoArguments().will(returnValue((ApplicationContext) mockAppCtx.proxy()));
        mockBeanContainer.expects(once()).method("findBean").with(eq(MAPPINGID), eq(IControllerCommand.class)).will(returnValue((IControllerCommand) mockCommand.proxy()));
        final LazyCommandController ctrl = new LazyCommandControllerBuilder((ApplicationContext) mockAppCtx.proxy()).setCommandNameParam(COMMAND_NAME_PARAM).setBeanContainer((ILazyBeanContainer) mockBeanContainer.proxy()).create();
        Mock mockRequest = new Mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameter").with(eq(COMMAND_NAME_PARAM)).will(returnValue(MAPPINGID));
        Mock mockResponse = new Mock(HttpServletResponse.class);
        ModelAndView mav = ctrl.handleRequestInternal((HttpServletRequest) mockRequest.proxy(), (HttpServletResponse) mockResponse.proxy());
        assertNotNull(mav);
        assertEquals(mav.getViewName(), "ok");
    }

    public void testHandleRequestWithoutBeanContainer() throws Exception {
        mockAppCtx.expects(once()).method("getBean").with(eq(BEANID), eq(IControllerCommand.class)).will(returnValue((IControllerCommand) mockCommand.proxy()));
        mockAppCtx.expects(once()).method("containsBean").with(eq(BEANID)).will(returnValue(true));
        Map<String, String> mappings = new HashMap<String, String>();
        mappings.put(MAPPINGID, BEANID);
        final LazyCommandController ctrl = new LazyCommandControllerBuilder((ApplicationContext) mockAppCtx.proxy()).setCommandNameParam(COMMAND_NAME_PARAM).setMappings(mappings).create();
        Mock mockRequest = new Mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameter").with(eq(COMMAND_NAME_PARAM)).will(returnValue(MAPPINGID));
        Mock mockResponse = new Mock(HttpServletResponse.class);
        ModelAndView mav = ctrl.handleRequestInternal((HttpServletRequest) mockRequest.proxy(), (HttpServletResponse) mockResponse.proxy());
        assertNotNull(mav);
        assertEquals(mav.getViewName(), "ok");
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.mockCommand = new Mock(IControllerCommand.class);
        mockCommand.expects(once()).method("execute").with(isA(HttpServletRequest.class), isA(HttpServletResponse.class)).will(returnValue(new ModelAndView("ok")));
        this.mockAppCtx = new Mock(ApplicationContext.class);
    }

    protected void tearDown() throws Exception {
        this.mockCommand = null;
        this.mockAppCtx = null;
        super.tearDown();
    }

    private static final class LazyCommandControllerBuilder {

        private LazyCommandController controller;

        private ApplicationContext appCtx;

        private LazyCommandControllerBuilder(ApplicationContext appCtx) {
            this.appCtx = appCtx;
            this.controller = new LazyCommandController();
        }

        public LazyCommandControllerBuilder setCommandNameParam(String cmd) {
            this.controller.setCommandNameParam(cmd);
            return (this);
        }

        public LazyCommandControllerBuilder setBeanContainer(ILazyBeanContainer cont) {
            this.controller.setBeanContainer(cont);
            return (this);
        }

        public LazyCommandControllerBuilder setMappings(Map<String, String> mappings) {
            this.controller.setMappings(mappings);
            return (this);
        }

        public LazyCommandController create() throws Exception {
            controller.setApplicationContext(appCtx);
            controller.afterPropertiesSet();
            return (controller);
        }
    }
}
