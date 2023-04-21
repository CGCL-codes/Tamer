package lejos.nxt.comm;

import lejos.nxt.*;
import java.io.*;
import javax.microedition.io.*;

/**
 * Generic lejos nxt connection class. Provide access to standard read/write
 * methods. This code supports both asynchronous (used for Bluetooth connections)
 * and synchronous (used for USB and RS485) I/O operations for the actual reading
 * and writing of the low level buffers.
 * NOTE: The code in this class makes a number of assumptions:
 * 1. The input and output buffers have been sized to match the underlying 
 *    device, and the packet header size. In particular the synchronous
 *    code assumes that the entire output buffer can be written using a single
 *    atomic write operation.
 * 2. Although the code can handle any size packet header the default is
 *    assumed to be 2 bytes. If this is not the case (for example USB), then
 *    the setIOMode function must be over-ridden.
 * 3. This class allows the use of a "soft" EOF implementation which uses a 
 *    zero length packet as an EOF marker. This only operates when in packet
 *    mode and can be overridden. Currently this is used for USB and RS485
 *    devices. It is not used for Bluetooth connections.
 * 4. Some devices (like USB), have an inherent packet structure. The current
 *    PC assumes that when in packet mode an entire packet will fit within
 *    a single USB packet. This limits the maximum packet size which can be
 *    used over USB connections to 63 bytes. This code does not currently
 *    enforce this limit.
 * @author andy
 */
public abstract class NXTConnection implements StreamConnection {

    public static final int LCP = 1;

    public static final int PACKET = 0;

    public static final int RAW = 2;

    static final int CS_IDLE = 0;

    static final int CS_DISCONNECTED = 1;

    static final int CS_DISCONNECTING2 = 2;

    static final int CS_DISCONNECTING = 3;

    static final int CS_CONNECTED = 4;

    static final int CS_DATALOST = 5;

    static final int CS_EOF = 6;

    static final int DEF_HEADER = 2;

    private static final int CLOSETIMEOUT1 = 1000;

    private static final int CLOSETIMEOUT2 = 500;

    int state = CS_IDLE;

    int header;

    byte[] inBuf;

    byte[] outBuf;

    int inCnt;

    int inOffset;

    int outCnt;

    int outOffset;

    int pktOffset;

    int pktLen;

    int bufSz;

    InputStream is;

    OutputStream os;

    String address;

    public String getAddress() {
        return address;
    }

    /**
     * Write all of the current output buffer to the device.
     * NOTE: To ensure correct operation of packet mode, this function should
     * only return 1 if all of the data will eventually be written. It should
     * avoid writing part of the data.
     * @param wait if true wait until the output has been written
     * @return -ve if error, 0 if not written, +ve if written
     */
    abstract int flushBuffer(boolean wait);

    /**
	 * Attempt to write bytes to the Bluetooth connection. Optionally wait if it
	 * is not possible to write at the moment. Supports both packet and stream
	 * write operations. If in packet mode a set of header bytes indicating
	 * the size of the packet will be sent ahead of the data.
	 * NOTE: If in packet mode and writing large packets (> 254 bytes), then
	 * the blocking mode (wait = true), should be used to ensure that the packet
	 * is sent correctly.
	 * @param	data	The data to be written.
	 * @param	len		The number of bytes to write.
	 * @param	wait	True if the call should block until all of the data has
	 *					been sent.
	 * @return			> 0 number of bytes written.
	 *					0 Request would have blocked (and wait was false).
	 *					-1 An error occurred
	 *					-2 Data has been lost (See notes above).
	 */
    public synchronized int write(byte[] data, int len, boolean wait) {
        int offset = -header;
        int hdr = len;
        if (state == CS_DATALOST) {
            state = CS_CONNECTED;
            return -2;
        }
        if (state < CS_CONNECTED) return -1;
        if (outCnt > 0 && !wait) return 0;
        ioloop: while (offset < len) {
            while (outCnt >= outBuf.length) {
                if (!wait && header == 0) break ioloop;
                if (flushBuffer(true) < 0) disconnected();
                if (state < CS_CONNECTED) break ioloop;
            }
            if (offset < 0) {
                outBuf[outCnt++] = (byte) hdr;
                hdr >>= 8;
                offset++;
            } else {
                int cnt = (outBuf.length - outCnt);
                if (cnt > len - offset) cnt = len - offset;
                System.arraycopy(data, offset, outBuf, outCnt, cnt);
                outCnt += cnt;
                offset += cnt;
            }
        }
        int written = flushBuffer(wait);
        if (written > 0) return offset;
        if (written < 0) disconnected();
        return written;
    }

