package org.ikasan.connector.basefiletransfer.net;

import java.util.List;

/**
 * Client Filter interface
 * 
 * @author Ikasan Development Team 
 */
public interface ClientFilter {

    /**
     * Return true if the ClientListEntry matches
     * @param lsEntry
     * @return true if the ClientListEntry matches else false
     */
    public boolean match(ClientListEntry lsEntry);

    /**
     * return a filtered list of ClientListEntry objects 
     * @param entries
     * @return filtered list of ClientListEntry objects
     */
    public List<ClientListEntry> filter(List<ClientListEntry> entries);
}
