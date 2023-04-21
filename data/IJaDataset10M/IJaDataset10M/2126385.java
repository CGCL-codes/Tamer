package java.awt.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * Convolution filter.
 * 
 * ConvolveOp convolves the source image with a Kernel to generate a
 * destination image.  This involves multiplying each pixel and its neighbors
 * with elements in the kernel to compute a new pixel.
 * 
 * Each band in a Raster is convolved and copied to the destination Raster.
 * 
 * For BufferedImages, convolution is applied to all components.  If the
 * source is not premultiplied, the data will be premultiplied before
 * convolving.  Premultiplication will be undone if the destination is not
 * premultiplied.  Color conversion will be applied if needed.
 * 
 * @author jlquinn@optonline.net
 */
public class ConvolveOp implements BufferedImageOp, RasterOp {

    /** Edge pixels are set to 0. */
    public static final int EDGE_ZERO_FILL = 0;

    /** Edge pixels are copied from the source. */
    public static final int EDGE_NO_OP = 1;

    private Kernel kernel;

    private int edge;

    private RenderingHints hints;

    /**
   * Construct a ConvolveOp.
   * 
   * The edge condition specifies that pixels outside the area that can be
   * filtered are either set to 0 or copied from the source image.
   * 
   * @param kernel The kernel to convolve with.
   * @param edgeCondition Either EDGE_ZERO_FILL or EDGE_NO_OP.
   * @param hints Rendering hints for color conversion, or null.
   */
    public ConvolveOp(Kernel kernel, int edgeCondition, RenderingHints hints) {
        this.kernel = kernel;
        edge = edgeCondition;
        this.hints = hints;
    }

