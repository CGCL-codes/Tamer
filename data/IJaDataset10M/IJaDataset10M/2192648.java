package org.ogre4j;

import org.xbig.base.*;

public enum GuiHorizontalAlignment implements INativeEnum<GuiHorizontalAlignment> {

    GHA_LEFT(GuiHorizontalAlignmentHelper.ENUM_VALUES[0]), GHA_CENTER(GuiHorizontalAlignmentHelper.ENUM_VALUES[1]), GHA_RIGHT(GuiHorizontalAlignmentHelper.ENUM_VALUES[2]);

    private int value;

    GuiHorizontalAlignment(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public GuiHorizontalAlignment getEnum(int val) {
        return toEnum(val);
    }

    public static final GuiHorizontalAlignment toEnum(int retval) {
        if (retval == GHA_LEFT.value) return GuiHorizontalAlignment.GHA_LEFT; else if (retval == GHA_CENTER.value) return GuiHorizontalAlignment.GHA_CENTER; else if (retval == GHA_RIGHT.value) return GuiHorizontalAlignment.GHA_RIGHT;
        throw new RuntimeException("wrong number in jni call for an enum");
    }
}

class GuiHorizontalAlignmentHelper {

    static {
        System.loadLibrary("ogre4j");
    }

    public static final int[] ENUM_VALUES = getEnumValues();

    private static native int[] getEnumValues();
}

;
