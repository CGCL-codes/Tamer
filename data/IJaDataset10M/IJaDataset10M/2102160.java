package com.hp.hpl.jena.graph.compose;

import com.hp.hpl.jena.graph.*;

/**
    Base class for the two-operand composition operations; has two graphs L and R
    @author kers
    @author Ian Dickinson - refactored most of the content to {@link CompositionBase}.
*/
public abstract class Dyadic extends CompositionBase {

    protected Graph L;

    protected Graph R;

    /**
        When the graph is constructed, copy the prefix mappings of both components
        into this prefix mapping. The prefix mapping doesn't change afterwards with the
        components, which might be regarded as a bug.
    */
    public Dyadic(Graph L, Graph R) {
        this.L = L;
        this.R = R;
        getPrefixMapping().setNsPrefixes(L.getPrefixMapping()).setNsPrefixes(R.getPrefixMapping());
    }

    public void close() {
        L.close();
        R.close();
    }

    /**
        Generic dependsOn, true iff it depends on either of the subgraphs.
    */
    public boolean dependsOn(Graph other) {
        return other == this || L.dependsOn(other) || R.dependsOn(other);
    }

    public Union union(Graph X) {
        return new Union(this, X);
    }

    /**
         Answer the left (first) operand of this Dyadic.
    */
    public Object getL() {
        return L;
    }

    /**
         Answer the right (second) operand of this Dyadic.
    */
    public Object getR() {
        return R;
    }
}
