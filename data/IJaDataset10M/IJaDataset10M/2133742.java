package swarm;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

class RotText {

    public static final int R_0 = 0;

    public static final int R_90 = 90;

    public static final int R_180 = 180;

    public static final int R_270 = 270;

    private String _s;

    private Font _f;

    private int _rot;

    private int _width, _height;

    RotText(String s, int rot, Font f) {
        _s = s;
        _rot = rot;
        _f = f;
    }

    public int getWidth() {
        return _width;
    }

    public Image getImage(Component c, Graphics g, FontMetrics fm) {
        int desc = fm.getMaxDescent();
        int ht = fm.getHeight();
        _width = fm.stringWidth(_s);
        _height = ht;
        _f = fm.getFont();
        if (_width == 0 || _height == 0) return null;
        Image i = c.createImage(_width, _height);
        if (i == null) return null;
        Graphics ig = i.getGraphics();
        ig.setFont(_f);
        int base = ht - fm.getMaxDescent();
        ig.drawString(_s, 0, base);
        if (_rot != R_0) {
            Image ni = _imageRotate(i, c, _width, _height, _rot);
            i.flush();
            i = ni;
        }
        return i;
    }

    public Image getImage(Component c, Graphics g, int width, int height) {
        FontMetrics fm = g.getFontMetrics(_f);
        return getImage(c, g, fm);
    }

    private Image _imageRotate(Image i, Component c, int width, int height, int rot) {
        int src_array[] = new int[width * height];
        PixelGrabber src = new PixelGrabber(i, 0, 0, width, height, src_array, 0, width);
        try {
            src.grabPixels();
        } catch (InterruptedException e) {
            return i;
        }
        int dst_array[] = new int[height * width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (rot == R_90) {
                    dst_array[(height * (width - 1 - x)) + y] = src_array[(y * width) + x];
                } else if (rot == R_270) {
                    dst_array[(x * height) + ((height - 1) - y)] = src_array[(y * width) + x];
                } else {
                    dst_array[(y * width) + x] = src_array[(((height - 1) - y) * width) + x];
                }
            }
        }
        MemoryImageSource ms = new MemoryImageSource(height, width, dst_array, 0, height);
        return c.getToolkit().createImage(ms);
    }
}
