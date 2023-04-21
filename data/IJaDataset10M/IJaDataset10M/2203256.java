package com.kejikeji.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class BaseMultiActionController extends MultiActionController {

    public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws Throwable {
        logger.error(ex.getMessage(), ex);
        throw ex;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        String name = this.getMethodNameResolver().getHandlerMethodName(arg0);
        long startTime = System.currentTimeMillis();
        try {
            return super.handleRequestInternal(arg0, arg1);
        } finally {
            long totalTimeMillis = System.currentTimeMillis() - startTime;
            logger.trace("StopWatch '" + name + "': running time (millis) = " + totalTimeMillis);
        }
    }
}
