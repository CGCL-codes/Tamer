package gumbo.tech.space;

import gumbo.tech.math.Matrix4;

/**
 * An object that can spatially transform its own geometry using a Matrix4,
 * typically in a stateless manner, as in a shape deformation. As such,
 * transforms are cummulative.
 * 
 * @see Matrix4
 * 
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 * @version $Revision: 1.1 $
 */
public interface Transformable {

    /**
	 * Convert's this object's geometry using the specified spatial transform.
	 * If this object's geometry is spatially constrained some or all of the
	 * mapping may be ignored.
	 * @param transform Temp input spatial transform. Never null.
	 */
    public void transformGeometry(Matrix4 transform);
}
