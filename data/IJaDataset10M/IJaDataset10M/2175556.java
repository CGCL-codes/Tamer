package org.biomage.Interface;

import java.util.*;
import org.biomage.Protocol.HardwareApplication;

/**
 *  
 *  
 */
public interface HasHardwareApplications {

    /**
     *  Inner list class for holding multiple entries for attribute 
     *  hardwareApplications.  Simply creates a named vector.
     *  
     */
    public class HardwareApplications_list extends Vector {
    }

    /**
     *  Set method for hardwareApplications
     *  
     *  @param value to set
     *  
     *  
     */
    public void setHardwareApplications(HardwareApplications_list hardwareApplications);

    /**
     *  Get method for hardwareApplications
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public HardwareApplications_list getHardwareApplications();

    /**
     *  Method to add HardwareApplication to HardwareApplications_list
     *  
     */
    public void addToHardwareApplications(HardwareApplication hardwareApplication);

    /**
     *  Method to add HardwareApplication at position to 
     *  HardwareApplications_list
     *  
     */
    public void addToHardwareApplications(int position, HardwareApplication hardwareApplication);

    /**
     *  Method to get HardwareApplication from HardwareApplications_list
     *  
     */
    public HardwareApplication getFromHardwareApplications(int position);

    /**
     *  Method to remove by position from HardwareApplications_list
     *  
     */
    public void removeElementAtFromHardwareApplications(int position);

    /**
     *  Method to remove first HardwareApplication from 
     *  HardwareApplications_list
     *  
     */
    public void removeFromHardwareApplications(HardwareApplication hardwareApplication);
}
