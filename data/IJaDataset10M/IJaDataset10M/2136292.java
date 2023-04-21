package com.googlecode.psiprobe.controllers.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.tanukisoftware.wrapper.WrapperManager;

public class RestartJvmController extends ParameterizableViewController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean done = false;
        try {
            Class.forName("org.tanukisoftware.wrapper.WrapperManager");
            logger.info("JVM is RESTARTED by " + request.getRemoteAddr());
            WrapperManager.restartAndReturn();
            done = true;
        } catch (ClassNotFoundException e) {
            logger.info("WrapperManager not found. Do you have wrapper.jar in the classpath?");
        }
        return new ModelAndView(getViewName(), "done", Boolean.valueOf(done));
    }
}
