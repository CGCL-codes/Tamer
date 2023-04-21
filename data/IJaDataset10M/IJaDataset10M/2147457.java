package uk.ac.rdg.resc.ncwms.coords;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.rdg.resc.ncwms.coords.CurvilinearGrid.Cell;

/**
 * An object that provides an approximate means for mapping from longitude-latitude
 * coordinates to i and j index coordinates in a curvilinear grid.
 * @todo Some duplication of {@link HorizontalGrid}?  There's a difference in
 * how the "tick marks" along the axes are set up: see how the Regular1DCoordAxes
 * are created.
 * @author Jon
 */
final class LookUpTable {

    private static final Logger logger = LoggerFactory.getLogger(LookUpTable.class);

    private DataBuffer iIndices;

    private DataBuffer jIndices;

    private final int nLon;

    private final int nLat;

    private final AffineTransform transform = new AffineTransform();

    /**
     * A {@link DirectColorModel} that holds data as unsigned shorts, ignoring
     * the red and alpha components.  (int value = green &lt;&lt; 8 | blue)
     */
    private static final ColorModel COLOR_MODEL = new DirectColorModel(16, 0x00000000, 0x0000ff00, 0x000000ff, 0x00000000);

    /** This value in the look-up table means "missing value" */
    private static final int MISSING_VALUE = 65535;

    /** This is the maximum index that can be stored in the LUT */
    private static final int MAX_INDEX = 65534;

    /**
     * Creates an empty look-up table (with all indices set to -1).
     * @param curvGrid The CurvilinearGrid which this LUT will approximate
     * @param minResolution The minimum resolution of the LUT in degrees
     */
    public LookUpTable(CurvilinearGrid curvGrid, double minResolution) {
        GeographicBoundingBox bbox = curvGrid.getBoundingBox();
        double lonDiff = bbox.getEastBoundLongitude() - bbox.getWestBoundLongitude();
        double latDiff = bbox.getNorthBoundLatitude() - bbox.getSouthBoundLatitude();
        this.nLon = (int) Math.ceil(lonDiff / minResolution);
        this.nLat = (int) Math.ceil(latDiff / minResolution);
        if (this.nLon <= 0 || this.nLat <= 0) {
            String msg = String.format("nLon (=%d) and nLat (=%d) must be positive and > 0", this.nLon, this.nLat);
            throw new IllegalStateException(msg);
        }
        double lonStride = lonDiff / (this.nLon - 1);
        double latStride = latDiff / (this.nLat - 1);
        this.transform.scale(1.0 / lonStride, 1.0 / latStride);
        this.transform.translate(-bbox.getWestBoundLongitude(), -bbox.getSouthBoundLatitude());
        this.makeLuts(curvGrid);
    }

    /**
     * Generates the data for the look-up tables
     */
    private void makeLuts(CurvilinearGrid curvGrid) {
        BufferedImage iIm = this.createBufferedImage();
        BufferedImage jIm = this.createBufferedImage();
        Graphics2D ig2d = iIm.createGraphics();
        Graphics2D jg2d = jIm.createGraphics();
        ig2d.setTransform(this.transform);
        jg2d.setTransform(this.transform);
        for (Cell cell : curvGrid) {
            Path2D path = cell.getBoundaryPath();
            if (cell.getI() > MAX_INDEX || cell.getJ() > MAX_INDEX) {
                throw new IllegalStateException("Can't store indices greater than " + MAX_INDEX);
            }
            ig2d.setPaint(new Color(cell.getI()));
            jg2d.setPaint(new Color(cell.getJ()));
            ig2d.fill(path);
            jg2d.fill(path);
            double shiftLon = cell.getCentre().getLongitude() > 0.0 ? -360.0 : 360.0;
            path.transform(AffineTransform.getTranslateInstance(shiftLon, 0.0));
            ig2d.fill(path);
            jg2d.fill(path);
        }
        this.iIndices = iIm.getRaster().getDataBuffer();
        this.jIndices = jIm.getRaster().getDataBuffer();
    }

    /**
     * Creates and returns a new {@link BufferedImage} that stores pixel data
     * as unsigned shorts.  Initializes all pixel values to {@link #MISSING_VALUE}.
     * @return
     */
    private BufferedImage createBufferedImage() {
        WritableRaster raster = COLOR_MODEL.createCompatibleWritableRaster(this.nLon, this.nLat);
        BufferedImage im = new BufferedImage(COLOR_MODEL, raster, true, null);
        for (int y = 0; y < im.getHeight(); y++) {
            for (int x = 0; x < im.getWidth(); x++) {
                im.setRGB(x, y, MISSING_VALUE);
            }
        }
        logger.debug("Created BufferedImage of size {},{}, data buffer type {}", new Object[] { im.getWidth(), im.getHeight(), im.getRaster().getDataBuffer().getClass() });
        return im;
    }

    /**
     * Returns the nearest coordinates in the original CurvilinearGrid
     * to the given longitude-latitude
     * point, or null if the given longitude-latitude point is not in the domain
     * of this look-up table.
     * @param longitude The longitude of the point of interest
     * @param latitude The latitude of the point of interest
     * @return A newly-created integer array with two values: the first value is
     * the i coordinate in the grid, the second is the j coordinate.  Returns
     * null if the given longitude-latitude point is not in the domain of this LUT.
     */
    public int[] getGridCoordinates(double longitude, double latitude) {
        Point2D indexPoint = this.transform.transform(new Point2D.Double(longitude, latitude), null);
        int iLon = (int) Math.round(indexPoint.getX());
        int iLat = (int) Math.round(indexPoint.getY());
        if (iLon < 0 || iLat < 0 || iLon >= this.nLon || iLat >= this.nLat) {
            return null;
        }
        int index = iLon + (iLat * this.nLon);
        int iIndex = this.iIndices.getElem(index);
        int jIndex = this.jIndices.getElem(index);
        if (iIndex < 0 || iIndex > MAX_INDEX || jIndex < 0 || jIndex > MAX_INDEX) {
            return null;
        }
        return new int[] { iIndex, jIndex };
    }

    /**
     * Gets the number of points in this look-up table along its longitude axis
     */
    public int getNumLonPoints() {
        return this.nLon;
    }

    /**
     * Gets the number of points in this look-up table along its latitude axis
     */
    public int getNumLatPoints() {
        return this.nLat;
    }

    /**
     * Gets an affine transform that converts from lat-lon coordinates to the
     * indices in the lat-lon grid used by this look-up table.
     * @return
     */
    public AffineTransform getTransform() {
        return this.transform;
    }
}
