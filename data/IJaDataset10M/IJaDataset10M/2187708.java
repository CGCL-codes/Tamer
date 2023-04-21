package com.dyuproject.protostuff.me;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Common io utils for between protostuff and protobuf formats.
 *
 * @author David Yu
 * @created Nov 12, 2009
 */
final class IOUtil {

    private IOUtil() {
    }

    /**
     * Merges the {@code message} with the byte array using the given {@code schema}.
     */
    static void mergeFrom(byte[] data, int offset, int length, Object message, Schema schema, boolean decodeNestedMessageAsGroup) {
        try {
            final ByteArrayInput input = new ByteArrayInput(data, offset, length, decodeNestedMessageAsGroup);
            schema.mergeFrom(input, message);
            input.checkLastTagWas(0);
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " + "never happen).");
        } catch (IOException e) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should " + "never happen).");
        }
    }

    /**
     * Merges the {@code message} from the {@link InputStream} with the supplied 
     * {@code buf} to use.
     */
    static void mergeFrom(InputStream in, byte[] buf, Object message, Schema schema, boolean decodeNestedMessageAsGroup) throws IOException {
        final CodedInput input = new CodedInput(in, buf, decodeNestedMessageAsGroup);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }

    /**
     * Merges the {@code message} from the {@link InputStream} using the given {@code schema}.
     */
    static void mergeFrom(InputStream in, Object message, Schema schema, boolean decodeNestedMessageAsGroup) throws IOException {
        final CodedInput input = new CodedInput(in, decodeNestedMessageAsGroup);
        schema.mergeFrom(input, message);
        input.checkLastTagWas(0);
    }

    /**
     * The {@code buf} size limits the size of the message that must be read.
     * A ProtobufException (sizeLimitExceeded) will be thrown if the 
     * size of the delimited message is larger.
     */
    static void mergeDelimitedFrom(InputStream in, byte[] buf, Object message, Schema schema, boolean decodeNestedMessageAsGroup) throws IOException {
        final int size = in.read();
        if (size == -1) throw new EOFException("mergeDelimitedFrom");
        final int len = size < 0x80 ? size : CodedInput.readRawVarint32(in, size);
        if (len != 0) {
            if (len > buf.length) {
                throw new ProtobufException("size limit exceeded. " + len + " > " + buf.length);
            }
            fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len, decodeNestedMessageAsGroup);
            try {
                schema.mergeFrom(input, message);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw ProtobufException.truncatedMessage(e);
            }
            input.checkLastTagWas(0);
        }
    }

    /**
     * Merges the {@code message} (delimited) from the {@link InputStream} 
     * using the given {@code schema}.
     */
    static void mergeDelimitedFrom(InputStream in, Object message, Schema schema, boolean decodeNestedMessageAsGroup) throws IOException {
        final int size = in.read();
        if (size == -1) throw new EOFException("mergeDelimitedFrom");
        final int len = size < 0x80 ? size : CodedInput.readRawVarint32(in, size);
        if (len != 0) {
            if (len > CodedInput.DEFAULT_BUFFER_SIZE) {
                final CodedInput input = new CodedInput(new LimitedInputStream(in, len), decodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
                return;
            }
            byte[] buf = new byte[len];
            fillBufferFrom(in, buf, 0, len);
            final ByteArrayInput input = new ByteArrayInput(buf, 0, len, decodeNestedMessageAsGroup);
            try {
                schema.mergeFrom(input, message);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw ProtobufException.truncatedMessage(e);
            }
            input.checkLastTagWas(0);
        }
    }

    /**
     * Used by the code generated messages that implement {@link java.io.Externalizable}.
     * Merges from the {@link DataInput}.
     */
    static void mergeDelimitedFrom(DataInput in, Object message, Schema schema, boolean decodeNestedMessageAsGroup) throws IOException {
        final byte size = in.readByte();
        final int len = 0 == (size & 0x80) ? size : CodedInput.readRawVarint32(in, size);
        if (len != 0) {
            if (len > CodedInput.DEFAULT_BUFFER_SIZE && in instanceof InputStream) {
                final CodedInput input = new CodedInput(new LimitedInputStream((InputStream) in, len), decodeNestedMessageAsGroup);
                schema.mergeFrom(input, message);
                input.checkLastTagWas(0);
            } else {
                final byte[] buf = new byte[len];
                in.readFully(buf, 0, len);
                final ByteArrayInput input = new ByteArrayInput(buf, 0, len, decodeNestedMessageAsGroup);
                try {
                    schema.mergeFrom(input, message);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw ProtobufException.truncatedMessage(e);
                }
                input.checkLastTagWas(0);
            }
        }
        if (!schema.isInitialized(message)) throw new UninitializedMessageException(message, schema);
    }

    /**
     * Fills the byte buffer from the {@link InputStream} with the specified length.
     */
    static void fillBufferFrom(InputStream in, byte[] buf, int offset, int len) throws IOException {
        for (int read = 0; len > 0; len -= read, offset += read) {
            read = in.read(buf, offset, len);
            if (read == -1) throw ProtobufException.truncatedMessage();
        }
    }
}
