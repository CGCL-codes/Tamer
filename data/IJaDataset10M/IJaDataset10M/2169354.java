package de.grogra.ext.x3d.interfaces;

import de.grogra.ext.x3d.objects.X3DCoordinate;

/**
 * Interface for nodes with a coordinate node.
 * 
 * @author Udo Bischof, Uwe Mannl
 *
 */
public interface Coordinate {

    public X3DCoordinate getCoord();

    public void setCoord(X3DCoordinate coord);
}
