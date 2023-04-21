package nij.qrfrp.experimental.gabor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import nij.qrfrp.util.ImageUtil;
import nij.qrfrp.util.MathUtil;

/**
 * <p>Class that calculates the ridge frequency for a single block of fingerprint data.  
 * First, the average orientation of the block is determined.  This 
 * average orientation is used to rotate the block of fingerprint data such
 * that the ridges and valleys run vertically, and the image is cropped to 
 * remove any invalid data introduced by the rotation.  The pixel intensities 
 * are then summed down each column.  The summed data is dilated to detect 
 * peaks, which are used to calculate frequency. </p>
 * 
 * <p>NOTE: While the "peaks" detected are actually the valleys, they prove 
 * to be an accurate measure of ridge frequency.  There are instances where 
 * the middle of the ridges contain many pores (and therefore high intensities),
 * which would throw off a "ridge-based" approach.  This could be tested in 
 * the future.</p>   
 * 
 * <p> Implementation is based upon the code/approach of Peter Kovesi (see 
 * <a href="http://www.csse.uwa.edu.au/~pk/research/matlabfns/FingerPrints/Docs/index.html" >
 * "Example of fingerprint enhancement"</a>).  A similar approach is presented by 
 * L. Hong, Y. Wan, A. Jain (see <a href="http://www.cse.msu.edu/publications/tech/TR/MSU-CPS-97-35.ps.gz">
 * "Fingerprint Image Enhancement: Algorithm and Performance Evaluation"</a>).</p>
 * 
 * <p>This software is distributed under the GNU General Public License, version 3.0.
 * A copy of the license should have been distributed along with the code. If not, 
 * please visit http://www.fsf.org to view the terms of the license.</p>
 * 
 * @author jmetzger 
 */
public class BlockFrequency {

    private static final boolean DEBUG = false;

    private static final boolean SAVE_BLOCKS = true;

    /**
	 * <p>Performs the frequency calculation.</p>
	 * 
	 * <p>NOTE: The orientation array can be any dimension, but is traditionally
	 * the same size as the input image.  An equally-sized orientation array is 
	 * passed by RidgeFrequency while iterating through the blocks of the
	 * fingerprint image with the orientation at 
	 * <code>orient[x][y]</code> corresponding to the pixel accessed 
	 * as <code>inputImage.getRGB(x,y)</code>.
	 * 
	 * @param inputImage a BufferedImage containing the fingerprint data
	 * @param orient orientation array containing the ridge orientation at each pixel [0-180)
	 * @param windowSize the size of the window over which the summed fingerprint column data is dilated  
	 * @return a double representing the estimated frequency of the block  
	 * @throws IOException if there is a problem writing a diagnostic image
	 * @see nij.qrfrp.experimental.gabor.RidgeFrequency
	 */
    public static double calculate(BufferedImage inputImage, double[][] orient, int windowSize) throws IOException {
        double meanCosOrient = 0;
        double meanSinOrient = 0;
        double totalValidOrientations = 0;
        double blockOrientation = 0;
        int orientWidth = orient.length;
        int orientHeight = orient[0].length;
        for (int i = 0; i < orientHeight; i++) {
            for (int j = 0; j < orientWidth; j++) {
                if (orient[j][i] != -1) {
                    orient[j][i] = Math.toRadians(orient[j][i]);
                    meanCosOrient += Math.cos(2 * orient[j][i]);
                    meanSinOrient += Math.sin(2 * orient[j][i]);
                    totalValidOrientations++;
                }
            }
        }
        if (totalValidOrientations == 0) totalValidOrientations = 1;
        meanCosOrient /= totalValidOrientations;
        meanSinOrient /= totalValidOrientations;
        blockOrientation = Math.toDegrees((Math.atan2(meanSinOrient, meanCosOrient) / 2));
        if (blockOrientation > 180 || blockOrientation < 0) return -1;
        double rotation = 0;
        if (blockOrientation > 90) rotation = -(180 - blockOrientation); else if (blockOrientation < 90) rotation = 90 - blockOrientation;
        if ((int) blockOrientation == 0) rotation = 90;
        rotation = -rotation;
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(rotation), inputImage.getWidth() / 2, inputImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rIm = op.filter(inputImage, null);
        int cropsize = (int) (inputImage.getHeight() / Math.sqrt(2));
        int offset = (int) (((double) inputImage.getHeight() - cropsize) / 2);
        rIm = rIm.getSubimage(offset, offset, cropsize, cropsize);
        Raster rImRaster = rIm.getData();
        BufferedImage subIm = new BufferedImage(rIm.getWidth(), rIm.getHeight(), BufferedImage.TYPE_INT_RGB);
        WritableRaster subImRaster = subIm.getRaster();
        for (int i = 0; i < rIm.getHeight(); i++) {
            for (int j = 0; j < rIm.getWidth(); j++) {
                if (DEBUG) System.out.print((rImRaster.getSample(j, i, 0)) + " ");
                subImRaster.setSample(j, i, 0, rImRaster.getSample(j, i, 0));
                subImRaster.setSample(j, i, 1, rImRaster.getSample(j, i, 0));
                subImRaster.setSample(j, i, 2, rImRaster.getSample(j, i, 0));
            }
            if (DEBUG) System.out.println();
        }
        int[] colSumArray = ImageUtil.sumImageColumns(subIm);
        if (DEBUG) {
            System.out.println("COLUMN SUMS:");
            for (int i = 0; i < colSumArray.length; i++) {
                System.out.print(colSumArray[i] + ", ");
            }
            System.out.println();
        }
        int[] dilation = MathUtil.dilate(colSumArray, windowSize);
        double meanColSumArray = MathUtil.avgIntArray(colSumArray);
        if (DEBUG) System.out.println("Mean=" + meanColSumArray);
        LinkedList<Integer> ll = MathUtil.findPeaks(colSumArray, dilation, meanColSumArray);
        double freq = 0;
        if (ll.size() >= 2) {
            double firstPeak = ll.get(0);
            if (DEBUG) System.out.println("Peak1=" + firstPeak);
            double lastPeak = ll.get(ll.size() - 1);
            if (DEBUG) System.out.println("Peak2=" + lastPeak);
            double noPeaks = ll.size() - 1;
            if (DEBUG) System.out.println("#Peaks=" + noPeaks);
            double wavelength = (lastPeak - firstPeak) / (noPeaks);
            freq = 1 / wavelength;
            if (DEBUG) System.out.println("Freq=" + freq);
        } else {
            freq = 0;
        }
        if (SAVE_BLOCKS) {
            long label = System.currentTimeMillis();
            Graphics g = subIm.getGraphics();
            g.setColor(Color.RED);
            for (int i = 0; i < ll.size(); i++) {
                g.drawLine(ll.get(i), 0, ll.get(i), subIm.getHeight());
            }
            if (DEBUG) System.out.println("Block orientaiton estimated to be: " + blockOrientation + " degrees; block rotated: " + rotation + " degrees.");
            ImageIO.write(inputImage, "bmp", new File(".\\diagnostics\\blocks\\" + label + "original.bmp"));
            ImageIO.write(subIm, "bmp", new File(".\\diagnostics\\blocks\\" + label + "rotated_" + (int) rotation + "_" + (int) blockOrientation + ".bmp"));
        }
        return freq;
    }

    public static void main(String[] args) throws IOException {
    }
}
