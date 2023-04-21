package com.bix.util.blizfiles.dds;

/**
 * This is the representation of a color with an alpha channel.
 * 
 * @author squid
 */
class DDSColorAlpha extends DDSColor {

    private int alpha;

    public DDSColorAlpha(DDSColor color, int alpha) {
        super(color);
        this.alpha = alpha;
    }

    public DDSColorAlpha(int color, int alpha) {
        super(color);
        this.setAlpha(alpha);
    }

    public void setAlpha(int alpha) {
        this.alpha = (byte) (alpha & 0xFF);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public int getAsInteger() {
        int result = 0;
        result |= (((long) this.getRed() & 0xFF) << 24);
        result |= (((long) this.getGreen() & 0xFF) << 16);
        result |= (((long) this.getBlue() & 0xFF) << 8);
        result |= ((long) this.getAlpha() & 0xFF);
        return result;
    }
}
