package com.codegen.persistence.handler;

import com.codegen.common.CodeGenConstants;

public class LiferayPortletHandlerTest extends BaseHandlerTestCase {

    public void testHandle() throws Exception {
        LiferayPortletHandler ch = new LiferayPortletHandler();
        ctx.setInputParameter(CodeGenConstants.web_portlet_portletName, "hello");
        ch.doHandle("<portlet> <portlet-name>1</portlet-name></portlet>", ctx);
    }

    public void testValidate() {
    }
}
