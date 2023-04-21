package org.openscience.jmol;

import java.awt.Graphics;

/**
 * Provides an interface for graphical components so that
 * proper depth rendering occurs using z-buffering. Improper
 * depth rendering will still occur for spatially intersecting
 * shapes. This is an unavoidable limitation of the pseudo-3D
 * rendering implemented here.
 *
 * @author Bradley A. Smith (bradley@baysmith.com)
 */
interface Shape {

    public void render(Graphics g);

    public double getZ();
}
