package net.sourceforge.javabits.graph;

import java.util.Map;
import java.util.Set;
import net.sourceforge.javabits.lang.Pair;

public interface Graph<N, E> {

    /**
     * Edge direction from the node's perspective.
     * <p>
     * Edges are either incoming or outgoing.
     */
    public static enum Direction {

        IN(End.TARGET), OUT(End.SOURCE);

        private final End end;

        private Direction(final End end) {
            this.end = end;
        }

        public Direction getOther() {
            return Direction.values()[this.ordinal() ^ 1];
        }

        public End getConnectionEnd() {
            return this.end;
        }
    }

    /**
     * Node role from the edge's perspective.
     * <p>
     * Nodes are either source or target nodes from the edge's perspective.
     */
    public static enum End {

        SOURCE(Direction.OUT), TARGET(Direction.IN);

        private final Direction connector;

        private End(final Direction connector) {
            this.connector = connector;
        }

        public End getOther() {
            return End.values()[this.ordinal() ^ 1];
        }

        public Direction getConnectionDirection() {
            return connector;
        }
    }

    public boolean addNode(N node);

    public boolean addEdge(N source, N target, E edge);

    public boolean removeEdge(E edge);

    public Set<N> getNodeSet();

    public Set<E> getEdgeSet();

    public Set<E> getEdgeSet(N source, N target);

    public Map<E, Pair<N>> getEdgeMap();

    public Map<E, N> getEdgeMap(final N node, Direction direction);

    public Map<N, Set<E>> getEdgeMap(Direction direction, N node);

    public Set<N> getAdjacentNodeSet(N node, Direction direction);

    public void clear();
}
