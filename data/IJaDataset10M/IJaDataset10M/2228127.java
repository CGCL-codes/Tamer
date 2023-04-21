package org.amlfilter.service;

import java.util.List;
import org.amlfilter.model.Process;

/**
 * An interface that declares functionality to manage
 * processanization related artifacts
 *
 * @author Harish Seshadri
 * @version $Id: ProcessServiceInterface.java,v 1.1 2007/01/28 07:13:33 hseshadr Exp $
 */
public interface ProcessServiceInterface {

    /**
	 * Get the processanization
     * @param pProcessId The processanization id
	 * @return The processanization object
	 */
    public Process getProcess(long pProcessId) throws Exception;

    /**
	 * Refresh the processanization
     * @param pProcess The processanization
	 */
    public void refreshProcess(Process pProcess) throws Exception;

    /**
	 * Get the uncached processanization
     * @param pProcessId The processanization id
	 * @return The processanization object
	 */
    public Process getUncachedProcess(long pProcessId) throws Exception;

    /**
     * Get all the processanizations
     * @return The processanizations
     */
    public List getAllProcesss() throws Exception;

    /**
     * Update the processanization
     * @param pProcess The processanization
     */
    public void updateProcess(Process pProcess) throws Exception;

    /**
     * Store the processanization
     * @param pProcess The processanization
     */
    public void storeProcess(Process pProcess) throws Exception;

    /**
     * Delete the processanization
     * @param pProcess The processanization
     */
    public void deleteProcess(Process pProcess) throws Exception;

    /**
     * Load all the processanization artifacts
     */
    public void loadAll() throws Exception;

    /**
    /**
     * Validate the process. This is done by a series of conditions:
     * Case 1: The process id passed in is empty
     * Case 2: The process id passed in does not correspond to any process
     * Case 3: IP address mismatch from process configuration
     * @param pProcessId The process id
     * @param pRemoteHost The remote host
     * @param pValidateIPAddresses Should we validate the IP addresses bound to the process
     * @throws ProcessServiceException
     */
    public void validateProcess(Long pProcessId, String pRemoteHost, boolean pValidateIPAddresses) throws ProcessServiceException;

    /**
     * Get the processanization designations
     * @return The processanization designations
     */
    public String[] getProcessDesignations(Long pProcessId) throws Exception;
}
