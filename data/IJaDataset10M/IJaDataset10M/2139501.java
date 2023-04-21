package org.chessworks.common.javatools.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;

/**
 * A {@link BufferedReader} which throws a {@link ClosedChannelException}
 * everytime a read attempt is made.
 *
 * Requires Java 1.5 and above.
 *
 * @author Doug Bateman ("DuckStorm")
 */
public class ClosedBufferedReader extends BufferedReader implements Cloneable {

    /**
	 * The value always returned by {@link #toString()}.
	 *
	 * @return "ClosedReader"
	 */
    public static final String TO_STRING = "ClosedReader";

    /**
	 * The value always returned by {@link #hashCode()}.
	 *
	 * @return "ClosedReader".hashCode()
	 */
    public static final int HASH_CODE = TO_STRING.hashCode();

    /**
	 * The singleton instance. Clients will generally refer to use this instance
	 * instead of invoking the class constructor.
	 */
    public static ClosedReader INSTANCE = new ClosedReader();

    /**
	 * Constructs a new NullBufferedReader.
	 */
    public ClosedBufferedReader() {
        super(new StringReader(""), 1);
    }

    /**
	 * Does nothing. The reader is by definition closed.
	 *
	 * @see java.io.BufferedReader#close()
	 */
    @Override
    public void close() throws IOException {
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#mark(int)
	 */
    @Override
    public void mark(int readAheadLimit) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Returns false, indicating that attempts to use bookmarks in the stream
	 * throw exceptions.
	 *
	 * @return false
	 * @see java.io.BufferedReader#markSupported()
	 */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#read()
	 */
    @Override
    public int read() throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#read(char[], int, int)
	 */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#read(char[])
	 */
    @Override
    public int read(char[] cbuf) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#read(java.nio.CharBuffer)
	 */
    @Override
    public int read(CharBuffer target) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#readLine()
	 */
    @Override
    public String readLine() throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Returns true, indicating the next read won't block (because it'll throw
	 * an exception.
	 *
	 * @return true
	 * @see java.io.BufferedReader#ready()
	 */
    @Override
    public boolean ready() throws IOException {
        return true;
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#reset()
	 */
    @Override
    public void reset() throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Throws a ClosedChannelException();
	 *
	 * @throws ClosedChannelException
	 *             always
	 * @see java.io.BufferedReader#skip(long)
	 */
    @Override
    public long skip(long n) throws IOException {
        throw new ClosedChannelException();
    }

    /**
	 * Returns the value of the {@link #INSTANCE} constant.
	 *
	 * @return the value of the INSTANCE constant
	 * @see java.lang.Object#clone()
	 */
    @Override
    public ClosedReader clone() {
        return INSTANCE;
    }

    /**
	 * Returns true if the other object is also a {@link ClosedReader}.
	 *
	 * @return true if the other object is also a {@link ClosedReader}.
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
	 * @return "ClosedReader".hashCode()
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return HASH_CODE;
    }

    /**
	 * Returns the value of the {@link #TO_STRING} constant.
	 *
	 * @return "ClosedReader"
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return TO_STRING;
    }
}
