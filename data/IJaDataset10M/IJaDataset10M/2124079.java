package org.jaffa.modules.messaging.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;
import org.jaffa.modules.messaging.services.ConfigurationService;
import org.jaffa.modules.messaging.services.IJmsProviderPlugin;
import org.jaffa.modules.messaging.services.jbossmq.JBossMQPlugin;
import org.jaffa.modules.messaging.services.configdomain.ConsumerPolicy;
import org.jaffa.modules.messaging.services.configdomain.QueueInfo;
import org.jaffa.modules.messaging.services.configdomain.TopicInfo;

/** This is a utility class to generate an EJB module jar, based on a configuration file.
 * The following parameters need to be provided before invoking the generate() method
 * <ul>
 *   <li> configurationFileName: the name (with path) of the configuration file (eg. "C:/a-deploy-folder/an-app.sar/resources/jaffa-messaging-config.xml") </li>
 *   <li> ejbModuleFileName: the EJB Module JAR to generate (eg. "C:/a-deploy-folder/an-app.sar/an-app.ear/jaffa-messaging-ejb.jar") </li>
 *   <li> datasourceLocalJndiName: the local JNDI name used to access the application's DataSource (eg. "jdbc/an-app") </li>
 *   <li> datasourceGlobalJndiName: the global JNDI name used to access the application's DataSource (eg. "java:/an-appDatasource") </li>
 * </ul>
 */
public class EjbModuleGenerator {

    private static final Logger log = Logger.getLogger(EjbModuleGenerator.class);

    /**
     * Holds value of property configurationFileName.
     */
    private String m_configurationFileName;

    /**
     * Holds value of property ejbModuleFileName.
     */
    private String m_ejbModuleFileName;

    /**
     * Holds value of property datasourceLocalJndiName.
     */
    private String m_datasourceLocalJndiName;

    /**
     * Holds value of property datasourceGlobalJndiName.
     */
    private String m_datasourceGlobalJndiName;

    private IJmsProviderPlugin m_plugin = new JBossMQPlugin();

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
     * Getter for property ejbModuleFileName.
     * @return Value of property ejbModuleFileName.
     */
    public String getEjbModuleFileName() {
        return m_ejbModuleFileName;
    }

    /**
     * Setter for property ejbModuleFileName.
     * @param ejbModuleFileName New value of property ejbModuleFileName.
     */
    public void setEjbModuleFileName(String ejbModuleFileName) {
        m_ejbModuleFileName = ejbModuleFileName;
    }

    /**
     * Getter for property datasourceLocalJndiName.
     * @return Value of property datasourceLocalJndiName.
     */
    public String getDatasourceLocalJndiName() {
        return m_datasourceLocalJndiName;
    }

    /**
     * Setter for property datasourceLocalJndiName.
     * @param datasourceLocalJndiName New value of property datasourceLocalJndiName.
     */
    public void setDatasourceLocalJndiName(String datasourceLocalJndiName) {
        m_datasourceLocalJndiName = datasourceLocalJndiName;
    }

    /**
     * Getter for property datasourceGlobalJndiName.
     * @return Value of property datasourceGlobalJndiName.
     */
    public String getDatasourceGlobalJndiName() {
        return m_datasourceGlobalJndiName;
    }

    /**
     * Setter for property datasourceGlobalJndiName.
     * @param datasourceGlobalJndiName New value of property datasourceGlobalJndiName.
     */
    public void setDatasourceGlobalJndiName(String datasourceGlobalJndiName) {
        m_datasourceGlobalJndiName = datasourceGlobalJndiName;
    }

    /** Generates ejb-jar.xml and jboss.xml based on the configuration file.
     * The files are then archived into a file, as specified by the ejbModuleFileName parameter.
     * @throws Exception if any error occurs.
     */
    public void generate() throws Exception {
        JarOutputStream out = null;
        try {
            checkParameters();
            System.setProperty("org.jaffa.modules.messaging.services.ConfigurationService", m_configurationFileName);
            ConfigurationService config = ConfigurationService.getInstance();
            if (!validDestinationDefined(config)) {
                if (log.isInfoEnabled()) log.info("EJB Module JAR not generated since a destination having 'consumerPolicy != none' has not been defined in " + m_configurationFileName);
                return;
            }
            out = new JarOutputStream(new FileOutputStream(m_ejbModuleFileName), createManifest());
            addEjbJarXml(config, out);
            addJbossXml(config, out);
        } finally {
            if (out != null) out.close();
        }
    }

