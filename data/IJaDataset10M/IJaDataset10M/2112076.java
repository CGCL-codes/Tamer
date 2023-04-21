package com.trendsoft.eye.cls;

/**
 * @author vgagin
 *
 * TODO Add JavaDoc
 */
public class SConstantString extends SConstant {

    public static final int TAG = 8;

    public final int stringIndex;

    public SConstantString(int stringIndex) {
        super(TAG);
        this.stringIndex = stringIndex;
    }
}
