package gpsmate.visuals.heightprofile;

import gpsmate.geodata.ElevationExtrema;
import gpsmate.geodata.GeoTool;
import gpsmate.geodata.Placemark;
import gpsmate.geodata.Point;
import gpsmate.geodata.Track;
import gpsmate.geodata.UtmPoint;
import gpsmate.utils.Configuration;
import gpsmate.utils.SymbolInfo;
import java.awt.Color;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * HeightProfiler
 * 
 * @author longdistancewalker
 */
public abstract class HeightProfiler {

    /**
   * Distance from profile border to icon border.
   */
    public static final int ICON_SPACING_BORDER = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.border.spacing"));

    /**
   * Minimum space in pixels between bottom of the icon and the top of the
   * height line.
   */
    public static final int ICON_SPACING_MIN = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.spacing.min"));

    /**
   * Maximum space in pixels between bottom of the icon and the top of the
   * height line.
   */
    public static final int ICON_SPACING_MAX = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.spacing.max"));

    /**
   * Minimum width of the resulting height profile.
   */
    public static final int WIDTH_MIN = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.width.min"));

    /**
   * Maximum width of the resulting height profile.
   */
    public static final int WIDTH_MAX = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.width.max"));

    /**
   * Minimum height of the resulting height profile.
   */
    public static final int HEIGHT_MIN = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.height.min"));

    /**
   * Maximum width of the resulting height profile.
   */
    public static final int HEIGHT_MAX = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.height.max"));

    /**
   * Minimum icon size.
   */
    public static final int ICON_SIZE_MIN = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.size.min"));

    /**
   * Maximum icon size.
   */
    public static final int ICON_SIZE_MAX = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.size.max"));

    /**
   * Minimum number of grid lines on the vertical axis (0 means no grids, no
   * labels).
   */
    public static final int SPAN_STEPS_VERTICAL_MIN = 0;

    /**
   * Maximum number of grid lines on the vertical axis.
   */
    public static final int SPAN_STEPS_VERTICAL_MAX = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.spansteps.vertical.max"));

    /**
   * Minimum number of grid lines on the horizontal axis (0 means no grids, no
   * labels).
   */
    public static final int SPAN_STEPS_HORIZONTAL_MIN = 0;

    /**
   * Maximum number of grid lines on the horizontal axis.
   */
    public static final int SPAN_STEPS_HORIZONTAL_MAX = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.spansteps.horizontal.max"));

    /**
   * Current width of the height profile.
   */
    protected int width = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.width.default"));

    /**
   * Current height of the height profile.
   */
    protected int height = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.height.default"));

    /**
   * The elevation extrema holds the lowest and highest point of the current
   * track.
   */
    protected ElevationExtrema elevationExtrema = null;

    /**
   * Smoothing class to prevent single excesses in elevation data.
   */
    protected ElevationSmoother smoother = null;

    /**
   * Reducing class to reduce the number of track points (if there are more
   * points than horizontal pixels...).
   */
    protected PointReducer reducer = null;

    /**
   * Current track for which the height profile is built.
   */
    protected Track track = null;

    /**
   * List of extracted points to be used for rendering.
   */
    protected List<HeightProfilePoint> points = null;

    /**
   * List of points of interest to be used for rendering.
   */
    protected List<HeightProfilePointOfInterest> pointsOfInterest = null;

    /**
   * Background color of the rendered image. Must be set and painted before
   * anything else!
   */
    protected Color backgroundColor = Color.white;

    /**
   * Color of the border line and labels.
   */
    protected Color borderColor = Color.black;

    /**
   * Color of the grid lines.
   */
    protected Color gridColor = Color.black;

    /**
   * Color of the height line.
   */
    protected Color lineColor = new Color(81, 114, 189);

    /**
   * Color of the filled area below the height line.
   */
    protected Color fillColor = new Color(81, 114, 189, 40);

    /**
   * Color of the icon line.
   */
    protected Color iconLineColor = Color.black;

    /**
   * Difference in meters between the highest and lowest point of the track.
   * (Might be set to be more than the real difference to prevent the profile
   * from looking like sine waves...)
   */
    protected double heightSpan = -1;

    /**
   * Lowest point along the track. Will be the first label on the vertical grid.
   */
    protected double spanLowerLimit;

    /**
   * Current number of grid lines at the vertical axis.
   */
    protected int spanStepsVertical = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.spansteps.vertical.default"));

