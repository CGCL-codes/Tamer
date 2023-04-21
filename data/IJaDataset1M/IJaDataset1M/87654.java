package org.starobjects.jpa.metamodel.facets.object.namedquery;

import javax.persistence.NamedQuery;
import org.nakedobjects.metamodel.facets.FacetHolder;

public class JpaNamedQueriesFacetAnnotation extends JpaNamedQueryFacetAbstract implements JpaNamedQueryFacet {

    public JpaNamedQueriesFacetAnnotation(NamedQuery[] namedQueries, FacetHolder holder) {
        super(holder);
        add(namedQueries);
    }
}