    /** Throws IllegalArgumentException if the parameters are not provided.
     * @throws IllegalArgumentException if the parameters are not provided.
     */
    private void checkParameters() throws IllegalArgumentException {
        if (m_configurationFileName == null) throw new IllegalArgumentException("Missing parameter: configurationFileName");
        if (m_ejbModuleFileName == null) throw new IllegalArgumentException("Missing parameter: ejbModuleFileName");
        if (m_datasourceLocalJndiName == null) throw new IllegalArgumentException("Missing parameter: datasourceLocalJndiName");
        if (m_datasourceGlobalJndiName == null) throw new IllegalArgumentException("Missing parameter: datasourceGlobalJndiName");
    }

    /** Returns true if a destination has been defined that has consumerPolicy != none.
     * @param config the configuration service.
     * @return true if a destination has been defined that has consumerPolicy != none.
     */
    private boolean validDestinationDefined(ConfigurationService config) {
        boolean foundValidDestination = false;
        String[] queueNames = config.getQueueNames();
        if (queueNames != null) {
            for (String queueName : queueNames) {
                QueueInfo queueInfo = config.getQueueInfo(queueName);
                if (queueInfo.getConsumerPolicy() != ConsumerPolicy.NONE) {
                    foundValidDestination = true;
                    break;
                }
            }
        }
        if (!foundValidDestination) {
            String[] topicNames = config.getTopicNames();
            if (topicNames != null) {
                for (String topicName : topicNames) {
                    TopicInfo topicInfo = config.getTopicInfo(topicName);
                    if (topicInfo.getConsumerPolicy() != ConsumerPolicy.NONE) {
                        foundValidDestination = true;
                        break;
                    }
                }
            }
        }
        return foundValidDestination;
    }

    /** Creates a manifest for the EJB Module JAR.
     * @return the manifest.
     * @throws IOException if any I/O error occurs.
     */
    private Manifest createManifest() throws IOException {
        StringBuilder buf = new StringBuilder().append("Manifest-Version: 1.0\n").append("Created-By: ").append(System.getProperty("java.version")).append(" (").append(System.getProperty("java.vendor")).append(")\n").append("Implementation-Title: JaffaComponentsMessaging-Consumers\n").append("Implementation-Version: 1.0\n").append("Built-By: ").append(System.getProperty("user.name")).append("\n").append("Built-Date: ").append(new Date()).append("\n");
        InputStream in = new BufferedInputStream(new ByteArrayInputStream(buf.toString().getBytes()));
        Manifest mf = new Manifest(in);
        return mf;
    }

    /** Add ejb-jar.xml to the jar.
     * @param config the configuration service.
     * @param out the jar file to write to.
     * @throws Exception if any error occurs.
     */
    private void addEjbJarXml(ConfigurationService config, JarOutputStream out) throws Exception {
        StringBuilder mdbBuf = new StringBuilder();
        StringBuilder ctBuf = new StringBuilder();
        StringBuilder mdBuf = new StringBuilder();
        String[] queueNames = config.getQueueNames();
        if (queueNames != null) {
            for (String queueName : queueNames) {
                QueueInfo queueInfo = config.getQueueInfo(queueName);
                if (queueInfo.getConsumerPolicy() != ConsumerPolicy.NONE) {
                    addEjbJarXmlFragments(queueInfo.getName(), m_plugin.toQueueName(queueInfo.getName()), true, mdbBuf, ctBuf, mdBuf);
                }
            }
        }
        String[] topicNames = config.getTopicNames();
        if (topicNames != null) {
            for (String topicName : topicNames) {
                TopicInfo topicInfo = config.getTopicInfo(topicName);
                if (topicInfo.getConsumerPolicy() != ConsumerPolicy.NONE) {
                    addEjbJarXmlFragments(topicInfo.getName(), m_plugin.toTopicName(topicInfo.getName()), false, mdbBuf, ctBuf, mdBuf);
                }
            }
        }
        StringBuilder buf = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n").append("<ejb-jar version='2.1' xmlns='http://java.sun.com/xml/ns/j2ee' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd'>\n").append("  <display-name>JaffaComponentsMessaging-Consumers</display-name>\n").append("  <enterprise-beans>\n").append(mdbBuf.toString()).append("  </enterprise-beans>\n").append("  <assembly-descriptor>\n").append(ctBuf.toString()).append(mdBuf.toString()).append("  </assembly-descriptor>\n").append("</ejb-jar>\n");
        out.putNextEntry(new JarEntry("META-INF/ejb-jar.xml"));
        out.write(buf.toString().getBytes());
        out.closeEntry();
    }

