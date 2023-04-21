package Quick3dApplet;

import java.applet.Applet;
import java.awt.*;
import java.io.*;

public final class PixstoreBump implements Texture {

    public int[] pix;

    private int width, height;

    public boolean adjusted = false;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthm() {
        return width - 1;
    }

    public int getHeightm() {
        return height - 1;
    }

    private int depthAt(Pixstore bumpMap, int x, int y) {
        x += bumpMap.getWidth();
        y += bumpMap.getHeight();
        x = x % bumpMap.getWidth();
        y = y % bumpMap.getHeight();
        int c = x + y * bumpMap.getWidth();
        return (bumpMap.pix[c] & 0xff) + ((bumpMap.pix[c] >> 8) & 0xff) + ((bumpMap.pix[c] >> 16) & 0xff);
    }

    public PixstoreBump(Pixstore image, Pixstore bumpMap, int factor) {
        width = image.getWidth();
        height = image.getHeight();
        pix = new int[width * height];
        int xBump = 7;
        int yBump = 7;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int c = x + y * width;
                xBump = (((depthAt(bumpMap, x - 1, y) - depthAt(bumpMap, x + 1, y)) * factor) >> 12) + 15;
                yBump = (((depthAt(bumpMap, x, y - 1) - depthAt(bumpMap, x, y + 1)) * factor) >> 12) + 15;
                final int max = 31;
                if (xBump < 0) xBump = 0; else if (xBump > max) xBump = max;
                if (yBump < 0) yBump = 0; else if (yBump > max) yBump = max;
                pix[c] = ((image.pix[c] & 0xfe) >> 1) + ((image.pix[c] & 0xfe00) >> 2) + ((image.pix[c] & 0xfe0000) >> 3) + (xBump << 21) + (yBump << 26);
            }
        }
    }

    public static int mulCol(int col, int frac) {
        return ((((col & 0x3f80) * frac) >> 6) & 0xff00) | (((col & 0x7f) * frac) >> 7) | ((((col & 0x1fc000) * frac) >> 5) & 0xff0000);
    }

    public void getPix(int x, int y, Pixel p) {
        int c = pix[x + y * width];
        p.b = (c & 0x7f) << 1;
        p.g = (c >> 6) & 0xfe;
        p.r = (c >> 13) & 0xfe;
        p.bumpX = (c >> 21) & 31;
        p.bumpY = (c >> 26) & 31;
        p.transp = 0;
    }

    public void setPix(int x, int y, Pixel p) {
        pix[x + y * width] = (p.b >> 1) + ((p.g & 0xfe) << 6) + ((p.r & 0xfe) << 13) + (p.bumpX << 21) + (p.bumpY << 26);
    }
}
