package pl.matt.media.filter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class provides a simple way of calculating a histogram for a given
 * image. This class implements <code>Visualizable</code>, for easy viewing
 * and image output.
 * 
 * TODO: There is still a fair amount to be implemented here, as histograms play
 * an extremely important part in image analysis, machine vision due to their
 * statistical importance.
 * 
 * Another possible expansion is to increase the flexibility by allowing a
 * variable number of bins. HSB support would also be a nice addition.
 * 
 * @author James Matthews
 */
public class Histogram implements Visualizable {

    /**
	 * The background colour of the visualized histogram
	 */
    protected Color clrBackground;

    /**
	 * The bins for the three colour bands
	 */
    protected long[][] colourBins;

    /**
	 * The cumulative histogram bins.
	 */
    protected long[][] cumulativeBins;

    /**
	 * Is the image greyscale?
	 */
    protected boolean isGreyscale = false;

    /**
	 * Additional options for drawing.
	 * 
	 * @see #DO_SOLID
	 * @see #DO_LINE
	 */
    protected int drawOptions = DO_SOLID;

    /** The red band */
    public static final int BAND_RED = 0;

    /** The green band */
    public static final int BAND_GREEN = 1;

    /** The blue band */
    public static final int BAND_BLUE = 2;

    /** The grey band */
    public static final int BAND_GREY = BAND_RED;

    /** All the bands */
    public static final int BAND_ALL = 3;

    /** Draw the histogram as a line */
    public static final int DO_LINE = 1000;

    /**
	 * Draw the histograms as a solid shape. The class renders the bands in
	 * order (RGB), therefore the solid shapes may obscure each other.
	 */
    public static final int DO_SOLID = 2000;

    /** Creates a new instance of Histogram */
    public Histogram() {
        clrBackground = Color.WHITE;
    }

    /**
	 * Creates a new instance of Histogram, as well as specifying an image to
	 * calculate a histogram for. The histogram is calculated immediately.
	 * 
	 * @param img
	 *            the image to calculate the histogram for.
	 */
    public Histogram(BufferedImage img) {
        this(img, DO_LINE);
    }

    /**
	 * As <code>Histogram(BufferedImage)</code> with additional drawing
	 * options.
	 * 
	 * @param img
	 *            the image to calculate the histogram for.
	 * @param options
	 *            additional drawing options.
	 * @see #Histogram(BufferedImage)
	 */
    public Histogram(BufferedImage img, int options) {
        create(img);
        drawOptions = options;
    }

