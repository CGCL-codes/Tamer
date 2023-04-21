package com.matthewtavares.jipt.processing;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.matthewtavares.jipt.util.ImageCreator;
import com.matthewtavares.jipt.util.PixelExtractor;

/**
 * 
 * @author Matt Tavares - mattTavares@gmail.com
 * 
 *         A class that performs Wavelet Transformations at multiple levels
 * 
 */
public class FastWaveletTransformer extends ImageOperator {

    /** Number of wavelets and inverse wavelets to compute and show */
    private static int WAVELET_LEVELS = 1;

    /**
	 * Default Constructor
	 */
    public FastWaveletTransformer() {
    }

    ;

    /**
	 * Decomposes an image using the fast wavelet transform. Shows the wavelet
	 * decomposition and the inverse wavelet.
	 * 
	 * (See ImageOperator transform)
	 */
    public Image transform(Image image) {
        int[][][] imgData = PixelExtractor.getPixelData(image);
        int numImgRows = imgData.length;
        int numImgCols = imgData[0].length;
        double[][] haar = new double[2][2];
        haar[0][0] = 1 / Math.sqrt(2.0);
        haar[0][1] = 1 / Math.sqrt(2.0);
        haar[1][0] = 1 / Math.sqrt(2.0);
        haar[1][1] = -1 / Math.sqrt(2.0);
        double[] scaleVector = { haar[0][1], haar[0][0] };
        double[] waveletVector = { haar[1][1], haar[1][0] };
        double[] inverseScaleVector = { haar[0][0], haar[0][1] };
        double[] inverseWaveletVector = { haar[1][0], haar[1][1] };
        double[][][] approximationData = toDouble(imgData);
        int inversePlacementHorizontal = imgData[0].length + 50;
        int placementVertical = 0;
        int imgDataOneDim[] = PixelExtractor.toOneDImg(imgData);
        Image originalImage = new Container().createImage(new MemoryImageSource(numImgCols, numImgRows, imgDataOneDim, 0, numImgCols));
        JPanel originalJPanel = new JPanel();
        originalJPanel.add(new JLabel(new ImageIcon(originalImage)));
        JPanel[][] jpanelArray = new JPanel[WAVELET_LEVELS][4];
        for (int i = 0; i < WAVELET_LEVELS; i++) {
            double[][][] waveletConvolution1 = doWaveletConvolutionRows(approximationData, waveletVector);
            double[][][] scaleConvolution1 = doWaveletConvolutionRows(approximationData, scaleVector);
            double[][][] waveletDownSample1 = performWaveletDownsampleCols(waveletConvolution1);
            double[][][] scaleDownSample1 = performWaveletDownsampleCols(scaleConvolution1);
            double[][][] waveletConvolution2 = performWaveletConvolutionCols(waveletDownSample1, waveletVector);
            double[][][] scaleConvolution2 = performWaveletConvolutionCols(waveletDownSample1, scaleVector);
            double[][][] waveletConvolution3 = performWaveletConvolutionCols(scaleDownSample1, waveletVector);
            double[][][] scaleConvolution3 = performWaveletConvolutionCols(scaleDownSample1, scaleVector);
            double[][][] waveletDownSample2 = performWaveletDownsampleRows(waveletConvolution2);
            double[][][] scaleDownSample2 = performWaveletDownsampleRows(scaleConvolution2);
            double[][][] waveletDownSample3 = performWaveletDownsampleRows(waveletConvolution3);
            double[][][] scaleDownSample3 = performWaveletDownsampleRows(scaleConvolution3);
            approximationData = scaleDownSample3;
            int[][][] intData = boundImgData(toInt(scaleColorData(waveletDownSample2)));
            int numRows = intData.length;
            int numCols = intData[0].length;
            int imgDataOneD[] = PixelExtractor.toOneDImg(intData);
            Image imageLR = new Container().createImage(new MemoryImageSource(numCols, numRows, imgDataOneD, 0, numCols));
            intData = boundImgData(toInt(scaleColorData(scaleDownSample2)));
            imgDataOneD = PixelExtractor.toOneDImg(intData);
            Image imageLL = new Container().createImage(new MemoryImageSource(numCols, numRows, imgDataOneD, 0, numCols));
            intData = boundImgData(toInt(scaleColorData(waveletDownSample3)));
            imgDataOneD = PixelExtractor.toOneDImg(intData);
            Image imageUR = new Container().createImage(new MemoryImageSource(numCols, numRows, imgDataOneD, 0, numCols));
            intData = boundImgData(toInt(scaleColorData(scaleDownSample3)));
            imgDataOneD = PixelExtractor.toOneDImg(intData);
            Image imageUL = new Container().createImage(new MemoryImageSource(numCols, numRows, imgDataOneD, 0, numCols));
            JPanel[] tempPanels = getImageQuads(imageUL, imageUR, imageLL, imageLR);
            jpanelArray[i] = tempPanels;
            displayImageQuads(jpanelArray, "Wavelet Transform " + (i + 1), i + 1, placementVertical, 0, intData.length + 20, intData[0].length + 20);
            double[][][] upSample1 = upSampleRows(waveletDownSample2);
            double[][][] upSample2 = upSampleRows(scaleDownSample2);
            double[][][] upSample3 = upSampleRows(waveletDownSample3);
            double[][][] upSample4 = upSampleRows(scaleDownSample3);
            double[][][] waveletConvolution4 = performWaveletConvolutionCols(upSample1, inverseWaveletVector);
            double[][][] scaleConvolution4 = performWaveletConvolutionCols(upSample2, inverseScaleVector);
            double[][][] waveletConvolution5 = performWaveletConvolutionCols(upSample3, inverseWaveletVector);
            double[][][] scaleConvolution5 = performWaveletConvolutionCols(upSample4, inverseScaleVector);
            double[][][] waveletAddition1 = addImageData(waveletConvolution4, scaleConvolution4);
            double[][][] waveletAddition2 = addImageData(waveletConvolution5, scaleConvolution5);
            double[][][] upSample5 = upSampleCols(waveletAddition1);
            double[][][] upSample6 = upSampleCols(waveletAddition2);
            double[][][] waveletConvolution6 = doWaveletConvolutionRows(upSample5, inverseWaveletVector);
            double[][][] scaleConvolution6 = doWaveletConvolutionRows(upSample6, inverseScaleVector);
            double[][][] waveletAddition3 = addImageData(waveletConvolution6, scaleConvolution6);
            numRows = waveletAddition3.length;
            numCols = waveletAddition3[0].length;
            intData = toInt(waveletAddition3);
            imgDataOneD = PixelExtractor.toOneDImg(intData);
            Image reconstructedImage = new Container().createImage(new MemoryImageSource(numCols, numRows, imgDataOneD, 0, numCols));
            ImageCreator.displayImage(reconstructedImage, "Inverse Wavelet Transform" + (i + 1), placementVertical, inversePlacementHorizontal);
            placementVertical += intData.length + 50;
        }
        return image;
    }

