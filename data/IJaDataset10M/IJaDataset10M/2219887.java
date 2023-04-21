package org.jives.implementors.network.jxse.rendezvous;

import java.lang.reflect.Constructor;
import lombok.Getter;
import lombok.Setter;
import org.jives.implementors.network.jxse.JXSEImplementor;
import org.jives.implementors.network.jxse.manager.MultiInstanceEndpoint;
import org.jives.implementors.network.jxse.utils.NetworkLog;

/**
 * Class which manages the connection of Rendezvous/Relays
 * 
 * @author simonesegalini
 */
public class RendezvousRelayManager extends MultiInstanceEndpoint implements Runnable {

    @Getter
    private String rendezvousName;

    private int tcpPort;

    @Getter
    private static String scriptName;

    @Getter
    private static String scriptMD5;

    @Getter
    @Setter
    private static String rendezvousID;

    private Thread r;

    /**
	 * The constructor.
	 * 
	 * @param mainThread
	 * 			  the caller thread
	 * @param rendezvousName
	 *            the name of the rendezvous/relay
	 * @param tcpPort
	 * 			  the Tcp port used
	 * @param scriptName
	 *            the name of the script executed
	 * @param scriptMD5
	 * 			  the MD5 of the whole Jives Script
	 */
    public RendezvousRelayManager(Thread mainThread, String rendezvousName, int tcpPort, String scriptName, String scriptMD5) {
        super(mainThread);
        this.rendezvousName = rendezvousName;
        this.tcpPort = tcpPort;
        RendezvousRelayManager.scriptName = scriptName;
        RendezvousRelayManager.scriptMD5 = scriptMD5;
    }

    /**
	 * Method used to start the network (a RendezvousRelayConnection)
	 */
    private void launchNetwork() {
        launchRendezvousRelay();
    }

    /**
	 * Method used to start a RendezvousRelayConnection
	 */
    private void launchRendezvousRelay() {
        try {
            if (endpoint == null) {
                endpointClass = loadClass(RendezvousRelayConnection.class.getCanonicalName(), true);
                Constructor<?> ctor = endpointClass.getConstructor(String.class, int.class);
                endpoint = ctor.newInstance(rendezvousName, tcpPort);
            }
            JXSEImplementor.getInstance().setRendezvous(true);
            invoke("init", new Class[0], new Object[0]);
            r = new Thread((Runnable) endpoint);
            r.start();
        } catch (Exception e) {
            String eMessage = "Unable to start rendezvous: " + e.toString() + " ";
            if (e.getCause() != null) {
                eMessage += e.getCause().getMessage();
            } else {
                eMessage += e.getMessage();
            }
            NetworkLog.logMsg(NetworkLog.LOG_FATAL, this, eMessage);
            e.printStackTrace();
        }
    }

    /**
	 * The run method, used to launch the network.
	 * 
	 * @see RendezvousRelayManager#launchNetwork
	 */
    @Override
    public void run() {
        this.launchNetwork();
    }

    @Override
    protected Thread getEndpointThread() {
        return r;
    }
}
