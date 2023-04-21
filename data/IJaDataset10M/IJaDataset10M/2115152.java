package com.liferay.portal.wsrp.servlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wsrp4j.producer.util.Base64;
import org.apache.wsrp4j.producer.util.ObjectDeserializer;

/**
 * <a href="ResourceProxyServlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael Young
 *
 */
public class ResourceProxyServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) {
        String targetUrl = req.getParameter("url");
        String cookie = req.getParameter("cookie");
        if (targetUrl != null) {
            try {
                targetUrl = ObjectDeserializer.deserializeString(Base64.decode(targetUrl));
                URL url = new URL(targetUrl);
                URLConnection con = url.openConnection();
                cookie = ObjectDeserializer.deserializeString(Base64.decode(cookie));
                con.setRequestProperty("Cookie", cookie);
                con.connect();
                res.setContentType(con.getContentType());
                res.setContentLength(con.getContentLength());
                InputStream in = con.getInputStream();
                OutputStream out = res.getOutputStream();
                int next;
                while ((next = in.read()) != -1) {
                    out.write(next);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                _log.warn(e);
            }
        }
    }

    private static Log _log = LogFactory.getLog(ResourceProxyServlet.class);
}
