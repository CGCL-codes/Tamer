package uy.com.ideasoft.pronaos.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import uy.com.ideasoft.pronaos.AlfrescoUtils;

/**
 * @author martin
 *
 */
public class WebScriptServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(WebScriptServlet.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String ticket = (String) session.getAttribute("alf_ticket");
        if (ticket == null) {
            req.getRequestDispatcher("/autherror.jsp").include(req, resp);
            return;
        } else {
            log.debug("uri: " + req.getRequestURI());
            String params = req.getQueryString();
            String prefix = req.getContextPath() + req.getServletPath();
            String contentPath = req.getRequestURI().substring(prefix.length()) + "?" + params;
            log.debug("content url: " + contentPath);
            String[] contentType = new String[1];
            String url = AlfrescoUtils.appendTicket(contentPath, ticket, true);
            InputStream in = AlfrescoUtils.runWebScript(url);
            if (contentType[0] != null) {
                resp.setContentType(contentType[0]);
            }
            AlfrescoUtils.streamcopy(in, resp.getOutputStream());
            in.close();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            in = AlfrescoUtils.runWebScript(url);
            AlfrescoUtils.streamcopy(in, stream);
            in.close();
            String output = new String(stream.toByteArray());
            log.debug("Response :" + output);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String webScript = req.getParameter("webScript");
        HttpSession session = req.getSession();
        String ticket = (String) session.getAttribute("alf_ticket");
        if (ticket == null) {
            req.getRequestDispatcher("/autherror.jsp").include(req, resp);
            return;
        } else {
            String url = AlfrescoUtils.appendTicket(AlfrescoUtils.BASE_SERVICE_URL + webScript, ticket);
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            Enumeration he = req.getHeaderNames();
            while (he.hasMoreElements()) {
                String headerName = (String) he.nextElement();
                conn.addRequestProperty(headerName, req.getHeader(headerName));
            }
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            AlfrescoUtils.streamcopy(req.getInputStream(), out);
            out.close();
            out = resp.getOutputStream();
            AlfrescoUtils.streamcopy(conn.getInputStream(), out);
            out.flush();
        }
    }
}
