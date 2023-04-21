package jphotoshop.filter;

public class IceFilter extends NullOpFilter {

    public int doColor(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        red = Math.abs(red - green - blue) * 3 / 2;
        blue = Math.abs(blue - red - green) * 3 / 2;
        green = Math.abs(green - red - blue) * 3 / 2;
        if (red < 0) {
            red = 0;
        }
        if (red > 0xff) {
            red = 0xff;
        }
        if (green < 0) {
            green = 0;
        }
        if (green > 0xff) {
            green = 0xff;
        }
        if (blue < 0) {
            blue = 0;
        }
        if (blue > 0xff) {
            blue = 0xff;
        }
        return (pixel & 0xff000000) | (red << 16) | (green << 8) | (blue);
    }
}
