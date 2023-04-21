package uk.org.ogsadai.astro.server;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.resource.ResourceID;

/**
 * RESTful web service that creates a TAP federation. A configuration file is
 * expected at the location <code>WEB-INF/etc/tap-factory.properties</code>.
 * 
 * @author Amy Krause, EPCC, The University of Edinburgh
 */
@Path("")
public class TAPFactory {

    public static final String CONFIG_PATH = "/WEB-INF/etc/";

    public static final String METADOC_FILE = "metadoc.xml";

    public static final String PROPERTIES_FILE = "astrogrid.properties";

    public static final String CONTEXT_FILE = "context.xml";

    private static Properties mConfig;

    private static String mServer;

    private static Server mDQPServer;

    private static String mWAR;

    private static String mDSAUserName;

    private static String mDSAPassword;

    @Context
    ServletContext context;

    private String mDQP_JDBC;

    private String mDQP_JDBC_Parameters;

    @Path("")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String createDistributedTAP(String arguments) throws Exception {
        return createTAP(arguments);
    }

    @Path("")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String newContact(@FormParam("config") String arguments) throws Exception {
        String url = createTAP(arguments);
        return "<html><body><a href=\"" + url + "\">" + url + "</a></body></html>";
    }

    private String createTAP(String arguments) throws Exception {
        if (mConfig == null) {
            loadConfig();
        }
        Properties properties = new Properties();
        properties.load(new StringReader(arguments));
        Set<TAPServerConfig> tapServers = new HashSet<TAPServerConfig>();
        for (String name : properties.stringPropertyNames()) {
            TAPServerConfig config = new TAPServerConfig(name, properties.getProperty(name));
            tapServers.add(config);
        }
        String tapName = "dsa-" + UUID.randomUUID().toString();
        File instanceDir = new File(context.getRealPath("/") + CONFIG_PATH + tapName);
        if (!instanceDir.mkdirs()) {
            System.out.println("Could not create config directory " + instanceDir);
            throw new IOException("Could not create TAP server configuration.");
        }
        System.out.println("Created config dir: " + instanceDir);
        File cacheDir = new File(instanceDir, "cache");
        if (!cacheDir.mkdirs()) {
            System.out.println("Could not create cache directory " + cacheDir);
            throw new IOException("Could not create TAP server configuration.");
        }
        System.out.println("Created cache dir: " + cacheDir);
        ResourceID dqpResource = CreateTAPServerFederation.createDQPResource(mDQPServer, tapServers);
        System.out.println("Created DQP Resource " + dqpResource + " at " + mDQPServer.getDefaultBaseServicesURL());
        String tapURL = mServer + tapName;
        File metadoc = new File(instanceDir, METADOC_FILE);
        DistributedTAPConfiguration.writeMetadoc(tapServers, metadoc);
        File propertiesFile = new File(instanceDir, PROPERTIES_FILE);
        DistributedTAPConfiguration.writeProperties(tapURL, mDQP_JDBC, dqpResource, mDQP_JDBC_Parameters, metadoc.getAbsolutePath(), cacheDir.getAbsolutePath(), new File(instanceDir.getParent(), PROPERTIES_FILE), propertiesFile);
        File contextFile = new File(instanceDir, CONTEXT_FILE);
        DistributedTAPConfiguration.writeContext(new File(instanceDir.getParent(), CONTEXT_FILE), contextFile, propertiesFile.getAbsolutePath());
        System.out.println("Wrote configuration.");
        DeployTAP.deploy(mServer, "/" + tapName, contextFile.toURI().toASCIIString(), mWAR, mDSAUserName, mDSAPassword);
        System.out.println("Deployed TAP endpoint to " + tapURL);
        System.out.println("Initialising job database ...");
        DeployTAP.initialiseJobDatabase(tapURL, mDSAUserName, mDSAPassword);
        return tapURL;
    }

    private void loadConfig() throws IOException {
        String configPath = context.getRealPath("/") + CONFIG_PATH + "tap-factory.xml";
        BeanFactory beanFactory = new XmlBeanFactory(new FileSystemResource(configPath));
        mDSAUserName = (String) beanFactory.getBean("tomcat.dsa.username");
        mDSAPassword = (String) beanFactory.getBean("tomcat.dsa.password");
        mServer = (String) beanFactory.getBean("tap.server.url");
        mDQPServer = (Server) beanFactory.getBean("dqp.server");
        URL baseURL;
        if (beanFactory.containsBean("dqp.server.url")) {
            baseURL = (URL) beanFactory.getBean("dqp.server.url");
            mDQPServer.setDefaultBaseServicesURL(baseURL);
        } else {
            baseURL = mDQPServer.getDefaultBaseServicesURL();
        }
        StringBuilder jdbcURL = new StringBuilder();
        jdbcURL.append("jdbc:ogsadai://");
        jdbcURL.append(baseURL.getHost());
        if (baseURL.getPort() != -1) {
            jdbcURL.append(":").append(baseURL.getPort());
        }
        jdbcURL.append(baseURL.getPath());
        mDQP_JDBC = jdbcURL.toString();
        if (beanFactory.containsBean("dqp.jdbc.parameters")) {
            mDQP_JDBC_Parameters = (String) beanFactory.getBean("dqp.jdbc.parameters");
        }
        System.out.println("Loaded configuration.");
        System.out.println("TAP server: " + mServer);
        System.out.println("DQP server: " + mDQPServer.getDefaultBaseServicesURL());
        System.out.println("JDBC connection: " + mDQP_JDBC);
        System.out.println("JDBC parameters: " + mDQP_JDBC_Parameters);
    }
}
