package org.tolven.assembler.jboss;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.util.Properties;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.Extension.Parameter;
import org.tolven.plugin.TolvenCommandPlugin;
import org.tolven.security.auth.PasswordStoreImpl;
import org.tolven.tools.ant.TolvenCopy;
import org.tolven.tools.ant.TolvenJar;

/**
 * This plugin assembles all of the tolven specific configuration files for the JBoss appserver
 * 
 * @author Joseph Isaac
 *
 */
public class JBossAssembler extends TolvenCommandPlugin {

    public static final String CONFDIR = "server/tolven/conf";

    public static final String LIBDIR = "server/tolven/lib";

    public static final String MESSAGE_DIGEST_ALGORITHM = "md5";

    public static final String EXTENSIONPOINT_DB_PLUGIN_COMPONENT = "databasePlugin";

    public static final String EXTENSIONPOINT_LIBJAR_COMPONENT = "libJar";

    public static final String EXTENSIONPOINT_JBOSS_COMPONENT = "jbossComponent";

    public static final String EXTENSIONPOINT_TOLVENDS_PROVIDER = "tolvenDSProvider";

    public static final String EXTENSIONPOINT_TOLVENJMS_PROVIDER = "tolvenJMSProvider";

    public static final String EXTENSIONPOINT_TOLVENLDAP_PRODUCT_DEF = "tolvenLDAPProductDefinition";

    public static final String EXTENSIONPOINT_SERVERSECURITY_PRODUCT = "serverSecurityProduct";

    public static final String EXTENSIONPOINT_SERVERSECURITY_PRODUCT_DEF = "serverSecurityProductDefinition";

    public static final String ATTRIBUTE_TEMPLATE_LOGIN_CONFIG = "template-login-config";

    public static final String EXTENSIONPOINT_USER_LOGIN_CONTEXT = "userLoginContext";

    public static final String EXTENSION_LDAPSOURCE = "ldapSource";

    public static final String EXTENSIONPOINT_DEPLOY = "deploy";

    public static final String EXTENSIONPOINT_LIB_CLASSES = "classes";

    private Logger logger = Logger.getLogger(JBossAssembler.class);

