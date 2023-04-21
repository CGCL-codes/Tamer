package org.mobicents.servlet.sip;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import javax.sip.ListeningPoint;
import junit.framework.TestCase;
import org.apache.catalina.connector.Connector;
import org.apache.log4j.Logger;

/**
 * This class is responsible for reading up the properties configuration file
 * and starting/stopping tomcat. It delegates to the test case inheriting from it 
 * the deployment of the context and the location of the dar configuration file
 * since it should map to the test case.
 */
public abstract class SipServletTestCase extends TestCase {

    private static transient Logger logger = Logger.getLogger(SipServletTestCase.class);

    protected String tomcatBasePath;

    protected String projectHome;

    protected SipEmbedded tomcat;

    protected String sipIpAddress;

    protected String serviceFullClassName = "org.mobicents.servlet.sip.catalina.SipStandardService";

    protected String serverName = "SIP-Servlet-Tomcat-Server";

    protected String listeningPointTransport = ListeningPoint.UDP;

    protected boolean createTomcatOnStartup = true;

    protected boolean autoDeployOnStartup = true;

    protected boolean initTomcatOnStartup = true;

    protected boolean startTomcatOnStartup = true;

    protected boolean addSipConnectorOnStartup = true;

    protected Connector sipConnector;

    public SipServletTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (System.getProperty("org.mobicents.testsuite.testhostaddr") == null) {
            System.setProperty("org.mobicents.testsuite.testhostaddr", "127.0.0.1");
        }
        sipIpAddress = "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + "";
        logger.info("sip ip address is " + sipIpAddress);
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/mobicents/servlet/sip/testsuite/testsuite.properties");
        try {
            properties.load(inputStream);
        } catch (NullPointerException e) {
            inputStream = SipServletTestCase.class.getResourceAsStream("org/mobicents/servlet/sip/testsuite/testsuite.properties");
            properties.load(inputStream);
        }
        tomcatBasePath = System.getenv("CATALINA_HOME");
        projectHome = System.getenv("SIP_SERVLETS_HOME");
        if (this.tomcatBasePath == null || this.tomcatBasePath.length() <= 0) this.tomcatBasePath = properties.getProperty("tomcat.home");
        if (this.projectHome == null || this.projectHome.length() <= 0) this.projectHome = properties.getProperty("project.home");
        logger.info("Tomcat base Path is : " + tomcatBasePath);
        logger.info("Project Home is : " + projectHome);
        if (createTomcatOnStartup) {
            createTomcat();
        }
    }

    protected void createTomcat() throws Exception {
        tomcat = new SipEmbedded(serverName, serviceFullClassName);
        tomcat.setLoggingFilePath(projectHome + File.separatorChar + "sip-servlets-test-suite" + File.separatorChar + "testsuite" + File.separatorChar + "src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar);
        logger.info("Log4j path is : " + tomcat.getLoggingFilePath());
        String darConfigurationFile = getDarConfigurationFile();
        tomcat.setDarConfigurationFilePath(darConfigurationFile);
        if (initTomcatOnStartup) {
            Properties sipStackProperties = getSipStackProperties();
            tomcat.initTomcat(tomcatBasePath, sipStackProperties);
            tomcat.addHttpConnector(8080);
            if (addSipConnectorOnStartup) {
                sipConnector = tomcat.addSipConnector(serverName, sipIpAddress, 5070, listeningPointTransport);
            }
        }
        if (startTomcatOnStartup) {
            tomcat.startTomcat();
        }
        if (autoDeployOnStartup) {
            deployApplication();
        }
    }

    protected Properties getSipStackProperties() {
        return null;
    }

    @Override
    protected void tearDown() throws Exception {
        if (createTomcatOnStartup) tomcat.stopTomcat();
        super.tearDown();
    }

    /**
	 * Delegates the choice of the application to deploy to the test case 
	 */
    protected abstract void deployApplication();

    /**
	 * Delegates the choice of the default application router 
	 * configuration file to use to the test case
	 */
    protected abstract String getDarConfigurationFile();
}
