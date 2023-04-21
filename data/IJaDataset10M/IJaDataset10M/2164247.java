package org.opengis.metadata.identification;

import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;

/**
 * Level of detail expressed as a scale factor or a ground distance.
 *
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @version 3.0
 * @since   2.0
 *
 * @navassoc - - - RepresentativeFraction
 */
@UML(identifier = "MD_Resolution", specification = ISO_19115)
public interface Resolution {

    /**
     * Level of detail expressed as the scale of a comparable hardcopy map or chart.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     *
     * @return Level of detail expressed as the scale of a comparable hardcopy, or {@code null}.
     *
     * @condition {@linkplain #getDistance() Distance} not documented.
     */
    @Profile(level = CORE)
    @UML(identifier = "equivalentScale", obligation = CONDITIONAL, specification = ISO_19115)
    RepresentativeFraction getEquivalentScale();

    /**
     * Ground sample distance.
     * Only one of {@linkplain #getEquivalentScale equivalent scale} and
     * {@linkplain #getDistance ground sample distance} may be provided.
     * <p>
     * <TABLE WIDTH="80%" ALIGN="center" CELLPADDING="18" BORDER="4" BGCOLOR="#FFE0B0">
     *   <TR><TD>
     *     <P align="justify"><B>Warning:</B> The return type of this method may change in GeoAPI
     *     3.1. It may be replaced by the {@link javax.measure.quantity.Length} type in order to
     *     provide unit of measurement together with the value.</P>
     *   </TD></TR>
     * </TABLE>
     *
     * @return The ground sample distance, or {@code null}.
     * @unitof Distance
     *
     * @condition {@linkplain #getEquivalentScale() Equivalent scale} not documented.
     */
    @Profile(level = CORE)
    @UML(identifier = "distance", obligation = CONDITIONAL, specification = ISO_19115)
    Double getDistance();
}
