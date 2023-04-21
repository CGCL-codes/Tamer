package nij.qrfrp.experimental;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.imageio.ImageIO;
import nij.qrfrp.util.ImageUtil;

/**
 * Process a Hilditch-thinned image; take every X pixels along each ridge contour;
 * 
 * 
 * <p>This software is distributed under the GNU General Public License, version 3.0.
 * A copy of the license should have been distributed along with the code. If not, 
 * please visit http://www.fsf.org to view the terms of the license.</p>
 * 
 * @author jmetzger 
 */
public class RidgeThinner {

    public static final int DEFAULT_PIXEL_SPACING = 5;

    BufferedImage inputImage = null;

    int[][] processedPixels = null;

    BufferedImage outputImage = null;

    int pixelSpacing = DEFAULT_PIXEL_SPACING;

    public RidgeThinner(BufferedImage inputImage, int pixelSpacing) {
        this.processedPixels = new int[inputImage.getWidth()][inputImage.getHeight()];
        for (int i = 0; i < inputImage.getHeight(); i++) {
            for (int j = 0; j < inputImage.getWidth(); j++) {
                processedPixels[j][i] = 0;
            }
        }
        this.inputImage = inputImage;
        this.outputImage = ImageUtil.createCopy(inputImage);
        this.pixelSpacing = pixelSpacing;
    }

    public BufferedImage execute() {
        Raster inputRaster = inputImage.getData();
        WritableRaster outputWRaster = outputImage.getRaster();
        for (int i = 0; i < inputImage.getHeight(); i++) {
            for (int j = 0; j < inputImage.getWidth(); j++) {
                int sample = inputRaster.getSample(j, i, 0);
                if (sample > 0 && processedPixels[j][i] != 1) {
                    int ridgePixelCounter = 0;
                    Stack<Point> points = new Stack<Point>();
                    points.add(new Point(j, i));
                    while (!points.isEmpty()) {
                        Point temp = points.pop();
                        int tempX = temp.x;
                        int tempY = temp.y;
                        processedPixels[tempX][tempY] = 1;
                        if (ridgePixelCounter % pixelSpacing != 0) {
                            outputWRaster.setSample(tempX, tempY, 0, 0);
                        }
                        ridgePixelCounter++;
                        for (int k = tempY - 1; k <= tempY + 1; k++) {
                            for (int m = tempX - 1; m <= tempX + 1; m++) {
                                try {
                                    if (inputRaster.getSample(m, k, 0) > 0 && processedPixels[m][k] == 0) {
                                        points.push(new Point(m, k));
                                    }
                                } catch (Exception ee) {
                                }
                            }
                        }
                    }
                }
            }
        }
        return outputImage;
    }

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        BufferedImage inputImage = ImageIO.read(new File("./test/thinnedImage.bmp"));
        RidgeThinner ridgeThinner = new RidgeThinner(inputImage, 8);
        long start = System.currentTimeMillis();
        BufferedImage outputImage = ridgeThinner.execute();
        long end = System.currentTimeMillis();
        double diff = (double) ((end - start) / 1000.0);
        System.out.println("duration = " + diff);
        ImageIO.write(outputImage, "bmp", new File("./test/hilditchPlus.bmp"));
        int thinCounter = 0;
        int mthinCounter = 0;
        Raster in = inputImage.getData();
        Raster out = outputImage.getData();
        System.out.println("Calculating point quantities.");
        for (int i = 0; i < inputImage.getHeight(); i++) {
            for (int j = 0; j < inputImage.getWidth(); j++) {
                if (in.getSample(j, i, 0) > 0) thinCounter++;
                if (out.getSample(j, i, 0) > 0) mthinCounter++;
            }
        }
        System.out.println("Edge points in thinned image:" + thinCounter);
        System.out.println("Edge points in addtionally thinned image:" + mthinCounter);
    }
}
