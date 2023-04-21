package twod;

import java.awt.Color;

public class Screen {

    int[] _pixels;

    int _width;

    int _height;

    PixelProcessor _processor;

    public Screen(int width, int height) {
        _pixels = new int[width * height];
        _width = width;
        _height = height;
        setColor(-1);
    }

    public int getX() {
        return _width;
    }

    public int getY() {
        return _height;
    }

    public int[] getPixels() {
        return _pixels;
    }

    public void drawPixel(int x, int y, int color) {
        _pixels[y * _width + x] = color;
    }

    public void clear() {
        int max = getX() * getY();
        for (int i = 0; i < max; i++) {
            _pixels[i] = 0;
        }
    }

    public void setPixelProcessor(PixelProcessor p) {
        _processor = p;
    }

    public PixelProcessor getPixelProcessor() {
        return _processor;
    }

    public void setColor(int color) {
        _processor = new Default(color, _width);
    }

    public void setColor(Color color) {
        int alpha = 255;
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int value = (alpha << 24) | (red << 16) | (green << 8) | blue;
        setColor(value);
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        int deltax = x1 - x0, deltay = y1 - y0, majorIncrA = 0, majorIncrB = 0, minorIncrA = 0, minorIncrB = 0, twop_o = 0, twopleft = 0, twopright = 0;
        int current_x = x0, current_y = y0;
        if (Math.abs(deltay) <= Math.abs(deltax)) {
            if (x0 < x1) {
                majorIncrA = 1;
                if (y0 < y1) {
                    twop_o = 2 * deltay - deltax;
                    twopleft = 2 * deltay;
                    twopright = 2 * (deltay - deltax);
                    minorIncrA = 0;
                    minorIncrB = 1;
                } else {
                    twop_o = 2 * deltay + deltax;
                    twopright = 2 * deltay;
                    twopleft = 2 * (deltay + deltax);
                    minorIncrA = -1;
                    minorIncrB = 0;
                }
            } else {
                majorIncrA = -1;
                if (y0 < y1) {
                    twop_o = -2 * deltay - deltax;
                    twopright = -2 * deltay;
                    twopleft = 2 * (-deltay - deltax);
                    minorIncrA = 1;
                    minorIncrB = 0;
                } else {
                    twop_o = -2 * deltay + deltax;
                    twopleft = -2 * deltay;
                    twopright = 2 * (-deltay + deltax);
                    minorIncrA = 0;
                    minorIncrB = -1;
                }
            }
            while (current_x != x1) {
                current_x += majorIncrA;
                if (twop_o < 0) {
                    twop_o += twopleft;
                    current_y += minorIncrA;
                } else {
                    twop_o += twopright;
                    current_y += minorIncrB;
                }
                _processor.drawPixel(current_x, current_y);
            }
        } else {
            if (y0 < y1) {
                majorIncrB = 1;
                if (x0 < x1) {
                    twop_o = deltay - 2 * deltax;
                    twopright = -2 * deltax;
                    twopleft = 2 * (deltay - deltax);
                    minorIncrA = 0;
                    minorIncrB = 1;
                } else {
                    twop_o = -deltay - 2 * deltax;
                    twopleft = -2 * deltax;
                    twopright = 2 * (-deltay - deltax);
                    minorIncrA = -1;
                    minorIncrB = 0;
                }
            } else {
                majorIncrB = -1;
                if (x0 < x1) {
                    twop_o = deltay + 2 * deltax;
                    twopleft = 2 * deltax;
                    twopright = 2 * (deltay + deltax);
                    minorIncrA = 1;
                    minorIncrB = 0;
                } else {
                    twop_o = -deltay + 2 * deltax;
                    twopright = 2 * deltax;
                    twopleft = 2 * (-deltay + deltax);
                    minorIncrA = 0;
                    minorIncrB = -1;
                }
            }
            while (current_y != y1) {
                current_y += majorIncrB;
                if (twop_o < 0) {
                    twop_o += twopleft;
                    current_x += minorIncrB;
                } else {
                    twop_o += twopright;
                    current_x += minorIncrA;
                }
                _processor.drawPixel(current_x, current_y);
            }
        }
    }

    private class Default implements PixelProcessor {

        int _width;

        int _color;

        public Default(int color, int width) {
            _color = color;
            _width = width;
        }

        public void drawPixel(int x, int y) {
            int index = y * _width + x;
            if (index > 0 && index < _pixels.length) {
                _pixels[y * _width + x] = _color;
            }
        }
    }
}
