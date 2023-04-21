package com.parfumball.file;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.parfumball.pcap.NetworkInterface;
import com.parfumball.pcap.Packet;
import com.parfumball.pcap.PcapException;

/**
 * A basic PcapInputStream that can be extended to read 
 * packet capture files of specific types. We assume that all
 * packet capture files have essentially two portions:<p>
 * 
 * 1. A file header - that has general information regarding the
 *    packet capture. This includes some sort of a magic identifier
 *    to identify the type of file and the network interface type on
 *    which the packets were captured. The network interface type is
 *    usually specified as an integer - the same interface type may be
 *    mapped as different values in different file types.<br>
 * 
 * 2. The capture data - each packet in the capture data is 
 *    generally prefixed with some sort of packet record header that
 *    includes the time at which the packet was captured and the real
 *    and captured length. Some packet sniffers (e.g., snoop) also add
 * 	  padding bytes to pad each packet.<p>
 * 
 * A PcapInputStream instance has the following life cycle - it is 
 * first instantiated. Then its open() method is called. This is when
 * it should read the file header - or throw an IOException or a 
 * PcapException. Then, its getNextPacket() method is called repeatedly
 * until either all packets are read or until an exception occurs.
 * After all packets have been read (or whenever appropriate), the close()
 * method is called.
 *  
 * @author prasanna
 */
public abstract class PcapInputStream {

    /**
     * The magic identifier used to identify the type of the file 
     * varies from one packet sniffer to another. Some use integers
     * (e.g., libpcap), others use strings (e.g., snoop). Since all
     * integers can also be represented as strings, I chose to 
     * represent the magic identifier as a string. There is a slight
     * overhead in terms of comparison for equality but it is acceptable.
     */
    protected String magic;

    /**
     * The network interface on which the packets were captured.
     * The network interface is typically a dummy object. Use
     * after open() is called on this instance.
     */
    protected NetworkInterface iface;

    /**
     * The packet capture length specified at capture time. Use after
     * open()ing this object. 
     */
    protected int snaplen;

    /**
     * The underlying DataInputStream. This is used to wrap around
     * the InputStream parameter provided in the open() method.
     */
    protected DataInputStream in;

    /**
     * Returns true if this PcapInputStream is closed.
     */
    protected boolean closed;

    /**
     * The name assigned to this PcapInputStream. Generally the name
     * of the file from which the packets are read.
     */
    protected String name;

    /**
     * The serial number of the packet. A running count that starts from 1.
     */
    protected int serialNo;

    /**
     * Create an instance of a PcapInputStream. This
     * implementation does nothing.
     */
    protected PcapInputStream() {
        super();
    }

    /**
     * Create an instance of a PcapInputStream with the given name.
     * 
     * @param name
     */
    protected PcapInputStream(String name) {
        this.name = name;
    }

    /**
     * Open this PcapInputStream using the underlying InputStream.
     * Concrete subsclasses will typically wrap <code>in</code>
     * into a DataInputStream to be able to conveniently read
     * typed data (e.g., integers, shorts etc.) A concrete implementation
     * may assume that the read offset is pointing to the logical beginning 
     * of the InputStream - that is, it can expect that the next read
     * will result in reading the magic identifier. A concrete implementation
     * must read any magic identifier followed by the file header. The
     * implementation may throw an exception if an error occurs. An IOException
     * must be thrown on I/O errors (EOF etc.) A PcapException must be
     * thrown if an application (logical) error occurs (e.g, unrecognized file
     * type). 
     * 
     * @param in The InputStream from which to read.
     * @throws IOException If an IO error occurs reading the file.
     * @throws PcapException If the implementation encounters a logical error
     * reading the file (e.g., invalid data or unrecognized type).
     */
    public void open(InputStream input) throws IOException, PcapException {
        if (input == null) {
            throw new NullPointerException("InputStream argument is null.");
        }
        if (closed) {
            throw new PcapException("PcapInputStream is closed.");
        }
        in = new DataInputStream(input);
        readMagic();
        readFileHeader();
    }

    /**
     * Read the magic header (if any). Concrete subclasses must set the
     * <code>magic</code> member in this method.
     * 
     * @throws IOException
     * @throws PcapException
     */
    protected abstract void readMagic() throws IOException, PcapException;

    /**
     * Read the file header (if any). Concrete subclasses must set the
     * other file header related members in this method.
     * 
     * @throws IOException
     * @throws PcapException
     */
    protected abstract void readFileHeader() throws IOException, PcapException;

    /**
     * Returns the next Packet after reading it from the underlying 
     * InputStream. Concrete implementations must return null if the end
     * of file has been reached. If an I/O error occurs an IOException is
     * thrown. If a logical error occurs, a PcapException is thrown.
     * 
     * @return The next Packet or null if no more packets are available.
     * @throws IOException If an I/O error occurs.
     * @throws PcapException If an application error occurs.
     */
    public abstract Packet getNextPacket() throws IOException, PcapException;

    /**
     * Returns a friendly description of this PcapInputStream type. The
     * implementation of this method returns null.
     * 
     * @return
     */
    public String getDescription() {
        return null;
    }

    /**
     * Returns the name set on this PcapInputStream. Will return null
     * if no name has been set.
     * 
     * @return
     */
    public String getName() {
        return null;
    }

    /**
     * Sets the name of this PcapInputStream to the given name. 
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Closes the underlying InputStream. After close() has been called
     * the PcapInputStream object must not be used anymore.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        if (closed) {
            return;
        }
        try {
            in.close();
        } finally {
            closed = true;
        }
    }

    /**
     * Returns true if this PcapInputStream is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Returns the NetworkInterface that abstracts the 
     * one on which the packets were captured. This is not a 
     * real NetworkInterface. Will return null if called
     * on an unopened PcapInputStream.
     * 
     * @return Returns the iface.
     */
    public NetworkInterface getIface() {
        return iface;
    }

    /**
     * Returns the magic identifier. Returns null if 
     * this PcapInputStream has not been opened.
     * 
     * @return Returns the magic.
     */
    public String getMagic() {
        return magic;
    }

    /**
     * Returns the snaplen - the number of bytes that were asked
     * to be captured. Will return 0 if this PcapInputStream has not
     * been opened.
     * 
     * @return Returns the snaplen.
     */
    public int getSnaplen() {
        return snaplen;
    }
}
