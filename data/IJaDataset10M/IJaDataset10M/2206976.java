package com.intel.gpe.clients.api.jsdl;

import com.intel.gpe.clients.api.Job;
import com.intel.gpe.constants.ProcessorType;

/**
 * The abstraction of the JSDL job definition.
 * 
 * @author Alexander Lukichev
 * @version $Id: JSDLJob.java,v 1.10 2006/05/02 12:59:20 baklushin Exp $
 */
public interface JSDLJob extends Job {

    /**
     * Set /jsdl:Jobdefinition/@id attribute
     * @param id The job identifier
     */
    public void setId(String id);

    /**
     * @return The job identifier
     */
    public String getId();

    /**
     * Add data stage-in element.
     * @param uri The location of the file to stage-in
     * @param fileSystem The name of the filesystem where to put the file
     * @param file The local name of the file (in the working directory)
     */
    public void addDataStagingImportElement(String uri, String fileSystem, String file);

    /**
     * Add data stage-out element
     * @param fileSystem The name of the filesystem where to get the file from
     * @param file The local name of the file (in the working directory)
     * @param uri The remote location where the file is to be put
     */
    public void addDataStagingExportElement(String fileSystem, String file, String uri);

    /**
     * Set requirements for target O/S
     *  
     * @param osType - the requirements structure
     */
    public void setOperatingSystemRequirements(OperatingSystemRequirementsType osType);

    /**
     * Get the required O/S characteristics.
     * 
     * @return the O/S requirements
     */
    public OperatingSystemRequirementsType getOperatingSystemRequirements();

    /**
     * Set the requirements for target CPU architecture
     * 
     * @param cpuArchitecture - the required CPU acrhitecture
     */
    public void setCPUArchitectureRequirements(ProcessorType cpuArchitecture);

    /**
     * Get the requirements for target CPU architecture
     * 
     * @return the required CPU acrhitecture
     */
    public ProcessorType getCPUArchitectureRequirements();

    /**
     * Set required CPU count
     * 
     * @param cpuCount - the required CPU count
     */
    public void setIndividualCPUCountRequirements(RangeValueType cpuCount);

    /**
     * Get the required CPU count per node
     * 
     * @return the required CPU count per node
     */
    public RangeValueType getIndividualCPUCountRequirements();

    /**
     * Set the required total CPU count
     * 
     * @param cpuCount - the required total CPU count
     */
    public void setTotalCPUCountRequirements(RangeValueType cpuCount);

    /**
     * Get the required CPU count
     * 
     * @return the required CPU count
     */
    public RangeValueType getTotalCPUCountRequirements();

    /**
     * Set the required RAM amount per node
     * 
     * @param physicalMemory - the required RAM amount per node
     */
    public void setIndividualPhysicalMemoryRequirements(RangeValueType physicalMemory);

    /**
     * Get the required RAM amount per node
     * 
     * @return - the required RAM amount per node
     */
    public RangeValueType getIndividualPhysicalMemoryRequirements();

    /**
     * Set the required CPU speed per node
     * 
     * @param cpuSpeed - the required CPU speed per node
     */
    public void setIndividualCPUSpeedRequirements(RangeValueType cpuSpeed);

    /**
     * Get the required CPU speed per node
     * 
     * @return the required CPU speed per node
     */
    public RangeValueType getIndividualCPUSpeedRequirements();

    /**
     * Set the required disk space per node
     * 
     * @param diskSpace - the required disk space per node
     */
    public void setIndividualDiskSpaceRequirements(RangeValueType diskSpace);

    /**
     * Get the required disk space per node
     * 
     * @return the required disk space per node
     */
    public RangeValueType getIndividualDiskSpaceRequirements();

    /**
     * Set the flag of exclusive execution
     * @param exclusiveExecutuion - if <code>true</code> then use the target machine exclusively
     */
    public void setExclusiveExecutionRequirements(boolean exclusiveExecutuion);

    /**
     * Get the flag of exclusive execution
     * @return the flag of exclusive execution
     */
    public boolean getExclusiveExecutionRequirements();
}
