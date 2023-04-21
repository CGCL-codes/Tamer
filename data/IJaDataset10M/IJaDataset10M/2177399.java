package com.hyper9.uvapi.types.virt.resources;

/**
 * An interface for objects that have compute resource properties such as
 * performance statistics and overall status.
 * 
 * @author akutz
 * 
 */
public interface ComputeResource extends ResourceBean {

    /**
     * Gets the CPU value in MHz.
     * 
     * @return The CPU value in MHz.
     */
    public Long getCpu();

    /**
     * Sets the CPU value in MHz.
     * 
     * @param toSet The CPU value in MHz.
     */
    public void setCpu(Long toSet);

    /**
     * Gets the CPU maximum value in MHz.
     * 
     * @return The CPU maximum value in MHz.
     */
    public Long getCpuMax();

    /**
     * Sets the CPU maximum value in MHz.
     * 
     * @param toSet The CPU maximum value in MHz.
     */
    public void setCpuMax(Long toSet);

    /**
     * Gets the memory value in MB.
     * 
     * @return The memory value in MB.
     */
    public Long getMem();

    /**
     * Sets the memory value in MB.
     * 
     * @param toSet The memory value in MB.
     */
    public void setMem(Long toSet);

    /**
     * Gets the memory maximum value in MB.
     * 
     * @return The memory maximum value in MB.
     */
    public Long getMemMax();

    /**
     * Sets the memory maximum value in MB.
     * 
     * @param toSet The memory maximum value in MB.
     */
    public void setMemMax(Long toSet);

    /**
     * Gets the server object's status.
     * 
     * @return The server object's status.
     */
    public String getStatus();

    /**
     * Sets the server object's status.
     * 
     * @param toSet The server object's status.
     */
    public void setStatus(String toSet);
}
