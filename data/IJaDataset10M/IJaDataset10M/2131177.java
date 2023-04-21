package net.sf.wwusmart.algorithms;

import net.sf.wwusmart.algorithms.framework.*;
import net.sf.wwusmart.helper.ShapeFileFormatException;
import net.sf.wwusmart.helper.OffParser;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * View based similarity with fourier Zernike descriptors (FZD).<br/>
 * <b>Descriptor Computing:</b>
 * <ul>
 * <li>Normalize the shape that it fits into a sphere with radius <tt>0.5</tt>
 * and the origin in <tt>(0.5, 0.5, 0.5)</tt>.</li>
 * <li>Make ten orthogonal projections with the camera position on the vertices
 * of a regular dodecahedron. The opposites of the vertices are not needed.</li>
 * <li>For every projection image compute the FZDs.</li>
 * </ul><br/>
 * <b>Matching:</b><br/>
 * The dissimilarity measure is the minimum value from 60 different rotations.
 * For every rotation the dissimilarity is the sum over the ten euclidian
 * distances between the FZDs.
 *
 * @see <a href="http://www.cs.jhu.edu/~misha/Papers/Chen03.pdf">
 *          On Visual Similarity Based 3D Model Retrieval</a>
 *
 * @author Robert Scherer
 */
public class VbsMatching implements JavaAlgorithmImplementation {

    /**The number of coefficients for the fourier part of the descriptor*/
    private static final int NUMBER_OF_COEFFICIENTS = 10;

    /** The size of the images with projections */
    private static final int IMAGE_SIZE = 128;

    /** The 60 different rotations between two camera positions */
    private static int[][] rotations;

    /** The weight factor for the fourier descriptor */
    private static final float ALPHA = (float) 0.3;

    /** The weight factor for the Zernike descriptor */
    private static final float BETA = (float) 0.7;

    /** The width of the descriptor representation image */
    private static final int IMAGE_WIDTH = 800;

    /** The height of the descriptor representation image */
    private static final int IMAGE_HEIGHT = 1000;

    /** The name of the algorithm */
    private static final String NAME = "View based similarity";

    /** The name of the authors of the algorithm */
    private static final String AUTHORS_NAMES = "Robert Scherer";

    /** The actual version of the algorithm */
    private static final String VERSION = "1.0";

    /** A description for this algorithm */
    private static final String DESCRIPTION = "View based similarity with fourier Zernike descriptors (FZD).<br>" + "<b>Descriptor Computing:</b>" + "<ul><li>Normalize the shape that it fits into a sphere with radius <tt>0.5</tt> " + "and the origin in <tt>(0.5, 0.5, 0.5)</tt>.</li>" + "<li>Make ten orthogonal projections with the camera position on the vertices " + "of a regular dodecahedron. The opposites of the vertices are not needed.</li>" + "<li>For every projection image compute the FZDs.</li></ul><br>" + "<b>Matching:</b><br>" + "The dissimilarity measure is the minimum value from 60 different rotations. " + "For every rotation the dissimilarity is the sum over the ten euclidian distances between the FZDs.<br>" + "For detailed information see " + "<a href=\"http://www.cs.jhu.edu/~misha/Papers/Chen03.pdf\">" + "On Visual Similarity Based 3D Model Retrieval</a>";

    /**
     * Get the 60 different rotations between two camera positions
     */
    public void initialize() {
        rotations = getRotations();
    }

    /**
     * The name of the algoritm.
     *
     * @return name of the algorithm
     */
    public String getName() {
        return NAME;
    }

    /**
     * A description for the algorithm.
     *
     * @return a description for the algorithm
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Returns the actual version of the algorithm.
     *
     * @return actual version
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * Returns the type of shapes this algorithm can handle.
     *
     * @return <tt>EnumSet</tt> of {@link net.sf.wwusmart.algorithms.framework.ShapeType#OFF_3D}
     */
    public Set<ShapeType> getApplicableTypes() {
        return EnumSet.of(ShapeType.OFF_3D);
    }

    /**
     * No parameters needed for this algorithm.
     *
     * @return an empty Vector
     */
    public List<Parameter> getParameters() {
        return new Vector<Parameter>();
    }

