package com.wikipy.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.wikipy.content.ContentService;
import com.wikipy.repository.RepositoryService;

/**
 * Servlet implementation class TextFormReceiver
 */
public class TextFormReceiver extends HttpServlet {

    private static final String UTF_8 = "UTF-8";

    private static final long serialVersionUID = 1L;

    private ContentService contentService;

    private RepositoryService repositoryService;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TextFormReceiver() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        contentService = (ContentService) ctx.getBean("contentService");
        repositoryService = (RepositoryService) ctx.getBean("repositoryService");
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        Enumeration enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String key = (String) enums.nextElement();
            params.put(key, request.getParameter(key));
        }
        if (request.getParameterValues(RepositoryService.PROP_ASPECT) != null) {
            params.put(RepositoryService.PROP_ASPECT, request.getParameterValues(RepositoryService.PROP_ASPECT));
        }
        String pid = request.getParameter("_parentid");
        if (pid == null) {
            response.sendError(400);
            return;
        }
        try {
            repositoryService.appendChildren(pid, params);
        } catch (HttpStatusExceptionImpl e) {
            response.sendError(e.getCode());
        } catch (RuntimeException e) {
            response.sendError(500);
        }
    }
}
