package org.ogre4j;

import org.xbig.base.*;

public enum ImageFlags implements INativeEnum<ImageFlags> {

    IF_COMPRESSED(ImageFlagsHelper.ENUM_VALUES[0]), IF_CUBEMAP(ImageFlagsHelper.ENUM_VALUES[1]), IF_3D_TEXTURE(ImageFlagsHelper.ENUM_VALUES[2]);

    private int value;

    ImageFlags(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public ImageFlags getEnum(int val) {
        return toEnum(val);
    }

    public static final ImageFlags toEnum(int retval) {
        if (retval == IF_COMPRESSED.value) return ImageFlags.IF_COMPRESSED; else if (retval == IF_CUBEMAP.value) return ImageFlags.IF_CUBEMAP; else if (retval == IF_3D_TEXTURE.value) return ImageFlags.IF_3D_TEXTURE;
        throw new RuntimeException("wrong number in jni call for an enum");
    }
}

class ImageFlagsHelper {

    static {
        System.loadLibrary("ogre4j");
    }

    public static final int[] ENUM_VALUES = getEnumValues();

    private static native int[] getEnumValues();
}

;
