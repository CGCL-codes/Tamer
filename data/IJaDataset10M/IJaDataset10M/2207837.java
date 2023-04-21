package org.bing.adapter.com.caucho.hessian.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract output stream for Hessian requests.
 *
 * <pre>
 * OutputStream os = ...; // from http connection
 * AbstractOutput out = new HessianSerializerOutput(os);
 * String value;
 *
 * out.startCall("hello");  // start hello call
 * out.writeString("arg1"); // write a string argument
 * out.completeCall();      // complete the call
 * </pre>
 */
public abstract class AbstractHessianOutput {

    private SerializerFactory _defaultSerializerFactory;

    protected SerializerFactory _serializerFactory;

    private byte[] _byteBuffer;

    /**
   * Sets the serializer factory.
   */
    public void setSerializerFactory(SerializerFactory factory) {
        _serializerFactory = factory;
    }

    /**
   * Gets the serializer factory.
   */
    public SerializerFactory getSerializerFactory() {
        if (_serializerFactory == _defaultSerializerFactory) {
            _serializerFactory = new SerializerFactory();
        }
        return _serializerFactory;
    }

    /**
   * Gets the serializer factory.
   */
    protected final SerializerFactory findSerializerFactory() {
        SerializerFactory factory = _serializerFactory;
        if (factory == null) {
            factory = SerializerFactory.createDefault();
            _defaultSerializerFactory = factory;
            _serializerFactory = factory;
        }
        return factory;
    }

    /**
   * Initialize the output with a new underlying stream.
   */
    public void init(OutputStream os) {
    }

    /**
   * Writes a complete method call.
   */
    public void call(String method, Object[] args) throws IOException {
        int length = args != null ? args.length : 0;
        startCall(method, length);
        for (int i = 0; i < length; i++) writeObject(args[i]);
        completeCall();
    }

    /**
   * Starts the method call:
   *
   * <code><pre>
   * C
   * </pre></code>
   *
   * @param method the method name to call.
   */
    public abstract void startCall() throws IOException;

    /**
   * Starts the method call:
   *
   * <code><pre>
   * C string int
   * </pre></code>
   *
   * @param method the method name to call.
   */
    public abstract void startCall(String method, int length) throws IOException;

