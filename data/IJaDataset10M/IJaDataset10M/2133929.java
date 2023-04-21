package maltcms.ui.charts;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;

/**
 * This class extends the {@link XYPlot} to a Plot with a zoomable background
 * image. This class supports only axis with the same lower and upper bounds
 * like the image.
 * 
 * @author Mathias Wilhelm(mwilhelm A T TechFak.Uni-Bielefeld.DE) (mwilhelm@techfak.uni-bielefeld.de)
 */
public class XYBPlot extends XYPlot implements Serializable {

    private static final long serialVersionUID = 1311452000544833279L;

    private transient BufferedImage backgroundImage = null;

    private transient int imageHeight, imageWidth;

    private Double maxUpperXAxisBound, maxUpperYAxisBound;

    private Double minLowerXAxisBound, minLowerYAxisBound;

    private String backgroundImageFilename;

    /**
	 * Default constructor. The dataset and renderer will be initialized with
	 * <code>null</code>. ATTENTION: The dimensions of this chart are read out
	 * from the ranges of the axis.
	 * 
	 * @param backgroundFilename
	 *            full qualified file name of the background image
	 * @param xAxis
	 *            x axis of the plot
	 * @param yAxis
	 *            y axis of the plot
	 * @throws IOException
	 *             Includes any I/O exceptions that may occur
	 */
    public XYBPlot(final String backgroundFilename, final NumberAxis xAxis, final NumberAxis yAxis) throws IOException {
        super(null, xAxis, yAxis, null);
        this.backgroundImageFilename = backgroundFilename;
        this.maxUpperXAxisBound = xAxis.getUpperBound();
        this.maxUpperYAxisBound = yAxis.getUpperBound();
        this.minLowerXAxisBound = xAxis.getLowerBound();
        this.minLowerYAxisBound = yAxis.getLowerBound();
        initBackgroundImage();
        updateBackground();
    }

    /**
	 * Default constructor. The dataset and renderer will be initialized with
	 * <code>null</code>. ATTENTION: The dimensions of this chart are read out
	 * from image.
	 * 
	 * @param backgroundFilename
	 *            full qualified file name of the background image
	 * @param xAxisLabel
	 *            label of the x axis
	 * @param yAxisLabel
	 *            label of the y axis
	 * @throws IOException
	 *             Includes any I/O exceptions that may occur
	 */
    public XYBPlot(final String backgroundFilename, final String xAxisLabel, final String yAxisLabel) throws IOException {
        super(null, new NumberAxis(xAxisLabel), new NumberAxis(yAxisLabel), null);
        this.backgroundImageFilename = backgroundFilename;
        initBackgroundImage();
        this.maxUpperXAxisBound = (double) this.imageWidth;
        this.maxUpperYAxisBound = (double) this.imageHeight;
        this.minLowerXAxisBound = 0d;
        this.minLowerYAxisBound = 0d;
        getDomainAxis().setRange(this.minLowerXAxisBound, this.maxUpperXAxisBound);
        getRangeAxis().setRange(this.minLowerYAxisBound, this.maxUpperYAxisBound);
        updateBackground();
    }

    /**
	 * Reads and initialize the background image.
	 * 
	 * @throws IOException
	 *             Includes any I/O exceptions that may occur
	 */
    private void initBackgroundImage() throws IOException {
        this.backgroundImage = ImageIO.read(new File(this.backgroundImageFilename));
        this.imageHeight = this.backgroundImage.getHeight();
        this.imageWidth = this.backgroundImage.getWidth();
        this.setBackgroundImageAlpha(1.0f);
        this.setBackgroundAlpha(1.0f);
        this.setRangeGridlinePaint(Color.white);
    }

    /**
	 * Maps a point from axis coordinates to image coordinates.
	 * 
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @return point in image coordinates
	 */
    public Point translatePointToImageCoords(final double x, final double y) {
        final double axisXperc = (x - this.minLowerXAxisBound) / (this.maxUpperXAxisBound - this.minLowerXAxisBound);
        final double axisYperc = (y - this.minLowerYAxisBound) / (this.maxUpperYAxisBound - this.minLowerYAxisBound);
        final int newX = (int) (axisXperc * this.imageWidth);
        final int newY = (int) (axisYperc * this.imageHeight);
        return new Point(newX, newY);
    }

