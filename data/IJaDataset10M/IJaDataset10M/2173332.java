package es.caib.sistra.persistence.delegate;

import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import es.caib.sistra.persistence.intf.IdiomaFacade;
import es.caib.sistra.persistence.util.IdiomaFacadeUtil;

/**
 * Business delegate pera consultar idiomas.
 */
public class IdiomaDelegate implements StatelessDelegate {

    public List listarLenguajes() throws DelegateException {
        try {
            return getFacade().listarLenguajes();
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }

    public String lenguajePorDefecto() throws DelegateException {
        try {
            return getFacade().lenguajePorDefecto();
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }

    private IdiomaFacade getFacade() throws NamingException, RemoteException, CreateException {
        return IdiomaFacadeUtil.getHome().create();
    }

    protected IdiomaDelegate() throws DelegateException {
    }
}
