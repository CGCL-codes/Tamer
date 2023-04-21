package etri;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Plot2D class
 * @author Doosik Kim
 *
 */
public class Plot2D {

    private int _width;

    private int _height;

    private BufferedImage _image;

    private Graphics2D _graphics;

    private double[][] _value;

    private int[][] _ypos;

    private String[] _label;

    private double _x1 = 0;

    private double _x2 = 0;

    private double _y1 = 0;

    private double _y2 = 0;

    /**
	 * constructor
	 * @param width width of graph in pixel
	 * @param height height of graph in pixel
	 */
    public Plot2D(int width, int height) {
        this(width, height, 1);
    }

    /**
	 * constructor
	 * @param width width of graph in pixel
	 * @param height height of graph in pixel
	 * @param count the number of functions
	 */
    public Plot2D(int width, int height, int count) {
        _width = width;
        _height = height;
        _image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        _graphics = _image.createGraphics();
        _graphics.setBackground(Color.WHITE);
        _graphics.clearRect(0, 0, width, height);
        _value = new double[count][width];
        _ypos = new int[count][width];
        _label = new String[count];
    }

    public interface function {

        /**
		 * function f to be used at draw function
		 * @param x input value
		 * @return output value
		 */
        public double f(double x);
    }

    /**
	 * set plot range
	 * @param x1 x1
	 * @param x2 x2
	 * @param y1 y1
	 * @param y2 y2
	 * @throws Exception if range is bad
	 */
    public void setRange(double x1, double x2, double y1, double y2) throws Exception {
        if (x1 >= x2 || y1 >= y2) throw new Exception("invalid range");
        _x1 = x1;
        _x2 = x2;
        _y1 = y1;
        _y2 = y2;
    }

    /**
	 * set plot function
	 * @param index index of function
	 * @param f function
	 * @throws Exception if the size of function array is mismatched
	 */
    public void setFunction(int index, function f) throws Exception {
        setFunction(index, f, "function " + ((Integer) (index + 1)).toString());
    }

    /**
	 * set plot function
	 * @param f function
	 * @param label label of function
	 * @throws Exception if function count is zero 
	 */
    public void setFunction(function f, String label) throws Exception {
        setFunction(0, f, label);
    }

    /**
	 * set plot function
	 * @param f function
	 * @throws Exception if the size of function array is mismatched
	 */
    public void setFunction(function[] f) throws Exception {
        int count = f.length;
        if (count != _label.length) throw new Exception("invalid size");
        for (int i = 0; i < count; i++) setFunction(i, f[i], "");
    }

    /**
	 * set plot function
	 * @param f function
	 * @param label label of function
	 * @throws Exception if the size of function array is mismatched 
	 */
    public void setFunction(function[] f, String[] label) throws Exception {
        int count = f.length;
        if (count != _label.length) throw new Exception("invalid size");
        for (int i = 0; i < count; i++) {
            if (i < label.length) setFunction(i, f[i], label[i]); else setFunction(i, f[i], "");
        }
    }

    /**
	 * set plot function
	 * @param index index of function
	 * @param f function
	 * @param label label of function
	 * @throws Exception if either index or range is bad 
	 */
    public void setFunction(int index, function f, String label) throws Exception {
        if (index < 0 || index >= _label.length) throw new Exception("invalid index");
        if (_x1 >= _x2 || _y1 >= _y2) throw new Exception("invalid range");
        if (label.length() == 0) {
            label = "f " + ((Integer) (index + 1)).toString();
        }
        _label[index] = label;
        double diffx = _x2 - _x1;
        double diffy = _y2 - _y1;
        double oldX = _x1 - 1;
        for (int i = 0; i < _width; i++) {
            double x = i * diffx / _width + _x1;
            if (x == oldX) _value[index][i] = _value[index][i - 1]; else _value[index][i] = f.f(x);
            oldX = x;
        }
        int y0 = getOriginY();
        for (int i = 0; i < _width; i++) {
            _ypos[index][i] = y0 - (int) (_value[index][i] * _height / diffy);
        }
    }

