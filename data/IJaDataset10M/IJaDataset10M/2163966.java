package jpiv2;

import javax.media.jai.PlanarImage;
import javax.media.jai.JAI;
import javax.media.jai.ROI;
import javax.media.jai.operator.DFTDescriptor;
import java.awt.image.renderable.ParameterBlock;
import java.util.List;
import java.io.BufferedWriter;

/** Calculates the two dimensional cross-correlation function of two images.
 *
 */
public final class PivUtil {

    /** Calculates the two dimensional cross-correlation function of two images.
     * @param piA The first image (e.g. interrogation area).
     * @param piB The second image (e.g. interrogation area).
     * @return The correlation function.
     **/
    public static PlanarImage correlate(PlanarImage piA, PlanarImage piB) {
        int w = piA.getWidth();
        int h = piA.getHeight();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(piA);
        pb.add(DFTDescriptor.SCALING_UNITARY);
        pb.add(DFTDescriptor.REAL_TO_COMPLEX);
        piA = (PlanarImage) JAI.create("dft", pb);
        pb.removeSources();
        pb.addSource(piA);
        piA = (PlanarImage) JAI.create("conjugate", pb);
        pb.removeSources();
        pb.addSource(piB);
        piB = (PlanarImage) JAI.create("dft", pb);
        pb.removeSources();
        pb.removeParameters();
        pb.addSource(piA);
        pb.addSource(piB);
        PlanarImage corr = (PlanarImage) JAI.create("multiplycomplex", pb);
        pb.removeSources();
        pb.removeParameters();
        pb.addSource(corr);
        pb.add(DFTDescriptor.SCALING_UNITARY);
        pb.add(DFTDescriptor.COMPLEX_TO_REAL);
        corr = (PlanarImage) JAI.create("idft", pb);
        pb.removeSources();
        pb.removeParameters();
        pb.addSource(corr);
        pb.add(corr.getWidth() / 2);
        pb.add(corr.getHeight() / 2);
        corr = (PlanarImage) JAI.create("periodicshift", pb);
        if (w < corr.getWidth() || h < corr.getHeight()) {
            pb.removeSources();
            pb.removeParameters();
            pb.addSource(corr);
            pb.add((float) -(corr.getWidth() - w) / 2);
            pb.add((float) -(corr.getHeight() - h) / 2);
            pb.add(null);
            corr = (PlanarImage) JAI.create("translate", pb);
            pb.removeSources();
            pb.removeParameters();
            pb.addSource(corr);
            pb.add(0f);
            pb.add(0f);
            pb.add((float) w);
            pb.add((float) h);
            return ((PlanarImage) JAI.create("crop", pb));
        } else {
            return ((PlanarImage) corr);
        }
    }

    /** Divides the correlation matrix by a pyramid weighting function.
     * @param subCorrMat The biased correlation function
     * @param xOffset If this matrix is merely a search area within a larger
     * correlation matrix, this is the offset of the search area.
     * @param yOffset If this matrix is merely a search area within a larger
     * correlation matrix, this is the offset of the search area.
     * @param w Width of the original correlation matrix.
     * @param h Height of the original correlation matrix.
     * @return The corrected correlation function
     */
    public static float[][] divideByWeightingFunction(float[][] subCorrMat, int xOffset, int yOffset, int w, int h) {
        for (int i = 0; i < subCorrMat.length; i++) {
            for (int j = 0; j < subCorrMat[0].length; j++) {
                subCorrMat[i][j] = subCorrMat[i][j] * (Math.abs(j + xOffset - w / 2) / w * 2 + Math.abs(i + yOffset - h / 2) / h * 2 + 1);
            }
        }
        return subCorrMat;
    }

