package uk.ac.ebi.intact.model;

import java.util.Collection;

/**
 * For annotated objects that can have confidences.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since 1.4.0
 */
public interface ConfidenceHolder<T> {

    void setConfidences(Collection<T> confidences);

    void addConfidence(T confidence);

    void removeConfidence(T confidence);

    Collection<T> getConfidences();
}
