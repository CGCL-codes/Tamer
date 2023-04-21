package org.nightlabs.jfire.webapp.serverconfig;

import org.apache.struts.action.ActionForm;
import org.nightlabs.jfire.servermanager.config.DatabaseCf;
import org.nightlabs.jfire.servermanager.config.J2eeCf;
import org.nightlabs.jfire.servermanager.config.JDOCf;
import org.nightlabs.jfire.servermanager.config.JFireServerConfigModule;
import org.nightlabs.jfire.servermanager.config.RootOrganisationCf;
import org.nightlabs.jfire.servermanager.config.ServerCf;
import org.nightlabs.jfire.servermanager.config.SmtpMailServiceCf;

/**
 * @author marco
 */
public class ServerConfigForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String action;

    private boolean setupMode;

    private String jfireServerConfigModule_rootOrg_organisationID;

    private String jfireServerConfigModule_rootOrg_organisationName;

    private String jfireServerConfigModule_rootOrg_serverID;

    private String jfireServerConfigModule_rootOrg_serverName;

    private String jfireServerConfigModule_rootOrg_j2eeServerType;

    private String jfireServerConfigModule_rootOrg_initialContextURL;

    private String jfireServerConfigModule_serverID;

    private String jfireServerConfigModule_serverName;

    private String jfireServerConfigModule_j2eeServerType;

    private String jfireServerConfigModule_initialContextURL;

    private String jfireServerConfigModule_j2eeDeployBaseDirectory;

    private String jfireServerConfigModule_j2eeServerConfigurator;

    private String jfireServerConfigModule_databaseDriverName_noTx;

    private String jfireServerConfigModule_databaseDriverName_localTx;

    private String jfireServerConfigModule_databaseDriverName_xa;

    private String jfireServerConfigModule_databaseURL;

    private String jfireServerConfigModule_databasePrefix;

    private String jfireServerConfigModule_databaseSuffix;

    private String jfireServerConfigModule_databaseUserName;

    private String jfireServerConfigModule_databasePassword;

    private String jfireServerConfigModule_databaseAdapter;

    private String jfireServerConfigModule_datasourceConfigFile;

    private String jfireServerConfigModule_datasourceTemplateDSXMLFile;

    private String jfireServerConfigModule_datasourceMetadataTypeMapping;

    private String jfireServerConfigModule_jdoDeploymentDirectory;

    private String jfireServerConfigModule_jdoDeploymentDescriptorFile;

    private String jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile;

    private String jfireServerConfigModule_jdoPersistenceConfigurationFile;

    private String jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile;

    private String jfireServerConfigModule_smtpEncryptionMethod;

    private String jfireServerConfigModule_smtpHost;

    private String jfireServerConfigModule_smtpMailFrom;

    private String jfireServerConfigModule_smtpPassword;

    private String jfireServerConfigModule_smtpPort;

    private String jfireServerConfigModule_smtpUsername;

    private Boolean jfireServerConfigModule_smtpUseAuthentication;

    /**
	 * @return Returns the action.
	 */
    public String getAction() {
        return action;
    }

    /**
	 * @param action The action to set.
	 */
    public void setAction(String action) {
        this.action = action;
    }

    /**
	 * @return Returns the setupMode.
	 */
    public boolean isSetupMode() {
        return setupMode;
    }

    /**
	 * @param setupMode The setupMode to set.
	 */
    public void setSetupMode(boolean setupMode) {
        this.setupMode = setupMode;
    }

    /**
	 * @return Returns the jfireServerConfigModule.
	 */
    public JFireServerConfigModule getJfireServerConfigModule() {
        JFireServerConfigModule cfMod = new JFireServerConfigModule();
        ServerCf rootOrgServer = new ServerCf(jfireServerConfigModule_rootOrg_serverID);
        RootOrganisationCf rootOrg = new RootOrganisationCf(jfireServerConfigModule_rootOrg_organisationID, jfireServerConfigModule_rootOrg_organisationName, rootOrgServer);
        ServerCf localServer = new ServerCf();
        J2eeCf j2ee = new J2eeCf();
        DatabaseCf database = new DatabaseCf();
        JDOCf jdo = new JDOCf();
        SmtpMailServiceCf smtp = new SmtpMailServiceCf();
        cfMod.setRootOrganisation(rootOrg);
        cfMod.setLocalServer(localServer);
        cfMod.setJ2ee(j2ee);
        cfMod.setDatabase(database);
        cfMod.setJdo(jdo);
        cfMod.setSmtp(smtp);
        rootOrg.setOrganisationID(jfireServerConfigModule_rootOrg_organisationID);
        rootOrg.setOrganisationName(jfireServerConfigModule_rootOrg_organisationName);
        rootOrgServer.setServerID(jfireServerConfigModule_rootOrg_serverID);
        rootOrgServer.setServerName(jfireServerConfigModule_rootOrg_serverName);
        rootOrgServer.setJ2eeServerType(jfireServerConfigModule_rootOrg_j2eeServerType);
        rootOrgServer.setInitialContextURL(jfireServerConfigModule_rootOrg_initialContextURL);
        localServer.setServerID(jfireServerConfigModule_serverID);
        localServer.setServerName(jfireServerConfigModule_serverName);
        localServer.setJ2eeServerType(jfireServerConfigModule_j2eeServerType);
        localServer.setInitialContextURL(jfireServerConfigModule_initialContextURL);
        j2ee.setJ2eeDeployBaseDirectory(jfireServerConfigModule_j2eeDeployBaseDirectory);
        j2ee.setServerConfigurator(jfireServerConfigModule_j2eeServerConfigurator);
        database.setDatabaseDriverName_noTx(jfireServerConfigModule_databaseDriverName_noTx);
        database.setDatabaseDriverName_localTx(jfireServerConfigModule_databaseDriverName_localTx);
        database.setDatabaseDriverName_xa(jfireServerConfigModule_databaseDriverName_xa);
        database.setDatabaseURL(jfireServerConfigModule_databaseURL);
        database.setDatabasePrefix(jfireServerConfigModule_databasePrefix);
        database.setDatabaseSuffix(jfireServerConfigModule_databaseSuffix);
        database.setDatabaseUserName(jfireServerConfigModule_databaseUserName);
        database.setDatabasePassword(jfireServerConfigModule_databasePassword);
        database.setDatabaseAdapter(jfireServerConfigModule_databaseAdapter);
        database.setDatasourceConfigFile(jfireServerConfigModule_datasourceConfigFile);
        database.setDatasourceTemplateDSXMLFile(jfireServerConfigModule_datasourceTemplateDSXMLFile);
        database.setDatasourceMetadataTypeMapping(jfireServerConfigModule_datasourceMetadataTypeMapping);
        jdo.setJdoDeploymentDirectory(jfireServerConfigModule_jdoDeploymentDirectory);
        jdo.setJdoDeploymentDescriptorFile(jfireServerConfigModule_jdoDeploymentDescriptorFile);
        jdo.setJdoDeploymentDescriptorTemplateFile(jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile);
        jdo.setJdoPersistenceConfigurationFile(jfireServerConfigModule_jdoPersistenceConfigurationFile);
        jdo.setJdoPersistenceConfigurationTemplateFile(jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile);
        smtp.setEncryptionMethod(jfireServerConfigModule_smtpEncryptionMethod);
        smtp.setHost(jfireServerConfigModule_smtpHost);
        smtp.setMailFrom(jfireServerConfigModule_smtpMailFrom);
        smtp.setPassword(jfireServerConfigModule_smtpPassword);
        smtp.setUsername(jfireServerConfigModule_smtpUsername);
        smtp.setUseAuthentication(jfireServerConfigModule_smtpUseAuthentication);
        if (jfireServerConfigModule_smtpPort == null || jfireServerConfigModule_smtpPort.isEmpty()) {
            if (SmtpMailServiceCf.ENCRYPTION_METHOD_NONE.equals(smtp.getEncryptionMethod())) smtp.setPort(SmtpMailServiceCf.DEFAULT_PORT_PLAIN); else if (SmtpMailServiceCf.ENCRYPTION_METHOD_SSL.equals(smtp.getEncryptionMethod())) smtp.setPort(SmtpMailServiceCf.DEFAULT_PORT_SSL);
        } else smtp.setPort(Integer.valueOf(jfireServerConfigModule_smtpPort));
        return cfMod;
    }

    /**
	 * @param jfireServerConfigModule The jfireServerConfigModule to set.
	 */
    public void setJfireServerConfigModule(JFireServerConfigModule cfMod) {
        jfireServerConfigModule_rootOrg_organisationID = cfMod.getRootOrganisation().getOrganisationID();
        jfireServerConfigModule_rootOrg_organisationName = cfMod.getRootOrganisation().getOrganisationName();
        jfireServerConfigModule_rootOrg_serverID = cfMod.getRootOrganisation().getServer().getServerID();
        jfireServerConfigModule_rootOrg_serverName = cfMod.getRootOrganisation().getServer().getServerName();
        jfireServerConfigModule_rootOrg_j2eeServerType = cfMod.getRootOrganisation().getServer().getJ2eeServerType();
        jfireServerConfigModule_rootOrg_initialContextURL = cfMod.getRootOrganisation().getServer().getInitialContextURL();
        jfireServerConfigModule_serverID = cfMod.getLocalServer().getServerID();
        jfireServerConfigModule_serverName = cfMod.getLocalServer().getServerName();
        jfireServerConfigModule_j2eeServerType = cfMod.getLocalServer().getJ2eeServerType();
        jfireServerConfigModule_initialContextURL = cfMod.getLocalServer().getInitialContextURL();
        jfireServerConfigModule_j2eeDeployBaseDirectory = cfMod.getJ2ee().getJ2eeDeployBaseDirectory();
        jfireServerConfigModule_j2eeServerConfigurator = cfMod.getJ2ee().getServerConfigurator();
        jfireServerConfigModule_databaseDriverName_noTx = cfMod.getDatabase().getDatabaseDriverName_noTx();
        jfireServerConfigModule_databaseDriverName_localTx = cfMod.getDatabase().getDatabaseDriverName_localTx();
        jfireServerConfigModule_databaseDriverName_xa = cfMod.getDatabase().getDatabaseDriverName_xa();
        jfireServerConfigModule_databaseURL = cfMod.getDatabase().getDatabaseURL();
        jfireServerConfigModule_databasePrefix = cfMod.getDatabase().getDatabasePrefix();
        jfireServerConfigModule_databaseSuffix = cfMod.getDatabase().getDatabaseSuffix();
        jfireServerConfigModule_databaseUserName = cfMod.getDatabase().getDatabaseUserName();
        jfireServerConfigModule_databasePassword = cfMod.getDatabase().getDatabasePassword();
        jfireServerConfigModule_databaseAdapter = cfMod.getDatabase().getDatabaseAdapter();
        jfireServerConfigModule_datasourceConfigFile = cfMod.getDatabase().getDatasourceConfigFile();
        jfireServerConfigModule_datasourceTemplateDSXMLFile = cfMod.getDatabase().getDatasourceTemplateDSXMLFile();
        jfireServerConfigModule_datasourceMetadataTypeMapping = cfMod.getDatabase().getDatasourceMetadataTypeMapping();
        jfireServerConfigModule_jdoDeploymentDirectory = cfMod.getJdo().getJdoDeploymentDirectory();
        jfireServerConfigModule_jdoDeploymentDescriptorFile = cfMod.getJdo().getJdoDeploymentDescriptorFile();
        jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile = cfMod.getJdo().getJdoDeploymentDescriptorTemplateFile();
        jfireServerConfigModule_jdoPersistenceConfigurationFile = cfMod.getJdo().getJdoPersistenceConfigurationFile();
        jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile = cfMod.getJdo().getJdoPersistenceConfigurationTemplateFile();
        jfireServerConfigModule_smtpEncryptionMethod = cfMod.getSmtp().getEncryptionMethod();
        jfireServerConfigModule_smtpHost = cfMod.getSmtp().getHost();
        jfireServerConfigModule_smtpMailFrom = cfMod.getSmtp().getMailFrom();
        jfireServerConfigModule_smtpPassword = cfMod.getSmtp().getPassword();
        jfireServerConfigModule_smtpPort = String.valueOf(cfMod.getSmtp().getPort());
        jfireServerConfigModule_smtpUsername = cfMod.getSmtp().getUsername();
        jfireServerConfigModule_smtpUseAuthentication = cfMod.getSmtp().getUseAuthentication();
        if (SmtpMailServiceCf.ENCRYPTION_METHOD_NONE.equals(jfireServerConfigModule_smtpEncryptionMethod) && SmtpMailServiceCf.DEFAULT_PORT_PLAIN == cfMod.getSmtp().getPort()) {
            jfireServerConfigModule_smtpPort = "";
        }
        if (SmtpMailServiceCf.ENCRYPTION_METHOD_SSL.equals(jfireServerConfigModule_smtpEncryptionMethod) && SmtpMailServiceCf.DEFAULT_PORT_SSL == cfMod.getSmtp().getPort()) {
            jfireServerConfigModule_smtpPort = "";
        }
    }

    /**
	 * @return Returns the jfireServerConfigModule_rootOrg_organisationID.
	 */
    public String getJfireServerConfigModule_rootOrg_organisationID() {
        return jfireServerConfigModule_rootOrg_organisationID;
    }

    /**
	 * @param jfireServerConfigModule_rootOrg_organisationID The jfireServerConfigModule_rootOrg_organisationID to set.
	 */
    public void setJfireServerConfigModule_rootOrg_organisationID(String jfireServerConfigModule_rootOrg_organisationID) {
        this.jfireServerConfigModule_rootOrg_organisationID = jfireServerConfigModule_rootOrg_organisationID;
    }

    /**
	 * @return Returns the jfireServerConfigModule_rootOrg_serverID.
	 */
    public String getJfireServerConfigModule_rootOrg_serverID() {
        return jfireServerConfigModule_rootOrg_serverID;
    }

    /**
	 * @param jfireServerConfigModule_rootOrg_serverID The jfireServerConfigModule_rootOrg_serverID to set.
	 */
    public void setJfireServerConfigModule_rootOrg_serverID(String jfireServerConfigModule_rootOrg_serverID) {
        this.jfireServerConfigModule_rootOrg_serverID = jfireServerConfigModule_rootOrg_serverID;
    }

    /**
	 * @return Returns the jfireServerConfigModule_rootOrg_serverName.
	 */
    public String getJfireServerConfigModule_rootOrg_serverName() {
        return jfireServerConfigModule_rootOrg_serverName;
    }

    /**
	 * @param jfireServerConfigModule_rootOrg_serverName The jfireServerConfigModule_rootOrg_serverName to set.
	 */
    public void setJfireServerConfigModule_rootOrg_serverName(String jfireServerConfigModule_rootOrg_serverName) {
        this.jfireServerConfigModule_rootOrg_serverName = jfireServerConfigModule_rootOrg_serverName;
    }

    /**
	 * @return Returns the jfireServerConfigModule_rootOrg_initialContextURL.
	 */
    public String getJfireServerConfigModule_rootOrg_initialContextURL() {
        return jfireServerConfigModule_rootOrg_initialContextURL;
    }

    /**
	 * @param jfireServerConfigModule_rootOrg_initialContextURL The jfireServerConfigModule_rootOrg_initialContextURL to set.
	 */
    public void setJfireServerConfigModule_rootOrg_initialContextURL(String jfireServerConfigModule_rootOrg_initialContextURL) {
        this.jfireServerConfigModule_rootOrg_initialContextURL = jfireServerConfigModule_rootOrg_initialContextURL;
    }

    /**
	 * @return Returns the jfireServerConfigModule_rootOrg_j2eeServerType.
	 */
    public String getJfireServerConfigModule_rootOrg_j2eeServerType() {
        return jfireServerConfigModule_rootOrg_j2eeServerType;
    }

    /**
	 * @param jfireServerConfigModule_rootOrg_j2eeServerType The jfireServerConfigModule_rootOrg_j2eeServerType to set.
	 */
    public void setJfireServerConfigModule_rootOrg_j2eeServerType(String jfireServerConfigModule_rootOrg_j2eeServerType) {
        this.jfireServerConfigModule_rootOrg_j2eeServerType = jfireServerConfigModule_rootOrg_j2eeServerType;
    }

    /**
	 * @return Returns the jfireServerConfigModule_rootOrg_organisationName.
	 */
    public String getJfireServerConfigModule_rootOrg_organisationName() {
        return jfireServerConfigModule_rootOrg_organisationName;
    }

    /**
	 * @param jfireServerConfigModule_rootOrg_organisationName The jfireServerConfigModule_rootOrg_organisationName to set.
	 */
    public void setJfireServerConfigModule_rootOrg_organisationName(String jfireServerConfigModule_rootOrg_organisationName) {
        this.jfireServerConfigModule_rootOrg_organisationName = jfireServerConfigModule_rootOrg_organisationName;
    }

    public String getJfireServerConfigModule_databaseDriverName_noTx() {
        return jfireServerConfigModule_databaseDriverName_noTx;
    }

    public void setJfireServerConfigModule_databaseDriverName_noTx(String jfireServerConfigModule_databaseDriverName_noTx) {
        this.jfireServerConfigModule_databaseDriverName_noTx = jfireServerConfigModule_databaseDriverName_noTx;
    }

    public String getJfireServerConfigModule_databaseDriverName_localTx() {
        return jfireServerConfigModule_databaseDriverName_localTx;
    }

    public void setJfireServerConfigModule_databaseDriverName_localTx(String jfireServerConfigModule_databaseDriverName_localTx) {
        this.jfireServerConfigModule_databaseDriverName_localTx = jfireServerConfigModule_databaseDriverName_localTx;
    }

    public String getJfireServerConfigModule_databaseDriverName_xa() {
        return jfireServerConfigModule_databaseDriverName_xa;
    }

    public void setJfireServerConfigModule_databaseDriverName_xa(String jfireServerConfigModule_databaseDriverName_xa) {
        this.jfireServerConfigModule_databaseDriverName_xa = jfireServerConfigModule_databaseDriverName_xa;
    }

    public String getJfireServerConfigModule_databaseURL() {
        return jfireServerConfigModule_databaseURL;
    }

    public void setJfireServerConfigModule_databaseURL(String jfireServerConfigModule_databaseURL) {
        this.jfireServerConfigModule_databaseURL = jfireServerConfigModule_databaseURL;
    }

    /**
	 * @return Returns the jfireServerConfigModule_databasePassword.
	 */
    public String getJfireServerConfigModule_databasePassword() {
        return jfireServerConfigModule_databasePassword;
    }

    /**
	 * @param jfireServerConfigModule_databasePassword The jfireServerConfigModule_databasePassword to set.
	 */
    public void setJfireServerConfigModule_databasePassword(String jfireServerConfigModule_databasePassword) {
        this.jfireServerConfigModule_databasePassword = jfireServerConfigModule_databasePassword;
    }

    /**
	 * @return Returns the jfireServerConfigModule_databasePrefix.
	 */
    public String getJfireServerConfigModule_databasePrefix() {
        return jfireServerConfigModule_databasePrefix;
    }

    /**
	 * @param jfireServerConfigModule_databasePrefix The jfireServerConfigModule_databasePrefix to set.
	 */
    public void setJfireServerConfigModule_databasePrefix(String jfireServerConfigModule_databasePrefix) {
        this.jfireServerConfigModule_databasePrefix = jfireServerConfigModule_databasePrefix;
    }

    /**
	 * @return Returns the jfireServerConfigModule_databaseSuffix.
	 */
    public String getJfireServerConfigModule_databaseSuffix() {
        return jfireServerConfigModule_databaseSuffix;
    }

    /**
	 * @param jfireServerConfigModule_databaseSuffix The jfireServerConfigModule_databaseSuffix to set.
	 */
    public void setJfireServerConfigModule_databaseSuffix(String jfireServerConfigModule_databaseSuffix) {
        this.jfireServerConfigModule_databaseSuffix = jfireServerConfigModule_databaseSuffix;
    }

    /**
	 * @return Returns the jfireServerConfigModule_databaseUserName.
	 */
    public String getJfireServerConfigModule_databaseUserName() {
        return jfireServerConfigModule_databaseUserName;
    }

    /**
	 * @param jfireServerConfigModule_databaseUserName The jfireServerConfigModule_databaseUserName to set.
	 */
    public void setJfireServerConfigModule_databaseUserName(String jfireServerConfigModule_databaseUserName) {
        this.jfireServerConfigModule_databaseUserName = jfireServerConfigModule_databaseUserName;
    }

    /**
	 * @return Returns the jfireServerConfigModule_databaseAdapter.
	 */
    public String getJfireServerConfigModule_databaseAdapter() {
        return jfireServerConfigModule_databaseAdapter;
    }

    /**
	 * @param jfireServerConfigModule_databaseAdapter The jfireServerConfigModule_databaseAdapter to set.
	 */
    public void setJfireServerConfigModule_databaseAdapter(String jfireServerConfigModule_databaseCreator) {
        this.jfireServerConfigModule_databaseAdapter = jfireServerConfigModule_databaseCreator;
    }

    /**
	 * @return Returns the jfireServerConfigModule_j2eeServerType.
	 */
    public String getJfireServerConfigModule_j2eeServerType() {
        return jfireServerConfigModule_j2eeServerType;
    }

    /**
	 * @param jfireServerConfigModule_j2eeServerType The jfireServerConfigModule_j2eeServerType to set.
	 */
    public void setJfireServerConfigModule_j2eeServerType(String jfireServerConfigModule_j2eeServerType) {
        this.jfireServerConfigModule_j2eeServerType = jfireServerConfigModule_j2eeServerType;
    }

    /**
	 * @return Returns the jfireServerConfigModule_initialContextURL.
	 */
    public String getJfireServerConfigModule_initialContextURL() {
        return jfireServerConfigModule_initialContextURL;
    }

    /**
	 * @param jfireServerConfigModule_initialContextURL The jfireServerConfigModule_initialContextURL to set.
	 */
    public void setJfireServerConfigModule_initialContextURL(String jfireServerConfigModule_initialContextURL) {
        this.jfireServerConfigModule_initialContextURL = jfireServerConfigModule_initialContextURL;
    }

    /**
	 * @return Returns the jfireServerConfigModule_jdoDeploymentDirectory.
	 */
    public String getJfireServerConfigModule_jdoDeploymentDirectory() {
        return jfireServerConfigModule_jdoDeploymentDirectory;
    }

    /**
	 * @param jfireServerConfigModule_jdoDeploymentDirectory The jfireServerConfigModule_jdoDeploymentDirectory to set.
	 */
    public void setJfireServerConfigModule_jdoDeploymentDirectory(String jfireServerConfigModule_jdoConfigDirectory) {
        this.jfireServerConfigModule_jdoDeploymentDirectory = jfireServerConfigModule_jdoConfigDirectory;
    }

    /**
	 * @return Returns the jfireServerConfigModule_jdoDeploymentDescriptorFile.
	 */
    public String getJfireServerConfigModule_jdoDeploymentDescriptorFile() {
        return jfireServerConfigModule_jdoDeploymentDescriptorFile;
    }

    /**
	 * @param jfireServerConfigModule_jdoDeploymentDescriptorFile The jfireServerConfigModule_jdoDeploymentDescriptorFile to set.
	 */
    public void setJfireServerConfigModule_jdoDeploymentDescriptorFile(String jfireServerConfigModule_jdoConfigFilePrefix) {
        this.jfireServerConfigModule_jdoDeploymentDescriptorFile = jfireServerConfigModule_jdoConfigFilePrefix;
    }

    /**
	 * @return Returns the jfireServerConfigModule_datasourceConfigFile.
	 */
    public String getJfireServerConfigModule_datasourceConfigFile() {
        return jfireServerConfigModule_datasourceConfigFile;
    }

    /**
	 * @param jfireServerConfigModule_datasourceConfigFile The jfireServerConfigModule_datasourceConfigFile to set.
	 */
    public void setJfireServerConfigModule_datasourceConfigFile(String jfireServerConfigModule_jdoConfigFileSuffix) {
        this.jfireServerConfigModule_datasourceConfigFile = jfireServerConfigModule_jdoConfigFileSuffix;
    }

    /**
	 * @return Returns the jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile.
	 */
    public String getJfireServerConfigModule_jdoDeploymentDescriptorTemplateFile() {
        return jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile;
    }

    /**
	 * @param jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile The jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile to set.
	 */
    public void setJfireServerConfigModule_jdoDeploymentDescriptorTemplateFile(String jfireServerConfigModule_jdoTemplateDSXMLFile) {
        this.jfireServerConfigModule_jdoDeploymentDescriptorTemplateFile = jfireServerConfigModule_jdoTemplateDSXMLFile;
    }

    public String getJfireServerConfigModule_jdoPersistenceConfigurationFile() {
        return jfireServerConfigModule_jdoPersistenceConfigurationFile;
    }

    public void setJfireServerConfigModule_jdoPersistenceConfigurationFile(String jfireServerConfigModule_jdoPersistenceConfigurationFile) {
        this.jfireServerConfigModule_jdoPersistenceConfigurationFile = jfireServerConfigModule_jdoPersistenceConfigurationFile;
    }

    public String getJfireServerConfigModule_jdoPersistenceConfigurationTemplateFile() {
        return jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile;
    }

    public void setJfireServerConfigModule_jdoPersistenceConfigurationTemplateFile(String jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile) {
        this.jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile = jfireServerConfigModule_jdoPersistenceConfigurationTemplateFile;
    }

    /**
	 * @return Returns the jfireServerConfigModule_serverName.
	 */
    public String getJfireServerConfigModule_serverName() {
        return jfireServerConfigModule_serverName;
    }

    /**
	 * @param jfireServerConfigModule_serverName The jfireServerConfigModule_serverName to set.
	 */
    public void setJfireServerConfigModule_serverName(String jfireServerConfigModule_serverName) {
        this.jfireServerConfigModule_serverName = jfireServerConfigModule_serverName;
    }

    /**
	 * @return Returns the jfireServerConfigModule_serverID.
	 */
    public String getJfireServerConfigModule_serverID() {
        return jfireServerConfigModule_serverID;
    }

    /**
	 * @param jfireServerConfigModule_serverID The jfireServerConfigModule_serverID to set.
	 */
    public void setJfireServerConfigModule_serverID(String jfireServerConfigModule_serverID) {
        this.jfireServerConfigModule_serverID = jfireServerConfigModule_serverID;
    }

    /**
	 * @return Returns the jfireServerConfigModule_j2eeDeployBaseDirectory.
	 */
    public String getJfireServerConfigModule_j2eeDeployBaseDirectory() {
        return jfireServerConfigModule_j2eeDeployBaseDirectory;
    }

    /**
	 * @param jfireServerConfigModule_j2eeDeployBaseDirectory The jfireServerConfigModule_j2eeDeployBaseDirectory to set.
	 */
    public void setJfireServerConfigModule_j2eeDeployBaseDirectory(String jfireServerConfigModule_j2eeDeployBaseDirectory) {
        this.jfireServerConfigModule_j2eeDeployBaseDirectory = jfireServerConfigModule_j2eeDeployBaseDirectory;
    }

    /**
	 * @return the jfireServerConfigModule_j2eeServerConfigurator
	 */
    public String getJfireServerConfigModule_j2eeServerConfigurator() {
        return jfireServerConfigModule_j2eeServerConfigurator;
    }

    /**
	 * @param jfireServerConfigModule_j2eeServerConfigurator the jfireServerConfigModule_j2eeServerConfigurator to set
	 */
    public void setJfireServerConfigModule_j2eeServerConfigurator(String jfireServerConfigModule_j2eeServerConfigurator) {
        this.jfireServerConfigModule_j2eeServerConfigurator = jfireServerConfigModule_j2eeServerConfigurator;
    }

    /**
	 * @return the jfireServerConfigModule_datasourceTemplateDSXMLFile
	 */
    public String getJfireServerConfigModule_datasourceTemplateDSXMLFile() {
        return jfireServerConfigModule_datasourceTemplateDSXMLFile;
    }

    /**
	 * @param jfireServerConfigModule_datasourceTemplateDSXMLFile the jfireServerConfigModule_datasourceTemplateDSXMLFile to set
	 */
    public void setJfireServerConfigModule_datasourceTemplateDSXMLFile(String jfireServerConfigModule_datasourceTemplateDSXMLFile) {
        this.jfireServerConfigModule_datasourceTemplateDSXMLFile = jfireServerConfigModule_datasourceTemplateDSXMLFile;
    }

    /**
	 * @return the jfireServerConfigModule_datasourceMetadataTypeMapping
	 */
    public String getJfireServerConfigModule_datasourceMetadataTypeMapping() {
        return jfireServerConfigModule_datasourceMetadataTypeMapping;
    }

    /**
	 * @param jfireServerConfigModule_datasourceMetadataTypeMapping the jfireServerConfigModule_datasourceMetadataTypeMapping to set
	 */
    public void setJfireServerConfigModule_datasourceMetadataTypeMapping(String jfireServerConfigModule_datasourceMetadataTypeMapping) {
        this.jfireServerConfigModule_datasourceMetadataTypeMapping = jfireServerConfigModule_datasourceMetadataTypeMapping;
    }

    /**
	 * @return the jfireServerConfigModule_smtpEncryptionMethod
	 */
    public String getJfireServerConfigModule_smtpEncryptionMethod() {
        return jfireServerConfigModule_smtpEncryptionMethod;
    }

    /**
	 * @param jfireServerConfigModule_smtpEncryptionMethod the jfireServerConfigModule_smtpEncryptionMethod to set
	 */
    public void setJfireServerConfigModule_smtpEncryptionMethod(String jfireServerConfigModule_smtpEncryptionMethod) {
        this.jfireServerConfigModule_smtpEncryptionMethod = jfireServerConfigModule_smtpEncryptionMethod;
    }

    /**
	 * @return the jfireServerConfigModule_smtpHost
	 */
    public String getJfireServerConfigModule_smtpHost() {
        return jfireServerConfigModule_smtpHost;
    }

    /**
	 * @param jfireServerConfigModule_smtpHost the jfireServerConfigModule_smtpHost to set
	 */
    public void setJfireServerConfigModule_smtpHost(String jfireServerConfigModule_smtpHost) {
        this.jfireServerConfigModule_smtpHost = jfireServerConfigModule_smtpHost;
    }

    /**
	 * @return the jfireServerConfigModule_smtpMailFrom
	 */
    public String getJfireServerConfigModule_smtpMailFrom() {
        return jfireServerConfigModule_smtpMailFrom;
    }

    /**
	 * @param jfireServerConfigModule_smtpMailFrom the jfireServerConfigModule_smtpMailFrom to set
	 */
    public void setJfireServerConfigModule_smtpMailFrom(String jfireServerConfigModule_smtpMailFrom) {
        this.jfireServerConfigModule_smtpMailFrom = jfireServerConfigModule_smtpMailFrom;
    }

    /**
	 * @return the jfireServerConfigModule_smtpPassword
	 */
    public String getJfireServerConfigModule_smtpPassword() {
        return jfireServerConfigModule_smtpPassword;
    }

    /**
	 * @param jfireServerConfigModule_smtpPassword the jfireServerConfigModule_smtpPassword to set
	 */
    public void setJfireServerConfigModule_smtpPassword(String jfireServerConfigModule_smtpPassword) {
        this.jfireServerConfigModule_smtpPassword = jfireServerConfigModule_smtpPassword;
    }

    /**
	 * @return the jfireServerConfigModule_smtpPort
	 */
    public String getJfireServerConfigModule_smtpPort() {
        return jfireServerConfigModule_smtpPort;
    }

    /**
	 * @param jfireServerConfigModule_smtpPort the jfireServerConfigModule_smtpPort to set
	 */
    public void setJfireServerConfigModule_smtpPort(String jfireServerConfigModule_smtpPort) {
        this.jfireServerConfigModule_smtpPort = jfireServerConfigModule_smtpPort;
    }

    /**
	 * @return the jfireServerConfigModule_smtpUsername
	 */
    public String getJfireServerConfigModule_smtpUsername() {
        return jfireServerConfigModule_smtpUsername;
    }

    /**
	 * @param jfireServerConfigModule_smtpUsername the jfireServerConfigModule_smtpUsername to set
	 */
    public void setJfireServerConfigModule_smtpUsername(String jfireServerConfigModule_smtpUsername) {
        this.jfireServerConfigModule_smtpUsername = jfireServerConfigModule_smtpUsername;
    }

    /**
	 * @return the jfireServerConfigModule_smtpUseAuthentication
	 */
    public Boolean getJfireServerConfigModule_smtpUseAuthentication() {
        return jfireServerConfigModule_smtpUseAuthentication;
    }

    /**
	 * @param jfireServerConfigModule_smtpUseAuthentication the jfireServerConfigModule_smtpUseAuthentication to set
	 */
    public void setJfireServerConfigModule_smtpUseAuthentication(Boolean jfireServerConfigModule_smtpUseAuthentication) {
        this.jfireServerConfigModule_smtpUseAuthentication = jfireServerConfigModule_smtpUseAuthentication;
    }
}
