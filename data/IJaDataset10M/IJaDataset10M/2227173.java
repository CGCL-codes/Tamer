package etch.bindings.java.transport.fmt.binary;

import java.io.IOException;
import etch.bindings.java.msg.ComboValidator;
import etch.bindings.java.msg.Field;
import etch.bindings.java.msg.Message;
import etch.bindings.java.msg.StructValue;
import etch.bindings.java.msg.Type;
import etch.bindings.java.msg.Validator;
import etch.bindings.java.msg.ValueFactory;
import etch.bindings.java.support.Validator_int;
import etch.bindings.java.support.Validator_object;
import etch.bindings.java.support.Validator_string;
import etch.bindings.java.transport.ArrayValue;
import etch.bindings.java.transport.TaggedDataInput;
import etch.bindings.java.transport.fmt.TypeCode;
import etch.util.Assertion;
import etch.util.FlexBuffer;

/**
 * BinaryTaggedDataInput has methods to support reading tagged
 * values from an input buffer.
 */
public final class BinaryTaggedDataInput extends BinaryTaggedData implements TaggedDataInput {

    /**
	 * Constructs the BinaryTaggedDataInput with a null buffer.
	 * 
	 * @param vf the value factory for the service.
	 * @param uri the uri used to construct the transport stack.
	 */
    public BinaryTaggedDataInput(ValueFactory vf, String uri) {
        super(vf);
    }

    private FlexBuffer buf;

    private int lengthBudget;

    public Message readMessage(FlexBuffer buf) throws IOException {
        this.buf = buf;
        lengthBudget = buf.avail();
        try {
            Message msg = startMessage();
            readKeysAndValues(msg);
            endMessage(msg);
            return msg;
        } finally {
            this.buf = null;
            lengthBudget = 0;
        }
    }

    private StructValue readStruct() throws IOException {
        StructValue sv = startStruct();
        readKeysAndValues(sv);
        endStruct(sv);
        return sv;
    }

    private ArrayValue readArray(Validator v) throws IOException {
        ArrayValue av = startArray();
        readValues(av, v);
        endArray(av);
        return av;
    }

    private void readKeysAndValues(StructValue sv) throws IOException {
        Type t = sv.type();
        while (true) {
            Field key = readField(t);
            if (key == null) break;
            Validator v = t.getValidator(key);
            if (v != null) {
                sv.put(key, readValue(v));
            } else {
                Object obj = readValue(Validator_object.get(0));
                if (false) sv.put(key, obj);
            }
        }
    }

    private void readValues(ArrayValue av, Validator v) throws IOException {
        Validator ev = v.elementValidator();
        while (true) {
            Object value = readValue(ev, true);
            if (value == NONE) break;
            av.add(value);
        }
    }

    private Message startMessage() throws IOException {
        byte version = buf.getByte();
        if (version != VERSION) throw new IOException(String.format("binary tagged data version mismatch: got %d expected %d", version, VERSION));
        Type t = readType();
        int length = readLength();
        return new Message(t, vf, length);
    }

    private void endMessage(Message msg) {
    }

    private StructValue startStruct() throws IOException {
        Type t = readType();
        int length = readLength();
        return new StructValue(t, vf, length);
    }

    private void endStruct(StructValue struct) {
    }

    @SuppressWarnings("deprecation")
    private ArrayValue startArray() throws IOException {
        byte type = buf.getByte();
        Type customStructType;
        if (type == TypeCode.CUSTOM || type == TypeCode.STRUCT) customStructType = readType(); else customStructType = null;
        int dim = readIntegerValue();
        if (dim <= 0 || dim > Validator.MAX_NDIMS) throw new IllegalArgumentException("dim <= 0 || dim > Validator.MAX_NDIMS");
        int length = readLength();
        Object array = allocArrayValue(type, customStructType, dim, length);
        return new ArrayValue(array, type, customStructType, dim);
    }

    private void endArray(ArrayValue array) {
        array.compact();
    }

    private Type readType() throws IOException {
        Object obj = readValue(intOrStrValidator, false);
        if (obj instanceof Integer) {
            Integer id = (Integer) obj;
            Type type = vf.getType(id);
            if (type == null) type = new Type(id, id.toString());
            return type;
        }
        Assertion.check(obj instanceof String, "obj instanceof String");
        String name = (String) obj;
        Type type = vf.getType(name);
        if (type == null) type = new Type(name);
        return type;
    }

    private Field readField(Type type) throws IOException {
        Object obj = readValue(intOrStrValidator, true);
        if (obj == NONE) return null;
        if (obj instanceof Integer) {
            Integer id = (Integer) obj;
            Field field = type.getField(id);
            if (field == null) field = new Field(id, id.toString());
            return field;
        }
        Assertion.check(obj instanceof String, "obj instanceof String");
        String name = (String) obj;
        Field field = type.getField(name);
        if (field == null) field = new Field(name);
        return field;
    }

    private final Validator intOrStrValidator = new ComboValidator(Validator_int.get(0), Validator_string.get(0));

    private int readLength() throws IOException {
        int length = readIntegerValue();
        if (length < 0 || length > lengthBudget) throw new IllegalArgumentException("length < 0 || length > lengthBudget");
        lengthBudget -= length;
        return length;
    }

    private Integer readIntegerValue() throws IOException {
        return (Integer) readValue(intValidator);
    }

    private final Validator intValidator = Validator_int.get(0);

    private Object validateValue(Validator v, Object value) {
        if (v == null) return null;
        if (value == null) return null;
        return v.validateValue(value);
    }

    private Object validateValue(Validator v, boolean noneOk, Object value) {
        if (noneOk && value == NONE) return value;
        return validateValue(v, value);
    }

    private Object readValue(Validator v) throws IOException {
        return readValue(v, false);
    }

    @SuppressWarnings("deprecation")
    private Object readValue(Validator v, boolean noneOk) throws IOException {
        byte type = buf.getByte();
        switch(type) {
            case TypeCode.NULL:
                return validateValue(v, null);
            case TypeCode.NONE:
                return validateValue(v, noneOk, NONE);
            case TypeCode.BOOLEAN_FALSE:
                return validateValue(v, Boolean.FALSE);
            case TypeCode.BOOLEAN_TRUE:
                return validateValue(v, Boolean.TRUE);
            case TypeCode.BYTE:
                return validateValue(v, buf.getByte());
            case TypeCode.SHORT:
                return validateValue(v, buf.getShort());
            case TypeCode.INT:
                return validateValue(v, buf.getInt());
            case TypeCode.LONG:
                return validateValue(v, buf.getLong());
            case TypeCode.FLOAT:
                return validateValue(v, buf.getFloat());
            case TypeCode.DOUBLE:
                return validateValue(v, buf.getDouble());
            case TypeCode.BYTES:
                return validateValue(v, readBytes());
            case TypeCode.ARRAY:
                return validateValue(v, fromArrayValue(readArray(v)));
            case TypeCode.EMPTY_STRING:
                return validateValue(v, "");
            case TypeCode.STRING:
                return validateValue(v, new String(readBytes(), vf.getStringEncoding()));
            case TypeCode.STRUCT:
            case TypeCode.CUSTOM:
                return validateValue(v, vf.importCustomValue(readStruct()));
            default:
                if (type >= TypeCode.MIN_TINY_INT && type <= TypeCode.MAX_TINY_INT) return validateValue(v, type);
                throw new UnsupportedOperationException("unsupported type code " + type);
        }
    }

    private byte[] readBytes() throws IOException {
        int length = readLength();
        byte[] b = new byte[length];
        buf.getFully(b);
        return b;
    }
}
