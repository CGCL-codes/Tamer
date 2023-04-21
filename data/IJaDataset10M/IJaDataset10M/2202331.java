package org.jbuzz.telnet.option;

import java.io.IOException;
import java.lang.reflect.Field;
import org.jbuzz.telnet.TelnetSocket;

/**
 * This class defines serveral methods for telnet option negotiation.
 * 
 * @see org.jbuzz.telnet.option.TelnetOptionSession
 * @see org.jbuzz.telnet.TelnetSocket
 * @author Lung-Kai Cheng (lkcheng@users.sourceforge.net)
 */
public abstract class TelnetOption {

    /**
	 * 8-bit Data Path
	 */
    public static final int BINARY = 0;

    /**
	 * Echo
	 */
    public static final int ECHO = 1;

    /**
	 * Prepare to Reconnect
	 */
    public static final int RCP = 2;

    /**
	 * Suppress Go Ahead
	 */
    public static final int SGA = 3;

    /**
	 * Approximate Message Size
	 */
    public static final int NAMS = 4;

    /**
	 * Give Status
	 */
    public static final int STATUS = 5;

    /**
	 * Timing Mark
	 */
    public static final int TM = 6;

    /**
	 * Remote Controlled Transmission and Echo
	 */
    public static final int RCTE = 7;

    /**
	 * Negotiate About Output Line Width
	 */
    public static final int NAOL = 8;

    /**
	 * Negotiate About Output Page Size
	 */
    public static final int NAOP = 9;

    /**
	 * Negotiate About CR Disposition
	 */
    public static final int NAOCRD = 10;

    /**
	 * Negotiate About Horizontal Tab Stops
	 */
    public static final int NAOHTS = 11;

    /**
	 * Negotiate About Horizontal Tab Disposition
	 */
    public static final int NAOHTD = 12;

    /**
	 * Negotiate About Formfeed Disposition
	 */
    public static final int NAOFFD = 13;

    /**
	 * Negotiate About Vertical Tab Stops
	 */
    public static final int NAOVTS = 14;

    /**
	 * Negotiate About Vertical Tab Disposition
	 */
    public static final int NAOVTD = 15;

    /**
	 * Negotiate About Output LF Disposition
	 */
    public static final int NAOLFD = 16;

    /**
	 * Extended ASCII Character Set
	 */
    public static final int XASCII = 17;

    /**
	 * Force Logout
	 */
    public static final int LOGOUT = 18;

    /**
	 * Byte Macro
	 */
    public static final int BM = 19;

    /**
	 * Data Entry Terminal
	 */
    public static final int DET = 20;

    /**
	 * Supdup Protocol
	 */
    public static final int SUPDUP = 21;

    /**
	 * Supdup Output
	 */
    public static final int SUPDUPOUTPUT = 22;

    /**
	 * Send Location
	 */
    public static final int SNDLOC = 23;

    /**
	 * Terminal Type
	 */
    public static final int TTYPE = 24;

    /**
	 * End of Record
	 */
    public static final int EOR = 25;

    /**
	 * TACACS User Identification
	 */
    public static final int TUID = 26;

    /**
	 * Output Marking
	 */
    public static final int OUTMRK = 27;

    /**
	 * Terminal Location Number
	 */
    public static final int TTYLOC = 28;

    /**
	 * Telnet 3270 Regime
	 */
    public static final int T3270REGIME = 29;

    /**
	 * X.3 PAD
	 */
    public static final int X3PAD = 30;

    /**
	 * Window Size
	 */
    public static final int NAWS = 31;

    /**
	 * Terminal Speed
	 */
    public static final int TSPEED = 32;

    /**
	 * Remote Flow Control
	 */
    public static final int LFLOW = 33;

    /**
	 * Linemode Option
	 */
    public static final int LINEMODE = 34;

    /**
	 * X Display Location
	 */
    public static final int XDISPLOC = 35;

    /**
	 * Old - Environmental Variables
	 */
    public static final int OLD_ENVIRON = 36;

    /**
	 * Authenticate
	 */
    public static final int AUTHENTICATION = 37;

    /**
	 * Encryption Option
	 */
    public static final int ENCRYPT = 38;

    /**
	 * New - Environmental Variables
	 */
    public static final int NEW_ENVIRON = 39;

    /**
	 * Extended Options List
	 */
    public static final int EXOPL = 255;

    static String[] displayNames = new String[256];

    static {
        Field[] fields = TelnetOption.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                displayNames[field.getInt(null)] = field.getName();
            } catch (Exception e) {
                continue;
            }
        }
    }

    public static String getDisplayName(int type) {
        try {
            return displayNames[type];
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * Get type of the option.
	 * 
	 * @return type of the option
	 */
    public abstract int getType();

    public String getDisplayName() {
        return getDisplayName(this.getType());
    }

    /**
	 * Enable the option for the telnet socket.
	 * 
	 * @param telnetSocket
	 *            telnet socket to enable
	 * @throws IOException
	 *             the exception will be thrown when negotiation failed
	 */
    public abstract void enable(TelnetSocket telnetSocket) throws IOException;

    /**
	 * Disable the option for the telnet socket.
	 * 
	 * @param telnetSocket
	 *            telnet socket to disable
	 * @throws IOException
	 *             the exception will be thrown when negotiation failed
	 */
    public abstract void disable(TelnetSocket telnetSocket) throws IOException;

    /**
	 * Handle WILL option command received from the remote side.
	 * 
	 * @param telnetSocket
	 *            telnet socket which receives the command
	 * @throws IOException
	 *             the exception will be thrown when negotiation failed
	 */
    public abstract void doWILL(TelnetSocket telnetSocket) throws IOException;

    /**
	 * Handle WONT option command received from the remote side.
	 * 
	 * @param telnetSocket
	 *            telnet socket which receives the command
	 * @throws IOException
	 *             the exception will be thrown when negotiation failed
	 */
    public abstract void doWONT(TelnetSocket telnetSocket) throws IOException;

    /**
	 * Handle DO option command received from the remote side.
	 * 
	 * @param telnetSocket
	 *            telnet socket which receives the command
	 * @throws IOException
	 *             the exception will be thrown when negotiation failed
	 */
    public abstract void doDO(TelnetSocket telnetSocket) throws IOException;

    /**
	 * Handle DONT option command received from the remote side.
	 * 
	 * @param telnetSocket
	 *            telnet socket which receives the command
	 * @throws IOException
	 *             the exception will be thrown when negotiation failed
	 */
    public abstract void doDONT(TelnetSocket telnetSocket) throws IOException;

    /**
	 * Handle SB option command received from the remote side.
	 * 
	 * @param telnetSocket
	 *            telnet socket which receives the command
	 * @throws IOException
	 *             the exception will be thrown when subnegotiation failed
	 */
    public abstract void doSB(TelnetSocket telnetSocket) throws IOException;
}
