package com.caucho.hessian.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

/**
 * Abstract base class for Hessian requests.  Hessian users should only
 * need to use the methods in this class.
 *
 * <pre>
 * AbstractHessianInput in = ...; // get input
 * String value;
 *
 * in.startReply();         // read reply header
 * value = in.readString(); // read string value
 * in.completeReply();      // read reply footer
 * </pre>
 */
public abstract class AbstractHessianInput {

    private HessianRemoteResolver resolver;

    private byte[] _buffer;

    /**
   * Initialize the Hessian stream with the underlying input stream.
   */
    public void init(InputStream is) {
    }

    /**
   * Returns the call's method
   */
    public abstract String getMethod();

    /**
   * Sets the resolver used to lookup remote objects.
   */
    public void setRemoteResolver(HessianRemoteResolver resolver) {
        this.resolver = resolver;
    }

    /**
   * Sets the resolver used to lookup remote objects.
   */
    public HessianRemoteResolver getRemoteResolver() {
        return resolver;
    }

    /**
   * Sets the serializer factory.
   */
    public void setSerializerFactory(SerializerFactory ser) {
    }

    /**
   * Reads the call
   *
   * <pre>
   * c major minor
   * </pre>
   */
    public abstract int readCall() throws IOException;

    /**
   * For backward compatibility with HessianSkeleton
   */
    public void skipOptionalCall() throws IOException {
    }

    /**
   * Reads a header, returning null if there are no headers.
   *
   * <pre>
   * H b16 b8 value
   * </pre>
   */
    public abstract String readHeader() throws IOException;

    /**
   * Starts reading the call
   *
   * <p>A successful completion will have a single value:
   *
   * <pre>
   * m b16 b8 method
   * </pre>
   */
    public abstract String readMethod() throws IOException;

    /**
   * Reads the number of method arguments
   *
   * @return -1 for a variable length (hessian 1.0)
   */
    public int readMethodArgLength() throws IOException {
        return -1;
    }

    /**
   * Starts reading the call, including the headers.
   *
   * <p>The call expects the following protocol data
   *
   * <pre>
   * c major minor
   * m b16 b8 method
   * </pre>
   */
    public abstract void startCall() throws IOException;

    /**
   * Completes reading the call
   *
   * <p>The call expects the following protocol data
   *
   * <pre>
   * Z
   * </pre>
   */
    public abstract void completeCall() throws IOException;

    /**
   * Reads a reply as an object.
   * If the reply has a fault, throws the exception.
   */
    public abstract Object readReply(Class expectedClass) throws Throwable;

    /**
   * Starts reading the reply
   *
   * <p>A successful completion will have a single value:
   *
   * <pre>
   * r
   * v
   * </pre>
   */
    public abstract void startReply() throws Throwable;

    /**
   * Starts reading the body of the reply, i.e. after the 'r' has been
   * parsed.
   */
    public void startReplyBody() throws Throwable {
    }

    /**
   * Completes reading the call
   *
   * <p>A successful completion will have a single value:
   *
   * <pre>
   * z
   * </pre>
   */
    public abstract void completeReply() throws IOException;

    /**
   * Reads a boolean
   *
   * <pre>
   * T
   * F
   * </pre>
   */
    public abstract boolean readBoolean() throws IOException;

    /**
   * Reads a null
   *
   * <pre>
   * N
   * </pre>
   */
    public abstract void readNull() throws IOException;

    /**
   * Reads an integer
   *
   * <pre>
   * I b32 b24 b16 b8
   * </pre>
   */
    public abstract int readInt() throws IOException;

    /**
   * Reads a long
   *
   * <pre>
   * L b64 b56 b48 b40 b32 b24 b16 b8
   * </pre>
   */
    public abstract long readLong() throws IOException;

    /**
   * Reads a double.
   *
   * <pre>
   * D b64 b56 b48 b40 b32 b24 b16 b8
   * </pre>
   */
    public abstract double readDouble() throws IOException;

