package org.apache.jackrabbit.j2ee;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This Class implements a servlet that is used for initializing the log4j
 * facitilies for the containing webapp. since all classes in a webapp share
 * the same Log4J repository, this servlet offers a centralized mechanism to
 * configure the different loggers.
 * <p/>
 * please note, that Log4J holds its logging repository in static class variables,
 * so the configuration spans all classes using the same classloader hierarchy.
 * in a typical j2ee 4 classloaders are provided by the appserver: a shared,
 * a server, a container and an application classloader:
 * <xmp>
 *       Bootstrap
 *          |
 *        System
 *          |
 *        Shared
 *       /      \
 *  Server     Container
 *                /   \
 *           Webapp1  Webapp2 ...
 *
 * </xmp>
 * Classes are searched in the classloader hierarchy by aksing the parent
 * classloader first. so if you put the log4j.jar in the 'shared' classloader,
 * all webapps will shared the same log4j logger repository. if every webapp
 * should use it's own hierarchy, the log4j.jar must be put in the classpath
 * of the webapp classloader, usually WEB-INF/lib.
 * <p/>
 * Please note further that the exact way how the classloader hierarchy is
 * set-up depends on the appserver you are using.
 */
public class LoggingServlet extends HttpServlet {

    /**
     * The init param for the log4j configuration. this can either point to
     * a resource inside the application or a filepath. if the filename ends with
     * ".xml", the {@link DOMConfigurator} is used to configure Log4J, otherwise
     * the {@link PropertyConfigurator} is used.
     */
    public static final String INIT_PARAM_LOG4J_CONFIG = "log4j-config";

    /**
     * {@inheritDoc}
     */
    public void init() throws ServletException {
        configure();
        Logger.getRootLogger().info("Logging initialized.");
    }

    /**
     * Configures Log4J using the config specified by the
     * {@link #INIT_PARAM_LOG4J_CONFIG} init parameter.
     *
     * @throws ServletException
     */
    private void configure() throws ServletException {
        String log4jConfig = getServletConfig().getInitParameter(INIT_PARAM_LOG4J_CONFIG);
        InputStream in = getServletContext().getResourceAsStream(log4jConfig);
        if (in == null) {
            try {
                in = new FileInputStream(log4jConfig);
            } catch (FileNotFoundException e) {
                throw new ServletException("Unable to initialize log4j: " + e.toString());
            }
        } else {
            log4jConfig = "ctx:" + log4jConfig;
        }
        try {
            if (log4jConfig.endsWith(".xml")) {
                InputSource ins = new InputSource(in);
                ins.setSystemId(log4jConfig);
                configureXML(ins);
            } else {
                configureProperties(in);
            }
        } catch (IOException e) {
            throw new ServletException("Unable to initialize log4j: " + e.toString());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Configures Log4J using the {@link DOMConfigurator}
     *
     * @param in
     * @throws ServletException
     * @throws IOException
     */
    private void configureXML(InputSource in) throws ServletException, IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (in.getSystemId().startsWith("ctx:")) {
                builder.setEntityResolver(new ContextResolver());
            }
            Document document = builder.parse(in);
            Element root = document.getDocumentElement();
            DOMConfigurator.configure(root);
        } catch (ParserConfigurationException e) {
            throw new ServletException("Unable to create configuration XML parser", e);
        } catch (SAXException e) {
            throw new ServletException("Configuration file syntax error.", e);
        }
    }

    /**
     * Configures Log4J using the {@link PropertyConfigurator}
     *
     * @param in
     * @throws ServletException
     * @throws IOException
     */
    private void configureProperties(InputStream in) throws ServletException, IOException {
        Properties log4jProperties = new Properties();
        log4jProperties.load(in);
        PropertyConfigurator.configure(log4jProperties);
    }

    /**
     * own EntityResolver to resolve entities inside servlet context
     */
    private class ContextResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            try {
                URI uri = new URI(systemId);
                if (uri.getScheme().equals("ctx")) {
                    InputStream in = getServletContext().getResourceAsStream(uri.getPath());
                    if (in != null) {
                        InputSource ins = new InputSource(in);
                        ins.setSystemId(systemId);
                        return ins;
                    }
                }
                log("Error while resolving entity. Unkwon systemid: " + systemId);
            } catch (URISyntaxException e) {
                log("Error while resolving entity: " + e.toString());
            }
            return null;
        }
    }
}
