package de.grogra.ext.x3d.interfaces;

import de.grogra.ext.x3d.objects.X3DNormal;

/**
 * Interface for nodes with a normal node.
 * 
 * @author Udo Bischof, Uwe Mannl
 *
 */
public interface Normal {

    public X3DNormal getNormal();

    public void setNormal(X3DNormal normal);
}