    /**
     * Returns the names of the authors for this algorithm.
     *
     * @return names of the authors
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
     * Not needed for this algorithm.
     *
     * @throws net.sf.wwusmart.algorithms.framework.InvalidParameterValueException
     */
    public void processNewParameters() throws InvalidParameterValuesException {
        return;
    }

    /**
     * Renders a descriptor as ten curves of the ten different FZDs for the
     * ten views. Also shows the all values in a table.
     *
     * @param shapeData the original shape data
     * @param descData the descriptor stored in the database
     * @param renderer Descriptor renderer with many options to render descriptors
     */
    public void renderDescriptor(byte[] shapeData, byte[] descData, DescriptorRenderer renderer) {
        if (descData == null) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, "Error rendering descriptor: " + "descriptor data is null");
            return;
        }
        double[][] desc = new double[10][45];
        try {
            desc = (double[][]) net.sf.wwusmart.helper.ByteArrayHelper.getObject(descData);
        } catch (IOException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage bi = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, IMAGE_WIDTH - 1, IMAGE_HEIGHT - 1);
        g.drawLine(0, 1 * IMAGE_HEIGHT / 5, IMAGE_WIDTH, 1 * IMAGE_HEIGHT / 5);
        g.drawLine(0, 2 * IMAGE_HEIGHT / 5, IMAGE_WIDTH, 2 * IMAGE_HEIGHT / 5);
        g.drawLine(0, 3 * IMAGE_HEIGHT / 5, IMAGE_WIDTH, 3 * IMAGE_HEIGHT / 5);
        g.drawLine(0, 4 * IMAGE_HEIGHT / 5, IMAGE_WIDTH, 4 * IMAGE_HEIGHT / 5);
        g.drawLine(IMAGE_WIDTH / 2, 0, IMAGE_WIDTH / 2, IMAGE_HEIGHT);
        int posX = IMAGE_WIDTH / 2;
        int posY = -IMAGE_HEIGHT / 5;
        for (int i = 0; i < 10; i++) {
            BufferedImage oneBi = drawOneHistogram(desc[i]);
            if (i % 2 == 0) {
                posY += IMAGE_HEIGHT / 5;
            }
            g.drawImage(oneBi, (i % 2) * posX, posY, null);
        }
        renderer.render("Set of integrated descriptors", bi, DescriptorRenderer.Mode.IMAGEICON);
        DecimalFormat df = new DecimalFormat("0.000000");
        String descString = "";
        for (int i = 0; i < desc.length; i++) {
            descString += "Descripor " + (i + 1) + ":\n";
            for (int j = 0; j < desc[i].length; j++) {
                if (j == 0) {
                    descString += "Fourier coefficients:\n";
                }
                if (j % 5 == 0 && j != 0) {
                    descString += "\n";
                }
                if (j == 10) {
                    descString += "Zernike moments:\n";
                }
                descString += df.format(desc[i][j]) + " | ";
                if (j == desc[i].length - 1) {
                    descString += "\n----------------------------------------\n";
                }
            }
        }
        renderer.render("Values of all descriptors", descString, DescriptorRenderer.Mode.TEXT_STRING);
    }

    /**
     * Computes a descriptor by following steps.
     * <ul>
     * <li>Projections on the xy-plane with ten different rotations equaly distributed
     * over the vertices of a dodecahedron.</li>
     * <li>For the ten images a fourier-descriptor is computed after a contour-tracing
     * algoritm(Theo Pavlidis).</li>
     * <li>For every image a Zernike-moment-descriptor is computed with m = n = 10;
     * 35 magnitudes of the Zernike coefficients are stored.</li>
     *
     * The shape descriptor consists of ten descriptors, one for each image,
     * with 45 double values:<br/>
     * Fourier-descriptor: <tt>FD = (Fd0,...,Fd9)</tt><br/>
     * Zernike moments descriptor: <tt>ZD = (Zd0,...,Zd34)</tt><br/>
     * Integrated descriptor:
     * <tt>FZD = (alpha*Fd0, ..., alpha*Fd9, beta*Zd0, ..., beta*Zd34)</tt>
     * where alpha and beta are weight factors.
     *
     * @param shape the shape as byte array
     * @return integrated descriptor with <tt>10*45 double</tt> values
     */
    public byte[] computeDescriptor(byte[] shape) {
        if (shape == null) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, "Error computing descriptor: " + "shape data is null");
            return null;
        }
        String s = new String(shape);
        OffParser or = null;
        try {
            or = new OffParser(s);
        } catch (IOException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ShapeFileFormatException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        or.normalizeShapesInSphere(.5f, .5f, .5f, .5f);
        float[][] vertices = or.getVertices();
        int[][] faces = or.getFaces();
        BufferedImage[] bis = new BufferedImage[10];
        Point[] centroids = new Point[10];
        Polygon[] contour = new Polygon[10];
        for (int i = 0; i < 10; i++) {
            bis[i] = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bis[i].createGraphics();
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
            g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
            centroids[i] = new Point();
            contour[i] = new Polygon();
        }
        float[][] normVertices = rotateOFFX(vertices, Math.PI);
        projection(normVertices, faces, bis[0]);
        projection(rotateOFFY(normVertices, 2 * Math.PI / 5), faces, bis[1]);
        projection(rotateOFFY(normVertices, 4 * Math.PI / 5), faces, bis[2]);
        projection(rotateOFFX(normVertices, -(3 * Math.PI / 10)), faces, bis[3]);
        projection(rotateOFFX(rotateOFFY(normVertices, Math.PI / 5), Math.PI / 10), faces, bis[4]);
        projection(rotateOFFX(rotateOFFY(normVertices, Math.PI / 5), 2 * Math.PI / 5), faces, bis[5]);
        projection(rotateOFFX(rotateOFFY(normVertices, 2 * Math.PI / 5), -(3 * Math.PI / 10)), faces, bis[6]);
        projection(rotateOFFX(rotateOFFY(normVertices, 3 * Math.PI / 5), Math.PI / 10), faces, bis[7]);
        projection(rotateOFFX(rotateOFFY(normVertices, 3 * Math.PI / 5), 2 * Math.PI / 5), faces, bis[8]);
        projection(rotateOFFX(rotateOFFY(normVertices, 4 * Math.PI / 5), -(3 * Math.PI / 5)), faces, bis[9]);
        for (int i = 0; i < 10; i++) {
            contourTracing(bis[i], centroids[i], contour[i]);
        }
        double[][] fourierCoeffs = new double[10][10];
        for (int i = 0; i < 10; i++) {
            fourierCoeffs[i] = fourierDescriptor(contour[i], centroids[i]);
        }
        double[][] zernikeMoments = new double[10][35];
        for (int i = 0; i < 10; i++) {
            zernikeMoments[i] = zernikeDescriptor(bis[i], centroids[i]);
        }
        double[][] integratedDescriptor = new double[10][45];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 45; j++) {
                if (j < 10) {
                    integratedDescriptor[i][j] = ALPHA * fourierCoeffs[i][j];
                } else if (j >= 10) {
                    integratedDescriptor[i][j] = BETA * zernikeMoments[i][j - 10];
                }
            }
        }
        byte[] res;
        try {
            res = net.sf.wwusmart.helper.ByteArrayHelper.getBytes(integratedDescriptor);
            return res;
        } catch (IOException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * The similarity between two shapes is computed as the L2-Norm between
     * the two descriptor in different permutations.<br/>
     *
     * <tt>diss = min<sub>i</sub>{sum<sub>k=1</sub><sup>10</sup>(d(I1<sub>k</sub>, I2<sub>k</sub>))}  i=1,...,60</tt><br/>
     *
     * where <tt>d</tt> denotes the dissimilarity between two images(L1-norm), i denotes
     * the different rotations between the camera positions of the two 3D-shapes.
     * <tt>I1</tt>, <tt>I2</tt> are the correponding images under i-th rotation.
     *
     * @param d1 descriptor of shape one
     * @param d2 descriptor of shape two
     * @return similarity between the two descriptors
     */
    public double match(byte[] d1, byte[] d2) {
        if (d1 == null || d2 == null) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, "Error while matching: " + "at least one descriptor is null");
            return Double.NaN;
        }
        double[][] p1 = new double[10][45];
        double[][] p2 = new double[10][45];
        try {
            p1 = (double[][]) net.sf.wwusmart.helper.ByteArrayHelper.getObject(d1);
            p2 = (double[][]) net.sf.wwusmart.helper.ByteArrayHelper.getObject(d2);
        } catch (IOException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VbsMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < 60; i++) {
            double diss = 0;
            for (int j = 0; j < 10; j++) {
                diss += L2Norm(p1[j], p2[rotations[i][j]]);
            }
            if (diss < min) {
                min = diss;
            }
        }
        double normalizedDiss = min / Math.sqrt(45);
        double normalizedSimilarity = 1 - normalizedDiss;
        if (normalizedSimilarity < 0) {
            return 0;
        } else if (normalizedSimilarity > 1) return 1;
        return normalizedSimilarity;
    }

    /**
     * Contour-tracing algorithm by Theo Pavlidis.<br />
     * Extracts a <tt>Polygon</tt> from a <tt>BufferedImage</tt> with a black-and-white
     * painting by finding a black starting point and then walk walk around the
     * border of the silhouette. Also computes the centroid of the extracted contour
     * by side effect.
     *
     * @param bi <tt>BufferedImage</tt> with a silhouette
     * @param centroid the centroid of the silhouette
     * @return a contour as a polygon
     */
    private Polygon contourTracing(BufferedImage bi, Point centroid, Polygon poly) {
        int direction = 0;
        Point startPos = new Point(0, 0);
        search: for (int y = 0; y < IMAGE_SIZE; y++) {
            for (int x = 0; x < IMAGE_SIZE; x++) {
                if (getRGBorZero(new Point(x, y), bi) == Color.black.getRGB()) {
                    startPos.x = x;
                    startPos.y = y;
                    break search;
                }
            }
        }
        Point pos = startPos;
        int counter = 0;
        do {
            poly.addPoint(pos.x, pos.y);
            Point p1 = new Point(0, 0);
            Point p2 = new Point(0, 0);
            Point p3 = new Point(0, 0);
            if (direction == 3) {
                p1 = new Point(pos.x - 1, pos.y - 1);
                p2 = new Point(pos.x, pos.y - 1);
                p3 = new Point(pos.x + 1, pos.y - 1);
            } else if (direction == 2) {
                p1 = new Point(pos.x - 1, pos.y + 1);
                p2 = new Point(pos.x - 1, pos.y);
                p3 = new Point(pos.x - 1, pos.y - 1);
            } else if (direction == 1) {
                p1 = new Point(pos.x + 1, pos.y + 1);
                p2 = new Point(pos.x, pos.y + 1);
                p3 = new Point(pos.x - 1, pos.y + 1);
            } else if (direction == 0) {
                p1 = new Point(pos.x + 1, pos.y - 1);
                p2 = new Point(pos.x + 1, pos.y);
                p3 = new Point(pos.x + 1, pos.y + 1);
            }
            if (getRGBorZero(p1, bi) == Color.black.getRGB()) {
                pos = p1;
                if (direction == 0) {
                    direction = 3;
                } else {
                    direction--;
                }
            } else if (getRGBorZero(p2, bi) == Color.black.getRGB()) {
                pos = p2;
            } else if (getRGBorZero(p3, bi) == Color.black.getRGB()) {
                pos = p3;
            } else {
                direction = (direction + 1) % 4;
            }
            counter++;
        } while (!pos.equals(startPos) || counter <= 3);
        int centroidX = 0;
        int centroidY = 0;
        for (int x : poly.xpoints) {
            centroidX += x;
        }
        for (int y : poly.ypoints) {
            centroidY += y;
        }
        centroid.x = centroidX / poly.npoints;
        centroid.y = centroidY / poly.npoints;
        return poly;
    }

    /**
     * Draws one of the ten curves for the descriptor representation image.
     *
     * @param d one part of the descriptor
     * @return image with a curve representing one FZD
     */
    private BufferedImage drawOneHistogram(double[] d) {
        BufferedImage res = new BufferedImage(IMAGE_WIDTH / 2, IMAGE_HEIGHT / 5, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = res.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, res.getWidth(), res.getHeight());
        g.setColor(Color.BLACK);
        int gap = 10;
        g.drawLine(gap, gap, gap, res.getHeight() - gap);
        g.drawLine(gap, res.getHeight() - gap, res.getWidth() - gap, res.getHeight() - gap);
        double max = 0;
        for (int i = 0; i < d.length; i++) {
            if (max < d[i]) {
                max = d[i];
            }
        }
        double factor = 1 / max * (res.getHeight() - 20);
        double[] scaledD = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            scaledD[i] = d[i] * factor;
        }
        double step = (res.getWidth() - 2 * gap - 5) / d.length;
        for (int i = 0; i < 9; i++) {
            g.drawLine((int) (gap + i * step), (int) (res.getHeight() - gap - scaledD[i]), (int) (gap + (i + 1) * step), (int) (res.getHeight() - gap - scaledD[i + 1]));
        }
        for (int i = 10; i < d.length - 1; i++) {
            g.drawLine((int) (gap + (i - 1) * step), (int) (res.getHeight() - gap - scaledD[i]), (int) (gap + i * step), (int) (res.getHeight() - gap - scaledD[i + 1]));
        }
        return res;
    }

    /**
     * Computes the factorial of <tt>n</tt>.
     *
     * @param n parameter for the factorial
     * @return factorial(n)
     */
    private double fak(int n) {
        long result = 1;
        for (int i = 2; i <= n; ++i) {
            result *= i;
        }
        return result;
    }

    /**
      * Computes a fourier descriptor from a contour.
      *
      * @param poly the contour
      * @param centro the centroid of the contour
      * @return magnitudes of normalized fourier coefficients
      */
    private double[] fourierDescriptor(Polygon poly, Point centro) {
        double[] complexZero = new double[2];
        int[] contourX = new int[NUMBER_OF_COEFFICIENTS * 2];
        int[] contourY = new int[NUMBER_OF_COEFFICIENTS * 2];
        for (int i = 0; i < contourX.length; i++) {
            int step = poly.npoints / (NUMBER_OF_COEFFICIENTS * 2);
            contourX[i] = poly.xpoints[i * step];
        }
        for (int i = 0; i < contourY.length; i++) {
            int step = poly.npoints / (NUMBER_OF_COEFFICIENTS * 2);
            contourY[i] = poly.ypoints[i * step];
        }
        double[] distances = new double[NUMBER_OF_COEFFICIENTS * 2];
        for (int i = 0; i < NUMBER_OF_COEFFICIENTS * 2; i++) {
            double[] di = { (double) (contourX[i] - centro.x), (double) (contourY[i] - centro.y) };
            distances[i] = L2Norm(di, complexZero);
        }
        double[] aReal = new double[NUMBER_OF_COEFFICIENTS * 2];
        double[] aImag = new double[NUMBER_OF_COEFFICIENTS * 2];
        for (int i = 0; i < NUMBER_OF_COEFFICIENTS * 2; i++) {
            aReal[i] = 0;
            aImag[i] = 0;
            for (int j = 0; j < NUMBER_OF_COEFFICIENTS * 2; j++) {
                aReal[i] += distances[j] * Math.cos((-2 * Math.PI * i * j) / (double) (NUMBER_OF_COEFFICIENTS * 2));
                aImag[i] += distances[j] * Math.sin((-2 * Math.PI * i * j) / (double) (NUMBER_OF_COEFFICIENTS * 2));
            }
            aReal[i] = aReal[i] / (double) (NUMBER_OF_COEFFICIENTS * 2);
            aImag[i] = aImag[i] / (double) (NUMBER_OF_COEFFICIENTS * 2);
        }
        double[] res = new double[NUMBER_OF_COEFFICIENTS];
        double[] a0 = { aReal[0], aImag[0] };
        double normA0 = L2Norm(a0, complexZero);
        if (normA0 == 0) {
            double[] zeroes = new double[NUMBER_OF_COEFFICIENTS];
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
     * Returns the rgb value from point <tt>p</tt> of the <tt>BufferedImage</tt>
     * or 0 if <tt>p</tt> lies outside the image.
     *
     * @param p Point in- or outside the image
     * @param bi <tt>BufferedImage</tt> to get rgb values from
     * @return rgb value at point <tt>p</tt> or zero if <tt>p</tt> is outside the image
     */
    private int getRGBorZero(Point p, BufferedImage bi) {
        if (p.x < 0 || p.y < 0 || p.x > bi.getWidth() - 1 || p.y > bi.getHeight() - 1) {
            return 0;
        } else {
            return bi.getRGB(p.x, p.y);
        }
    }

    /**
     * Compute the L2-Norm of two vectors.
     *
     * @param d1 vector one
     * @param d2 vector two
     * @return L1-Norm of d1 and d2
     */
    private double L2Norm(double[] d1, double[] d2) {
        double resSquare = 0;
        for (int i = 0; i < d1.length; i++) {
            resSquare += Math.pow((d1[i] - d2[i]), 2);
        }
        double res = Math.sqrt(resSquare);
        return res;
    }

    /**
     * Z-buffer algorithm by just ignoring the z-values an draw the triangles
     * on a BufferedImage.
     *
     * @param vertices the vertices of the shape to project
     * @param faces the triangles of the shape to project
     * @param bi <tt>BufferedImage</tt> to paint on
     */
    private void projection(float[][] vertices, int[][] faces, BufferedImage bi) {
        for (int[] f : faces) {
            int[] xPoints = new int[f.length];
            int[] yPoints = new int[f.length];
            for (int i = 0; i < f.length; i++) {
                xPoints[i] = (int) (vertices[f[i]][0] * IMAGE_SIZE);
                yPoints[i] = (int) (vertices[f[i]][1] * IMAGE_SIZE);
            }
            Graphics2D g2d = bi.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, f.length);
            g2d.fillPolygon(xPoints, yPoints, f.length);
        }
    }

    /**
     * Rotates the shape by degree (in radian measure) around x-axis.
     *
     * @param vertices the vertices to rotate
     * @param degree rotation angle in radian measure
     * @return rotated vertices
     */
    private float[][] rotateOFFX(float[][] vertices, double degree) {
        float[][] rot = new float[vertices.length][3];
        float[][] verts = new float[vertices.length][3];
        for (int i = 0; i < vertices.length; i++) {
            verts[i][0] = vertices[i][0];
            verts[i][1] = vertices[i][1] - 0.5f;
            verts[i][2] = vertices[i][2] - 0.5f;
            rot[i][0] = verts[i][0];
            rot[i][1] = (float) (Math.cos(degree) * verts[i][1] - Math.sin(degree) * verts[i][2]);
            rot[i][2] = (float) (Math.sin(degree) * verts[i][1] + Math.cos(degree) * verts[i][2]);
            rot[i][0] = rot[i][0];
            rot[i][1] = rot[i][1] + 0.5f;
            rot[i][2] = rot[i][2] + 0.5f;
        }
        return rot;
    }

    /**
     * Rotates the shape by degree (in radian measure) around y-axis.
     *
     * @param vertices the vertices to rotate
     * @param degree rotation angle in radian measure
     * @return rotated vertices
     */
    private float[][] rotateOFFY(float[][] vertices, double degree) {
        float[][] rot = new float[vertices.length][3];
        float[][] verts = new float[vertices.length][3];
        for (int i = 0; i < vertices.length; i++) {
            verts[i][0] = vertices[i][0] - 0.5f;
            verts[i][1] = vertices[i][1];
            verts[i][2] = vertices[i][2] - 0.5f;
            rot[i][0] = (float) (Math.cos(degree) * verts[i][0] + Math.sin(degree) * verts[i][2]);
            rot[i][1] = verts[i][1];
            rot[i][2] = (float) (-Math.sin(degree) * verts[i][0] + Math.cos(degree) * verts[i][2]);
            rot[i][0] = rot[i][0] + 0.5f;
            rot[i][1] = rot[i][1];
            rot[i][2] = rot[i][2] + 0.5f;
        }
        return rot;
    }

    /**
     * Computes a Zernike descriptor.
     * With <tt>n</tt>(order) = <tt>m</tt>(repetition) = 10 ==> 35 coefficients
     *
     * @param bi the <tt>BufferedImage</tt> to compute the moments from
     * @param centro the centroid of the silhouette
     * @return 35 magnitudes of Zernike moments
     */
    private double[] zernikeDescriptor(BufferedImage bi, Point centro) {
        double[][] aNMReal = new double[11][11];
        double[][] aNMImag = new double[11][11];
        double diameter = (double) IMAGE_SIZE * Math.sqrt(2);
        int mass = 0;
        for (int n = 1; n <= 10; n++) {
            for (int m = 0; m <= n; m++) {
                if ((n - m) % 2 == 0) {
                    for (int x = 0; x < IMAGE_SIZE; x++) {
                        for (int y = 0; y < IMAGE_SIZE; y++) {
                            if (getRGBorZero(new Point(x, y), bi) == Color.black.getRGB()) {
                                mass++;
                                aNMReal[n][m] += zernikeVReal(n, m, x / diameter, y / diameter, centro.x / diameter, centro.y / diameter);
                                aNMImag[n][m] += -zernikeVImag(n, m, x / diameter, y / diameter, centro.x / diameter, centro.y / diameter);
                            }
                        }
                    }
                    aNMReal[n][m] = n + 1 / Math.PI * aNMReal[n][m];
                    aNMImag[n][m] = n + 1 / Math.PI * aNMReal[n][m];
                }
            }
        }
        double[] normalizedMagnitudes = new double[35];
        double[] complexZero = { 0, 0 };
        int counter = 0;
        for (int n = 0; n < 11; n++) {
            for (int m = 0; m < 11; m++) {
                if (aNMReal[n][m] != 0.0 || aNMImag[n][m] != 0.0) {
                    double[] val = { aNMReal[n][m], aNMImag[n][m] };
                    normalizedMagnitudes[counter] = (L2Norm(val, complexZero)) / (double) mass;
                    counter++;
                }
            }
        }
        return normalizedMagnitudes;
    }

    /**
     * The "R"-component of the Zernike moments.
     *
     * @param n order of Zernike moments
     * @param m repetition of Zernike moments
     * @param roh distance between (x,y) and the shape´s centroid
     * @return Zernike R-component
     */
    private double zernikeR(int n, int m, double roh) {
        double res = 0.0;
        int s = 0;
        while (s <= (n - m) / 2) {
            double numerator = fak(n - s);
            double denominator = fak(s) * fak(((n + m) / 2) - s) * fak(((n - m) / 2) - s);
            res += Math.pow(-1, s) * (numerator / denominator) * Math.pow(roh, (n - 2 * s));
            s++;
        }
        return res;
    }

    /**
     * The real "V"-component of Zernike-moments (ZM).
     *
     * @param n order of ZM
     * @param m repetition of ZM
     * @param x x-coordinate
     * @param y y-coordinate
     * @param centroX x-coordinate of the shapes centroid
     * @param centroY y-coordinate of the shapes centroid
     * @return the real-part of zernikes "V" component
     */
    private double zernikeVReal(int n, int m, double x, double y, double centroX, double centroY) {
        double[] p = { x, y };
        double[] ce = { centroX, centroY };
        double roh = L2Norm(p, ce);
        double theta = Math.atan2(y - centroY, x - centroX);
        double zernikeRMNRoh = zernikeR(n, m, roh);
        double vReal = zernikeRMNRoh * Math.cos(m * theta);
        return vReal;
    }

    /**
     * The imaginary "V"-component of Zernike-moments (ZM).
     *
     * @param n order of ZM
     * @param m repetition of ZM
     * @param x x-coordinate
     * @param y y-coordinate
     * @param centroX x-coordinate of the shapes centroid
     * @param centroY y-coordinate of the shapes centroid
     * @return the imag-part of zernikes "V" component
     */
    private double zernikeVImag(int n, int m, double x, double y, double centroX, double centroY) {
        double[] p = { x, y };
        double[] ce = { centroX, centroY };
        double roh = L2Norm(p, ce);
        double theta = Math.atan2(y - centroY, x - centroX);
        double zernikeRMNRoh = zernikeR(n, m, roh);
        double vImag = zernikeRMNRoh * Math.sin(m * theta);
        return vImag;
    }

    /**
     * 60 different permutation of the ten descriptors as the
     * symmetry-group of the dodecahedron (isomorph to A5).
     *
     * @return 60 different permutations of {0,9}
     */
    private int[][] getRotations() {
        int[][] rot = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, { 0, 2, 8, 6, 9, 3, 5, 4, 1, 7 }, { 0, 8, 1, 5, 7, 6, 3, 9, 2, 4 }, { 1, 5, 0, 8, 2, 4, 3, 9, 7, 6 }, { 1, 0, 7, 3, 6, 8, 4, 2, 5, 9 }, { 1, 7, 5, 4, 9, 3, 8, 6, 0, 2 }, { 2, 0, 4, 6, 5, 1, 9, 8, 3, 7 }, { 2, 4, 3, 9, 7, 6, 1, 5, 0, 8 }, { 2, 3, 0, 1, 8, 9, 6, 7, 4, 5 }, { 3, 2, 7, 1, 6, 4, 8, 0, 9, 5 }, { 3, 7, 9, 8, 5, 1, 4, 6, 2, 0 }, { 3, 9, 2, 4, 0, 8, 1, 5, 7, 6 }, { 4, 5, 6, 7, 8, 9, 0, 1, 2, 3 }, { 4, 6, 2, 0, 3, 7, 9, 8, 5, 1 }, { 4, 2, 5, 9, 1, 0, 7, 3, 6, 8 }, { 5, 1, 9, 8, 3, 7, 2, 0, 4, 6 }, { 5, 9, 4, 2, 6, 8, 7, 3, 1, 0 }, { 5, 4, 1, 7, 0, 2, 8, 6, 9, 3 }, { 6, 4, 8, 0, 9, 5, 3, 2, 7, 1 }, { 6, 8, 7, 3, 1, 0, 5, 9, 4, 2 }, { 6, 7, 4, 5, 2, 3, 0, 1, 8, 9 }, { 7, 3, 6, 8, 4, 2, 5, 9, 1, 0 }, { 7, 6, 1, 5, 0, 8, 2, 4, 3, 9 }, { 7, 1, 3, 2, 9, 5, 8, 0, 6, 4 }, { 8, 9, 0, 1, 2, 3, 4, 5, 6, 7 }, { 8, 0, 6, 4, 7, 1, 3, 2, 9, 5 }, { 8, 6, 9, 3, 5, 4, 1, 7, 0, 2 }, { 9, 5, 3, 2, 7, 1, 6, 4, 8, 0 }, { 9, 3, 8, 6, 0, 2, 1, 7, 5, 4 }, { 9, 8, 5, 1, 4, 6, 2, 0, 3, 7 }, { 0, 8, 2, 4, 3, 9, 7, 6, 1, 5 }, { 0, 2, 1, 7, 5, 4, 9, 3, 8, 6 }, { 0, 1, 8, 9, 6, 7, 4, 5, 2, 3 }, { 1, 7, 0, 2, 8, 6, 9, 3, 5, 4 }, { 1, 0, 5, 9, 4, 2, 6, 8, 7, 3 }, { 1, 5, 7, 6, 3, 9, 2, 4, 0, 8 }, { 2, 3, 4, 5, 6, 7, 8, 9, 0, 1 }, { 2, 4, 0, 8, 1, 5, 7, 6, 3, 9 }, { 2, 0, 3, 7, 9, 8, 5, 1, 4, 6 }, { 3, 9, 7, 6, 1, 5, 0, 8, 2, 4 }, { 3, 7, 2, 0, 4, 6, 5, 1, 9, 8 }, { 3, 2, 9, 5, 8, 0, 6, 4, 7, 1 }, { 4, 2, 6, 8, 7, 3, 1, 0, 5, 9 }, { 4, 6, 5, 1, 9, 8, 3, 7, 2, 0 }, { 4, 5, 2, 3, 0, 1, 8, 9, 6, 7 }, { 5, 1, 4, 6, 2, 0, 3, 7, 9, 8 }, { 5, 4, 9, 3, 8, 6, 0, 2, 1, 7 }, { 5, 9, 1, 0, 7, 3, 6, 8, 4, 2 }, { 6, 7, 8, 9, 0, 1, 2, 3, 4, 5 }, { 6, 8, 4, 2, 5, 9, 1, 0, 7, 3 }, { 6, 4, 7, 1, 3, 2, 9, 5, 8, 0 }, { 7, 1, 6, 4, 8, 0, 9, 5, 3, 2 }, { 7, 6, 3, 9, 2, 4, 0, 8, 1, 5 }, { 7, 3, 1, 0, 5, 9, 4, 2, 6, 8 }, { 8, 0, 9, 5, 3, 2, 7, 1, 6, 4 }, { 8, 9, 6, 7, 4, 5, 2, 3, 0, 1 }, { 8, 6, 0, 2, 1, 7, 5, 4, 9, 3 }, { 9, 3, 5, 4, 1, 7, 0, 2, 8, 6 }, { 9, 5, 8, 0, 6, 4, 7, 1, 3, 2 }, { 9, 8, 3, 7, 2, 0, 4, 6, 5, 1 } };
        return rot;
    }
}
