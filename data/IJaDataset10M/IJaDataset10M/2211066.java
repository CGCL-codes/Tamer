package com.google.gwt.dev.jjs.impl.gflow;

/**
 * A flow function receives node assumptions and transforms them according to
 * node semantics. Typical flow functions update either outgoing assumptions 
 * (forward flow)   or incoming assumptions (backward flow) but not both.
 *  
 * @param <N> graph node type.
 * @param <E> edge type.
 * @param <G> graph type.
 * @param <A> analysis assumption type.
 */
public interface FlowFunction<N, E, G extends Graph<N, E, ?>, A extends Assumption<A>> {

    /**
   * Interpret node by computing new node assumptions from current ones. 
   */
    void interpret(N node, G g, AssumptionMap<E, A> assumptionMAp);
}
