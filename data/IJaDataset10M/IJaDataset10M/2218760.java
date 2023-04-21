package de.lamasep.map.graph;

import de.lamasep.map.Node;
import de.lamasep.map.StreetMap;

/**
 *
 * Dynamischer Graph
 * 
 * Knoten expandieren sich erst, wenn die Kanten angefordert werden und
 * die verwendeteten Policies dies zulassen.
 * 
 * @author Andreas Brandl <mail@andreas-brandl.de>
 * @author Johanna Böhm <mail@johanna-boehm.de>
 */
class DynamicStreetGraph implements Graph {

    /**
     * Straßenkarte
     */
    private final StreetMap map;

    /**
     * Dynamische Policy
     */
    private final DynamicPolicy policy;

    /**
     * Konstruktor.
     * @param map       Karte <code>map != null</code>
     * @param policy    Policy <code>policy != null</code>
     * @throws IllegalArgumentException falls 
     *          <code>map == null || policy == null</code>
     */
    public DynamicStreetGraph(StreetMap map, DynamicPolicy policy) {
        if (map == null) {
            throw new IllegalArgumentException("map == null");
        }
        if (policy == null) {
            throw new IllegalArgumentException("policy == null");
        }
        this.map = map;
        this.policy = policy;
    }

    /**
     * Gibt die GraphNode für eine Node zurück.
     * @param node  Node <code>node != null</code>
     * @return GraphNode
     */
    @Override
    public GraphNode get(Node node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        return new DynamicStreetGraphNode(map, policy, node, null);
    }
}
