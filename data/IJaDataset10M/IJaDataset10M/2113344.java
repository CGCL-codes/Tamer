package com.flazr.amf;

import com.flazr.util.ValueToEnum;
import java.util.Arrays;
import static com.flazr.amf.Amf0Value.Type.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Amf0Value {

    private static final Logger logger = LoggerFactory.getLogger(Amf0Value.class);

    private Amf0Value() {
    }

    public static enum Type implements ValueToEnum.IntValue {

        NUMBER(0x00), BOOLEAN(0x01), STRING(0x02), OBJECT(0x03), NULL(0x05), UNDEFINED(0x06), MAP(0x08), ARRAY(0x0A), DATE(0x0B), LONG_STRING(0x0C), UNSUPPORTED(0x0D);

        private final int value;

        private Type(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        private static final ValueToEnum<Type> converter = new ValueToEnum<Type>(Type.values());

        public static Type valueToEnum(final int value) {
            return converter.valueToEnum(value);
        }

        private static Type getType(final Object value) {
            if (value == null) {
                return NULL;
            } else if (value instanceof String) {
                return STRING;
            } else if (value instanceof Number) {
                return NUMBER;
            } else if (value instanceof Boolean) {
                return BOOLEAN;
            } else if (value instanceof Amf0Object) {
                return OBJECT;
            } else if (value instanceof Map) {
                return MAP;
            } else if (value instanceof Object[]) {
                return ARRAY;
            } else if (value instanceof Date) {
                return DATE;
            } else {
                throw new RuntimeException("unexpected type: " + value.getClass());
            }
        }
    }

    private static final byte BOOLEAN_TRUE = 0x01;

    private static final byte BOOLEAN_FALSE = 0x00;

    private static final byte[] OBJECT_END_MARKER = new byte[] { 0x00, 0x00, 0x09 };

    public static void encode(final ChannelBuffer out, final Object value) {
        final Type type = Type.getType(value);
        if (logger.isDebugEnabled()) {
            logger.debug(">> " + toString(type, value));
        }
        out.writeByte((byte) type.value);
        switch(type) {
            case NUMBER:
                if (value instanceof Double) {
                    out.writeLong(Double.doubleToLongBits((Double) value));
                } else {
                    out.writeLong(Double.doubleToLongBits(Double.valueOf(value.toString())));
                }
                return;
            case BOOLEAN:
                out.writeByte((Boolean) value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
                return;
            case STRING:
                encodeString(out, (String) value);
                return;
            case NULL:
                return;
            case MAP:
                out.writeInt(0);
            case OBJECT:
                final Map<String, Object> map = (Map) value;
                for (final Map.Entry<String, Object> entry : map.entrySet()) {
                    encodeString(out, entry.getKey());
                    encode(out, entry.getValue());
                }
                out.writeBytes(OBJECT_END_MARKER);
                return;
            case ARRAY:
                final Object[] array = (Object[]) value;
                out.writeInt(array.length);
                for (Object o : array) {
                    encode(out, o);
                }
                return;
            case DATE:
                final long time = ((Date) value).getTime();
                out.writeLong(Double.doubleToLongBits(time));
                out.writeShort((short) 0);
                return;
            default:
                throw new RuntimeException("unexpected type: " + type);
        }
    }

    private static String decodeString(final ChannelBuffer in) {
        final short size = in.readShort();
        final byte[] bytes = new byte[size];
        in.readBytes(bytes);
        return new String(bytes);
    }

    private static void encodeString(final ChannelBuffer out, final String value) {
        final byte[] bytes = value.getBytes();
        out.writeShort((short) bytes.length);
        out.writeBytes(bytes);
    }

    public static void encode(final ChannelBuffer out, final Object... values) {
        for (final Object value : values) {
            encode(out, value);
        }
    }

    public static Object decode(final ChannelBuffer in) {
        final Type type = Type.valueToEnum(in.readByte());
        final Object value = decode(in, type);
        if (logger.isDebugEnabled()) {
            logger.debug("<< " + toString(type, value));
        }
        return value;
    }

    private static Object decode(final ChannelBuffer in, final Type type) {
        switch(type) {
            case NUMBER:
                return Double.longBitsToDouble(in.readLong());
            case BOOLEAN:
                return in.readByte() == BOOLEAN_TRUE;
            case STRING:
                return decodeString(in);
            case ARRAY:
                final int arraySize = in.readInt();
                final Object[] array = new Object[arraySize];
                for (int i = 0; i < arraySize; i++) {
                    array[i] = decode(in);
                }
                return array;
            case MAP:
            case OBJECT:
                final int count;
                final Map<String, Object> map;
                if (type == MAP) {
                    count = in.readInt();
                    map = new LinkedHashMap<String, Object>();
                    if (count > 0 && logger.isDebugEnabled()) {
                        logger.debug("non-zero size for MAP type: {}", count);
                    }
                } else {
                    count = 0;
                    map = new Amf0Object();
                }
                int i = 0;
                final byte[] endMarker = new byte[3];
                while (in.readable()) {
                    in.getBytes(in.readerIndex(), endMarker);
                    if (Arrays.equals(endMarker, OBJECT_END_MARKER)) {
                        in.skipBytes(3);
                        if (logger.isDebugEnabled()) {
                            logger.debug("end MAP / OBJECT, found object end marker [000009]");
                        }
                        break;
                    }
                    if (count > 0 && i++ == count) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("stopping map decode after reaching count: {}", count);
                        }
                        break;
                    }
                    map.put(decodeString(in), decode(in));
                }
                return map;
            case DATE:
                final long dateValue = in.readLong();
                in.readShort();
                return new Date((long) Double.longBitsToDouble(dateValue));
            case LONG_STRING:
                final int stringSize = in.readInt();
                final byte[] bytes = new byte[stringSize];
                in.readBytes(bytes);
                return new String(bytes);
            case NULL:
            case UNDEFINED:
            case UNSUPPORTED:
                return null;
            default:
                throw new RuntimeException("unexpected type: " + type);
        }
    }

    private static String toString(final Type type, final Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(type).append(" ");
        if (type == Type.ARRAY) {
            sb.append(Arrays.toString((Object[]) value));
        } else {
            sb.append(value);
        }
        sb.append(']');
        return sb.toString();
    }
}