    @Override
    protected void doStart() throws Exception {
        logger.debug("*** start ***");
    }

    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        File[] tmpFiles = getPluginTmpDir().listFiles();
        if (tmpFiles != null && tmpFiles.length > 0) {
            return;
        }
        executeRequiredPlugins(args);
        collectTolvenDSProduct();
        assembleTolvenLDAPProduct();
        collectTolvenJMSProduct();
        assembleServerSecurityProduct();
        assembleLoginConfigProduct();
        assembleLibClasses();
        assembleLibJars();
        copyToStageDir();
    }

    protected void executeRequiredPlugins(String[] args) throws Exception {
        ExtensionPoint dbPluginExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_DB_PLUGIN_COMPONENT);
        Extension dbPluginExtension = getSingleConnectedExtension(dbPluginExtensionPoint);
        String dbPluginDescriptor = dbPluginExtension.getDeclaringPluginDescriptor().getId();
        execute(dbPluginDescriptor, args);
        execute("org.tolven.assembler.ear", args);
        execute("org.tolven.assembler.jboss.tomcatserver", args);
    }

    protected void collectTolvenDSProduct() throws IOException {
        ExtensionPoint jBossComponentExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_JBOSS_COMPONENT);
        for (Extension jBossComponentExtension : jBossComponentExtensionPoint.getConnectedExtensions()) {
            String destinationTolvenDSFilename = getDescriptor().getAttribute("tolvenDS").getValue();
            PluginDescriptor jBossComponentPluginDescriptor = jBossComponentExtension.getDeclaringPluginDescriptor();
            File destinationTolvenDSFile = new File(getPluginTmpDir(jBossComponentPluginDescriptor), destinationTolvenDSFilename);
            ExtensionPoint tolvenDSProviderExtensionPoint = jBossComponentPluginDescriptor.getExtensionPoint(EXTENSIONPOINT_TOLVENDS_PROVIDER);
            if (tolvenDSProviderExtensionPoint == null) {
                throw new RuntimeException("ExtensionPoint '" + EXTENSIONPOINT_TOLVENDS_PROVIDER + "' not found in " + jBossComponentPluginDescriptor.getId());
            }
            for (Extension tolvenDSProviderExtension : tolvenDSProviderExtensionPoint.getConnectedExtensions()) {
                String sourceTolvenDS = tolvenDSProviderExtension.getParameter("tolvenDS").valueAsString();
                File sourceTolvenDSPluginTmpDir = getPluginTmpDir(tolvenDSProviderExtension.getDeclaringPluginDescriptor());
                File sourceTolvenDSFile = new File(sourceTolvenDSPluginTmpDir, sourceTolvenDS);
                if (!destinationTolvenDSFile.exists() || sourceTolvenDSFile.lastModified() > destinationTolvenDSFile.lastModified()) {
                    logger.debug(destinationTolvenDSFile.getPath() + " was replaced since its source files are more recent");
                    logger.debug("Copy " + sourceTolvenDSFile.getPath() + " to " + destinationTolvenDSFile);
                    FileUtils.copyFile(sourceTolvenDSFile, destinationTolvenDSFile);
                } else {
                    logger.debug(destinationTolvenDSFile.getPath() + " is more recent than any of its source file: " + sourceTolvenDSFile.getPath());
                }
            }
        }
    }

    protected void assembleTolvenLDAPProduct() {
        ExtensionPoint productDefExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_TOLVENLDAP_PRODUCT_DEF);
        for (Extension productDefExtension : productDefExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor productDefPluginDescriptor = productDefExtension.getDeclaringPluginDescriptor();
            String connectionString = productDefExtension.getParameter("connectionString").valueAsString();
            String sourceTolvenLDAP = productDefExtension.getParameter("sourceTolvenLDAP").valueAsString();
            File sourceTolvenLDAPFile = getFilePath(productDefPluginDescriptor, sourceTolvenLDAP);
            File productDefPluginDataDir = getPluginTmpDir(productDefPluginDescriptor);
            String destinationTolvenLDAP = productDefExtension.getParameter("destinationTolvenLDAP").valueAsString();
            File destinationTolvenLDAPFile = new File(productDefPluginDataDir, destinationTolvenLDAP);
            ExtensionPoint ldapSourceExtensionPoint = getDescriptor().getExtensionPoint(EXTENSION_LDAPSOURCE);
            ExtensionPoint parentLDAPSourceExtensionPoint = getParentExtensionPoint(ldapSourceExtensionPoint);
            PluginDescriptor parentLDAPDescriptor = parentLDAPSourceExtensionPoint.getDeclaringPluginDescriptor();
            String ldapProtocol = (String) evaluate(parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.protocol").getDefaultValue(), parentLDAPDescriptor);
            String ldapHostname = (String) evaluate(parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.hostname").getDefaultValue(), parentLDAPDescriptor);
            String ldapPort = (String) evaluate(parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.port").getDefaultValue(), parentLDAPDescriptor);
            String connectionStringValue = ldapProtocol + "://" + ldapHostname + ":" + ldapPort;
            Properties properties = new Properties();
            properties.setProperty(connectionString, connectionStringValue);
            properties.setProperty("org.tolven.ldap.rootDN", getTolvenConfigWrapper().getLDAPServerRootUser());
            properties.setProperty("org.tolven.ldap.rootDNPasswordAlias", getTolvenConfigWrapper().getLDAPServerRootPasswordId());
            logger.debug("Copy " + sourceTolvenLDAPFile.getPath() + " to " + destinationTolvenLDAPFile);
            TolvenCopy.copyFile(sourceTolvenLDAPFile, destinationTolvenLDAPFile, properties);
        }
    }

    protected void collectTolvenJMSProduct() throws IOException {
        ExtensionPoint jBossComponentExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_JBOSS_COMPONENT);
        for (Extension jBossComponentExtension : jBossComponentExtensionPoint.getConnectedExtensions()) {
            String destinationTolvenJMSFilename = getDescriptor().getAttribute("tolvenJMS").getValue();
            PluginDescriptor jBossComponentPluginDescriptor = jBossComponentExtension.getDeclaringPluginDescriptor();
            File destinationTolvenJMSFile = new File(getPluginTmpDir(jBossComponentPluginDescriptor), destinationTolvenJMSFilename);
            ExtensionPoint tolvenJMSProviderExtensionPoint = jBossComponentPluginDescriptor.getExtensionPoint(EXTENSIONPOINT_TOLVENJMS_PROVIDER);
            if (tolvenJMSProviderExtensionPoint == null) {
                throw new RuntimeException("ExtensionPoint '" + EXTENSIONPOINT_TOLVENJMS_PROVIDER + "' not found in " + jBossComponentPluginDescriptor.getId());
            }
            for (Extension tolvenJMSProviderExtension : tolvenJMSProviderExtensionPoint.getConnectedExtensions()) {
                String sourceTolvenJMS = tolvenJMSProviderExtension.getParameter("tolvenJMS").valueAsString();
                File sourceTolvenJMSPluginTmpDir = getPluginTmpDir(tolvenJMSProviderExtension.getDeclaringPluginDescriptor());
                File sourceTolvenJMSFile = new File(sourceTolvenJMSPluginTmpDir, sourceTolvenJMS);
                if (!destinationTolvenJMSFile.exists() || sourceTolvenJMSFile.lastModified() > destinationTolvenJMSFile.lastModified()) {
                    logger.debug(destinationTolvenJMSFile.getPath() + " was replaced since its source files are more recent");
                    logger.debug("Copy " + sourceTolvenJMSFile.getPath() + " to " + destinationTolvenJMSFile);
                    FileUtils.copyFile(sourceTolvenJMSFile, destinationTolvenJMSFile);
                } else {
                    logger.debug(destinationTolvenJMSFile.getPath() + " is more recent than any of its source file: " + sourceTolvenJMSFile.getPath());
                }
            }
        }
    }

    protected void assembleServerSecurityProduct() throws IOException {
        ExtensionPoint productDefExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_SERVERSECURITY_PRODUCT_DEF);
        for (Extension productDefExtension : productDefExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor productDefPluginDescriptor = productDefExtension.getDeclaringPluginDescriptor();
            String sourceTolvenInit = productDefExtension.getParameter("sourceTolvenInit").valueAsString();
            File sourceTolvenInitFile = getFilePath(productDefPluginDescriptor, sourceTolvenInit);
            File productDefPluginDataDir = getPluginTmpDir(productDefPluginDescriptor);
            String destinationTolvenInit = productDefExtension.getParameter("destinationTolvenInit").valueAsString();
            File destinationTolvenInitFile = new File(productDefPluginDataDir, destinationTolvenInit);
            String credentialDirname = productDefExtension.getParameter("credentialDir").valueAsString();
            String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
            File appserverHomeDir = new File(appserverHome);
            File deployCredentialDir = new File(appserverHomeDir, credentialDirname);
            String productExtensionPointId = productDefExtension.getParameter("extensionPointId").valueAsString();
            ExtensionPoint productExtensionPoint = productDefPluginDescriptor.getExtensionPoint(productExtensionPointId);
            if (productExtensionPoint == null) {
                throw new RuntimeException("ExtensionPoint '" + productExtensionPointId + "' not found in " + productDefPluginDescriptor.getId());
            }
            Properties properties = new Properties();
            String appServerCredentialDirname = getTolvenConfigWrapper().getPasswordServer().getCredentialDir();
            File appServerCredentialDir = new File(appServerCredentialDirname);
            File passwordStoreFile = generatePasswordStoreFile(appServerCredentialDir);
            properties.setProperty("tolvendev-tolven-passwordStore-property", new File(deployCredentialDir, passwordStoreFile.getName()).toURI().toURL().toExternalForm());
            String passwordStoreCredentialGroupId = getTolvenConfigWrapper().getPasswordServer().getId();
            File passwordKeyStoreFile = getTolvenConfigWrapper().getKeyStoreFile(passwordStoreCredentialGroupId);
            properties.setProperty("tolvendev-tolven-keystore-property", new File(deployCredentialDir, passwordKeyStoreFile.getName()).toURI().toURL().toExternalForm());
            properties.setProperty("tolvendev-tolven-keystore", passwordKeyStoreFile.getName());
            String passwordKeyStoreType = getTolvenConfigWrapper().getKeyStoreType(passwordStoreCredentialGroupId);
            properties.setProperty("tolvendev-tolven-keystore-type-property", passwordKeyStoreType);
            properties.setProperty("tolvendev-tolven-keystore-type", passwordKeyStoreType);
            String appServerCredentialGroupId = getTolvenConfigWrapper().getAppServer().getId();
            File sourceAppServerSSLKeyStoreFile = getTolvenConfigWrapper().getKeyStoreFile(appServerCredentialGroupId);
            properties.setProperty("javax-net-ssl-keyStorePasswordId", appServerCredentialGroupId);
            properties.setProperty("javax-net-ssl-keyStore", new File(deployCredentialDir, sourceAppServerSSLKeyStoreFile.getName()).getPath().replace("\\", "/"));
            String appServerSSLKeyStoreType = getTolvenConfigWrapper().getKeyStoreType(appServerCredentialGroupId);
            properties.setProperty("javax-net-ssl-keyStoreType", appServerSSLKeyStoreType);
            File sourceAppServerSSLTrustStoreFile = getTolvenConfigWrapper().getTrustStoreFile(appServerCredentialGroupId);
            properties.setProperty("javax-net-ssl-trustStore", new File(deployCredentialDir, sourceAppServerSSLTrustStoreFile.getName()).getPath().replace("\\", "/"));
            String appServerSSLTrustStoreType = getTolvenConfigWrapper().getTrustStoreType(appServerCredentialGroupId);
            properties.setProperty("javax-net-ssl-trustStoreType", appServerSSLTrustStoreType);
            ExtensionPoint serverSecurityExtensionPoint = getMyExtensionPoint(EXTENSIONPOINT_SERVERSECURITY_PRODUCT);
            String promptForPassword = null;
            String passwordPrompt = null;
            for (Extension productExtension : productExtensionPoint.getConnectedExtensions()) {
                promptForPassword = (String) evaluate(productExtension.getParameter("promptForPassword").valueAsString(), productExtension.getDeclaringPluginDescriptor());
                logger.debug("Prompt for password from: " + productExtension.getDeclaringPluginDescriptor() + " is: " + promptForPassword);
                if (productExtension.getParameter("passwordPrompt") != null) {
                    passwordPrompt = productExtension.getParameter("passwordPrompt").valueAsString();
                    if (passwordPrompt != null) {
                        logger.debug("Password prompt from: " + productExtension.getUniqueId() + " is: " + passwordPrompt);
                    }
                }
                break;
            }
            if (promptForPassword == null) {
                promptForPassword = (String) evaluate(serverSecurityExtensionPoint.getParameterDefinition("promptForPassword").getDefaultValue());
                if (promptForPassword != null) {
                    logger.debug("Prompt for password from: " + serverSecurityExtensionPoint.getUniqueId() + " is: " + promptForPassword);
                }
                passwordPrompt = (String) evaluate(serverSecurityExtensionPoint.getParameterDefinition("passwordPrompt").getDefaultValue());
                if (passwordPrompt != null) {
                    logger.debug("Password prompt from: " + serverSecurityExtensionPoint.getUniqueId() + " is: " + passwordPrompt);
                }
            }
            if (passwordPrompt == null) {
                passwordPrompt = getDescriptor().getAttribute("default-passwordPrompt").getValue();
                logger.debug("Using default password prompt");
            }
            if ("false".equals(promptForPassword)) {
                String passwordServerCredentialGroupId = getTolvenConfigWrapper().getPasswordServerId();
                char[] password = getPassword(passwordServerCredentialGroupId);
                if (password == null) {
                    throw new RuntimeException("No passwordPrompt is requested, and yet no password is associated with credential group: " + passwordServerCredentialGroupId);
                }
                properties.setProperty("password-store-entry-prompt", passwordPrompt + ":" + new String(password));
            } else {
                properties.setProperty("password-store-entry-prompt", passwordPrompt);
            }
            logger.debug("Copy " + sourceTolvenInitFile.getPath() + " to " + destinationTolvenInitFile);
            TolvenCopy.copyFile(sourceTolvenInitFile, destinationTolvenInitFile, properties);
        }
    }

    private File generatePasswordStoreFile(File localCredentialDir) throws IOException {
        String groupId = getTolvenConfigWrapper().getPasswordServerId();
        if (groupId == null) {
            throw new RuntimeException("The password server groupId is null");
        }
        char[] passwordStorePassword = getPassword(groupId);
        if (passwordStorePassword == null) {
            throw new RuntimeException("Password for password server group Id " + groupId + " is null");
        }
        KeyStore keyStore = getTolvenConfigWrapper().getKeyStore(groupId, passwordStorePassword);
        if (keyStore == null) {
            throw new RuntimeException("Could not find password server keystore with group Id: " + groupId);
        }
        PasswordStoreImpl passwordStore = new PasswordStoreImpl(keyStore, passwordStorePassword);
        String appserverId = getTolvenConfigWrapper().getAppServerId();
        if (appserverId == null) {
            throw new RuntimeException("The appserver Id is null for group Id: " + groupId);
        }
        char[] appserverIdPassword = getPassword(appserverId);
        if (appserverIdPassword == null) {
            throw new RuntimeException("The password is null when retrieved with appserver password Id: '" + appserverId + "'");
        }
        passwordStore.setPassword(appserverId, appserverIdPassword);
        String ldapRootPasswordId = getTolvenConfigWrapper().getLDAPServerRootPasswordId();
        if (ldapRootPasswordId == null) {
            throw new RuntimeException("The ldap root password Id is null");
        }
        char[] ldapRootPassword = getPassword(ldapRootPasswordId);
        if (ldapRootPassword == null) {
            throw new RuntimeException("The ldap root password is null when retrieved with password Id: '" + ldapRootPasswordId + "'");
        }
        passwordStore.setPassword(ldapRootPasswordId, ldapRootPassword);
        String dbRootPasswordId = getTolvenConfigWrapper().getDBServerRootPasswordId();
        if (dbRootPasswordId == null) {
            throw new RuntimeException("The db root password Id is null");
        }
        char[] dbRootPassword = getPassword(dbRootPasswordId);
        if (dbRootPassword == null) {
            throw new RuntimeException("The db root password is null when retrieved with password Id: '" + dbRootPasswordId + "'");
        }
        passwordStore.setPassword(dbRootPasswordId, dbRootPassword);
        String mdbuserPasswordId = getTolvenConfigWrapper().getMDBUserId();
        if (mdbuserPasswordId == null) {
            throw new RuntimeException("The mdbuser Id is null");
        }
        char[] mdbuserPassword = getPassword(mdbuserPasswordId);
        if (mdbuserPassword == null) {
            throw new RuntimeException("The password is null when retrieved with mdbuser password Id: '" + mdbuserPasswordId + "'");
        }
        passwordStore.setPassword(mdbuserPasswordId, mdbuserPassword);
        File passwordStoreFile = new File(localCredentialDir, "passwordStore.properties");
        FileOutputStream out = null;
        Properties encryptedPasswords = passwordStore.getEncryptedPasswords();
        try {
            passwordStoreFile.getParentFile().mkdirs();
            out = new FileOutputStream(passwordStoreFile);
            logger.debug("Store encrypted passwords in " + passwordStoreFile.getPath());
            encryptedPasswords.store(out, null);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return passwordStoreFile;
    }

    protected void assembleLoginConfigProduct() {
        String templateFilename = getDescriptor().getAttribute(ATTRIBUTE_TEMPLATE_LOGIN_CONFIG).getValue();
        File templateFile = getFilePath(templateFilename);
        StringBuffer originalXML = new StringBuffer();
        try {
            originalXML.append(FileUtils.readFileToString(templateFile));
        } catch (IOException ex) {
            throw new RuntimeException("Could not read the login-config template file: " + templateFile.getPath(), ex);
        }
        String xslt = null;
        try {
            xslt = getXSLT();
        } catch (XMLStreamException ex) {
            throw new RuntimeException("Could not generate the XSLT for login-config the file: " + templateFile.getPath(), ex);
        }
        File xsltFile = new File(getPluginTmpDir(), "login-config-xslt.xml");
        logger.debug("Write xslt file " + xsltFile.getPath());
        try {
            FileUtils.writeStringToFile(xsltFile, xslt);
        } catch (IOException ex) {
            throw new RuntimeException("Could not write the login-config xslt file: " + xsltFile.getPath() + " as\n" + xslt, ex);
        }
        String translatedXMLString = getTranslatedXML(originalXML.toString(), xslt);
        String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
        File appserverHomeDir = new File(appserverHome);
        File stageDirAppserverHomeDir = new File(getStageDir(), appserverHomeDir.getName());
        File stageAppserverConfDir = new File(stageDirAppserverHomeDir, CONFDIR);
        File destinationXMLFile = new File(stageAppserverConfDir, "login-config.xml");
        destinationXMLFile.getParentFile().mkdirs();
        logger.debug("Write translated server.xml file to " + destinationXMLFile);
        try {
            FileUtils.writeStringToFile(destinationXMLFile, translatedXMLString);
        } catch (IOException ex) {
            throw new RuntimeException("Could not write the login-config to file: " + destinationXMLFile.getPath() + " as\n" + translatedXMLString, ex);
        }
    }

    protected String getXSLT() throws XMLStreamException {
        StringWriter xslt = new StringWriter();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = null;
        try {
            xmlStreamWriter = factory.createXMLStreamWriter(xslt);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement("xsl:stylesheet");
            xmlStreamWriter.writeAttribute("version", "2.0");
            xmlStreamWriter.writeNamespace("xsl", "http://www.w3.org/1999/XSL/Transform");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement("xsl:output");
            xmlStreamWriter.writeAttribute("method", "xml");
            xmlStreamWriter.writeAttribute("indent", "yes");
            xmlStreamWriter.writeAttribute("encoding", "UTF-8");
            xmlStreamWriter.writeAttribute("omit-xml-declaration", "no");
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeCharacters("\n");
            addMainTemplate(xmlStreamWriter);
            addPolicyTemplate(xmlStreamWriter);
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.writeEndDocument();
        } finally {
            if (xmlStreamWriter != null) {
                xmlStreamWriter.close();
            }
        }
        return xslt.toString();
    }

    protected void addMainTemplate(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("xsl:template");
        xmlStreamWriter.writeAttribute("match", "/ | * | @* | text() | comment()");
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:copy");
        xmlStreamWriter.writeAttribute("select", ".");
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:apply-templates");
        xmlStreamWriter.writeAttribute("select", "* | @* | text() | comment()");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
    }

    protected void addPolicyTemplate(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("xsl:template");
        xmlStreamWriter.writeAttribute("match", "policy");
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:element");
        xmlStreamWriter.writeAttribute("name", "{name()}");
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:for-each");
        xmlStreamWriter.writeAttribute("select", "@*");
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:attribute");
        xmlStreamWriter.writeAttribute("name", "{name()}");
        xmlStreamWriter.writeStartElement("xsl:value-of");
        xmlStreamWriter.writeAttribute("select", ".");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:value-of");
        xmlStreamWriter.writeAttribute("select", "text()");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        addDefaultDBLoginContext(xmlStreamWriter);
        addDefaultRuleQueueLoginContext(xmlStreamWriter);
        ExtensionPoint userLoginContextExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_USER_LOGIN_CONTEXT);
        int nConnectedExtensions = userLoginContextExtensionPoint.getConnectedExtensions().size();
        if (nConnectedExtensions > 1) {
            throw new RuntimeException("Only one or no extensions are allowed to be connected to: " + userLoginContextExtensionPoint.getUniqueId());
        }
        if (nConnectedExtensions == 0) {
            addDefaultUserLoginContext(xmlStreamWriter);
        } else {
            Extension userLoginContextExtension = getSingleConnectedExtension(userLoginContextExtensionPoint);
            addCustomUserLoginContext(userLoginContextExtension, xmlStreamWriter);
        }
        addDefaultJmsXARealmLoginContext(xmlStreamWriter);
        addDefaultJMXConsoleLoginContext(xmlStreamWriter);
        xmlStreamWriter.writeStartElement("xsl:copy-of");
        xmlStreamWriter.writeAttribute("select", "*");
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeStartElement("xsl:apply-templates");
        xmlStreamWriter.writeAttribute("select", "* | @* | text() | comment()");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeCharacters("\n");
    }

    protected void addDefaultDBLoginContext(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("application-policy");
        xmlStreamWriter.writeAttribute("name", "tolvenDB");
        xmlStreamWriter.writeStartElement("authentication");
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.DBPasswordStoreLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "username");
        xmlStreamWriter.writeCharacters(getTolvenConfigWrapper().getDBServer().getUser());
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "passwordStoreAlias");
        xmlStreamWriter.writeCharacters(getTolvenConfigWrapper().getDBServerRootPasswordId());
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.ManagedConnectionFactoryLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "password-stacking");
        xmlStreamWriter.writeCharacters("useFirstPass");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "managedConnectionFactoryKey");
        xmlStreamWriter.writeCharacters("ManagedConnectionFactory");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "managedConnectionFactoryName");
        xmlStreamWriter.writeCharacters("jboss.jca:service=XATxCM,name=DefaultDS");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "serverId");
        xmlStreamWriter.writeCharacters("jboss");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    protected void addDefaultRuleQueueLoginContext(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("application-policy");
        xmlStreamWriter.writeAttribute("name", "tolvenRuleQueue");
        xmlStreamWriter.writeStartElement("authentication");
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.PasswordStoreLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "username");
        xmlStreamWriter.writeCharacters(getTolvenConfigWrapper().getMDBUserId());
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "passwordStoreAlias");
        xmlStreamWriter.writeCharacters(getTolvenConfigWrapper().getMDBUserId());
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    protected void addDefaultUserLoginContext(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("application-policy");
        xmlStreamWriter.writeAttribute("name", "tolvenLDAP");
        xmlStreamWriter.writeStartElement("authentication");
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.KeyLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "jaasSecurityDomain");
        xmlStreamWriter.writeCharacters("tolven/ldap");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "principalDNPrefix");
        xmlStreamWriter.writeCharacters("uid");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "principalDNSuffix");
        ExtensionPoint ldapSourceExtensionPoint = getDescriptor().getExtensionPoint(EXTENSION_LDAPSOURCE);
        ExtensionPoint parentLDAPSourceExtensionPoint = getParentExtensionPoint(ldapSourceExtensionPoint);
        PluginDescriptor parentLDAPDescriptor = parentLDAPSourceExtensionPoint.getDeclaringPluginDescriptor();
        String ldapPeople = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.people").getDefaultValue();
        String eval_ldapPeople = (String) evaluate(ldapPeople, parentLDAPDescriptor);
        if (eval_ldapPeople == null) {
            throw new RuntimeException("plugin property: ldapPeople '" + ldapPeople + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        String ldapSuffix = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.suffix").getDefaultValue();
        String eval_ldapSuffix = (String) evaluate(ldapSuffix, parentLDAPDescriptor);
        if (eval_ldapSuffix == null) {
            throw new RuntimeException("plugin property: ldapSuffix '" + ldapSuffix + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        xmlStreamWriter.writeCharacters(eval_ldapPeople + "," + eval_ldapSuffix);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "rolesCtxDN");
        String ldapGroups = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.groups").getDefaultValue();
        String eval_ldapGroups = (String) evaluate(ldapGroups, parentLDAPDescriptor);
        if (eval_ldapGroups == null) {
            throw new RuntimeException("plugin property: ldapGroups '" + ldapGroups + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        xmlStreamWriter.writeCharacters(eval_ldapGroups + "," + eval_ldapSuffix);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "roleAttributeID");
        xmlStreamWriter.writeCharacters("cn");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "guestPrincipalName");
        xmlStreamWriter.writeCharacters("tolvenGuest");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "guestPassword");
        xmlStreamWriter.writeCharacters("tolvenGuest");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.jboss.security.ClientLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "restore-login-identity");
        xmlStreamWriter.writeCharacters("true");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    protected void addCustomUserLoginContext(Extension userLoginContextExtension, XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        PluginDescriptor pluginDescriptor = userLoginContextExtension.getDeclaringPluginDescriptor();
        xmlStreamWriter.writeStartElement("application-policy");
        xmlStreamWriter.writeAttribute("name", "tolvenLDAP");
        xmlStreamWriter.writeStartElement("authentication");
        for (Parameter loginModuleParameter : userLoginContextExtension.getParameters("login-module")) {
            xmlStreamWriter.writeStartElement("login-module");
            String code = loginModuleParameter.getSubParameter("code").valueAsString();
            String eval_code = (String) evaluate(code, pluginDescriptor);
            if (eval_code == null) {
                throw new RuntimeException("plugin property: code '" + code + "'evaluated to: null");
            }
            xmlStreamWriter.writeAttribute("code", eval_code);
            String flag = loginModuleParameter.getSubParameter("flag").valueAsString();
            String eval_flag = (String) evaluate(code, pluginDescriptor);
            if (eval_flag == null) {
                throw new RuntimeException("plugin property: flag '" + flag + "'evaluated to: null");
            }
            xmlStreamWriter.writeAttribute("flag", flag);
            for (Parameter moduleOptionParameter : loginModuleParameter.getSubParameters("module-option")) {
                xmlStreamWriter.writeStartElement("module-option");
                String name = moduleOptionParameter.getSubParameter("name").valueAsString();
                String eval_name = (String) evaluate(name, pluginDescriptor);
                if (eval_name == null) {
                    throw new RuntimeException("plugin property: name '" + name + "'evaluated to: null");
                }
                xmlStreamWriter.writeAttribute("name", name);
                String value = moduleOptionParameter.getSubParameter("value").valueAsString();
                String eval_value = (String) evaluate(value, pluginDescriptor);
                if (eval_value == null) {
                    throw new RuntimeException("plugin property: value '" + value + "'evaluated to: null");
                }
                xmlStreamWriter.writeCharacters(value);
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();
        }
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    protected void addDefaultJmsXARealmLoginContext(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("application-policy");
        xmlStreamWriter.writeAttribute("name", "JmsXARealm");
        xmlStreamWriter.writeStartElement("authentication");
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.KeyLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "jaasSecurityDomain");
        xmlStreamWriter.writeCharacters("tolven/ldap");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "principalDNPrefix");
        xmlStreamWriter.writeCharacters("uid");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "principalDNSuffix");
        ExtensionPoint ldapSourceExtensionPoint = getDescriptor().getExtensionPoint(EXTENSION_LDAPSOURCE);
        ExtensionPoint parentLDAPSourceExtensionPoint = getParentExtensionPoint(ldapSourceExtensionPoint);
        PluginDescriptor parentLDAPDescriptor = parentLDAPSourceExtensionPoint.getDeclaringPluginDescriptor();
        String ldapPeople = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.people").getDefaultValue();
        String eval_ldapPeople = (String) evaluate(ldapPeople, parentLDAPDescriptor);
        if (eval_ldapPeople == null) {
            throw new RuntimeException("plugin property: ldapPeople '" + ldapPeople + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        String ldapSuffix = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.suffix").getDefaultValue();
        String eval_ldapSuffix = (String) evaluate(ldapSuffix, parentLDAPDescriptor);
        if (eval_ldapSuffix == null) {
            throw new RuntimeException("plugin property: ldapSuffix '" + ldapSuffix + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        xmlStreamWriter.writeCharacters(eval_ldapPeople + "," + eval_ldapSuffix);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "rolesCtxDN");
        String ldapGroups = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.groups").getDefaultValue();
        String eval_ldapGroups = (String) evaluate(ldapGroups, parentLDAPDescriptor);
        if (eval_ldapGroups == null) {
            throw new RuntimeException("plugin property: ldapGroups '" + ldapGroups + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        xmlStreamWriter.writeCharacters(eval_ldapGroups + "," + eval_ldapSuffix);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "roleAttributeID");
        xmlStreamWriter.writeCharacters("cn");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "guestPrincipalName");
        xmlStreamWriter.writeCharacters("tolvenGuest");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "guestPassword");
        xmlStreamWriter.writeCharacters("tolvenGuest");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.ManagedConnectionFactoryLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "password-stacking");
        xmlStreamWriter.writeCharacters("useFirstPass");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "managedConnectionFactoryKey");
        xmlStreamWriter.writeCharacters("ManagedConnectionFactory");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "managedConnectionFactoryName");
        xmlStreamWriter.writeCharacters("jboss.jca:service=TxCM,name=JmsXA");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "serverId");
        xmlStreamWriter.writeCharacters("jboss");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    protected void addDefaultJMXConsoleLoginContext(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("application-policy");
        xmlStreamWriter.writeAttribute("name", "jmx-console");
        xmlStreamWriter.writeStartElement("authentication");
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.tolven.security.auth.KeyLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "jaasSecurityDomain");
        xmlStreamWriter.writeCharacters("tolven/ldap");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "principalDNPrefix");
        xmlStreamWriter.writeCharacters("uid");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "principalDNSuffix");
        ExtensionPoint ldapSourceExtensionPoint = getDescriptor().getExtensionPoint(EXTENSION_LDAPSOURCE);
        ExtensionPoint parentLDAPSourceExtensionPoint = getParentExtensionPoint(ldapSourceExtensionPoint);
        PluginDescriptor parentLDAPDescriptor = parentLDAPSourceExtensionPoint.getDeclaringPluginDescriptor();
        String ldapPeople = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.people").getDefaultValue();
        String eval_ldapPeople = (String) evaluate(ldapPeople, parentLDAPDescriptor);
        if (eval_ldapPeople == null) {
            throw new RuntimeException("plugin property: ldapPeople '" + ldapPeople + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        String ldapSuffix = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.suffix").getDefaultValue();
        String eval_ldapSuffix = (String) evaluate(ldapSuffix, parentLDAPDescriptor);
        if (eval_ldapSuffix == null) {
            throw new RuntimeException("plugin property: ldapSuffix '" + ldapSuffix + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        xmlStreamWriter.writeCharacters(eval_ldapPeople + "," + eval_ldapSuffix);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "rolesCtxDN");
        String ldapGroups = parentLDAPSourceExtensionPoint.getParameterDefinition("ldap.groups").getDefaultValue();
        String eval_ldapGroups = (String) evaluate(ldapGroups, parentLDAPDescriptor);
        if (eval_ldapGroups == null) {
            throw new RuntimeException("plugin property: ldapGroups '" + ldapGroups + "'evaluated to: null for: " + ldapSourceExtensionPoint.getUniqueId());
        }
        xmlStreamWriter.writeCharacters(eval_ldapGroups + "," + eval_ldapSuffix);
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "roleAttributeID");
        xmlStreamWriter.writeCharacters("cn");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "guestPrincipalName");
        xmlStreamWriter.writeCharacters("tolvenGuest");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("module-option");
        xmlStreamWriter.writeAttribute("name", "guestPassword");
        xmlStreamWriter.writeCharacters("tolvenGuest");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("login-module");
        xmlStreamWriter.writeAttribute("code", "org.jboss.security.ClientLoginModule");
        xmlStreamWriter.writeAttribute("flag", "required");
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    protected void assembleLibJars() throws IOException {
        ExtensionPoint extensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_LIBJAR_COMPONENT);
        ExtensionPoint appServerExtensionPoint = getParentExtensionPoint(extensionPoint);
        String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
        File appserverHomeDir = new File(appserverHome);
        File stageDirAppserverHomeDir = new File(getStageDir(), appserverHomeDir.getName());
        File stageAppserverLibDir = new File(stageDirAppserverHomeDir, LIBDIR);
        for (Extension extension : appServerExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor pluginDescriptor = extension.getDeclaringPluginDescriptor();
            for (Parameter parameter : extension.getParameters("jar")) {
                File sourceJar = getFilePath(pluginDescriptor, parameter.valueAsString());
                File destJar = new File(stageAppserverLibDir, sourceJar.getName());
                FileUtils.copyFile(sourceJar, destJar);
            }
        }
    }

    protected void assembleLibClasses() throws IOException {
        File jar = new File(getPluginTmpDir(), getDescriptor().getId() + ".jar");
        jar.delete();
        ExtensionPoint classesExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_LIB_CLASSES);
        for (Extension classesExtension : classesExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor pluginDescriptor = classesExtension.getDeclaringPluginDescriptor();
            String dirname = classesExtension.getParameter("dir").valueAsString();
            String eval_dirname = (String) evaluate(dirname, pluginDescriptor);
            if (eval_dirname == null) {
                throw new RuntimeException("plugin property: dir '" + dirname + "'evaluated to: null for: " + pluginDescriptor);
            }
            File dir = getFilePath(pluginDescriptor, dirname);
            TolvenJar.jarDir(dir, jar, true);
        }
        if (jar.exists()) {
            String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
            File appserverHomeDir = new File(appserverHome);
            File stageDirAppserverHomeDir = new File(getStageDir(), appserverHomeDir.getName());
            File stageAppserverLibDir = new File(stageDirAppserverHomeDir, LIBDIR);
            logger.debug("Copy " + jar.getPath() + " to " + stageAppserverLibDir.getPath());
            FileUtils.copyFileToDirectory(jar, stageAppserverLibDir, true);
        }
    }

    protected void copyToStageDir() throws IOException {
        ExtensionPoint stageExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_DEPLOY);
        for (Extension stageExtension : stageExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor stagePluginDescriptor = stageExtension.getDeclaringPluginDescriptor();
            String deployDirname = stageExtension.getParameter("stageDir").valueAsString();
            File jbossTmpDir = new File(getPluginTmpDir(stagePluginDescriptor), deployDirname);
            String appserverHomeDirname = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
            File appserverHomeDir = new File(appserverHomeDirname);
            File jbossStageDir = new File(getStageDir(), appserverHomeDir.getName());
            logger.debug("Copy " + jbossTmpDir.getPath() + " to " + jbossStageDir.getPath());
            jbossStageDir.mkdirs();
            FileUtils.copyDirectory(jbossTmpDir, jbossStageDir);
        }
    }

    @Override
    protected void doStop() throws Exception {
        logger.debug("*** stop ***");
    }
}
