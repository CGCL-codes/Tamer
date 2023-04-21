package php.java.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import php.java.bridge.ISession;
import php.java.bridge.http.IContext;
import php.java.bridge.http.IContextFactory;

/**
 * Create session contexts for servlets.<p> In addition to the
 * standard ContextFactory this factory keeps a reference to the
 * HttpServletRequest.
 *
 * @see php.java.bridge.http.ContextFactory
 * @see php.java.bridge.http.ContextServer
 */
public class RemoteServletContextFactory extends SimpleServletContextFactory {

    protected RemoteServletContextFactory(Servlet servlet, ServletContext ctx, HttpServletRequest proxy, HttpServletRequest req, HttpServletResponse res) {
        super(servlet, ctx, proxy, req, res, false);
    }

    /**
     * Set the HttpServletRequest for session sharing.
     *  @param req The HttpServletRequest
     */
    protected void setSessionFactory(HttpServletRequest req) {
        this.proxy = req;
    }

    /**{@inheritDoc}*/
    public ISession getSession(String name, short clientIsNew, int timeout) {
        if (name != null) return visited.getSimpleSession(name, clientIsNew, timeout);
        if (session != null) return session;
        if (proxy == null) throw new NullPointerException("This context " + getId() + " doesn't have a session proxy.");
        return session = HttpSessionFacade.getFacade(this, kontext, proxy, res, clientIsNew, timeout);
    }

    /**
     * Create and add a new ContextFactory.
     * @param servlet The servlet
     * @param kontext The servlet context
     * @param proxy The request proxy
     * @param req The HttpServletRequest
     * @param res The HttpServletResponse
     * @return The created ContextFactory
     */
    public static IContextFactory addNew(Servlet servlet, ServletContext kontext, HttpServletRequest proxy, HttpServletRequest req, HttpServletResponse res) {
        RemoteServletContextFactory ctx = new RemoteServletContextFactory(servlet, kontext, proxy, req, res);
        return ctx;
    }

    /**
     * Return an emulated JSR223 context.
     * @return The context.
     * @see php.java.servlet.HttpContext
     */
    public IContext createContext() {
        IContext ctx = new HttpContext(kontext, req, res);
        ctx.setAttribute(IContext.SERVLET_CONTEXT, kontext, IContext.ENGINE_SCOPE);
        ctx.setAttribute(IContext.SERVLET_CONFIG, servlet.getServletConfig(), IContext.ENGINE_SCOPE);
        ctx.setAttribute(IContext.SERVLET, servlet, IContext.ENGINE_SCOPE);
        ctx.setAttribute(IContext.SERVLET_REQUEST, new RemoteHttpServletRequest(this, req), IContext.ENGINE_SCOPE);
        ctx.setAttribute(IContext.SERVLET_RESPONSE, new RemoteHttpServletResponse(res), IContext.ENGINE_SCOPE);
        return ctx;
    }
}
