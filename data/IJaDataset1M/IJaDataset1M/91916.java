package net.paoding.rose.demo.controller;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.paoding.rose.web.ControllerInterceptorAdapter;

public class TraceInterceptor extends ControllerInterceptorAdapter {

    @Override
    public Object before(HttpServletRequest request, HttpServletResponse response, Object controller, Method action) throws Exception {
        logger.info("enter " + controller.getClass().getSimpleName() + "." + action.getName());
        return true;
    }

    @Override
    public void after(HttpServletRequest request, HttpServletResponse response, Object controller, Method action, Object instruction) throws Exception {
        logger.debug("return " + instruction + " after " + controller.getClass().getSimpleName() + "." + action.getName());
    }
}
