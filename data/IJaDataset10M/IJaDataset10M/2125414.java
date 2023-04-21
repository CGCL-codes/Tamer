package org.infoeng.ofbiz.edi;

import org.ofbiz.webapp.event.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OpenAS2EventHandler implements EventHandler {

    public void init(ServletContext ctxt) throws EventHandlerException {
    }

    public String invoke(String eventPath, String eventMethod, HttpServletRequest req, HttpServletResponse res) throws EventHandlerException {
        return "SUCCESS";
    }
}
