package com.kaixinff.kaixin001.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kaixinff.kaixin001.util.KXClient;
import com.kaixinff.kaixin001.util.KXUtil;
import com.kaixinff.net.HttpClient;

@SuppressWarnings("serial")
public class GetVerifyImageServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpClient client = null;
        String cookieKey = req.getParameter("cookiekey");
        if (cookieKey != null) {
            boolean isNew = "true".equals(req.getParameter("isnew"));
            client = KXClient.getInstance(cookieKey, isNew);
        } else {
            client = KXUtil.client;
        }
        String url = "http://www.kaixin001.com" + req.getRequestURI() + "?norect=" + req.getParameter("norect") + "&randnum=" + req.getParameter("randnum");
        byte[] b = client.doGet(url, KXUtil.REG_URL).getBytes();
        resp.setContentType("image/gif");
        resp.getOutputStream().write(b);
    }
}
