package org.biomage.Interface;

import java.util.*;
import org.biomage.BioAssayData.BioAssayData;

/**
 *  
 *  
 */
public interface HasBioAssayDataSources {

    /**
     *  Inner list class for holding multiple entries for attribute 
     *  bioAssayDataSources.  Simply creates a named vector.
     *  
     */
    public class BioAssayDataSources_list extends Vector {
    }

    /**
     *  Set method for bioAssayDataSources
     *  
     *  @param value to set
     *  
     *  
     */
    public void setBioAssayDataSources(BioAssayDataSources_list bioAssayDataSources);

    /**
     *  Get method for bioAssayDataSources
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public BioAssayDataSources_list getBioAssayDataSources();

    /**
     *  Method to add BioAssayData to BioAssayDataSources_list
     *  
     */
    public void addToBioAssayDataSources(BioAssayData bioAssayData);

    /**
     *  Method to add BioAssayData at position to 
     *  BioAssayDataSources_list
     *  
     */
    public void addToBioAssayDataSources(int position, BioAssayData bioAssayData);

    /**
     *  Method to get BioAssayData from BioAssayDataSources_list
     *  
     */
    public BioAssayData getFromBioAssayDataSources(int position);

    /**
     *  Method to remove by position from BioAssayDataSources_list
     *  
     */
    public void removeElementAtFromBioAssayDataSources(int position);

    /**
     *  Method to remove first BioAssayData from BioAssayDataSources_list
     *  
     */
    public void removeFromBioAssayDataSources(BioAssayData bioAssayData);
}
