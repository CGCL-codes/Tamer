package mrusanov.fantasyruler.map.pathfinding;

public interface AStarHeuristic {

    float getCost(int x, int y, int tx, int ty);
}
