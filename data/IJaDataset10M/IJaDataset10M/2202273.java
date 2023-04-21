package org.biomage.Interface;

import java.util.*;
import org.biomage.DesignElement.CompositeSequence;

/**
 *  
 *  
 */
public interface HasComposite {

    /**
     *  Set method for composite
     *  
     *  @param value to set
     *  
     *  
     */
    public void setComposite(CompositeSequence composite);

    /**
     *  Get method for composite
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public CompositeSequence getComposite();
}
