package org.opengis.metadata.lineage;

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.metadata.quality.Scope;
import org.opengis.metadata.maintenance.ScopeCode;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;

/**
 * Information about the events or source data used in constructing the data specified by
 * the scope or lack of knowledge about lineage.
 *
 * Only one of {@linkplain #getStatement statement}, {@linkplain #getProcessSteps process steps}
 * and {@linkplain #getSources sources} shall be provided.
 *
 * @author  Martin Desruisseaux (IRD)
 * @version 3.0
 * @since   2.0
 *
 * @navassoc - - - ProcessStep
 * @navassoc - - - Source
 */
@UML(identifier = "LI_Lineage", specification = ISO_19115)
public interface Lineage {

    /**
     * General explanation of the data producer's knowledge about the lineage of a dataset.
     *
     * @return Explanation of the data producer's knowledge about the lineage, or {@code null}.
     *
     * @condition Shall be provided only if {@linkplain Scope#getLevel scope level} is
     *            {@link ScopeCode#DATASET} or {@link ScopeCode#SERIES}.
     */
    @Profile(level = CORE)
    @UML(identifier = "statement", obligation = CONDITIONAL, specification = ISO_19115)
    InternationalString getStatement();

    /**
     * Information about an event in the creation process for the data specified by the scope.
     *
     * @return Information about an event in the creation process.
     *
     * @condition Mandatory if {@linkplain #getStatement statement} and
     *            {@linkplain #getSources source} are not provided.
     */
    @UML(identifier = "processStep", obligation = CONDITIONAL, specification = ISO_19115)
    Collection<? extends ProcessStep> getProcessSteps();

    /**
     * Information about the source data used in creating the data specified by the scope.
     *
     * @return Information about the source data.
     *
     * @condition Mandatory if {@linkplain #getStatement statement} and
     *           {@linkplain #getProcessSteps process step} are not provided.
     */
    @UML(identifier = "source", obligation = CONDITIONAL, specification = ISO_19115)
    Collection<? extends Source> getSources();
}
