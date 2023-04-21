package org.biomage.Interface;

import java.util.*;
import org.biomage.Description.Description;

/**
 *  
 *  
 */
public interface HasDescriptions {

    /**
     *  Inner list class for holding multiple entries for attribute 
     *  descriptions.  Simply creates a named vector.
     *  
     */
    public class Descriptions_list extends Vector {
    }

    /**
     *  Set method for descriptions
     *  
     *  @param value to set
     *  
     *  
     */
    public void setDescriptions(Descriptions_list descriptions);

    /**
     *  Get method for descriptions
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Descriptions_list getDescriptions();

    /**
     *  Method to add Description to Descriptions_list
     *  
     */
    public void addToDescriptions(Description description);

    /**
     *  Method to add Description at position to Descriptions_list
     *  
     */
    public void addToDescriptions(int position, Description description);

    /**
     *  Method to get Description from Descriptions_list
     *  
     */
    public Description getFromDescriptions(int position);

    /**
     *  Method to remove by position from Descriptions_list
     *  
     */
    public void removeElementAtFromDescriptions(int position);

    /**
     *  Method to remove first Description from Descriptions_list
     *  
     */
    public void removeFromDescriptions(Description description);
}
