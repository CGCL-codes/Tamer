package org.rr.jsendfile.app;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.rr.jsendfile.util.ProgInfo;
import org.rr.jsendfile.util.Sysinfo;
import org.rr.jsendfile.util.net.ServerListener;
import org.rr.jsendfile.util.net.ServerLogWriter;
import org.rr.jsendfile.util.typ.HostAddress;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * <pre>
 * 
 *  
 *   
 *    Der eigendlich Server: 
 *    
 *     usage: sendfiled 
 *    
 *     -s Spoolfiledirectory
 *     -p port
 *     -V Version
 *     -h help
 *    
 *     die jsendfile-server.conf erm�glicht das &quot;�berschreiben&quot; der Parameter. Alle Werte die in der config 
 *     definiert wurden gelten unabh�ngig davon, was an parametern �bergeben wurde. Bei der Einrichtung
 *     des Saftserver als Service unter Windows ist die �bergabe von Parametern so ohne weiteres nicht
 *     m�glich. Wird der Server manuell eingerichtet, so kann die config datei leer gelassen werden und
 *     der Server individuell �ber parameter gestuert werden.
 *    
 *   
 *  
 * </pre>
 */
public class SendfiledWrapper implements WrapperListener {

    private int port = 0;

    private boolean startable = true;

    private File file = new File(Sysinfo.getCurPath());

    private File spoolPath = new File(this.getCurPath() + "/spool/");

    /** Creates a new instance of Sendfiled */
    public SendfiledWrapper() {
        new ServerLogWriter("Sendfiled v" + ProgInfo.VERSION + " started");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WrapperManager.start(new SendfiledWrapper(), args);
    }

    public void parseParameters(String[] args) {
        LongOpt[] longopts = new LongOpt[4];
        int c;
        longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longopts[1] = new LongOpt("port", LongOpt.REQUIRED_ARGUMENT, null, 'p');
        longopts[2] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V');
        longopts[3] = new LongOpt("spoolfiledirectory", LongOpt.REQUIRED_ARGUMENT, null, 's');
        Getopt g = new Getopt("Sendfiled", args, "-:s:p:hV;", longopts);
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'h':
                    startable = false;
                    System.out.println("usage: sedfiled [[-p Portnumber] [-s Spoolfiledirectory] [-t Tempfiledirectory]]");
                    System.out.println("");
                    System.out.println("options:   -p Port");
                    System.out.println("           -s Spoolfiledirectory");
                    System.out.println("           -h Help");
                    System.out.println("           -V Version");
                    System.out.println("\nlong:     --spoolfiledirectory, --tempfiledirectory, --port, --help, --version");
                    break;
                case 'V':
                    startable = false;
                    System.out.println("Sendfiled v" + ProgInfo.VERSION + " From " + ProgInfo.AUTHOR);
                    break;
                case 'p':
                    try {
                        port = Integer.valueOf(g.getOptarg()).intValue();
                    } catch (Exception e) {
                    }
                    break;
                case 's':
                    String argument1 = g.getOptarg();
                    if (new File(argument1).exists()) {
                        spoolPath = new File(argument1);
                    }
                    break;
                default:
                    startable = false;
                    new ServerLogWriter("Error on Argument");
                    break;
            }
        }
        try {
            if (Sysinfo.getUserAllowConfigFile() == null || Sysinfo.getHostAllowConfigFile() == null || Sysinfo.getServerConfigFile() == null) {
                new ServerLogWriter("One of the config files could not be found");
                WrapperManager.stop(Sysinfo.RET_CONFIGFILE_NOT_FOUND);
            }
            Properties props = new Properties();
            File configFile = Sysinfo.getServerConfigFile();
            InputStream in = new FileInputStream(Sysinfo.getServerConfigFile());
            props.load(in);
            String propSpoolfiledirectory = props.getProperty("spoolfiledirectory");
            String propPort = props.getProperty("port");
            if (propSpoolfiledirectory != null && propSpoolfiledirectory.length() > 0) {
                this.spoolPath = new File(propSpoolfiledirectory);
            }
            if (!this.spoolPath.exists()) {
                new ServerLogWriter("Spoolfilepath " + this.spoolPath + " did not exists");
                return;
            }
            if (propPort != null && propPort.length() > 0) {
                try {
                    this.port = Integer.parseInt(propPort);
                } catch (NumberFormatException nfe) {
                }
            }
            if (port == 0) {
                port = new HostAddress().getPort();
            }
        } catch (Exception e) {
        }
        if (startable) {
            this.startServer();
        }
    }

    /** Starts the Server (ServerListner) */
    public void startServer() {
        ServerListener ServerListnerThread = new ServerListener();
        ServerListnerThread.startServer(port, spoolPath);
    }

    /** Returns the current path of this */
    public String getCurPath() {
        return file.toString();
    }

    /**
     * The start method is called when the WrapperManager is signaled by the native wrapper code that it can start its application. This method call is expected
     * to return, so a new thread should be launched if necessary.
     * 
     * This method do the same like the original main method but starts the server as an own thread so this method will be return directly with null.
     * 
     * @param args List of arguments used to initialize the application.
     * 
     * @return Any error code if the application should exit on completion of the start method. If there were no problems then this method should return null.
     */
    public Integer start(final String[] args) {
        new Thread(new Runnable() {

            public void run() {
                new SendfiledWrapper().parseParameters(args);
            }
        }).start();
        return null;
    }

    /**
     * Called when the application is shutting down. The Wrapper assumes that this method will return fairly quickly. If the shutdown code code could
     * potentially take a long time, then WrapperManager.signalStopping() should be called to extend the timeout period. If for some reason, the stop method can
     * not return, then it must call WrapperManager.stopped() to avoid warning messages from the Wrapper.
     * 
     * @param exitCode The suggested exit code that will be returned to the OS when the JVM exits.
     * 
     * @return The exit code to actually return to the OS. In most cases, this should just be the value of exitCode, however the user code has the option of
     *         changing the exit code if there are any problems during shutdown.
     */
    public int stop(int exitCode) {
        new ServerLogWriter(ServerListener.listenerThreadGroup.activeCount() + " Threads running on Shutdown");
        if (exitCode == 0) {
            Sysinfo.setProcessStatus(-1);
        } else {
            Sysinfo.setProcessStatus(exitCode);
        }
        while (ServerListener.listenerThreadGroup.activeCount() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                new ServerLogWriter("Error waiting at server shutdown");
            }
        }
        return exitCode;
    }

    /**
     * Called whenever the native wrapper code traps a system control signal against the Java process. It is up to the callback to take any actions necessary.
     * Possible values are: WrapperManager.WRAPPER_CTRL_C_EVENT, WRAPPER_CTRL_CLOSE_EVENT, WRAPPER_CTRL_LOGOFF_EVENT, or WRAPPER_CTRL_SHUTDOWN_EVENT
     * 
     * @param event The system control signal.
     */
    public void controlEvent(int event) {
        if (WrapperManager.isControlledByNativeWrapper()) {
        } else {
            if ((event == WrapperManager.WRAPPER_CTRL_C_EVENT) || (event == WrapperManager.WRAPPER_CTRL_CLOSE_EVENT) || (event == WrapperManager.WRAPPER_CTRL_SHUTDOWN_EVENT)) {
                WrapperManager.stop(0);
            }
        }
    }
}
