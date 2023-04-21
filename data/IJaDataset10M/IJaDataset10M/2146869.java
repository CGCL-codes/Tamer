package org.ogre4j;

import org.xbig.base.*;

public enum TextureType implements INativeEnum<TextureType> {

    TEX_TYPE_1D(TextureTypeHelper.ENUM_VALUES[0]), TEX_TYPE_2D(TextureTypeHelper.ENUM_VALUES[1]), TEX_TYPE_3D(TextureTypeHelper.ENUM_VALUES[2]), TEX_TYPE_CUBE_MAP(TextureTypeHelper.ENUM_VALUES[3]);

    private int value;

    TextureType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public TextureType getEnum(int val) {
        return toEnum(val);
    }

    public static final TextureType toEnum(int retval) {
        if (retval == TEX_TYPE_1D.value) return TextureType.TEX_TYPE_1D; else if (retval == TEX_TYPE_2D.value) return TextureType.TEX_TYPE_2D; else if (retval == TEX_TYPE_3D.value) return TextureType.TEX_TYPE_3D; else if (retval == TEX_TYPE_CUBE_MAP.value) return TextureType.TEX_TYPE_CUBE_MAP;
        throw new RuntimeException("wrong number in jni call for an enum");
    }
}

class TextureTypeHelper {

    static {
        System.loadLibrary("ogre4j");
    }

    public static final int[] ENUM_VALUES = getEnumValues();

    private static native int[] getEnumValues();
}

;
