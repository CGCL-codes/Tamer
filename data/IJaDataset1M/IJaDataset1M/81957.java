package net.engine.picture;

import java.awt.*;
import java.util.Random;

public class Picture {

    public byte data[][];

    public int width;

    public int height;

    public Color[] palette;

    public Picture(int width, int height) {
        this.height = height;
        this.width = width;
        data = new byte[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = -128;
            }
        }
        palette = new Color[256];
    }

    public void setPixel(int x, int y, int colour) {
        if ((x >= 0) && (x < width)) {
            if ((y >= 0) && (y < height)) {
                data[y][x] = toByte(colour);
            }
        }
    }

    public int getPixel(int x, int y) {
        if ((x >= 0) && (x < width)) {
            if ((y >= 0) && (y < height)) {
                return fromByte(data[y][x]);
            }
        }
        return -1;
    }

    private byte toByte(int colour) {
        return (byte) (colour - 128);
    }

    private int fromByte(byte b) {
        return 128 + b;
    }

    public void line(int x0, int y0, int x1, int y1, int colourIndex) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        setPixel(x0, y0, colourIndex);
        if (Math.abs(dx) > Math.abs(dy)) {
            float m = (float) dy / (float) dx;
            float b = y0 - m * x0;
            dx = (dx < 0) ? -1 : 1;
            while (x0 != x1) {
                x0 += dx;
                setPixel(x0, Math.round(m * x0 + b), colourIndex);
            }
        } else if (dy != 0) {
            float m = (float) dx / (float) dy;
            float b = x0 - m * y0;
            dy = (dy < 0) ? -1 : 1;
            while (y0 != y1) {
                y0 += dy;
                setPixel(Math.round(m * y0 + b), y0, colourIndex);
            }
        }
    }

    public Color getColour(int index) {
        return palette[index];
    }

    public void setColor(int index, Color color) {
        palette[index] = color;
    }

    public void setColors(Object... o) {
        ColourGradient.generate(palette, o);
    }

    public void rect(int x0, int y0, int x1, int y1, int colourIndex) {
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                setPixel(x, y, colourIndex);
            }
        }
    }

    private int getSmooth(int x, int y, int indexToIgnore) {
        int result = 0;
        int count = 0;
        int index = getPixel(x, y);
        if ((index != -1) && (index != indexToIgnore)) {
            result += index * 4;
            count += 4;
        }
        index = getPixel(x - 1, y);
        if ((index != -1) && (index != indexToIgnore)) {
            result += index;
            count++;
        }
        index = getPixel(x + 1, y);
        if ((index != -1) && (index != indexToIgnore)) {
            result += index;
            count++;
        }
        index = getPixel(x, y - 1);
        if ((index != -1) && (index != indexToIgnore)) {
            result += index;
            count++;
        }
        index = getPixel(x, y + 1);
        if ((index != -1) && (index != indexToIgnore)) {
            result += index;
            count++;
        }
        if (count == 0) {
            return indexToIgnore;
        }
        return result / count;
    }

    public void plasma(int indexToIgnore) {
        byte data2[][] = new byte[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data2[y][x] = toByte(getSmooth(x, y, indexToIgnore));
            }
        }
        System.arraycopy(data2, 0, data, 0, height);
    }

    public void speckle(int amount) {
        Random random = new Random(System.nanoTime());
        byte data2[][] = new byte[height][width];
        System.arraycopy(data, 0, data2, 0, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = getPixel(x + random.nextInt(amount * 2 + 1) - amount, y + random.nextInt(amount * 2 + 1) - amount);
                if (pixel != -1) {
                    data2[y][x] = toByte(pixel);
                }
            }
        }
        System.arraycopy(data2, 0, data, 0, height);
    }

    public void circle(int xCenter, int yCenter, int radius, int colourIndex) {
        for (int x = -radius; x <= radius; x++) {
            int y = (int) Math.sqrt(radius * radius - x * x);
            line(x + xCenter, yCenter - y, x + xCenter, yCenter + y, colourIndex);
        }
    }
}
