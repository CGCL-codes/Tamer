package net.zschech.gwt.comet.server.impl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.mortbay.jetty.SessionManager;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.Context.SContext;

/**
 * An extension of {@link BlockingAsyncServlet} for Jetty.
 * 
 * This extension improves on the default session keep alive strategy, refreshing the connection just before the session
 * expires, by updating the session managers last access time when ever sending data down the Comet connection
 * 
 * @author Richard Zschech
 */
public class Jetty6AsyncServlet extends BlockingAsyncServlet {

    private SessionManager sessionManager;

    @Override
    public void init(ServletContext context) throws ServletException {
        super.init(context);
        sessionManager = ((Context) ((SContext) context).getContextHandler()).getSessionHandler().getSessionManager();
    }

    @Override
    protected boolean access(HttpSession httpSession) {
        sessionManager.access(httpSession, false);
        return true;
    }
}
