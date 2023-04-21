package csimage.demo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;
import java.util.Vector;
import javax.swing.SwingUtilities;
import csimage.PosBuffImage;
import csimage.ScaledPoint;
import csimage.TheMatrix;
import csimage.util.Utilities;
import csimage.util.dbg;

/**
 * @author I-Ling
 */
public class JuliaSets extends TheMatrix {

    /**
     *  
     */
    static final int MAX_ITER = 256;

    private final double x_min = -2;

    private final double y_min = -2;

    private final double x_max = 2;

    private final double y_max = 2;

    private final double cx = -0.567;

    private final double cy = -0.1278;

    private final double ratio = (x_max - x_min) / (y_max - y_min);

    private static int colorSchemeChoice;

    private final String DIR = "csjava/pictures/05/";

    public static void main(String[] args) {
        colorSchemeChoice = 3;
        julia();
    }

    public static void julia() {
        JuliaSets j = new JuliaSets(500, 500, true);
        j.calculateJulia();
        j.setVisible(true);
        dbg.sayln("done");
        dbg.sayln("all julia files will be saved in " + j.DIR + "...");
    }

    private boolean recalJulia = false;

    private boolean zoomOut = false;

    public void paint(Graphics g) {
        dbg.sayln("paint called");
        Graphics2D g2 = (Graphics2D) g;
        Insets insets = getInsets();
        if (recalJulia) {
            juliaSelectedSquare();
            recalJulia = false;
        }
        if (zoomOut) {
            ImageDataSet imgSet = (ImageDataSet) imageStack.pop();
            _image = imgSet.getImage();
            currStartX = imgSet.getX();
            currStartY = imgSet.getY();
            x_len = imgSet.getWidth();
            y_len = imgSet.getHeight();
            zoomOut = false;
        }
        g2.drawImage(_image, (int) (insets.left), (int) (insets.top), this);
        if (_mouseListen && _pressedPoint != null && _currentPoint != null) {
            drawSelectedRect(g2);
        }
    }

    public void drawSelectedRect(Graphics2D g2) {
        Rectangle rect = convertRectangleToDraw(_pressedPoint, _currentPoint);
        Color c = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        g2.setColor(c);
        g2.draw(rect);
    }

    public JuliaSets(int width, int height) {
        super(width, height);
    }

    public JuliaSets(int width, int height, boolean mouseListen) {
        super(width, height, mouseListen);
    }

    private double currStartX = x_min;

    private double currStartY = y_min;

    private double x_len = x_max - x_min;

    private double y_len = y_max - y_min;

