package com.googlecode.grs.event;

/**
 * 
 * @author Zhang
 *
 */
public interface CompassListener {

    /**
	 * 
	 * @param heading
	 */
    public void onOrientationChanged(double heading);
}