    /**
   * Current number of grid lines at the horizontal axis.
   */
    protected int spanStepsHorizontal = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.spansteps.horizontal.default"));

    /**
   * Current icon size.
   */
    protected int iconSize = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.size.default"));

    /**
   * Current icon spacing (number of pixels between line and icon's bottom
   * line).
   */
    protected int iconSpacing = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.icon.spacing.default"));

    /**
   * Indicates the distance between the single dashes of one grid line.
   */
    protected int gridSpacing = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.grid.spacing"));

    /**
   * Current width of the grid lines.
   */
    protected int gridWidth = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.grid.width"));

    /**
   * Current width of the height profile line.
   */
    protected int lineWidth = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.line.width"));

    /**
   * Current width of the border line.
   */
    protected int borderWidth = Integer.parseInt(Configuration.getInstance().getProperty("heightprofile.border.width"));

    /**
   * Indicates whether the grid should be drawn behind the height profile. If
   * false, the grid lines will be in front.
   */
    protected boolean drawGridBehindProfile = true;

    /**
   * Indicates whether the points of interest should be drawn or not.
   */
    protected boolean drawPointsOfInterest = true;

    /**
   * If set, there will be lines from bottom of the profile to the height line
   * wherever a point of interes is located.
   */
    protected boolean drawPointsOfInterestLines = false;

    /**
   * Indicates a full rerendering of the height profile, that is: reduction and
   * smoothing has to be done again.
   */
    private boolean fullReRender = false;

    /**
   * Creates the profile given the previously set parameters.
   * 
   * @return a 1:1 preview as RenderedImage
   */
    public RenderedImage createProfile() {
        if (fullReRender) {
            extractPoints(track);
            points = reducer.reducePoints(points);
            points = smoother.smoothPoints(points);
            fullReRender = false;
        }
        return render();
    }

    /**
   * @return the iconLineColor
   */
    public Color getIconLineColor() {
        return iconLineColor;
    }

    /**
   * @return the iconSize
   */
    public int getIconSize() {
        return iconSize;
    }

    /**
   * @param iconSize
   *          the iconSize to set
   */
    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    /**
   * @return the iconSpacing
   */
    public int getIconSpacing() {
        return iconSpacing;
    }

    /**
   * @param iconSpacing
   *          the iconSpacing to set
   */
    public void setIconSpacing(int iconSpacing) {
        this.iconSpacing = iconSpacing;
    }

    /**
   * @param iconLineColor
   *          the iconLineColor to set
   */
    public void setIconLineColor(Color iconLineColor) {
        this.iconLineColor = iconLineColor;
    }

    /**
   * @return the drawPointsOfInterestLines
   */
    public boolean isDrawPointsOfInterestLines() {
        return drawPointsOfInterestLines;
    }

    /**
   * @param drawPointsOfInterestLines
   *          the drawPointsOfInterestLines to set
   */
    public void setDrawPointsOfInterestLines(boolean drawPointsOfInterestLines) {
        this.drawPointsOfInterestLines = drawPointsOfInterestLines;
    }

    /**
   * @return the drawPointsOfInterest
   */
    public boolean isDrawPointsOfInterest() {
        return drawPointsOfInterest;
    }

    /**
   * @param drawPointsOfInterest
   *          the drawPointsOfInterest to set
   */
    public void setDrawPointsOfInterest(boolean drawPointsOfInterest) {
        this.drawPointsOfInterest = drawPointsOfInterest;
    }

    /**
   * @return the gridColor
   */
    public Color getGridColor() {
        return gridColor;
    }

    /**
   * @param gridColor
   *          the gridColor to set
   */
    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    /**
   * @return the spanStepsHorizontal
   */
    public int getSpanStepsHorizontal() {
        return spanStepsHorizontal;
    }

    /**
   * @param spanStepsHorizontal
   *          the spanStepsHorizontal to set
   */
    public void setSpanStepsHorizontal(int spanStepsHorizontal) {
        this.spanStepsHorizontal = spanStepsHorizontal;
    }

    /**
   * @return the fillColor
   */
    public Color getFillColor() {
        return fillColor;
    }

    /**
   * @param fillColor
   *          the fillColor to set
   */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
   * @return the backgroundColor
   */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
   * @return the borderColor
   */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
   * @return the lineColor
   */
    public Color getLineColor() {
        return lineColor;
    }

