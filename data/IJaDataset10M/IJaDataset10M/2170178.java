package ru.cos.sim.ras.duo.algo.pathfinding;

import java.util.Iterator;
import ru.cos.sim.ras.duo.digraph.Vertex;
import ru.cos.sim.ras.duo.dijkstra.DijkstraProcessor;
import ru.cos.sim.ras.duo.dijkstra.DijkstraProcessor.PathSelector;
import ru.cos.sim.ras.duo.dijkstra.DijkstraVertexInfo.Backtrack;
import ru.cos.sim.ras.duo.utils.SortedList;

public class BestOnePathSelector implements PathSelector {

    @Override
    public Backtrack select(SortedList<Backtrack> variants) {
        Iterator<Backtrack> iter = variants.iterator();
        return iter.hasNext() ? iter.next() : null;
    }

    public static class Factory implements PathSelector.Factory {

        @Override
        public PathSelector createPathSelector(DijkstraProcessor processor, Vertex start, Vertex end) {
            return new BestOnePathSelector();
        }
    }
}
