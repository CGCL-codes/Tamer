package net.sf.wwusmart.algorithms;

import net.sf.wwusmart.algorithms.framework.*;
import net.sf.wwusmart.helper.ContourFileParser;
import net.sf.wwusmart.helper.ShapeFileFormatException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 2D Matching algorithm with fourier descriptors(FDs).<br />
 * Needs clean boundary contours<br />
 * <b>Descriptor computing:</b><br />
 * <ul>
 * <li> Compute fourier coefficients a<sub>n</sub>, <tt>n = 0, 1, ..., N-1</tt> where <tt>N</tt> is the number
 *      of boundary points. In this implementation the number of boundary points is <tt>60</tt>.</li>
 * <li> The fourier descriptor (FD) consists of the 30 <tt>double</tt> values between 0 and 1:<br />
 *      <center><tt> FD = (|a<sub>1</sub>|/|a<sub>0</sub>|, |a<sub>2</sub>|/|a<sub>0</sub>|, ..., |a<sub>N/2</sub>|/|a<sub>0</sub>|)</tt></center>
 * </li>
 * </ul>
 * <b>Matching:</b><br />
 * The dissimilarity value is computed as the euclidian distance between two FDs.
 *
 * @see <a href="http://knight.cis.temple.edu/~lakaemper/courses/cis595_2004/papers/fourierShape.pdf">
 *              A Comparative Study on Shape Retrieval Using Fourier Descriptors
 *              with Different Shape Signatures</a>
 *
 * @author Robert Scherer
 */
public class Fourier2DMatching implements JavaAlgorithmImplementation {

    /** The number of values for the fourier descriptor */
    private static final int NUMBER_OF_VALUES = 30;

    /** The heigth of the descriptor representation image */
    private static final int IMAGE_HEIGHT = 256;

    /** The width of the descriptor representation image */
    private static final int IMAGE_WIDTH = 512;

    /** The name of the algorithm */
    private static final String NAME = "Fourier based similarity";

    /** The name of the author */
    private static final String AUTHORS_NAMES = "Robert Scherer";

    /** The version of this algorithm */
    private static final String VERSION = "1.0";

    /** A description for this algorithm */
    private static final String DESCRIPTION = "2D Matching algorithm with fourier-descriptors.<br>" + "Needs clean boundary contours.<br>" + "<b>Descriptor computing:</b><br>" + "<ul>" + "<li> Compute fourier coefficients a<sub>n</sub>, <tt>n=0,1,...N-1</tt> where <tt>N</tt> is the number" + "of boundary points. In this implementation the number of boundary points is <tt>60</tt>.</li>" + "<li> The fourier descriptor (FD) consists of the 30 <tt>double</tt> values:<br>" + "<center><tt> FD = (|a<sub>1</sub>|/|a<sub>0</sub>|, |a<sub>2</sub>|/|a<sub>0</sub>|, ..., |a<sub>N/2</sub>|/|a<sub>0</sub>|)</tt></center>" + "</li>" + "</ul>" + "<a href=\"http://knight.cis.temple.edu/~lakaemper/courses/cis595_2004/papers/fourierShape.pdf\">" + "A Comparative Study on Shape Retrieval Using Fourier Descriptors with Different Shape Signatures</a>";

    /**
     * No initialize for this algorithm.
     */
    public void initialize() {
    }

    /**
     * Returns the name of the algorithm.
     *
     * @return author: "Robert Scherer"
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns a description for this algorithm.
     *
     * @return a description for this algorithm
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Returns the actual version of this algorithm
     *
     * @return actual version ("1.0")
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * No parameters used in this algorithm.
     *
     * @return an empty list
     */
    public List<Parameter> getParameters() {
        List<Parameter> l = new LinkedList<Parameter>();
        return l;
    }

    /**
     * Returns the type of shapes this algorithm can handle.
     *
     * @return <tt>EnumSet</tt> of {@link net.sf.wwusmart.algorithms.framework.ShapeType#CONTOUR_2D}
     */
    public Set<ShapeType> getApplicableTypes() {
        return EnumSet.of(ShapeType.CONTOUR_2D);
    }

