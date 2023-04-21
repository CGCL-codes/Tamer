package preprocessing.newGUI;

import game.trainers.cmaes.PrintfFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @author Petr Galik
 *         <p/>
 *         This class is spectrogram visualization. Spcrogram visualization is created by putting
 *         time on X axis, frequency on Y axis and for all their values there is power of given
 *         in specific time and frequency.
 *         Class is an JPanel extension and rescaling is performed, when resizing is done.
 *         It has got its own data buffer.
 */
class SpectrogramPanel extends JPanel {

    private Image scaledSpectrogram = null;

    private BufferedImage spectrogram = null;

    private BufferedImage spectrum = null;

    private float zoom = 1.0f;

    private float vzoom = 1.0f;

    double minIntensity;

    double scaleFactor;

    private double[][] data;

    private int width;

    private int height;

    private ColorMap cmap = ColorMap.getJet(64);

    private double minVal;

    private double maxVal;

    int topMargin = 10;

    int rightMargin = 10;

    int bottomMargin = 10;

    int leftMargin = 10;

    int axisWidth = 70;

    int axisHeight = 25;

    int spectrumWidth = 40;

    int gapBetwenSpectrogramAndSpectrum = 10;

    int currentWindowIndex = 0;

    double maxFrequency = 0.0;

    /**
     * Default constructor initializes internal properties.
     */
    public SpectrogramPanel() {
        data = null;
        width = 0;
        height = 0;
    }

    /**
     * Constructor specifing all needed information for spectrogram to be drawn.
     *
     * @param data               2D double array representing levels of intensity. First index is time and second frequency.
     * @param currentWindowIndex In order to label current window in spectrogram it is necessary to know its index within data parameter.
     * @param maxFrequency       Specifies maximal frequency by which Y axis will be seared.
     */
    public SpectrogramPanel(double[][] data, int currentWindowIndex, double maxFrequency) {
        this.currentWindowIndex = currentWindowIndex;
        insertData(data, currentWindowIndex, maxFrequency);
    }

    /**
     * Updates component with new data. New spectrogram will be drawn according to new provided parameters.
     *
     * @param data               2D double array representing levels of intensity. First index is time and second frequency.
     * @param currentWindowIndex In order to label current window in spectrogram it is necessary to know its index within data parameter.
     * @param maxFrequency       Specifies maximal frequency by which Y axis will be seared.
     */
    public void insertData(double[][] data, int currentWindowIndex, double maxFrequency) {
        this.data = data;
        width = data.length;
        height = data[0].length;
        this.currentWindowIndex = currentWindowIndex;
        this.maxFrequency = maxFrequency;
        computeSpectrogram();
    }

