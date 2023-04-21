package org.nexusbpm.activiti.servicetasks;

import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;
import org.activiti.designer.integration.servicetask.annotation.Help;
import org.activiti.designer.integration.servicetask.annotation.Property;
import org.activiti.designer.integration.servicetask.annotation.Runtime;

/**
 * Defines the SAS Statistics nexusbpm node.
 * 
 * @author Matthew Sandoz
 */
@Runtime(delegationClass = "org.nexusbpm.activiti.SasStatisticsNexusJavaDelegation")
@Help(displayHelpShort = "Invoke SAS Code")
public class SasNexusTask extends AbstractCustomServiceTask {

    @Property(type = PropertyType.MULTILINE_TEXT, displayName = "SAS Code", required = false)
    @Help(displayHelpShort = "SAS Code to execute")
    private String sasCode;

    @Property(type = PropertyType.MULTILINE_TEXT, displayName = "Output")
    @Help(displayHelpShort = "SAS execution output")
    private String output;

    @Property(type = PropertyType.MULTILINE_TEXT, displayName = "Error")
    @Help(displayHelpShort = "SAS execution errors")
    private String error;

    @Property(type = PropertyType.BOOLEAN_CHOICE, displayName = "Keep Session?", required = false)
    @Help(displayHelpShort = "Should we try to re-use existing sessions?")
    private Boolean keepSessions;

    @Property(type = PropertyType.TEXT, displayName = "Server Address")
    @Help(displayHelpShort = "Name the sheet being populated")
    private String serverAddress;

    @Property(type = PropertyType.TEXT, displayName = "Session Object", required = false)
    @Help(displayHelpShort = "If it works, a handle to the open session")
    private String session;

    @Property(type = PropertyType.TEXT, displayName = "Data File", required = false)
    @Help(displayHelpShort = "Name of the file resource to use for data")
    private String dataFile;

    @Property(type = PropertyType.TEXT, displayName = "Output File", required = false)
    @Help(displayHelpShort = "Name of the file resource to use for output")
    private String outputFile;

    @Override
    public String contributeToPaletteDrawer() {
        return NexusConstants.NEXUS_PALETTE;
    }

    @Override
    public String getName() {
        return "SAS Stats";
    }

    @Override
    public String getSmallIconPath() {
        return "icons/sas.png";
    }
}