    /**
   * Construct a ConvolveOp.
   * 
   * The edge condition defaults to EDGE_ZERO_FILL.
   * 
   * @param kernel The kernel to convolve with.
   */
    public ConvolveOp(Kernel kernel) {
        this.kernel = kernel;
        edge = EDGE_ZERO_FILL;
        hints = null;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (src == dst) throw new IllegalArgumentException();
        if (dst == null) dst = createCompatibleDestImage(src, src.getColorModel());
        BufferedImage src1 = src;
        if (!src.isPremultiplied) {
            src1 = createCompatibleDestImage(src, src.getColorModel());
            src.copyData(src1.getRaster());
            src1.coerceData(true);
        }
        BufferedImage dst1 = dst;
        if (!src.getColorModel().equals(dst.getColorModel())) dst1 = createCompatibleDestImage(src, src.getColorModel());
        filter(src1.getRaster(), dst1.getRaster());
        if (dst1 != dst) {
            Graphics2D gg = dst.createGraphics();
            gg.setRenderingHints(hints);
            gg.drawImage(dst1, 0, 0, null);
            gg.dispose();
        }
        return dst;
    }

    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        return new BufferedImage(dstCM, src.getRaster().createCompatibleWritableRaster(), src.isPremultiplied, null);
    }

    public RenderingHints getRenderingHints() {
        return hints;
    }

    /**
   * @return The edge condition.
   */
    public int getEdgeCondition() {
        return edge;
    }

    /**
   * Returns (a clone of) the convolution kernel.
   *
   * @return The convolution kernel.
   */
    public Kernel getKernel() {
        return (Kernel) kernel.clone();
    }

    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (src == dest) throw new IllegalArgumentException();
        if (src.getWidth() < kernel.getWidth() || src.getHeight() < kernel.getHeight()) throw new ImagingOpException(null);
        if (dest == null) dest = createCompatibleDestRaster(src); else if (src.numBands != dest.numBands) throw new ImagingOpException(null);
        if (edge == EDGE_ZERO_FILL) {
            float[] zeros = new float[src.getNumBands() * src.getWidth() * (kernel.getYOrigin() - 1)];
            Arrays.fill(zeros, 0);
            dest.setPixels(src.getMinX(), src.getMinY(), src.getWidth(), kernel.getYOrigin() - 1, zeros);
        } else {
            float[] vals = new float[src.getNumBands() * src.getWidth() * (kernel.getYOrigin() - 1)];
            src.getPixels(src.getMinX(), src.getMinY(), src.getWidth(), kernel.getYOrigin() - 1, vals);
            dest.setPixels(src.getMinX(), src.getMinY(), src.getWidth(), kernel.getYOrigin() - 1, vals);
        }
        float[] kvals = kernel.getKernelData(null);
        float[] tmp = new float[kernel.getWidth() * kernel.getHeight()];
        for (int y = src.getMinY() + kernel.getYOrigin(); y < src.getMinY() + src.getHeight() - kernel.getYOrigin() / 2; y++) {
            float[] t1 = new float[(kernel.getXOrigin() - 1) * src.getNumBands()];
            if (edge == EDGE_ZERO_FILL) Arrays.fill(t1, 0); else src.getPixels(src.getMinX(), y, kernel.getXOrigin() - 1, 1, t1);
            dest.setPixels(src.getMinX(), y, kernel.getXOrigin() - 1, 1, t1);
            for (int x = src.getMinX(); x < src.getWidth() + src.getMinX(); x++) {
                for (int b = 0; b < src.getNumBands(); b++) {
                    float v = 0;
                    src.getSamples(x, y, kernel.getWidth(), kernel.getHeight(), b, tmp);
                    for (int i = 0; i < tmp.length; i++) v += tmp[i] * kvals[i];
                    dest.setSample(x, y, b, v);
                }
            }
            float[] t2 = new float[(kernel.getWidth() / 2) * src.getNumBands()];
            if (edge == EDGE_ZERO_FILL) Arrays.fill(t2, 0); else src.getPixels(src.getMinX() + src.getWidth() - (kernel.getWidth() / 2), y, kernel.getWidth() / 2, 1, t2);
            dest.setPixels(src.getMinX() + src.getWidth() - (kernel.getWidth() / 2), y, kernel.getWidth() / 2, 1, t2);
        }
        for (int y = src.getMinY(); y < src.getHeight() + src.getMinY(); y++) for (int x = src.getMinX(); x < src.getWidth() + src.getMinX(); x++) {
        }
        for (int y = src.getMinY(); y < src.getHeight() + src.getMinY(); y++) for (int x = src.getMinX(); x < src.getWidth() + src.getMinX(); x++) {
        }
        if (edge == EDGE_ZERO_FILL) {
            float[] zeros = new float[src.getNumBands() * src.getWidth() * (kernel.getHeight() / 2)];
            Arrays.fill(zeros, 0);
            dest.setPixels(src.getMinX(), src.getHeight() + src.getMinY() - (kernel.getHeight() / 2), src.getWidth(), kernel.getHeight() / 2, zeros);
        } else {
            float[] vals = new float[src.getNumBands() * src.getWidth() * (kernel.getHeight() / 2)];
            src.getPixels(src.getMinX(), src.getHeight() + src.getMinY() - (kernel.getHeight() / 2), src.getWidth(), kernel.getHeight() / 2, vals);
            dest.setPixels(src.getMinX(), src.getHeight() + src.getMinY() - (kernel.getHeight() / 2), src.getWidth(), kernel.getHeight() / 2, vals);
        }
        return dest;
    }

    public WritableRaster createCompatibleDestRaster(Raster src) {
        return src.createCompatibleWritableRaster();
    }

    public Rectangle2D getBounds2D(BufferedImage src) {
        return src.getRaster().getBounds();
    }

    public Rectangle2D getBounds2D(Raster src) {
        return src.getBounds();
    }

    /** Return corresponding destination point for source point.
   * 
   * ConvolveOp will return the value of src unchanged.
   * @param src The source point.
   * @param dst The destination point.
   * @see java.awt.image.RasterOp#getPoint2D(java.awt.geom.Point2D,
   * java.awt.geom.Point2D)
   */
    public Point2D getPoint2D(Point2D src, Point2D dst) {
        if (dst == null) return (Point2D) src.clone();
        dst.setLocation(src);
        return dst;
    }
}
