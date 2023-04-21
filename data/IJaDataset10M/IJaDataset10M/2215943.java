package ee.webAppToolkit.core.expert.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.annotations.Path;
import ee.webAppToolkit.core.exceptions.ConfigurationException;
import ee.webAppToolkit.core.exceptions.HttpException;
import ee.webAppToolkit.core.exceptions.RedirectException;
import ee.webAppToolkit.core.expert.RequestHandler;
import ee.webAppToolkit.core.expert.ThreadLocalProvider;

@Singleton
public class WebAppToolkitServlet extends HttpServlet {

    @Inject
    private Logger _logger;

    private static final long serialVersionUID = 1L;

    private RequestHandler _requestHandler;

    private ThreadLocalProvider<String> _pathProvider;

    private ThreadLocalProvider<String> _contextProvider;

    @Inject
    public WebAppToolkitServlet(RequestHandler requestHandler, @Path ThreadLocalProvider<String> pathProvider, @Context ThreadLocalProvider<String> contextProvider) {
        _requestHandler = requestHandler;
        _pathProvider = pathProvider;
        _contextProvider = contextProvider;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            _requestHandler.init(config.getServletContext().getContextPath());
        } catch (ConfigurationException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String contextPath = httpServletRequest.getContextPath();
        String pathInfo = httpServletRequest.getPathInfo();
        _contextProvider.set(contextPath);
        _pathProvider.set(contextPath + pathInfo);
        try {
            Result result = _requestHandler.handleRequest();
            if (result != null) {
                String characterEncoding = result.getCharacterEncoding();
                String contentType = result.getContentType();
                _logger.info("Writing result to output stream, character encoding '" + characterEncoding + "', content type '" + contentType + "'");
                for (Entry<String, String> entry : result.getHeaders().entrySet()) {
                    httpServletResponse.addHeader(entry.getKey(), entry.getValue());
                }
                httpServletResponse.setCharacterEncoding(characterEncoding);
                httpServletResponse.setContentType(contentType);
                httpServletResponse.getOutputStream().write(result.getBytes());
                httpServletResponse.getOutputStream().flush();
                while (!httpServletResponse.isCommitted()) {
                    System.out.println("waiting");
                }
            }
        } catch (RedirectException e) {
            String location = e.getLocation();
            _logger.info("Redirecting to location '" + location + "'");
            httpServletResponse.sendRedirect(location);
        } catch (HttpException e) {
            int statusCode = e.getStatusCode();
            String message = e.getMessage();
            if (statusCode == 500) {
                _logger.info("Error processing request, message '" + message + "'");
                StringWriter s = new StringWriter();
                e.printStackTrace(new PrintWriter(s));
                httpServletResponse.sendError(statusCode, s.toString());
                _logger.info("StackTrace: " + s.toString());
            } else {
                httpServletResponse.sendError(statusCode, message);
            }
        }
    }
}
