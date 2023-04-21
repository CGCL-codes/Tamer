package com.yahoo.jute.compiler;

/**
 *
 * @author Milind Bhandarkar
 */
public class JField {

    private JType mType;

    private String mName;

    /**
     * Creates a new instance of JField
     */
    public JField(JType type, String name) {
        mType = type;
        mName = name;
    }

    public String getSignature() {
        return mType.getSignature();
    }

    public String genCppDecl() {
        return mType.genCppDecl(mName);
    }

    public String genCDecl() {
        return mType.genCDecl(mName);
    }

    public String genJavaDecl() {
        return mType.genJavaDecl(mName);
    }

    public String genJavaConstructorParam(String fname) {
        return mType.genJavaConstructorParam(fname);
    }

    public String getName() {
        return mName;
    }

    public String getTag() {
        return mName;
    }

    public JType getType() {
        return mType;
    }

    public String genCppGetSet(int fIdx) {
        return mType.genCppGetSet(mName, fIdx);
    }

    public String genJavaGetSet(int fIdx) {
        return mType.genJavaGetSet(mName, fIdx);
    }

    public String genJavaWriteMethodName() {
        return mType.genJavaWriteMethod(getName(), getTag());
    }

    public String genJavaReadMethodName() {
        return mType.genJavaReadMethod(getName(), getTag());
    }

    public String genJavaCompareTo() {
        return mType.genJavaCompareTo(getName());
    }

    public String genJavaEquals() {
        return mType.genJavaEquals(getName(), "peer." + getName());
    }

    public String genJavaHashCode() {
        return mType.genJavaHashCode(getName());
    }

    public String genJavaConstructorSet(String fname) {
        return mType.genJavaConstructorSet(mName, fname);
    }
}
