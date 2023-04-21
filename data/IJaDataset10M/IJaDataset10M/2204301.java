package com.googlecode.dni.internal.structure;

import static org.objectweb.asm.Opcodes.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import com.googlecode.dni.internal.DniInternal;
import com.googlecode.dni.internal.MemoryAccess;
import com.googlecode.dni.type.nativetype.NativeChar;
import com.googlecode.dni.type.nativetype.NativeInt;
import com.googlecode.dni.type.nativetype.NativeLong;
import com.googlecode.dni.type.nativetype.NativeShort;
import com.googlecode.dni.type.nativetype.NativeTypes;
import com.googlecode.dni.type.nativetype.SizeT;
import com.googlecode.dni.type.nativetype.WCharT;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * <p>
 *  Encapsulates details of a primitive type.
 * </p>
 *
 * @author Matthew Wilson
 */
final class PrimitiveType {

    private static final Map<Class<?>, PrimitiveType> TYPES = new HashMap<Class<?>, PrimitiveType>();

    static {
        put(new PrimitiveType(boolean.class, Opcodes.IRETURN, "__bool__ uint32_t", "Boolean", 'Z', 4));
        put(new PrimitiveType(byte.class, Opcodes.IRETURN, "int8_t", "Byte", 'B', 1));
        put(new PrimitiveType(short.class, Opcodes.IRETURN, "int16_t", "Short", 'S', 2));
        put(new PrimitiveType(char.class, Opcodes.IRETURN, "uint16_t", "Char", 'C', 2));
        put(new PrimitiveType(int.class, Opcodes.IRETURN, "int32_t", "Int", 'I', 4));
        put(new PrimitiveType(long.class, Opcodes.LRETURN, "int64_t", "Long", 'J', 8));
        put(new PrimitiveType(float.class, Opcodes.FRETURN, "float", "Float", 'F', 4));
        put(new PrimitiveType(double.class, Opcodes.DRETURN, "double", "Double", 'D', 8));
        putNative("char", NativeChar.class, NativeTypes.NATIVE_CHAR_SIZE, int.class);
        putNative("short", NativeShort.class, NativeTypes.NATIVE_SHORT_SIZE, int.class);
        putNative("int", NativeInt.class, NativeTypes.NATIVE_INT_SIZE, int.class);
        putNative("long", NativeLong.class, NativeTypes.NATIVE_LONG_SIZE, long.class);
        putNative("wchar_t", WCharT.class, NativeTypes.W_CHAR_T_SIZE, int.class);
        putNative("size_t", SizeT.class, NativeTypes.SIZE_T_SIZE, long.class);
    }

    private final Class<?> javaType;

    private final int returnOpcode;

    private final String cType;

    private final String propertyName;

    private final char propertySignature;

    private final int size;

    private final int javaSize;

    /**
     * @param javaType
     *            the Java primitive type
     * @param returnOpcode
     *            the opcode for returning the value
     * @param cType
     *            the C name (for debugging)
     * @param propertyName
     *            the unsafe getter/setter name
     * @param propertySignature
     *            the signature letter for the unsafe getter/setter type
     * @param size
     *            the size in bytes of the C type
     */
    private PrimitiveType(final Class<?> javaType, final int returnOpcode, final String cType, final String propertyName, final char propertySignature, final int size) {
        this.javaType = javaType;
        this.returnOpcode = returnOpcode;
        this.cType = cType;
        this.propertyName = propertyName;
        this.propertySignature = propertySignature;
        this.size = size;
        this.javaSize = size;
    }

