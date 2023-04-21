package org.gbif.portal.dto.geospatial;

/**
 * Represents a cell with the lat long limits, along with a density count.
 * 
 * @author trobertson
 */
public class LatLongCellDensityDTO {

    protected Float minLat;

    protected Float minLong;

    protected Float maxLat;

    protected Float maxLong;

    protected int count;

    /**
	 * Convienience
	 * @param minLat
	 * @param minLong
	 * @param maxLat
	 * @param maxLong
	 * @param count
	 */
    public LatLongCellDensityDTO(Float minLat, Float maxLat, Float minLong, Float maxLong, int count) {
        this.minLat = minLat;
        this.minLong = minLong;
        this.maxLat = maxLat;
        this.maxLong = maxLong;
        this.count = count;
    }

    /**
	 * @return the count
	 */
    public int getCount() {
        return count;
    }

    /**
	 * @param count the count to set
	 */
    public void setCount(int count) {
        this.count = count;
    }

    /**
	 * @return the maxLat
	 */
    public Float getMaxLat() {
        return maxLat;
    }

    /**
	 * @param maxLat the maxLat to set
	 */
    public void setMaxLat(Float maxLat) {
        this.maxLat = maxLat;
    }

    /**
	 * @return the maxLong
	 */
    public Float getMaxLong() {
        return maxLong;
    }

    /**
	 * @param maxLong the maxLong to set
	 */
    public void setMaxLong(Float maxLong) {
        this.maxLong = maxLong;
    }

    /**
	 * @return the minLat
	 */
    public Float getMinLat() {
        return minLat;
    }

    /**
	 * @param minLat the minLat to set
	 */
    public void setMinLat(Float minLat) {
        this.minLat = minLat;
    }

    /**
	 * @return the minLong
	 */
    public Float getMinLong() {
        return minLong;
    }

    /**
	 * @param minLong the minLong to set
	 */
    public void setMinLong(Float minLong) {
        this.minLong = minLong;
    }
}
