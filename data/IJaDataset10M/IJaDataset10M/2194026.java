package org.biomage.Interface;

import java.util.*;
import org.biomage.Description.OntologyEntry;

/**
 *  
 *  
 */
public interface HasAssociations {

    /**
     *  Inner list class for holding multiple entries for attribute 
     *  associations.  Simply creates a named vector.
     *  
     */
    public class Associations_list extends Vector {
    }

    /**
     *  Set method for associations
     *  
     *  @param value to set
     *  
     *  
     */
    public void setAssociations(Associations_list associations);

    /**
     *  Get method for associations
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public Associations_list getAssociations();

    /**
     *  Method to add OntologyEntry to Associations_list
     *  
     */
    public void addToAssociations(OntologyEntry ontologyEntry);

    /**
     *  Method to add OntologyEntry at position to Associations_list
     *  
     */
    public void addToAssociations(int position, OntologyEntry ontologyEntry);

    /**
     *  Method to get OntologyEntry from Associations_list
     *  
     */
    public OntologyEntry getFromAssociations(int position);

    /**
     *  Method to remove by position from Associations_list
     *  
     */
    public void removeElementAtFromAssociations(int position);

    /**
     *  Method to remove first OntologyEntry from Associations_list
     *  
     */
    public void removeFromAssociations(OntologyEntry ontologyEntry);
}