    /**
	 * Create the histogram for a given image. The class detects whether the
	 * input image is greyscale or colour, and creates the histogram
	 * accordingly.
	 * 
	 * @param img
	 *            the image to calculate the histogram for.
	 */
    public void create(BufferedImage img) {
        int imgType = img.getType();
        int w = img.getWidth(), h = img.getHeight();
        Raster in_pixels = img.getRaster();
        isGreyscale = (imgType == BufferedImage.TYPE_BYTE_GRAY) ? true : false;
        if (isGreyscale) {
            colourBins = new long[1][256];
            cumulativeBins = new long[1][256];
        } else {
            colourBins = new long[3][256];
            cumulativeBins = new long[3][256];
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (isGreyscale) {
                    colourBins[BAND_GREY][in_pixels.getSample(i, j, BAND_GREY)]++;
                } else {
                    colourBins[BAND_RED][in_pixels.getSample(i, j, BAND_RED)]++;
                    colourBins[BAND_GREEN][in_pixels.getSample(i, j, BAND_GREEN)]++;
                    colourBins[BAND_BLUE][in_pixels.getSample(i, j, BAND_BLUE)]++;
                }
            }
        }
        createCumulative();
    }

    /**
	 * Create the cumulative bins.
	 */
    protected void createCumulative() {
        long cumulativeR = 0;
        long cumulativeG = 0;
        long cumulativeB = 0;
        for (int i = 0; i < 256; i++) {
            cumulativeR += colourBins[0][i];
            cumulativeBins[0][i] = cumulativeR;
            if (!isGreyscale()) {
                cumulativeG += colourBins[1][i];
                cumulativeBins[1][i] = cumulativeG;
                cumulativeB += colourBins[2][i];
                cumulativeBins[2][i] = cumulativeB;
            }
        }
    }

    /**
	 * Has this histogram been created for a greyscale image?
	 * 
	 * @return whether the histogram is greyscale.
	 */
    public boolean isGreyscale() {
        return isGreyscale;
    }

    /**
	 * Set the draw option for the histogram.
	 * 
	 * @param drawOptions
	 *            the draw options.
	 */
    public void setDrawOptions(int drawOptions) {
        this.drawOptions = drawOptions;
    }

    /**
	 * Set the background colour for the visualized histogram.
	 * 
	 * @param bkc
	 *            the colour (default is Color.lightGray)
	 */
    public void setBackgroundColour(Color bkc) {
        clrBackground = bkc;
    }

    /**
	 * Retrieve the current histogram data.
	 * 
	 * @param i
	 *            the index of the colour bin (0-255).
	 * @param b
	 *            the colour band (BAND_RED, BAND_GREEN, BAND_BLUE or
	 *            BAND_GREY).
	 * @return the histogram data for the given bin and band.
	 */
    public long getFrequency(int i, int b) {
        return colourBins[b][i];
    }

    /**
	 * Retrieve the frequency for a given band (band 0). The method is defined
	 * as: <code>
	 *  <b>return</b> getFrequency(i, 0);
	 * </code>
	 * 
	 * @param i
	 *            the grey level index.
	 * @return the frequency at level <i>i</i>.
	 */
    public long getFrequency(int i) {
        return getFrequency(i, 0);
    }

    /**
	 * Return the frequencies for a given band.
	 * 
	 * @param b
	 *            the band (red, green or blue).
	 * @return the frequencies of the band.
	 */
    public long[] getFrequencies(int b) {
        return colourBins[b];
    }

    /**
	 * Retrieve the cumulative histogram data.
	 * 
	 * @param i
	 *            the index of the cumulative bin.
	 * @param b
	 *            the colour band.
	 * @return the cumulative frequency for the index and band.
	 */
    public long getCumulativeFrequency(int i, int b) {
        return cumulativeBins[b][i];
    }

    /**
	 * Return a cumulative frequency at a given index for band 0.
	 * 
	 * @param i
	 *            the index.
	 * @return the cumulative frequency at index <i>i</i>.
	 */
    public long getCumulativeFrequency(int i) {
        return getCumulativeFrequency(i, 0);
    }

    /**
	 * Returns the array of cumulative frequencies for the given colour band.
	 * 
	 * @param b
	 *            the colour band.
	 * @return the cumulative frequencies for the colour band.
	 */
    public long[] getCumulativeFrequencies(int b) {
        return cumulativeBins[b];
    }

    /**
	 * Return the maximum values for each band in the histogram.
	 * 
	 * @return the maximum values.
	 */
    public long[] getMaximum() {
        long max = Long.MIN_VALUE;
        long[] maxs = new long[colourBins.length];
        for (int b = 0; b < colourBins.length; b++) {
            for (int i = 0; i < 256; i++) {
                if (colourBins[b][i] > max) max = colourBins[b][i];
            }
        }
        return maxs;
    }

    /**
	 * This method returns the most frequent bucket for each band in the
	 * histogram. If multiple buckets are equal in frequency, the first is
	 * returned.
	 * 
	 * @return the most frequent buckets for each band.
	 */
    public int[] getMostFrequent() {
        int[] freqs = new int[colourBins.length];
        int position = 0;
        long max = 0;
        for (int b = 0; b < colourBins.length; b++) {
            for (int i = 0; i < 256; i++) {
                if (colourBins[b][i] > max) {
                    position = i;
                    max = colourBins[b][i];
                }
            }
            freqs[b] = position;
        }
        return freqs;
    }

    /**
	 * Returns the mean values for each band in the histogram.
	 * 
	 * @return the mean values.
	 */
    public double[] getMean() {
        long total = 0;
        double[] means = new double[colourBins.length];
        for (int b = 0; b < colourBins.length; b++) {
            for (int i = 0; i < 256; i++) {
                total += colourBins[b][i] * i;
            }
            means[b] = total / cumulativeBins[b][255];
        }
        return means;
    }

    /**
	 * Returns the standard deviations for each band in the histogram.
	 * 
	 * @return the standard deviations.
	 */
    public double[] getStandardDeviation() {
        long mean = 0;
        long total = 0;
        double dt, dm;
        double[] medians = new double[colourBins.length];
        for (int b = 0; b < colourBins.length; b++) {
            mean = total = 0;
            for (int i = 0; i < 256; i++) {
                total += colourBins[b][i] * i * i;
                mean += colourBins[b][i] * i;
            }
            dt = total / (double) cumulativeBins[b][255];
            dm = mean / (double) cumulativeBins[b][255];
            medians[b] = Math.pow((dt - (dm * dm)), 0.5);
        }
        return medians;
    }

    /**
	 * Renders the histogram as a line or group of solids. Note that the
	 * histogram is auto-scaled, using the maximum histogram bin value. If the
	 * render function is to draw the histogram using lines, it simply joins the
	 * values together to form a continuous line. If a solid object is to be
	 * drawn, the render function interpolates the histogram data across the
	 * width of the image, sometimes causing a slight discrepancy.
	 * 
	 * @param gfx
	 *            the graphics context
	 * @param width
	 *            width of the histogram image
	 * @param height
	 *            height of the histogram image
	 */
    public void render(java.awt.Graphics gfx, int width, int height) {
        long[] maximum = getMaximum();
        double dx = width / 255.0;
        double dy = height / (double) Math.max(maximum[0], Math.max(maximum[1], maximum[2]));
        Graphics2D g = (Graphics2D) gfx;
        g.setColor(clrBackground);
        g.fillRect(0, 0, width, height);
        double sx, sy, ex, ey;
        Color[] bandColours = { new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255), Color.BLACK };
        if ((drawOptions & DO_LINE) == DO_LINE) {
            for (int b = BAND_RED; b <= BAND_BLUE; b++) {
                g.setColor(bandColours[((!isGreyscale) ? b : 3)]);
                for (int i = 1; i < 256; i++) {
                    sx = (i - 1) * dx;
                    sy = height - (colourBins[b][i - 1] * dy);
                    ex = i * dx;
                    ey = height - (colourBins[b][i] * dy);
                    g.drawLine((int) Math.round(sx), (int) Math.round(sy), (int) Math.round(ex), (int) Math.round(ey));
                }
                if (isGreyscale) break;
            }
        } else if ((drawOptions & DO_SOLID) == DO_SOLID) {
            for (int b = BAND_RED; b <= BAND_BLUE; b++) {
                g.setColor(bandColours[((!isGreyscale) ? b : 3)]);
                for (int i = 0; i < width; i++) {
                    sx = i;
                    sy = height;
                    ex = sx;
                    ey = height - (colourBins[b][(int) Math.round(i / dx)] * dy);
                    g.drawLine((int) Math.round(sx), (int) Math.round(sy), (int) Math.round(ex), (int) Math.round(ey));
                }
                if (isGreyscale) break;
            }
        }
    }

    /**
	 * Write the visualized histogram to an image file.
	 * 
	 * @param filename
	 *            the filename of the image to write.
	 * @param width
	 *            width of the image.
	 * @param height
	 *            height of the image.
	 */
    public void writeImage(String filename, int width, int height) {
        try {
            pl.matt.media.utils.ImageHelper.writeVisualizedImage(filename, width, height, this);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
	 * Write the given histogram band to a <code>FileWriter</code>.
	 * 
	 * @param freqFile
	 *            the file writer.
	 * @param band
	 *            the band to write.
	 */
    protected void writeBand(File freqFile, int band) {
        try {
            FileWriter writer = new FileWriter(freqFile);
            for (int i = 0; i < 256; i++) writer.write(Long.toString(colourBins[band][i]) + "\r\n");
            writer.flush();
            writer.close();
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }

    /**
	 * Write frequencies for a greyscale histogram.
	 * 
	 * @param freqFile
	 *            the greyscale frequency file.
	 */
    public void writeFrequencies(File freqFile) {
        writeBand(freqFile, 0);
    }

    /**
	 * Write frequencies for an RGB image.
	 * 
	 * @param redFile
	 *            the red frequency file.
	 * @param greenFile
	 *            the green frequency file.
	 * @param blueFile
	 *            the blue frequency file.
	 */
    public void writeFrequencies(File redFile, File greenFile, File blueFile) {
        if (isGreyscale) throw new IllegalArgumentException("image not rgb.");
        writeBand(redFile, 0);
        writeBand(greenFile, 1);
        writeBand(blueFile, 2);
    }

    /**
	 * Write cumulative frequencies for a given colour band.
	 * 
	 * @param cumFreqFile
	 *            the cumulative frequency file.
	 * @param band
	 *            the band.
	 */
    protected void writeCumulativeBand(File cumFreqFile, int band) {
        try {
            FileWriter writer = new FileWriter(cumFreqFile);
            for (int i = 0; i < 256; i++) writer.write(Long.toString(cumulativeBins[band][i]) + "\r\n");
            writer.flush();
            writer.close();
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }

    /**
	 * Write the cumulative frequencies for a greyscale image.
	 * 
	 * @param freqFile
	 *            the greyscale cumulative frequency file.
	 */
    public void writeCumulativeFrequencies(File freqFile) {
        writeCumulativeBand(freqFile, 0);
    }

    /**
	 * Write the cumulative frequencies for an RGB image.
	 * 
	 * @param redFile
	 *            the red cumulative frequency file.
	 * @param greenFile
	 *            the green cumulative frequency file.
	 * @param blueFile
	 *            the blue cumulative frequency file.
	 */
    public void writeCumulativeFrequencies(File redFile, File greenFile, File blueFile) {
        if (isGreyscale) throw new IllegalArgumentException("image not rgb.");
        writeCumulativeBand(redFile, 0);
        writeCumulativeBand(greenFile, 1);
        writeCumulativeBand(blueFile, 2);
    }
}
