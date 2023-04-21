package com.sjt.pi.controller;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sjt.pi.engine.MappingEngine;
import com.sjt.pi.engine.PIEngine;
import com.sjt.pi.utils.RequestConstants;

/**
 * Servlet implementation class Mainframe
 */
public class Mainframe extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String ERROR_PAGE = "404.jsp";

    private static final String RESULT_ARG = "result";

    private static PIEngine engine;

    private static MappingEngine mEngine;

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public Mainframe() {
        super();
        engine = PIEngine.getInstance();
        mEngine = PIEngine.getInstance().getMappingEngine();
    }

    /**
	 * @see Servlet#init(ServletConfig)
	 */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
	 * @see HttpServlet#service(HttpServletRereq.setAttribute("fits", true);quest request, HttpServletResponse response)
	 */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            engine.setupDataContainer(request, response);
            SessionProvider.setSession(request.getSession());
            final String action = request.getParameter(RequestConstants.ACTION_KEY);
            if (request.getParameter(RequestConstants.ACTION_KEY) != null) {
                request.getSession().setAttribute(RequestConstants.VALID, true);
                if (mEngine.containsKey(action)) {
                    engine.getChainManager().run(engine.getDataContainer());
                    if (request.getAttribute(RESULT_ARG) != null) {
                        response.sendRedirect((String) request.getAttribute(RESULT_ARG));
                    } else {
                        response.sendRedirect(mEngine.get(action));
                    }
                } else {
                    response.sendRedirect(ERROR_PAGE);
                }
            } else {
                response.sendRedirect(ERROR_PAGE);
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
