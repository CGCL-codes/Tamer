package net.sf.jawp.game.service;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import net.sf.jawp.RMIConst;
import net.sf.jawp.api.service.JAWPGameService;
import net.sf.jawp.gf.api.services.UserService;
import net.sf.jawp.gf.api.services.rmi.UserServiceRMI;
import net.sf.jawp.gf.server.rmi.UserServiceRMIImpl;
import net.sf.jawp.util.Log;

/**
 * 
 * @author jarek
 * @version $Revision$
 *
 */
public class JAWPRMIHelper {

    private static final Log LOG = Log.getLog(JAWPRMIHelper.class);

    private Registry registry;

    private UserServiceRMI userServiceRMI;

    public static JAWPRMIHelper getInstance() {
        return JAWPRMIHelperHolder.instance;
    }

    public synchronized void startRMI(final UserService<JAWPGameService> usrService) {
        if (this.registry == null) {
            try {
                final Registry reg = LocateRegistry.createRegistry(RMIConst.RMI_PORT);
                this.registry = reg;
            } catch (final RemoteException re) {
                LOG.error(re, re);
                throw new RuntimeException(re);
            }
        }
        if (this.userServiceRMI == null) {
            try {
                final UserServiceRMIImpl<JAWPGameService> userImpl = new UserServiceRMIImpl<JAWPGameService>(usrService);
                Naming.rebind("//localhost:" + RMIConst.RMI_PORT + "/userService", userImpl);
                this.userServiceRMI = userImpl;
            } catch (final RemoteException re) {
                LOG.error(re, re);
                throw new RuntimeException(re);
            } catch (final MalformedURLException e) {
                LOG.error(e, e);
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void stopRMI() {
        if (this.userServiceRMI != null) {
            try {
                Naming.unbind("//localhost:" + RMIConst.RMI_PORT + "/userService");
                this.userServiceRMI = null;
            } catch (final AccessException e) {
                LOG.error(e, e);
                throw new RuntimeException(e);
            } catch (final RemoteException e) {
                LOG.error(e, e);
                throw new RuntimeException(e);
            } catch (final NotBoundException e) {
                LOG.error(e, e);
                throw new RuntimeException(e);
            } catch (final MalformedURLException e) {
                LOG.error(e, e);
                throw new RuntimeException(e);
            }
        }
    }

    private static class JAWPRMIHelperHolder {

        private static JAWPRMIHelper instance = new JAWPRMIHelper();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void finalize() throws Throwable {
        stopRMI();
    }
}
