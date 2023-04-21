package org.sf.spring3d.texture.util;

import java.nio.ByteBuffer;

/**
 * TODO_DOCUMENT_ME
 * 
 * @since 0.0.1
 */
public class Texture {

    private ByteBuffer _pixels;

    private int _width;

    private int _height;

    public Texture(ByteBuffer pixels, int width, int height) {
        this._height = height;
        this._pixels = pixels;
        this._width = width;
    }

    public int getHeight() {
        return _height;
    }

    public ByteBuffer getPixels() {
        return _pixels;
    }

    public int getWidth() {
        return _width;
    }
}
