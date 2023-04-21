package net.sf.istcontract.aws.configuration;

import java.io.File;
import java.io.InputStream;
import net.sf.istcontract.aws.AgentParser;

/**
 * 
 * FileDirectoryFacilitator is a specific implementation of DirectoryFacilitator
 * that obtains the agent information from a file.
 * 
 * It is part of the CommunicationManager component.
 * 
 * @author sergio
 * 
 */
public class FileDirectoryFacilitator extends BaseDirectoryFaclitator implements DirectoryFacilitator {

    private String agentsFilename;

    public FileDirectoryFacilitator(String stFile) throws InvalidAgentConfiguration {
        agentsFilename = stFile;
        InputStream is;
        is = AgentParser.class.getResourceAsStream("/" + stFile);
        try {
            System.out.println("Taking agent configuration from: " + AgentParser.class.getResource("/" + stFile).toString());
            as = AgentParser.getAgentScenarioFromXml(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidAgentConfiguration(e);
        }
    }

    public long getConfigurationVersionNumber() {
        return new File(agentsFilename).lastModified();
    }
}
