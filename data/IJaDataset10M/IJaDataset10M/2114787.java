package jdbm.extser;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Used to assign classId codes to Java primitives, to arrays of Java
 * primitives, and to <code>Object[]</code>. Also provides some utility
 * methods for converting between a classId, a label for the class, and an
 * object that is an instance of the class.
 * 
 * @author <a href="mailto:thompsonbry@users.sourceforge.net">Bryan Thompson</a>
 * @version $Id: NativeType.java,v 1.4 2006/05/02 16:20:07 thompsonbry Exp $
 */
public class NativeType {

    /**
     * A <code>null</code>.
     */
    public static final int NULL = 0x0;

    public static final int BOOLEAN = 0x1;

    public static final int BYTE = 0x2;

    public static final int CHAR = 0x3;

    public static final int SHORT = 0x4;

    public static final int INT = 0x5;

    public static final int LONG = 0x6;

    public static final int FLOAT = 0x7;

    public static final int DOUBLE = 0x8;

    public static final int BOOLEAN_ARRAY = 0x9;

    public static final int BYTE_ARRAY = 0xa;

    public static final int CHAR_ARRAY = 0xb;

    public static final int SHORT_ARRAY = 0xc;

    public static final int INT_ARRAY = 0xd;

    public static final int LONG_ARRAY = 0xe;

    public static final int FLOAT_ARRAY = 0xf;

    public static final int DOUBLE_ARRAY = 0x10;

    /**
     * An array of Java objects (vs an array of primitives).
     */
    public static final int OBJECT_ARRAY = 0x11;

    /**
     * The starting classId index assigned to Java Objects.
     */
    public static final int FIRST_OBJECT_INDEX = 0x12;

    /**
     * Return true if the <i>classId </i> identifies a Java Object (vs a
     * Java primitive or an Array type).
     * 
     * @param classId The classId.
     */
    public static final boolean isJavaObject(int classId) {
        return classId >= FIRST_OBJECT_INDEX;
    }

    /**
     * Return true iff the <i>classId </i> corresponds to a
     * <code>null</code>.
     * 
     * @param classId The classId.
     */
    public static final boolean isNull(int classId) {
        return classId == NULL;
    }

    /**
     * Return true iff the <i>classId </i> corresponds to one of the Java
     * primitive datatypes {boolean, byte, char, short, int, long, float, or
     * double}.
     * 
     * @param classId The classId.
     */
    public static final boolean isPrimitive(int classId) {
        return classId >= BOOLEAN && classId <= DOUBLE;
    }

    /**
     * Return true iff the <i>classId </i> corresponds to one of the
     * primitive array types, eg, <code>byte[]</code>.
     * 
     * @param classId
     *            The classId.
     */
    public static final boolean isPrimitiveArrayType(int classId) {
        return classId >= BOOLEAN_ARRAY && classId <= DOUBLE_ARRAY;
    }

    /**
     * Return true iff the <i>classId </i> corresponds to one of the
     * primitive array types, eg, <code>byte[]</code> or to an array of
     * some class, eg, <code>Object[]</code>,<code>String[]</code>,
     * etc.
     * 
     * @param classId
     *            The classId.
     */
    public static final boolean isArrayType(int classId) {
        return classId >= BOOLEAN_ARRAY && classId <= OBJECT_ARRAY;
    }