    /**
     * Get any available data into the input buffer.
     * @param wait if true wait for data to be available.
     * @return -ve if error, 0 if not read, +ve if read
     */
    abstract int fillBuffer(boolean wait);

    /**
	 * Attempt to read data from the connection. Optionally wait for data to
	 * become available. Supports both packet and stream mode operations. When
	 * in packet mode the packet length bytes are automatically processed. The
	 * read will return just a single packet. If the packet is larger then the
	 * requested length then the rest of the packet will be returned in the
	 * following reads. If wait is true then in packet mode the call will wait
	 * until either the entire packet can be read or outLen bytes are available.
	 * In stream mode the call will return if at least 1 byte has been read.
	 * @param	data	Location to return the data. If null the data is discarded.
	 * @param	outLen	Max number of bytes to read.
	 * @param	wait	Should the call block waiting for data.
	 * @return			> 0 number of bytes read.
	 *      			0 no bytes available (and wait was false).
	 *					-1 EOF/Connection closed.
	 *					-2 data lost (see notes).
     *                  -3 Some other error
	 */
    public synchronized int read(byte[] data, int outLen, boolean wait) {
        int offset = 0;
        if (state == CS_IDLE) return -3;
        if (state == CS_DATALOST) {
            state = CS_CONNECTED;
            return -2;
        }
        if (state == CS_EOF) {
            inCnt = 0;
            inOffset = 0;
            return -1;
        }
        if (fillBuffer(false) < 0) disconnected();
        if (state == CS_DISCONNECTED && inCnt <= 0) return -1;
        if (!wait && inCnt <= 0) return 0;
        if (header == 0) {
            pktOffset = 0;
            pktLen = outLen;
        }
        ioloop: while (pktOffset < pktLen) {
            while (inCnt <= 0) {
                if (!wait) return offset;
                if (state != CS_CONNECTED) {
                    if (offset > 0) break ioloop;
                    if (state == CS_DISCONNECTED) return -1;
                    if (state == CS_DATALOST) {
                        state = CS_CONNECTED;
                        return -2;
                    }
                    return -3;
                }
                if (fillBuffer(true) < 0) disconnected();
            }
            if (pktOffset < 0) {
                pktLen += ((int) inBuf[inOffset++] & 0xff) << (header + pktOffset) * 8;
                pktOffset++;
                inCnt--;
            } else {
                if (offset >= outLen) return offset;
                int len = (inOffset + inCnt > inBuf.length ? inBuf.length - inOffset : inCnt);
                if (len > outLen - offset) len = outLen - offset;
                if (len > pktLen - pktOffset) len = pktLen - pktOffset;
                if (data != null) System.arraycopy(inBuf, inOffset, data, offset, len);
                offset += len;
                inOffset += len;
                pktOffset += len;
                inCnt -= len;
                if (header == 0) wait = false;
            }
            inOffset = inOffset % inBuf.length;
        }
        pktOffset = -header;
        pktLen = 0;
        if (header > 0 && offset == 0) {
            state = CS_EOF;
            return -1;
        }
        return offset;
    }

    /**
	 * Indicate the number of bytes available to be read. Supports both packet
	 * mode and stream connections.
	 * @param	what	0 (all modes) return the number of bytes that can be
	 *					read without blocking.
	 *					1 (packet mode) return the number of bytes still to be
	 *					read from the current packet.
	 *					2 (packet mode) return the length of the current packet.
     * @return  number of bytes available
	 */
    public synchronized int available(int what) {
        if (state == CS_IDLE) return -1;
        if (state == CS_DATALOST) {
            state = CS_CONNECTED;
            return -2;
        }
        fillBuffer(false);
        if (header > 0) {
            if (pktOffset < 0) read(null, 0, false);
            if (pktOffset < 0) return 0;
            if (what == 2) return pktLen;
            int ret = pktLen - pktOffset;
            if (what == 0 && ret > inCnt) ret = inCnt;
            return ret;
        } else return inCnt;
    }

    public int available() {
        return available(0);
    }

    void setHeader(int sz) {
        header = sz;
        pktOffset = -header;
        pktLen = 0;
    }

    /**
	 * Set operating mode. Controls the packet/stream mode of this channel.
	 * For packet mode it defines the header size to be used.
	 * @param mode	I/O mode to be used for this connection. NXTConnection.RAW, .LCP, or .PACKET
	 */
    public void setIOMode(int mode) {
        if (mode == PACKET || mode == LCP) setHeader(DEF_HEADER); else setHeader(0);
    }