    /**
	 * get y-origin coordinate
	 * @return y-origin coordinate
	 */
    private int getOriginY() {
        double origY = _y1 == 0 ? 0 : (_y1 * _height) / (_y1 - _y2);
        return _height - (int) origY;
    }

    /**
	 * get value of key from option.
	 * if option is "type=line,label=yes" and key is "type", the return value is "line"
	 * @param option
	 * @param key
	 * @return value string found
	 */
    private String getOptionValue(String option, String key) {
        String token = key + "=";
        int tokenLength = token.length();
        if (tokenLength <= 1) return "";
        int first = option.indexOf(token);
        if (first < 0) return "";
        String value = option.substring(first + tokenLength);
        first = value.indexOf(',');
        if (first <= 0) return value;
        return value.substring(0, first);
    }

    private void drawGraph(String option) {
        Color[] color = { Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.CYAN };
        String type = getOptionValue(option, "type");
        String label = getOptionValue(option, "label");
        String stepOption = getOptionValue(option, "step");
        int step;
        try {
            step = Integer.parseInt(stepOption);
        } catch (NumberFormatException e) {
            step = 1;
        }
        if (step <= 0) step = 1;
        if (type.equals("line")) {
            for (int i = 0; i < _label.length; i++) {
                _graphics.setColor(color[i % color.length]);
                for (int x = 1; x < _width; x++) {
                    int y1 = _ypos[i][x - 1];
                    int y2 = _ypos[i][x];
                    if (Double.isInfinite(_value[i][x - 1]) || Double.isInfinite(_value[i][x])) continue;
                    _graphics.drawLine(x - 1, y1, x, y2);
                }
            }
        } else if (type.equals("dot")) {
            for (int i = 0; i < _label.length; i++) {
                _graphics.setColor(color[i % color.length]);
                for (int x = 0; x < _width; x += step) {
                    int y = _ypos[i][x];
                    if (Double.isInfinite(_value[i][x])) continue;
                    _graphics.drawRect(x, y, 1, 1);
                }
            }
        } else if (type.equals("vertical")) {
            int y0 = getOriginY();
            for (int i = 0; i < _label.length; i++) {
                _graphics.setColor(color[i % color.length]);
                for (int x = 0; x < _width; x += step) {
                    int y = _ypos[i][x];
                    if (Double.isInfinite(_value[i][x])) continue;
                    _graphics.drawLine(x, y, x, y0);
                }
            }
        }
        if (!label.equals("no")) {
            for (int i = 0; i < _label.length; i++) {
                _graphics.setColor(color[i % color.length]);
                int y = _ypos[i][_width - 1];
                if (y <= 10) y = 11; else if (y > _height) y = _height;
                _graphics.drawString(_label[i], _width - 50, y);
            }
        }
    }

    private void drawAxis() {
        if (_x1 >= _x2 || _y1 >= _y2) return;
        double origX = _x1 == 0 ? 0 : (_x1 * _width) / (_x1 - _x2);
        double origY = _y1 == 0 ? 0 : (_y1 * _height) / (_y1 - _y2);
        int x0 = (int) origX;
        int y0 = _height - (int) origY;
        if (x0 < 0) x0 = 0; else if (x0 >= _width) x0 = _width - 1;
        if (y0 < 0) y0 = 0; else if (y0 >= _height) y0 = _height - 1;
        _graphics.setColor(Color.BLACK);
        _graphics.drawLine(0, y0, _width, y0);
        _graphics.drawLine(x0, 0, x0, _height);
        _graphics.drawString(((Double) _x1).toString(), 1, y0 - 1);
        _graphics.drawString(((Double) _x2).toString(), _width - 30, y0 - 1);
        _graphics.drawString(((Double) _y1).toString(), x0 + 1, _height - 1);
        _graphics.drawString(((Double) _y2).toString(), x0 + 1, 10 + 1);
    }

