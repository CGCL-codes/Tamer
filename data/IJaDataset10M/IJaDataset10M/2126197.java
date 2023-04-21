package org.jaffa.modules.messaging.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Date;
import org.apache.log4j.Logger;
import org.jaffa.modules.messaging.services.ConfigurationService;
import org.jaffa.modules.messaging.services.configdomain.Config;
import org.jaffa.modules.messaging.services.configdomain.QueueInfo;
import org.jaffa.modules.messaging.services.configdomain.TopicInfo;

/** This is a utility class to generate a JBossMQ destinations-service.xml file listing the required destinations.
 * The following parameters need to be provided before invoking the generate() method
 * <ul>
 *   <li> configurationFileName: the name (with path) of the configuration file (eg. "C:/a-deploy-folder/an-app.sar/resources/jaffa-messaging-config.xml") </li>
 *   <li> outputFileName: the application specific JBossMQ destinations-service.xml file to generate (eg. "C:/a-deploy-folder/an-app.sar/jaffa-messaging-destinations-service.xml") </li>
 *   <li> roles: (optional) a comma-separated list of roles which have access to all the Destinations (eg. "guest") </li>
 * </ul>
 */
public class JBossMQDestinationsGenerator {

    private static final Logger log = Logger.getLogger(JBossMQDestinationsGenerator.class);

    /**
     * Holds value of property configurationFileName.
     */
    private String m_configurationFileName;

    /**
     * Holds value of property outputFileName.
     */
    private String m_outputFileName;

    /**
     * Holds value of property roles.
     */
    private String m_roles;

    /**
     * Getter for property configurationFileName.
     * @return Value of property configurationFileName.
     */
    public String getConfigurationFileName() {
        return m_configurationFileName;
    }

    /**
     * Setter for property configurationFileName.
     * @param configurationFileName New value of property configurationFileName.
     */
    public void setConfigurationFileName(String configurationFileName) {
        m_configurationFileName = configurationFileName;
    }

    /**
     * Getter for property outputFileName.
     * @return Value of property outputFileName.
     */
    public String getOutputFileName() {
        return m_outputFileName;
    }

    /**
     * Setter for property outputFileName.
     * @param outputFileName New value of property outputFileName.
     */
    public void setOutputFileName(String outputFileName) {
        m_outputFileName = outputFileName;
    }

    /**
     * Getter for property roles.
     * @return Value of property roles.
     */
    public String getRoles() {
        return m_roles;
    }

    /**
     * Setter for property roles.
     * @param roles New value of property roles.
     */
    public void setRoles(String roles) {
        m_roles = roles;
    }

    /** Generates a JBossMQ destinations-service.xml file listing the required destinations.
     * @throws Exception if any error occurs.
     */
    public void generate() throws Exception {
        Writer out = null;
        try {
            checkParameters();
            System.setProperty("org.jaffa.modules.messaging.services.ConfigurationService", m_configurationFileName);
            ConfigurationService config = ConfigurationService.getInstance();
            String[] queueNames = config.getQueueNames();
            String[] topicNames = config.getTopicNames();
            if ((queueNames == null || queueNames.length == 0) && (topicNames == null || topicNames.length == 0)) {
                if (log.isInfoEnabled()) log.info("destinations-service.xml not generated since Destination(s) have not been defined in " + m_configurationFileName);
                return;
            }
            out = new BufferedWriter(new FileWriter(m_outputFileName));
            addDestinations(config, out);
        } finally {
            if (out != null) out.close();
        }
    }

    /** Throws IllegalArgumentException if the parameters are not provided.
     * @throws IllegalArgumentException if the parameters are not provided.
     */
    private void checkParameters() throws IllegalArgumentException {
        if (m_configurationFileName == null) throw new IllegalArgumentException("Missing parameter: configurationFileName");
        if (m_outputFileName == null) throw new IllegalArgumentException("Missing parameter: outputFileName");
    }

    /** Add jboss.xml to the jar.
     * @param config the configuration service.
     * @param out the jar file to write to.
     * @throws Exception if any error occurs.
     */
    protected void addDestinations(ConfigurationService config, Writer out) throws Exception {
        StringBuilder secBuf = new StringBuilder();
        if (getRoles() != null && getRoles().length() > 0) {
            secBuf.append("    <depends optional-attribute-name='SecurityManager'>jboss.mq:service=SecurityManager</depends>\n").append("    <attribute name='SecurityConf'>\n").append("      <security>\n");
            for (String role : getRoles().split(",")) secBuf.append("        <role name='").append(role).append("' read='true' write='true' create='true'/>\n");
            secBuf.append("      </security>\n").append("    </attribute>\n");
        }
        StringBuilder mbeanBuf = new StringBuilder();
        String[] queueNames = config.getQueueNames();
        if (queueNames != null) {
            for (String queueName : queueNames) {
                QueueInfo queueInfo = config.getQueueInfo(queueName);
                mbeanBuf.append("  <mbean code='org.jboss.mq.server.jmx.Queue' name='jboss.mq.destination:service=Queue,name=").append(queueInfo.getName()).append("'>\n").append("    <depends optional-attribute-name='DestinationManager'>jboss.mq:service=DestinationManager</depends>\n").append(secBuf.toString()).append("  </mbean>\n");
            }
        }
        String[] topicNames = config.getTopicNames();
        if (topicNames != null) {
            for (String topicName : topicNames) {
                TopicInfo topicInfo = config.getTopicInfo(topicName);
                mbeanBuf.append("  <mbean code='org.jboss.mq.server.jmx.Topic' name='jboss.mq.destination:service=Topic,name=").append(topicInfo.getName()).append("'>\n").append("    <depends optional-attribute-name='DestinationManager'>jboss.mq:service=DestinationManager</depends>\n").append(secBuf.toString()).append("  </mbean>\n");
            }
        }
        StringBuilder buf = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n").append(createManifest()).append("<server>\n").append(mbeanBuf.toString()).append("</server>\n");
        out.write(buf.toString());
    }

    /** Creates a manifest for the JBossMQ destinations-service.xml file.
     * @return the manifest for the JBossMQ destinations-service.xml file.
     */
    protected String createManifest() {
        StringBuilder buf = new StringBuilder().append("<!-- $").append(" Created-By=").append(System.getProperty("java.version")).append(" (").append(System.getProperty("java.vendor")).append(")").append(" Implementation-Title=JaffaComponentsMessaging-Destinations").append(" Implementation-Version=1.0").append(" Built-By=").append(System.getProperty("user.name")).append(" Built-Date=").append(new Date()).append(" $ -->\n");
        return buf.toString();
    }

    /** This will create an instance of the JBossMQDestinationsGenerator and set the various properties using the supplied arguments
     * It will then invoke the generate() method.
     * @param args The following arguments need to be passed: configurationFileName, outputFileName, roles.
     */
    public static void main(String... args) {
        if (args.length < 2) throw new IllegalArgumentException("Usage: JBossMQDestinationsGenerator <configurationFileName> <outputFileName> {<roles>}");
        try {
            JBossMQDestinationsGenerator obj = new JBossMQDestinationsGenerator();
            obj.setConfigurationFileName(args[0]);
            obj.setOutputFileName(args[1]);
            if (args.length > 2) obj.setRoles(args[2]);
            obj.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