    private Stack imageStack = new Stack();

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            recalJulia = true;
        }
    }

    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (SwingUtilities.isRightMouseButton(e) && !imageStack.isEmpty()) {
            zoomOut = true;
            repaint();
        }
    }

    public double pixelToJuliaX(int x) {
        return pixelToJuliaX((double) x);
    }

    public double pixelToJuliaX(double x) {
        assert _image != null;
        int width = getImageWidth();
        dbg.say(dbg.pairln("width, new_currX: ", "" + width, "" + (currStartX + (x / width) * x_len)));
        return currStartX + (x / width) * x_len;
    }

    public double pixelToJuliaY(int y) {
        return pixelToJuliaY((double) y);
    }

    public double pixelToJuliaY(double y) {
        assert _image != null;
        int height = getImageHeight();
        dbg.say(dbg.pairln("height, new_currY: ", "" + height, "" + (currStartY + (y / height) * y_len)));
        return currStartY + (y / height) * y_len;
    }

    public void calculateJulia() {
        int width = getImageWidth();
        int height = getImageHeight();
        dbg.sayln("height = " + height + ", width = " + width);
        final double scale_x = x_len / width;
        final double scale_y = y_len / height;
        dbg.sayln("recalculate julia");
        dbg.say(dbg.pairln("scales: ", "" + scale_x, "" + scale_y));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double x = currStartX + scale_x * i;
                double y = currStartY + scale_y * j;
                double z_real = x;
                double z_plex = y;
                int k = 0;
                for (; k < MAX_ITER; k++) {
                    double old_z_real = z_real;
                    z_real = z_real * z_real - z_plex * z_plex + cx;
                    z_plex = 2 * z_plex * old_z_real + cy;
                    double z_norm_squared = z_real * z_real + z_plex * z_plex;
                    if (z_norm_squared > 4) {
                        break;
                    }
                }
                setColor(i, j, selectColorScheme(colorSchemeChoice, k));
            }
        }
    }

    private int imageCount = 0;

    private int _bgImageIndex = 0;

    private Vector _positionedImages = new Vector();

    private void juliaSelectedSquare() {
        assert _mouseListen && _pressedPoint != null && _releasedPoint != null;
        dbg.sayln("store julia...");
        ImageDataSet imgSet = new ImageDataSet(_image, currStartX, currStartY, x_len, y_len);
        imageStack.push(imgSet);
        saveJulia(_image);
        if (_pressedPoint.getX() != _releasedPoint.getX() && _pressedPoint.getY() != _releasedPoint.getY()) {
            dbg.sayln("need to recalculate julia...");
            double minX = Math.min(_pressedPoint.getX(), _releasedPoint.getX());
            double minY = Math.min(_pressedPoint.getY(), _releasedPoint.getY());
            dbg.sayln(dbg.pair("top-left", "" + minX, "" + minY));
            double maxX = Math.max(_pressedPoint.getX(), _releasedPoint.getX());
            double maxY = Math.max(_pressedPoint.getY(), _releasedPoint.getY());
            dbg.sayln(dbg.pair("bottom-right", "" + maxX, "" + maxY));
            doJuliaSquare(minX, minY, maxX, maxY);
            resetMousePoints();
        } else {
            juliaSelectedPoint();
        }
    }

    private void doJuliaSquare(double minX, double minY, double maxX, double maxY) {
        double endX = pixelToJuliaX(maxX);
        double endY = pixelToJuliaY(maxY);
        currStartX = pixelToJuliaX(minX);
        currStartY = pixelToJuliaY(minY);
        x_len = endX - currStartX;
        y_len = endY - currStartY;
        calculateJulia();
    }

    private void saveJulia(BufferedImage image) {
        dbg.sayln("save julia...");
        File outDir = new File(DIR);
        if (!outDir.exists()) {
            if (!outDir.mkdir()) {
                dbg.sayln("save not done");
                return;
            }
        }
        String name = "Julia" + imageCount + ".jpg";
        imageCount++;
        saveAsPngImage(DIR + name);
    }

    private void juliaSelectedPoint() {
        assert _mouseListen && _clickedPoint != null;
        dbg.sayln("recalculate julia for clicks...");
        doJuliaSquareByRatio(_clickedPoint.getX(), _clickedPoint.getY(), 2.0d);
        resetMousePoints();
    }

    private void doJuliaSquareByRatio(double midX, double midY, double magScale) {
        double centerX = pixelToJuliaX(midX);
        double centerY = pixelToJuliaY(midY);
        x_len = x_len / magScale;
        y_len = x_len / ratio;
        dbg.say(dbg.pairln("new x-y len: ", "" + x_len, "" + y_len));
        currStartX = centerX - x_len / 2;
        currStartY = centerY - y_len / 2;
        dbg.say(dbg.pairln("new x-y: ", "" + currStartX, "" + currStartY));
        calculateJulia();
    }

    private void resetMousePoints() {
        _pressedPoint = null;
        _releasedPoint = null;
        _currentPoint = null;
    }

    public Color selectColorScheme(int choice, int k) {
        switch(choice) {
            case 1:
                return ColorUtility.colorScheme1(k, MAX_ITER);
            case 2:
                return ColorUtility.colorScheme2(k, MAX_ITER);
            case 3:
                return ColorUtility.colorScheme3(k, MAX_ITER);
            case 4:
                return ColorUtility.colorScheme4(k, MAX_ITER);
            case 5:
            default:
                return ColorUtility.colorScheme5(k, MAX_ITER);
        }
    }

    public BufferedImage copy(BufferedImage source) {
        int width = _image.getWidth();
        int height = _image.getHeight();
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        AffineTransform xform = AffineTransform.getScaleInstance(1, 1);
        g2.drawRenderedImage(source, xform);
        g2.dispose();
        return target;
    }

    /**
     * Returns an image which is a composition of two images. Whichever is
     * larger will appear to be on the bottom.
     * 
     * @param destImg
     *            an image to be placed on the bottom of the new composed image,
     *            unless it is smaller than srcImg
     * @param srcImg
     *            an image to be placed on the top, unless it is larger than
     *            destImg
     * @param x
     *            x-coordinate of the top (smaller) image
     * @param y
     *            y-coordinate of the top (smaller) image
     * @return a result image after composition
     */
    public BufferedImage composeLoadedImages(BufferedImage destImg, BufferedImage srcImg, int x, int y) {
        int width = destImg.getWidth();
        int height = destImg.getHeight();
        boolean rebuild = false;
        if (srcImg.getWidth() > width) {
            width = srcImg.getWidth();
            rebuild = true;
        }
        if (srcImg.getHeight() > height) {
            height = srcImg.getHeight();
            rebuild = true;
        }
        if (rebuild) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2D = img.createGraphics();
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2D.drawImage(destImg, 0, 0, this);
            g2D.drawImage(srcImg, x, y, this);
            return img;
        } else {
            Graphics2D destG2D = destImg.createGraphics();
            destG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            destG2D.drawImage(srcImg, x, y, this);
            return destImg;
        }
    }

    /**
     * Returns an image which is a composition of two images. Whichever is
     * larger will appear to be on the bottom.
     * 
     * @param destImg
     *            an image to be placed on the bottom of the new composed image,
     *            unless it is smaller than srcImg
     * @param srcImg
     *            an image to be placed on the top at its original location,
     *            unless it is larger than destImg
     * @return a result image after composition
     */
    public BufferedImage composeLoadedImages(BufferedImage destImg, PosBuffImage srcImg) {
        return composeLoadedImages(destImg, srcImg.getImage(), srcImg.getX(), srcImg.getY());
    }

    /**
     * Returns an image (width x height) composed of a vector of images stored
     * in _positionedImages. The original background image will stay the same.
     * 
     * @param width
     *            width of the composed image
     * @param height
     *            height of the composed image
     * @return a result image after composition
     */
    public BufferedImage composeLoadedImages(int width, int height) {
        BufferedImage composedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D destG2D = composedImage.createGraphics();
        destG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        PosBuffImage locImg = (PosBuffImage) _positionedImages.get(_bgImageIndex);
        destG2D.drawImage(locImg.getImage(), locImg.getX(), locImg.getY(), this);
        for (int i = 0; i < _positionedImages.size(); ++i) {
            if (i != _bgImageIndex) {
                locImg = (PosBuffImage) _positionedImages.get(i);
                destG2D.drawImage(locImg.getImage(), locImg.getX(), locImg.getY(), this);
            }
        }
        return composedImage;
    }

    /**
     * Returns a Rectangle object of the four given coordinates x1, y1, x2, and
     * y2 having this method for two purposes: (1) Since Java Rectangle can only
     * construct from the left-top corner point and the width and the height of
     * the rectangle, this method serves as an alternative constructor, which
     * determins the left-top corner coordinate, the width and the height (2)
     * Taking x1, y1, x2, y2 from mouse event directly, they are scaled and
     * positioned corresponding to the image object. To draw the rectangle on
     * the application frame, the actual pixel has to include "unscaled" insets
     * 
     * @param x1
     *            a candidate x-coordinate of the rectangle
     * @param y1
     *            a candidate y-coordinate of the rectangle
     * @param x2
     *            a candidate x-coordinate of the rectangle
     * @param y2
     *            a candidate y-coordinate of the rectangle
     * @return a rectangle of the four given coordinates
     */
    public Rectangle convertRectangleToDraw(int x1, int y1, int x2, int y2) {
        int x = convertXtoDraw(Math.min(x1, x2));
        int y = convertYtoDraw(Math.min(y1, y2));
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);
        return new Rectangle(x, y, w, h);
    }

    /**
     * Returns a Rectangle object of the two given points p1 and p2
     * 
     * @param p1
     *            a point of the rectangle
     * @param p2
     *            another point of the rectangle
     * @return a rectangle of the two given points
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public Rectangle convertRectangleToDraw(Point p1, Point p2) {
        return convertRectangleToDraw(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Returns a Rectangle object of the two given points p1 and p2 after scaled
     * 
     * @param p1
     *            a scaled point of the rectangle
     * @param p2
     *            another scaled point of the rectangle
     * @return a rectangle of the two given points after scaled
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public Rectangle convertRectangleToDraw(ScaledPoint sp1, ScaledPoint sp2) {
        return convertRectangleToDraw(sp1.getX(), sp1.getY(), sp2.getX(), sp2.getY());
    }

    /**
     * Converts a scaled x-coordinate on an image and returns the corresponding
     * x-coordinate of a pixel on the application frame to include insets... for
     * drawing purposes
     * 
     * @param scaledX
     *            scaled x-coordinate of a pixel
     * @return x-coordinate of a pixel on the application frame
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public int convertXtoDraw(int scaledX) {
        Insets insets = this.getInsets();
        return scaledX + (int) (insets.left);
    }

    /**
     * Converts a mouse clicked point on an image and returns the corresponding
     * x-coordinate of a pixel on the application frame to include insets... for
     * drawing purposes
     * 
     * @param point
     *            mouse clicked point of a pixel
     * @return x-coordinate of a pixel on the application frame
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public int convertXtoDraw(Point point) {
        return convertXtoDraw(point.x);
    }

    /**
     * Converts a scaled mouse clicked point on an image and returns the
     * corresponding x-coordinate of a pixel on the application frame to include
     * insets... for drawing purposes
     * 
     * @param spt
     *            scaled mouse clicked point of a pixel
     * @return x-coordinate of a pixel on the application frame
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public int convertXtoDraw(ScaledPoint spt) {
        return convertXtoDraw(spt.getX());
    }

    /**
     * Converts a scaled y-coordinate on an image and returns the corresponding
     * y-coordinate of a pixel on the application frame to include insets... for
     * drawing purposes
     * 
     * @param scaledY
     *            scaled y-coordinate of a pixel
     * @return y-coordinate of a pixel on the application frame
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public int convertYtoDraw(int scaledY) {
        Insets insets = this.getInsets();
        return scaledY + (int) (insets.top);
    }

    /**
     * Converts a mouse clicked point on an image and returns the corresponding
     * y-coordinate of a pixel on the application frame to include insets... for
     * drawing purposes
     * 
     * @param point
     *            mouse clicked point of a pixel
     * @return y-coordinate of a pixel on the application frame
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public int convertYtoDraw(Point point) {
        return convertYtoDraw(point.y);
    }

    /**
     * Converts a scaled mouse clicked point on an image and returns the
     * corresponding y-coordinate of a pixel on the application frame to include
     * insets... for drawing purposes
     * 
     * @param spt
     *            scaled mouse clicked point of a pixel
     * @return y-coordinate of a pixel on the application frame
     * @see convertRectangleToDraw(int x1, int y1, int x2, int y2)
     */
    public int convertYtoDraw(ScaledPoint spt) {
        return convertYtoDraw(spt.getY());
    }

    /**
     * Loads an image from the given path as a background image on the bottom.
     * When there is only one image loaded, this image will be the main image of
     * this object.
     * 
     * @param path
     *            (fully qualified) name of where to load the image
     */
    public void loadBackgroundImage(String path) {
        BufferedImage img = Utilities.getBufferedImageFromFile(path);
        if (img != null) {
            _positionedImages.add((PosBuffImage) new PosBuffImage(img, 0, 0));
            _bgImageIndex = _positionedImages.size() - 1;
            if (_image == null) {
                setImage(img);
            } else {
                setImage(composeLoadedImages(img.getWidth(), img.getHeight()));
            }
            dbg.sayln("background " + path + " loaded...");
        }
    }

    /**
     * Loads an image from the given path.
     * 
     * @param path
     *            (fully qualified) name of where to load the image
     */
    public void loadImage(String path) {
        loadImage(path, 0, 0);
    }

    /**
     * Loads an image from the given path to (x, y) position.
     * 
     * @param path
     *            (fully qualified) name of where to load the image
     * @param x
     *            x-coordinate of where the image goes to
     * @param y
     *            y-coordinate of where the image goes to
     */
    public void loadImage(String path, int x, int y) {
        loadImageAt(path, x, y);
    }

    public void loadImage(String path, Point p) {
        loadImageAt(path, p.x, p.y);
    }

    /**
     * Loads an image from the given path to (x, y) position.
     * 
     * @param path
     *            (fully qualified) name of where to load the image
     * @param x
     *            x-coordinate of where the image goes to
     * @param y
     *            y-coordinate of where the image goes to
     */
    public void loadImageAt(String path, int x, int y) {
        BufferedImage img = Utilities.getBufferedImageFromFile(path);
        if (img != null) {
            _positionedImages.add((PosBuffImage) new PosBuffImage(img, x, y));
            if (_positionedImages.size() == 1) {
                dbg.sayln("set image");
                setImage(img);
            } else {
                dbg.sayln("compose image");
                setImage(composeLoadedImages(_image, img, x, y));
            }
            dbg.sayln("image " + path + " loaded at " + dbg.pair("" + x, "" + y));
        }
    }

    public void loadImageAt(String path, Point p) {
        loadImageAt(path, p.x, p.y);
    }

    /**
     * Loads multiple images from an array of path's.
     * 
     * @param paths
     *            an array of (fully qualified) names of where to load the
     *            images
     */
    public void loadImages(String[] paths) {
        for (int i = 0; i < paths.length; ++i) {
            loadImageAt(paths[i], 0, 0);
        }
    }

    /**
     * Loads multiple images from an array of path's to corresponding (x, y)
     * positions.
     * 
     * @param paths
     *            an array of (fully qualified) names of where to load the
     *            images
     * @param xs
     *            an array of x-coordinates of where the image goes to; its
     *            indice must correspond to the ones of paths
     * @param ys
     *            an array of y-coordinates of where the image goes to; its
     *            indice must correspond to the ones of paths
     */
    public void loadImages(String[] paths, int[] xs, int[] ys) {
        assert paths.length == xs.length && xs.length == ys.length : "the number of image file names is consistent with the number of coordinates";
        for (int i = 0; i < paths.length; ++i) {
            loadImageAt(paths[i], xs[i], ys[i]);
        }
    }

    /**
     * Loads multiple images from an array of path's to corresponding points
     * 
     * @param paths
     *            an array of (fully qualified) names of where to load the
     *            images
     * @param points
     *            an array of points of where the image goes to; its indice must
     *            correspond to the ones of paths
     */
    public void loadImages(String[] paths, Point[] points) {
        assert paths.length == points.length : "the number of image file names is consistent with the number of coordinates";
        for (int i = 0; i < paths.length; ++i) {
            loadImageAt(paths[i], points[i].x, points[i].y);
        }
    }

    /**
     * Loads an image previously implemented for testing the speed of image
     * loading
     * 
     * @param path
     *            (fully qualified) names of where to load the image
     */
    public void loadImageTest(String path) {
        BufferedImage img = Utilities.loadBufferedImageFromFile(path);
        if (img != null) {
            _positionedImages.add((PosBuffImage) new PosBuffImage(img, 0, 0));
            setImage(img);
        }
    }

    /**
     * Loads a JPEG image from the given path.
     * 
     * @param path
     *            (fully qualified) name of where to load the image
     */
    public void loadJPEGImage(String path) {
        BufferedImage img = Utilities.getBufferedImageFromJPEGFile(path);
        if (img != null) {
            _positionedImages.add((PosBuffImage) new PosBuffImage(img, 0, 0));
            setImage(img);
        }
    }

    class ImageDataSet {

        private BufferedImage img;

        private double x;

        private double y;

        private double width;

        private double height;

        ImageDataSet(BufferedImage im, double startX, double startY, double x_len, double y_len) {
            img = copy(im);
            x = startX;
            y = startY;
            width = x_len;
            height = y_len;
        }

        BufferedImage getImage() {
            return img;
        }

        double getX() {
            return x;
        }

        double getY() {
            return y;
        }

        double getWidth() {
            return width;
        }

        double getHeight() {
            return height;
        }
    }
}
