package flex.messaging.config;

import flex.messaging.LocalizedException;
import flex.messaging.util.Trace;
import flex.messaging.util.ClassUtil;
import javax.servlet.ServletConfig;
import java.io.File;

/**
 * Manages which ConfigurationParser implementation will be
 * used to read in the services configuration file and determines
 * where the configuration file is located.
 * <p>
 * The default location of the configuration file is
 * /WEB-INF/flex/services-config.xml, however this value can
 * be specified in a servlet init-param &quot;services.configuration.file&quot;
 * to the MessageBrokerServlet.
 * </p>
 * <p>
 * The ConfigurationParser implementation can also be specified in
 * a servlet init-param &quot;services.configuration.parser&quot; to
 * the MessageBrokerServlet.
 * </p>
 *
 * @author Peter Farland
 * @see ConfigurationParser
 * @exclude
 */
public class FlexConfigurationManager implements ConfigurationManager {

    static final String DEFAULT_CONFIG_PATH = "/WEB-INF/flex/services-config.xml";

    protected String configurationPath = null;

    protected ConfigurationFileResolver configurationResolver = null;

    protected ConfigurationParser parser = null;

    public MessagingConfiguration getMessagingConfiguration(ServletConfig servletConfig) {
        MessagingConfiguration config = new MessagingConfiguration();
        if (servletConfig != null) {
            String serverInfo = servletConfig.getServletContext().getServerInfo();
            config.getSecuritySettings().setServerInfo(serverInfo);
        }
        verifyMinimumJavaVersion();
        parser = getConfigurationParser(servletConfig);
        if (parser == null) {
            LocalizedException lme = new LocalizedException();
            lme.setMessage(10138);
            throw lme;
        }
        setupConfigurationPathAndResolver(servletConfig);
        parser.parse(configurationPath, configurationResolver, config);
        if (servletConfig != null) {
            config.getSystemSettings().setPaths(servletConfig.getServletContext());
        }
        return config;
    }

    public void reportTokens() {
        parser.reportTokens();
    }

    protected ConfigurationParser getConfigurationParser(ServletConfig servletConfig) {
        ConfigurationParser parser = null;
        Class parserClass = null;
        String className = null;
        if (servletConfig != null) {
            String p = servletConfig.getInitParameter("services.configuration.parser");
            if (p != null) {
                className = p.trim();
                try {
                    parserClass = ClassUtil.createClass(className);
                    parser = (ConfigurationParser) parserClass.newInstance();
                } catch (Throwable t) {
                    if (Trace.config) {
                        Trace.trace("Could not load configuration parser as: " + className);
                    }
                }
            }
        }
        if (parser == null) {
            try {
                ClassUtil.createClass("org.apache.xpath.CachedXPathAPI");
                className = "flex.messaging.config.ApacheXPathServerConfigurationParser";
                parserClass = ClassUtil.createClass(className);
                parser = (ConfigurationParser) parserClass.newInstance();
            } catch (Throwable t) {
                if (Trace.config) {
                    Trace.trace("Could not load configuration parser as: " + className);
                }
            }
        }
        if (parser == null) {
            try {
                className = "flex.messaging.config.XPathServerConfigurationParser";
                parserClass = ClassUtil.createClass(className);
                ClassUtil.createClass("javax.xml.xpath.XPathExpressionException");
                parser = (ConfigurationParser) parserClass.newInstance();
            } catch (Throwable t) {
                if (Trace.config) {
                    Trace.trace("Could not load configuration parser as: " + className);
                }
            }
        }
        if (Trace.config && parser != null) {
            Trace.trace("Services Configuration Parser: " + parser.getClass().getName());
        }
        return parser;
    }

    /**
     * If no entry is specified in web.xml, assumed services-config.xml in the web application
     * If an entry is specified for windows starting with '/', it's assumed to be in the web application.
     * If an entry is specified for windows not starting with '\', it's assumed to be on the local file system.
     * If an entry is specified for non-windows starting with '/', we will first look in the web application
     *  then the the local file system.
     */
    protected void setupConfigurationPathAndResolver(ServletConfig servletConfig) {
        if (servletConfig != null) {
            String p = servletConfig.getInitParameter("services.configuration.file");
            if ((p == null) || (p.trim().length() == 0)) {
                configurationPath = DEFAULT_CONFIG_PATH;
                configurationResolver = new ServletResourceResolver(servletConfig.getServletContext());
            } else {
                configurationPath = p.trim();
                boolean isWindows = File.separator.equals("\\");
                boolean isServletResource = isWindows && configurationPath.startsWith("/");
                if (isServletResource || !isWindows) {
                    ServletResourceResolver resolver = new ServletResourceResolver(servletConfig.getServletContext());
                    boolean available = resolver.isAvailable(configurationPath, isServletResource);
                    if (available) {
                        configurationResolver = (ConfigurationFileResolver) resolver;
                    } else {
                        configurationResolver = new LocalFileResolver(LocalFileResolver.SERVER);
                    }
                } else {
                    configurationResolver = new LocalFileResolver(LocalFileResolver.SERVER);
                }
            }
        } else {
            configurationPath = DEFAULT_CONFIG_PATH;
            configurationResolver = new ServletResourceResolver(servletConfig.getServletContext());
        }
    }

    protected void verifyMinimumJavaVersion() throws ConfigurationException {
        try {
            boolean minimum = false;
            String version = System.getProperty("java.version");
            String vendor = System.getProperty("java.vendor");
            version = version.replace('.', ':');
            version = version.replace('_', ':');
            String[] split = version.split(":");
            int first = Integer.parseInt(split[0]);
            if (first > 1) {
                minimum = true;
            } else if (first == 1) {
                int second = Integer.parseInt(split[1]);
                if (second > 4) {
                    minimum = true;
                } else if (second == 4) {
                    int third = Integer.parseInt(split[2]);
                    if (third > 2) {
                        minimum = true;
                    } else if (third == 2) {
                        if ((vendor != null) && (vendor.indexOf("Sun") != -1)) {
                            int fourth = Integer.parseInt(split[3]);
                            if (fourth >= 6) {
                                minimum = true;
                            }
                        } else {
                            minimum = true;
                        }
                    }
                }
            }
            if (!minimum) {
                ConfigurationException cx = new ConfigurationException();
                if ((vendor != null) && (vendor.indexOf("Sun") != -1)) {
                    cx.setMessage(10139, new Object[] { System.getProperty("java.version") });
                } else {
                    cx.setMessage(10140, new Object[] { System.getProperty("java.version") });
                }
                throw cx;
            }
        } catch (Throwable t) {
            if (t instanceof ConfigurationException) {
                throw ((ConfigurationException) t);
            } else {
                if (Trace.config) {
                    Trace.trace("Could not verified required java version. version=" + System.getProperty("java.version"));
                }
            }
        }
    }
}
