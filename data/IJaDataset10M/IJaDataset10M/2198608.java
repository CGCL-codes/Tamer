package com.migniot.streamy.proxy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Delegating servlet context handler, with a workaround for bad jetty CONNECT
 * requests handling.
 */
public class StreamyContextHandler extends ServletContextHandler {

    /**
	 * Handle "443" target, badly parsed by jetty ServletContextHandler by
	 * forcing invocation of underlying
	 * {@link #doHandle(String, Request, HttpServletRequest, HttpServletResponse)}
	 */
    @Override
    public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if ("CONNECT".equals(request.getMethod())) {
            String serverName = request.getServerName();
            String serverPortString = request.getRequestURI();
            int serverPort = Integer.parseInt(serverPortString);
            target = new StringBuilder("/").append(serverName).append(":").append(serverPort).toString();
        }
        super.doScope(target, baseRequest, request, response);
    }
}
