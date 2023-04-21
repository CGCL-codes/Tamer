package org.jowidgets.spi.impl.swt.common.image;

import org.eclipse.swt.graphics.Image;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.spi.impl.image.ImageHandle;
import org.jowidgets.spi.impl.image.ImageRegistry;

public final class SwtImageRegistry extends ImageRegistry {

    private static final SwtImageRegistry INSTANCE = new SwtImageRegistry(new SwtImageHandleFactory());

    private SwtImageRegistry(final SwtImageHandleFactory imageHandleFactory) {
        super(imageHandleFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized ImageHandle<Image> getImageHandle(final IImageConstant key) {
        return (ImageHandle<Image>) super.getImageHandle(key);
    }

    public synchronized Image getImage(final IImageConstant key) {
        if (key == null) {
            return null;
        }
        final ImageHandle<Image> imageHandle = getImageHandle(key);
        if (imageHandle != null) {
            return imageHandle.getImage();
        } else {
            throw new IllegalArgumentException("No image found for the image constant '" + key + "'");
        }
    }

    public SwtImageHandleFactory getSwtImageHandleFactory() {
        return (SwtImageHandleFactory) super.getImageHandleFactory();
    }

    public static SwtImageRegistry getInstance() {
        return INSTANCE;
    }
}
