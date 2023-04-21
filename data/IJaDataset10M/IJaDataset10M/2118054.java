package org.acplt.oncrpc.apps.jportmap;

import java.io.IOException;
import java.net.InetAddress;
import org.acplt.oncrpc.*;
import org.acplt.oncrpc.server.*;

/**
 * The class <code>OncRpcEmbeddedPortmap</code> provides an embeddable
 * portmap service, which is automatically started in its own thread if
 * the (operating) system does not already provide the portmap service.
 * If an embedded portmap service is started it will stop only after the
 * last ONC/RPC program has been deregistered.
 *
 * @version $Revision: 1.2 $ $Date: 2003/08/14 08:00:08 $ $State: Exp $ $Locker:  $
 * @author Harald Albrecht
 */
public class OncRpcEmbeddedPortmap {

    /**
     * Constructs an embeddable portmap service of class
     * <code>OncRpcEmbeddedPortmap</code> and starts the service if no
     * other (external) portmap service is available. This constructor is
     * the same as <code>OncRpcEmbeddedPortmap</code> calling with a
     * timeout of 3 seconds.
     *
     * <p>The constructor starts the portmap service in its own thread and
     * then returns.
     *
     * @see OncRpcEmbeddedPortmap#embeddedPortmapInUse
     */
    public OncRpcEmbeddedPortmap() throws OncRpcException, IOException {
        this(3000);
    }

    /**
     * Constructs an embeddable portmap service of class
     * <code>OncRpcEmbeddedPortmap</code> and starts the service if no
     * other (external) portmap service is available.
     *
     * <p>The constructor starts the portmap service in its own thread and
     * then returns.
     *
     * @param checkTimeout timeout in milliseconds to wait before assuming
     *   that no portmap service is currently available.
     *
     * @see OncRpcEmbeddedPortmap#embeddedPortmapInUse
     */
    public OncRpcEmbeddedPortmap(int checkTimeout) throws OncRpcException, IOException {
        if (!isPortmapRunning(checkTimeout)) {
            embeddedPortmap = new embeddedjportmap();
            embeddedPortmapThread = new OncRpcEmbeddedPortmapThread(embeddedPortmap);
            embeddedPortmap.serviceThread = embeddedPortmapThread;
            embeddedPortmapThread.start();
        }
    }

    /**
     * Indicates whether a portmap service (regardless whether it's supplied
     * by the operating system or an embedded portmap service) is currently
     * running. This method will check for 3 seconds for an answer from a
     * portmap before assuming that no one exists.
     *
     * @return <code>true</code>, if a portmap service (either external or
     *   embedded) is running and can be contacted.
     */
    public static boolean isPortmapRunning() {
        return isPortmapRunning(3000);
    }

    /**
     * Indicates whether a portmap service (regardless whether it's supplied
     * by the operating system or an embedded portmap service) is currently
     * running.
     *
     * @param checkTimeout timeout in milliseconds to wait before assuming
     *   that no portmap service is currently available.
     *
     * @return <code>true</code>, if a portmap service (either external or
     *   embedded) is running and can be contacted.
     */
    public static boolean isPortmapRunning(int checkTimeout) {
        boolean available = false;
        try {
            OncRpcPortmapClient portmap = new OncRpcPortmapClient(InetAddress.getByName("127.0.0.1"));
            portmap.getOncRpcClient().setTimeout(checkTimeout);
            portmap.ping();
            available = true;
        } catch (OncRpcException e) {
        } catch (IOException e) {
        }
        return available;
    }

    /**
     * Indicates whether the embedded portmap service is in use.
     *
     * @return <code>true</code>, if embedded portmap service is currently
     *   used.
     */
    public boolean embeddedPortmapInUse() {
        return embeddedPortmapThread != null;
    }

    /**
     * Returns the thread object running the embedded portmap service.
     *
     * @return Thread object or <code>null</code> if no embedded portmap
     *   service has been started.
     */
    public Thread getEmbeddedPortmapServiceThread() {
        return embeddedPortmapThread;
    }

    /**
     * Returns object implementing the embedded portmap service.
     *
     * @return Embedded portmap object or <code>null</code> if no
     *   embedded portmap service has been started.
     */
    public jportmap getEmbeddedPortmap() {
        return embeddedPortmap;
    }

    /**
     * Stop the embedded portmap service if it is running. Normaly you should
     * not use this method except you need to force the embedded portmap
     * service to terminate. Under normal conditions the thread responsible
     * for the embedded portmap service will terminate automatically after the
     * last ONC/RPC program has been deregistered.
     *
     * <p>This method
     * just signals the portmap thread to stop processing ONC/RPC portmap
     * calls and to terminate itself after it has cleaned up after itself.
     */
    public void shutdown() {
        OncRpcServerStub portmap = embeddedPortmap;
        if (portmap != null) {
            portmap.stopRpcProcessing();
        }
    }

    /**
     * Portmap object acting as embedded portmap service or <code>null</code>
     * if no embedded portmap service is necessary because the operating
     * system already supplies one or another portmapper is already running.
     */
    protected embeddedjportmap embeddedPortmap;

    /**
     * References thread object running the embedded portmap service.
     */
    protected Thread embeddedPortmapThread;

    /**
     * Extend the portmap service so that it automatically takes itself out
     * of service when the last ONC/RPC programs is deregistered.
     */
    class embeddedjportmap extends jportmap {

        /**
         * Creates a new instance of an embeddable portmap service.
         */
        public embeddedjportmap() throws IOException, OncRpcException {
        }

        /**
         * Thread running the embedded portmap service.
         */
        protected Thread serviceThread;

        /**
         * Deregister all port settings for a particular (program, version) for
         * all transports (TCP, UDP, ...). This method basically falls back to
         * the implementation provided by the <code>jrpcgen</code> superclass,
         * but checks whether there are other ONC/RPC programs registered. If
         * not, it signals itself to shut down the portmap service.
         *
         * @param params (program, version) to deregister. The protocol and port
         *   fields are not used.
         *
         * @return <code>true</code> if deregistration succeeded.
         */
        XdrBoolean unsetPort(OncRpcServerIdent params) {
            XdrBoolean ok = super.unsetPort(params);
            if (ok.booleanValue()) {
                boolean onlyPmap = true;
                int size = servers.size();
                for (int idx = 0; idx < size; ++idx) {
                    if (((OncRpcServerIdent) servers.elementAt(idx)).program != PMAP_PROGRAM) {
                        onlyPmap = false;
                        break;
                    }
                }
                if (onlyPmap && (serviceThread != null)) {
                    stopRpcProcessing();
                }
            }
            return ok;
        }
    }

    /**
     * The class <code>OncRpcEmbeddedPortmapThread</code> implements a thread
     * which will run an embedded portmap service.
     */
    class OncRpcEmbeddedPortmapThread extends Thread {

        /**
         * Construct a new embedded portmap service thread and associate
         * it with the portmap object to be used as the service. The service
         * is not started yet.
         */
        public OncRpcEmbeddedPortmapThread(embeddedjportmap portmap) {
            super("embedded portmap service thread");
            this.portmap = portmap;
        }

        /**
         * Run the embedded portmap service thread, starting dispatching
         * of all portmap transports until we get the signal to shut down.
         */
        public void run() {
            try {
                portmap.run(portmap.transports);
            } catch (Exception e) {
            }
            portmap.close(portmap.transports);
            portmap.serviceThread = null;
        }

        /**
         * The embedded portmap service object this thread belongs to. The
         * service object implements the ONC/RPC dispatcher and the individual
         * remote procedures for a portmapper).
         */
        private embeddedjportmap portmap;
    }
}
