package util;

/**
* This class defines colors.
*/
public class Color {

    public static final int white = 0xffffff;

    public static final int lightGray = 0xc0c0c0;

    public static final int gray = 0x808080;

    public static final int darkGray = 0x404040;

    public static final int black = 0;

    public static final int red = 0xff0000;

    public static final int pink = 0xffafaf;

    public static final int orange = 0xffc800;

    public static final int yellow = 0xffff00;

    public static final int green = 0x00ff00;

    public static final int magenta = 0xff00ff;

    public static final int cyan = 0x00ffff;

    public static final int blue = 0x0000ff;

    public static final int rgb(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) throw new IllegalArgumentException("Value out of range");
        return ((r << 16) | (g << 8) | b);
    }
}
