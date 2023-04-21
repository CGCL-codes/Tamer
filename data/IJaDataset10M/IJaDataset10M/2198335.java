package de.tuberlin.cs.ni.neubauer.mpcd.edges;

public class MurataListEdgeContainerFactory implements EdgeContainerFactory {

    @Override
    public EdgeContainer getEdgeContainer(Boolean weightedDU) {
        return new MurataListEdgeContainer(weightedDU);
    }
}
