package org.pagger.data.picture.geo.gpx;

import org.jscience.geography.coordinates.LatLong;
import org.pagger.util.Validator;

/**
 * Two lat/lon pairs defining the extent of an element.
 * 
 * @author Franz Wilhelmstötter
 */
public class Bounds {

    private final LatLong _minLatLong;

    private final LatLong _maxLatLong;

    /**
	 * Create a new geometric bounds.
	 * 
	 * @param minLatLong the minimal point.
	 * @param maxLatLong the maximal point.
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
    public Bounds(final LatLong minLatLong, final LatLong maxLatLong) {
        Validator.notNull(minLatLong, "Minimal point");
        Validator.notNull(maxLatLong, "Maximal point");
        _minLatLong = minLatLong;
        _maxLatLong = maxLatLong;
    }

    /**
	 * Return the minimal point. The returned value is never {@code null}.
	 * 
	 * @return the minimal point.
	 */
    public LatLong getMinLatLong() {
        return _minLatLong;
    }

    /**
	 * Return the maximal point. The returned value is never {@code null}.
	 * 
	 * @return the maximal point.
	 */
    public LatLong getMaxLatLong() {
        return _maxLatLong;
    }
}
