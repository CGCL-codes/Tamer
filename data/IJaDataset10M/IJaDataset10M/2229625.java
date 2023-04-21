package com.xith3d.scenegraph;

/**
 * Texture2D defines attributes that apply to .
 */
public class Texture2D extends Texture {

    /**
     * Used by direct sub-classes to define where their capabilty
     * bit positions start. The first bit in a sub-class should be defined
     * as Texture2D.LAST_CAPS_BIT_POSITION+1.
     */
    public static final int LAST_CAPS_BIT_POSITION = Texture.LAST_CAPS_BIT_POSITION;

    /**
     * Constructs a new Texture2D object.
     */
    public Texture2D() {
        super();
    }

    /**
     * Constructs a new Texture2D object.
     */
    public Texture2D(int mipmapMode, int format, int width, int height) {
        super(mipmapMode, format, width, height);
    }
}