    /**
     * Perform a blocking read on the connection
     * @param data byte array to store the results.
     * @param len max number of bytes to read
     * @return actual number of bytes read, return < 0 for error
     */
    public int read(byte[] data, int len) {
        return read(data, len, true);
    }

    /**
     * Perform a blocking write on the connection
     * @param data byte array to be written.
     * @param len number of bytes to write
     * @return actual number of bytes written, return < 0 for error
     */
    public int write(byte[] data, int len) {
        return write(data, len, true);
    }

    /**
	 * Return the InputStream for this connection.
	 *
	 * @return the input stream
	 */
    public InputStream openInputStream() {
        return (is != null ? is : (is = new NXTInputStream(this, bufSz - header)));
    }

    /**
	 * Return the OutputStream for this connection
	 *
	 * @return the output stream
	 */
    public OutputStream openOutputStream() {
        return (os != null ? os : (os = new NXTOutputStream(this, bufSz - header)));
    }

    /**
	 * Return the DataInputStream for this connect
	 *
	 * @return the data input stream
	 */
    public DataInputStream openDataInputStream() {
        return new DataInputStream(openInputStream());
    }

    /**
	 * Return the DataOutputStream for this connection.
	 *
	 * @return the data output stream
	 */
    public DataOutputStream openDataOutputStream() {
        return new DataOutputStream(openOutputStream());
    }

    /**
	 * Called when the remote side of the connection disconnects.
	 * Mark the connection as now disconected.
	 */
    synchronized boolean disconnected() {
        notifyAll();
        if (state <= CS_DISCONNECTED) return false;
        freeConnection();
        state = CS_DISCONNECTED;
        outCnt = 0;
        return true;
    }

    /**
     * Send an EOF packet to the remote system.
     */
    synchronized void sendEOF() {
        if (header > 0) {
            for (int i = 0; state >= CS_CONNECTED && outCnt > 0 && i < CLOSETIMEOUT2; i++) {
                flushBuffer(false);
                try {
                    wait(1);
                } catch (Exception e) {
                }
            }
            write(null, 0, false);
        }
    }

    /**
     * Disconnect the device/channel
     */
    void disconnect() {
        disconnected();
    }

    /**
     * Tell the lower levels that they can release any resources for this
     * connection.
     */
    void freeConnection() {
    }

    /**
	 * Close the connection. Flush any pending output. Inform the remote side
	 * that the connection is now closed. Free resources.
	 */
    public void close() {
        if (state == CS_IDLE) return;
        synchronized (this) {
            if (state >= CS_CONNECTED) {
                sendEOF();
                state = CS_DISCONNECTING;
            }
        }
        for (int i = 0; state == CS_DISCONNECTING && (outCnt > 0 && i < CLOSETIMEOUT1); i++) {
            flushBuffer(false);
            read(null, inBuf.length, false);
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
        for (int i = 0; state == CS_DISCONNECTING && i < CLOSETIMEOUT2; i++) {
            flushBuffer(false);
            read(null, inBuf.length, false);
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
        synchronized (this) {
            outCnt = 0;
            if (state == CS_EOF) state = CS_DISCONNECTING;
        }
        if (state == CS_DISCONNECTING) disconnect();
        synchronized (this) {
            while (state > CS_DISCONNECTED) try {
                wait();
            } catch (Exception e) {
            }
            state = CS_IDLE;
            inBuf = null;
            outBuf = null;
        }
    }

    /**
     * Discard and input
     * Dumps any input data
     */
    synchronized void discardInput() {
        do {
            inCnt = 0;
            inOffset = 0;
            try {
                wait(1);
            } catch (Exception e) {
            }
            fillBuffer(false);
        } while (inCnt > 0);
        setHeader(header);
    }

    /**
	 * Read a packet from the stream. Do not block and for small packets
	 * (< bufSz), do not return a partial packet.
	 * @param	buf		Buffer to read data into.
	 * @param	len		Number of bytes to read.
	 * @return			> 0 number of bytes read.
	 *					other values see read.
	 */
    public int readPacket(byte buf[], int len) {
        int pkt = available(1);
        if (pkt == -2) return -2;
        if (pkt < bufSz && available(0) < pkt) return 0;
        return read(buf, len, false);
    }

    /**
	 * Send a data packet.
	 * Must be in data mode.
	 * @param buf the data to send
	 * @param bufLen the number of bytes to send
     * @return number of bytes written
	 */
    public int sendPacket(byte[] buf, int bufLen) {
        if (bufLen <= outBuf.length - header) {
            return write(buf, bufLen, false);
        }
        return 0;
    }
}
