package com.google.api.chart.features;

import com.google.api.chart.LocationMaker;

/**
 * Marks support for map features.
 * 
 * @author sorenad
 * @param <T>
 */
public interface MapFeature<T> {

    /**
	 * Specifies the locations to be used in a map chart.
	 * @param locations
	 * @return
	 */
    public T locations(LocationMaker... locations);
}
