package com.google.gwt.resources.rg;

/**
 * Indicates that an image is not suitable for being added to an image strip.
 */
class UnsuitableForStripException extends Exception {

    private static final long serialVersionUID = -1;

    private final ImageBundleBuilder.ImageRect rect;

    public UnsuitableForStripException(ImageBundleBuilder.ImageRect rect) {
        this.rect = rect;
    }

    public UnsuitableForStripException(ImageBundleBuilder.ImageRect rect, String msg) {
        super(msg);
        this.rect = rect;
    }

    public UnsuitableForStripException(String msg, Throwable cause) {
        super(msg, cause);
        this.rect = null;
    }

    public ImageBundleBuilder.ImageRect getImageRect() {
        return rect;
    }
}