    /**
	 * This method will update the background image on the chart.
	 */
    public void updateBackground() {
        checkAxis();
        final Point p1 = translatePointToImageCoords(getDomainAxis().getLowerBound(), getRangeAxis().getLowerBound());
        final Point p2 = translatePointToImageCoords(getDomainAxis().getUpperBound(), getRangeAxis().getUpperBound());
        this.setBackgroundImage(this.backgroundImage.getSubimage((int) (p1.getX()), (int) (this.imageHeight - p2.getY()), (int) (p2.getX() - p1.getX()), (int) (p2.getY() - p1.getY())));
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void zoomDomainAxes(final double factor, final PlotRenderingInfo info, final Point2D source) {
        if (factor == 0.0d) {
            getDomainAxis().setRange(this.minLowerXAxisBound, this.maxUpperXAxisBound);
        } else {
            super.zoomDomainAxes(factor, info, source);
        }
        updateBackground();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void zoomRangeAxes(final double factor, final PlotRenderingInfo info, final Point2D source) {
        if (factor == 0.0d) {
            getRangeAxis().setRange(this.minLowerYAxisBound, this.maxUpperYAxisBound);
        } else {
            super.zoomRangeAxes(factor, info, source);
        }
        updateBackground();
    }

    /**
	 * Checks the X and Y axis to its upper and lower bound. If one is out of
	 * bounds, it will set to the minimum or maximum of the image.
	 */
    private void checkAxis() {
        final ValueAxis x = getDomainAxis();
        final ValueAxis y = getRangeAxis();
        if (x.getUpperBound() > this.maxUpperXAxisBound) {
            x.setRange(x.getLowerBound(), this.maxUpperXAxisBound);
        }
        if (x.getLowerBound() < this.minLowerXAxisBound) {
            x.setRange(this.minLowerXAxisBound, x.getUpperBound());
        }
        if (y.getUpperBound() > this.maxUpperYAxisBound) {
            y.setRange(y.getLowerBound(), this.maxUpperYAxisBound);
        }
        if (y.getLowerBound() < this.minLowerYAxisBound) {
            y.setRange(this.minLowerYAxisBound, y.getUpperBound());
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void zoomDomainAxes(final double factor, final PlotRenderingInfo info, final Point2D source, final boolean useAnchor) {
        super.zoomDomainAxes(factor, info, source, useAnchor);
        updateBackground();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void zoomRangeAxes(final double factor, final PlotRenderingInfo info, final Point2D source, final boolean useAnchor) {
        super.zoomRangeAxes(factor, info, source, useAnchor);
        updateBackground();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void zoomDomainAxes(final double lowerPercent, final double upperPercent, final PlotRenderingInfo info, final Point2D source) {
        super.zoomDomainAxes(lowerPercent, upperPercent, info, source);
        updateBackground();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void zoomRangeAxes(final double lowerPercent, final double upperPercent, final PlotRenderingInfo info, final Point2D source) {
        super.zoomRangeAxes(lowerPercent, upperPercent, info, source);
        updateBackground();
    }

    /**
	 * Getter.
	 * 
	 * @return maximum of x axis
	 */
    public double getMaxX() {
        return this.maxUpperXAxisBound;
    }

    /**
	 * Getter.
	 * 
	 * @return minimum of x axis
	 */
    public double getMinX() {
        return this.minLowerXAxisBound;
    }

    /**
	 * Getter.
	 * 
	 * @return maximum of y axis
	 */
    public double getMaxY() {
        return this.maxUpperYAxisBound;
    }

    /**
	 * Getter.
	 * 
	 * @return minimum of y axis
	 */
    public double getMinY() {
        return this.minLowerYAxisBound;
    }

    /**
	 * Is responsible for the serialization and to store all needed object in a
	 * way, that readObject can find it again.
	 * 
	 * @param out
	 *            output stream
	 * @throws IOException
	 *             Includes any I/O exceptions that may occur
	 */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(this.backgroundImageFilename);
        out.writeObject(this.maxUpperXAxisBound);
        out.writeObject(this.maxUpperYAxisBound);
        out.writeObject(this.minLowerXAxisBound);
        out.writeObject(this.minLowerYAxisBound);
    }

    /**
	 * Responsible for the reading of serialized objects.
	 * 
	 * @param in
	 *            input stream
	 * @throws IOException
	 *             Includes any I/O exceptions that may occur
	 * @throws ClassNotFoundException
	 *             Includes any class not found exception that may occur
	 */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.backgroundImageFilename = (String) in.readObject();
        this.maxUpperXAxisBound = (Double) in.readObject();
        this.maxUpperYAxisBound = (Double) in.readObject();
        this.minLowerXAxisBound = (Double) in.readObject();
        this.minLowerYAxisBound = (Double) in.readObject();
        if (this.backgroundImageFilename != null && !this.backgroundImageFilename.equals("")) {
            initBackgroundImage();
            updateBackground();
        }
    }

    /**
	 * Translates a given screen point from a mouse event to the coredponding
	 * image coordinates.
	 * 
	 * @param screenPoint
	 *            screen point
	 * @param screenDataArea
	 *            scaled data area
	 * @return image coordinates
	 */
    public Point translatePointToImageCoord(final Point2D screenPoint, final Rectangle2D screenDataArea) {
        final ValueAxis da = this.getDomainAxis();
        final ValueAxis ra = this.getRangeAxis();
        final double x = da.java2DToValue(screenPoint.getX(), screenDataArea, this.getDomainAxisEdge());
        final double y = ra.java2DToValue(screenPoint.getY(), screenDataArea, this.getRangeAxisEdge());
        return translatePointToImageCoords(x, y);
    }
}
