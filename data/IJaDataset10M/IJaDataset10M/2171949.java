package com.potix.web.servlet;

import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import com.potix.lang.D;
import com.potix.util.logging.Log;
import com.potix.web.servlet.http.Https;

/**
 * The servlet used to forward a request to another URL.
 * The forward target is specified as init-param, so you might need to
 * create and name one servlet for each target.
 *
 * <p>init-param:
 *
 * <dl>
 *  <dt>target</dt>
 *  <dd>The target URL.</dd>
 * </dl>
 *
 * @author <a href="mailto:tomyeh@potix.com">Tom M. Yeh</a>
 */
public class ForwardServlet extends GenericServlet {

    private static final Log log = Log.lookup(ForwardServlet.class);

    /** The target URL. */
    private String _target;

    public ForwardServlet() {
    }

    public void init() throws ServletException {
        _target = getInitParameter("target");
        if (_target == null || _target.length() == 0) throw new ServletException("ForwardServlet requires the init param, target");
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            final String uri = Https.locate(getServletContext(), request, _target, null);
            Https.forward(getServletContext(), request, response, uri, null, 0);
        } catch (Error ex) {
            if (D.ON && log.debugable()) log.realCause(ex);
            throw ex;
        } catch (RuntimeException ex) {
            if (D.ON && log.debugable()) log.realCause(ex);
            throw ex;
        }
    }
}
