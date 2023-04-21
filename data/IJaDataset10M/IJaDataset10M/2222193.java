package com.stockapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.stockapp.generateView.GenerateView;
import com.stockapp.util.HttpHelper;

public class SpringappController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirect = request.getParameter("redirect");
        logger.info("In springappcontroller. Redirect = " + redirect);
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        Object bean = null;
        GenerateView view = null;
        try {
            view = (GenerateView) context.getBean(redirect);
        } catch (Exception e) {
            System.out.println("bean not defined. continuuing to jsp");
        }
        try {
            if (view != null) redirect = view.buildContent(request);
        } catch (Exception e) {
            System.out.println("Exception e " + e.getMessage());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/" + redirect + ".jsp");
        dispatcher.forward(request, response);
        return null;
    }
}
