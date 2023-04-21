package verinec.adaptation.snmp.tftpserver;

import java.util.logging.*;
import java.io.*;

/**
 * Class responsible for encapsulating TFTP packets. It is actually a straight
 * forward implementation of the RFC 1350.
 * 
 * <p>Most of the source was taken from
 * <a href="http://www.it.rit.edu/~netsyslb/vksf522/class_examples/Day09_TFTPServer/TFTPPacket.pdf">
 * http://www.it.rit.edu/~netsyslb/vksf522/class_examples/Day09_TFTPServer/TFTPPacket.pdf</a><br>
 * I removed the gui part. </p>
 * 
 * @author christoph.ehret at unifr.ch
 * @version 1.0
 *
 */
public class TFTPPacket {

    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Read Request opcode
     */
    public static final int RRQ = 1;

    /**
     * Write Request opcode
     */
    public static final int WRQ = 2;

    /**
     * Data opcode
     */
    public static final int DATA = 3;

    /**
     * ACK opcode
     */
    public static final int ACK = 4;

    /**
     * Error opcode
     */
    public static final int ERROR = 5;

    /**
     * Unknown opcode
     */
    public static final int UNKNOWN = -1;

    /**
     * <i>Unknowncode</i> error code
     */
    public static final int UNKNOWNCODE = 0;

    /**
     * <i>File not found</i> error code
     */
    public static final int FILENOTFOUND = 1;

    /**
     * <i>Access violation</i> error code
     */
    public static final int ACCESVIOLATION = 2;

    /**
     * <i>Disk full</i> error code
     */
    public static final int DISKFULL = 3;

    /**
     * <i>Illegal operation</i> error code
     */
    public static final int ILLEGALOPERATION = 4;

    /**
     * <i>Unknown TID</i> error code
     */
    public static final int UNKNOWNTID = 5;

    /**
     * <i>File exists</i> error code
     */
    public static final int FILEEXISTS = 6;

    /**
     * <i>No such user</i> error code
     */
    public static final int NOSUCHUSER = 7;

    private int opcode;

    private String fname;

    private String mode;

    private int blkNo;

    private byte[] data;

    private int errNo;

    private String errMsg;

    /**
     * Sets the data opcode to the given opcode
     * @param opcode Opcode number that specifies the data <i>identity</i>
     */
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    /**
     * Sets the name of the file to write
     * @param fname Name of the file to write
     */
    public void setFName(String fname) {
        this.fname = fname;
    }

    /**
     * Sets the mode
     * @param mode Mode to use
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Sets the blocknumber of the packet
     * @param blkNo Block number of the packet
     */
    public void setBlockNo(int blkNo) {
        this.blkNo = blkNo;
    }

    /**
     * Sets the data
     * @param data Data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * Sets the error number, i.e one of the error codes
     * @param errNo
     */
    public void setErrorNo(int errNo) {
        this.errNo = errNo;
    }

    /**
     * Sets the error message
     * @param errMsg Error message
     */
    public void setErrorMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * Gets the opcode
     * @return Opcode number
     */
    public int getOpcode() {
        return this.opcode;
    }

    /**
     * Gets the name of the file to write
     * @return Name of the file to write
     */
    public String getFName() {
        return this.fname;
    }

    /**
     * Gets the mode to use
     * @return Mode to use
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * Gets the block number of the packet
     * @return Block number of the packet
     */
    public int getBlockNo() {
        return this.blkNo;
    }

    /**
     * Gets the data
     * @return Data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * Gets the error opcode number
     * @return Error opcode number
     */
    public int getErrorNo() {
        return this.errNo;
    }

    /**
     * Gets the error message 
     * @return Error message
     */
    public String getErrorMsg() {
        return this.errMsg;
    }

    /**
     * Parses a raw packet to get different informations
     * <p>
     * @param rawPacket Raw data
     * @param offset Offset from where begining to read in rawPacket
     * @param length Number of bytes to read in rawPacket
     */
    public void parse(byte[] rawPacket, int offset, int length) {
        ByteArrayInputStream bais = new ByteArrayInputStream(rawPacket, offset, length);
        DataInputStream dis = new DataInputStream(bais);
        clearPacket();
        try {
            opcode = dis.readShort();
            switch(opcode) {
                case RRQ:
                case WRQ:
                    fname = readNTString(dis);
                    mode = readNTString(dis);
                    break;
                case DATA:
                    blkNo = dis.readShort();
                    data = new byte[length - 4];
                    for (int i = 0; i < length - 4; i++) data[i] = dis.readByte();
                    break;
                case ACK:
                    blkNo = dis.readShort();
                    break;
                case ERROR:
                    errNo = dis.readShort();
                    errMsg = readNTString(dis);
                    break;
                default:
                    logger.log(Level.SEVERE, "Exception in TFTPPacket.parse : " + opcode + " unknown");
                    return;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occured while parsing TFTPPacket ", e);
        }
    }

    /**
     * Builds a TFTP packet
     * @return The byte array for the packet
     */
    public byte[] build() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeShort(opcode);
            switch(opcode) {
                case RRQ:
                case WRQ:
                    dos.writeBytes(fname);
                    dos.writeByte(0);
                    dos.writeBytes(mode);
                    dos.writeByte(0);
                    break;
                case DATA:
                    dos.writeShort(blkNo);
                    for (int i = 0; i < data.length; i++) dos.writeByte(data[i]);
                    break;
                case ACK:
                    dos.writeShort(blkNo);
                    break;
                case ERROR:
                    dos.writeShort(errNo);
                    dos.writeBytes(errMsg);
                    dos.writeByte(0);
                    break;
                default:
                    logger.log(Level.SEVERE, "Exception occured in TFTPPacket.build : " + opcode + " unknown");
                    return null;
            }
            dos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            clearPacket();
            logger.log(Level.SEVERE, "Exception occured while building TFTP packet " + e);
            return null;
        }
    }

    /**
     * Method to put all the packet fields to their default value
     */
    public void clearPacket() {
        opcode = UNKNOWN;
        fname = "";
        mode = "";
        blkNo = UNKNOWN;
        data = null;
        errNo = UNKNOWN;
        errMsg = "";
    }

    /**
     * Reads a null terminated string for a data input stream
     * @param dis The stream to read from.
     * @return The next null terminated string from dis.
     * @throws IOException If reading the string fails.
     */
    public String readNTString(DataInputStream dis) throws IOException {
        String str = "";
        while (true) {
            byte octet = dis.readByte();
            if (octet == 0) break;
            str += (char) octet;
        }
        return str;
    }
}
