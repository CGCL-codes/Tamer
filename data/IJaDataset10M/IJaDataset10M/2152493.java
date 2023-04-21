package org.miv.jism.core;

/**
 * Describe a pixel of a JismContext.
 * Implementation of a pixel depends of the context. A 2D context
 * only need to use two coordinates attributes whereas a 3D needs
 * three... So this interface allows contexts to have their own
 * optimized implementation of a pixel.
 *
 * @author Guilhelm Savin
 *
 * @see org.miv.jism.core.JismContext
 **/
public interface Pixel {

    /**
	 * Return abscissa of the pixel.
	 *
	 * @return abscissa
	 **/
    public int x();

    /**
	 * Return ordinate of the pixel.
	 *
	 * @return ordinate
	 **/
    public int y();

    /**
	 * Return depth of the pixel.
	 *
	 * @return depth
	 **/
    public int z();

    /**
	 * Return level intensity of the pixel.
	 *
	 * @return intensity
	 **/
    public int level();
}
