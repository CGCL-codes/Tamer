package javamath.server.cas.impl.Maple;

import java.io.*;
import javamath.server.*;

/**
 * This class is responsible for handling the communication between
 * a MAPLE session and a Maple process. 
 * It maintains a state variable for the current state of Maple execution. 
 */
class MapleProcess implements Runnable {

    /**
	* Should we be debugging?
	*/
    public static boolean debug = false;

    /**
	* ASCII codes for characters we wish to read.
	*/
    private static final int ASCII_AT = (int) '@';

    private static final int ASCII_e = (int) 'e';

    private static final int ASCII_i = (int) 'i';

    private static final int ASCII_COLON = (int) ':';

    /**
	* get the .m files in the nativecode directory. We load them up
	* before mapleinit.
	*/
    File nativeCodeDir = new File(Server.JavamathHome + "/src/server/cas/impl/Maple/nativecode");

    /**
	* The executing Maple process for this session.
	*/
    private Process mapleProcess;

    /**  Maple is waiting for input. We've just read "@i :" */
    static final int INPUT_WAIT = 0;

    /**  Maple is processing information.  */
    static final int PROCESSING = 1;

    /**  Maple is sending normal output.  */
    static final int NORMAL_OUTPUT = 2;

    /**  Maple is sending error output.  */
    static final int ERROR_OUTPUT = 3;

    /**  A serious error with Maple.  */
    static final int READ_ERROR = 4;

    /** waiting to find out what kind of state change is coming up */
    static final int READ_AT = 5;

    private int state;

    public String stateString(int state) {
        switch(state) {
            case INPUT_WAIT:
                return "INPUT_WAIT";
            case PROCESSING:
                return "PROCESSING";
            case NORMAL_OUTPUT:
                return "NORMAL_OUTPUT";
            case ERROR_OUTPUT:
                return "ERROR_OUTPUT";
            case READ_ERROR:
                return "READ_ERROR";
            case READ_AT:
                return "READ_AT";
        }
        return "stateString FAILED";
    }

    /** * The input stream for reading data from MAPLE.  */
    private InputStream mapleIn;

    /** * The output stream for sending data to MAPLE.  */
    private DataOutputStream mapleOut;

    private StringBuffer normalBuffer = new StringBuffer();

    private StringBuffer errorBuffer = new StringBuffer();

    /**
	* This thread polls for output from MAPLE.
	*/
    private Thread readThread;

