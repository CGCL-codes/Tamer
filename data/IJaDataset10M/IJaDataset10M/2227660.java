package com.cube42.echoverse.viewer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import com.cube42.echoverse.model.EntityID;
import com.cube42.echoverse.model.EntityInfoCollection;
import com.cube42.echoverse.model.Information;
import com.cube42.echoverse.model.InformationKey;
import com.cube42.util.instruction.InstructionDefCollection;

/**
 * Represents a viewer that can log into the Echoverse
 *
 * @author  Matt Paulin
 * @version $Id: Echoviewer.java,v 1.2 2003/03/12 01:48:36 zer0wing Exp $
 */
public interface Echoviewer extends Remote {

    /**
     * Informs the viewer with a message from the server
     *
     * @param   String message
     * @throws  RemoteException if a network error occurs
     */
    public void remoteServerMessage(String message) throws RemoteException;

    /**
     * Request the viewer to select one of the provided entity shells
     * <p>
     * Returns null if non were selected
     *
     * @param   shells  A hashtable of the entity shells names to select from
     *                  The key is the name of the shell, the value is the
     *                  user currently using that shell
     * @return  The name of the shell selected
     * @throws  RemoteException if a network error occurs
     */
    public String remoteSelectShell(Hashtable shells) throws RemoteException;

    /**
     * Tells the viewer all the available instructions for the shell they are using
     *
     * @param   instructionDefs     The available instructions
     * @throws  RemoteException if a network error occurs
     */
    public void remoteSetShellInstructions(InstructionDefCollection instructionDefs) throws RemoteException;

    /**
     * Sets the EchoverseSession to communicate with
     *
     * @param   session     The EchoverseSession to communicate with
     * @throws  RemoteException if a network error occurs
     */
    public void remoteSetEchoverseSession(EchoverseSession session) throws RemoteException;

    /**
     * Sets the EntityID of the entity that the viewer is using in the world
     *
     * @param   entityID    The EntityID that the viewer is using
     * @throws  RemoteException if a network error occurs
     */
    public void remoteSetControlledEntityID(EntityID entityID) throws RemoteException;

    /**
     * Updates a collection of Entity Info to be displayed in the viewer
     *
     * @param   updateInfo  The entity info to be updated in the viewer
     * @throws  RemoteException if a network error occurs
     */
    public void remoteUpdateEntityInfo(EntityInfoCollection updateInfo) throws RemoteException;

    /**
     * Removes a collection of Entity Info from the viewer
     *
     * @param   removeInfo      The entity info to be removed from the viewer
     * @throws  RemoteException if a network error occurs
     */
    public void remoteRemoveEntityInfo(EntityInfoCollection removeInfo) throws RemoteException;

    /**
     * Notifies the viewer of specific information
     *
     * @param   enactor     The entity that sent the information
     * @param   key         An InformationKey describing what this information is
     * @param   information The information
     */
    public void remoteInform(EntityID enactor, InformationKey key, Information information) throws RemoteException;

    /**
     * Checks to see if the viewer is connected
     *
     * @return  true    If the viewer is connected
     * @throws  RemoteException if a network error occurs
     */
    public boolean ping() throws RemoteException;

    /**
     * Called when the Echoverse disconnects the viewer
     *
     * @throws  RemoteException if a network error occurs
     */
    public void remoteDisconnect() throws RemoteException;
}
