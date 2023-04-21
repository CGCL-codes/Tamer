package scpn.wsauth.session;

import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author jrmacias <jrmacias@cnb.csic.es>
 */
@WebService(serviceName = "SessionManagerWS")
public class SessionManagerWS {

    @EJB
    private SessionManagerBean ejbRef;

    @WebMethod(operationName = "addSessionId")
    public boolean addSessionId(@WebParam(name = "sessionTicket") SessionTicket sessionTicket) throws UnsupportedOperationException {
        return ejbRef.addSessionId(sessionTicket);
    }

    @WebMethod(operationName = "getSessionId")
    public SessionTicket getSessionId(@WebParam(name = "userId") Integer userId) throws UnsupportedOperationException {
        return ejbRef.getSessionId(userId);
    }

    @WebMethod(operationName = "getAllSessionIds")
    public List<SessionTicket> getAllSessionIds() {
        return ejbRef.getAllSessionIds();
    }

    @WebMethod(operationName = "getNewSession")
    public SessionTicket getNewSession(@WebParam(name = "user") String user, @WebParam(name = "password") String password) throws ScpnAuthenticationException {
        return ejbRef.getNewSession(user, password);
    }

    @WebMethod(operationName = "checkPassword")
    public boolean checkPassword(@WebParam(name = "user") String user, @WebParam(name = "pass") String pass) {
        return ejbRef.checkPassword(user, pass);
    }
}
