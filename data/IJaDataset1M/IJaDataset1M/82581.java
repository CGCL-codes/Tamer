package com.dyuproject.protostuff;

import static com.dyuproject.protostuff.StringSerializer.computeUTF8Size;
import java.io.IOException;

/**
 * Computes the size of the data to be written.
 * 
 * Used internally by {@link CodedOutput}.
 *
 * @author David Yu
 * @created Nov 9, 2009
 */
public final class ComputedSizeOutput implements Output {

    /**
     * Computes the serialized size of a message.
     */
    public static <T extends Message<T>> int getSize(T message) {
        return getSize(message, message.cachedSchema(), false);
    }

    /**
     * Computes the serialized size of a message tied to a schema.
     */
    public static <T> int getSize(T message, Schema<T> schema) {
        return getSize(message, schema, false);
    }

    /**
     * Computes the serialized size of a message tied to a schema.
     */
    public static <T> int getSize(T message, Schema<T> schema, boolean encodeNestedMessageAsGroup) {
        ComputedSizeOutput sizeCount = new ComputedSizeOutput(encodeNestedMessageAsGroup);
        try {
            schema.writeTo(sizeCount, message);
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException " + "(should never happen).", e);
        }
        return sizeCount.size;
    }

    private int size = 0;

    private final boolean encodeNestedMessageAsGroup;

    public ComputedSizeOutput() {
        this(false);
    }

    public ComputedSizeOutput(boolean encodeNestedMessageAsGroup) {
        this.encodeNestedMessageAsGroup = encodeNestedMessageAsGroup;
    }

    /**
     * Gets the size of the bytes written to this output.
     */
    public int getSize() {
        return size;
    }

    /**
     * Resets the size to zero.
     */
    ComputedSizeOutput reset() {
        size = 0;
        return this;
    }

    public void writeInt32(int fieldNumber, int value, boolean repeated) throws IOException {
        int s = ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT));
        s += value < 0 ? 10 : ProtobufOutput.computeRawVarint32Size(value);
        size += s;
    }

    public void writeUInt32(int fieldNumber, int value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint32Size(value);
    }

    public void writeSInt32(int fieldNumber, int value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint32Size(ProtobufOutput.encodeZigZag32(value));
    }

    public void writeFixed32(int fieldNumber, int value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32)) + ProtobufOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeSFixed32(int fieldNumber, int value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32)) + ProtobufOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeInt64(int fieldNumber, long value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint64Size(value);
    }

    public void writeUInt64(int fieldNumber, long value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint64Size(value);
    }

    public void writeSInt64(int fieldNumber, long value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT)) + ProtobufOutput.computeRawVarint64Size(ProtobufOutput.encodeZigZag64(value));
    }

    public void writeFixed64(int fieldNumber, long value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64)) + ProtobufOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeSFixed64(int fieldNumber, long value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64)) + ProtobufOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeFloat(int fieldNumber, float value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32)) + ProtobufOutput.LITTLE_ENDIAN_32_SIZE;
    }

    public void writeDouble(int fieldNumber, double value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64)) + ProtobufOutput.LITTLE_ENDIAN_64_SIZE;
    }

    public void writeBool(int fieldNumber, boolean value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_VARINT)) + 1;
    }

    public void writeEnum(int fieldNumber, int value, boolean repeated) throws IOException {
        writeInt32(fieldNumber, value, repeated);
    }

    public void writeString(int fieldNumber, String value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED));
        final int strSize = computeUTF8Size(value, 0, value.length());
        size += ProtobufOutput.computeRawVarint32Size(strSize) + strSize;
    }

    public void writeBytes(int fieldNumber, ByteString value, boolean repeated) throws IOException {
        writeByteArray(fieldNumber, value.getBytes(), repeated);
    }

    public void writeByteArray(int fieldNumber, byte[] value, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += ProtobufOutput.computeRawVarint32Size(value.length) + value.length;
    }

    public void writeByteRange(boolean utf8String, int fieldNumber, byte[] value, int offset, int length, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED));
        size += ProtobufOutput.computeRawVarint32Size(length) + length;
    }

    public <T> void writeObject(final int fieldNumber, final T value, final Schema<T> schema, final boolean repeated) throws IOException {
        if (encodeNestedMessageAsGroup) {
            writeObjectEncodedAsGroup(fieldNumber, value, schema, repeated);
            return;
        }
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED));
        final int last = size;
        schema.writeTo(this, value);
        final int actualSize = ProtobufOutput.computeRawVarint32Size(size - last);
        size += actualSize;
    }

    <T> void writeObjectEncodedAsGroup(int fieldNumber, T value, Schema<T> schema, boolean repeated) throws IOException {
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_START_GROUP));
        schema.writeTo(this, value);
        size += ProtobufOutput.computeRawVarint32Size(WireFormat.makeTag(fieldNumber, WireFormat.WIRETYPE_END_GROUP));
    }
}
