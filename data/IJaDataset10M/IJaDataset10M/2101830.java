package com.ideo.jso.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.ideo.jso.conf.AbstractConfigurationLoader;
import com.ideo.jso.conf.AbstractGroupBuilder;
import com.ideo.jso.conf.Group;
import com.ideo.jso.util.URLUtils;
import com.ideo.jso.util.Initializer;
import com.ideo.jso.util.ResourcesBuffer;

/**
 * JSO Servlet handling request for resources for a specific timestamp.
 * 
 * @author Julien Maupoux
 *
 */
public class JsoServlet extends HttpServlet {

    private static final long serialVersionUID = 444281561398023573L;

    public static final String FILENAME_PARAM = "JSO_FILENAME";

    public static final String RETENTION_PARAM = "JSO_RETENTION";

    public static final String TIMESTAMP = "timestamp";

    private static final int OUT_BUFFER_SIZE = 1000;

    private ServletConfig servletConfig;

    private static final Logger LOG = Logger.getLogger(JsoServlet.class);

    /**
	 * 
	 */
    public void init() throws ServletException {
        super.init();
    }

    /**
	 * @param config
	 */
    public void init(ServletConfig config) throws ServletException {
        servletConfig = config;
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jso.properties"));
        } catch (IOException e) {
            LOG.error("Could not find a jso.properties file. JsoServlet Initialization failure!", e);
            throw new ServletException(e);
        }
        try {
            Initializer.initialize(properties, null, getRealPath(config));
        } catch (Exception e) {
            LOG.error("JsoServlet Initialisation failure!", e);
            throw new ServletException(e);
        }
    }

    private String getRealPath(ServletConfig config) {
        String realPath = URLUtils.getRealPath(this, config.getServletContext(), "");
        return realPath;
    }

    /**
	 * @return ServletConfig
	 */
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
	 * @param req
	 * @param res
	 */
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String sTimeStamp = (String) req.getParameter(TIMESTAMP);
        long timestamp = (sTimeStamp != null) ? Long.parseLong(sTimeStamp) : 0;
        String fileName = URLUtils.getFileName(request.getPathInfo());
        String extension = URLUtils.getFileExtension(fileName);
        String grpName = URLUtils.getStrictFileName(fileName);
        Map groups = AbstractConfigurationLoader.getInstance().getUpdatedGroups();
        Group group = (Group) groups.get(grpName);
        if (group != null) {
            ByteArrayOutputStream bufferOut = new ByteArrayOutputStream(OUT_BUFFER_SIZE);
            try {
                if (AbstractGroupBuilder.getInstance().buildGroupJsIfNeeded(group, bufferOut, getServletContext())) {
                    group.getJsBuffer().update(bufferOut.toByteArray(), timestamp);
                }
                bufferOut.flush();
            } finally {
                bufferOut.close();
            }
        }
        response.setContentType(URLUtils.getMimeTypeByExtension(extension) + "; CHARSET=" + URLUtils.DEFAULT_ENCODING);
        ResourcesBuffer buffer = null;
        if (extension != null && extension.toLowerCase().equals("js")) buffer = group.getJsBuffer(); else buffer = group.getCssBuffer();
        response.getOutputStream().write(buffer.getData(), 0, buffer.getData().length);
    }

    /**
	 * @return
	 */
    public String getServletInfo() {
        return null;
    }

    /**
	 * 
	 */
    public void destroy() {
    }
}
