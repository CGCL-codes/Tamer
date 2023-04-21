package ch.epfl.lsr.adhoc.routing.ncode;

import java.util.Vector;
import java.net.*;
import ch.epfl.lsr.adhoc.runtime.Message;

/**
 * This is a simple implementation of network coded packet.
 * <p>
 * This Message is a very simple implementation of Message, which allows to
 * encapsulate another message in a network coded message. It is created 
 * through the NCodeFactory class (also a simple test class).
 * <p>
 * @see NCodeFactory
 * @see Message
 * @author Kav� Salamatian
 * @version 1.0
 */
public class NCodeMessage extends Message {

    /** Encapsulate the actual message */
    protected byte[] inbuf = new byte[NCGlobals.MaxBufLength];

    protected int inLength = 0;

    protected byte[] outbuf = new byte[NCGlobals.MaxBufLength];

    ;

    protected int outLength = 0;

    /**
   * Creates a new instance of NCodeMessage.
   * <p>
   * The type of this message object is initialized at creation time and cannot
   * be changed later (for better use in a MessagePool).
   * <p>
   * @param type The type of service for this message
   */
    public NCodeMessage(char type) {
        super(type);
    }

    public synchronized void prepareData() {
        addBytes(outbuf, outLength);
    }

    public synchronized void readData() {
        inLength = getBytes(inbuf);
    }

    public synchronized void setOutbuf(byte[] buf, int bufLength) {
        outbuf = buf;
        outLength = bufLength;
    }

    /**
   * Returns the textual message contained whithin this message object.
   * <p>
   * @return The textual message in this message object
   */
    public synchronized int getInBuf(byte[] buf) {
        System.arraycopy(inbuf, 0, buf, 0, inLength);
        return inLength;
    }

    public byte int2byte(int val) {
        return (byte) val;
    }

    public int byte2int(byte val) {
        int i = val;
        if (i < 0) {
            i = i + 256;
            return i;
        } else {
            return i;
        }
    }
}