    /**
   * @param drawGridBehindProfile
   *          the drawGridBehindProfile to set
   */
    public void setDrawGridBehindProfile(boolean drawGridBehindProfile) {
        this.drawGridBehindProfile = drawGridBehindProfile;
    }

    /**
   * @param spanLowerLimit
   *          the spanLowerLimit to set
   */
    public void setSpanLowerLimit(double spanLowerLimit) {
        this.spanLowerLimit = spanLowerLimit;
    }

    /**
   * @return the spanStepsVertical
   */
    public int getSpanStepsVertical() {
        return spanStepsVertical;
    }

    /**
   * @param spanStepsVertical
   *          the spanStepsVertical to set
   */
    public void setSpanStepsVertical(int spanStepsVertical) {
        this.spanStepsVertical = spanStepsVertical;
    }

    /**
   * @return the heightSpan
   */
    public double getHeightSpan() {
        return heightSpan;
    }

    /**
   * @param heightSpan
   *          the heightSpan to set
   */
    public void setHeightSpan(double heightSpan) {
        this.heightSpan = heightSpan;
    }

    /**
   * @param backgroundColor
   *          the backgroundColor to set
   */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
   * @param borderColor
   *          the borderColor to set
   */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
   * @param lineColor
   *          the lineColor to set
   */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
   * @param track
   *          the track to set
   */
    public void setTrack(Track track) {
        fullReRender = true;
        this.track = track;
        elevationExtrema = GeoTool.computeElevationExtrema(track);
        this.spanLowerLimit = elevationExtrema.getMin();
        this.heightSpan = elevationExtrema.getMax() - elevationExtrema.getMin();
        extractPoints(track);
    }

    /**
   * @return the width
   */
    public int getWidth() {
        return width;
    }

    /**
   * @param width
   *          the width to set
   */
    public void setWidth(int width) {
        if (this.width != width) fullReRender = true;
        this.width = width;
    }

    /**
   * @return the height
   */
    public int getHeight() {
        return height;
    }

    /**
   * @param height
   *          the height to set
   */
    public void setHeight(int height) {
        if (this.height != height) fullReRender = true;
        this.height = height;
    }

    /**
   * @param smoother
   *          the smoother to set
   */
    public void setSmoother(ElevationSmoother smoother) {
        this.smoother = smoother;
    }

    /**
   * @param reducer
   *          the reducer to set
   */
    public void setReducer(PointReducer reducer) {
        this.reducer = reducer;
    }

    /**
   * Renders the height profile and returns a preview image.
   * 
   * @return preview which can be used for further tasks
   */
    protected abstract RenderedImage render();

    /**
   * Saves the height profile to the specified file.
   * 
   * @param filename
   * @return
   */
    public abstract boolean saveToFile(String filename);

    public void extractPoints(Track t) {
        points = new ArrayList<HeightProfilePoint>();
        pointsOfInterest = new ArrayList<HeightProfilePointOfInterest>();
        if (t.getPoints().size() > 0) {
            for (Placemark p : t.getPointsOfInterest()) {
                pointsOfInterest.add(new HeightProfilePointOfInterest(GeoTool.convertLatLonToUtm(p.getLocation()), SymbolInfo.getSymbol(p)));
            }
            UtmPoint previous = GeoTool.convertLatLonToUtm(t.getPoints().get(0));
            double distanceFromStart = 0.0;
            int i = 0;
            for (Point p : t.getPoints()) {
                UtmPoint utm = GeoTool.convertLatLonToUtm(p);
                double distance = utm.getDistance3d(previous);
                distanceFromStart += distance;
                points.add(new HeightProfilePoint(utm, distanceFromStart));
                previous = utm;
                for (HeightProfilePointOfInterest poi : pointsOfInterest) {
                    if (isNearer(points.get(i), poi)) poi.setNextPointOnTrack(i);
                }
                i++;
            }
            for (HeightProfilePointOfInterest poi : pointsOfInterest) {
                poi.setDistanceToStart(points.get(poi.getNextPointOnTrack()).getDistanceToStart());
                poi.setElevation(points.get(poi.getNextPointOnTrack()).getElevation());
            }
        }
    }

    private boolean isNearer(HeightProfilePoint trackPoint, HeightProfilePointOfInterest poi) {
        if (poi.getDistance3d(trackPoint) < poi.getDistance3d(this.points.get(poi.getNextPointOnTrack()))) return true;
        return false;
    }
}
