package com.teletalk.jserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Properties;

/**
 * The JServerLauncher is a bootstrap class used to load a JServer. This class makes it possible to 
 * completely reload/restart a JServer through the use of a special class loader.<br>
 * <br>
 * To use the JServerLauncher to start a JServer, simply use JServerLauncher as main class to run the 
 * application and pass the same arguments that would be used if JServer was the main class. Below is an example of 
 * this: <br>
 * <br>
 * <code>java -cp .;jserver.jar com.teletalk.jserver.JServerLauncher -name MyServer</code>.<br>
 * <br>
 * <br>
 *  JServerLauncher may also be used to launch JServers which use a custom main class. When doing so, the switch 
 * <code>-class</code> must be specified along with the fully qualified name of the custom main class. For instance, if a server is normally 
 * started with the line<br>
 * <br>
 * <code>java -cp .;jserver.jar mypackage.MyMainClass arg1 arg2</code><br>
 * <br>
 * that line must be changed to<br>
 * <br>
 * <code>java -cp .;jserver.jar com.teletalk.jserver.JServerLauncher -class mypackage.MyMainClass arg1 arg2</code>.<br>
 * <br>
 * <br>
 * To make the JServerLauncher print which classes are loaded to standard out, the system property <code>JServer.classloader.verbose</code> 
 * must be set to <code>true</code>. Example:<br>
 * <br>
 * <code>java -Dcom.teletalk.jserver.JServerClassLoader.verbose=true -cp .;jserver.jar com.teletalk.jserver.JServerLauncher mypackage.MyMainClass arg1 arg2</code>.<br>
 * <br>
 * To force the use of a System.exit(0) call when shutting down the server, the property com.teletalk.jserver.JServerLauncher.callSystemExit must be set 
 * to true (-Dcom.teletalk.jserver.JServerLauncher.callSystemExit=true).
 * 
 * @author Tobias L�fstrand
 *
 * @since 1.1 Build 545
 */
public final class JServerLauncher {

    private static final String callSystemExitSystemPropertyName = "com.teletalk.jserver.JServerLauncher.callSystemExit";

    private static final boolean callSystemExit;

    private static String mainClassName = null;

    private static String[] mainClassArgs = null;

    private static boolean canRestart = true;

    private static boolean restartOrStopCommandReceived = false;

    private static JServerClassLoader serverClassLoader = null;

    private static final Method restartMethod;

    private static final String JServerClassName = "com.teletalk.jserver.JServer";

    private static Class JServerClass = null;

    static {
        final Properties p = System.getProperties();
        final Iterator it = p.keySet().iterator();
        String key;
        boolean useSystemExitFound = false;
        while (it.hasNext()) {
            key = (String) it.next();
            if (key.trim().equalsIgnoreCase(callSystemExitSystemPropertyName)) {
                String value = (String) p.get(key);
                if ((value != null) && (value.trim().equalsIgnoreCase("true"))) {
                    useSystemExitFound = true;
                }
            }
        }
        callSystemExit = useSystemExitFound;
        Method m = null;
        try {
            m = ((Class) JServerLauncher.class).getMethod("restart", null);
        } catch (Exception e) {
            System.out.println("Error while attempting to instantiate restart method! (" + e.toString() + ")");
        } finally {
            restartMethod = m;
        }
    }

    /**
	 * The famous main method that gets it all moving.
	 */
    public static void main(String[] args) {
        launcherMain(args);
    }

    /**
	 * Start the server. This method conforms to the interface defined by <a href="../../../../utilities/JService/JService.html">JService</a> 
	 * for starting a server as a Windows NT service.
	 */
    public static void startService(String args[]) {
        launcherMain(args);
    }

    /**
	 * Callback method for the JServer class to restart the server.
	 */
    public static final void restart() {
        synchronized (JServerLauncher.class) {
            canRestart = true;
            restartOrStopCommandReceived = true;
            ((Class) (JServerLauncher.class)).notify();
        }
    }

    /**
	 * Performs the actual restart.
	 */
    private static void doRestart() {
        serverClassLoader = null;
        JServerClass = null;
        System.gc();
        System.runFinalization();
        try {
            Thread.sleep(2500);
        } catch (Throwable e) {
        }
    }

    /**
	 * Stops the server. This method conforms to the interface defined by <a href="../../../../utilities/JService/JService.html">JService</a> 
	 * for stopping server that is running as a Windows NT service.
	 */
    public static void stopService() {
        synchronized (JServerLauncher.class) {
            canRestart = false;
            restartOrStopCommandReceived = true;
            ((Class) (JServerLauncher.class)).notify();
        }
    }

    /**
	 * Performs the actual stopping.
	 */
    private static void doStop() {
        try {
            if (JServerClass != null) {
                Method getJServerMethod = JServerClass.getMethod("getJServer", new Class[] {});
                Method stopJServerMethod = JServerClass.getMethod("stopJServer", new Class[] {});
                Object jserver = getJServerMethod.invoke(null, null);
                if (jserver != null) {
                    stopJServerMethod.invoke(jserver, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (callSystemExit) System.exit(0);
    }

    /**
	 * The main method of the launcher.
	 */
    private static void launcherMain(final String[] args) {
        mainClassName = "com.teletalk.jserver.JServer";
        mainClassArgs = args;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-class")) {
                if (args.length > 1) {
                    mainClassName = args[1];
                    if (args.length > 2) {
                        mainClassArgs = new String[args.length - 2];
                        System.arraycopy(args, 2, mainClassArgs, 0, mainClassArgs.length);
                    }
                } else {
                    throw new RuntimeException("Class name must be specified with switch -class!");
                }
            }
        }
        try {
            while (canRestart) {
                canRestart = false;
                runServer();
                synchronized (JServerLauncher.class) {
                    while (!restartOrStopCommandReceived) ((Class) (JServerLauncher.class)).wait();
                    restartOrStopCommandReceived = false;
                }
                if (canRestart) doRestart();
            }
            doStop();
        } catch (Exception e) {
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            throw new RuntimeException("Fatal error while starting! (" + sw.toString() + ")");
        }
    }

    /**
	 * Runs/starts the server.
	 */
    private static void runServer() throws Exception {
        serverClassLoader = new JServerClassLoader();
        JServerClass = serverClassLoader.loadClass(JServerClassName);
        if (restartMethod != null) {
            final Method registerLauncherRestartMethodMethod = JServerClass.getMethod("registerLauncherRestartMethod", new Class[] { Method.class });
            registerLauncherRestartMethodMethod.invoke(null, new Object[] { restartMethod });
        }
        final Class mainClass = serverClassLoader.loadClass(mainClassName);
        final Method mainMethod = mainClass.getMethod("main", new Class[] { String[].class });
        mainMethod.invoke(null, new Object[] { mainClassArgs });
    }
}
