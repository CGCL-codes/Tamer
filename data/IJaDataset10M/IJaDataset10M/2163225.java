package org.epoline.phoenix.common.shared.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.epoline.phoenix.authentication.shared.UserAuthenticator;
import org.epoline.phoenix.common.shared.ItemServiceStatus;
import org.epoline.phoenix.common.shared.PhoenixException;
import org.epoline.service.shared.security.base.SecServerInfo;
import org.epoline.service.support.BaseServiceInterface;

/**
 *
 * Communicator interface. Is needed for Jini support. Implementer of this
 * interface will be registered in Jini Lookup service. Via an implementer GUI
 * client will get all remote references to different managers of one server.
 */
public interface IServicePhoenix extends Remote, BaseServiceInterface {

    static final String SERVICE_NAME = "PHOENIX_SERVICE";

    public Remote getManagerRemote(String service) throws PhoenixException, RemoteException;

    public SecServerInfo getSecServerInfo() throws RemoteException;

    public ItemServiceStatus getServiceSelfcheckingResult(UserAuthenticator auth) throws RemoteException;
}
