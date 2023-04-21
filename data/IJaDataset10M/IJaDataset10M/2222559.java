package net.sf.jawp.gf.server.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.security.auth.login.LoginException;
import net.sf.jawp.gf.api.services.LoginService;
import net.sf.jawp.gf.api.services.rmi.LoginServiceRMI;
import net.sf.jawp.gf.api.services.rmi.SessionServiceRMI;

/**
 * 
 * @author jarek
 * @version $Revision: 1.5 $
 *
 * @param <GAMESERVICE> native game service
 */
public final class LoginServiceRMIImpl<GAMESERVICE> extends UnicastRemoteObject implements LoginServiceRMI<GAMESERVICE> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final LoginService<GAMESERVICE> service;

    /** Creates a new instance of LoginServiceRMIImpl */
    public LoginServiceRMIImpl(final LoginService<GAMESERVICE> srv) throws RemoteException {
        this.service = srv;
    }

    public String getServerSeed() {
        return this.service.getServerSeed();
    }

    public SessionServiceRMI<GAMESERVICE> login(final String clientString) throws RemoteException, LoginException {
        return new SessionServiceRMIImpl<GAMESERVICE>(this.service.login(clientString));
    }
}