    /**
     * Returns the authors of the algorithm.
     *
     * @return names of authors
     */
    public String getAuthors() {
        return AUTHORS_NAMES;
    }

    /**
     * Returns <tt>true</tt>
     *
     * @return <tt>true</tt>
     */
    public boolean isQueryShapeMandatory() {
        return true;
    }

    /**
     * Not needed for this algorithm
     *
     * @throws net.sf.wwusmart.algorithms.framework.InvalidParameterValueException
     *      never thrown
     */
    public void processNewParameters() throws InvalidParameterValuesException {
    }

    /**
     * Renders a descriptor as a curve of fourier coefficents and a
     * string representation of the values of the fourier descriptor.
     *
     * @param shapeData content of .c-file
     * @param descData the fourier descriptor stored in the database
     * @param renderer Descriptor renderer with many options to render descriptors
     */
    public void renderDescriptor(byte[] shapeData, byte[] descData, DescriptorRenderer renderer) {
        double[] desc = new double[NUMBER_OF_VALUES];
        if (descData == null) {
            renderer.render("Error", "Error rendering descriptor: " + "descriptor data is null", DescriptorRenderer.Mode.TEXT_STRING);
            Logger.getLogger(FourierZernike2DMatching.class.getName()).log(Level.FINER, "Error rendering descriptor: Data is null.");
            return;
        }
        try {
            desc = (double[]) net.sf.wwusmart.helper.ByteArrayHelper.getObject(descData);
        } catch (IOException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage bi = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(1, 1, IMAGE_WIDTH - 3, IMAGE_HEIGHT - 3);
        g.drawLine(20, 20, 20, IMAGE_HEIGHT - 20);
        g.drawLine(20, IMAGE_HEIGHT - 20, IMAGE_WIDTH - 20, IMAGE_HEIGHT - 20);
        int step = (IMAGE_WIDTH - 50) / desc.length;
        int[] scaledDesc = new int[desc.length];
        double max = 0;
        for (int i = 0; i < desc.length; i++) {
            max = Math.max(max, desc[i]);
        }
        double factor = 1 / max * (IMAGE_HEIGHT - 60);
        for (int i = 0; i < desc.length; i++) {
            scaledDesc[i] = (int) (desc[i] * factor);
        }
        g.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.darkGray);
        for (int i = 0; i < desc.length - 1; i++) {
            g.drawLine(22 + step * i, IMAGE_HEIGHT - 22 - scaledDesc[i], 22 + step * (i + 1), IMAGE_HEIGHT - 22 - scaledDesc[i + 1]);
        }
        renderer.render("Centroid Contour", bi, DescriptorRenderer.Mode.IMAGEICON);
        DecimalFormat df = new DecimalFormat("0.000000");
        String descString = "";
        for (int i = 1; i <= desc.length; i++) {
            descString += df.format(desc[i - 1]) + " | ";
            if (i % 5 == 0) {
                descString += "\n";
            }
        }
        renderer.render("Values", descString, DescriptorRenderer.Mode.TEXT_STRING);
    }

