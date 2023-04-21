package org.monet.loginservice.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.monet.loginservice.actions.ActionFactory;
import org.monet.loginservice.control.log.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class Login extends HttpServlet {

    private Logger logger;

    private ActionFactory actinFactory;

    @Inject
    public void injectLogger(Logger logger) {
        this.logger = logger;
    }

    @Inject
    public void injectActionFactory(ActionFactory actinFactory) {
        this.actinFactory = actinFactory;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet()");
        logger.info(request.getRequestURL().toString());
        String url = request.getRequestURL().toString();
        if (!url.endsWith("/login/")) super.doGet(request, response); else {
            String action = StringEscapeUtils.escapeHtml(request.getParameter("action"));
            if (action != null) {
                this.actinFactory.getAction(action).execute(request, response, this.getServletContext().getRealPath(""));
            } else {
                this.actinFactory.getAction(ActionFactory.ACTION_NOACTION).execute(request, response, this.getServletContext().getRealPath(""));
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
