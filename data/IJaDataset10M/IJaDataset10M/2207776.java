package net.sf.jode.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.Comparable;

/**
 * Represents a java bytecode field (class variable).  A field
 * consists of the following parts:
 *
 * <dl>
 *
 * <dt>name</dt><dd>The field's name</dd>
 *
 * <dt>type</dt><dd>The field's {@link TypeSignature type signature}
 * in bytecode format.</dd>
 *
 * <dt>signature</dt><dd>The field's {@link TypeSignature type signature}
 * in bytecode format including template information.</dd>
 *
 * <dt>modifiers</dt><dd>The modifiers of the field like private, public etc.
 * These are created by or-ing the constants {@link Modifier#PUBLIC},
 * {@link Modifier#PRIVATE}, {@link Modifier#PROTECTED}, 
 * {@link Modifier#STATIC}, {@link Modifier#FINAL}, 
 * {@link Modifier#VOLATILE}, {@link Modifier#TRANSIENT}, 
 * {@link Modifier#STRICT}
 * of class {@link java.lang.reflect.Modifier}. </dt>
 *
 * <dt>synthetic</dt><dd>true if this field is synthetic.</dd>
 *
 * <dt>deprecated</dt><dd>true if this field is deprecated.</dd>
 *
 * <dt>constant</dt> <dd>Final static fields may have a constant
 * value.  This is either of type String, Integer, Long, Float or
 * Double.  </dt>
 *
 * </dl>
 *
 * @author Jochen Hoenicke
 * @see net.sf.jode.bytecode.TypeSignature
 * @see net.sf.jode.bytecode.BasicBlocks
 */
public final class FieldInfo extends BinaryInfo implements Comparable {

    int modifier;

    String name;

    String typeSig;

    Object constant;

    boolean deprecatedFlag;

    /**
     * The type signature that also contains template information.
     */
    private String signature;

    /**
     * Creates a new empty field info.
     */
    public FieldInfo() {
    }

    /**
     * Creates a new field with given name, type and modifiers.
     * @param name the name of the field.
     * @param typeSig the typeSig the type signature.
     * @param modifier the modifier
     * @see TypeSignature
     * @see Modifier
     */
    public FieldInfo(String name, String typeSig, int modifier) {
        this.name = name;
        this.typeSig = typeSig;
        this.modifier = modifier;
    }

    protected void readAttribute(String name, int length, ConstantPool cp, DataInputStream input, int howMuch) throws IOException {
        if (howMuch >= ClassInfo.DECLARATIONS && name.equals("ConstantValue")) {
            if (length != 2) throw new ClassFormatException("ConstantValue attribute has wrong length");
            int index = input.readUnsignedShort();
            constant = cp.getConstant(index);
        } else if (name.equals("Synthetic")) {
            modifier |= ACC_SYNTHETIC;
            if (length != 0) throw new ClassFormatException("Synthetic attribute has wrong length");
        } else if (name.equals("Deprecated")) {
            deprecatedFlag = true;
            if (length != 0) throw new ClassFormatException("Deprecated attribute has wrong length");
        } else if (name.equals("Signature")) {
            signature = cp.getUTF8(input.readUnsignedShort());
        } else super.readAttribute(name, length, cp, input, howMuch);
    }

    void read(ConstantPool constantPool, DataInputStream input, int howMuch) throws IOException {
        modifier = input.readUnsignedShort();
        name = constantPool.getUTF8(input.readUnsignedShort());
        typeSig = constantPool.getUTF8(input.readUnsignedShort());
        readAttributes(constantPool, input, howMuch);
    }

    void reserveSmallConstants(GrowableConstantPool gcp) {
    }

    void prepareWriting(GrowableConstantPool gcp) {
        gcp.putUTF8(name);
        gcp.putUTF8(typeSig);
        if (constant != null) {
            gcp.putUTF8("ConstantValue");
            if (typeSig.charAt(0) == 'J' || typeSig.charAt(0) == 'D') gcp.putLongConstant(constant); else gcp.putConstant(constant);
        }
        if (isSynthetic()) gcp.putUTF8("Synthetic");
        if (deprecatedFlag) gcp.putUTF8("Deprecated");
        prepareAttributes(gcp);
    }