    /**
     * @param primitiveType
     *            the main Java type
     * @param returnOpcode
     *            the opcode for returning the value
     * @param cType
     *            the C type (for debugging)
     * @param javaSize
     *            the size in bytes of the Java type
     */
    private PrimitiveType(final PrimitiveType primitiveType, final int returnOpcode, final String cType, final int javaSize) {
        this.javaType = primitiveType.javaType;
        this.returnOpcode = returnOpcode;
        this.cType = cType;
        this.propertyName = primitiveType.propertyName;
        this.propertySignature = primitiveType.propertySignature;
        this.size = primitiveType.size;
        this.javaSize = javaSize;
        assert javaSize >= primitiveType.size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        str.append("javaType=").append(this.javaType.getName()).append(',');
        try {
            for (Field field : Opcodes.class.getFields()) {
                if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getType() == int.class && field.getInt(null) == this.returnOpcode) {
                    str.append(field.getName());
                    break;
                }
            }
        } catch (Exception exception) {
            str.append("???");
        }
        str.append(",cType=").append(this.cType);
        str.append(",unsafePropName=").append(this.propertyName);
        str.append(",unsafePropSig=").append(this.propertySignature);
        str.append(",nativeSize=").append(this.size);
        str.append(",javaSize=").append(this.javaSize);
        return str.append(']').toString();
    }

    /** @return the java type */
    Class<?> getJavaType() {
        return this.javaType;
    }

    /** @return the return opcode */
    int getReturnOpcode() {
        return this.returnOpcode;
    }

    /** @return the C type */
    String getCType() {
        return this.cType;
    }

    /** @return the size */
    int getSize() {
        return this.size;
    }

    /** @return the javaSize */
    int getJavaSize() {
        return this.javaSize;
    }

    /**
     * <p>
     *  Writes byte-code to get the value and place it on the stack.
     * </p>
     * <p>
     *  Pre-condition: the pointer value (long) must be on the evaluation stack.
     * </p>
     * <p>
     *  Post-condition: the read value is on the stack, being of appropriate
     *  size for {@link #getJavaType()}.
     * </p>
     *
     * @param methodVisitor
     *            the method visitor
     * @param unsigned
     *            whether the value is unsigned
     */
    void writeGet(final MethodVisitor methodVisitor, final boolean unsigned) {
        if (unsigned && this.size == 2 && this.size != this.javaSize) {
            writeGetCharSpecial(methodVisitor);
            return;
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC, MemoryAccess.UNSAFE_ACCESS_TYPE, "get" + this.propertyName, "(J)" + this.propertySignature);
        if (this.size != this.javaSize) {
            switch(this.javaSize) {
                case 4:
                    convertToIntForGetter(methodVisitor, unsigned);
                    break;
                case 8:
                    convertToLongForGetter(methodVisitor, unsigned);
                    break;
                default:
                    throw new AssertionError(this);
            }
        }
    }

    /**
     * <p>
     *  Writes byte-code to set the value.
     * </p>
     * <p>
     *  Pre-condition: the pointer value (long) and value (Java type) must be on
     *  the evaluation stack.
     * </p>
     * <p>
     *  Post-condition: pointer and value popped.
     * </p>
     *
     * @param methodVisitor
     *            the method visitor
     */
    void writeSet(final MethodVisitor methodVisitor) {
        if (this.size != this.javaSize) {
            if (this.javaSize == 8) {
                convertFromLongForSetter(methodVisitor);
            }
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC, MemoryAccess.UNSAFE_ACCESS_TYPE, "put" + this.propertyName, "(J" + this.propertySignature + ")V");
    }

    /**
     * Looks up the primitive type given the class type.
     *
     * @param type
     *            the primitive Java type, or a <code>NativeXxx</code> type
     * @return the primitive type
     */
    static PrimitiveType getType(final Class<?> type) {
        PrimitiveType primitiveType = TYPES.get(type);
        if (primitiveType == null) {
            throw new IllegalArgumentException(type.toString());
        }
        return primitiveType;
    }

    private void writeGetCharSpecial(final MethodVisitor methodVisitor) {
        methodVisitor.visitMethodInsn(INVOKESTATIC, MemoryAccess.UNSAFE_ACCESS_TYPE, "getChar", "(J)C");
        switch(this.javaSize) {
            case 4:
                break;
            case 8:
                methodVisitor.visitInsn(I2L);
                break;
            default:
                throw new AssertionError(this);
        }
    }

    private void convertToIntForGetter(final MethodVisitor methodVisitor, final boolean unsigned) {
        if (unsigned) {
            if (this.size != 1) {
                throw new AssertionError(this);
            }
            DniInternal.writeConstantInt(methodVisitor, 0xff);
            methodVisitor.visitInsn(IAND);
        }
    }

    private void convertToLongForGetter(final MethodVisitor methodVisitor, final boolean unsigned) {
        if (unsigned) {
            switch(this.size) {
                case 1:
                    DniInternal.writeConstantInt(methodVisitor, 0xff);
                    methodVisitor.visitInsn(IAND);
                    methodVisitor.visitInsn(I2L);
                    break;
                case 4:
                    methodVisitor.visitInsn(I2L);
                    methodVisitor.visitLdcInsn(0xffffffffL);
                    methodVisitor.visitInsn(LAND);
                    break;
                default:
                    throw new AssertionError(this);
            }
        } else {
            methodVisitor.visitInsn(I2L);
        }
    }

    private void convertFromLongForSetter(final MethodVisitor methodVisitor) {
        if (this.size < 8) {
            methodVisitor.visitInsn(L2I);
        }
    }

    @SuppressFBWarnings(value = "PMB_POSSIBLE_MEMORY_BLOAT", justification = "Only called from <clinit>")
    private static void put(final PrimitiveType primitiveType) {
        TYPES.put(primitiveType.javaType, primitiveType);
    }

    private static void putNative(final String cType, final Class<?> type, final int size, final Class<?> javaType) {
        Class<?> readingType;
        switch(size) {
            case 1:
                readingType = byte.class;
                break;
            case 2:
                readingType = short.class;
                break;
            case 4:
                readingType = int.class;
                break;
            case 8:
                readingType = long.class;
                break;
            default:
                throw new AssertionError("Unsupported native size: " + cType + " " + size);
        }
        PrimitiveType basis = TYPES.get(readingType);
        PrimitiveType returnType = TYPES.get(javaType);
        TYPES.put(type, new PrimitiveType(basis, returnType.returnOpcode, cType, returnType.javaSize));
    }
}
