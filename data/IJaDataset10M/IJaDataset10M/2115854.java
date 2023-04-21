package com.volatileengine.image;

import com.volatileengine.datatypes.ArrayByte;

/**
 * @author darkprophet
 * 
 */
public class DXT1AImage extends Mipmap {

    public DXT1AImage(Texture tex, int mipmapLevel, int width, int height, ArrayByte pixels) {
        super(tex, mipmapLevel, width, height, pixels);
    }

    @Override
    public SurfaceCapability getSurfaceCapability() {
        return SurfaceCapability.NONE;
    }

    @Override
    public boolean isCompressed() {
        return true;
    }

    @Override
    public ImageType imageType() {
        return ImageType.DXT1A;
    }
}
