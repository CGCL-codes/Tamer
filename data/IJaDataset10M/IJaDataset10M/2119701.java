package org.fao.fenix.domain.map.geoserver;

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
        this("EPSG:4326", -180, -90, 180, 90);
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[ srs: '" + srs + "'" + " minx:" + minX + " maxx:" + maxX + " miny:" + minY + " maxy:" + maxY + "']";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BoundingBox other = (BoundingBox) obj;
        if (this.srs != other.srs && (this.srs == null || !this.srs.equals(other.srs))) {
            return false;
        }
        if (this.minX != other.minX) {
            return false;
        }
        if (this.minY != other.minY) {
            return false;
        }
        if (this.maxX != other.maxX) {
            return false;
        }
        if (this.maxY != other.maxY) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.minX) ^ (Double.doubleToLongBits(this.minX) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.minY) ^ (Double.doubleToLongBits(this.minY) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.maxX) ^ (Double.doubleToLongBits(this.maxX) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.maxY) ^ (Double.doubleToLongBits(this.maxY) >>> 32));
        return hash;
    }
}