    /**
     * Coumputes spectrogram from provided informations and stores result into spectrogram property.
     * Spectrogram property has the same index range as input data and thus can be later scaled.
     */
    private void computeSpectrogram() {
        try {
            maxVal = Integer.MIN_VALUE;
            minVal = Integer.MAX_VALUE;
            for (int x = 0; x < data.length; x++) {
                for (int y = 0; y < data[0].length; y++) {
                    if (data[x][y] > maxVal) maxVal = data[x][y];
                    if (data[x][y] < minVal) minVal = data[x][y];
                }
            }
            minIntensity = Math.abs(minVal);
            double maxIntensity = maxVal + minIntensity;
            int maxYIndex = height - 1;
            Dimension d = new Dimension(width, height);
            spectrogram = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            scaleFactor = (0x3f / maxIntensity);
            for (int i = 0; i < data.length; i++) {
                for (int j = maxYIndex; j >= 0; j--) {
                    int itemColor = (int) ((data[i][j] + minIntensity) * scaleFactor);
                    spectrogram.setRGB(i, maxYIndex - j, cmap.getColor(itemColor));
                }
            }
            zoom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets vertical zoom property.
     *
     * @param vzoom vertical zoom
     */
    protected void vzoomSet(float vzoom) {
        this.vzoom = vzoom;
        zoom();
    }

    /**
     * Sets horizontal zoom property.
     *
     * @param zoom horizontal zoom
     */
    protected void hzoomSet(float zoom) {
        zoomSet(zoom);
    }

    /**
     * Sets horizontal zoom property and performs zooming.
     *
     * @param zoom horizontal zoom
     */
    protected void zoomSet(float zoom) {
        this.zoom = zoom;
        zoom();
    }

    /**
     * Calculates scaledSpectrogram according to setting of vzoom and zoom properties.
     */
    void zoom() {
        if (spectrogram != null) {
            int width = spectrogram.getWidth();
            int height = spectrogram.getHeight();
            width = (int) (zoom * width);
            height = (int) (vzoom * height);
            ImageFilter scaleFilter = new ReplicateScaleFilter(width, height);
            scaledSpectrogram = createImage(new FilteredImageSource(spectrogram.getSource(), scaleFilter));
            revalidate();
            repaint();
        }
    }

    /**
     * Returns vertical zoom.
     *
     * @return vertical zoom
     */
    public float getVZoom() {
        return vzoom;
    }

    /**
     * Returns horizontal zoom.
     *
     * @return horizontal zoom
     */
    public float getHZoom() {
        return zoom;
    }

    /**
     * Returns data on index [x][y].
     *
     * @param x first array index
     * @param y second array index
     * @return data on index [x][y]
     */
    public double getData(int x, int y) {
        return data[x][y];
    }

    /**
     * Clears spectrogram. Should be called when displayed data are no longer valid.
     */
    public void clear() {
        data = null;
        width = 0;
        height = 0;
        spectrogram = null;
    }

    /**
     * Paints spectrogram, spectrum, X axis, Y axis description and lines labelling current window.
     *
     * @param g into this object the resulting image will be stored
     */
    @Override
    public void paint(Graphics g) {
        Dimension sz = getSize();
        vzoomSet((float) (sz.getHeight() - topMargin - bottomMargin - axisHeight) / height);
        hzoomSet((float) (sz.getWidth() - leftMargin - axisWidth - rightMargin - spectrumWidth - gapBetwenSpectrogramAndSpectrum) / width);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, sz.width, sz.height);
        drawYAxis(g, sz.getHeight() - topMargin - bottomMargin - axisHeight, (int) sz.getHeight() - bottomMargin - axisHeight, (int) (axisWidth * 0.8));
        drawXDescription(g, sz.getWidth() - leftMargin - axisWidth - rightMargin - spectrumWidth - gapBetwenSpectrogramAndSpectrum, leftMargin + axisWidth, (int) (sz.getHeight() - axisHeight * 0.3 - bottomMargin));
        drawSpectrum((int) sz.getHeight() - topMargin - bottomMargin - axisHeight);
        if (spectrogram != null) {
            g.drawImage(scaledSpectrogram, leftMargin + axisWidth, topMargin, (ImageObserver) null);
            g.setColor(Color.BLACK);
            g.drawLine(leftMargin + axisWidth + (int) (zoom * width * (double) currentWindowIndex / data.length), topMargin, leftMargin + axisWidth + (int) (zoom * width * (double) currentWindowIndex / data.length), (int) (topMargin + Math.ceil(height * vzoom)) - 1);
            g.drawLine(leftMargin + axisWidth + (int) (zoom * width * (double) (currentWindowIndex + 1) / data.length) - 1, topMargin, leftMargin + axisWidth + (int) (zoom * width * (double) (currentWindowIndex + 1) / data.length) - 1, (int) (topMargin + Math.ceil(height * vzoom)) - 1);
        }
        if (spectrum != null) {
            g.drawImage(spectrum, sz.width - rightMargin - spectrumWidth, topMargin, (ImageObserver) null);
        }
    }

    /**
     * Draws spectrum into spectrum property with specified height.
     *
     * @param height specifies height of spectrum property without scaling
     */
    void drawSpectrum(int height) {
        spectrum = new BufferedImage(spectrumWidth, height, BufferedImage.TYPE_INT_RGB);
        for (int j = height; j > 0; j--) for (int i = 0; i < spectrum.getWidth(); i++) spectrum.setRGB(i, height - j, cmap.getColor((int) Math.ceil(63.0 * (double) j / (double) height)));
    }

    /**
     * Draws Y axis into g while height, starting Y position and x origin are specified.
     *
     * @param g              here will drawing take place
     * @param height         in pixels of axis
     * @param startY         offset on y axis
     * @param image_x_origin specifies x origin
     */
    protected void drawYAxis(Graphics g, double height, int startY, int image_x_origin) {
        double ymin = 0;
        double ymax = maxFrequency;
        g.setColor(Color.BLACK);
        double yRange = ymax - ymin;
        if (Double.isNaN(yRange)) yRange = 0;
        g.drawString("f [Hz]", (int) (image_x_origin - axisWidth * 0.6), startY - (int) height / 2);
        g.drawLine(image_x_origin, startY, image_x_origin, startY - (int) height);
        if (yRange == 0) return;
        int unitOrder = (int) Math.floor(Math.log(yRange / 5) / Math.log(10));
        double unitDistance = Math.pow(10, unitOrder);
        double image_unitDistance = unitDistance / yRange * height;
        if (image_unitDistance < 20) {
            unitDistance *= 5;
        } else if (image_unitDistance < 50) {
            unitDistance *= 2;
        }
        double unitStart = ymin;
        double modulo = ymin % unitDistance;
        if (modulo != 0) {
            if (modulo > 0) unitStart += unitDistance - modulo; else unitStart += Math.abs(modulo);
        }
        PrintfFormat labelFormat;
        if (unitOrder > 0) {
            labelFormat = new PrintfFormat("%.0f");
        } else {
            labelFormat = new PrintfFormat("%." + (-unitOrder) + "f");
        }
        for (double i = unitStart; i <= ymax; i += unitDistance) {
            double yunit = (i - ymin) / yRange * height;
            g.drawLine(image_x_origin + 5, startY - (int) yunit, image_x_origin - 5, startY - (int) yunit);
            g.drawString(labelFormat.sprintf(i), image_x_origin - 30, startY - (int) yunit + 5);
        }
    }

    /**
     * Draws x axis description label "t [s]".
     *
     * @param g              here will drawing take place
     * @param width          width of axis in pixels
     * @param startX         offset on x axis
     * @param image_y_origin specifies y origin
     */
    protected void drawXDescription(Graphics g, double width, int startX, int image_y_origin) {
        g.setColor(Color.BLACK);
        g.drawString("t [s]", (int) (startX + width / 2 - 10), image_y_origin);
    }
}
