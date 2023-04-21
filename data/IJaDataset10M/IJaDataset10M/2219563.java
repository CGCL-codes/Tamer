package org.zkoss.web.servlet.jsp;

import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.zkoss.lang.D;
import org.zkoss.lang.Classes;
import org.zkoss.util.CacheMap;
import org.zkoss.util.logging.Log;
import org.zkoss.util.WaitLock;
import org.zkoss.web.servlet.http.Https;

/**
 * JspLoaderServlet Load the compiled JSP pages.
 * @author andrewho
 */
public final class JspLoaderServlet extends HttpServlet {

    private static final Log log = Log.lookup(JspLoaderServlet.class);

    /** The jsp page associated with the compiled jsp page class. */
    private final Map _jspServlets = new JspCache();

    public JspLoaderServlet() {
    }

    /**Get the compiled jsp servlet class.
	 * @param servletPath The servet path.
	 * @return Servlet The compiled jsp servlet class.
	 */
    public final Servlet getJspServlet(final String servletPath) throws ServletException {
        WaitLock lock = null;
        for (; ; ) {
            final Object o;
            synchronized (_jspServlets) {
                o = _jspServlets.get(servletPath);
                if (o == null) _jspServlets.put(servletPath, lock = new WaitLock());
            }
            if (o instanceof Servlet) return (Servlet) o;
            if (o == null) break;
            if (!((WaitLock) o).waitUntilUnlock(5 * 60 * 1000)) log.warning("Take too long to wait loading JSP: " + servletPath + "\nTry to load this page again automatically...");
        }
        try {
            final String className = Jsps.mapJspToJavaName(servletPath);
            if (D.ON && log.finerable()) log.finer("Loading jsp: " + className + " path=" + servletPath);
            final Servlet servlet = (Servlet) Classes.newInstanceByThread(className);
            servlet.init(getServletConfig());
            synchronized (_jspServlets) {
                _jspServlets.put(servletPath, servlet);
            }
            return servlet;
        } catch (Throwable ex) {
            synchronized (_jspServlets) {
                _jspServlets.remove(servletPath);
            }
            if (ex instanceof RuntimeException) throw (RuntimeException) ex;
            if (ex instanceof Error) throw (Error) ex;
            if (ex instanceof ServletException) throw (ServletException) ex;
            throw new ServletException(ex);
        } finally {
            lock.unlock();
        }
    }

    protected void service(HttpServletRequest hreq, HttpServletResponse hres) throws IOException, ServletException {
        getJspServlet(Https.getThisServletPath(hreq)).service(hreq, hres);
    }

    public void destroy() {
        synchronized (_jspServlets) {
            for (Iterator it = _jspServlets.values().iterator(); it.hasNext(); ) {
                final Object val = it.next();
                if (val instanceof Servlet) ((Servlet) val).destroy();
            }
            _jspServlets.clear();
        }
    }

    private static class JspCache extends CacheMap {

        JspCache() {
            setMaxSize(500);
            setLifetime(60 * 60 * 1000);
        }

        protected void onExpunge(Value v) {
            final Object val = v.getValue();
            if (val instanceof Servlet) ((Servlet) val).destroy();
        }
    }
}
