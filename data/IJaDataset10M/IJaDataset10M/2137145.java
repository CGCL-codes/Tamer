package com.simpledata.filetools.encoders;

import java.io.IOException;
import java.io.OutputStream;
import com.simpledata.filetools.Secu;

/**
 * Counts the bytes that are written to a given output stream 
 * and informs a monitor about it. 
 *
 * @version $Id: ZCountOS.java,v 1.2 2007/04/02 17:04:25 perki Exp $
 * @author Simpledata SARL, 2004, all rights reserved. 
 */
public class ZCountOS extends OutputStream {

    OutputStream os;

    Counted c;

    public ZCountOS(Secu.Monitor monitor, String title, OutputStream os, long zeroAt, long finalAt) {
        c = new Counted(monitor, title, zeroAt, finalAt);
        this.os = os;
    }

    /**
	 * @see java.io.OutputStream#write(int)
	 */
    public void write(int b) throws IOException {
        os.write(c.count(b));
    }

    public void close() throws IOException {
        c.count(-1);
        os.close();
    }

    public void write(byte b[]) throws IOException {
        c.countPlus(b.length);
        os.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        c.countPlus(len);
        os.write(b, off, len);
    }

    public void flush() throws IOException {
        os.flush();
    }
}
