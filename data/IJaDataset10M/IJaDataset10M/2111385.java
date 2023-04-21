package org.jbuzz.telnet;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class encapsulates the telnet command code and parameter.
 * 
 * @see org.jbuzz.telnet.option.TelnetOptionCommand
 * @author Lung-Kai Cheng (lkcheng@users.sourceforge.net)
 */
public class TelnetCommand {

    public static final int SE = 240;

    public static final int NOP = 241;

    public static final int DM = 242;

    public static final int BRK = 243;

    public static final int IP = 244;

    public static final int AO = 245;

    public static final int AYT = 246;

    public static final int EC = 247;

    public static final int EL = 248;

    public static final int GA = 249;

    public static final int SB = 250;

    public static final int WILL = 251;

    public static final int WONT = 252;

    public static final int DO = 253;

    public static final int DONT = 254;

    public static final int IAC = 255;

    public static final int INVALID = -1;

    int code;

    byte[] parameter;

    /**
	 * Constructor with uninitialized command code and parameter.
	 */
    public TelnetCommand() {
        this.code = INVALID;
    }

    /**
	 * Constructor with command code
	 * 
	 * @param code
	 *            command code
	 */
    public TelnetCommand(int code) {
        this.code = code;
    }

    /**
	 * Constructor with command code and parameter.
	 * 
	 * @param code
	 *            command code
	 * @param parameter
	 *            command parameter
	 */
    public TelnetCommand(int code, byte[] parameter) {
        this.code = code;
        this.parameter = parameter;
    }

    /**
	 * Get the command code.
	 * 
	 * @return command code
	 */
    public int getCode() {
        return this.code;
    }

    /**
	 * Set the command code.
	 * 
	 * @param code
	 *            command code
	 */
    public void setCode(int code) {
        this.code = code;
    }

    /**
	 * Check whether the command parameter is existed.
	 * 
	 * @return whether the command parameter is existed
	 */
    public boolean hasParameter() {
        return this.parameter != null;
    }

    /**
	 * Get the command parameter.
	 * 
	 * @return command parameter
	 */
    public byte[] getParameter() {
        return this.parameter;
    }

    /**
	 * Set the command parameter.
	 * 
	 * @param parameter
	 *            command parameter
	 */
    public void setParameter(byte[] parameter) {
        this.parameter = parameter;
    }

    /**
	 * Remove the command parameter
	 */
    public void removeParameter() {
        this.parameter = null;
    }

    /**
	 * Write binary format data of the telnet command to the output.
	 * 
	 * @param out
	 *            output to write
	 * @throws IOException
	 *             the exception exception will be thrown when writing error
	 */
    public void write(OutputStream out) throws IOException {
        if (this.code == INVALID) {
            throw new IOException();
        }
        out.write(IAC);
        out.write(this.code);
        if (this.parameter != null) {
            out.write(this.parameter, 0, this.parameter.length);
        }
        out.flush();
    }
}
