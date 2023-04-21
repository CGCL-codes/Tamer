package com.joystick;

import ade.exceptions.ADECallException;
import ade.*;
import ade.ADEGlobals.RecoveryState;
import ade.ADEGlobals.ServerState;
import com.*;
import java.io.*;
import java.lang.reflect.Array;
import java.math.*;
import java.net.*;
import java.rmi.*;
import java.util.*;
import static utilities.Util.*;

/** <code>JoystickServerImpl</code> takes input from a joystick (e.g., the
 * Logitech Dual Action) and passes on velocity commands to servers
 * implementing the {@link com.interfaces.VelocityServer VelocityServer} interface.
 * Although it has been tested with only the Logitech Dual Action gamepad,
 * it reads directly from the joystick event device, so it should work with
 * others, modulo button/axis differences.
 */
public class JoystickServerImpl extends ADEServerImpl implements JoystickServer {

    private static String prg = "JoystickServerImpl";

    private static String type = "JoystickServerImpl";

    private static boolean verbose = false;

    private static boolean useVel = false;

    private static boolean gotVel = false;

    private static String velVersion = "MoPoServerImpl";

    private Object vel;

    private static String devName = "/dev/input/js0";

    private RandomAccessFile dev;

    private short value = 0;

    private short event = 0;

    private short input = 0;

    private Boolean update = false;

    private byte[] mBuffer = new byte[8];

    private short[] values = new short[12];

    private static double[] defaultVels;

    private Reader r;

    private Writer w;

    private boolean status = false;

    private static boolean altSteer = false;

    private static double defTV = 0.0;

    private static double defRV = 0.0;

    /**
     * This method will be activated whenever a client calls the
     * requestConnection(uid) method. Any connection-specific initialization
     * should be included here, either general or user-specific.
     */
    protected void clientConnectReact(String user) {
        System.out.println(myID + ": got connection from " + user + "!");
        return;
    }

    /**
     * This method will be activated whenever a client that has called the
     * requestConnection(uid) method fails to update (meaning that the
     * heartbeat signal has not been received by the reaper), allowing both
     * general and user specific reactions to lost connections. If it returns
     * true, the client's connection is removed.
     */
    protected boolean clientDownReact(String user) {
        System.out.println(myID + ": lost connection with " + user + "!");
        return false;
    }

    /**
     * This method will be activated whenever a client that has called
     * the requestConnection(uid) method fails to update (meaning that the
     * heartbeat signal has not been received by the reaper). If it returns
     * true, the client's connection is removed. 
     */
    protected boolean attemptRecoveryClientDown(String user) {
        return false;
    }

    /**
     * This method will be activated whenever the heartbeat returns a
     * remote exception (i.e., the server this is sending a
     * heartbeat to has failed). 
     */
    protected void serverDownReact(String s) {
        if (s.indexOf(velVersion) >= 0) gotVel = false;
        return;
    }

    /** This method will be activated whenever the heartbeat reconnects
     * to a client (e.g., the server this is sending a heartbeat to has
     * failed and then recovered). <b>NOTE:</b> the pseudo-reference will
     * not be set until <b>after</b> this method is executed. To perform
     * operations on the newly (re)acquired reference, you must use the
     * <tt>ref</tt> parameter object.
     * @param s the ID of the {@link ade.ADEServer ADEServer} that connected
     * @param ref the pseudo-reference for the requested server */
    protected void serverConnectReact(String s, Object ref) {
        if (s.indexOf(velVersion) >= 0) gotVel = true;
        return;
    }

    /**
     * Adds additional local checks for credentials before allowing a shutdown
     * must return "false" if shutdown is denied, true if permitted
     */
    protected boolean localrequestShutdown(Object credentials) {
        return false;
    }

    /** The server is always ready to provide its service after it has come up */
    protected boolean localServicesReady() {
        return true;
    }

