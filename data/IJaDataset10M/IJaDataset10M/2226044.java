package com.martiansoftware.nailgun;

import java.io.IOException;

/**
 * Wraps an OutputStream to send writes in NailGun chunks.  Because
 * multiple NGOutputStreams wrap the same OutputStream (that is, 
 * the OutputStream obtained from the Socket connection with
 * the client), writes are synchronized on the underlying OutputStream.
 * If this were not the case, write interleaving could completely
 * break the NailGun protocol.
 * 
 * @author <a href="http://www.martiansoftware.com/contact.html">Marty Lamb</a>
 */
class NGOutputStream extends java.io.DataOutputStream {

    private final Object lock;

    private byte streamCode;

    /**
	 * Creates a new NGOutputStream wrapping the specified
	 * OutputStream and using the specified Nailgun chunk code.
	 * @param out the OutputStream to wrap
	 * @param streamCode the NailGun chunk code associated with this
	 * stream (i.e., '1' for stdout, '2' for stderr).
	 */
    public NGOutputStream(java.io.OutputStream out, byte streamCode) {
        super(out);
        this.lock = out;
        this.streamCode = streamCode;
    }

    /**
	 * @see java.io.OutputStream.write(byte[])
	 */
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /**
	 * @see java.io.OutputStream.write(int)
	 */
    public void write(int b) throws IOException {
        byte[] b2 = { (byte) b };
        write(b2, 0, 1);
    }

    /**
	 * @see java.io.OutputStream.write(byte[],int,int)
	 */
    public void write(byte[] b, int offset, int len) throws IOException {
        synchronized (lock) {
            writeInt(len);
            writeByte(streamCode);
            out.write(b, offset, len);
        }
        flush();
    }
}
