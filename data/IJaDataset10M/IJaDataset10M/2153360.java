package com.sporthenon.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public abstract class AbstractServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    protected static Logger logger = Logger.getLogger("sporthenon");

    @Override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected void handleException(Throwable e) {
        logger.error(e.getMessage(), e);
    }
}
