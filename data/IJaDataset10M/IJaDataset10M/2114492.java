package de.tuberlin.cs.ni.neubauer.mpcd.edges;

/**
 * represents choice of the concrete EdgeContainer implementation
 * @author neubauer
 *
 */
public interface EdgeContainerFactory {

    public EdgeContainer getEdgeContainer(Boolean unweighted);
}