    /**
     * Computes a fourier descriptor from the contour of a shape.
     * The descriptor consists of an array of 30 <tt>double</tt> values between 0 and 1;
     *
     * @param shape the shape (content of .c file) as bytearray
     * @return fourier descriptor with 30 coefficients as bytearray
     */
    public byte[] computeDescriptor(byte[] shape) {
        if (shape == null) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, "Error computing descriptor: " + "shape data is null");
            return null;
        }
        ContourFileParser cfp = null;
        try {
            cfp = new ContourFileParser(shape);
        } catch (IOException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ShapeFileFormatException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
        Polygon contour = cfp.getPolygon();
        Point centroid = cfp.getCentroid();
        double[] fc = fourierDescriptor(contour, centroid);
        try {
            byte[] res = net.sf.wwusmart.helper.ByteArrayHelper.getBytes(fc);
            return res;
        } catch (IOException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Matches two shape by computing the euclidian distance between the two
     * descriptors.
     *
     * @param d1 descriptor of shape one
     * @param d2 descriptor of shape two
     * @return similarity measure between 0 and 1
     */
    public double match(byte[] d1, byte[] d2) {
        if (d1 == null && d2 == null) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, "Error while matching: " + "both descriptors are null");
            return Double.NaN;
        }
        double[] desc1 = new double[NUMBER_OF_VALUES];
        double[] desc2 = new double[NUMBER_OF_VALUES];
        try {
            desc1 = (double[]) net.sf.wwusmart.helper.ByteArrayHelper.getObject(d1);
            desc2 = (double[]) net.sf.wwusmart.helper.ByteArrayHelper.getObject(d2);
        } catch (IOException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Fourier2DMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
        double dissimilarity = L2Norm(desc1, desc2);
        return 1 - dissimilarity;
    }

    /**
      * Computes a fourier descriptor from a contour given as a <tt>Polygon</tt>.
      *
      * @param poly the contour
      * @param centro the centroid of the contour
      * @return magnitudes of normalized fourier coefficients
      */
    private double[] fourierDescriptor(Polygon poly, Point centro) {
        double[] complexZero = new double[2];
        int[] contourX = new int[NUMBER_OF_VALUES * 2];
        int[] contourY = new int[NUMBER_OF_VALUES * 2];
        double[] c = { centro.x, centro.y };
        int step = poly.npoints / (NUMBER_OF_VALUES * 2);
        for (int i = 0; i < contourX.length; i++) {
            contourX[i] = poly.xpoints[i * step];
        }
        for (int i = 0; i < contourY.length; i++) {
            contourY[i] = poly.ypoints[i * step];
        }
        double[] distances = new double[NUMBER_OF_VALUES * 2];
        for (int i = 0; i < NUMBER_OF_VALUES * 2; i++) {
            double[] p = { (double) contourX[i], (double) contourY[i] };
            distances[i] = L2Norm(p, c);
        }
        double[] aReal = new double[NUMBER_OF_VALUES * 2];
        double[] aImag = new double[NUMBER_OF_VALUES * 2];
        for (int i = 0; i < NUMBER_OF_VALUES * 2; i++) {
            aReal[i] = 0;
            aImag[i] = 0;
            for (int j = 0; j < NUMBER_OF_VALUES * 2; j++) {
                aReal[i] += distances[j] * Math.cos((-2 * Math.PI * i * j) / (double) (NUMBER_OF_VALUES * 2));
                aImag[i] += distances[j] * Math.sin((-2 * Math.PI * i * j) / (double) (NUMBER_OF_VALUES * 2));
            }
            aReal[i] = aReal[i] / (double) (NUMBER_OF_VALUES * 2);
            aImag[i] = aImag[i] / (double) (NUMBER_OF_VALUES * 2);
        }
        double[] res = new double[NUMBER_OF_VALUES];
        double[] a0 = { aReal[0], aImag[0] };
        double normA0 = L2Norm(a0, complexZero);
        if (normA0 == 0) {
            double[] zeroes = new double[NUMBER_OF_VALUES];
            return zeroes;
        } else {
            for (int i = 1; i <= res.length; i++) {
                double[] ai = { aReal[i], aImag[i] };
                res[i - 1] = L2Norm(ai, complexZero) / normA0;
            }
            return res;
        }
    }

    /**
     * Computes the L2-Norm between two vectors of the same length.
     *
     * @param d1 vector one
     * @param d2 vector two
     * @return L2-Norm of d1 and d2
     */
    private double L2Norm(double[] d1, double[] d2) {
        double resSquare = 0;
        for (int i = 0; i < d1.length; i++) {
            resSquare += Math.pow((d1[i] - d2[i]), 2);
        }
        double res = Math.sqrt(resSquare);
        return res;
    }
}
