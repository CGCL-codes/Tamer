package eu.planets_project.tb.impl.services.util.wee;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.planets_project.ifr.core.servreg.api.ServiceRegistry;
import eu.planets_project.ifr.core.servreg.api.ServiceRegistryFactory;
import eu.planets_project.ifr.core.wee.api.wsinterface.WeeService;
import eu.planets_project.ifr.core.wee.api.wsinterface.WftRegistryService;

/**
 * An util object for getting instances of the WeeService, WftRegistryService and ServiceRegistry remote objects
 * @author <a href="mailto:andrew.lindley@ait.ac.at">Andrew Lindley</a>
 * @since 24.09.2009
 *
 */
public class WeeRemoteUtil {

    private Log log = LogFactory.getLog(WeeRemoteUtil.class);

    private WftRegistryService wftRegImp;

    private WeeService weeService;

    private ServiceRegistry registry;

    private static WeeRemoteUtil weeRemote = null;

    public static synchronized WeeRemoteUtil getInstance() {
        if (weeRemote == null) weeRemote = new WeeRemoteUtil();
        return weeRemote;
    }

    private WeeRemoteUtil() {
        this.getWeeRemoteObjects();
    }

    public WeeService getWeeService() {
        return weeService;
    }

    public WftRegistryService getWeeRegistryService() {
        return wftRegImp;
    }

    public ServiceRegistry getServiceRegistry() {
        return registry;
    }

    private void getWeeRemoteObjects() {
        try {
            Context ctx = new javax.naming.InitialContext();
            wftRegImp = (WftRegistryService) PortableRemoteObject.narrow(ctx.lookup("planets-project.eu/WftRegistryService/remote"), WftRegistryService.class);
            weeService = (WeeService) PortableRemoteObject.narrow(ctx.lookup("planets-project.eu/WeeService/remote"), WeeService.class);
        } catch (NamingException e) {
            log.error("Could not retrieve the WeeService or WftRegistryService object" + e);
        }
        registry = ServiceRegistryFactory.getServiceRegistry();
    }
}
