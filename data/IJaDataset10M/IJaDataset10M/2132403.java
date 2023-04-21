package com.joi.server.servlet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.thoughtworks.xstream.XStream;

public class FrontControllerServlet extends HttpServlet {

    private static final long serialVersionUID = 7874084328348490908L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        this.doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        String servletPath = request.getRequestURI().replace(request.getContextPath(), "");
        System.out.println(servletPath);
        Map<String, String> serviceMap = (Map<String, String>) ctx.getBean("serviceMap", Map.class);
        String classMethod = serviceMap.get(servletPath);
        String[] classMethodArray = classMethod.split(":");
        Object service = (Object) ctx.getBean(classMethodArray[0]);
        com.joi.server.service.RequestResponse rr = new com.joi.server.service.RequestResponse(request, response);
        Method m = null;
        Object resultObject = null;
        System.out.println(Arrays.asList(classMethodArray));
        try {
            m = service.getClass().getMethod(classMethodArray[1], com.joi.server.service.RequestResponse.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        AsyncContext asyncCtx = request.startAsync();
        Executor executor = (Executor) request.getServletContext().getAttribute("executor");
        if (m != null) executor.execute(new AsyncRequestProcessor(m, service, rr, asyncCtx));
    }
}
