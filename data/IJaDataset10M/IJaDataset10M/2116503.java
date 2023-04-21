package gov.nasa.worldwindx.applications.sar.render;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Polyline;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.Logging;
import java.awt.*;
import java.util.ArrayList;

/**
 * Renders a plane model at a position with a given heading. The plane is parallel to the ground.
 * An optional 'shadow' shape is rendered on the ground.
 * @author Patrick Murris
 * @version $Id: PlaneModel.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class PlaneModel implements Renderable {

    private Position position;

    private Angle heading;

    private Double length = 100d;

    private Double width = 100d;

    private Color color = Color.YELLOW;

    private boolean showShadow = true;

    private double shadowScale = 1d;

    private Color shadowColor = Color.YELLOW;

    private Polyline planeModel;

    private Polyline shadowModel;

    /**
     * Renders a plane model with the defaul dimensions and color.
     */
    public PlaneModel() {
    }

    /**
     * Renders a plane model with the specified dimensions and color.
     * @param length the plane length in meters
     * @param width the plane width in meter.
     * @param color the plane color.
     */
    public PlaneModel(Double length, Double width, Color color) {
        this.length = length;
        this.width = width;
        this.color = color;
    }

    public void setPosition(Position pos) {
        if (pos == null) {
            String msg = Logging.getMessage("nullValue.PositionIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.position = pos;
        clearRenderables();
    }

    public Position getPosition() {
        return this.position;
    }

    public void setHeading(Angle head) {
        if (head == null) {
            String msg = Logging.getMessage("nullValue.AngleIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.heading = head;
        clearRenderables();
    }

    public Angle getHeading() {
        return this.heading;
    }

    public void setShowShadow(boolean state) {
        this.showShadow = state;
    }

    public boolean getShowShadow() {
        return this.showShadow;
    }

    public double getShadowScale() {
        return this.shadowScale;
    }

    public void setShadowScale(double shadowScale) {
        this.shadowScale = shadowScale;
        clearRenderables();
    }

    public Color getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        clearRenderables();
    }

    public void render(DrawContext dc) {
        if (dc == null) {
            String msg = Logging.getMessage("nullValue.DrawContextIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (this.position == null || this.heading == null) return;
        if (this.planeModel == null) createRenderables(dc);
        this.planeModel.render(dc);
        if (this.showShadow && this.shadowModel != null) this.shadowModel.render(dc);
    }

    private void createRenderables(DrawContext dc) {
        ArrayList<LatLon> positions = computePlaneShape(dc, this.width, this.length);
        this.planeModel = new Polyline(positions, this.position.getElevation());
        this.planeModel.setPathType(Polyline.LINEAR);
        this.planeModel.setFollowTerrain(false);
        this.planeModel.setNumSubsegments(1);
        this.planeModel.setColor(this.color);
        positions = computePlaneShape(dc, this.shadowScale * this.width, this.shadowScale * this.length);
        this.shadowModel = new Polyline(positions, this.position.getElevation());
        this.shadowModel.setPathType(Polyline.LINEAR);
        this.shadowModel.setFollowTerrain(true);
        this.shadowModel.setColor(this.shadowColor);
    }

    private void clearRenderables() {
        this.planeModel = null;
        this.shadowModel = null;
    }

    private ArrayList<LatLon> computePlaneShape(DrawContext dc, double width, double length) {
        ArrayList<LatLon> positions = new ArrayList<LatLon>();
        LatLon center = this.position;
        double hl = length / 2;
        double hw = width / 2;
        double radius = dc.getGlobe().getRadius();
        LatLon p = LatLon.rhumbEndPosition(center, this.heading.radians, hl / radius);
        positions.add(p);
        double d = Math.sqrt(hw * hw + hl * hl);
        double a = Math.PI / 2 + Math.asin(hl / d);
        p = LatLon.rhumbEndPosition(center, this.heading.radians + a, d / radius);
        positions.add(p);
        p = LatLon.rhumbEndPosition(center, this.heading.radians - a, d / radius);
        positions.add(p);
        positions.add(positions.get(0));
        return positions;
    }
}