    /**
	 * Upsample by increasing the number of rows by 2 times. Simply put black
	 * pixels in the new pixel slots.
	 * 
	 * @param imgData
	 *            The image data to up-sample.
	 * 
	 * @return Image data twice the size in row height.
	 */
    private double[][][] upSampleRows(double[][][] imgData) {
        int numRows = imgData.length;
        int numCols = imgData[0].length;
        double[][][] upSampledData = new double[numRows * 2][numCols][4];
        for (int row = 0; row < numRows * 2; row++) {
            for (int col = 0; col < numCols; col++) {
                for (int colorIndex = 0; colorIndex < 4; colorIndex++) {
                    if (row % 2 != 0) {
                        upSampledData[row][col][colorIndex] = 0;
                    } else {
                        upSampledData[row][col][colorIndex] = imgData[row / 2][col][colorIndex];
                    }
                }
            }
        }
        return upSampledData;
    }

    /**
	 * Upsample by increasing the number of columns by 2 times. Simply put black
	 * pixels in the new pixel slots.
	 * 
	 * @param imgData
	 *            The image data to up-sample.
	 * 
	 * @return Image data twice the size in column width.
	 */
    private double[][][] upSampleCols(double[][][] imgData) {
        int numRows = imgData.length;
        int numCols = imgData[0].length;
        double[][][] upSampledData = new double[numRows][numCols * 2][4];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols * 2; col++) {
                for (int colorIndex = 0; colorIndex < 4; colorIndex++) {
                    if (col % 2 == 0) {
                        upSampledData[row][col][colorIndex] = imgData[row][col / 2][colorIndex];
                    } else {
                        upSampledData[row][col][colorIndex] = 0;
                    }
                }
            }
        }
        return upSampledData;
    }

    /**
	 * Convolves image pixel data along its rows with a wavelet vector
	 * 
	 * @param imgData
	 *            The image data to convolve with
	 * @param waveletVector
	 *            The wavelet vector to convolve with
	 * 
	 * @return The result of the convolution
	 */
    private double[][][] doWaveletConvolutionRows(double[][][] imgData, double[] waveletVector) {
        int numRows = imgData.length;
        int numCols = imgData[0].length;
        double[][][] convolution = new double[numRows][numCols + 1][4];
        for (int row = 0; row < numRows; row++) {
            double[][] currentRow = imgData[row];
            for (int colorIndex = 1; colorIndex < 4; colorIndex++) {
                double[] intensities = new double[numCols];
                for (int col = 0; col < numCols; col++) {
                    intensities[col] = currentRow[col][colorIndex];
                }
                double[] rowConvolution = convolveArrays(intensities, waveletVector);
                for (int i = 0; i < rowConvolution.length; i++) {
                    convolution[row][i][colorIndex] = rowConvolution[i];
                }
            }
        }
        return convolution;
    }

    /**
	 * Convolves image pixel data along its columns with a wavelet vector
	 * 
	 * @param imgData
	 *            The image data to convolve with
	 * @param waveletVector
	 *            The wavelet vector to convolve with
	 * 
	 * @return The result of the convolution
	 */
    private double[][][] performWaveletConvolutionCols(double[][][] imgData, double[] waveletVector) {
        int numRows = imgData.length;
        int numCols = imgData[0].length;
        double[][][] convolution = new double[numRows + 1][numCols][4];
        for (int col = 0; col < numCols; col++) {
            double[][] currentCol = new double[numRows][4];
            for (int row = 0; row < numRows; row++) {
                currentCol[row] = imgData[row][col];
            }
            for (int colorIndex = 1; colorIndex < 4; colorIndex++) {
                double[] intensities = new double[numRows];
                for (int i = 0; i < numRows; i++) {
                    intensities[i] = currentCol[i][colorIndex];
                }
                double[] colConvolution = convolveArrays(intensities, waveletVector);
                for (int j = 0; j < colConvolution.length; j++) {
                    convolution[j][col][colorIndex] = colConvolution[j];
                }
            }
        }
        return convolution;
    }

    /**
	 * Downsample by removing every other column in the image data
	 * 
	 * @param imgData
	 *            The image data to downsample
	 * 
	 * @return The downsampled image data (every other column removed)
	 */
    private static double[][][] performWaveletDownsampleCols(double[][][] imgData) {
        int numRows = imgData.length;
        int numCols = imgData[0].length / 2;
        double[][][] downSample = new double[numRows][numCols][4];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                for (int colorIndex = 1; colorIndex < 4; colorIndex++) {
                    downSample[row][col][colorIndex] = imgData[row][2 * col + 1][colorIndex];
                }
            }
        }
        return downSample;
    }

    /**
	 * Downsample by removing every other row in the image data
	 * 
	 * @param imgData
	 *            The image data to downsample
	 * 
	 * @return The downsampled image data (every other row removed)
	 */
    private static double[][][] performWaveletDownsampleRows(double[][][] imgData) {
        int numRows = imgData.length / 2;
        int numCols = imgData[0].length;
        double[][][] downSample = new double[numRows][numCols][4];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                for (int colorIndex = 1; colorIndex < 4; colorIndex++) {
                    downSample[row][col][colorIndex] = imgData[2 * row + 1][col][colorIndex];
                }
            }
        }
        return downSample;
    }

    /**
	 * Convolves two arrays together. Reverses the second array and multiplies
	 * each element in the first array, summing the multiplications of each
	 * first array index with each second array element.
	 * 
	 * @param array1
	 *            The first array to convolve
	 * @param array2
	 *            The second array to convolve
	 * 
	 * @return The result of the convolution
	 */
    private static double[] convolveArrays(double[] array1, double[] array2) {
        int length1 = array1.length;
        double[] padded1 = new double[length1 + 2];
        int length2 = array2.length;
        double[] flipped2 = new double[length2];
        for (int i = 0; i < length2; i++) {
            flipped2[i] = array2[length2 - 1 - i];
        }
        padded1[0] = 0;
        padded1[length1 + 1] = 0;
        for (int i = 1; i < length1 + 1; i++) {
            padded1[i] = array1[i - 1];
        }
        double convolved[] = new double[length1 + 1];
        for (int i = 0; i < length1 + 1; i++) {
            for (int j = 0; j < length2; j++) {
                convolved[i] += padded1[i + j] * flipped2[j];
            }
        }
        return convolved;
    }

    /**
	 * Gets a JPanel with 4 images
	 * 
	 * @param upperLeftImage Upper left image
	 * @param upperRightImage Upper right image
	 * @param lowerLeftImage Lower left image
	 * @param lowerRightImage Lower right image
	 * 
	 * @return A JPanel with 4 images
	 */
    protected JPanel[] getImageQuads(Image upperLeftImage, Image upperRightImage, Image lowerLeftImage, Image lowerRightImage) {
        JPanel[] jPanelArray = new JPanel[4];
        jPanelArray[0] = new JPanel();
        jPanelArray[0].add(new JLabel(new ImageIcon(upperLeftImage)));
        jPanelArray[1] = new JPanel();
        jPanelArray[1].add(new JLabel(new ImageIcon(upperRightImage)));
        jPanelArray[2] = new JPanel();
        jPanelArray[2].add(new JLabel(new ImageIcon(lowerLeftImage)));
        jPanelArray[3] = new JPanel();
        jPanelArray[3].add(new JLabel(new ImageIcon(lowerRightImage)));
        return jPanelArray;
    }

    /**
	 * Displays a JFrame with 4 JPanels in it
	 * 
	 * @param upperLeftPanel
	 * @param upperRightPanel
	 * @param lowerLeftPanel
	 * @param lowerRightPanel
	 * @param frameTitle
	 * @param xLoc
	 *            X screen position
	 * @param yLoc
	 *            Y screen position
	 */
    protected void displayImagePanelQuads(JPanel upperLeftPanel, JPanel upperRightPanel, JPanel lowerLeftPanel, JPanel lowerRightPanel, String frameTitle, int xLoc, int yLoc) {
        JFrame imageFrame = new JFrame(frameTitle);
        JPanel imagePanel = new JPanel();
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(2);
        gridLayout.setColumns(2);
        gridLayout.setHgap(2);
        gridLayout.setVgap(2);
        imagePanel.setLayout(gridLayout);
        imagePanel.add(upperLeftPanel);
        imagePanel.add(upperRightPanel);
        imagePanel.add(lowerLeftPanel);
        imagePanel.add(lowerRightPanel);
        imageFrame.add(imagePanel);
        imageFrame.setLocation(yLoc, xLoc);
        imageFrame.pack();
        imageFrame.setVisible(true);
    }

    /**
	 * Displays a JFrame with 4 JPanels in it
	 * 
	 * @param upperLeftPanel
	 * @param upperRightPanel
	 * @param lowerLeftPanel
	 * @param lowerRightPanel
	 * @param frameTitle
	 * @param xLoc
	 *            X screen position
	 * @param yLoc
	 *            Y screen position
	 */
    protected void displayImageQuads(JPanel[][] jpanelArray, String frameTitle, int iterations, int col, int row, int height, int width) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int x = 0;
        int y = 0;
        int gh = 1;
        int gw = 1;
        for (int i = iterations - 1; i >= 0; i--) {
            c.gridheight = gh;
            c.gridwidth = gw;
            if (i == iterations - 1) {
                for (int j = 0; j < 4; j++) {
                    c.gridx = x;
                    c.gridy = y;
                    panel.add(jpanelArray[i][j], c);
                    if (j == 0) {
                        x++;
                    } else if (j == 1) {
                        x--;
                        y++;
                    } else if (j == 2) {
                        x++;
                    }
                }
                gw *= 6;
                gh *= 6;
            } else {
                for (int j = 1; j < 4; j++) {
                    if (j == 1) {
                        x = 2;
                        y = 0;
                    }
                    if (j == 2) {
                        x = 0;
                        y = 3;
                    } else if (j == 3) {
                        x += 1;
                    }
                    c.gridx = x;
                    c.gridy = y;
                    panel.add(jpanelArray[i][j], c);
                }
            }
            gw *= 2;
            gh *= 2;
        }
        JFrame frame = new JFrame(frameTitle);
        frame.add(panel);
        frame.setLocation(row, col);
        frame.pack();
        frame.setVisible(true);
    }
}
