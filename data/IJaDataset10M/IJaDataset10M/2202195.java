package org.javasurf.base;

/**
 * Generic interface for a IDescriptor class. A IDescriptor extracts descriptor
 * components for a given set of detected interest points, given when a class
 * that implements the IDescriptor interface is instantiated.
 * 
 * @author Alessandro Martini, Claudio Fantacci
 */
public interface IDescriptor {

    /**
	 * Extracts descriptor components for a given set of detected interest
	 * points.
	 */
    public void generateAllDescriptors();
}