    /** Adds suitable content to the input buffers for generating ejb-jar.xml
     * @param name the destination name.
     * @param mdbBuf buffer to hold information for MDBs.
     * @param ctBuf buffer to hold container-transaction for each MDB.
     * @param mdBuf buffer to hold message-destination for each destination.
     */
    private void addEjbJarXmlFragments(String name, String destinationName, boolean isQueue, StringBuilder mdbBuf, StringBuilder ctBuf, StringBuilder mdBuf) {
        String displayName = name + "MDB";
        String ejbName = name + "MessageBean";
        mdbBuf.append("    <message-driven>\n");
        mdbBuf.append("      <display-name>").append(displayName).append("</display-name>\n");
        mdbBuf.append("      <ejb-name>").append(ejbName).append("</ejb-name>\n");
        mdbBuf.append("      <ejb-class>org.jaffa.modules.messaging.services.JaffaMessageBean</ejb-class>\n");
        mdbBuf.append("      <transaction-type>Bean</transaction-type>\n");
        if (isQueue) mdbBuf.append("      <message-destination-type>javax.jms.Queue</message-destination-type>\n"); else mdbBuf.append("      <message-destination-type>javax.jms.Topic</message-destination-type>\n");
        mdbBuf.append("      <message-destination-link>").append(destinationName).append("</message-destination-link>\n");
        mdbBuf.append("      <activation-config>\n");
        mdbBuf.append("        <activation-config-property>\n");
        mdbBuf.append("          <activation-config-property-name>acknowledgeMode</activation-config-property-name>\n");
        mdbBuf.append("          <activation-config-property-value>Auto-acknowledge</activation-config-property-value>\n");
        mdbBuf.append("        </activation-config-property>\n");
        mdbBuf.append("        <activation-config-property>\n");
        mdbBuf.append("          <activation-config-property-name>destinationType</activation-config-property-name>\n");
        if (isQueue) mdbBuf.append("          <activation-config-property-value>javax.jms.Queue</activation-config-property-value>\n"); else mdbBuf.append("          <activation-config-property-value>javax.jms.Topic</activation-config-property-value>\n");
        mdbBuf.append("        </activation-config-property>\n");
        mdbBuf.append("      </activation-config>\n");
        mdbBuf.append("      <resource-ref>\n");
        mdbBuf.append("        <description>Application DataSource</description>\n");
        mdbBuf.append("        <res-ref-name>").append(m_datasourceLocalJndiName).append("</res-ref-name>\n");
        mdbBuf.append("        <res-type>javax.sql.DataSource</res-type>\n");
        mdbBuf.append("        <res-auth>Container</res-auth>\n");
        mdbBuf.append("      </resource-ref>\n");
        mdbBuf.append("    </message-driven>\n");
        mdBuf.append("    <message-destination>\n");
        mdBuf.append("      <display-name>Destination ").append(destinationName).append("</display-name>\n");
        mdBuf.append("      <message-destination-name>").append(destinationName).append("</message-destination-name>\n");
        mdBuf.append("    </message-destination>\n");
    }

