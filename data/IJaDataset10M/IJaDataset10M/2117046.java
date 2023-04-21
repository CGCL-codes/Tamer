package net.sourceforge.parser.avi;

import java.io.IOException;

/**
 * @author aza_sf@yahoo.com
 *
 * @version $Revision: 28 $
 */
public abstract class ByteStream {

    public abstract int read() throws IOException;

    public abstract int read(byte b[]) throws IOException;

    public abstract long skip(long n) throws IOException;

    public abstract void close() throws IOException;

    public abstract void setCounter(int n);

    public abstract long getCounter();

    public abstract void resetCounter();

    public abstract void position(long n) throws IOException;

    public abstract long position() throws IOException;
}
