package com.googlecode.java2objc.objc;

import com.google.code.java2objc.code.ObjcType;

/**
 * Parameter for an Objective C method
 * 
 * @author Inderjeet Singh
 */
public final class ObjcMethodParam extends ObjcNode {

    private final ObjcType type;

    private final String name;

    private final int arrayCount;

    public ObjcMethodParam(ObjcType type, String name, int arrayCount) {
        this.type = type;
        this.name = name;
        this.arrayCount = arrayCount;
    }

    public ObjcType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getArrayCount() {
        return arrayCount;
    }
}
