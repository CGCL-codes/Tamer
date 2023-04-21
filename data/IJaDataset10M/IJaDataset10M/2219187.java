package net.sf.jimo.irc.impl;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import net.sf.jimo.im.api.IMConstants;
import net.sf.jimo.im.api.IMListener;
import net.sf.jimo.im.api.IMProtocol;
import net.sf.jimo.irc.api.IRCConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

public class IRCActivator implements BundleActivator {

    public static IRCActivator INSTANCE;

    Map mapProtocol = Collections.synchronizedMap(new HashMap());

    Map mapReg = Collections.synchronizedMap(new HashMap());

    public IRCActivator() {
        INSTANCE = this;
    }

    public void start(final BundleContext context) throws Exception {
        Dictionary properties = new Hashtable();
        properties.put(Constants.SERVICE_PID, IRCConstants.PID_IRCPROTOCOLFACTORY);
        context.registerService(ManagedServiceFactory.class.getName(), new ManagedServiceFactory() {

            public void deleted(String pid) {
                IRCProtocol protocol = (IRCProtocol) mapProtocol.remove(pid);
                if (protocol != null && protocol.isConnected()) {
                    try {
                        protocol.disconnect();
                    } catch (IllegalStateException e) {
                    }
                    ServiceRegistration registration = (ServiceRegistration) mapReg.get(pid);
                    if (registration != null) {
                        registration.unregister();
                        mapReg.remove(pid);
                    }
                }
            }

            public String getName() {
                return IRCConstants.PROTOCOL_FACTORYNAME;
            }

            public void updated(String pid, Dictionary properties) throws ConfigurationException {
                IRCProtocol protocol;
                if (properties == null) return;
                if (!mapProtocol.containsKey(pid)) {
                    protocol = new IRCProtocol();
                    mapProtocol.put(pid, protocol);
                } else {
                    protocol = (IRCProtocol) mapProtocol.get(pid);
                }
                try {
                    if (protocol.isConnected()) {
                        protocol.disconnect();
                    }
                } catch (IllegalStateException e) {
                }
                ServiceReference ref = context.getServiceReference(IMListener.class.getName());
                String username = (String) properties.get(IMConstants.IM_ACCOUNTNAME);
                String password = (String) properties.get(IMConstants.IM_ACCOUNTPASSWORD);
            }
        }, properties);
    }

    ;

    public void stop(BundleContext context) throws Exception {
        for (Iterator iter = mapProtocol.values().iterator(); iter.hasNext(); ) {
            IMProtocol element = (IMProtocol) iter.next();
        }
    }
}
