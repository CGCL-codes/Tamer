package xxl.connectivity.jts;

import xxl.core.spatial.geometries.MultiLineString2D;
import com.vividsolutions.jts.geom.MultiLineString;

/** Implementation of the {@link MultiLineString2D} interface based on JTS {@link MultiLineString}s.
 */
public class MultiLineString2DAdapter extends GeometryCollection2DAdapter<LineString2DAdapter> implements MultiLineString2D<LineString2DAdapter> {

    /** Sole constructor: wraps the given {@link MultiLineString} inside this object 
	 * @param multiLineString The JTS-MultiLineString to wrap 
	 */
    public MultiLineString2DAdapter(MultiLineString multiLineString) {
        super(multiLineString);
    }

    /** @inheritDoc
	 *  @see MultiLineString#isClosed()
	 */
    public boolean isClosed() {
        return ((MultiLineString) geometry).isClosed();
    }

    /** @inheritDoc
	 */
    public double getLength() {
        return ((MultiLineString) geometry).getLength();
    }

    /** Returns the encapsulated Geometry in its declared type.
	 * @return the underlying JTS- Geometry	
	 */
    public MultiLineString getJTSGeometry() {
        return (MultiLineString) super.getJTSGeometry();
    }
}
