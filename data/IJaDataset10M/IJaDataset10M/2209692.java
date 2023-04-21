package org.mobicents.ssf.examples.web.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mobicents.ssf.examples.services.CallManager;
import org.mobicents.ssf.examples.services.CallStatus;
import org.mobicents.ssf.examples.web.JsonView;
import org.mobicents.ssf.servlet.WebDispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CallController {

    private Logger logger = LoggerFactory.getLogger(CallController.class);

    @Autowired
    CallManager callManager;

    @RequestMapping("/start.do")
    public String processSubmit(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getSession().getServletContext().getNamedDispatcher("webDispatcher");
        dispatcher.include(req, res);
        String appSessionId = (String) req.getAttribute(WebDispatcherServlet.SIP_APPLICATION_SESSION_ID);
        if (logger.isTraceEnabled()) {
            logger.trace("appSessionId:" + appSessionId);
        }
        req.getSession().setAttribute("appSessionId", appSessionId);
        return "calling";
    }

    @RequestMapping("/calling.do")
    public String processCalling(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        return "calling";
    }

    @RequestMapping("/status.do")
    public ModelAndView calling(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String appSessionId = (String) req.getSession().getAttribute("appSessionId");
        if (logger.isTraceEnabled()) {
            logger.trace("appSessionId=" + appSessionId);
        }
        ModelAndView mav = new ModelAndView(JsonView.instance);
        CallStatus status = callManager.getStatus(appSessionId);
        if (logger.isTraceEnabled()) {
            logger.trace("status=" + status);
        }
        mav.addObject(JsonView.JSON_ROOT, status);
        return mav;
    }
}
