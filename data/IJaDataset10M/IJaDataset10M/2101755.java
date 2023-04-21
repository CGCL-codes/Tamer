package org.nakedobjects.noa.reflect;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.hide.HiddenFacet;
import org.nakedobjects.noa.filters.AbstractFilter;
import org.nakedobjects.noa.filters.Filter;
import org.nakedobjects.noa.security.Session;

public class NakedObjectAssociationFilters {

    private NakedObjectAssociationFilters() {
    }

    /**
     * Filters only fields that are for properties (ie 1:1 associations)
     */
    public static final Filter<NakedObjectAssociation> PROPERTIES = new AbstractFilter<NakedObjectAssociation>() {

        @Override
        public boolean accept(final NakedObjectAssociation f) {
            return f.isObject();
        }
    };

    /**
     * Returns all fields (that is, filters out nothing).
     */
    public static final Filter<NakedObjectAssociation> ALL = new AbstractFilter<NakedObjectAssociation>() {

        @Override
        public boolean accept(final NakedObjectAssociation f) {
            return true;
        }
    };

    /**
     * Filters only fields that are for collections (ie 1:m associations)
     */
    public static final Filter<NakedObjectAssociation> COLLECTIONS = new AbstractFilter<NakedObjectAssociation>() {

        @Override
        public boolean accept(final NakedObjectAssociation f) {
            return f.isCollection();
        }
    };

    /**
     * Filters only properties that are visible statically, ie have not been hidden at compile time.
     */
    public static final Filter<NakedObjectAssociation> STATICALLY_VISIBLE_ASSOCIATIONS = new AbstractFilter<NakedObjectAssociation>() {

        @Override
        public boolean accept(final NakedObjectAssociation f) {
            return !f.containsFacet(HiddenFacet.class);
        }
    };

    /**
     * Filters only properties that are visible statically, ie have not been hidden at compile time.
     */
    public static Filter<NakedObjectAssociation> dynamicallyVisible(final Session session, final NakedObject target) {
        return new AbstractFilter<NakedObjectAssociation>() {

            @Override
            public boolean accept(final NakedObjectAssociation nakedObjectAssociation) {
                final boolean isStaticallyHidden = nakedObjectAssociation.containsFacet(HiddenFacet.class);
                final Consent visible = nakedObjectAssociation.isVisible(session, target);
                return !isStaticallyHidden && visible.isAllowed();
            }
        };
    }
}
