package com.myrealtor.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class MyRealtorExceptionHandller implements HandlerExceptionResolver {

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        log.error("Handling exception " + e.getClass() + " for handler: " + handler, e);
        ModelAndView model = new ModelAndView("exception");
        model.addObject("message", "General Error!");
        return model;
    }
}
