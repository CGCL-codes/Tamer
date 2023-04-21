package ru.cos.sim.ras.duo;

import ru.cos.sim.ras.duo.digraph.Digraph;
import ru.cos.sim.ras.duo.digraph.Edge;
import ru.cos.sim.ras.duo.digraph.Vertex;

public interface PathFinder {

    public Iterable<Edge> findPath(Edge start, Vertex end);

    public static interface Factory {

        public PathFinder createPathFinder(Digraph graph);
    }
}
