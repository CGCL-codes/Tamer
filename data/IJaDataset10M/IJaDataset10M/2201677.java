package com.liferay.portlet.httpbridge.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href="HttpService.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class HttpService extends com.httpbridge.webproxy.ui.HttpService {

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpBridgeServletResponse httpBridgeRes = new HttpBridgeServletResponse(res);
        super.service(req, httpBridgeRes);
    }
}