    /** Searches for the highest value in a correlation matrix.
     * A pyramid weighting function is applied prior to peak search.
     * Gaussian Peak Fit is used for sub pixel accuracy.
     * @param subCorrMat A single correlation matrix.
     * @param x the x coordinate of the search area
     * @param y the y coordinate of the search area
     * @param w width of the search area
     * @param h height of the search area
     * @return The indices of the highest value {i,j} or {y,x} and the peak height.
     */
    public static double[] getGaussianPeak(PlanarImage subCorrMat, int x, int y, int w, int h) {
        int[] iCoord = new int[2];
        double[] dCoord = new double[2];
        double height;
        float[][] pixelBlock = getPixelBlock(subCorrMat, x, y, w, h);
        pixelBlock = divideByWeightingFunction(pixelBlock, x, y, w, h);
        iCoord = getPeak(pixelBlock);
        dCoord = gaussianPeakFit(pixelBlock, iCoord[0], iCoord[1]);
        if (Math.abs(dCoord[0] - iCoord[0]) < w / 2 && Math.abs(dCoord[1] - iCoord[1]) < h / 2) {
            dCoord[0] += x;
            dCoord[1] += y;
            height = pixelBlock[iCoord[1]][iCoord[0]];
        } else {
            dCoord[0] = 0;
            dCoord[1] = 0;
            height = -1;
        }
        double[] ret = { dCoord[0], dCoord[1], height };
        return (ret);
    }

    /** Searches for the highest value in a correlation matrix.
     * A pyramid weighting function is applied prior to peak search.
     * Parabolic Peak Fit is used for sub pixel accuracy.
     * @param subCorrMat A single correlation matrix.
     * @param x the x coordinate of the search area
     * @param y the y coordinate of the search area
     * @param w width of the search area
     * @param h height of the search area
     * @return The indices of the highest value {i,j} or {y,x} and the peak height.
     */
    public static double[] getParabolicPeak(PlanarImage subCorrMat, int x, int y, int w, int h) {
        int[] iCoord = new int[2];
        double[] dCoord = new double[2];
        double height;
        float[][] pixelBlock = getPixelBlock(subCorrMat, x, y, w, h);
        pixelBlock = divideByWeightingFunction(pixelBlock, x, y, w, h);
        iCoord = getPeak(pixelBlock);
        dCoord = parabolicPeakFit(pixelBlock, iCoord[0], iCoord[1]);
        if (Math.abs(dCoord[0] - iCoord[0]) < w / 2 && Math.abs(dCoord[1] - iCoord[1]) < h / 2) {
            dCoord[0] += x;
            dCoord[1] += y;
            height = pixelBlock[iCoord[1]][iCoord[0]];
        } else {
            dCoord[0] = 0;
            dCoord[1] = 0;
            height = -1;
        }
        double[] ret = { dCoord[0], dCoord[1], height };
        return (ret);
    }

    /** Finds the highest value in a correlation matrix (i=y, j=x)
     * @param subCorrMat A single correlation matrix.
     * @param x the x coordinate of the search area
     * @param y the y coordinate of the search area
     * @param w width of the search area
     * @param h height of the search area
     * @return The indices of the highest value {i,j} or {y,x}.
     */
    public static int[] getPeak(float[][] subCorrMat, int x, int y, int w, int h) {
        int[] coord = new int[2];
        float peakValue = 0;
        for (int i = y; i < y + h; ++i) {
            for (int j = x; j < x + w; ++j) {
                if (subCorrMat[i][j] > peakValue) {
                    peakValue = subCorrMat[i][j];
                    coord[0] = j;
                    coord[1] = i;
                }
            }
        }
        return (coord);
    }

    /** Finds the highest value in a correlation matrix.
     * @param subCorrMat A single correlation matrix.
     * @return The indices of the highest value {i,j} or {y,x}.
     */
    public static int[] getPeak(float[][] subCorrMat) {
        int[] coord = new int[2];
        float peakValue = 0;
        for (int i = 0; i < subCorrMat.length; ++i) {
            for (int j = 0; j < subCorrMat[0].length; ++j) {
                if (subCorrMat[i][j] > peakValue) {
                    peakValue = subCorrMat[i][j];
                    coord[0] = j;
                    coord[1] = i;
                }
            }
        }
        return (coord);
    }