    /**
     * A human consumable name for the corresponding class.
     * 
     * @param classId The classId.
     */
    public static String asString(int classId) {
        switch(classId) {
            case NULL:
                return "null";
            case BOOLEAN:
                return java.lang.Boolean.class.getName();
            case BYTE:
                return java.lang.Byte.class.getName();
            case CHAR:
                return java.lang.Character.class.getName();
            case SHORT:
                return java.lang.Short.class.getName();
            case INT:
                return java.lang.Integer.class.getName();
            case LONG:
                return java.lang.Long.class.getName();
            case FLOAT:
                return java.lang.Float.class.getName();
            case DOUBLE:
                return java.lang.Double.class.getName();
            case BOOLEAN_ARRAY:
                return "boolean[]";
            case BYTE_ARRAY:
                return "byte[]";
            case CHAR_ARRAY:
                return "char[]";
            case SHORT_ARRAY:
                return "short[]";
            case INT_ARRAY:
                return "int[]";
            case LONG_ARRAY:
                return "long[]";
            case FLOAT_ARRAY:
                return "float[]";
            case DOUBLE_ARRAY:
                return "double[]";
            case OBJECT_ARRAY:
                return "Object[]";
            default:
                return "Object<" + classId + ">";
        }
    }

    /**
     * Return the {@link NativeType} for a Java Object.
     * 
     * @param val
     *            The Java Object (including arrays, nulls, etc.).
     * 
     * @return The {@link NativeType}
     * 
     * @exception UnsupportedOperationException
     *                if there is no native type for that Java Object.
     */
    public static int getNativeType(Object val) {
        if (val == null) return NULL; else if (val instanceof Boolean) return BOOLEAN; else if (val instanceof Character) return CHAR; else if (val instanceof Number) {
            if (val instanceof Byte) return BYTE; else if (val instanceof Short) return SHORT; else if (val instanceof Integer) return INT; else if (val instanceof Long) return LONG; else if (val instanceof Float) return FLOAT; else if (val instanceof Double) return DOUBLE; else return FIRST_OBJECT_INDEX;
        } else if (val.getClass().isArray()) {
            if (val.getClass().getComponentType().isPrimitive()) {
                Class componentType = val.getClass().getComponentType();
                if (componentType.equals(Boolean.TYPE)) return BOOLEAN_ARRAY; else if (componentType.equals(Byte.TYPE)) return BYTE_ARRAY; else if (componentType.equals(Character.TYPE)) return CHAR_ARRAY; else if (componentType.equals(Short.TYPE)) return SHORT_ARRAY; else if (componentType.equals(Integer.TYPE)) return INT_ARRAY; else if (componentType.equals(Long.TYPE)) return LONG_ARRAY; else if (componentType.equals(Float.TYPE)) return FLOAT_ARRAY; else if (componentType.equals(Double.TYPE)) return DOUBLE_ARRAY; else throw new AssertionError();
            } else {
                return OBJECT_ARRAY;
            }
        } else {
            return FIRST_OBJECT_INDEX;
        }
    }