    /**
	 * display graph
	 * @param title title of graph
	 * @param option display option
	 */
    public void display(String title, String option) {
        ImageFrame frame = new ImageFrame();
        drawGraph(option);
        drawAxis();
        frame.setTitle(title);
        frame.setImage(_image);
        int frameWidth = _width + ImageFrame.BORDER_WIDTH;
        int frameHeight = _height + ImageFrame.BORDER_HEIGHT;
        frame.setSize(frameWidth, frameHeight);
        frame.getCloseButton().setLabel("Close plot");
        frame.setVisible(true);
    }

    public static void testMultiple() {
        final int count = 5;
        Plot2D plot = new Plot2D(800, 800, count);
        double x1 = -10;
        double x2 = 10;
        double y1 = -2;
        double y2 = 10;
        Plot2D.function[] f = new Plot2D.function[count];
        f[0] = new Plot2D.function() {

            public double f(double x) {
                return java.lang.Math.sin(x);
            }
        };
        f[1] = new Plot2D.function() {

            public double f(double x) {
                return java.lang.Math.cos(x);
            }
        };
        f[2] = new Plot2D.function() {

            public double f(double x) {
                return 1 + x / 2;
            }
        };
        f[3] = new Plot2D.function() {

            public double f(double x) {
                return java.lang.Math.log(x);
            }
        };
        f[4] = new Plot2D.function() {

            public double f(double x) {
                return x + 2;
            }
        };
        String[] label = { "sin(x)", "cos(x)", "1+x/2", "log(x)", "x+2" };
        try {
            plot.setRange(x1, x2, y1, y2);
            plot.setFunction(f, label);
            plot.display("Multiple plot test", "type=line");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testSingle() {
        Plot2D plot = new Plot2D(800, 800);
        double x1 = -10;
        double x2 = 10;
        double y1 = -2;
        double y2 = 10;
        Plot2D.function f = new Plot2D.function() {

            public double f(double x) {
                return java.lang.Math.sin(x);
            }
        };
        try {
            plot.setRange(x1, x2, y1, y2);
            plot.setFunction(f, "sin(x)");
            plot.display("single plot test", "type=dot,step=10");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int factorial(int x) {
        if (x <= 1) return 1; else return x * factorial(x - 1);
    }

    public static void testExponentialDistribution() {
        final int count = 5;
        Plot2D plot = new Plot2D(800, 800, count);
        double x1 = 0;
        double x2 = 10;
        double y1 = 0;
        double y2 = 0.5;
        final double lamda = 0.5;
        Plot2D.function[] f = new Plot2D.function[count];
        f[0] = new Plot2D.function() {

            public double f(double x) {
                int y = 1;
                return java.lang.Math.pow(lamda * x, y) * java.lang.Math.exp(-lamda * x) / factorial(y);
            }
        };
        f[1] = new Plot2D.function() {

            public double f(double x) {
                int y = 2;
                return java.lang.Math.pow(lamda * x, y) * java.lang.Math.exp(-lamda * x) / factorial(y);
            }
        };
        f[2] = new Plot2D.function() {

            public double f(double x) {
                int y = 3;
                return java.lang.Math.pow(lamda * x, y) * java.lang.Math.exp(-lamda * x) / factorial(y);
            }
        };
        f[3] = new Plot2D.function() {

            public double f(double x) {
                int y = 4;
                return java.lang.Math.pow(lamda * x, y) * java.lang.Math.exp(-lamda * x) / factorial(y);
            }
        };
        f[4] = new Plot2D.function() {

            public double f(double x) {
                int y = 5;
                return java.lang.Math.pow(lamda * x, y) * java.lang.Math.exp(-lamda * x) / factorial(y);
            }
        };
        String[] label = { "pois(1)", "pois(2)", "pois(3)", "pois(4)", "pois(5)" };
        try {
            plot.setRange(x1, x2, y1, y2);
            plot.setFunction(f, label);
            plot.display("Multiple plot test", "type=line,label=no");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        testExponentialDistribution();
    }
}
