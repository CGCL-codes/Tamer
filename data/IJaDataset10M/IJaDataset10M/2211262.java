package com.scheublegroup.icon;

/**
 * @author c.scheuble
 */
public class IconEntryHeader {

    private int bitsPerPixel;

    private long colorsImportant;

    private long colorsUsed;

    private IconEntryCompressions compression;

    private long headerSize;

    private long height;

    private long imageSize;

    private int planes;

    private long width;

    private long xPixelsPerM;

    private long yPixelsPerM;

    public IconEntryHeader() {
        super();
    }

    /**
     * Create a copy of another header.
     * 
     * @param visitor Another header.
     */
    public IconEntryHeader(final IconEntryHeader visitor) {
        this.bitsPerPixel = visitor.bitsPerPixel;
        this.colorsImportant = visitor.colorsImportant;
        this.colorsUsed = visitor.colorsUsed;
        this.compression = visitor.compression;
        this.headerSize = visitor.headerSize;
        this.height = visitor.height;
        this.imageSize = visitor.imageSize;
        this.planes = visitor.planes;
        this.width = visitor.width;
        this.xPixelsPerM = visitor.xPixelsPerM;
        this.yPixelsPerM = visitor.yPixelsPerM;
    }

    /**
     * Perform some sanity checks.
     */
    public void doSomeChecks() {
        if (getWidth() * 2 != getHeight()) {
            System.out.println(this + ": In header, height is not twice the width");
        }
    }

    public final int getBitsPerPixel() {
        return bitsPerPixel;
    }

    public final long getColorsImportant() {
        return colorsImportant;
    }

    public final long getColorsUsed() {
        return colorsUsed;
    }

    public final IconEntryCompressions getCompression() {
        return compression;
    }

    public final long getHeaderSize() {
        return headerSize;
    }

    public final long getHeight() {
        return height;
    }

    public final long getImageSize() {
        return imageSize;
    }

    public final int getPlanes() {
        return planes;
    }

    public final long getWidth() {
        return width;
    }

    public final long getxPixelsPerM() {
        return xPixelsPerM;
    }

    public final long getyPixelsPerM() {
        return yPixelsPerM;
    }

    public final void setBitsPerPixel(int bitsPerPixel) {
        this.bitsPerPixel = bitsPerPixel;
    }

    public final void setColorsImportant(long colorsImportant) {
        this.colorsImportant = colorsImportant;
    }

    public final void setColorsUsed(long colorsUsed) {
        this.colorsUsed = colorsUsed;
    }

    public final void setCompression(IconEntryCompressions compression) {
        this.compression = compression;
    }

    public final void setHeaderSize(long headerSize) {
        this.headerSize = headerSize;
    }

    public final void setHeight(long height) {
        this.height = height;
    }

    public final void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public final void setPlanes(int planes) {
        this.planes = planes;
    }

    public final void setWidth(long width) {
        this.width = width;
    }

    public final void setxPixelsPerM(long xPixelsPerM) {
        this.xPixelsPerM = xPixelsPerM;
    }

    public final void setyPixelsPerM(long yPixelsPerM) {
        this.yPixelsPerM = yPixelsPerM;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        builder.append(": bpp= ");
        builder.append(Integer.toString(bitsPerPixel));
        builder.append(", colorsImportant= ");
        builder.append(Long.toString(colorsImportant));
        builder.append(", colorsUsed= ");
        builder.append(Long.toString(colorsUsed));
        builder.append(", compression= ");
        builder.append(compression);
        builder.append(", headerSize= ");
        builder.append(Long.toString(headerSize));
        builder.append(", height= ");
        builder.append(Long.toString(height));
        builder.append(", imageSize= ");
        builder.append(Long.toString(imageSize));
        builder.append(", planes= ");
        builder.append(Integer.toString(planes));
        builder.append(", width= ");
        builder.append(Long.toString(width));
        builder.append(", xPixelsPerM= ");
        builder.append(Long.toString(xPixelsPerM));
        builder.append(", yPixelsPerM= ");
        builder.append(Long.toString(yPixelsPerM));
        builder.append(".");
        return builder.toString();
    }
}
