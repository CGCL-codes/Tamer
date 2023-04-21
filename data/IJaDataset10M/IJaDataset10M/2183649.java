package org.chessworks.common.javatools.io;

import java.io.IOException;
import java.io.Writer;
import java.nio.channels.ClosedChannelException;

/**
 * A {@link Writer} which throws a {@link ClosedChannelException} everytime a
 * write attempt is made.
 *
 * Requires Java 1.5 and above.
 *
 * @author Doug Bateman ("DuckStorm")
 */
public class ClosedWriter extends Writer implements Cloneable {

    /**
	 * The value always returned by {@link #toString()}.
	 *
	 * @return "ClosedWriter"
	 */
    public static final String TO_STRING = "ClosedWriter";

    /**
	 * The value always returned by {@link #hashCode()}.
	 *
	 * @return "ClosedWriter".hashCode()
	 */
    public static final int HASH_CODE = TO_STRING.hashCode();

    /**
	 * The singleton instance. Clients will generally refer to use this instance
	 * instead of invoking the class constructor.
	 */
    public static ClosedWriter INSTANCE = new ClosedWriter();

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#append(char)
	 */
    @Override
    public Writer append(char c) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#append(java.lang.CharSequence, int, int)
	 */
    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#append(java.lang.CharSequence)
	 */
    @Override
    public Writer append(CharSequence csq) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Does nothing. The writer is by definition closed.
	 *
	 * @see java.io.Writer#close()
	 */
    @Override
    public void close() throws IOException {
    }

    /**
	 * Does nothing.
	 *
	 * @see java.io.Writer#flush()
	 */
    @Override
    public void flush() throws IOException {
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#write(char[], int, int)
	 */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#write(char[])
	 */
    @Override
    public void write(char[] cbuf) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#write(int)
	 */
    @Override
    public void write(int c) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#write(java.lang.String, int, int)
	 */
    @Override
    public void write(String str, int off, int len) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.Writer#write(java.lang.String)
	 */
    @Override
    public void write(String str) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Returns the value of the {@link #INSTANCE} constant.
	 *
	 * @return the value of the INSTANCE constant
	 * @see java.lang.Object#clone()
	 */
    @Override
    public ClosedWriter clone() {
        return INSTANCE;
    }

    /**
	 * Returns true if the other object is also a {@link ClosedWriter}.
	 *
	 * @return true if the other object is also a {@link ClosedWriter}.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass().equals(obj.getClass())) return true;
        return false;
    }

    /**
	 * Returns the value of the {@link #HASHCODE} constant.
	 *
	 * @return "ClosedWriter".hashCode()
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return HASH_CODE;
    }

    /**
	 * Returns the value of the {@link #TO_STRING} constant.
	 *
	 * @return "ClosedWriter"
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return TO_STRING;
    }
}