    /**
   * For Hessian 2.0, use the Header envelope instead
   *
   * @deprecated
   */
    public void writeHeader(String name) throws IOException {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    /**
   * Writes the method tag.
   *
   * <code><pre>
   * string
   * </pre></code>
   *
   * @param method the method name to call.
   */
    public abstract void writeMethod(String method) throws IOException;

    /**
   * Completes the method call:
   *
   * <code><pre>
   * </pre></code>
   */
    public abstract void completeCall() throws IOException;

    /**
   * Writes a boolean value to the stream.  The boolean will be written
   * with the following syntax:
   *
   * <code><pre>
   * T
   * F
   * </pre></code>
   *
   * @param value the boolean value to write.
   */
    public abstract void writeBoolean(boolean value) throws IOException;

    /**
   * Writes an integer value to the stream.  The integer will be written
   * with the following syntax:
   *
   * <code><pre>
   * I b32 b24 b16 b8
   * </pre></code>
   *
   * @param value the integer value to write.
   */
    public abstract void writeInt(int value) throws IOException;

    /**
   * Writes a long value to the stream.  The long will be written
   * with the following syntax:
   *
   * <code><pre>
   * L b64 b56 b48 b40 b32 b24 b16 b8
   * </pre></code>
   *
   * @param value the long value to write.
   */
    public abstract void writeLong(long value) throws IOException;

    /**
   * Writes a double value to the stream.  The double will be written
   * with the following syntax:
   *
   * <code><pre>
   * D b64 b56 b48 b40 b32 b24 b16 b8
   * </pre></code>
   *
   * @param value the double value to write.
   */
    public abstract void writeDouble(double value) throws IOException;

    /**
   * Writes a date to the stream.
   *
   * <code><pre>
   * T  b64 b56 b48 b40 b32 b24 b16 b8
   * </pre></code>
   *
   * @param time the date in milliseconds from the epoch in UTC
   */
    public abstract void writeUTCDate(long time) throws IOException;

    /**
   * Writes a null value to the stream.
   * The null will be written with the following syntax
   *
   * <code><pre>
   * N
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeNull() throws IOException;

    /**
   * Writes a string value to the stream using UTF-8 encoding.
   * The string will be written with the following syntax:
   *
   * <code><pre>
   * S b16 b8 string-value
   * </pre></code>
   *
   * If the value is null, it will be written as
   *
   * <code><pre>
   * N
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeString(String value) throws IOException;

    /**
   * Writes a string value to the stream using UTF-8 encoding.
   * The string will be written with the following syntax:
   *
   * <code><pre>
   * S b16 b8 string-value
   * </pre></code>
   *
   * If the value is null, it will be written as
   *
   * <code><pre>
   * N
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeString(char[] buffer, int offset, int length) throws IOException;

    /**
   * Writes a byte array to the stream.
   * The array will be written with the following syntax:
   *
   * <code><pre>
   * B b16 b18 bytes
   * </pre></code>
   *
   * If the value is null, it will be written as
   *
   * <code><pre>
   * N
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeBytes(byte[] buffer) throws IOException;

    /**
   * Writes a byte array to the stream.
   * The array will be written with the following syntax:
   *
   * <code><pre>
   * B b16 b18 bytes
   * </pre></code>
   *
   * If the value is null, it will be written as
   *
   * <code><pre>
   * N
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeBytes(byte[] buffer, int offset, int length) throws IOException;

    /**
   * Writes a byte buffer to the stream.
   */
    public abstract void writeByteBufferStart() throws IOException;

    /**
   * Writes a byte buffer to the stream.
   *
   * <code><pre>
   * b b16 b18 bytes
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeByteBufferPart(byte[] buffer, int offset, int length) throws IOException;

    /**
   * Writes the last chunk of a byte buffer to the stream.
   *
   * <code><pre>
   * b b16 b18 bytes
   * </pre></code>
   *
   * @param value the string value to write.
   */
    public abstract void writeByteBufferEnd(byte[] buffer, int offset, int length) throws IOException;

    /**
   * Writes a full output stream.
   */
    public void writeByteStream(InputStream is) throws IOException {
        writeByteBufferStart();
        if (_byteBuffer == null) _byteBuffer = new byte[1024];
        byte[] buffer = _byteBuffer;
        int len;
        while ((len = is.read(buffer, 0, buffer.length)) > 0) {
            if (len < buffer.length) {
                int len2 = is.read(buffer, len, buffer.length - len);
                if (len2 < 0) {
                    writeByteBufferEnd(buffer, 0, len);
                    return;
                }
                len += len2;
            }
            writeByteBufferPart(buffer, 0, len);
        }
        writeByteBufferEnd(buffer, 0, 0);
    }

    /**
   * Writes a reference.
   *
   * <code><pre>
   * Q int
   * </pre></code>
   *
   * @param value the integer value to write.
   */
    protected abstract void writeRef(int value) throws IOException;

    /**
   * Removes a reference.
   */
    public boolean removeRef(Object obj) throws IOException {
        return false;
    }

    /**
   * Replaces a reference from one object to another.
   */
    public abstract boolean replaceRef(Object oldRef, Object newRef) throws IOException;

    /**
   * Adds an object to the reference list.  If the object already exists,
   * writes the reference, otherwise, the caller is responsible for
   * the serialization.
   *
   * <code><pre>
   * R b32 b24 b16 b8
   * </pre></code>
   *
   * @param object the object to add as a reference.
   *
   * @return true if the object has already been written.
   */
    public abstract boolean addRef(Object object) throws IOException;

    /**
   * @param obj
   * @return
   */
    public abstract int getRef(Object obj);

    /**
   * Resets the references for streaming.
   */
    public void resetReferences() {
    }

    /**
   * Writes a generic object to the output stream.
   */
    public abstract void writeObject(Object object) throws IOException;

    /**
   * Writes the list header to the stream.  List writers will call
   * <code>writeListBegin</code> followed by the list contents and then
   * call <code>writeListEnd</code>.
   *
   * <code><pre>
   * V
   *   x13 java.util.ArrayList   # type
   *   x93                       # length=3
   *   x91                       # 1
   *   x92                       # 2
   *   x93                       # 3
   * &lt;/list>
   * </pre></code>
   */
    public abstract boolean writeListBegin(int length, String type) throws IOException;

    /**
   * Writes the tail of the list to the stream.
   */
    public abstract void writeListEnd() throws IOException;

    /**
   * Writes the map header to the stream.  Map writers will call
   * <code>writeMapBegin</code> followed by the map contents and then
   * call <code>writeMapEnd</code>.
   *
   * <code><pre>
   * M type (<key> <value>)* Z
   * </pre></code>
   */
    public abstract void writeMapBegin(String type) throws IOException;

    /**
   * Writes the tail of the map to the stream.
   */
    public abstract void writeMapEnd() throws IOException;

    /**
   * Writes the object header to the stream (for Hessian 2.0), or a
   * Map for Hessian 1.0.  Object writers will call
   * <code>writeObjectBegin</code> followed by the map contents and then
   * call <code>writeObjectEnd</code>.
   *
   * <code><pre>
   * C type int <key>*
   * C int <value>*
   * </pre></code>
   *
   * @return true if the object has already been defined.
   */
    public int writeObjectBegin(String type) throws IOException {
        writeMapBegin(type);
        return -2;
    }

    /**
   * Writes the end of the class.
   */
    public void writeClassFieldLength(int len) throws IOException {
    }

    /**
   * Writes the tail of the object to the stream.
   */
    public void writeObjectEnd() throws IOException {
    }

    public void writeReply(Object o) throws IOException {
        startReply();
        writeObject(o);
        completeReply();
    }

    public void startReply() throws IOException {
    }

    public void completeReply() throws IOException {
    }

    public void writeFault(String code, String message, Object detail) throws IOException {
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
    }
}
