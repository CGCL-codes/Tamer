package jopt.js.spi.search;

import jopt.csp.spi.search.SearchTechniquesImpl;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.js.api.search.JsSearchTechniques;

public class JsSearchTechniquesImpl extends SearchTechniquesImpl implements JsSearchTechniques {

    public JsSearchTechniquesImpl(ConstraintStore store) {
        super(store);
    }
}