    /** Add jboss.xml to the jar.
     * @param config the configuration service.
     * @param out the jar file to write to.
     * @throws Exception if any error occurs.
     */
    private void addJbossXml(ConfigurationService config, JarOutputStream out) throws Exception {
        StringBuilder mdbBuf = new StringBuilder();
        String[] queueNames = config.getQueueNames();
        if (queueNames != null) {
            for (String queueName : queueNames) {
                QueueInfo queueInfo = config.getQueueInfo(queueName);
                if (queueInfo.getConsumerPolicy() != ConsumerPolicy.NONE) {
                    addJbossXmlFragment(queueInfo.getName(), m_plugin.toQueueName(queueInfo.getName()), mdbBuf, config, queueInfo.getConsumerPolicy() == ConsumerPolicy.SINGLE);
                }
            }
        }
        String[] topicNames = config.getTopicNames();
        if (topicNames != null) {
            for (String topicName : topicNames) {
                TopicInfo topicInfo = config.getTopicInfo(topicName);
                if (topicInfo.getConsumerPolicy() != ConsumerPolicy.NONE) {
                    addJbossXmlFragment(topicInfo.getName(), m_plugin.toTopicName(topicInfo.getName()), mdbBuf, config, topicInfo.getConsumerPolicy() == ConsumerPolicy.SINGLE);
                }
            }
        }
        StringBuilder buf = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n").append("<jboss>\n").append("  <enterprise-beans>\n").append(mdbBuf.toString()).append("  </enterprise-beans>\n").append("</jboss>\n");
        out.putNextEntry(new JarEntry("META-INF/jboss.xml"));
        out.write(buf.toString().getBytes());
        out.closeEntry();
    }

    /** Adds suitable content to the input buffer for generating jboss.xml
     * @param name the destination name.
     * @param mdbBuf buffer to hold information for MDBs.
     * @param config the configuration.
     * @param singleton true if the consumerPolicy is single.
     */
    private void addJbossXmlFragment(String name, String destinationName, StringBuilder mdbBuf, ConfigurationService config, boolean singleton) {
        String ejbName = name + "MessageBean";
        mdbBuf.append("    <message-driven>\n");
        mdbBuf.append("      <ejb-name>").append(ejbName).append("</ejb-name>\n");
        mdbBuf.append("      <destination-jndi-name>").append(destinationName).append("</destination-jndi-name>\n");
        if (config.getJmsConfig() != null && config.getJmsConfig().getUser() != null) {
            mdbBuf.append("      <mdb-user>").append(config.getJmsConfig().getUser()).append("</mdb-user>\n");
            mdbBuf.append("      <mdb-passwd>").append(config.getJmsConfig().getPassword() != null ? config.getJmsConfig().getPassword() : "").append("</mdb-passwd>\n");
        }
        if (singleton) mdbBuf.append("      <configuration-name>Singleton Message Driven Bean</configuration-name>\n");
        mdbBuf.append("      <resource-ref>\n");
        mdbBuf.append("        <res-ref-name>").append(m_datasourceLocalJndiName).append("</res-ref-name>\n");
        mdbBuf.append("        <jndi-name>").append(m_datasourceGlobalJndiName).append("</jndi-name>\n");
        mdbBuf.append("      </resource-ref>\n");
        mdbBuf.append("    </message-driven>\n");
    }

    /** This will create an instance of the EjbModuleGenerator and set the various properties using the supplied arguments
     * It will then invoke the generate() method.
     * @param args The following arguments need to be passed: configurationFileName, ejbModuleFileName, datasourceLocalJndiName, datasourceGlobalJndiName.
     */
    public static void main(String... args) {
        if (args.length < 4) throw new IllegalArgumentException("Usage: EjbModuleGenerator <configurationFileName> <ejbModuleFileName> <datasourceLocalJndiName> <datasourceGlobalJndiName>");
        try {
            EjbModuleGenerator obj = new EjbModuleGenerator();
            obj.setConfigurationFileName(args[0]);
            obj.setEjbModuleFileName(args[1]);
            obj.setDatasourceLocalJndiName(args[2]);
            obj.setDatasourceGlobalJndiName(args[3]);
            obj.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
