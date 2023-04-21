package com.jogamp.gluegen;

/** Utility class for handling Opaque directives for JavaEmitter. */
public class TypeInfo {

    private String name;

    private int pointerDepth;

    private JavaType javaType;

    private TypeInfo next;

    public TypeInfo(String name, int pointerDepth, JavaType javaType) {
        this.name = name;
        this.pointerDepth = pointerDepth;
        this.javaType = javaType;
    }

    public String name() {
        return name;
    }

    public int pointerDepth() {
        return pointerDepth;
    }

    public JavaType javaType() {
        return javaType;
    }

    public void setNext(TypeInfo info) {
        this.next = info;
    }

    public TypeInfo next() {
        return next;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer("TypeInfo: ");
        buf.append(name);
        buf.append(" pointerDepth ");
        buf.append(pointerDepth);
        buf.append(" JavaType " + javaType);
        return buf.toString();
    }
}
