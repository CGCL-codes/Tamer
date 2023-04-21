package org.matsim.lanes.otfvis.io;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * @author dgrether
 *
 */
public class OTFLaneLinkData {

    private Point2D.Double linkStart;

    private Point2D.Double linkEnd;

    private Double normalizedLinkVector;

    private Double linkOrthogonalVector;

    private double numberOfLanes = 1.0;

    private int maximalAlignment = 0;

    public void setLinkStart(double x, double y) {
        this.linkStart = new Point2D.Double(x, y);
    }

    public void setLinkEnd(double x, double y) {
        this.linkEnd = new Point2D.Double(x, y);
    }

    public void setNormalizedLinkVector(double x, double y) {
        this.normalizedLinkVector = new Point2D.Double(x, y);
    }

    public void setLinkOrthogonalVector(double x, double y) {
        this.linkOrthogonalVector = new Point2D.Double(x, y);
    }

    public Point2D.Double getLinkStart() {
        return linkStart;
    }

    public Point2D.Double getLinkEnd() {
        return linkEnd;
    }

    public Double getNormalizedLinkVector() {
        return normalizedLinkVector;
    }

    public Double getLinkOrthogonalVector() {
        return linkOrthogonalVector;
    }

    public void setNumberOfLanes(double nrLanes) {
        this.numberOfLanes = nrLanes;
    }

    public double getNumberOfLanes() {
        return this.numberOfLanes;
    }

    public void setMaximalAlignment(int maxAlign) {
        this.maximalAlignment = maxAlign;
    }

    public int getMaximalAlignment() {
        return this.maximalAlignment;
    }
}
