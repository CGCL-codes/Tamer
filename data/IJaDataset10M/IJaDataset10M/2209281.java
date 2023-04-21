package es.cim.sistra.conectorBus.persistence.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.cim.ESBClient.v1.ServiceESBClientFactory;
import es.cim.ESBClient.v1.tramitacion.TramitacionESBClient;
import es.cim.sistra.conectorBus.persistence.util.ConfiguracionUtil;

/**
 * Bean con la funcionalidad b�sica para interactuar con el bus del consell. 
 */
public class ESBConnectorEJB implements SessionBean {

    protected static Log log = LogFactory.getLog(ESBConnectorEJB.class);

    protected SessionContext ctx = null;

    private ServiceESBClientFactory sf = null;

    private TramitacionESBClient tramitacionService = null;

    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
        this.ctx = ctx;
    }

    public void ejbCreate() throws EJBException, RemoteException {
        try {
            String urlService = ConfiguracionUtil.getInstance().getProperty("bus.servicio.tramitacion.url");
            boolean generateTimestamp = new Boolean(ConfiguracionUtil.getInstance().getProperty("bus.generateTimestamp")).booleanValue();
            boolean logCalls = new Boolean(ConfiguracionUtil.getInstance().getProperty("bus.logCall")).booleanValue();
            boolean disableCnCheck = new Boolean(ConfiguracionUtil.getInstance().getProperty("bus.disableCnCheck")).booleanValue();
            String usuario = ConfiguracionUtil.getInstance().getProperty("bus.usuario");
            String password = ConfiguracionUtil.getInstance().getProperty("bus.password");
            this.sf = new ServiceESBClientFactory(generateTimestamp, logCalls, disableCnCheck);
            this.tramitacionService = sf.getTramitacionESBClient();
            this.tramitacionService.setAutenticacion(urlService, usuario, password);
        } catch (Exception e) {
            log.debug("Error al obtener propiedades conexion bus.", e);
            throw new EJBException("Error al obtener propiedades conexion bus.." + e.getMessage(), e);
        }
    }

    protected TramitacionESBClient getTramitacionService() {
        return tramitacionService;
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void unsetSessionContext() {
    }
}
