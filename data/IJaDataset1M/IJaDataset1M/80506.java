package org.biomage.Interface;

import java.util.*;
import org.biomage.BioAssay.PhysicalBioAssay;

/**
 *  
 *  
 */
public interface HasTarget {

    /**
     *  Set method for target
     *  
     *  @param value to set
     *  
     *  
     */
    public void setTarget(PhysicalBioAssay target);

    /**
     *  Get method for target
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public PhysicalBioAssay getTarget();
}