    /**
   * Reads a date.
   *
   * <pre>
   * T b64 b56 b48 b40 b32 b24 b16 b8
   * </pre>
   */
    public abstract long readUTCDate() throws IOException;

    /**
   * Reads a string encoded in UTF-8
   *
   * <pre>
   * s b16 b8 non-final string chunk
   * S b16 b8 final string chunk
   * </pre>
   */
    public abstract String readString() throws IOException;

    /**
   * Reads an XML node encoded in UTF-8
   *
   * <pre>
   * x b16 b8 non-final xml chunk
   * X b16 b8 final xml chunk
   * </pre>
   */
    public org.w3c.dom.Node readNode() throws IOException {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
   * Starts reading a string.  All the characters must be read before
   * calling the next method.  The actual characters will be read with
   * the reader's read() or read(char [], int, int).
   *
   * <pre>
   * s b16 b8 non-final string chunk
   * S b16 b8 final string chunk
   * </pre>
   */
    public abstract Reader getReader() throws IOException;

    /**
   * Starts reading a byte array using an input stream.  All the bytes
   * must be read before calling the following method.
   *
   * <pre>
   * b b16 b8 non-final binary chunk
   * B b16 b8 final binary chunk
   * </pre>
   */
    public abstract InputStream readInputStream() throws IOException;

    /**
   * Reads data to an output stream.
   *
   * <pre>
   * b b16 b8 non-final binary chunk
   * B b16 b8 final binary chunk
   * </pre>
   */
    public boolean readToOutputStream(OutputStream os) throws IOException {
        InputStream is = readInputStream();
        if (is == null) return false;
        if (_buffer == null) _buffer = new byte[256];
        try {
            int len;
            while ((len = is.read(_buffer, 0, _buffer.length)) > 0) {
                os.write(_buffer, 0, len);
            }
            return true;
        } finally {
            is.close();
        }
    }

    /**
   * Reads a byte array.
   *
   * <pre>
   * b b16 b8 non-final binary chunk
   * B b16 b8 final binary chunk
   * </pre>
   */
    public abstract byte[] readBytes() throws IOException;

    /**
   * Reads an arbitrary object from the input stream.
   *
   * @param expectedClass the expected class if the protocol doesn't supply it.
   */
    public abstract Object readObject(Class expectedClass) throws IOException;

    /**
   * Reads an arbitrary object from the input stream.
   */
    public abstract Object readObject() throws IOException;

    /**
   * Reads a remote object reference to the stream.  The type is the
   * type of the remote interface.
   *
   * <code><pre>
   * 'r' 't' b16 b8 type url
   * </pre></code>
   */
    public abstract Object readRemote() throws IOException;

    /**
   * Reads a reference
   *
   * <pre>
   * R b32 b24 b16 b8
   * </pre>
   */
    public abstract Object readRef() throws IOException;

    /**
   * Adds an object reference.
   */
    public abstract int addRef(Object obj) throws IOException;

    /**
   * Sets an object reference.
   */
    public abstract void setRef(int i, Object obj) throws IOException;

    /**
   * Resets the references for streaming.
   */
    public void resetReferences() {
    }

    /**
   * Reads the start of a list
   */
    public abstract int readListStart() throws IOException;

    /**
   * Reads the length of a list.
   */
    public abstract int readLength() throws IOException;

    /**
   * Reads the start of a map
   */
    public abstract int readMapStart() throws IOException;

    /**
   * Reads an object type.
   */
    public abstract String readType() throws IOException;

    /**
   * Returns true if the data has ended.
   */
    public abstract boolean isEnd() throws IOException;

    /**
   * Read the end byte
   */
    public abstract void readEnd() throws IOException;

    /**
   * Read the end byte
   */
    public abstract void readMapEnd() throws IOException;

    /**
   * Read the end byte
   */
    public abstract void readListEnd() throws IOException;

    public void close() throws IOException {
    }
}