    /** Finds the highest value in a correlation matrix (i=y, j=x)
     * @param subCorrMat A single correlation matrix.
     * @param x the x coordinate of the search area
     * @param y the y coordinate of the search area
     * @param w width of the search area
     * @param h height of the search area
     * @return The indices of the highest value {i,j} or {y,x}.
     */
    public static int[] getPeak(PlanarImage subCorrMat, int x, int y, int w, int h) {
        int[] peak = getPeak(getPixelBlock(subCorrMat, x, y, w, h));
        peak[0] += x;
        peak[1] += y;
        return (peak);
    }

    /**
     * Finds the highest value in a correlation matrix (i=y, j=x)
     * @param subCorrMat A single correlation matrix.
     * @return The indices of the highest value {i,j} or {y,x}.
     * @param roi Limit the search area to a region of interest.
     */
    public static int[] getPeak(PlanarImage subCorrMat, ROI roi) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(subCorrMat);
        pb.add(roi);
        pb.add(1);
        pb.add(1);
        pb.add(true);
        pb.add(1);
        subCorrMat = JAI.create("extrema", pb);
        List maxLocations = ((List[]) subCorrMat.getProperty("maxLocations"))[0];
        return ((int[]) maxLocations.get(0));
    }

    /**
     * Gaussian peak fit.
     * See Raffel, Willert, Kompenhans; 
     * Particle Image Velocimetry; 
     * 3rd printing; 
     * S. 131 
     * for details
     * @param subCorrMat some two dimensional data containing a correlation peak
     * @param x the horizontal peak position
     * @param y the vertical peak position
     * @return a double array containing the peak position 
     * with sub pixel accuracy
     */
    public static double[] gaussianPeakFit(float[][] subCorrMat, int x, int y) {
        double[] coord = new double[2];
        if (x == 0 || x == subCorrMat[0].length - 1 || y == 0 || y == subCorrMat.length - 1) {
            coord[0] = x;
            coord[1] = y;
        } else {
            coord[0] = x + (Math.log(subCorrMat[y][x - 1]) - Math.log(subCorrMat[y][x + 1])) / (2 * Math.log(subCorrMat[y][x - 1]) - 4 * Math.log(subCorrMat[y][x]) + 2 * Math.log(subCorrMat[y][x + 1]));
            coord[1] = y + (Math.log(subCorrMat[y - 1][x]) - Math.log(subCorrMat[y + 1][x])) / (2 * Math.log(subCorrMat[y - 1][x]) - 4 * Math.log(subCorrMat[y][x]) + 2 * Math.log(subCorrMat[y + 1][x]));
        }
        return (coord);
    }

    /**
     * Gaussian peak fit.
     * See Raffel, Willert, Kompenhans; 
     * Particle Image Velocimetry; 
     * 3rd printing; 
     * S. 131 
     * for details
     * A pyramid weighting function is applied to the fife values that are
     * used for the peak fit.
     * @param subCorrMat some two dimensional data containing a correlation peak
     * @param i the horizontal peak position
     * @param j the vertical peak position
     * @return a double array containing the peak position 
     * with sub pixel accuracy
     */
    public static double[] gaussianPeakFit(PlanarImage subCorrMat, int i, int j) {
        int w = subCorrMat.getWidth();
        int h = subCorrMat.getHeight();
        double[] coord = new double[2];
        if (i == 0 || i == subCorrMat.getWidth() - 1 || j == 0 || j == subCorrMat.getHeight() - 1) {
            coord[0] = i;
            coord[1] = j;
        } else {
            float[][] pixelBlock = get3x3PixelBlock(subCorrMat, i, j);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    pixelBlock[y][x] = pixelBlock[y][x] * (Math.abs(x - w / 2) / w * 2 + Math.abs(y - h / 2) / h * 2 + 1);
                }
            }
            coord[0] = i + ((Math.log(pixelBlock[1][0]) - Math.log(pixelBlock[1][2])) / (2 * Math.log(pixelBlock[1][0]) - 4 * Math.log(pixelBlock[1][1]) + 2 * Math.log(pixelBlock[1][2])));
            coord[1] = j + ((Math.log(pixelBlock[0][1]) - Math.log(pixelBlock[2][1])) / (2 * Math.log(pixelBlock[0][1]) - 4 * Math.log(pixelBlock[1][1]) + 2 * Math.log(pixelBlock[2][1])));
            pixelBlock = null;
        }
        return (coord);
    }

    /**
     * Parabolic peak fit.
     * See Raffel, Willert, Kompenhans; 
     * Particle Image Velocimetry; 
     * 3rd printing; 
     * S. 131 
     * for details
     * @param subCorrMat some two dimensional data containg a correlation peak
     * @param x the horizontal position of the correlation peak
     * @param y the vertical position of the correlation peak
     * @return a double array containing the peak position with sub-pixel accuracy
     */
    public static double[] parabolicPeakFit(float[][] subCorrMat, int x, int y) {
        int w = subCorrMat[0].length;
        int h = subCorrMat.length;
        double[] coord = new double[2];
        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) {
            coord[0] = x;
            coord[1] = y;
        } else {
            coord[0] = x + (subCorrMat[y][x - 1] - subCorrMat[y][x + 1]) / (2 * subCorrMat[y][x - 1] - 4 * subCorrMat[y][x] + 2 * subCorrMat[y][x + 1]);
            coord[1] = y + (subCorrMat[y - 1][x] - subCorrMat[y + 1][x]) / (2 * subCorrMat[y - 1][x] - 4 * subCorrMat[y][x] + 2 * subCorrMat[y + 1][x]);
        }
        return (coord);
    }

    /**
     * Parabolic peak fit.
     * See Raffel, Willert, Kompenhans; 
     * Particle Image Velocimetry; 
     * 3rd printing; 
     * S. 131 
     * for details
     * A pyramid weighting function is applied to the fife values that are
     * used for the peak fit.
     * @param subCorrMat some two dimensional data containg a correlation peak
     * @param i the horizontal position of the correlation peak
     * @param j the vertical position of the correlation peak
     * @return a double array containing the peak position with sub-pixel accuracy
     */
    public static double[] parabolicPeakFit(PlanarImage subCorrMat, int i, int j) {
        int w = subCorrMat.getWidth();
        int h = subCorrMat.getHeight();
        double[] coord = new double[2];
        if (i == 0 || i == subCorrMat.getWidth() - 1 || j == 0 || j == subCorrMat.getHeight() - 1) {
            coord[0] = i;
            coord[1] = j;
        } else {
            float[][] pixelBlock = get3x3PixelBlock(subCorrMat, i, j);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    pixelBlock[y][x] = pixelBlock[y][x] * (Math.abs(x - w / 2) / w * 2 + Math.abs(y - h / 2) / h * 2 + 1);
                }
            }
            coord[0] = i + (pixelBlock[1][0] - pixelBlock[1][2]) / (2 * pixelBlock[1][0] - 4 * pixelBlock[1][1] + 2 * pixelBlock[1][2]);
            coord[1] = j + (pixelBlock[0][1] - pixelBlock[2][1]) / (2 * pixelBlock[0][1] - 4 * pixelBlock[1][1] + 2 * pixelBlock[2][1]);
            pixelBlock = null;
        }
        return (coord);
    }

    private static float[][] get3x3PixelBlock(PlanarImage pi, int x, int y) {
        float[] fArray = new float[3 * 3];
        float[][] pixelBlock = new float[3][3];
        java.awt.Rectangle rec = new java.awt.Rectangle(x - 1, y - 1, 3, 3);
        fArray = pi.getData(rec).getSamples(x - 1, y - 1, 3, 3, 0, fArray);
        for (int i = 0; i < 3; i++) {
            System.arraycopy(fArray, i * 3, pixelBlock[i], 0, 3);
        }
        fArray = null;
        return (pixelBlock);
    }

    private static float[][] getPixelBlock(PlanarImage pi, int x, int y, int w, int h) {
        float[] fArray = new float[h * w];
        float[][] pixelBlock = new float[h][w];
        java.awt.Rectangle rec = new java.awt.Rectangle(x, y, w, h);
        fArray = pi.getData(rec).getSamples(x, y, w, h, 0, fArray);
        for (int i = 0; i < h; i++) {
            System.arraycopy(fArray, i * w, pixelBlock[i], 0, w);
        }
        return (pixelBlock);
    }

    /** Creates random particle position volumes of a Poiseuille flow.
     * The output files of this method might be used by the synthetic image
     * generation program of the EUROPIV II Project "Synthetic Image Generator" 
     * (SIG), Feb. 2001, by Bertrand Lecordier: Bertrand.Lecordier@coria.fr, 
     * Jose Nogueira: goriba@ing.uc3m.es, and Jerry Westerweel: 
     * j.westerweel@wbmt.tudelft.nl.
     * @param bwA A buffered writer for the random particle positions.
     * @param bwB A buffered writer for the diplaced particle positions.
     * @param vx The x dimension of the particle volume (real units).
     * @param vy The y dimension of the particle volume (real units).
     * @param vz The z dimension of the particle volume (real units).
     * @param a Eularian nutation angle of the particle volume (rad).
     * @param b Eularian rotation angle of the particle volume (rad).
     * @param c Eularian precession angle of the particle volume (rad).
     * @param np Number of particles.
     * @param dp Maximum displacement (real units).
     * @param w With of flow domain (fraction of vx, vy).
     */
    public static void createRandomParticleVolumes(BufferedWriter bwA, BufferedWriter bwB, double vx, double vy, double vz, double a, double b, double c, int np, double dp, double w) {
        try {
            System.out.print("Create particles...");
            double x, y, z, dz;
            java.text.DecimalFormat dfnum = (java.text.DecimalFormat) java.text.NumberFormat.getInstance(java.util.Locale.US);
            dfnum.applyPattern("0.000E00");
            int progress = 20;
            for (int n = 0; n < np; n++) {
                x = Math.random() * vx - vx / 2;
                y = Math.random() * vy - vy / 2;
                z = Math.random() * vz - vz / 2;
                dz = -dp / ((vx * w / 2) * (vx * w / 2)) * (x * x + y * y) + dp;
                if (dz < 0) dz = 0;
                if (dz < 0) dz = 0;
                bwA.write(dfnum.format(x * (Math.cos(b) * Math.cos(c) - Math.cos(a) * Math.sin(b) * Math.sin(c)) + y * (Math.sin(b) * Math.cos(c) + Math.cos(a) * Math.cos(b) * Math.sin(c)) + z * Math.sin(a) * Math.sin(c)) + "\t" + dfnum.format(x * (-Math.cos(b) * Math.sin(c) - Math.cos(a) * Math.sin(b) * Math.cos(c)) + y * (-Math.sin(b) * Math.sin(c) + Math.cos(a) * Math.cos(b) * Math.cos(c)) + z * Math.sin(a) * Math.cos(c)) + "\t" + dfnum.format(x * (Math.sin(a) * Math.sin(b)) + y * (-Math.sin(a) * Math.cos(b)) + z * Math.cos(a)));
                bwA.newLine();
                bwB.write(dfnum.format(x * (Math.cos(b) * Math.cos(c) - Math.cos(a) * Math.sin(b) * Math.sin(c)) + y * (Math.sin(b) * Math.cos(c) + Math.cos(a) * Math.cos(b) * Math.sin(c)) + (z + dz) * Math.sin(a) * Math.sin(c)) + "\t" + dfnum.format(x * (-Math.cos(b) * Math.sin(c) - Math.cos(a) * Math.sin(b) * Math.cos(c)) + y * (-Math.sin(b) * Math.sin(c) + Math.cos(a) * Math.cos(b) * Math.cos(c)) + (z + dz) * Math.sin(a) * Math.cos(c)) + "\t" + dfnum.format(x * (Math.sin(a) * Math.sin(b)) + y * (-Math.sin(a) * Math.cos(b)) + (z + dz) * Math.cos(a)));
                bwB.newLine();
                if (n == np / progress) {
                    System.out.print(n + "...");
                    progress = progress / 2;
                }
            }
            System.out.println("done.");
            bwA.flush();
            bwA.close();
            bwB.flush();
            bwB.close();
        } catch (java.io.IOException e) {
            System.err.println("IO exception while creating the particle position files.");
        }
    }

    /** Creates reference vector data of a Poiseuille flow.
     * The flow direction will be tilted by the specified angles. The output
     * data represents a number of parallel cuts through the flow (scan). 
     * @param outputFileName A file name for the output data, will be numbered automatically.
     * @param vx The x dimension of the particle volume (real units).
     * @param vy The y dimension of the particle volume (real units).
     * @param vz The z dimension of the particle volume (real units).
     * @param a Eularian nutation angle of the particle volume (rad).
     * @param b Eularian rotation angle of the particle volume (rad).
     * @param c Eularian precession angle of the particle volume (rad).
     * @param dp Maximum displacement (real units).
     * @param w Width of flow domain (fraction of vx, vy).
     * @param nsp Number of scan planes.
     * @param dsp distance between scan planes (unit: real).
     * @param scaleX horizontal scaling (pixel/real units).
     * @param scaleY vertical scaling (pixel/real units).(This parameter is just 
     * added for future compatibility. For the moment, pixels are assumed to be 
     * square: scaleX = scaleY = scaleZ)
     * @param x0 x-position of first vector.
     * @param y0 y-position of first vector.
     * @param hx horizontal vector spacing.
     * @param hy vertical vector spacing.
     * @param nx number of horizontal nodes.
     * @param ny number of vertical nodes.
     * @param topZ position of first (top) scan plane.
     * @param header creates a tecplot header if true.
     * @return An array that contains the names of the generated files.
     */
    public static String[] createReferenceData(String outputFileName, double vx, double vy, double vz, double a, double b, double c, double dp, double w, int nsp, double dsp, double scaleX, double scaleY, int x0, int y0, int hx, int hy, int nx, int ny, double topZ, boolean header) {
        String[] files = new String[nsp];
        double p = topZ;
        double dzo;
        int xe = x0 + (nx - 1) * hx;
        int ye = y0 + (ny - 1) * hy;
        double[][] theData = new double[nx * ny][5];
        int r = 0;
        java.text.DecimalFormat format = jpiv2.FileHandling.getCounterFormat(nsp);
        String baseName = jpiv2.FileHandling.stripExtension(outputFileName);
        for (int sp = 0; sp < nsp; sp++) {
            for (int y = y0; y <= ye; y += hy) {
                for (int x = x0; x <= xe; x += hx) {
                    double xo = (x - xe / 2 - x0 / 2) / scaleX * (Math.cos(b) * Math.cos(c) - Math.cos(a) * Math.sin(b) * Math.sin(c)) + (y - ye / 2 - y0 / 2) / scaleX * (-Math.cos(b) * Math.sin(c) - Math.cos(a) * Math.sin(b) * Math.cos(c)) + p * (Math.sin(a) * Math.sin(b));
                    double yo = (x - xe / 2 - x0 / 2) / scaleX * (Math.sin(b) * Math.cos(c) + Math.cos(a) * Math.cos(b) * Math.sin(c)) + (y - ye / 2 - y0 / 2) / scaleX * (-Math.sin(b) * Math.sin(c) + Math.cos(a) * Math.cos(b) * Math.cos(c)) + p * (-Math.sin(a) * Math.cos(b));
                    dzo = -dp / ((vx * w / 2) * (vx * w / 2)) * (xo * xo + yo * yo) + dp;
                    if (dzo < 0) dzo = 0;
                    theData[r][0] = x;
                    theData[r][1] = y;
                    theData[r][2] = dzo * Math.sin(a) * Math.sin(c) * scaleX;
                    theData[r][3] = dzo * Math.sin(a) * Math.cos(c) * scaleX;
                    theData[r][4] = dzo * Math.cos(a) * scaleX;
                    r++;
                }
            }
            new jpiv2.PivData(theData).writeDataToFile(baseName + "ref_" + format.format(sp), header);
            files[sp] = baseName + "ref_" + format.format(sp) + ".jvc";
            p += dsp;
            r = 0;
        }
        return files;
    }
}
