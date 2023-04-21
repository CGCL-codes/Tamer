package org.fao.fenix.domain.layer.util;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BoundingBox {

    @Column(nullable = false)
    private String srs;

    private double minX;

    private double minY;

    private double maxX;

    private double maxY;

    /**
     * 
     */
    public BoundingBox() {
        super();
    }

    /**
     * @param srs
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     */
    public BoundingBox(String srs, double minX, double minY, double maxX, double maxY) {
        super();
        this.srs = srs;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * @return the maxX
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * @param maxX
     *            the maxX to set
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /**
     * @return the maxY
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * @param maxY
     *            the maxY to set
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /**
     * @return the minX
     */
    public double getMinX() {
        return minX;
    }

    /**
     * @param minX
     *            the minX to set
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }

    /**
     * @return the minY
     */
    public double getMinY() {
        return minY;
    }

    /**
     * @param minY
     *            the minY to set
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /**
     * @return the srs
     */
    public String getSrs() {
        return srs;
    }

    /**
     * @param srs
     *            the srs to set
     */
    public void setSrs(String srs) {
        this.srs = srs;
    }
}
