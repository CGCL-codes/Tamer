package org.biomage.Interface;

import java.util.*;
import org.biomage.AuditAndSecurity.Contact;

/**
 *  
 *  
 */
public interface HasSourceContact {

    /**
     *  Inner list class for holding multiple entries for attribute 
     *  sourceContact.  Simply creates a named vector.
     *  
     */
    public class SourceContact_list extends Vector {
    }

    /**
     *  Set method for sourceContact
     *  
     *  @param value to set
     *  
     *  
     */
    public void setSourceContact(SourceContact_list sourceContact);

    /**
     *  Get method for sourceContact
     *  
     *  @return value of the attribute
     *  
     *  
     */
    public SourceContact_list getSourceContact();

    /**
     *  Method to add Contact to SourceContact_list
     *  
     */
    public void addToSourceContact(Contact contact);

    /**
     *  Method to add Contact at position to SourceContact_list
     *  
     */
    public void addToSourceContact(int position, Contact contact);

    /**
     *  Method to get Contact from SourceContact_list
     *  
     */
    public Contact getFromSourceContact(int position);

    /**
     *  Method to remove by position from SourceContact_list
     *  
     */
    public void removeElementAtFromSourceContact(int position);

    /**
     *  Method to remove first Contact from SourceContact_list
     *  
     */
    public void removeFromSourceContact(Contact contact);
}
