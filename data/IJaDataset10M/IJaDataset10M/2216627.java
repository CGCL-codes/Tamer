package net.sf.istcontract.aws.configuration;

/**
 * 
 * AgentConfiguration represents the abstract concept of a set of user
 * configuration parameters provided by the user, with no specific assumption
 * about how and where they are given.
 * 
 * It is part of the AgentShell component.
 * 
 * @author sergio
 * 
 */
public interface AgentConfiguration {

    String getIP();

    String getPort();

    DirectoryFacilitator getDirectoryFacilitator() throws InvalidAgentConfiguration;

    String getLogPath();

    String getLogType();

    String getTempPath();

    String getJaxbPath();

    String getFullPath();

    String getGlassfishUsername();

    String getGlassfishPassword();

    String getGlassfishAdminPort();

    String getContainer();

    String getSensorClass();

    String getActionReportClass();

    String getSensorActionClass();

    String getGlobalServerIP();

    String getGlobalConfigurationName();

    int getGlobalPollPeriod();

    boolean isConfigurationRemote();

    long getConfigurationVersion();
}
