package com.panomedic.colors;

/**
 *
 * @author Yare
 */
public class RGBPixel extends Pixel {

    public RGBPixel() {
        super();
    }

    public RGBPixel(int[] rgba) {
        super();
        if (rgba == null || rgba.length < 3) {
            return;
        }
        for (int i = 0; i < comp.length; i++) {
            comp[i] = rgba[i];
        }
        if (rgba.length < 4 || rgba[3] == 255) {
            opaque = true;
        } else {
            opaque = false;
        }
    }

    public int[] getRGBA() {
        int[] px = new int[] { 0, 0, 0, 0 };
        for (int i = 0; i < comp.length; i++) {
            px[i] = (int) comp[i];
        }
        if (opaque) {
            px[3] = 255;
        } else {
            px[3] = 0;
        }
        return px;
    }

    @Override
    public String getName() {
        return "RGB";
    }

    /**
     * Takes the given array in RGB, converts it to the actual color space and sets the components with that
     * @param rgba
     */
    public void setComp(int[] rgba) {
        for (int i = 0; i < comp.length; i++) {
            comp[i] = rgba[i];
        }
    }

    public static void main(String[] args) {
        int[] test = new int[] { 170, 200, 100, 255 };
        RGBPixel pix = new RGBPixel(test);
        System.out.println("Result: " + pix.getComp()[0] + "  " + pix.getComp()[1] + "  " + pix.getComp()[2] + ", opaque: " + pix.isOpaque());
        test = pix.getRGBA();
        System.out.println("Result: " + test[0] + "  " + test[1] + "  " + test[2] + ", alpha=" + test[3]);
    }
}