    /**
	* Start MAPLE, set up its initial state.
	*/
    MapleProcess() throws Exception {
        String[] mfiles = nativeCodeDir.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if ((name.charAt(name.length() - 1) == 'm') && (name.charAt(name.length() - 2) == '.')) return true; else return false;
            }
        });
        String mfilestring = "";
        for (int i = 0; i < mfiles.length; i++) {
            mfilestring += " -i " + Server.JavamathHome + "/src/server/cas/impl/Maple/nativecode/" + mfiles[i];
        }
        String MAPLE_COMMAND = Server.MapleCommand + " -z " + mfilestring + " -i " + Server.JavamathHome + "/src/server/cas/impl/Maple/mapleinit -q";
        mapleProcess = Runtime.getRuntime().exec(MAPLE_COMMAND);
        mapleIn = new BufferedInputStream(mapleProcess.getInputStream(), BUFFER_SIZE);
        mapleOut = new DataOutputStream(mapleProcess.getOutputStream());
        state = PROCESSING;
        readThread = new Thread(this);
        readThread.start();
        waitForInputPrompt();
        if (!inInputWait()) {
            throw new Exception("Unable to initialize Maple");
        }
    }

    /**
	* Wait around until the state changes to an input state.
	*/
    public synchronized void waitForInputPrompt() {
        debug("In waitForInputPrompt\n");
        debug("Before wait State = " + stateString(state) + "\n");
        try {
            while (state != INPUT_WAIT) {
                wait();
            }
        } catch (InterruptedException e) {
            debug("Interrupted exception caught\n");
        }
        debug("After wait State = " + stateString(state) + "\n");
        debug("End waitForInputPrompt\n");
    }

    public boolean inInputWait() {
        return (state == INPUT_WAIT);
    }

    /**
	 * Send a command to the MAPLE process. Adds a newline to the end
	 * of the command. This method also changes the state to PROCESSING.
	 */
    public void sendCommand(String command) throws Exception {
        waitForInputPrompt();
        if (state != INPUT_WAIT) {
            throw new Exception("INPUT_WAIT not attained" + getErrorOutput());
        }
        clearNormalOutput();
        clearErrorOutput();
        synchronized (this) {
            state = PROCESSING;
            mapleOut.writeBytes(command + "\n");
            mapleOut.flush();
            notifyAll();
        }
        debug("Sent: " + command + " to Maple\n");
    }

    /**
	 * Return the contents of the normal output buffer and clear the buffer. 
	 */
    public String getNormalOutput() {
        waitForInputPrompt();
        String s = normalBuffer.toString();
        clearNormalOutput();
        return s;
    }

    /**
	 * Return the contents of the error output buffer and clear the buffer.
	 */
    public String getErrorOutput() {
        String s = errorBuffer.toString();
        errorBuffer.setLength(0);
        return s;
    }

    public void clearErrorOutput() {
        errorBuffer.setLength(0);
    }

    /**
	* clear the normalBuffer so that  the next output
	* from Maple will be the next thing returned by getNormalOutput
	*/
    public void clearNormalOutput() {
        normalBuffer.setLength(0);
    }

    /**
	* Maximum size of the input buffer array.
	*/
    private static final int BUFFER_SIZE = 128;

    public void run() {
        int c;
        while (true) {
            try {
                debug("Waiting for a character\n");
                c = mapleIn.read();
                debug("character arrived = " + ((char) c) + "\n");
            } catch (java.io.IOException e) {
                synchronized (this) {
                    state = READ_ERROR;
                    notifyAll();
                }
                System.out.println("Error reading from MAPLE: " + e);
                System.out.flush();
                e.printStackTrace();
                return;
            }
            char d = (char) c;
            synchronized (this) {
                switch(state) {
                    case INPUT_WAIT:
                        debug("run: in state INPUT_WAIT -- should not happen\n");
                        break;
                    case PROCESSING:
                        if (c == ASCII_AT) {
                            state = READ_AT;
                        } else {
                            state = NORMAL_OUTPUT;
                            normalBuffer.append(d);
                        }
                        break;
                    case READ_AT:
                        if (c == ASCII_e) {
                            state = ERROR_OUTPUT;
                        } else if (c == ASCII_i) {
                            state = INPUT_WAIT;
                            try {
                                mapleIn.read();
                            } catch (Exception e) {
                                System.err.println("Couldn't read linefeed.\n");
                            }
                        } else {
                            state = READ_ERROR;
                        }
                        break;
                    case NORMAL_OUTPUT:
                        if (c == ASCII_AT) {
                            state = READ_AT;
                            break;
                        }
                        normalBuffer.append(d);
                        break;
                    case ERROR_OUTPUT:
                        if (c == ASCII_AT) {
                            state = READ_AT;
                        } else {
                            errorBuffer.append(d);
                        }
                        break;
                    case READ_ERROR:
                        break;
                }
                debug("Now in state " + stateString(state) + "\n");
                notifyAll();
            }
        }
    }

    /**
	 * This closes all streams and kills the underlying MAPLE process.
	 */
    void end() {
        debug("Ending MAPLE process.");
        try {
            sendCommand("quit;");
        } catch (Exception e) {
            System.out.println("Failed to stop MAPLE:" + e);
            System.out.flush();
            e.printStackTrace();
            return;
        }
        readThread.stop();
        mapleProcess.destroy();
        mapleIn = null;
        mapleOut = null;
        mapleProcess = null;
    }

    /**
	 * Print out debugging messages if we're in debug mode.
	 */
    private static void debug(String msg) {
        if (debug) {
            System.out.println("DEBUG: " + msg);
            System.out.flush();
        }
    }
}
