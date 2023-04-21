package com.codegen.persistence.handler;

import com.codegen.common.CodeGenConstants;

public class PortletHandlerTest extends BaseHandlerTestCase {

    public void testHandle() throws Exception {
        PortletHandler ch = new PortletHandler();
        ctx.setInputParameter(CodeGenConstants.web_portlet_portletName, "hello");
        ch.doHandle("<portlet> <portlet-name>1</portlet-name></portlet>", ctx);
    }

    public void testValidate() {
    }
}
