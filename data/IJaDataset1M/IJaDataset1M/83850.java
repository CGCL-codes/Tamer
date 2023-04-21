package es.caib.bantel.persistence.delegate;

import java.rmi.RemoteException;
import java.util.Properties;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import es.caib.bantel.persistence.intf.ConfiguracionFacadeLocal;
import es.caib.bantel.persistence.util.ConfiguracionFacadeUtil;

public class ConfiguracionDelegate implements StatelessDelegate {

    public Properties obtenerConfiguracion() throws DelegateException {
        try {
            return getFacade().obtenerPropiedades();
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }

    private ConfiguracionFacadeLocal getFacade() throws NamingException, RemoteException, CreateException {
        return ConfiguracionFacadeUtil.getLocalHome().create();
    }

    protected ConfiguracionDelegate() throws DelegateException {
    }
}
