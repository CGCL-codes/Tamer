package org.nakedobjects.noa.interactions;

import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.noa.facets.FacetFilters;
import org.nakedobjects.noa.facets.FacetHolder;

public final class InteractionUtils {

    private InteractionUtils() {
    }

    public static InteractionResult isVisibleResult(final FacetHolder facetHolder, final VisibilityContext<?> context) {
        final InteractionResult result = new InteractionResult(context.createInteractionEvent());
        final Facet[] facets = facetHolder.getFacets(FacetFilters.isA(HidingInteractionAdvisor.class));
        for (int i = 0; i < facets.length; i++) {
            final HidingInteractionAdvisor advisor = (HidingInteractionAdvisor) facets[i];
            result.advise(advisor.hides(context), advisor);
        }
        return result;
    }

    public static InteractionResultSet isVisibleResultSet(final FacetHolder facetHolder, final VisibilityContext<?> context, final InteractionResultSet resultSet) {
        return resultSet.add(isVisibleResult(facetHolder, context));
    }

    public static InteractionResult isUsableResult(final FacetHolder facetHolder, final UsabilityContext<?> context) {
        final InteractionResult result = new InteractionResult(context.createInteractionEvent());
        final Facet[] facets = facetHolder.getFacets(FacetFilters.isA(DisablingInteractionAdvisor.class));
        for (int i = 0; i < facets.length; i++) {
            final DisablingInteractionAdvisor advisor = (DisablingInteractionAdvisor) facets[i];
            final String disables = advisor.disables(context);
            result.advise(disables, advisor);
        }
        return result;
    }

    public static InteractionResultSet isUsableResultSet(final FacetHolder facetHolder, final UsabilityContext<?> context, final InteractionResultSet resultSet) {
        return resultSet.add(isUsableResult(facetHolder, context));
    }

    public static InteractionResult isValidResult(final FacetHolder facetHolder, final ValidityContext<?> context) {
        final InteractionResult result = new InteractionResult(context.createInteractionEvent());
        final Facet[] facets = facetHolder.getFacets(FacetFilters.isA(ValidatingInteractionAdvisor.class));
        for (int i = 0; i < facets.length; i++) {
            final ValidatingInteractionAdvisor advisor = (ValidatingInteractionAdvisor) facets[i];
            result.advise(advisor.invalidates(context), advisor);
        }
        return result;
    }

    public static InteractionResultSet isValidResultSet(final FacetHolder facetHolder, final ValidityContext<?> context, final InteractionResultSet resultSet) {
        return resultSet.add(isValidResult(facetHolder, context));
    }
}
