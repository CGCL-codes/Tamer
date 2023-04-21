package com.slashjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.slashjava.tools.BaseTarget;

/**
 * Servlet implementation class SlashJavaServlet
 */
public class SlashJavaServlet extends HttpServlet {

    private static final String SLASHJAVA_PROPERTIES = "slashjava.properties";

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(SlashJavaServlet.class.getName());

    private static Properties props = new Properties();

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public SlashJavaServlet() {
        super();
        log.info("initializing SlashJavaServlet");
        ClassLoader cl = SlashJavaServlet.class.getClassLoader();
        InputStream propStream = cl.getResourceAsStream(SLASHJAVA_PROPERTIES);
        if (propStream != null) {
            try {
                props.load(propStream);
            } catch (IOException e) {
                log.warn(e.toString(), e);
            } finally {
                try {
                    propStream.close();
                } catch (IOException e) {
                    log.warn(e.toString(), e);
                }
            }
        }
    }

    /**
	 * @see Servlet#init(ServletConfig)
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.debug("servlet init");
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork(request, response);
    }

    private void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();
        OutputStream out = response.getOutputStream();
        log.debug("path info: " + request.getPathInfo());
        log.debug("context path: " + request.getContextPath());
        String path = request.getServletPath();
        String className = path.substring(path.lastIndexOf("/") + 1, path.length());
        className = className.substring(0, className.indexOf(".java"));
        log.debug("servlet path: " + path);
        log.debug("query: " + request.getQueryString());
        try {
            Class c = Class.forName(className);
            Object obj = createNewInstance(className, request, response);
            Class[] parameterTypes = { Map.class, OutputStream.class };
            Method m = c.getDeclaredMethod("dispatch", parameterTypes);
            Object[] args = { params, out };
            Object contentType = m.invoke(obj, args);
            Class[] nullParams = new Class[0];
            m = c.getMethod("destroy", nullParams);
            Object[] nullArgs = new Object[0];
            m.invoke(obj, nullArgs);
        } catch (Exception e) {
            log.error(e.toString(), e);
            Object value = props.get("THROW_EXCEPTION");
            if (value != null) {
                if (Boolean.parseBoolean((String) value) == true) {
                    throw new ServletException(e);
                }
            }
        }
        out.close();
    }

    private Object createNewInstance(String className, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object obj = null;
        Class c;
        try {
            c = Class.forName(className);
            if (BaseTarget.class.isAssignableFrom(c)) {
                Object[] constructorParams = { request, response };
                Object[] nullParams = new Object[0];
                Class[] constructorArgs = { HttpServletRequest.class, HttpServletResponse.class };
                Class[] nullConstructorArgs = new Class[0];
                Constructor constructor = null;
                try {
                    constructor = c.getDeclaredConstructor(constructorArgs);
                    obj = constructor.newInstance(constructorParams);
                } catch (NoSuchMethodException e) {
                }
                if (constructor == null) {
                    try {
                        constructor = c.getDeclaredConstructor(nullConstructorArgs);
                        obj = constructor.newInstance(nullParams);
                    } catch (NoSuchMethodException e) {
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return obj;
    }
}