    /**
     * Implements the local shutdown mechanism that derived classes need to
     * implement to cleanly shutdown
     */
    protected void localshutdown() {
        System.out.print("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.print(prg + " shutting down...");
        r.halt();
        w.halt();
        Sleep(100);
        if (useVel) {
            try {
                call(vel, "setVels", 0.0, 0.0);
            } catch (ADECallException ace) {
                System.err.println(prg + ": Error setting vels: " + ace);
            }
        }
        System.out.println("done.");
    }

    /**
     * Get the most recently-read values.  A maping of values for the
     * Logitech Dual Action gamepad (default mode--LED off):<br>
     * values[0] - left stick x<br>
     * values[1] - left stick y<br>
     * values[2] - right stick x<br>
     * values[3] - right stick y<br>
     * values[4] - pad x<br>
     * values[5] - pad y<br>
     * values[6] - button 1<br>
     * values[7] - button 2<br>
     * values[8] - button 3<br>
     * values[9] - button 4<br>
     * values[10] - button 5 (L1)<br>
     * values[11] - button 6 (R1)<br>
     * @return an array of the values
     */
    public short[] getValues() throws RemoteException {
        return values;
    }

    /** 
     * JoystickServerImpl constructor.
     */
    public JoystickServerImpl() throws RemoteException {
        super();
        try {
            dev = new RandomAccessFile(devName, "r");
        } catch (IOException ioe) {
            System.err.println(prg + ": unable to set up in stream: " + ioe);
            System.exit(1);
        }
        if (useVel) {
            vel = getClient(velVersion);
            while (!gotVel) Sleep(100);
            try {
                defaultVels = (double[]) call(vel, "getDefaultVels");
            } catch (ADECallException ace) {
                System.err.println(prg + ": Error getting def vels: " + ace);
                defaultVels = new double[2];
            }
            if (defTV != 0.0) defaultVels[0] = defTV;
            if (defRV != 0.0) defaultVels[1] = defRV;
            System.out.println("defaultVels: " + defaultVels[0] + " " + defaultVels[1]);
        }
        r = new Reader();
        r.start();
        w = new Writer();
        w.start();
    }

    /**
     * Provide additional information for usage...
     */
    protected String additionalUsageInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Server-specific options:\n\n");
        sb.append("  -dev <device name>         <override default (" + devName + ")>\n");
        sb.append("  -pioneer                   <request Pioneer server>\n");
        sb.append("  -motion                    <request Motion server>\n");
        sb.append("  -segway                    <request Segway server>\n");
        sb.append("  -simbad                    <request Simbad server>\n");
        sb.append("  -adesim                    <request ADESim server>\n");
        sb.append("  -usarsim                   <request USARSim server>\n");
        sb.append("  -mopo                      <request MoPo server>\n");
        sb.append("  -altsteer                  <use single-stick steering>\n");
        sb.append("  -deftv tv					<default translational velocity>\n");
        sb.append("  -defrv rv					<default rotational velocity>\n");
        return sb.toString();
    }

