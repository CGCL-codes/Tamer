package aeolus.edge;

import java.util.*;
import net.jxta.discovery.*;
import net.jxta.document.*;
import net.jxta.peergroup.*;
import net.jxta.protocol.*;

public class ServiceDiscovery implements DiscoveryListener, Runnable {

    private volatile boolean running = false, stopping;

    private int pollTimeout, remoteResultsThreshold;

    private DiscoveryService discovery;

    private final Object shutdownLock = new Object();

    private ServiceDiscoveryEventHandler eventHandler;

    private String name;

    private Thread thread;

    public ServiceDiscovery(PeerGroup netPeerGroup, ServiceDiscoveryEventHandler eventHandler, String name, int pollTimeout, int remoteResultsThreshold) {
        discovery = netPeerGroup.getDiscoveryService();
        this.eventHandler = eventHandler;
        this.name = name;
        this.pollTimeout = pollTimeout;
        this.remoteResultsThreshold = remoteResultsThreshold;
        discovery.addDiscoveryListener(this);
    }

    protected void finalize() {
        discovery.removeDiscoveryListener(this);
    }

    public boolean start(boolean flushCache) {
        synchronized (shutdownLock) {
            if (running) return false;
            stopping = false;
            running = true;
        }
        if (flushCache) {
            try {
                @SuppressWarnings("unchecked") Enumeration en = discovery.getLocalAdvertisements(DiscoveryService.ADV, null, null);
                while (en.hasMoreElements()) discovery.flushAdvertisement((Advertisement) en.nextElement());
            } catch (Exception e) {
                System.err.println("error flushing local advertisements:");
                e.printStackTrace();
            }
        }
        thread = new Thread(this);
        thread.start();
        return true;
    }

    public boolean stop() {
        synchronized (shutdownLock) {
            if (!running) return false;
            stopping = true;
            thread.interrupt();
            while (running) {
                try {
                    shutdownLock.wait();
                } catch (InterruptedException ie) {
                }
            }
        }
        return true;
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {
        while (!stopping) {
            try {
                @SuppressWarnings("unchecked") Enumeration en = discovery.getLocalAdvertisements(DiscoveryService.ADV, "Name", "JXTASPEC:" + name);
                while (en.hasMoreElements()) eventHandler.handleServiceDiscoveryEvent((ModuleSpecAdvertisement) en.nextElement());
            } catch (Exception e) {
                System.err.println("error querying for local advertisements:");
                e.printStackTrace();
            }
            discovery.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", "JXTASPEC:" + name, remoteResultsThreshold, null);
            try {
                Thread.sleep(pollTimeout);
            } catch (InterruptedException ie) {
            }
        }
        synchronized (shutdownLock) {
            running = false;
            shutdownLock.notify();
        }
    }

    public void discoveryEvent(DiscoveryEvent event) {
        if (running) {
            DiscoveryResponseMsg res = event.getResponse();
            @SuppressWarnings("unchecked") Enumeration en = res.getAdvertisements();
            while (en.hasMoreElements()) {
                ModuleSpecAdvertisement mdsadv = (ModuleSpecAdvertisement) en.nextElement();
                if (mdsadv.getName().equals("JXTASPEC:" + name)) eventHandler.handleServiceDiscoveryEvent(mdsadv);
            }
        }
    }
}