    /**
     * Serialize a null object wrapping a Java primitive or an array type on
     * the output stream.
     * 
     * @param dos
     * @param classId
     *            The classId assigned to that object.
     * @param obj
     *            The object.
     * 
     * FIXME Add multi-dimensional array serialization (and test cases). We
     *       need to write out the #of dimensions and the length of each
     *       dimension before we write out the elements in the array.
     *       Traversal itself is difficult (and perhaps even impossible w/in
     *       Java) since we need to cast to a strongly typed array type
     *       before we can index into the array. If we can't support this in
     *       any optimized manner then we need to detect multidimensional
     *       arrays and route them through Java serialization.
     */
    public static void serialize(DataOutput dos, int classId, Object obj) throws IOException {
        switch(classId) {
            case NULL:
                return;
            case BOOLEAN:
                dos.writeBoolean(((Boolean) obj).booleanValue());
                return;
            case BYTE:
                dos.writeByte(((Byte) obj).byteValue());
                return;
            case CHAR:
                dos.writeChar(((Character) obj).charValue());
                return;
            case SHORT:
                dos.writeShort(((Short) obj).shortValue());
                return;
            case INT:
                dos.writeInt(((Integer) obj).intValue());
                return;
            case LONG:
                dos.writeLong(((Long) obj).longValue());
                return;
            case FLOAT:
                dos.writeFloat(((Float) obj).floatValue());
                return;
            case DOUBLE:
                dos.writeDouble(((Double) obj).doubleValue());
                return;
            case BOOLEAN_ARRAY:
                {
                    final boolean[] ary = (boolean[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeBoolean(ary[i]);
                    }
                    return;
                }
            case BYTE_ARRAY:
                {
                    final byte[] ary = (byte[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeByte(ary[i]);
                    }
                    return;
                }
            case CHAR_ARRAY:
                {
                    final char[] ary = (char[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeChar(ary[i]);
                    }
                    return;
                }
            case SHORT_ARRAY:
                {
                    final short[] ary = (short[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeShort(ary[i]);
                    }
                    return;
                }
            case INT_ARRAY:
                {
                    final int[] ary = (int[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeInt(ary[i]);
                    }
                    return;
                }
            case LONG_ARRAY:
                {
                    final long[] ary = (long[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeLong(ary[i]);
                    }
                    return;
                }
            case FLOAT_ARRAY:
                {
                    final float[] ary = (float[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeFloat(ary[i]);
                    }
                    return;
                }
            case DOUBLE_ARRAY:
                {
                    final double[] ary = (double[]) obj;
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeDouble(ary[i]);
                    }
                    return;
                }
            case OBJECT_ARRAY:
                {
                    final Object[] ary = (Object[]) obj;
                    final Class componentType = ary.getClass().getComponentType();
                    final int componentTypeId = dos.getSerializationHandler().getClassId(componentType);
                    dos.writePackedInt(componentTypeId);
                    final int len = ary.length;
                    dos.writePackedInt(len);
                    for (int i = 0; i < len; i++) {
                        dos.serialize(ary[i]);
                    }
                    return;
                }
            default:
                throw new AssertionError("classId=" + classId);
        }
    }

    /**
     * Reads a Java primitive or array type from the input stream.
     * 
     * @param dis
     * @param classId
     * @param versionId
     * @return The deserialized object.
     * @throws IOException
     */
    public static Object deserialize(DataInput dis, int classId, short versionId) throws IOException {
        switch(classId) {
            case NULL:
                return null;
            case BOOLEAN:
                {
                    if (dis.readBoolean()) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                }
            case BYTE:
                return new Byte(dis.readByte());
            case CHAR:
                return new Character(dis.readChar());
            case SHORT:
                return new Short(dis.readShort());
            case INT:
                return new Integer(dis.readInt());
            case LONG:
                return new Long(dis.readLong());
            case FLOAT:
                return new Float(dis.readFloat());
            case DOUBLE:
                return new Double(dis.readDouble());
            case BOOLEAN_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final boolean[] ary = new boolean[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readBoolean();
                    }
                    return ary;
                }
            case BYTE_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final byte[] ary = new byte[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readByte();
                    }
                    return ary;
                }
            case CHAR_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final char[] ary = new char[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readChar();
                    }
                    return ary;
                }
            case SHORT_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final short[] ary = new short[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readShort();
                    }
                    return ary;
                }
            case INT_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final int[] ary = new int[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readInt();
                    }
                    return ary;
                }
            case LONG_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final long[] ary = new long[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readLong();
                    }
                    return ary;
                }
            case FLOAT_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final float[] ary = new float[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readFloat();
                    }
                    return ary;
                }
            case DOUBLE_ARRAY:
                {
                    final int len = dis.readPackedInt();
                    final double[] ary = new double[len];
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.readDouble();
                    }
                    return ary;
                }
            case OBJECT_ARRAY:
                {
                    final int componentTypeId = dis.readPackedInt();
                    final Class componentType = dis.getSerializationHandler().getClass(componentTypeId);
                    final int len = dis.readPackedInt();
                    final Object[] ary = (Object[]) Array.newInstance(componentType, len);
                    for (int i = 0; i < len; i++) {
                        ary[i] = dis.deserialize();
                    }
                    return ary;
                }
            default:
                throw new AssertionError("classId=" + classId);
        }
    }
}
