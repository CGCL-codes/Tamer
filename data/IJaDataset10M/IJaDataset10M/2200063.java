package org.riverock.webmill.main;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.riverock.common.html.Header;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.port.PortalInfoImpl;

/**
 * @author Serge Maslyukov
 * Date: Nov 22, 2002
 * Time: 3:13:52 PM
 *
 * $Id: ServletCSS.java,v 1.11 2006/05/02 17:57:07 serg_main Exp $
 */
public final class ServletCSS extends HttpServlet {

    private static final Logger log = Logger.getLogger(ServletCSS.class);

    public ServletCSS() {
    }

    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Writer out = null;
        try {
            response.setContentType("text/css");
            out = response.getWriter();
            PortalInfoImpl p = PortalInfoImpl.getInstance(request.getServerName());
            if (log.isDebugEnabled()) log.debug("Dynamic status " + p.getSite().getCssDynamic());
            if (Boolean.TRUE.equals(p.getSite().getCssDynamic())) {
                if (log.isDebugEnabled()) {
                    log.debug("ID_SITE " + p.getSite().getSiteId());
                    log.debug("p.getDefaultLocale().toString() " + p.getDefaultLocale().toString());
                    log.debug("request parameter " + ContainerConstants.NAME_LANG_PARAM + ": " + request.getParameter(ContainerConstants.NAME_LANG_PARAM));
                    log.debug("Referer: " + Header.getReferer(request));
                }
                Long siteId = p.getSiteId();
                if (log.isDebugEnabled()) {
                    log.debug("siteId: " + siteId);
                }
                ContentCSS css = ContentCSS.getInstance(siteId);
                if (css == null || css.getIsEmpty()) {
                    out.write("<style type=\"text/css\"><!-- --></style>");
                } else {
                    out.write(css.getCss());
                }
                return;
            }
            String cssFile = (p.getSite().getCssFile() != null ? p.getSite().getCssFile() : "//styles.css");
            RequestDispatcher dispatcher = request.getRequestDispatcher(cssFile);
            if (log.isDebugEnabled()) {
                log.debug("forvard to static CSS: " + cssFile);
                log.debug("RequestDispatcher - " + dispatcher);
            }
            if (dispatcher == null) {
                if (log.isDebugEnabled()) {
                    log.debug("RequestDispatcher is null");
                }
                out.write("Error get dispatcher for path " + cssFile);
            } else {
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            final String es = "Error processing ServletCSS";
            log.error(es, e);
            throw new ServletException(es, e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
}
