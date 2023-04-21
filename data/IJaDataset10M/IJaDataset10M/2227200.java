package pl.org.minions.stigma.pathfinding.client;

import java.awt.geom.Point2D;

/**
 * Simplest algorithm to calculate heuristics - calculates
 * Euclidean distance between point and destination.
 */
public class SimpleHeuristic implements AStarHeuristic {

    /** {@inheritDoc} */
    @Override
    public float getHeuristicCost(int px, int py, int tx, int ty) {
        return (float) Point2D.distance(px, py, tx, ty);
    }
}
