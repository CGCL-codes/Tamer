package net.sf.crispy.impl.corba;

import net.sf.crispy.impl.MiniServer;
import net.sf.crispy.impl.ServiceManager;
import net.sf.crispy.impl.StaticCorbaProxy;
import net.sf.crispy.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;

public class MiniCorbaServer implements MiniServer {

    public static final int PORT = 1057;

    public static final String HOST = "127.0.0.1";

    protected static final Log log = LogFactory.getLog(MiniCorbaServer.class);

    private static boolean isStarted = false;

    private int port = PORT;

    private String host = HOST;

    private static Process orbdProcess = null;

    private ORB orb = null;

    private POA rootpoa = null;

    private org.omg.CORBA.Object namingContext = null;

    public MiniCorbaServer() {
        init();
    }

    public MiniCorbaServer(int pvPort) {
        port = pvPort;
        init();
    }

    public MiniCorbaServer(String pvHost, int pvPort) {
        if (pvHost != null && pvHost.length() > 0) {
            host = pvHost;
        }
        port = pvPort;
        init();
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    private void init() {
        String lvJrePath = System.getProperty("java.home") + "/bin/orbd -ORBInitialPort " + port + " -ORBInitialHost " + host + " -port " + port;
        try {
            if (orbdProcess == null) {
                System.out.println("ORB-Path: " + lvJrePath);
                Runtime lvRuntime = Runtime.getRuntime();
                orbdProcess = lvRuntime.exec(lvJrePath);
                log.info("ORBD is started on port: " + port);
                System.out.println("ORB-Process: " + orbdProcess);
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Error by starting ORBD by path " + lvJrePath + " - " + e, e);
            }
            if (ServiceManager.DEBUG_MODE_ON) {
                e.printStackTrace();
            }
        }
        try {
            if (isStarted == false) {
                String args[] = new String[] { "-ORBInitialPort", Integer.toString(port), "-ORBInitialHost", host };
                orb = ORB.init(args, null);
                log.info("ORB is init on host: " + host + " on port: " + port);
                rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
                rootpoa.the_POAManager().activate();
                log.info("RootPOA: " + rootpoa);
                namingContext = orb.resolve_initial_references("NameService");
                log.info("NamingContext: " + namingContext);
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Error by init MiniCorbaServer: " + e, e);
            }
            if (ServiceManager.DEBUG_MODE_ON) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            if (isStarted == false) {
                Thread lvThread = new Thread(new Runnable() {

                    public void run() {
                        orb.run();
                    }
                });
                lvThread.start();
                isStarted = true;
                log.info("MiniCorbaServer is on host: " + host + " on port: " + port + " started.");
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Error by starting the MiniCorbaServer: " + e, e);
            }
            if (ServiceManager.DEBUG_MODE_ON) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            if (orbdProcess != null) {
                orbdProcess.destroy();
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Error by stopping the ORBD: " + e, e);
            }
        }
        try {
            orb.destroy();
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Error by stopping MiniCorbaServer: " + e, e);
            }
        }
        isStarted = false;
    }

    public void addService(String pvServiceInterface, String pvServiceObject) {
        try {
            Object lvServiceObject = Util.createObject(pvServiceObject);
            addService(pvServiceInterface, lvServiceObject);
        } catch (Exception e) {
            if (ServiceManager.DEBUG_MODE_ON) {
                e.printStackTrace();
            }
        }
    }

    public void addService(String pvLookName, Object pvServiceObject) {
        try {
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference((Servant) pvServiceObject);
            NamingContextExt ncRef = NamingContextExtHelper.narrow(namingContext);
            String lvBindName = pvLookName.replaceAll("\\.", "_");
            if (log.isInfoEnabled()) {
                log.info("Bind Service with name: " + lvBindName + " and Service-Object: " + pvServiceObject);
            }
            NameComponent path[] = ncRef.to_name(lvBindName);
            org.omg.CORBA.Object href = (org.omg.CORBA.Object) StaticCorbaProxy.createWithHelperCorbaObject(pvLookName, ref);
            ncRef.rebind(path, href);
        } catch (Exception e) {
            if (ServiceManager.DEBUG_MODE_ON) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Util.initJdkLogger();
        MiniServer lvMiniServer = new MiniCorbaServer();
        lvMiniServer.start();
    }
}
