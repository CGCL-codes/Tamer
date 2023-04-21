package de.fzi.mapaco;

import de.fzi.mapaco.lattice.Vertex;

/**
 * @author bock
 *
 */
public interface Selector {

    public Vertex select(Vertex v, VertexSet availVs, PheromoneMatrix pm);
}
