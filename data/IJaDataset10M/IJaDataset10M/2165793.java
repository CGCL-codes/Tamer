package org.starobjects.jpa.metamodel.facets.prop.onetoone;

import javax.persistence.OneToOne;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacetAbstract;

/**
 * Derived by presence of {@link OneToOne#optional()}</tt> set to <tt>true</tt>.
 * 
 * <p>
 * This implementation indicates that the {@link FacetHolder} is <i>not</i> mandatory, as per
 * {@link #isInvertedSemantics()}.
 */
public class OptionalFacetDerivedFromJpaOneToOneAnnotation extends MandatoryFacetAbstract {

    public OptionalFacetDerivedFromJpaOneToOneAnnotation(final FacetHolder holder) {
        super(holder);
    }

    /**
     * Always returns <tt>false</tt>, indicating that the facet holder is in fact optional.
     */
    public boolean isRequiredButNull(final NakedObject nakedObject) {
        return false;
    }

    public boolean isInvertedSemantics() {
        return true;
    }
}
