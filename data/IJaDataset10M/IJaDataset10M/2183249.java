package org.opengis.metadata.lineage;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * Distance between consistent parts of (centre, left side, right side) adjacent pixels.
 *
 * @author  Cédric Briançon (Geomatys)
 * @version 3.0
 * @since   2.3
 */
@UML(identifier = "LE_NominalResolution", specification = ISO_19115_2)
public interface NominalResolution {

    /**
     * Distance between consistent parts of (centre, left side, right side) adjacent pixels
     * in the scan plane.
     * <p>
     * <TABLE WIDTH="80%" ALIGN="center" CELLPADDING="18" BORDER="4" BGCOLOR="#FFE0B0">
     *   <TR><TD>
     *     <P align="justify"><B>Warning:</B> The return type of this method may change in GeoAPI
     *     3.1. It may be replaced by the {@link javax.measure.quantity.Length} type in order to
     *     provide unit of measurement together with the value.</P>
     *   </TD></TR>
     * </TABLE>
     *
     * @return Distance between consistent parts of adjacent pixels in the scan plane.
     * @unitof Distance
     */
    @UML(identifier = "scanningResolution", obligation = MANDATORY, specification = ISO_19115_2)
    Double getScanningResolution();

    /**
     * Distance between consistent parts of (centre, left side, right side) adjacent pixels
     * in the object space.
     * <p>
     * <TABLE WIDTH="80%" ALIGN="center" CELLPADDING="18" BORDER="4" BGCOLOR="#FFE0B0">
     *   <TR><TD>
     *     <P align="justify"><B>Warning:</B> The return type of this method may change in GeoAPI
     *     3.1. It may be replaced by the {@link javax.measure.quantity.Length} type in order to
     *     provide unit of measurement together with the value.</P>
     *   </TD></TR>
     * </TABLE>
     *
     * @return Distance between consistent parts of adjacent pixels in the object space.
     * @unitof Distance
     */
    @UML(identifier = "groundResolution", obligation = MANDATORY, specification = ISO_19115_2)
    Double getGroundResolution();
}