    /** 
     * Parse additional command-line arguments
     * @return "true" if parse is successful, "false" otherwise 
     */
    protected boolean parseadditionalargs(String[] args) {
        boolean found = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-dev") && (++i < args.length)) {
                devName = args[i];
                found = true;
            } else if (args[i].equalsIgnoreCase("-pioneer")) {
                useVel = true;
                velVersion = "PioneerServerImpl";
                found = true;
            } else if (args[i].equalsIgnoreCase("-motion")) {
                useVel = true;
                velVersion = "MotionServerImpl";
                found = true;
            } else if (args[i].equalsIgnoreCase("-segway")) {
                useVel = true;
                velVersion = "SegwayServerImpl";
                found = true;
            } else if (args[i].equalsIgnoreCase("-simbad")) {
                useVel = true;
                velVersion = "SimbadServerImpl";
                found = true;
            } else if (args[i].equalsIgnoreCase("-adesim")) {
                useVel = true;
                velVersion = "ADESimServerImpl";
                found = true;
            } else if (args[i].equalsIgnoreCase("-usarsim")) {
                useVel = true;
                velVersion = "USARSimServerImpl";
                found = true;
            } else if (args[i].equalsIgnoreCase("-mopo")) {
                useVel = true;
                found = true;
            } else if (args[i].equalsIgnoreCase("-altsteer")) {
                altSteer = true;
                found = true;
            } else if (args[i].equalsIgnoreCase("-deftv")) {
                double tv;
                try {
                    tv = Double.parseDouble(args[i + 1]);
                    i++;
                    defTV = tv;
                } catch (NumberFormatException nfe) {
                    System.err.println(prg + ": deftv " + args[i + 1]);
                    System.err.println(prg + ": " + nfe);
                }
                found = true;
            } else if (args[i].equalsIgnoreCase("-defrv")) {
                double rv;
                try {
                    rv = Double.parseDouble(args[i + 1]);
                    i++;
                    defRV = rv;
                } catch (NumberFormatException nfe) {
                    System.err.println(prg + ": defrv " + args[i + 1]);
                    System.err.println(prg + ": " + nfe);
                }
                found = true;
            } else {
                System.out.println("Unrecognized argument: " + args[i]);
                return false;
            }
        }
        return found;
    }

    /**
     * The <code>Writer</code> is the main loop that "interprets" the
     * joystick and sends to Vel if requested.
     */
    private class Writer extends Thread {

        private boolean shouldWrite;

        public Writer() {
            shouldWrite = true;
        }

        public void run() {
            int i = 0;
            double tv, rv;
            while (shouldWrite) {
                Sleep(100);
                synchronized (update) {
                    if (update) {
                        update = false;
                    } else {
                        continue;
                    }
                }
                if (useVel) {
                    if (!altSteer) {
                        if (verbose) System.out.println(prg + ": L: " + values[1] + " R: " + values[3]);
                        tv = (double) (values[1] + values[3]) / 2.0;
                        tv = defaultVels[0] * (tv / (double) (-32767));
                        rv = (double) values[3] - (double) values[1];
                        rv = defaultVels[1] * (rv / (double) (-32767));
                    } else {
                        if (verbose) System.out.println(prg + ": T: " + values[0] + " R: " + values[1]);
                        tv = defaultVels[0] * ((double) values[1] / (double) (-32767));
                        if ((tv < (defaultVels[0] * 0.25)) && (tv > (defaultVels[0] * -0.25))) tv = 0;
                        if (tv >= 0) rv = defaultVels[1] * ((double) values[0] / (double) (-32767)); else rv = defaultVels[1] * ((double) values[0] / (double) (32767));
                    }
                    if (verbose) System.out.println("tv: " + tv + " rv: " + rv);
                    if (gotVel) {
                        try {
                            call(vel, "setVels", tv, rv);
                        } catch (ADECallException ace) {
                            System.err.println(prg + ": Error setting vels: " + ace);
                        }
                    }
                    if (values[6] != 0) {
                        try {
                            Runtime.getRuntime().exec("aplay com/joystick/honk.wav");
                        } catch (IOException ioe) {
                        }
                    }
                    if (values[7] != 0) {
                        defaultVels[0] *= 0.8;
                    }
                    if (values[9] != 0) {
                        defaultVels[0] *= 1.25;
                    }
                    if (values[8] != 0) {
                        try {
                            double[] p = (double[]) call(vel, "getPoseEgo");
                            System.out.println("Pose: " + p[0] + " " + p[1] + " " + p[2]);
                        } catch (ADECallException ace) {
                            System.err.println(prg + ": Error getting odometry: " + ace);
                        }
                    }
                }
            }
            System.out.println(prg + ": Exiting Writer thread...");
        }

        public void halt() {
            System.out.print("halting write thread...");
            shouldWrite = false;
        }
    }

    /**
     * The <code>Reader</code> is the main loop that listens to the joystick.
     */
    private class Reader extends Thread {

        private boolean shouldRead;

        public Reader() {
            shouldRead = true;
        }

        public void run() {
            int i = 0;
            while (shouldRead) {
                try {
                    dev.readFully(mBuffer);
                } catch (IOException ioe) {
                    System.err.println(prg + ": error reading from joystick: " + ioe);
                    halt();
                }
                synchronized (update) {
                    update = true;
                }
                event = (short) (0xFF & ((int) mBuffer[6]));
                if (event > 127) continue; else if (event == 1) input = 6; else input = 0;
                value = (short) ((0xFF & mBuffer[5]) << 8);
                value |= (short) (0xFF & mBuffer[4]);
                input += (short) (0xFF & ((int) mBuffer[7]));
                if (input < values.length) values[input] = value;
            }
            System.out.println(prg + ": Exiting Reader thread...");
        }

        public void halt() {
            System.out.print("halting read thread...");
            shouldRead = false;
        }
    }

    /**
     * Log a message using ADE Server Logging, if possible.  The ASL facility
     * takes care of adding timestamp, etc.
     * @param o the message to be logged
     */
    protected void logItASL(Object o) {
        canLogIt(o);
    }

    /**
     * Set the state of ADE Server Logging.  When true and logging is not
     * started, this starts logging.  When false and logging is started, this
     * stops logging and causes the log files to be written to disk.  ADE server     * logging is a global logging facility, so starting logging here enables
     * logging in a currently instantiated ADE servers.  NOTE: You want to stop
     * ADE server logging before quitting, or the files will not be complete.
     * @param state indicates whether to start (true) or stop (false) logging.
     */
    protected void setASL(boolean state) {
        try {
            setADEServerLogging(state);
        } catch (Exception e) {
            System.out.println("setASL: " + e);
        }
    }

    /**
     * <code>main</code> passes the arguments up to the ADEServerImpl
     * parent.  The parent does some magic and gets the system going.
     */
    public static void main(String[] args) throws Exception {
        ADEServerImpl.main(args);
    }
}
