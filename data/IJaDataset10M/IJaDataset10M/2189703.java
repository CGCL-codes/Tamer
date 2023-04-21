package org.monet.services.callbackservice;

public interface Callback_PortType extends java.rmi.Remote {

    public void responseCallback(java.lang.String serviceRequestId, byte[] responseDocument) throws java.rmi.RemoteException, org.monet.services.frontservice.SessionError, org.monet.services.frontservice.SystemError, org.monet.services.frontservice.DataError;
}
