package net.jxta.edutella.service.peertrust;

import net.jxta.edutella.peer.EdutellaService;
import net.jxta.edutella.peer.destination.Destination;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;
import net.jxta.service.Service;

/**
 * This interface specifies the PeertrustService for Edutella, which acts as a
 * kind of transportation layer for Peertrust.
 * 
 * $Id: PeertrustService.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:56 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public interface PeertrustService extends EdutellaService, Service {

    /**
     * Sends a direct message through a pipe to a specific destination.
     * 
     * @param destination
     *            the Destination
     * @param message
     *            the PeertrustMessage
     */
    public void sendMessage(Destination destination, PeertrustMessage message);

    /**
     * Sends a broadcast message using pipes. Note: The mechanism used to
     * establish pipe communication for broadcasts incorporates a certain amount
     * of overhead, that is why this method is not being used in the current
     * version of Peertrust but it should be fully functional.
     * 
     * @param message
     *            the PeertrustMessage
     */
    public void sendMessage(PeertrustMessage message);

    /**
     * Sends a broadcast message via the ResolverService. In most cases this is
     * the prefered method for broadcasting but keep in mind that this is
     * insecure and limited to sending Strings.
     * 
     * @param message
     *            the String message
     */
    public void sendMessage(String message);
}