    protected int getAttributeCount() {
        int count = super.getAttributeCount();
        if (constant != null) count++;
        if (isSynthetic()) count++;
        if (deprecatedFlag) count++;
        return count;
    }

    protected void writeAttributes(GrowableConstantPool gcp, DataOutputStream output) throws IOException {
        super.writeAttributes(gcp, output);
        if (constant != null) {
            output.writeShort(gcp.putUTF8("ConstantValue"));
            output.writeInt(2);
            int index;
            if (typeSig.charAt(0) == 'J' || typeSig.charAt(0) == 'D') index = gcp.putLongConstant(constant); else index = gcp.putConstant(constant);
            output.writeShort(index);
        }
        if (isSynthetic()) {
            output.writeShort(gcp.putUTF8("Synthetic"));
            output.writeInt(0);
        }
        if (deprecatedFlag) {
            output.writeShort(gcp.putUTF8("Deprecated"));
            output.writeInt(0);
        }
    }

    void write(GrowableConstantPool constantPool, DataOutputStream output) throws IOException {
        output.writeShort(modifier);
        output.writeShort(constantPool.putUTF8(name));
        output.writeShort(constantPool.putUTF8(typeSig));
        writeAttributes(constantPool, output);
    }

    protected void drop(int keep) {
        if (keep < ClassInfo.DECLARATIONS) constant = null;
        super.drop(keep);
    }

    /**
     * Gets the name of the field.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type signature of the field.
     * @return the type signature.
     * @see TypeSignature
     */
    public String getType() {
        return typeSig;
    }

    /**
     * Gets the type signature including template information of the field.
     * <b>WARNING:</b> This field may disappear and merged into getType later.
     * @return the type signature.
     * @see TypeSignature
     */
    public String getSignature() {
        return signature != null ? signature : typeSig;
    }

    /**
     * Gets the modifier of the field.
     * @return the modifiers.
     * @see Modifier
     */
    public int getModifiers() {
        return modifier;
    }

    /**
     * Tells whether this field is synthetic.
     * @return true if the field is synthetic.
     */
    public boolean isSynthetic() {
        return (modifier & ACC_SYNTHETIC) != 0;
    }

    /**
     * Tells whether this field is deprecated.
     * @return true if the field is deprecated.
     */
    public boolean isDeprecated() {
        return deprecatedFlag;
    }

    /**
     * Gets the constant value of the field.  For static final fields
     * that have a simple String, int, float, double or long constant, 
     * this returns the corresponding constant as String, Integer, Float
     * Double or long.  For other fields it returns null.
     * @return The constant, or null.
     */
    public Object getConstant() {
        return constant;
    }

    /**
     * Sets the name of the field.
     * @param newName the name.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Sets the type signature of the field.
     * @param newType the type signature.
     * @see TypeSignature
     */
    public void setType(String newType) {
        typeSig = newType;
    }

    /**
     * Sets the modifier of the field.
     * @param newModifier the modifiers.
     * @see Modifier
     */
    public void setModifiers(int newModifier) {
        modifier = newModifier;
    }

    public void setSynthetic(boolean flag) {
        if (flag) modifier |= ACC_SYNTHETIC; else modifier &= ~ACC_SYNTHETIC;
    }

    public void setDeprecated(boolean flag) {
        deprecatedFlag = flag;
    }

    public void setConstant(Object newConstant) {
        constant = newConstant;
    }

    /** 
     * Compares two FieldInfo objects for field order.  The field
     * order is as follows: First the static class intializer followed
     * by constructor with type signature sorted lexicographic.  Then
     * all other fields sorted lexicographically by name.  If two
     * fields have the same name, they are sorted by type signature,
     * though that can only happen for obfuscated code.
     *
     * @return a positive number if this field follows the other in
     * field order, a negative number if it preceeds the
     * other, and 0 if they are equal.  
     * @exception ClassCastException if other is not a ClassInfo.  */
    public int compareTo(Object other) {
        FieldInfo fi = (FieldInfo) other;
        int result = name.compareTo(fi.name);
        if (result == 0) result = typeSig.compareTo(fi.typeSig);
        return result;
    }

    public String toString() {
        return "Field " + Modifier.toString(modifier) + " " + typeSig + " " + name;
    }
}
