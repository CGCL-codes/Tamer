package org.biomage.Interface;

import java.util.*;
import org.biomage.Description.OntologyEntry;

/**
 *  
 *  
 */
public interface HasSpecies {

    /**
     *  Set method for species
     *  
     *  @param value to set
     *  
     *  
     */
    public void setSpecies(OntologyEntry species);

    /**
     *  Get method for species
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public OntologyEntry getSpecies();
}
