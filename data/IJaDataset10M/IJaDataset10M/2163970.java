package org.eclipse.swt.internal.image;

final class JPEGStartOfImage extends JPEGFixedSizeSegment {

    public JPEGStartOfImage() {
        super();
    }

    public JPEGStartOfImage(byte[] reference) {
        super(reference);
    }

    public JPEGStartOfImage(LEDataInputStream byteStream) {
        super(byteStream);
    }

    public int signature() {
        return JPEGFileFormat.SOI;
    }

    public int fixedSize() {
        return 2;
    }
}
