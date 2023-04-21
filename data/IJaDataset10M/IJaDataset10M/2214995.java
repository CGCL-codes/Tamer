package org.knopflerfish.bundle.desktop.prefs;

import org.osgi.framework.*;
import java.util.*;
import java.util.prefs.*;
import org.knopflerfish.bundle.desktop.swing.Activator;
import org.knopflerfish.bundle.desktop.swing.Util;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;

/**

 */
public class OSGiBundlePreferences extends MountedPreferences {

    protected Bundle bundle;

    protected BundleContext bc;

    OSGiPreferences sysNode;

    OSGiUsersPreferences usersNode;

    ServiceTracker psTracker;

    PreferencesService ps;

    String SYS_NAME = "sys";

    String USERS_NAME = "users";

    public OSGiBundlePreferences(Bundle bundle) {
        super();
        this.bundle = bundle;
        this.bc = Util.getBundleContext(bundle);
        if (bc == null) {
            Activator.log.debug("No BC for " + Util.getBundleName(bundle));
        } else {
            psTracker = new ServiceTracker(bc, PreferencesService.class.getName(), null) {

                public Object addingService(ServiceReference sr) {
                    Object obj = super.addingService(sr);
                    ps = (PreferencesService) obj;
                    mountService();
                    return obj;
                }

                public void removedService(ServiceReference sr, Object service) {
                    ps = null;
                    unmountService();
                    super.removedService(sr, service);
                }
            };
        }
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void open() {
        if (bundle == null) {
            throw new IllegalStateException("Bundle = null");
        }
        if (psTracker != null) {
            psTracker.open();
        }
    }

    public void close() {
        if (psTracker != null) {
            int state = bundle.getState();
            if (0 != (state & (Bundle.ACTIVE | Bundle.STARTING | Bundle.STOPPING))) {
                try {
                    Activator.log.debug("close tracker for " + bundle + ", state=" + state);
                    psTracker.close();
                } catch (Exception e) {
                    Activator.log.debug("Failed to close tracker", e);
                }
            } else {
                Activator.log.debug("skip tracker close since state=" + state);
            }
        }
        bundle = null;
        bc = null;
        psTracker = null;
        ps = null;
        sysNode = null;
        usersNode = null;
    }

    void unmountService() {
        if (sysNode != null) {
            unmount(SYS_NAME);
            sysNode = null;
        }
        if (usersNode != null) {
            unmount(USERS_NAME);
            usersNode = null;
        }
    }

    void mountService() {
        if (ps != null) {
            if (sysNode == null) {
                sysNode = new OSGiPreferences(null, ps.getSystemPreferences());
                mount(sysNode, SYS_NAME);
            }
            if (usersNode == null) {
                usersNode = new OSGiUsersPreferences(null, ps);
                mount(usersNode, USERS_NAME);
            }
        } else {
            Activator.log.warn("mount failed, no PreferencesService, " + Util.getBundleName(bundle));
        }
    }
}
