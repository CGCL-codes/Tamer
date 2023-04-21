package ucar.netcdf;

import ucar.util.Logger;
import ucar.util.RMILogger;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.AccessException;
import java.rmi.ServerException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Enumeration;
import java.rmi.ConnectException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * A UnicastRemoteObject implementation of NetcdfService.
 * @note NetcdfService is a placeholder directory service
 * for a more elaborate directory service,
 * hopefully to be provided later on.
 * @author $Author: rboller_cvs $
 * @version $Revision: 1.1 $ $Date: 2009/04/17 18:05:36 $
 */
public class NetcdfServer extends UnicastRemoteObject implements NetcdfService {

    public static void setLog(OutputStream out) {
        if (out != null) {
            logger_.logUpTo(Logger.DEBUG);
            logger_.setLog(out);
        } else {
            logger_.logUpTo(Logger.NOTICE);
            logger_.setLog(System.err);
            UnicastRemoteObject.setLog(out);
        }
    }

    public NetcdfServer(String[] exports, Registry registry) throws RemoteException, AlreadyBoundException {
        super();
        byName_ = new Hashtable();
        for (int ii = 0; ii < exports.length; ii++) export(exports[ii]);
        if (byName_.size() == 0) throw new IllegalArgumentException("No exports");
        if (registry != null) {
            registry_ = registry;
            registry.bind(SVC_NAME, this);
            logger_.logNotice(SVC_NAME + " bound in registry");
        }
    }

    public int ping() throws RemoteException {
        return 0;
    }

    public NetcdfRemoteProxy lookup(String dataSetName) throws RemoteException {
        final Entry entry = get(dataSetName);
        if (entry == null) throw new AccessException(dataSetName + " not available");
        AbstractNetcdf nc = null;
        try {
            nc = entry.getNetcdfFile();
        } catch (IOException ioe) {
            throw new ServerException("lookup", ioe);
        }
        return (NetcdfRemoteProxy) exportObject(new NetcdfRemoteProxyImpl(this, dataSetName, nc));
    }

    public String[] list() throws RemoteException {
        String[] ret = new String[byName_.size()];
        Enumeration ee = byName_.keys();
        for (int ii = 0; ee.hasMoreElements(); ii++) ret[ii] = (String) ee.nextElement();
        return ret;
    }

    public void export(File ff) {
        if (!ff.isFile()) throw new IllegalArgumentException(ff.getPath() + " not a File");
        Entry entry = new Entry(ff);
        String keyval = entry.keyValue();
        logger_.logDebug("Exporting " + ff + " as " + keyval);
        put(keyval, entry);
    }

    public void export(String path) {
        export(new File(path));
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (registry_ != null) {
            try {
                registry_.unbind(SVC_NAME);
            } catch (Exception ee) {
                logger_.logError("unbind: " + ee.getMessage());
            }
        }
        registry_ = null;
    }

    public static Registry startRegistry() throws RemoteException {
        logger_.logNotice("No registry, starting one");
        return LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    }

    public static Registry checkRegistry(Registry regis, int tryagain) throws RemoteException {
        if (regis == null) regis = startRegistry();
        NetcdfService existing = (NetcdfService) null;
        try {
            existing = (NetcdfService) regis.lookup(SVC_NAME);
        } catch (ConnectException ce) {
            if (--tryagain > 0) {
                return checkRegistry(startRegistry(), tryagain);
            }
            throw ce;
        } catch (NotBoundException nbe) {
            return regis;
        }
        try {
            existing.ping();
        } catch (ConnectException ce) {
            try {
                logger_.logNotice("unbinding dead registry entry");
                regis.unbind(SVC_NAME);
            } catch (NotBoundException nbe) {
            }
        }
        return regis;
    }

    public static void main(String args[]) {
        System.setSecurityManager(new RMISecurityManager());
        Registry regis = (Registry) null;
        try {
            regis = checkRegistry(LocateRegistry.getRegistry(), 2);
        } catch (Exception ee) {
            PrintStream ps = getLog();
            if (ps == null) ps = System.err;
            ps.println("NetcdfServer: error getting registry: " + ee.getMessage());
            ee.printStackTrace(ps);
            System.exit(1);
        }
        try {
            NetcdfServer svc = new NetcdfServer(args, regis);
        } catch (Throwable ee) {
            PrintStream ps = getLog();
            if (ps == null) ps = System.err;
            ps.println("NetcdfServer err: " + ee.getMessage());
            ee.printStackTrace(ps);
            System.exit(1);
        }
    }

    synchronized void _release(String keyval) {
        final Entry entry = (Entry) byName_.get(keyval);
        if (entry != null) entry.releaseNetcdfFile();
    }

    /**
	 * Gets the Entry associated with the specified name.
	 * @param dataSetName the name 
	 * @return the Entry, or null if not found
	 */
    private Entry get(String dataSetName) {
        return (Entry) byName_.get(dataSetName);
    }

    /**
	 * Puts the specified element into the Dictionary, using its
	 * keyValue() as key. 
	 * The element may be retrieved by doing a get() with the key value.
	 * name.  The element cannot be null.
	 * @param entry the new entry;
	 * @exception NullPointerException If the value of the specified
	 * element is null.
	 */
    private synchronized void put(String keyval, Entry entry) {
        byName_.put(keyval, entry);
    }

    /**
	 * @serial
	 */
    private Hashtable byName_;

    /**
	 * @serial
	 */
    private Registry registry_;

    static final RMILogger logger_ = new RMILogger();

    class Entry {

        final File dirent;

        NetcdfFile nc;

        int refcount;

        Entry(File ff) {
            this.dirent = ff;
            nc = (NetcdfFile) null;
            refcount = 0;
        }

        String keyValue() {
            final String name = dirent.getName();
            final int index = name.indexOf('.');
            return name.substring(0, index).intern();
        }

        /**
	 * Open entry.nc.
	 */
        private synchronized void open(boolean readonly) throws IOException {
            if (nc != null) throw new IllegalArgumentException("dataSet " + keyValue() + " already open");
            nc = new NetcdfFile(dirent, readonly);
        }

        /**
	 * Return entry.nc, opening if necessary.
	 * Increments reference count.
	 */
        synchronized NetcdfFile getNetcdfFile() throws IOException {
            if (nc == null) open(true);
            refcount++;
            logger_.logDebug("refcount: " + refcount);
            return nc;
        }

        /**
	 * Close entry.nc and delete any references to it,
	 * making it available for garbage collection.
	 */
        private synchronized void close() {
            if (nc != null) {
                logger_.logDebug("closing: " + nc.getFile());
                try {
                    nc.close();
                } catch (IOException ioe) {
                }
                nc = (NetcdfFile) null;
                refcount = 0;
            }
        }

        /**
	 * Decrement the reference count and close if no
	 * more references.
	 */
        synchronized void releaseNetcdfFile() {
            if (refcount > 0) refcount--;
            if (refcount == 0) close();
        }
    }
}
