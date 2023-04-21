package zowie.fractals;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;

/**
 * An object to calculate the Mandelbrot set of complex numbers, and render them to an ImagePanel
 * Inspired by  http://en.wikipedia.org/wiki/Mandelbrot_set#For_programmers
 *
 * @author COWIEM
 */
public class Mandelbrot extends FractalCoords {

    public Mandelbrot(FractalCoords coords) {
        super(coords);
    }

    public Mandelbrot(Complex corner, Dimensions dimensions, int iters) {
        super(corner, dimensions, iters);
    }

    public void setCoords(Complex corner, Dimensions dimensions) {
        this.corner = new Complex(corner);
        this.dimensions = new Dimensions(dimensions);
    }

    /** Get the current view coordinates */
    public FractalCoords getCoords() {
        return new FractalCoords(corner, dimensions, iters);
    }

    /**
	 * Render the set & display it on the aforegiven ImagePanel.
	 * Adjust the complex coordinates to fix the aspect ratio of the ImagePanel
	 * @return 
	 */
    public Image calculate(Canvas canvas) {
        Point cDimensions = canvas.getSize();
        if (cDimensions.x == 0 && cDimensions.y == 0) return null;
        if (isTall(cDimensions)) {
            double y = dimensions.y;
            dimensions.y *= ((double) cDimensions.y / (double) cDimensions.x) / (dimensions.y / dimensions.x);
            corner.y -= (dimensions.y - y) / 2;
        } else {
            double x = dimensions.x;
            dimensions.x *= ((double) cDimensions.x / (double) cDimensions.y) / (dimensions.x / dimensions.y);
            corner.x -= (dimensions.x - x) / 2;
        }
        ImageData iData = new ImageData(cDimensions.x, cDimensions.y, 8, new PaletteData(palette));
        for (int y = 0; y < cDimensions.y; y++) for (int x = 0; x < cDimensions.x; x++) {
            double mapX = ((x * dimensions.x) / cDimensions.x) + corner.x;
            double mapY = ((y * dimensions.y) / cDimensions.y) + corner.y;
            iData.setPixel(x, y, calculatePixel(mapX, mapY));
        }
        return new Image(canvas.getDisplay(), iData);
    }

    /**
	 * Calculate the given pixel for the Mandelbrot set
	 * @param x, the real number
	 * @param y, the imaginary number
	 */
    private int calculatePixel(double x, double y) {
        double origX = x, origY = y;
        int i = 0;
        for (i = 0; x * x + y * y <= (2 * 2) && i < iters; i++) {
            double xtemp = x * x - y * y + origX;
            y = 2 * x * y + origY;
            x = xtemp;
        }
        return i == iters ? BLACK : (i * BLACK) / iters;
    }

    private static final int BLACK = 200;

    /** The palette used by the raw image */
    private static RGB palette[];

    static {
        palette = new RGB[BLACK + 1];
        palette[BLACK] = new RGB(0, 0, 0);
        for (int i = 0, c = 255; i < 50; i++, c -= 5) palette[i] = new RGB(0, 255 - c, c);
        for (int i = 50, c = 255; i < 100; i++, c -= 5) palette[i] = new RGB(255 - c, 255, 0);
        for (int i = 100, c = 255; i < 150; i++, c -= 5) palette[i] = new RGB(c, c, 255 - c);
        for (int i = 150, c = 255; i < 200; i++, c -= 5) palette[i] = new RGB(255 - c, 0, 255);
    }

    private static boolean isTall(Point dim) {
        return dim.y > dim.x;
    }
}
