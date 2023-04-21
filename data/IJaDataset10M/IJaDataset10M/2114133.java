package com.homeautomate.listener;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.homeautomate.manager.IJdbManager;
import com.homeautomate.manager.ParameterManager;

/**
 * Servlet permettant au démarrage du serveur de démarrer le moteur
 * 
 * @author Gorille
 * 
 */
public class ListenerServlet extends HttpServlet {

    ApplicationContext wac;

    IJdbManager jdbm;

    ParameterManager pm;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        ServletContext servletContext = servletConfig.getServletContext();
        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        jdbm = (IJdbManager) wac.getBean("jdbManager");
        pm = (ParameterManager) wac.getBean("parameterManager");
    }
}
