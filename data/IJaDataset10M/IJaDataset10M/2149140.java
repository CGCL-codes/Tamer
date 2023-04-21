package org.dbe.composer.wfengine.bpel.server.admin.rdebug.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;

/**
 * Interface which defines the operations exposed by the remote debugger.
 */
public interface IServiceEventHandler extends Remote {

    /**
     * Event handler callback for engine events, such as process creation and termination.
     * @param aContextId the context id used to locate the callback
     * @param aProcessId the id of the process affected
     * @param aEventType the event type
     * @param aProcessName the name of the process
     */
    public void engineEventHandler(long aContextId, long aProcessId, int aEventType, QName aProcessName) throws RemoteException;

    /**
     * Event handler callback for process events, such as process state and info.
     * @param aContextId the context id used to locate the callback
     * @param aProcessId the id of the process affected
     * @param aPath path of node affected by the event
     * @param aEventType the event type
     * @param aFaultName an optional fault name if fault event
     * @param aText optional text
     * @param aName The process' QName.
     * @return True if the process needs to be suspended, False otherwise
     */
    public boolean processEventHandler(long aContextId, long aProcessId, String aPath, int aEventType, String aFaultName, String aText, QName aName) throws RemoteException;
}
