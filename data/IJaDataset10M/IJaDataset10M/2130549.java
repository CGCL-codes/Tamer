package org.opengis.geometry.coordinate;

import java.util.List;
import org.opengis.geometry.primitive.Surface;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * A surface composed of {@linkplain Polygon polygon surfaces} connected along their common
 * boundary curves. This differs from {@link Surface} only in the restriction on the types of
 * surface patches acceptable.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 *
 * @see GeometryFactory#createPolyhedralSurface
 */
@UML(identifier = "GM_PolyhedralSurface", specification = ISO_19107)
public interface PolyhedralSurface extends Surface {

    /**
     * Associates this surface with its individual facet polygons.
     */
    @UML(identifier = "patch", obligation = MANDATORY, specification = ISO_19107)
    List<? extends Polygon> getPatches();
}
