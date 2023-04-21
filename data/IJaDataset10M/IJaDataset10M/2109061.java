package org.sun.dbe.p2p;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import net.fada.FadaException;
import net.fada.directory.FadaInterface;
import net.fada.directory.FadaLookupLocator;
import net.fada.directory.FadaRemote;
import org.apache.log4j.Logger;
import org.dbe.servent.core.DefaultCoreComponent;

/**
 * 
 * @author bob
 */
public class AutoDiscoverNode extends DefaultCoreComponent {

    /** Fada Interface */
    private FadaInterface fp;

    /** fadaURL */
    private String fadaURL;

    /** Sync object */
    private Object sync = new Object();

    /** Fada discoverer thread */
    private FadaDiscoverer discoverer;

    /** if we want to stop */
    private boolean shutdown = false;

    /** true if the FadaInterface is valid */
    private boolean valid = false;

    /** logger */
    private Logger logger = Logger.getLogger(this.getClass());

    private FadaInterface localhostFadaProxy;

    private Thread discovererThread;

    public static FadaRemote localhostFadaRemote;

    /**
	 * 
	 */
    public AutoDiscoverNode() {
    }

    /**
	 * Return the FadaInterface. If the FadaInterface has not been found yet, it
	 * will wait.
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws InterruptedException
	 *             means that the thread was interrupted before finding the
	 *             FadaInterface
	 */
    public synchronized FadaInterface getFadaInterface() throws InterruptedException {
        if ((discoverer == null) || (discovererThread == null)) {
            startDiscoverer();
        }
        FadaInterface fi = null;
        try {
            fi = new FadaLookupLocator(new URL(fadaURL)).getRegistrar(Thread.currentThread().getContextClassLoader());
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (FadaException e1) {
            e1.printStackTrace();
        }
        valid = true;
        logger.info("Fada Node found at " + fadaURL);
        if (fadaURL.equals("http://localhost:2002")) {
            if (fp == null) {
                fp = localhostFadaProxy;
                logger.debug("Setting fp to " + localhostFadaProxy);
            }
        } else {
            fp = fi;
            logger.debug("Setting fp to " + fi);
        }
        valid = true;
        if ((!valid) && (!shutdown)) {
            synchronized (sync) {
                try {
                    while (fp == null) {
                        sync.wait();
                        logger.debug("Waiting for the discovery of the fada node");
                    }
                } catch (InterruptedException e) {
                    if (fp == null) {
                        throw e;
                    }
                }
            }
        }
        logger.debug("Returning fp, which is " + fp);
        return fp;
    }

    /**
	 * Find the node again
	 * 
	 */
    public void renew() {
        valid = false;
        synchronized (discoverer) {
            discoverer.notifyAll();
        }
    }

    /**
	 * Shutdown the Node
	 * 
	 */
    public void shutdown() {
        shutdown = true;
        synchronized (discoverer) {
            discoverer.notifyAll();
        }
    }

    /**
	 * Start the ServentFadaNode
	 * 
	 */
    public void start() {
        try {
            FadaLookupLocator fll = new FadaLookupLocator(new URL("http://localhost:2002"));
            localhostFadaProxy = fll.getRegistrar();
        } catch (FadaException e) {
        } catch (ClassNotFoundException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        this.fadaURL = config.getAttribute("fadaUrl");
        if (this.fadaURL == null) {
            this.fadaURL = "http://localhost:2002";
        }
        logger.debug("fadaURL: " + this.fadaURL);
        startDiscoverer();
    }

    /**
	 * 
	 */
    private void startDiscoverer() {
        discoverer = new FadaDiscoverer();
        discovererThread = new Thread(discoverer, "P2P Discoverer");
        discovererThread.start();
    }

    /**
	 * 
	 */
    public void restart() {
        stop();
        start();
    }

    /**
	 * 
	 */
    public void stop() {
        shutdown();
    }

    /**
	 * If Fada node goes down... we need to wait for it.
	 * 
	 * @author bob
	 */
    private class FadaDiscoverer implements Runnable {

        /** Time to wait to try again */
        private static final long TIME_TO_WAIT = 2000;

        /**
		 * @see java.lang.Runnable#run()
		 */
        public void run() {
            while (!shutdown) {
                logger.debug("FadaDiscover wake up !!!!");
                while (!valid) {
                    logger.debug("Looking for Fada Node at " + fadaURL);
                    try {
                        FadaInterface fi = new FadaLookupLocator(new URL(fadaURL)).getRegistrar();
                        valid = true;
                        logger.info("Fada Node found at " + fadaURL);
                        if (fadaURL.equals("http://localhost:2002")) {
                            if (fp == null) {
                                fp = localhostFadaProxy;
                                logger.debug("Setting fp to " + localhostFadaProxy);
                            }
                        } else {
                            fp = fi;
                            logger.debug("Setting fp to " + fi);
                        }
                        valid = true;
                        synchronized (sync) {
                            sync.notifyAll();
                        }
                    } catch (MalformedURLException e) {
                        logger.error("Fada node will never be found at " + fadaURL, e);
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        logger.error("FADA class was not found. It is possible you have some problem accessing to the FADA IP", e);
                    } catch (IOException e) {
                        logger.error("I/O exception looking for Fada Node at " + fadaURL, e);
                    } catch (FadaException e) {
                        logger.error("Exception trying to locate Fada node at  " + fadaURL, e);
                    }
                    if (shutdown) {
                        break;
                    }
                    try {
                        Thread.sleep(TIME_TO_WAIT);
                    } catch (InterruptedException e1) {
                        logger.debug("FadaDiscoverer was interrupted");
                    }
                }
                if (!shutdown) {
                    logger.debug("FadaDiscover is sleeping...");
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            logger.debug("FadaDiscoverer was woken up");
                        }
                    }
                }
            }
            logger.debug("SHUTDOWN: FadaDiscoverer finished !");
            synchronized (sync) {
                sync.notifyAll();
            }
        }
    }
}
